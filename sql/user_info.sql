-- �û�
CREATE TABLE `user_info` (
  `id` varchar(32) NOT NULL,
  `username` varchar(32) DEFAULT '',
  `password` varchar(32) DEFAULT '',
  `openid` varchar(64) DEFAULT '' COMMENT '΢��openid',
  `role` tinyint(1) NOT NULL COMMENT '1���2����',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��',
  PRIMARY KEY (`id`)
);