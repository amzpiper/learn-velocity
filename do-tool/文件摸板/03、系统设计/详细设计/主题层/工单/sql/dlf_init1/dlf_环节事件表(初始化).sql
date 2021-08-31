DECLARE
-- order script_init_dwd_order_t_repair_event_f_bm 汇聚主题库：环节事件表
--    目的：    全量分区  初始化采集dwi层 事件 插入到dwd层中 环节事件表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repair_event_f ,   schema : dwr_order,   中文名：环节事件表；
--    源表：  表名：dwi_bm_t_repair_event_info,   schema : dwi_bm,  中文名：事件；
--    作者：  lihq
--    创建日期：  2021-08-31
--    审核人：  lihq
--    审核时间：  2021-08-31
--    历史修改记录：  修改时间      修改作者         备注


    records            BIGINT;
    u_num              BIGINT;
    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    --查询出dwr表中是否有数据,没有则全量插入
    SELECT
        COUNT(1)
    FROM
        dwr_order.dwd_order_t_repair_event_f INTO records;

    IF (records = 0)
    THEN

         --创建分区
        EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repair_event_f'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表，简化SQL
        CREATE temp TABLE temp_max_all_dwi_bm_t_repair_event_info
        AS
        SELECT
            b.id,
            MAX(b.gmt_modified) AS gmt_modified
        FROM
            dwi_bm.dwi_bm_t_repair_event_info b
        GROUP BY
            b.id;


        INSERT INTO
            dwr_order.dwd_order_t_repair_event_f (
            event_id, --事件id
            event_type, --事件类型
            event_name, --事件名称
            event_time, --事件时间
            event_user_id, --事件处理人id
            event_euser_id, --事件处理人id（第三方）
            event_user_name, --事件处理人姓名
            target_user_id, --目标用户id
            target_euser_id, --目标用户id（第三方）
            target_user_name, --目标用户姓名
            track_id, --所属环节
            plan_relieve_time, --预计解挂时间
            remark, --事件描述
            hang_status, --挂起结果
            fact_relieve_hang, --实际解挂时间
            review_advise, --挂起审核意见
            comment_type, --类型
            photo, --追记照片
            sup_flag, --督办级别
            create_time, --创建时间
            sige_track_id, --四格环节ID
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
           TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
           a.dw_batch_number

        FROM
            dwi_bm.dwi_bm_t_repair_event_info a
                JOIN
                    temp_max_all_dwi_bm_t_repair_event_info x
                        ON (
                            a.id = x.id
                            AND a.gmt_modified = x.gmt_modified
                        )
                WHERE
                    TO_CHAR(a.gmt_modified, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;