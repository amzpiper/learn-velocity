DECLARE
-- order script_dm_order_overview_f_dwr_order 汇聚主题库：工单概览
--    目的：    全量分区 增量采集dwi层 报事报修工单表 插入到dwd层中 工单概览 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_order_overview_f ,   schema : dm_order,   中文名：工单概览；
--    源表：  表名：dwd_order_t_repairs_order_f,   schema : dwr_order,  中文名：报事报修工单表；
--    作者：  lixj
--    创建日期：  2021-05-16
--    审核人：  lixj
--    审核时间：  2021-05-16
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day=TO_CHAR((to_date(u_current_day_input)), 'yyyy-MM-dd');
    -- SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_order'',''dm_order_detials_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dm_order.dm_order_overview_f; -- 工单概览

    -- 2.新增数据

    INSERT INTO
        dm_order.dm_order_overview_f (    -- 工单概览
        order_id,
        order_type,    --问题类型
        -- order_type_name,
        order_time,    --报修时间
        order_status,    --工单状态
        public_flag,
        del_flag,
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        -- sig_order_subtype_name,
        evaluate_level,    --评价等级
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_id AS order_id,
        a.order_type AS order_type,
        a.order_time AS order_time,
        a.order_status AS order_status,
        case when d.com_name in (select dictionary_value from dwr_order.dwd_order_dictionary_d where dictionary_name = 'public') then '1' else '0' end public_flag,
        a.del_flag AS del_flag,
        a.outtime_status AS outtime_status,
        a.sig_order_subtype AS sig_order_subtype,
        a.evaluate_level AS evaluate_level,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwr_order.dwd_order_t_repairs_order_f a    -- 报事报修工单表
     left join dwr_enterprise.dwd_enterprise_t_com_f d on d.credit_code = a.credit_code; -- 公司表
    --  where '1' = '1';
    -- WHERE  TO_CHAR(b.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;
