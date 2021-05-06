/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/spanner/admin/instance/v1/spanner_instance_admin.proto

package com.google.spanner.admin.instance.v1;

public final class SpannerInstanceAdminProto {
  private SpannerInstanceAdminProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_ReplicaInfo_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_ReplicaInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_InstanceConfig_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_InstanceConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_Instance_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_Instance_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_Instance_LabelsEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_Instance_LabelsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_GetInstanceConfigRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_GetInstanceConfigRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_GetInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_GetInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_CreateInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_CreateInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_ListInstancesRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_ListInstancesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_ListInstancesResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_ListInstancesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_UpdateInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_UpdateInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_DeleteInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_DeleteInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_CreateInstanceMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_CreateInstanceMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_spanner_admin_instance_v1_UpdateInstanceMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_spanner_admin_instance_v1_UpdateInstanceMetadata_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n=google/spanner/admin/instance/v1/spann"
          + "er_instance_admin.proto\022 google.spanner."
          + "admin.instance.v1\032\034google/api/annotation"
          + "s.proto\032\027google/api/client.proto\032\037google"
          + "/api/field_behavior.proto\032\031google/api/re"
          + "source.proto\032\036google/iam/v1/iam_policy.p"
          + "roto\032\032google/iam/v1/policy.proto\032#google"
          + "/longrunning/operations.proto\032\033google/pr"
          + "otobuf/empty.proto\032 google/protobuf/fiel"
          + "d_mask.proto\032\037google/protobuf/timestamp."
          + "proto\"\332\001\n\013ReplicaInfo\022\020\n\010location\030\001 \001(\t\022"
          + "G\n\004type\030\002 \001(\01629.google.spanner.admin.ins"
          + "tance.v1.ReplicaInfo.ReplicaType\022\037\n\027defa"
          + "ult_leader_location\030\003 \001(\010\"O\n\013ReplicaType"
          + "\022\024\n\020TYPE_UNSPECIFIED\020\000\022\016\n\nREAD_WRITE\020\001\022\r"
          + "\n\tREAD_ONLY\020\002\022\013\n\007WITNESS\020\003\"\327\001\n\016InstanceC"
          + "onfig\022\014\n\004name\030\001 \001(\t\022\024\n\014display_name\030\002 \001("
          + "\t\022?\n\010replicas\030\003 \003(\0132-.google.spanner.adm"
          + "in.instance.v1.ReplicaInfo:`\352A]\n%spanner"
          + ".googleapis.com/InstanceConfig\0224projects"
          + "/{project}/instanceConfigs/{instance_con"
          + "fig}\"\332\003\n\010Instance\022\014\n\004name\030\001 \001(\t\022:\n\006confi"
          + "g\030\002 \001(\tB*\372A\'\n%spanner.googleapis.com/Ins"
          + "tanceConfig\022\024\n\014display_name\030\003 \001(\t\022\022\n\nnod"
          + "e_count\030\005 \001(\005\022D\n\005state\030\006 \001(\01620.google.sp"
          + "anner.admin.instance.v1.Instance.StateB\003"
          + "\340A\003\022F\n\006labels\030\007 \003(\01326.google.spanner.adm"
          + "in.instance.v1.Instance.LabelsEntry\022\025\n\re"
          + "ndpoint_uris\030\010 \003(\t\032-\n\013LabelsEntry\022\013\n\003key"
          + "\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:\0028\001\"7\n\005State\022\025\n\021ST"
          + "ATE_UNSPECIFIED\020\000\022\014\n\010CREATING\020\001\022\t\n\005READY"
          + "\020\002:M\352AJ\n\037spanner.googleapis.com/Instance"
          + "\022\'projects/{project}/instances/{instance"
          + "}\"\210\001\n\032ListInstanceConfigsRequest\022C\n\006pare"
          + "nt\030\001 \001(\tB3\340A\002\372A-\n+cloudresourcemanager.g"
          + "oogleapis.com/Project\022\021\n\tpage_size\030\002 \001(\005"
          + "\022\022\n\npage_token\030\003 \001(\t\"\202\001\n\033ListInstanceCon"
          + "figsResponse\022J\n\020instance_configs\030\001 \003(\01320"
          + ".google.spanner.admin.instance.v1.Instan"
          + "ceConfig\022\027\n\017next_page_token\030\002 \001(\t\"W\n\030Get"
          + "InstanceConfigRequest\022;\n\004name\030\001 \001(\tB-\340A\002"
          + "\372A\'\n%spanner.googleapis.com/InstanceConf"
          + "ig\"{\n\022GetInstanceRequest\0225\n\004name\030\001 \001(\tB\'"
          + "\340A\002\372A!\n\037spanner.googleapis.com/Instance\022"
          + ".\n\nfield_mask\030\002 \001(\0132\032.google.protobuf.Fi"
          + "eldMask\"\271\001\n\025CreateInstanceRequest\022C\n\006par"
          + "ent\030\001 \001(\tB3\340A\002\372A-\n+cloudresourcemanager."
          + "googleapis.com/Project\022\030\n\013instance_id\030\002 "
          + "\001(\tB\003\340A\002\022A\n\010instance\030\003 \001(\0132*.google.span"
          + "ner.admin.instance.v1.InstanceB\003\340A\002\"\222\001\n\024"
          + "ListInstancesRequest\022C\n\006parent\030\001 \001(\tB3\340A"
          + "\002\372A-\n+cloudresourcemanager.googleapis.co"
          + "m/Project\022\021\n\tpage_size\030\002 \001(\005\022\022\n\npage_tok"
          + "en\030\003 \001(\t\022\016\n\006filter\030\004 \001(\t\"o\n\025ListInstance"
          + "sResponse\022=\n\tinstances\030\001 \003(\0132*.google.sp"
          + "anner.admin.instance.v1.Instance\022\027\n\017next"
          + "_page_token\030\002 \001(\t\"\217\001\n\025UpdateInstanceRequ"
          + "est\022A\n\010instance\030\001 \001(\0132*.google.spanner.a"
          + "dmin.instance.v1.InstanceB\003\340A\002\0223\n\nfield_"
          + "mask\030\002 \001(\0132\032.google.protobuf.FieldMaskB\003"
          + "\340A\002\"N\n\025DeleteInstanceRequest\0225\n\004name\030\001 \001"
          + "(\tB\'\340A\002\372A!\n\037spanner.googleapis.com/Insta"
          + "nce\"\345\001\n\026CreateInstanceMetadata\022<\n\010instan"
          + "ce\030\001 \001(\0132*.google.spanner.admin.instance"
          + ".v1.Instance\022.\n\nstart_time\030\002 \001(\0132\032.googl"
          + "e.protobuf.Timestamp\022/\n\013cancel_time\030\003 \001("
          + "\0132\032.google.protobuf.Timestamp\022,\n\010end_tim"
          + "e\030\004 \001(\0132\032.google.protobuf.Timestamp\"\345\001\n\026"
          + "UpdateInstanceMetadata\022<\n\010instance\030\001 \001(\013"
          + "2*.google.spanner.admin.instance.v1.Inst"
          + "ance\022.\n\nstart_time\030\002 \001(\0132\032.google.protob"
          + "uf.Timestamp\022/\n\013cancel_time\030\003 \001(\0132\032.goog"
          + "le.protobuf.Timestamp\022,\n\010end_time\030\004 \001(\0132"
          + "\032.google.protobuf.Timestamp2\277\020\n\rInstance"
          + "Admin\022\314\001\n\023ListInstanceConfigs\022<.google.s"
          + "panner.admin.instance.v1.ListInstanceCon"
          + "figsRequest\032=.google.spanner.admin.insta"
          + "nce.v1.ListInstanceConfigsResponse\"8\202\323\344\223"
          + "\002)\022\'/v1/{parent=projects/*}/instanceConf"
          + "igs\332A\006parent\022\271\001\n\021GetInstanceConfig\022:.goo"
          + "gle.spanner.admin.instance.v1.GetInstanc"
          + "eConfigRequest\0320.google.spanner.admin.in"
          + "stance.v1.InstanceConfig\"6\202\323\344\223\002)\022\'/v1/{n"
          + "ame=projects/*/instanceConfigs/*}\332A\004name"
          + "\022\264\001\n\rListInstances\0226.google.spanner.admi"
          + "n.instance.v1.ListInstancesRequest\0327.goo"
          + "gle.spanner.admin.instance.v1.ListInstan"
          + "cesResponse\"2\202\323\344\223\002#\022!/v1/{parent=project"
          + "s/*}/instances\332A\006parent\022\241\001\n\013GetInstance\022"
          + "4.google.spanner.admin.instance.v1.GetIn"
          + "stanceRequest\032*.google.spanner.admin.ins"
          + "tance.v1.Instance\"0\202\323\344\223\002#\022!/v1/{name=pro"
          + "jects/*/instances/*}\332A\004name\022\234\002\n\016CreateIn"
          + "stance\0227.google.spanner.admin.instance.v"
          + "1.CreateInstanceRequest\032\035.google.longrun"
          + "ning.Operation\"\261\001\202\323\344\223\002&\"!/v1/{parent=pro"
          + "jects/*}/instances:\001*\332A\033parent,instance_"
          + "id,instance\312Ad\n)google.spanner.admin.ins"
          + "tance.v1.Instance\0227google.spanner.admin."
          + "instance.v1.CreateInstanceMetadata\022\235\002\n\016U"
          + "pdateInstance\0227.google.spanner.admin.ins"
          + "tance.v1.UpdateInstanceRequest\032\035.google."
          + "longrunning.Operation\"\262\001\202\323\344\223\002/2*/v1/{ins"
          + "tance.name=projects/*/instances/*}:\001*\332A\023"
          + "instance,field_mask\312Ad\n)google.spanner.a"
          + "dmin.instance.v1.Instance\0227google.spanne"
          + "r.admin.instance.v1.UpdateInstanceMetada"
          + "ta\022\223\001\n\016DeleteInstance\0227.google.spanner.a"
          + "dmin.instance.v1.DeleteInstanceRequest\032\026"
          + ".google.protobuf.Empty\"0\202\323\344\223\002#*!/v1/{nam"
          + "e=projects/*/instances/*}\332A\004name\022\232\001\n\014Set"
          + "IamPolicy\022\".google.iam.v1.SetIamPolicyRe"
          + "quest\032\025.google.iam.v1.Policy\"O\202\323\344\223\0027\"2/v"
          + "1/{resource=projects/*/instances/*}:setI"
          + "amPolicy:\001*\332A\017resource,policy\022\223\001\n\014GetIam"
          + "Policy\022\".google.iam.v1.GetIamPolicyReque"
          + "st\032\025.google.iam.v1.Policy\"H\202\323\344\223\0027\"2/v1/{"
          + "resource=projects/*/instances/*}:getIamP"
          + "olicy:\001*\332A\010resource\022\305\001\n\022TestIamPermissio"
          + "ns\022(.google.iam.v1.TestIamPermissionsReq"
          + "uest\032).google.iam.v1.TestIamPermissionsR"
          + "esponse\"Z\202\323\344\223\002=\"8/v1/{resource=projects/"
          + "*/instances/*}:testIamPermissions:\001*\332A\024r"
          + "esource,permissions\032x\312A\026spanner.googleap"
          + "is.com\322A\\https://www.googleapis.com/auth"
          + "/cloud-platform,https://www.googleapis.c"
          + "om/auth/spanner.adminB\215\002\n$com.google.spa"
          + "nner.admin.instance.v1B\031SpannerInstanceA"
          + "dminProtoP\001ZHgoogle.golang.org/genproto/"
          + "googleapis/spanner/admin/instance/v1;ins"
          + "tance\252\002&Google.Cloud.Spanner.Admin.Insta"
          + "nce.V1\312\002&Google\\Cloud\\Spanner\\Admin\\Inst"
          + "ance\\V1\352\002+Google::Cloud::Spanner::Admin:"
          + ":Instance::V1b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.AnnotationsProto.getDescriptor(),
              com.google.api.ClientProto.getDescriptor(),
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.iam.v1.IamPolicyProto.getDescriptor(),
              com.google.iam.v1.PolicyProto.getDescriptor(),
              com.google.longrunning.OperationsProto.getDescriptor(),
              com.google.protobuf.EmptyProto.getDescriptor(),
              com.google.protobuf.FieldMaskProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_spanner_admin_instance_v1_ReplicaInfo_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_spanner_admin_instance_v1_ReplicaInfo_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_ReplicaInfo_descriptor,
            new java.lang.String[] {
              "Location", "Type", "DefaultLeaderLocation",
            });
    internal_static_google_spanner_admin_instance_v1_InstanceConfig_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_spanner_admin_instance_v1_InstanceConfig_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_InstanceConfig_descriptor,
            new java.lang.String[] {
              "Name", "DisplayName", "Replicas",
            });
    internal_static_google_spanner_admin_instance_v1_Instance_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_spanner_admin_instance_v1_Instance_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_Instance_descriptor,
            new java.lang.String[] {
              "Name", "Config", "DisplayName", "NodeCount", "State", "Labels", "EndpointUris",
            });
    internal_static_google_spanner_admin_instance_v1_Instance_LabelsEntry_descriptor =
        internal_static_google_spanner_admin_instance_v1_Instance_descriptor
            .getNestedTypes()
            .get(0);
    internal_static_google_spanner_admin_instance_v1_Instance_LabelsEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_Instance_LabelsEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsRequest_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsRequest_descriptor,
            new java.lang.String[] {
              "Parent", "PageSize", "PageToken",
            });
    internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsResponse_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_ListInstanceConfigsResponse_descriptor,
            new java.lang.String[] {
              "InstanceConfigs", "NextPageToken",
            });
    internal_static_google_spanner_admin_instance_v1_GetInstanceConfigRequest_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_spanner_admin_instance_v1_GetInstanceConfigRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_GetInstanceConfigRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_spanner_admin_instance_v1_GetInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_spanner_admin_instance_v1_GetInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_GetInstanceRequest_descriptor,
            new java.lang.String[] {
              "Name", "FieldMask",
            });
    internal_static_google_spanner_admin_instance_v1_CreateInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(7);
    internal_static_google_spanner_admin_instance_v1_CreateInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_CreateInstanceRequest_descriptor,
            new java.lang.String[] {
              "Parent", "InstanceId", "Instance",
            });
    internal_static_google_spanner_admin_instance_v1_ListInstancesRequest_descriptor =
        getDescriptor().getMessageTypes().get(8);
    internal_static_google_spanner_admin_instance_v1_ListInstancesRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_ListInstancesRequest_descriptor,
            new java.lang.String[] {
              "Parent", "PageSize", "PageToken", "Filter",
            });
    internal_static_google_spanner_admin_instance_v1_ListInstancesResponse_descriptor =
        getDescriptor().getMessageTypes().get(9);
    internal_static_google_spanner_admin_instance_v1_ListInstancesResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_ListInstancesResponse_descriptor,
            new java.lang.String[] {
              "Instances", "NextPageToken",
            });
    internal_static_google_spanner_admin_instance_v1_UpdateInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(10);
    internal_static_google_spanner_admin_instance_v1_UpdateInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_UpdateInstanceRequest_descriptor,
            new java.lang.String[] {
              "Instance", "FieldMask",
            });
    internal_static_google_spanner_admin_instance_v1_DeleteInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(11);
    internal_static_google_spanner_admin_instance_v1_DeleteInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_DeleteInstanceRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_spanner_admin_instance_v1_CreateInstanceMetadata_descriptor =
        getDescriptor().getMessageTypes().get(12);
    internal_static_google_spanner_admin_instance_v1_CreateInstanceMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_CreateInstanceMetadata_descriptor,
            new java.lang.String[] {
              "Instance", "StartTime", "CancelTime", "EndTime",
            });
    internal_static_google_spanner_admin_instance_v1_UpdateInstanceMetadata_descriptor =
        getDescriptor().getMessageTypes().get(13);
    internal_static_google_spanner_admin_instance_v1_UpdateInstanceMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_spanner_admin_instance_v1_UpdateInstanceMetadata_descriptor,
            new java.lang.String[] {
              "Instance", "StartTime", "CancelTime", "EndTime",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.ClientProto.defaultHost);
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.AnnotationsProto.http);
    registry.add(com.google.api.ClientProto.methodSignature);
    registry.add(com.google.api.ClientProto.oauthScopes);
    registry.add(com.google.api.ResourceProto.resource);
    registry.add(com.google.api.ResourceProto.resourceReference);
    registry.add(com.google.longrunning.OperationsProto.operationInfo);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.AnnotationsProto.getDescriptor();
    com.google.api.ClientProto.getDescriptor();
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.iam.v1.IamPolicyProto.getDescriptor();
    com.google.iam.v1.PolicyProto.getDescriptor();
    com.google.longrunning.OperationsProto.getDescriptor();
    com.google.protobuf.EmptyProto.getDescriptor();
    com.google.protobuf.FieldMaskProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
