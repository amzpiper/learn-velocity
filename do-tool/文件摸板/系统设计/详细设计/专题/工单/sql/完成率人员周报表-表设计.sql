

DROP TABLE IF EXISTS "dm_order"."dm_successrate_week_people_f";
CREATE TABLE IF NOT EXISTS "dm_order"."dm_successrate_week_people_f"
(
    "id" serial,
    "order_type_name" character varying(60),
    "current_user_name" character varying(60),
    "current_user_id" character varying(60),
    "total" character varying(60),
    "usetime" character varying(60),
    "successrate" character varying(60),
    "timelyrate" character varying(60),
    "outtimecount" character varying(60),
	
    "qiangdian" character varying(60),
    "ruodian" character varying(60),
    "nuantong" character varying(60),
    "zongxiu" character varying(60),
    "qingjie" character varying(60),
    "lvhua" character varying(60),
    "anbao" character varying(60),
    "other" character varying(60),
	
    "dw_creation_by" character varying(100),
    "dw_creation_date" timestamp(6),
    "dw_last_update_by" character varying(100),
    "dw_last_update_date" timestamp(6),
    "dw_batch_number" bigint
)WITH(orientation=row,compression=no) 
DISTRIBUTE BY HASH("order_type_name");
COMMENT ON TABLE "dm_order"."dm_successrate_week_people_f" IS '完成率人员周报表';

COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.id IS '人员周报id';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.order_type_name IS '部门名称';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.current_user_name IS '员工名';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.current_user_id IS '员工id';

COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.total IS '总工单数';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.usetime IS '总用时';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.successrate IS '完成率';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.timelyrate IS '及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.outtimecount IS '超时数';

COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.qiangdian IS '强电';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.ruodian IS '弱点';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.nuantong IS '暖通';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.zongxiu IS '综修';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.qingjie IS '清洁';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.lvhua IS '绿化';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.anbao IS '安保';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.other IS '其他';

COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.dw_creation_by IS '数据创建者';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.dw_creation_date IS '数据创建时间';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.dw_last_update_by IS '数据最后更新者';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.dw_last_update_date IS '数据最后更新时间';
COMMENT ON COLUMN dm_order.dm_successrate_week_people_f.dw_batch_number IS '数据批次号';