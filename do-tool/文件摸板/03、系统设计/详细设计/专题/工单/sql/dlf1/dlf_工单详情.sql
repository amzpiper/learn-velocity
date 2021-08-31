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
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_order'',''dm_order_detials_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dm_order.dm_order_detials_f  -- 工单详情
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 跑批将前一天的数据插入到目标表
    INSERT INTO
        dm_order.dm_order_detials_f (    -- 工单详情
        order_no,    --工单号
        sig_order_subtype,    --四格问题子类型
        order_type,    --问题类型
        addr,    --问题地点
        description,    --问题描述
        connect,    --联系方式
        connector,    --联系人
        level,    --紧急程度
        create_time,    --创建事件
        source_type,    --来源类型
        order_time,    --报修时间
        order_status,    --工单状态
        del_flag,    --删除标识
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        evaluate_level,    --评价等级
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_no  AS order_no,
        a.sig_order_subtype  AS sig_order_subtype,
        a.order_type  AS order_type,
        a.addr  AS addr,
        a.description  AS description,
        a.connect  AS connect,
        a.connector  AS connector,
        a.level  AS level,
        a.create_time  AS create_time,
        a.source_type  AS source_type,
        a.order_time  AS order_time,
        a.order_status  AS order_status,
        a.del_flag  AS del_flag,
        a.outtime_status  AS outtime_status,
        a.sig_order_subtype  AS sig_order_subtype,
        a.evaluate_level  AS evaluate_level,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM
        dm_order.dm_order_detials_f a    -- 工单详情
    WHERE
        a.dw_creation_date = (to_timestamp(u_current_day, 'yyyy-MM-dd') + interval '-1 D');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM dm_order.dm_order_detials_f  -- 工单详情
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND record_id in (
        SELECT a.id
        FROM dwr_order.dwd_order_t_repairs_order_f a     -- 报事报修工单表
        JOIN dm_order.dm_order_detials_f b     -- 工单详情
        ON (a.id = b.record_id)
        WHERE TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = u_current_day
    );

    -- 2.新增数据

    INSERT INTO
        dm_order.dm_order_detials_f (    -- 工单详情
        order_no,    --工单号
        sig_order_subtype,    --四格问题子类型
        order_type,    --问题类型
        addr,    --问题地点
        description,    --问题描述
        connect,    --联系方式
        connector,    --联系人
        level,    --紧急程度
        create_time,    --创建事件
        source_type,    --来源类型
        order_time,    --报修时间
        order_status,    --工单状态
        del_flag,    --删除标识
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        evaluate_level,    --评价等级
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_no AS order_no,
        a.sige_order_id AS sig_order_subtype,
        a.order_type AS order_type,
        a.addr AS addr,
        a.desc AS description,
        a.connect AS connect,
        a.connector AS connector,
        a.level AS level,
        a.create_time AS create_time,
        a.source_type AS source_type,
        a.order_time AS order_time,
        a.order_status AS order_status,
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
    WHERE  TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = u_current_day;

END;