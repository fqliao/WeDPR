insert into `wedpr_config_table`(`config_key`, `config_value`) values("wedpr_algorithm_templates", '{"version":"1.0","templates":[{"name":"PSI","title":"数据对齐","supportable":true,"enable":true,"participateNumber":"2+","detail":"","version":"1.0"},{"name":"PIR","title":"匿踪查询","supportable":true,"enable":true,"participateNumber":"1","detail":"","version":"1.0"},{"name":"SQL","title":"联表分析","supportable":true,"enable":true,"participateNumber":"1+","detail":"","version":"1.0"},{"name":"MPC","title":"自定义计算","supportable":true,"enable":true,"participateNumber":"1+","detail":"","version":"1.0"},{"name":"XGB_TRAINING","title":"SecureLGBM训练","supportable":true,"enable":true,"participateNumber":"1+","needTagsProvider":true,"detail":"","version":"1.0"},{"name":"XGB_PREDICTING","title":"SecureLGBM预测","supportable":true,"enable":true,"participateNumber":"1+","needTagsProvider":true,"detail":"","version":"1.0"},{"name":"LR_TRAINING","title":"SecureLR建模","supportable":true,"enable":true,"participateNumber":"1+","needTagsProvider":true,"detail":"","version":"1.0"},{"name":"LR_PREDICTING","title":"SecureLR预测","supportable":true,"enable":true,"participateNumber":"1+","needTagsProvider":true,"detail":"","version":"1.0"},{"name":"PREPROCESSING","title":"数据预处理","supportable":true,"detail":"","version":"1.0"},{"name":"FEATURE_ENGINEERING","title":"特征工程","supportable":true,"detail":"","version":"1.0"}]}');
insert into `wedpr_config_table`(`config_key`, `config_value`) values("wedpr_create_user", 'useradd -m ${user_name}');
insert into `wedpr_config_table`(`config_key`, `config_value`) values('wedpr_set_permission', 'chmod -R 700 /home/${user_name}');
insert into `wedpr_config_table`(`config_key`, `config_value`) values("wedpr_start_jupyter", 'su - ${user_name} -c \'export JUPYTER_AUTH_SECRET=${auth_secret} && mkdir -p  ${jupyter_project_path} && nohup ${jupyter_binary} --config=${jupyter_config_path} --ip ${listen_ip} --port ${listen_port} --notebook-dir ${jupyter_project_path} >> ${user_name}.jupyter.out 2>&1 & \'');
insert into `wedpr_config_table`(`config_key`, `config_value`) values("wedpr_get_jupyter_pid", 'ps aux | grep -i ${jupyter_binary} | grep -i ${jupyter_project_path} | grep -v grep | awk -F\' \' \'{print $2}\'');
insert into `wedpr_config_table`(`config_key`, `config_value`) values("wedpr_delete_user", 'userdel -r ${user_name}');

INSERT INTO wedpr_group (group_id,group_name,admin_name,create_time,update_time,create_by,update_by,status) VALUES
	 ('1000000000000000','初始用户组','admin','2024-08-21 18:25:31','2024-08-21 18:25:31','','',0);
INSERT INTO wedpr_group_detail (group_id,username,create_time,update_time,create_by,update_by,status) VALUES
	 ('1000000000000000','admin','2024-08-21 18:25:31','2024-08-21 18:25:31','','',0);
INSERT INTO wedpr_role_permission (role_id,role_name,permission_id,create_time,update_time,create_by,update_by) VALUES
	 ('10','agency_admin','1','2024-08-21 18:25:31','2024-08-22 23:36:37','','');
INSERT INTO wedpr_user (username,email,password,phone,try_count,allowed_timestamp,create_time,update_time,create_by,update_by,status) VALUES
	 ('admin',NULL,'{bcrypt}$2a$10$ZCtuakpAnPjP0.OtO6s3kev6AG6r7cfKMsZD8ZaeNMiwI9XaKzO7q',NULL,0,0,'2024-08-21 18:25:31','2025-03-28 09:28:01','','admin',0);
INSERT INTO wedpr_user_role (username,role_id,create_time,update_time,create_by,update_by) VALUES
	 ('admin','10','2024-08-21 18:25:31','2024-08-21 18:25:31','','');














