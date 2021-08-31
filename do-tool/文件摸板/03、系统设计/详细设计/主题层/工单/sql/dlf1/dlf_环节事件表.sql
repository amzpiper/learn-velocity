DECLARE
-- order script_dwd_order_t_repair_event_f_bm 汇聚主题库：环节事件表
--    目的：    全量分区 增量采集dwi层 事件 插入到dwd层中 环节事件表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repair_event_f ,   schema : dwr_order,   中文名：环节事件表；
--    源表：  表名：dwi_bm_t_repair_event_info,   schema : dwi_bm,  中文名：事件；
--    作者：  lihq
--    创建日期：  2021-08-31
--    审核人：  lihq
--    审核时间：  2021-08-31
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repair_event_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_order.dwd_order_t_repair_event_f  -- 环节事件表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 跑批将前一天的数据插入到目标表
    INSERT INTO
        dwr_order.dwd_order_t_repair_event_f (    -- 环节事件表
        event_id,    --事件id
        event_type,    --事件类型
        event_name,    --事件名称
        event_time,    --事件时间
        event_user_id,    --事件处理人id
        event_euser_id,    --事件处理人id（第三方）
        event_user_name,    --事件处理人姓名
        target_user_id,    --目标用户id
        target_euser_id,    --目标用户id（第三方）
        target_user_name,    --目标用户姓名
        track_id,    --所属环节
        plan_relieve_time,    --预计解挂时间
        remark,    --事件描述
        hang_status,    --挂起结果
        fact_relieve_hang,    --实际解挂时间
        review_advise,    --挂起审核意见
        comment_type,    --类型
        photo,    --追记照片
        sup_flag,    --督办级别
        create_time,    --创建时间
        sige_track_id,    --四格环节ID
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.event_id  AS event_id,
        a.event_type  AS event_type,
        a.event_name  AS event_name,
        a.event_time  AS event_time,
        a.event_user_id  AS event_user_id,
        a.event_euser_id  AS event_euser_id,
        a.event_user_name  AS event_user_name,
        a.target_user_id  AS target_user_id,
        a.target_euser_id  AS target_euser_id,
        a.target_user_name  AS target_user_name,
        a.track_id  AS track_id,
        a.plan_relieve_time  AS plan_relieve_time,
        a.remark  AS remark,
        a.hang_status  AS hang_status,
        a.fact_relieve_hang  AS fact_relieve_hang,
        a.review_advise  AS review_advise,
        a.comment_type  AS comment_type,
        a.photo  AS photo,
        a.sup_flag  AS sup_flag,
        a.create_time  AS create_time,
        a.sige_track_id  AS sige_track_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM
        dwr_order.dwd_order_t_repair_event_f a    -- 环节事件表
    WHERE
        a.dw_creation_date = (to_timestamp(u_current_day, 'yyyy-MM-dd') + interval '-1 D');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM dwr_order.dwd_order_t_repair_event_f  -- 环节事件表
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND record_id in (
        SELECT a.id
        FROM dwi_bm.dwi_bm_t_repair_event_info a     -- 事件
        JOIN dwr_order.dwd_order_t_repair_event_f b     -- 环节事件表
        ON (a.id = b.record_id)
        WHERE TO_CHAR(a.gmt_modified, 'yyyy-MM-dd') = u_current_day
    );

    -- 2.新增数据

    INSERT INTO
        dwr_order.dwd_order_t_repair_event_f (    -- 环节事件表
        event_id,    --事件id
        event_type,    --事件类型
        event_name,    --事件名称
        event_time,    --事件时间
        event_user_id,    --事件处理人id
        event_euser_id,    --事件处理人id（第三方）
        event_user_name,    --事件处理人姓名
        target_user_id,    --目标用户id
        target_euser_id,    --目标用户id（第三方）
        target_user_name,    --目标用户姓名
        track_id,    --所属环节
        plan_relieve_time,    --预计解挂时间
        remark,    --事件描述
        hang_status,    --挂起结果
        fact_relieve_hang,    --实际解挂时间
        review_advise,    --挂起审核意见
        comment_type,    --类型
        photo,    --追记照片
        sup_flag,    --督办级别
        create_time,    --创建时间
        sige_track_id,    --四格环节ID
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.event_id AS event_id,
        a.event_type AS event_type,
        a.event_name AS event_name,
        a.event_time AS event_time,
        a.event_user_id AS event_user_id,
        a.event_euser_id AS event_euser_id,
        a.event_user_name AS event_user_name,
        a.target_user_id AS target_user_id,
        a.target_euser_id AS target_euser_id,
        a.target_user_name AS target_user_name,
        a.track_id AS track_id,
        a.plan_relieve_time AS plan_relieve_time,
        a.remark AS remark,
        a.hang_status AS hang_status,
        a.fact_relieve_hang AS fact_relieve_hang,
        a.review_advise AS review_advise,
        a.comment_type AS comment_type,
        a.photo AS photo,
        a.sup_flag AS sup_flag,
        a.create_time AS create_time,
        a.sige_track_id AS sige_track_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_repair_event_info a    -- 事件
    WHERE  TO_CHAR(a.gmt_modified, 'yyyy-MM-dd') = u_current_day;

END;