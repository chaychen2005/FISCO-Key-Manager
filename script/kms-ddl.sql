
-- ----------------------------
-- Table structure for tb_account_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_account_info (
  account varchar(50) binary NOT NULL COMMENT '系统账号',
  account_pwd varchar(250) NOT NULL COMMENT '登录密码',
  role_id int(11) NOT NULL COMMENT '所属角色编号',
  account_status int(1) NOT NULL DEFAULT '1' COMMENT '状态（1-未更新密码 2-正常 3-冻结） 默认1',
  description text COMMENT '备注',
  email varchar(40) DEFAULT NULL COMMENT '用户邮箱',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  modify_time datetime DEFAULT NULL COMMENT '修改时间',
  public_key varchar(250) COMMENT '该账号使用的公钥',
  creator varchar(50) binary COMMENT '创建该账号的账号',
  PRIMARY KEY (account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统账号信息表';

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_role (
  role_id int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  role_name varchar(120) DEFAULT NULL COMMENT '角色英文名称',
  role_name_zh varchar(120) DEFAULT NULL COMMENT '角色中文名称',
  role_status int(1) DEFAULT '1' COMMENT '状态（1-正常2-无效） 默认1',
  description text COMMENT '备注',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  modify_time datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (role_id),
  UNIQUE KEY UK_role_Name (role_name)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- ----------------------------
-- Table structure for tb_token
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_token (
  token varchar(120) NOT NULL PRIMARY KEY COMMENT 'token',
  value varchar(50) NOT NULL COMMENT '用户编号',
  expire_time datetime DEFAULT NULL COMMENT '失效时间',
  create_time datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='token信息表';

-- ----------------------------
-- Table structure for tb_key_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS tb_key_info (
  account varchar(50) binary NOT NULL COMMENT '系统账号',
  key_alias varchar(255) NOT NULL COMMENT '私钥标识',
  cipher_text text NOT NULL COMMENT '经管理员公钥加密的私钥',
  private_key text NOT NULL COMMENT '用户托管的信息，经访客加密密码加密的私钥',
  create_time datetime DEFAULT NULL COMMENT '托管私钥的时间',
  PRIMARY KEY (account,key_alias)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='私钥信息表';

