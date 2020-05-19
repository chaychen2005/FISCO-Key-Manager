
-- ----------------------------
-- 1、init tb_account_info data   admin/Abcd1234 56edd4d56db20ad3b7b387fb8963a639c020282c6a4195f7a699b1b487fb6567
-- ----------------------------
INSERT INTO `tb_account_info` (account,account_pwd,role_id,create_time,modify_time,public_key) VALUES ('admin', '$2a$10$B3916RQ45C7jh/5fJlGvvuOSx9xU5mrQq9sxmvteAZBmCStZxw0u6', 100000, now(), now(),'8d83963610117ed59cf2011d5b7434dca7bb570d4a16e63c66f0803f4c4b1c03a1125500e5ca699dfbb6b48d450a82a5020fcb3b43165b508c10cb1479c6ee49');



-- ----------------------------
-- 2、init tb_role data
-- ----------------------------
INSERT INTO `tb_role` (role_name,role_name_zh,create_time,modify_time) VALUES ('admin', '管理员', now(), now());
INSERT INTO `tb_role` (role_name,role_name_zh,create_time,modify_time) VALUES ('visitor', '普通用户', now(), now());
