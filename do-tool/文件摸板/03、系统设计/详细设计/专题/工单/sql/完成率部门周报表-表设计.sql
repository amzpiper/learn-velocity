DROP TABLE IF EXISTS "dm_order"."dm_successrate_week_emp_f";
CREATE TABLE IF NOT EXISTS "dm_order"."dm_successrate_week_emp_f"
(
    "id" serial,
    "order_type_name" character varying(60),
    "sig_order_subtype" character varying(60),
    "total" character varying(60),
    "successrate" character varying(60),
    "timelyrate" character varying(60),
    "paidantimelyrate" character varying(60),
    "jiedantimelyrate" character varying(60),
    "chulitimelyrate" character varying(60),
    "shenhetimelyrate" character varying(60),
    "huifangtimelyrate" character varying(60),
    "dw_creation_by" character varying(100),
    "dw_creation_date" timestamp(6),
    "dw_last_update_by" character varying(100),
    "dw_last_update_date" timestamp(6),
    "dw_batch_number" bigint
)WITH(orientation=row,compression=no) 
DISTRIBUTE BY HASH("order_type_name");
COMMENT ON TABLE "dm_order"."dm_successrate_week_emp_f" IS '完成率部门周报表';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.id IS '类型表id';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.order_type_name IS '部门名称';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.sig_order_subtype IS '子服务类型';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.total IS '总工单数';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.successrate IS '完成率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.timelyrate IS '及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.paidantimelyrate IS '派单及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.jiedantimelyrate IS '接单及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.chulitimelyrate IS '处理及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.shenhetimelyrate IS '审核及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.huifangtimelyrate IS '回访及时率';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.dw_creation_by IS '数据创建者';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.dw_creation_date IS '数据创建时间';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.dw_last_update_by IS '数据最后更新者';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.dw_last_update_date IS '数据最后更新时间';
COMMENT ON COLUMN dm_order.dm_successrate_week_emp_f.dw_batch_number IS '数据批次号';