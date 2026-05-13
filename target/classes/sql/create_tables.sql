-- =====================================================
-- tlias 数据库 - 班级表和学员表
-- =====================================================

CREATE TABLE IF NOT EXISTS clazz (
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    name        VARCHAR(30) NOT NULL UNIQUE COMMENT '班级名称',
    room        VARCHAR(20) COMMENT '班级教室',
    begin_date  DATE NOT NULL COMMENT '开课时间',
    end_date    DATE NOT NULL COMMENT '结课时间',
    master_id   INT UNSIGNED COMMENT '班主任ID',
    subject     TINYINT UNSIGNED NOT NULL COMMENT '1:Java 2:前端 3:大数据 4:Python 5:Go 6:嵌入式',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '修改时间'
) COMMENT '班级表';

CREATE TABLE IF NOT EXISTS student (
    id               INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    name             VARCHAR(10) NOT NULL COMMENT '姓名',
    no               CHAR(10) NOT NULL UNIQUE COMMENT '学号',
    gender           TINYINT UNSIGNED NOT NULL COMMENT '1:男 2:女',
    phone            VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    id_card          CHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    is_college       TINYINT UNSIGNED NOT NULL COMMENT '1:是 0:否',
    address          VARCHAR(100) COMMENT '联系地址',
    degree           TINYINT UNSIGNED COMMENT '1:初中 2:高中 3:大专 4:本科 5:硕士 6:博士',
    graduation_date  DATE COMMENT '毕业时间',
    clazz_id         INT UNSIGNED NOT NULL COMMENT '班级ID',
    violation_count  TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '违纪次数',
    violation_score  TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '违纪扣分',
    create_time      DATETIME COMMENT '创建时间',
    update_time      DATETIME COMMENT '修改时间'
) COMMENT '学员表';
