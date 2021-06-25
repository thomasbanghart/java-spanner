/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.connection;

import com.google.api.core.InternalApi;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ErrorCode;
import com.google.cloud.spanner.SessionPoolOptions;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerExceptionFactory;
import com.google.cloud.spanner.SpannerOptions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.spanner.v1.ExecuteSqlRequest.QueryOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * Internal connection API for Google Cloud Spanner. This class may introduce breaking changes
 * without prior notice.
 *
 * <p>Options for creating a {@link Connection} to a Google Cloud Spanner database.
 *
 * <p>Usage:
 *
 * <pre>
 * <!--SNIPPET {@link ConnectionOptions} usage-->
 * {@code
 * ConnectionOptions options = ConnectionOptions.newBuilder()
 *       .setUri("cloudspanner:/projects/my_project_id/instances/my_instance_id/databases/my_database_name?autocommit=false")
 *       .setCredentialsUrl("/home/cloudspanner-keys/my-key.json")
 *       .build();
 * try(Connection connection = options.getConnection()) {
 *   try(ResultSet rs = connection.executeQuery(Statement.of("SELECT SingerId, AlbumId, MarketingBudget FROM Albums"))) {
 *     while(rs.next()) {
 *       // do something
 *     }
 *   }
 * }
 * }
 * <!--SNIPPET {@link ConnectionOptions} usage-->
 * </pre>
 */
@InternalApi
public class ConnectionOptions {
  /** Supported connection properties that can be included in the connection URI. */
  public static class ConnectionProperty {
    private static final String[] BOOLEAN_VALUES = new String[] {"true", "false"};
    private final String name;
    private final String description;
    private final String defaultValue;
    private final String[] validValues;
    private final int hashCode;

    private static ConnectionProperty createStringProperty(String name, String description) {
      return new ConnectionProperty(name, description, "", null);
    }

    private static ConnectionProperty createBooleanProperty(
        String name, String description, boolean defaultValue) {
      return new ConnectionProperty(
          name, description, String.valueOf(defaultValue), BOOLEAN_VALUES);
    }

    private static ConnectionProperty createEmptyProperty(String name) {
      return new ConnectionProperty(name, "", "", null);
    }

    private ConnectionProperty(
        String name, String description, String defaultValue, String[] validValues) {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(description);
      Preconditions.checkNotNull(defaultValue);
      this.name = name;
      this.description = description;
      this.defaultValue = defaultValue;
      this.validValues = validValues;
      this.hashCode = name.toLowerCase().hashCode();
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ConnectionProperty)) {
        return false;
      }
      return ((ConnectionProperty) o).name.equalsIgnoreCase(this.name);
    }

    /** @return the name of this connection property. */
    public String getName() {
      return name;
    }

    /** @return the description of this connection property. */
    public String getDescription() {
      return description;
    }

    /** @return the default value of this connection property. */
    public String getDefaultValue() {
      return defaultValue;
    }

    /**
     * @return the valid values for this connection property. <code>null</code> indicates no
     *     restriction.
     */
    public String[] getValidValues() {
      return validValues;
    }
  }

  private static final LocalConnectionChecker LOCAL_CONNECTION_CHECKER =
      new LocalConnectionChecker();
  private static final boolean DEFAULT_USE_PLAIN_TEXT = false;
  static final boolean DEFAULT_AUTOCOMMIT = true;
  static final boolean DEFAULT_READONLY = false;
  static final boolean DEFAULT_RETRY_ABORTS_INTERNALLY = true;
  private static final String DEFAULT_CREDENTIALS = null;
  private static final String DEFAULT_OAUTH_TOKEN = null;
  private static final String DEFAULT_MIN_SESSIONS = null;
  private static final String DEFAULT_MAX_SESSIONS = null;
  private static final String DEFAULT_NUM_CHANNELS = null;
  private static final String DEFAULT_USER_AGENT = null;
  private static final String DEFAULT_OPTIMIZER_VERSION = "";
  private static final String DEFAULT_OPTIMIZER_STATISTICS_PACKAGE = "";
  private static final boolean DEFAULT_RETURN_COMMIT_STATS = false;
  private static final boolean DEFAULT_LENIENT = false;

  private static final String PLAIN_TEXT_PROTOCOL = "http:";
  private static final String HOST_PROTOCOL = "https:";
  private static final String DEFAULT_HOST = "https://spanner.googleapis.com";
  private static final String DEFAULT_EMULATOR_HOST = "http://localhost:9010";
  /** Use plain text is only for local testing purposes. */
  private static final String USE_PLAIN_TEXT_PROPERTY_NAME = "usePlainText";
  /** Name of the 'autocommit' connection property. */
  public static final String AUTOCOMMIT_PROPERTY_NAME = "autocommit";
  /** Name of the 'readonly' connection property. */
  public static final String READONLY_PROPERTY_NAME = "readonly";
  /** Name of the 'retry aborts internally' connection property. */
  public static final String RETRY_ABORTS_INTERNALLY_PROPERTY_NAME = "retryAbortsInternally";
  /** Name of the 'credentials' connection property. */
  public static final String CREDENTIALS_PROPERTY_NAME = "credentials";
  /** Name of the 'encodedCredentials' connection property. */
  public static final String ENCODED_CREDENTIALS_PROPERTY_NAME = "encodedCredentials";
  /**
   * OAuth token to use for authentication. Cannot be used in combination with a credentials file.
   */
  public static final String OAUTH_TOKEN_PROPERTY_NAME = "oauthToken";
  /** Name of the 'minSessions' connection property. */
  public static final String MIN_SESSIONS_PROPERTY_NAME = "minSessions";
  /** Name of the 'numChannels' connection property. */
  public static final String MAX_SESSIONS_PROPERTY_NAME = "maxSessions";
  /** Name of the 'numChannels' connection property. */
  public static final String NUM_CHANNELS_PROPERTY_NAME = "numChannels";
  /** Custom user agent string is only for other Google libraries. */
  private static final String USER_AGENT_PROPERTY_NAME = "userAgent";
  /** Query optimizer version to use for a connection. */
  private static final String OPTIMIZER_VERSION_PROPERTY_NAME = "optimizerVersion";
  /** Query optimizer statistics package to use for a connection. */
  private static final String OPTIMIZER_STATISTICS_PACKAGE_PROPERTY_NAME =
      "optimizerStatisticsPackage";
  /** Name of the 'lenientMode' connection property. */
  public static final String LENIENT_PROPERTY_NAME = "lenient";

  /** All valid connection properties. */
  public static final Set<ConnectionProperty> VALID_PROPERTIES =
      Collections.unmodifiableSet(
          new HashSet<>(
              Arrays.asList(
                  ConnectionProperty.createBooleanProperty(
                      AUTOCOMMIT_PROPERTY_NAME,
                      "Should the connection start in autocommit (true/false)",
                      DEFAULT_AUTOCOMMIT),
                  ConnectionProperty.createBooleanProperty(
                      READONLY_PROPERTY_NAME,
                      "Should the connection start in read-only mode (true/false)",
                      DEFAULT_READONLY),
                  ConnectionProperty.createBooleanProperty(
                      RETRY_ABORTS_INTERNALLY_PROPERTY_NAME,
                      "Should the connection automatically retry Aborted errors (true/false)",
                      DEFAULT_RETRY_ABORTS_INTERNALLY),
                  ConnectionProperty.createStringProperty(
                      CREDENTIALS_PROPERTY_NAME,
                      "The location of the credentials file to use for this connection. If neither this property or encoded credentials are set, the connection will use the default Google Cloud credentials for the runtime environment."),
                  ConnectionProperty.createStringProperty(
                      ENCODED_CREDENTIALS_PROPERTY_NAME,
                      "Base64-encoded credentials to use for this connection. If neither this property or a credentials location are set, the connection will use the default Google Cloud credentials for the runtime environment."),
                  ConnectionProperty.createStringProperty(
                      OAUTH_TOKEN_PROPERTY_NAME,
                      "A valid pre-existing OAuth token to use for authentication for this connection. Setting this property will take precedence over any value set for a credentials file."),
                  ConnectionProperty.createStringProperty(
                      MIN_SESSIONS_PROPERTY_NAME,
                      "The minimum number of sessions in the backing session pool. The default is 100."),
                  ConnectionProperty.createStringProperty(
                      MAX_SESSIONS_PROPERTY_NAME,
                      "The maximum number of sessions in the backing session pool. The default is 400."),
                  ConnectionProperty.createStringProperty(
                      NUM_CHANNELS_PROPERTY_NAME,
                      "The number of gRPC channels to use to communicate with Cloud Spanner. The default is 4."),
                  ConnectionProperty.createBooleanProperty(
                      USE_PLAIN_TEXT_PROPERTY_NAME,
                      "Use a plain text communication channel (i.e. non-TLS) for communicating with the server (true/false). Set this value to true for communication with the Cloud Spanner emulator.",
                      DEFAULT_USE_PLAIN_TEXT),
                  ConnectionProperty.createStringProperty(
                      USER_AGENT_PROPERTY_NAME,
                      "The custom user-agent property name to use when communicating with Cloud Spanner. This property is intended for internal library usage, and should not be set by applications."),
                  ConnectionProperty.createStringProperty(
                      OPTIMIZER_VERSION_PROPERTY_NAME,
                      "Sets the default query optimizer version to use for this connection."),
                  ConnectionProperty.createStringProperty(
                      OPTIMIZER_STATISTICS_PACKAGE_PROPERTY_NAME, ""),
                  ConnectionProperty.createBooleanProperty("returnCommitStats", "", false),
                  ConnectionProperty.createBooleanProperty(
                      "autoConfigEmulator",
                      "Automatically configure the connection to try to connect to the Cloud Spanner emulator (true/false). The instance and database in the connection string will automatically be created if these do not yet exist on the emulator.",
                      false),
                  ConnectionProperty.createBooleanProperty(
                      LENIENT_PROPERTY_NAME,
                      "Silently ignore unknown properties in the connection string/properties (true/false)",
                      DEFAULT_LENIENT))));

  private static final Set<ConnectionProperty> INTERNAL_PROPERTIES =
      Collections.unmodifiableSet(
          new HashSet<>(
              Collections.singletonList(
                  ConnectionProperty.createStringProperty(USER_AGENT_PROPERTY_NAME, ""))));
  private static final Set<ConnectionProperty> INTERNAL_VALID_PROPERTIES =
      Sets.union(VALID_PROPERTIES, INTERNAL_PROPERTIES);

  /**
   * Gets the default project-id for the current environment as defined by {@link
   * ServiceOptions#getDefaultProjectId()}, and if none could be found, the project-id of the given
   * credentials if it contains any.
   *
   * @param credentials The credentials to use to get the default project-id if none could be found
   *     in the environment.
   * @return the default project-id.
   */
  public static String getDefaultProjectId(Credentials credentials) {
    String projectId = SpannerOptions.getDefaultProjectId();
    if (projectId == null
        && credentials != null
        && credentials instanceof ServiceAccountCredentials) {
      projectId = ((ServiceAccountCredentials) credentials).getProjectId();
    }
    return projectId;
  }

  /**
   * Closes <strong>all</strong> {@link Spanner} instances that have been opened by connections
   * during the lifetime of this JVM. Call this method at the end of your application to free up
   * resources. You must close all {@link Connection}s that have been opened by your application
   * before calling this method. Failing to do so, will cause this method to throw a {@link
   * SpannerException}.
   *
   * <p>This method is also automatically called by a shutdown hook (see {@link
   * Runtime#addShutdownHook(Thread)}) when the JVM is shutdown gracefully.
   */
  public static void closeSpanner() {
    SpannerPool.INSTANCE.checkAndCloseSpanners();
  }

  /**
   * {@link SpannerOptionsConfigurator} can be used to add additional configuration for a {@link
   * Spanner} instance. Intended for tests.
   */
  @VisibleForTesting
  interface SpannerOptionsConfigurator {
    void configure(SpannerOptions.Builder options);
  }

  /** Builder for {@link ConnectionOptions} instances. */
  public static class Builder {
    private String uri;
    private String credentialsUrl;
    private String oauthToken;
    private Credentials credentials;
    private SessionPoolOptions sessionPoolOptions;
    private List<StatementExecutionInterceptor> statementExecutionInterceptors =
        Collections.emptyList();
    private SpannerOptionsConfigurator configurator;

    private Builder() {}

    /** Spanner {@link ConnectionOptions} URI format. */
    public static final String SPANNER_URI_FORMAT =
        "(?:cloudspanner:)(?<HOSTGROUP>//[\\w.-]+(?:\\.[\\w\\.-]+)*[\\w\\-\\._~:/?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=.]+)?/projects/(?<PROJECTGROUP>(([a-z]|[-.:]|[0-9])+|(DEFAULT_PROJECT_ID)))(/instances/(?<INSTANCEGROUP>([a-z]|[-]|[0-9])+)(/databases/(?<DATABASEGROUP>([a-z]|[-]|[_]|[0-9])+))?)?(?:[?|;].*)?";

    private static final String SPANNER_URI_REGEX = "(?is)^" + SPANNER_URI_FORMAT + "$";
    private static final Pattern SPANNER_URI_PATTERN = Pattern.compile(SPANNER_URI_REGEX);
    private static final String HOST_GROUP = "HOSTGROUP";
    private static final String PROJECT_GROUP = "PROJECTGROUP";
    private static final String INSTANCE_GROUP = "INSTANCEGROUP";
    private static final String DATABASE_GROUP = "DATABASEGROUP";
    private static final String DEFAULT_PROJECT_ID_PLACEHOLDER = "DEFAULT_PROJECT_ID";

    private boolean isValidUri(String uri) {
      return SPANNER_URI_PATTERN.matcher(uri).matches();
    }

    /**
     * Sets the URI of the Cloud Spanner database to connect to. A connection URI must be specified
     * in this format:
     *
     * <pre>
     * cloudspanner:[//host[:port]]/projects/project-id[/instances/instance-id[/databases/database-name]][\?property-name=property-value[;property-name=property-value]*]?
     * </pre>
     *
     * The property-value strings should be url-encoded.
     *
     * <p>The project-id part of the URI may be filled with the placeholder DEFAULT_PROJECT_ID. This
     * placeholder will be replaced by the default project id of the environment that is requesting
     * a connection.
     *
     * <p>The supported properties are:
     *
     * <ul>
     *   <li>credentials (String): URL for the credentials file to use for the connection. This
     *       property is only used if no credentials have been specified using the {@link
     *       ConnectionOptions.Builder#setCredentialsUrl(String)} method. If you do not specify any
     *       credentials at all, the default credentials of the environment as returned by {@link
     *       GoogleCredentials#getApplicationDefault()} will be used.
     *   <li>encodedCredentials (String): A Base64 encoded string containing the Google credentials
     *       to use. You should only set either this property or the `credentials` (file location)
     *       property, but not both at the same time.
     *   <li>autocommit (boolean): Sets the initial autocommit mode for the connection. Default is
     *       true.
     *   <li>readonly (boolean): Sets the initial readonly mode for the connection. Default is
     *       false.
     *   <li>minSessions (int): Sets the minimum number of sessions in the backing session pool.
     *   <li>maxSessions (int): Sets the maximum number of sessions in the backing session pool.
     *   <li>numChannels (int): Sets the number of gRPC channels to use for the connection.
     *   <li>retryAbortsInternally (boolean): Sets the initial retryAbortsInternally mode for the
     *       connection. Default is true.
     *   <li>optimizerVersion (string): Sets the query optimizer version to use for the connection.
     *   <li>autoConfigEmulator (boolean): Automatically configures the connection to connect to the
     *       Cloud Spanner emulator. If no host and port is specified in the connection string, the
     *       connection will automatically use the default emulator host/port combination
     *       (localhost:9010). Plain text communication will be enabled and authentication will be
     *       disabled. The instance and database in the connection string will automatically be
     *       created on the emulator if any of them do not yet exist. Any existing instance or
     *       database on the emulator will remain untouched. No other configuration is needed in
     *       order to connect to the emulator than setting this property.
     * </ul>
     *
     * @param uri The URI of the Spanner database to connect to.
     * @return this builder
     */
    public Builder setUri(String uri) {
      Preconditions.checkArgument(
          isValidUri(uri),
          "The specified URI is not a valid Cloud Spanner connection URI. Please specify a URI in the format \"cloudspanner:[//host[:port]]/projects/project-id[/instances/instance-id[/databases/database-name]][\\?property-name=property-value[;property-name=property-value]*]?\"");
      checkValidProperties(uri);
      this.uri = uri;
      return this;
    }

    /** Sets the {@link SessionPoolOptions} to use for the connection. */
    public Builder setSessionPoolOptions(SessionPoolOptions sessionPoolOptions) {
      Preconditions.checkNotNull(sessionPoolOptions);
      this.sessionPoolOptions = sessionPoolOptions;
      return this;
    }

    /**
     * Sets the URL of the credentials file to use for this connection. The URL may be a reference
     * to a file on the local file system, or to a file on Google Cloud Storage. References to
     * Google Cloud Storage files are only allowed when the application is running on Google Cloud
     * and the environment has access to the specified storage location. It also requires that the
     * Google Cloud Storage client library is present on the class path. The Google Cloud Storage
     * library is not automatically added as a dependency by the JDBC driver.
     *
     * <p>If you do not specify a credentialsUrl (either by using this setter, or by specifying on
     * the connection URI), the credentials returned by {@link
     * GoogleCredentials#getApplicationDefault()} will be used for the connection.
     *
     * @param credentialsUrl A valid file or Google Cloud Storage URL for the credentials file to be
     *     used.
     * @return this builder
     */
    public Builder setCredentialsUrl(String credentialsUrl) {
      this.credentialsUrl = credentialsUrl;
      return this;
    }

    /**
     * Sets the OAuth token to use with this connection. The token must be a valid token with access
     * to the resources (project/instance/database) that the connection will be accessing. This
     * authentication method cannot be used in combination with a credentials file. If both an OAuth
     * token and a credentials file is specified, the {@link #build()} method will throw an
     * exception.
     *
     * @param oauthToken A valid OAuth token for the Google Cloud project that is used by this
     *     connection.
     * @return this builder
     */
    public Builder setOAuthToken(String oauthToken) {
      this.oauthToken = oauthToken;
      return this;
    }

    @VisibleForTesting
    Builder setStatementExecutionInterceptors(List<StatementExecutionInterceptor> interceptors) {
      this.statementExecutionInterceptors = interceptors;
      return this;
    }

    @VisibleForTesting
    Builder setConfigurator(SpannerOptionsConfigurator configurator) {
      this.configurator = Preconditions.checkNotNull(configurator);
      return this;
    }

    public Builder setCredentials(Credentials credentials) {
      this.credentials = credentials;
      return this;
    }

    /** @return the {@link ConnectionOptions} */
    public ConnectionOptions build() {
      Preconditions.checkState(this.uri != null, "Connection URI is required");
      return new ConnectionOptions(this);
    }
  }

  /**
   * Create a {@link Builder} for {@link ConnectionOptions}. Use this method to create {@link
   * ConnectionOptions} that can be used to obtain a {@link Connection}.
   *
   * @return a new {@link Builder}
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  private final String uri;
  private final String warnings;
  private final String credentialsUrl;
  private final String encodedCredentials;
  private final String oauthToken;
  private final Credentials fixedCredentials;

  private final boolean usePlainText;
  private final String host;
  private final String projectId;
  private final String instanceId;
  private final String databaseName;
  private final Credentials credentials;
  private final SessionPoolOptions sessionPoolOptions;
  private final Integer numChannels;
  private final Integer minSessions;
  private final Integer maxSessions;
  private final String userAgent;
  private final QueryOptions queryOptions;
  private final boolean returnCommitStats;
  private final boolean autoConfigEmulator;

  private final boolean autocommit;
  private final boolean readOnly;
  private final boolean retryAbortsInternally;
  private final List<StatementExecutionInterceptor> statementExecutionInterceptors;
  private final SpannerOptionsConfigurator configurator;

  private ConnectionOptions(Builder builder) {
    Matcher matcher = Builder.SPANNER_URI_PATTERN.matcher(builder.uri);
    Preconditions.checkArgument(
        matcher.find(), String.format("Invalid connection URI specified: %s", builder.uri));
    this.warnings = checkValidProperties(builder.uri);

    this.uri = builder.uri;
    this.credentialsUrl =
        builder.credentialsUrl != null ? builder.credentialsUrl : parseCredentials(builder.uri);
    this.encodedCredentials = parseEncodedCredentials(builder.uri);
    // Check that not both a credentials location and encoded credentials have been specified in the
    // connection URI.
    Preconditions.checkArgument(
        this.credentialsUrl == null || this.encodedCredentials == null,
        "Cannot specify both a credentials URL and encoded credentials. Only set one of the properties.");

    this.oauthToken =
        builder.oauthToken != null ? builder.oauthToken : parseOAuthToken(builder.uri);
    this.fixedCredentials = builder.credentials;
    // Check that not both credentials and an OAuth token have been specified.
    Preconditions.checkArgument(
        (builder.credentials == null
                && this.credentialsUrl == null
                && this.encodedCredentials == null)
            || this.oauthToken == null,
        "Cannot specify both credentials and an OAuth token.");

    this.userAgent = parseUserAgent(this.uri);
    QueryOptions.Builder queryOptionsBuilder = QueryOptions.newBuilder();
    queryOptionsBuilder.setOptimizerVersion(parseOptimizerVersion(this.uri));
    queryOptionsBuilder.setOptimizerStatisticsPackage(parseOptimizerStatisticsPackage(this.uri));
    this.queryOptions = queryOptionsBuilder.build();
    this.returnCommitStats = parseReturnCommitStats(this.uri);
    this.autoConfigEmulator = parseAutoConfigEmulator(this.uri);
    this.usePlainText = this.autoConfigEmulator || parseUsePlainText(this.uri);
    this.host = determineHost(matcher, autoConfigEmulator, usePlainText);

    this.instanceId = matcher.group(Builder.INSTANCE_GROUP);
    this.databaseName = matcher.group(Builder.DATABASE_GROUP);
    // Using credentials on a plain text connection is not allowed, so if the user has not specified
    // any credentials and is using a plain text connection, we should not try to get the
    // credentials from the environment, but default to NoCredentials.
    if (builder.credentials == null
        && this.credentialsUrl == null
        && this.encodedCredentials == null
        && this.oauthToken == null
        && this.usePlainText) {
      this.credentials = NoCredentials.getInstance();
    } else if (this.oauthToken != null) {
      this.credentials = new GoogleCredentials(new AccessToken(oauthToken, null));
    } else if (this.fixedCredentials != null) {
      this.credentials = fixedCredentials;
    } else if (this.encodedCredentials != null) {
      this.credentials = getCredentialsService().decodeCredentials(this.encodedCredentials);
    } else {
      this.credentials = getCredentialsService().createCredentials(this.credentialsUrl);
    }
    this.minSessions =
        parseIntegerProperty(MIN_SESSIONS_PROPERTY_NAME, parseMinSessions(builder.uri));
    this.maxSessions =
        parseIntegerProperty(MAX_SESSIONS_PROPERTY_NAME, parseMaxSessions(builder.uri));
    this.numChannels =
        parseIntegerProperty(NUM_CHANNELS_PROPERTY_NAME, parseNumChannels(builder.uri));

    String projectId = matcher.group(Builder.PROJECT_GROUP);
    if (Builder.DEFAULT_PROJECT_ID_PLACEHOLDER.equalsIgnoreCase(projectId)) {
      projectId = getDefaultProjectId(this.credentials);
    }
    this.projectId = projectId;

    this.autocommit = parseAutocommit(this.uri);
    this.readOnly = parseReadOnly(this.uri);
    this.retryAbortsInternally = parseRetryAbortsInternally(this.uri);
    this.statementExecutionInterceptors =
        Collections.unmodifiableList(builder.statementExecutionInterceptors);
    this.configurator = builder.configurator;

    if (this.minSessions != null || this.maxSessions != null) {
      SessionPoolOptions.Builder sessionPoolOptionsBuilder =
          builder.sessionPoolOptions == null
              ? SessionPoolOptions.newBuilder()
              : builder.sessionPoolOptions.toBuilder();
      if (this.minSessions != null) {
        sessionPoolOptionsBuilder.setMinSessions(this.minSessions);
      }
      if (this.maxSessions != null) {
        sessionPoolOptionsBuilder.setMaxSessions(this.maxSessions);
      }
      this.sessionPoolOptions = sessionPoolOptionsBuilder.build();
    } else {
      this.sessionPoolOptions = builder.sessionPoolOptions;
    }
  }

  private static String determineHost(
      Matcher matcher, boolean autoConfigEmulator, boolean usePlainText) {
    if (matcher.group(Builder.HOST_GROUP) == null) {
      if (autoConfigEmulator) {
        return DEFAULT_EMULATOR_HOST;
      } else {
        return DEFAULT_HOST;
      }
    } else {
      if (usePlainText) {
        return PLAIN_TEXT_PROTOCOL + matcher.group(Builder.HOST_GROUP);
      } else {
        return HOST_PROTOCOL + matcher.group(Builder.HOST_GROUP);
      }
    }
  }

  private static Integer parseIntegerProperty(String propertyName, String value) {
    if (value != null) {
      try {
        return Integer.valueOf(value);
      } catch (NumberFormatException e) {
        throw SpannerExceptionFactory.newSpannerException(
            ErrorCode.INVALID_ARGUMENT,
            String.format("Invalid %s value specified: %s", propertyName, value),
            e);
      }
    }
    return null;
  }

  SpannerOptionsConfigurator getConfigurator() {
    return configurator;
  }

  @VisibleForTesting
  CredentialsService getCredentialsService() {
    return CredentialsService.INSTANCE;
  }

  @VisibleForTesting
  static boolean parseUsePlainText(String uri) {
    String value = parseUriProperty(uri, USE_PLAIN_TEXT_PROPERTY_NAME);
    return value != null ? Boolean.parseBoolean(value) : DEFAULT_USE_PLAIN_TEXT;
  }

  @VisibleForTesting
  static boolean parseAutocommit(String uri) {
    String value = parseUriProperty(uri, AUTOCOMMIT_PROPERTY_NAME);
    return value != null ? Boolean.parseBoolean(value) : DEFAULT_AUTOCOMMIT;
  }

  @VisibleForTesting
  static boolean parseReadOnly(String uri) {
    String value = parseUriProperty(uri, READONLY_PROPERTY_NAME);
    return value != null ? Boolean.parseBoolean(value) : DEFAULT_READONLY;
  }

  @VisibleForTesting
  static boolean parseRetryAbortsInternally(String uri) {
    String value = parseUriProperty(uri, RETRY_ABORTS_INTERNALLY_PROPERTY_NAME);
    return value != null ? Boolean.parseBoolean(value) : DEFAULT_RETRY_ABORTS_INTERNALLY;
  }

  @VisibleForTesting
  static String parseCredentials(String uri) {
    String value = parseUriProperty(uri, CREDENTIALS_PROPERTY_NAME);
    return value != null ? value : DEFAULT_CREDENTIALS;
  }

  @VisibleForTesting
  static String parseEncodedCredentials(String uri) {
    return parseUriProperty(uri, ENCODED_CREDENTIALS_PROPERTY_NAME);
  }

  @VisibleForTesting
  static String parseOAuthToken(String uri) {
    String value = parseUriProperty(uri, OAUTH_TOKEN_PROPERTY_NAME);
    return value != null ? value : DEFAULT_OAUTH_TOKEN;
  }

  @VisibleForTesting
  static String parseMinSessions(String uri) {
    String value = parseUriProperty(uri, MIN_SESSIONS_PROPERTY_NAME);
    return value != null ? value : DEFAULT_MIN_SESSIONS;
  }

  @VisibleForTesting
  static String parseMaxSessions(String uri) {
    String value = parseUriProperty(uri, MAX_SESSIONS_PROPERTY_NAME);
    return value != null ? value : DEFAULT_MAX_SESSIONS;
  }

  @VisibleForTesting
  static String parseNumChannels(String uri) {
    String value = parseUriProperty(uri, NUM_CHANNELS_PROPERTY_NAME);
    return value != null ? value : DEFAULT_NUM_CHANNELS;
  }

  @VisibleForTesting
  static String parseUserAgent(String uri) {
    String value = parseUriProperty(uri, USER_AGENT_PROPERTY_NAME);
    return value != null ? value : DEFAULT_USER_AGENT;
  }

  @VisibleForTesting
  static String parseOptimizerVersion(String uri) {
    String value = parseUriProperty(uri, OPTIMIZER_VERSION_PROPERTY_NAME);
    return value != null ? value : DEFAULT_OPTIMIZER_VERSION;
  }

  @VisibleForTesting
  static String parseOptimizerStatisticsPackage(String uri) {
    String value = parseUriProperty(uri, OPTIMIZER_STATISTICS_PACKAGE_PROPERTY_NAME);
    return value != null ? value : DEFAULT_OPTIMIZER_STATISTICS_PACKAGE;
  }

  @VisibleForTesting
  static boolean parseReturnCommitStats(String uri) {
    String value = parseUriProperty(uri, "returnCommitStats");
    return Boolean.parseBoolean(value);
  }

  static boolean parseAutoConfigEmulator(String uri) {
    String value = parseUriProperty(uri, "autoConfigEmulator");
    return Boolean.parseBoolean(value);
  }

  @VisibleForTesting
  static boolean parseLenient(String uri) {
    String value = parseUriProperty(uri, LENIENT_PROPERTY_NAME);
    return value != null ? Boolean.parseBoolean(value) : DEFAULT_LENIENT;
  }

  @VisibleForTesting
  static String parseUriProperty(String uri, String property) {
    Pattern pattern = Pattern.compile(String.format("(?is)(?:;|\\?)%s=(.*?)(?:;|$)", property));
    Matcher matcher = pattern.matcher(uri);
    if (matcher.find() && matcher.groupCount() == 1) {
      return matcher.group(1);
    }
    return null;
  }

  /** Check that only valid properties have been specified. */
  @VisibleForTesting
  static String checkValidProperties(String uri) {
    String invalidProperties = "";
    List<String> properties = parseProperties(uri);
    boolean lenient = parseLenient(uri);
    for (String property : properties) {
      if (!INTERNAL_VALID_PROPERTIES.contains(ConnectionProperty.createEmptyProperty(property))) {
        if (invalidProperties.length() > 0) {
          invalidProperties = invalidProperties + ", ";
        }
        invalidProperties = invalidProperties + property;
      }
    }
    if (lenient) {
      return String.format("Invalid properties found in connection URI: %s", invalidProperties);
    } else {
      Preconditions.checkArgument(
          invalidProperties.isEmpty(),
          String.format(
              "Invalid properties found in connection URI. Add lenient=true to the connection string to ignore unknown properties. Invalid properties: %s",
              invalidProperties));
      return null;
    }
  }

  @VisibleForTesting
  static List<String> parseProperties(String uri) {
    Pattern pattern = Pattern.compile("(?is)(?:\\?|;)(?<PROPERTY>.*?)=(?:.*?)");
    Matcher matcher = pattern.matcher(uri);
    List<String> res = new ArrayList<>();
    while (matcher.find() && matcher.group("PROPERTY") != null) {
      res.add(matcher.group("PROPERTY"));
    }
    return res;
  }

  /**
   * Create a new {@link Connection} from this {@link ConnectionOptions}. Calling this method
   * multiple times for the same {@link ConnectionOptions} will return multiple instances of {@link
   * Connection}s to the same database.
   *
   * @return a new {@link Connection} to the database referenced by this {@link ConnectionOptions}
   */
  public Connection getConnection() {
    LOCAL_CONNECTION_CHECKER.checkLocalConnection(this);
    return new ConnectionImpl(this);
  }

  /** The URI of this {@link ConnectionOptions} */
  public String getUri() {
    return uri;
  }

  /** The credentials URL of this {@link ConnectionOptions} */
  public String getCredentialsUrl() {
    return credentialsUrl;
  }

  String getOAuthToken() {
    return this.oauthToken;
  }

  Credentials getFixedCredentials() {
    return this.fixedCredentials;
  }

  /** The {@link SessionPoolOptions} of this {@link ConnectionOptions}. */
  public SessionPoolOptions getSessionPoolOptions() {
    return sessionPoolOptions;
  }

  /**
   * The minimum number of sessions in the backing session pool of this connection. The session pool
   * is shared between all connections in the same JVM that connect to the same Cloud Spanner
   * database using the same connection settings.
   */
  public Integer getMinSessions() {
    return minSessions;
  }

  /**
   * The maximum number of sessions in the backing session pool of this connection. The session pool
   * is shared between all connections in the same JVM that connect to the same Cloud Spanner
   * database using the same connection settings.
   */
  public Integer getMaxSessions() {
    return maxSessions;
  }

  /** The number of channels to use for the connection. */
  public Integer getNumChannels() {
    return numChannels;
  }

  /** The host and port number that this {@link ConnectionOptions} will connect to */
  public String getHost() {
    return host;
  }

  /** The Google Project ID that this {@link ConnectionOptions} will connect to */
  public String getProjectId() {
    return projectId;
  }

  /** The Spanner Instance ID that this {@link ConnectionOptions} will connect to */
  public String getInstanceId() {
    return instanceId;
  }

  /** The Spanner database name that this {@link ConnectionOptions} will connect to */
  public String getDatabaseName() {
    return databaseName;
  }

  /** The Spanner {@link DatabaseId} that this {@link ConnectionOptions} will connect to */
  public DatabaseId getDatabaseId() {
    Preconditions.checkState(projectId != null, "Project ID is not specified");
    Preconditions.checkState(instanceId != null, "Instance ID is not specified");
    Preconditions.checkState(databaseName != null, "Database name is not specified");
    return DatabaseId.of(projectId, instanceId, databaseName);
  }

  /**
   * The {@link Credentials} of this {@link ConnectionOptions}. This is either the credentials
   * specified in the credentialsUrl or the default Google application credentials
   */
  public Credentials getCredentials() {
    return credentials;
  }

  /** The initial autocommit value for connections created by this {@link ConnectionOptions} */
  public boolean isAutocommit() {
    return autocommit;
  }

  /** The initial readonly value for connections created by this {@link ConnectionOptions} */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * The initial retryAbortsInternally value for connections created by this {@link
   * ConnectionOptions}
   */
  public boolean isRetryAbortsInternally() {
    return retryAbortsInternally;
  }

  /** Any warnings that were generated while creating the {@link ConnectionOptions} instance. */
  @Nullable
  public String getWarnings() {
    return warnings;
  }

  /** Use http instead of https. Only valid for (local) test servers. */
  boolean isUsePlainText() {
    return usePlainText;
  }

  /**
   * The (custom) user agent string to use for this connection. If <code>null</code>, then the
   * default JDBC user agent string will be used.
   */
  String getUserAgent() {
    return userAgent;
  }

  /** The {@link QueryOptions} to use for the connection. */
  QueryOptions getQueryOptions() {
    return queryOptions;
  }

  /** Whether connections created by this {@link ConnectionOptions} return commit stats. */
  public boolean isReturnCommitStats() {
    return returnCommitStats;
  }

  /**
   * Whether connections created by this {@link ConnectionOptions} will automatically try to connect
   * to the emulator using the default host/port of the emulator, and automatically create the
   * instance and database that is specified in the connection string if these do not exist on the
   * emulator instance.
   */
  public boolean isAutoConfigEmulator() {
    return autoConfigEmulator;
  }

  /** Interceptors that should be executed after each statement */
  List<StatementExecutionInterceptor> getStatementExecutionInterceptors() {
    return statementExecutionInterceptors;
  }

  @Override
  public String toString() {
    return getUri();
  }
}
