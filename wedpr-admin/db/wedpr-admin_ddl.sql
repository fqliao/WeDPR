CREATE TABLE `wedpr_agency` (
  `agency_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '机构编号',
  `agency_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '机构名',
  `agency_desc` text COLLATE utf8mb4_bin NOT NULL COMMENT '机构描述',
  `agency_contact` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '机构联系人',
  `contact_phone` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '联系电话',
  `gateway_endpoint` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '网关地址',
  `agency_status` tinyint NOT NULL DEFAULT '0' COMMENT '机构状态(0:启用，1:禁用)',
  `user_count` int DEFAULT '0' COMMENT '机构用户数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`agency_id`),
  KEY `idx_agency_name` (`agency_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;



CREATE TABLE `wedpr_authorization_table` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '申请单ID',
  `applicant` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '申请人姓名',
  `applicant_agency` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '申请人所属机构',
  `apply_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '申请类型,如数据集权限申请等',
  `apply_title` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '申请标题',
  `apply_desc` text COLLATE utf8mb4_bin COMMENT '申请单详情',
  `auth_chain` text COLLATE utf8mb4_bin COMMENT '审批链信息',
  `apply_content` longtext COLLATE utf8mb4_bin NOT NULL COMMENT '申请信息',
  `apply_template` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '申请表单模板名',
  `status` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '审批状态',
  `current_auth_node` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '当前审批节点',
  `current_auth_node_agency` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '当前审批节点所属机构',
  `result` longtext COLLATE utf8mb4_bin COMMENT '包含所有审批链的审批结果',
  `execute_result` text COLLATE utf8mb4_bin COMMENT '审批单执行结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '审批单创建时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '审批单更新时间',
  PRIMARY KEY (`id`),
  KEY `applicant_index` (`applicant`,`applicant_agency`),
  KEY `status_index` (`status`),
  KEY `auth_node_index` (`current_auth_node`(128),`current_auth_node_agency`(128)),
  KEY `apply_title_index` (`apply_title`(128))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_cert` (
  `cert_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '证书id',
  `agency_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '机构id',
  `agency_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '机构名',
  `csr_file_text` text COLLATE utf8mb4_bin NOT NULL COMMENT '机构证书请求文件内容',
  `cert_file_text` text COLLATE utf8mb4_bin NOT NULL COMMENT '机构证书文件内容',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `cert_status` tinyint NOT NULL DEFAULT '0' COMMENT '证书状态(0：无证书，1：有效，2：过期，3：禁用)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `csr_file_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`cert_id`),
  KEY `idx_agency_name` (`agency_name`),
  KEY `idx_cert_status` (`cert_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_config_table` (
  `config_key` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '配置项主键',
  `config_value` longtext COLLATE utf8mb4_bin NOT NULL COMMENT '配置项的值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '配置创建时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '配置更新时间',
  `report_status` tinyint DEFAULT '0',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_dataset` (
  `dataset_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集id',
  `dataset_title` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集标题',
  `dataset_label` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集标签',
  `dataset_desc` text COLLATE utf8mb4_bin NOT NULL COMMENT '数据集描述',
  `dataset_fields` text COLLATE utf8mb4_bin COMMENT '数据源字段以及预览信息',
  `dataset_version_hash` varchar(64) COLLATE utf8mb4_bin DEFAULT '' COMMENT '数据集hash',
  `dataset_size` bigint DEFAULT '0' COMMENT '数据集大小',
  `dataset_record_count` bigint DEFAULT '0' COMMENT '数据集记录数目',
  `dataset_column_count` int DEFAULT '0' COMMENT '数据集列数目',
  `dataset_storage_type` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '数据集存储类型',
  `dataset_storage_path` varchar(1024) COLLATE utf8mb4_bin DEFAULT '' COMMENT '数据集存储路径',
  `owner_agency_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集所属机构名称',
  `owner_user_name` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集所属用户名',
  `data_source_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '数据源类型 : CSV、DB、XLSX、HDFS、HIVE',
  `data_source_meta` text COLLATE utf8mb4_bin NOT NULL COMMENT '数据源参数信息，JSON字符串',
  `visibility` int NOT NULL COMMENT '数据集可见性, 0: 私有，1: 公开可见',
  `visibility_details` text COLLATE utf8mb4_bin NOT NULL COMMENT '数据源可见范围描述, visibility 为1时有效',
  `approval_chain` text COLLATE utf8mb4_bin NOT NULL COMMENT '审批链信息',
  `status` tinyint NOT NULL COMMENT '数据集状态, 0: 有效，其他无效',
  `status_desc` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集状态描述',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `report_status` tinyint DEFAULT '0',
  PRIMARY KEY (`dataset_id`),
  KEY `dataset_title_index` (`dataset_title`(128)),
  KEY `owner_agency_name_index` (`owner_agency_name`),
  KEY `owner_user_name_index` (`owner_user_name`(128)),
  KEY `create_at_index` (`create_at`),
  KEY `update_at_index` (`update_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='数据集记录表';


CREATE TABLE `wedpr_dataset_permission` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集权限id',
  `dataset_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集id',
  `permission_type` int NOT NULL COMMENT '权限类型',
  `permission_scope` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '权限范围',
  `permission_subject_id` text COLLATE utf8mb4_bin NOT NULL COMMENT '数据集授权对象的id',
  `expired_at` date NOT NULL DEFAULT '9999-12-31',
  `create_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `dataset_id_index` (`dataset_id`),
  KEY `permission_type_index` (`permission_type`),
  KEY `permission_scope_index` (`permission_scope`),
  KEY `expired_at_index` (`expired_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='数据集权限表';


CREATE TABLE `wedpr_group` (
  `group_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `group_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `admin_name` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`group_id`),
  KEY `idx_group_name` (`group_name`),
  KEY `idx_admin_name` (`admin_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_group_detail` (
  `group_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`group_id`,`username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_job_dataset_relation` (
  `job_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '任务ID',
  `dataset_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据集ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  KEY `job_id_index` (`job_id`),
  KEY `dataset_id_index` (`dataset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_job_table` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '任务ID',
  `name` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务名称',
  `project_id` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务所属项目',
  `owner` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '任务发起人',
  `owner_agency` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '任务发起机构',
  `job_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '任务类型',
  `parties` text COLLATE utf8mb4_bin COMMENT '任务相关机构信息(json)',
  `param` longtext COLLATE utf8mb4_bin COMMENT '任务参数(json)',
  `status` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务状态',
  `job_result` longtext COLLATE utf8mb4_bin COMMENT '任务执行结果(json)',
  `report_status` tinyint DEFAULT '0' COMMENT '上报状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '任务更新时间',
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`(128)),
  KEY `owner_index` (`owner`(128)),
  KEY `owner_agency_index` (`owner_agency`(128)),
  KEY `project_index` (`project_id`(128)),
  KEY `status_index` (`status`(128)),
  KEY `report_status_index` (`report_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;



CREATE TABLE `wedpr_permission` (
  `permission_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `permission_name` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `permission_content` text COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;



CREATE TABLE `wedpr_project_table` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '项目ID',
  `name` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '项目名称',
  `desc` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '项目描述',
  `owner` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '项目属主',
  `owner_agency` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '项目所属机构',
  `project_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '项目类型(Export/Wizard)',
  `label` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目标签',
  `report_status` tinyint DEFAULT '0' COMMENT '上报状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '项目创建时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '项目更新时间',
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`(128)),
  KEY `owner_index` (`owner`(128),`owner_agency`(128)),
  KEY `project_type_index` (`project_type`(128)),
  KEY `label_index` (`label`(128)),
  KEY `report_status_index` (`report_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_role_permission` (
  `role_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `permission_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `idx_role_name` (`role_name`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_sync_status_table` (
  `resource_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '资源ID',
  `resource_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '资源类型',
  `trigger` varchar(255) COLLATE utf8mb4_bin DEFAULT '' COMMENT '触发者',
  `agency` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '发起机构',
  `resource_action` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '资源类型',
  `index` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源索引',
  `block_number` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源所在块高',
  `transaction_hash` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链上交易Hash',
  `status` varchar(1024) COLLATE utf8mb4_bin DEFAULT '' COMMENT '状态',
  `status_msg` text COLLATE utf8mb4_bin COMMENT '状态说明',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`resource_id`),
  KEY `trigger_index` (`trigger`),
  KEY `agency_index` (`agency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_user` (
  `username` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `email` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL,
  `password` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
  `phone` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `try_count` int DEFAULT NULL,
  `allowed_timestamp` bigint DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;


CREATE TABLE `wedpr_user_role` (
  `username` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `role_id` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `update_by` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`username`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;