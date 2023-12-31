CREATE TABLE sys_platform
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    platform    varchar(10)  DEFAULT NULL COMMENT '平台名称',
    login_url   varchar(100) DEFAULT NULL COMMENT '登录地址',
    create_time datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='电商平台账号';

CREATE TABLE sys_account
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    platform    varchar(10) DEFAULT NULL COMMENT '平台名称',
    username    varchar(20) DEFAULT NULL COMMENT '账号',
    password    varchar(20) DEFAULT NULL COMMENT '密码',
    status      int(1)      DEFAULT 0 COMMENT '账号状态',
    create_time datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='电商平台账号';

CREATE TABLE sys_task
(
    id           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    platform     varchar(10)  DEFAULT NULL COMMENT '平台名称',
    task_name    varchar(100) DEFAULT NULL COMMENT '任务名称',
    entrance_url varchar(100) DEFAULT NULL COMMENT '入口',
    email        varchar(50)  DEFAULT NULL COMMENT '邮箱',
    parallel_num int          DEFAULT 1 COMMENT '并行数量',
    create_time  datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='电商平台账号';


CREATE TABLE taobao_shop
(
    id          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    shop_url    varchar(200) DEFAULT NULL COMMENT '网店地址',
    shop_state  int          DEFAULT NULL COMMENT '网店状态',
    shop_name   varchar(50)  DEFAULT NULL COMMENT '网店名称',
    shop_id     bigint       DEFAULT NULL COMMENT '网店id',
    start_date  char(10)     DEFAULT NULL COMMENT '开店时间',
    extern_id   bigint(20)   DEFAULT NULL COMMENT '外部id',
    create_time datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY idx_extern_id (extern_id)
) ENGINE = InnoDB COMMENT ='淘宝网店';