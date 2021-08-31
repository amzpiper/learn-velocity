DECLARE
-- order script_dm_order_detials_f_dwr_order 汇聚主题库：工单详情
--    目的：    全量分区 增量采集dwi层 报事报修工单表 插入到dwd层中 工单详情 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_order_detials_f ,   schema : dm_order,   中文名：工单详情；
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
    DELETE FROM dm_order.dm_order_detials_f; -- 工单详情

    -- 2.新增数据

    INSERT INTO
        dm_order.dm_order_detials_f (    -- 工单详情
        order_id,
        order_no,    --工单号
        sige_order_id,    --四格问题子类型
        order_type,    --问题类型
        addr,    --问题地点
        description,    --问题描述
        connect_no,    --联系方式
        connector,    --联系人
        level_no,    --紧急程度
        create_time,    --创建事件
        source_type,    --来源类型
        order_time,    --报修时间
        order_status,    --工单状态
        del_flag,    --删除标识
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        evaluate_level,    --评价等级
        track_name,
        track_code,
        track_index,
        status,
        over_status,
        current_user_id,
        current_user_name,
        current_user_type,
        track_create_time,
        track_update_time,

        event_type,
        event_user_id,
        event_time,
        event_user_name,
        target_user_id,
        target_user_name,
        sup_flag,

        com_name,
        com_type,
        company_level,
        public_flag,
        proj_name,
        standard_time,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_id AS order_id,
        a.order_no AS order_no,
        a.sige_order_id AS sige_order_id,
        a.order_type AS order_type,
        a.addr AS addr,
        a.description AS description,
        a.connect_no AS connect_no,
        a.connector AS connector,
        a.level_no AS level_no,
        a.create_time AS create_time,
        a.source_type AS source_type,   
        a.order_time AS order_time,
        a.order_status AS order_status,
        a.del_flag AS del_flag,
        a.outtime_status AS outtime_status,
        a.sig_order_subtype AS sig_order_subtype,
        a.evaluate_level AS evaluate_level,
        b.track_name AS track_name,
        b.track_code AS track_code,
        case when b.track_code = 'NODE_DS_SUBMIT' then '1'
             when b.track_code = 'NODE_DS_DISPATCH' then '2'
             when b.track_code = 'NODE_DS_TAKE' then '3'
             when b.track_code = 'NODE_DS_DEAL' then '4'
             when b.track_code = 'NODE_DS_CHECK' then '5'
             when b.track_code = 'NODE_DS_EVLUATE' then '6'
             when b.track_code = 'NODE_DS_RECALL' then '7'
             else '8' end AS track_index,
        b.status AS status,
        --需要做判断
        case when c.event_type='3'then '1' else '0' end AS over_status,
        b.current_user_id AS current_user_id,
        b.current_user_name AS current_user_name,
        b.current_user_type AS current_user_type,
        b.create_time AS track_create_time,
        b.update_time AS track_update_time,
        

        c.event_type,
        c.event_user_id,
        c.event_time,
        c.event_user_name,
        c.target_user_id,
        c.target_user_name,
        c.sup_flag,
        d.com_name AS com_name,
        d.com_type AS com_type,
        d.level_no AS company_level,
        case when d.com_name in (select dictionary_value from dwr_order.dwd_order_dictionary_d where dictionary_name = 'public') then '1' else '0' end public_flag,
        e.res_name AS proj_name,
        f.dictionary_value AS standard_time,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwr_order.dwd_order_t_repair_track_f b    -- 报事报修工单表
     left join dwr_order.dwd_order_t_repairs_order_f a on a.order_id = b.order_id -- 环节表
     left join (SELECT x.* FROM dwr_order.dwd_order_t_repair_event_f x
        RIGHT JOIN (SELECT track_id, max(event_time) event_time FROM dwr_order.dwd_order_t_repair_event_f group by track_id) y
        on x.track_id = y.track_id and x.event_time = y.event_time) c on b.track_id = c.track_id -- 环节事件表
     left join dwr_enterprise.dwd_enterprise_t_com_f d on d.credit_code = a.credit_code -- 公司表
     left join dwr_space.dwd_space_detial_f e on a.proj_id = e.res_id and res_type = '0' -- 空间表
     left join dwr_order.dwd_order_dictionary_d f on f.dictionary_name = b.track_code; -- 字典表
--     where '1' = '1'; 
    -- WHERE  TO_CHAR(b.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;
