-- 工单标准表 --
DROP TABLE IF EXISTS "dwr_order"."dwd_order_dictionary_d";
CREATE TABLE IF NOT EXISTS "dwr_order"."dwd_order_dictionary_d"
(
    "id" serial,
    "dictionary_value" character varying(200),
    "dictionary_name" character varying(100),
    "type" character varying(50),
    "type_name" character varying(255),
    "dw_creation_by" character varying(100),
    "dw_creation_date" timestamp(6),
    "dw_last_update_by" character varying(100),
    "dw_last_update_date" timestamp(6),
    "dw_batch_number" bigint
)WITH(orientation=row,compression=no) 
DISTRIBUTE BY HASH("id") 
PARTITION BY RANGE (dw_creation_date)
(
    PARTITION P0 VALUES LESS THAN(to_timestamp('2020-05-01','YYYY-MM-DD HH24:MI:SS')),
    PARTITION PMAX VALUES LESS THAN(MAXVALUE)
);
COMMENT ON TABLE "dwr_order"."dwd_order_dictionary_d" IS '工单标准表';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.id IS '系统ID';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dictionary_value IS '字典值';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dictionary_name IS '字典名称';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.type IS '类别';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.type_name IS '类别名称';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dw_creation_by IS '数据创建者';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dw_creation_date IS '数据创建时间';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dw_last_update_by IS '数据最后更新者';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dw_last_update_date IS '数据最后更新时间';
COMMENT ON COLUMN dwr_order.dwd_order_dictionary_d.dw_batch_number IS '数据批次号';

DELETE FROM "dwr_order"."dwd_order_dictionary_d";
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('1','0','NODE_DS_SUBMIT','track','提单');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('2','1','NODE_DS_DISPATCH','track','派单');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('3','1','NODE_DS_TAKE','track','接单');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('4','2','NODE_DS_DEAL','track','处理');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('5','1','NODE_DS_CHECK','track','审核');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('6','0','NODE_DS_EVLUATE','track','评价');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('7','1','NODE_DS_RECALL','track','回访');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('8','0','NODE_DS_OVER','track','结束');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('9','北京华贸物业顾问有限公司第一分公司','public','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('10','北京华贸商业管理有限公司','public','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('11','北京国华置业有限公司','public','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('12','北京华贸中心公共设施','project','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('13','北京华贸中心写字楼二期','project','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('14','北京华贸中心写字楼一期','project','','');
INSERT INTO "dwr_order"."dwd_order_dictionary_d"(id,dictionary_value,dictionary_name,type,type_name) VALUES('15','中心商业-购物中心','project','','');

