DECLARE
-- order script_init_dwd_order_t_repair_track_f_bm 汇聚主题库：工单环节表
--    目的：    全量分区  初始化采集dwi层 环节 插入到dwd层中 工单环节表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repair_track_f ,   schema : dwr_order,   中文名：工单环节表；
--    源表：  表名：dwi_bm_t_repair_track_info,   schema : dwi_bm,  中文名：环节；
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
        dwr_order.dwd_order_t_repair_track_f INTO records;

    IF (records = 0)
    THEN

         --创建分区
        EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repair_track_f'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表，简化SQL
        CREATE temp TABLE temp_max_all_dwi_bm_t_repair_track_info
        AS
        SELECT
            b.id,
            MAX(b.gmt_modified) AS gmt_modified
        FROM
            dwi_bm.dwi_bm_t_repair_track_info b
        GROUP BY
            b.id;


        INSERT INTO
            dwr_order.dwd_order_t_repair_track_f (
            track_id, --环节主键
            track_name, --环节名称
            status_name, --状态名称
            track_code, --环节编码
            status, --环节状态
            order_id, --工单id
            track_desc, --轨迹描述
            current_euser_id, --第三方处理人ID（四格）
            current_user_type, --待处理人员类型
            current_user_tel, --待处理人员电话（四格）
            review_result, --审核结果
            create_time, --创建时间
            create_by, --创建人
            update_time, --更新时间
            update_by, --更新人
            version, --版本 锁
            sige_track_id, --四格环节ID
            sige_order_id, --四格服务工单id
            over_type, --工单结束类型
            dw_creation_by,
            dw_creation_date,
            dw_last_update_by,
            dw_last_update_date,
            dw_batch_number
        )
        SELECT
            a.track_id AS track_id,
            a.track_name AS track_name,
            a.status_name AS status_name,
            a.track_code AS track_code,
            a.status AS status,
            a.order_id AS order_id,
            a.track_desc AS track_desc,
            a.current_euser_id AS current_euser_id,
            a.current_user_type AS current_user_type,
            a.current_user_tel AS current_user_tel,
            a.review_result AS review_result,
            a.create_time AS create_time,
            a.create_by AS create_by,
            a.update_time AS update_time,
            a.update_by AS update_by,
            a.version AS version,
            a.sige_track_id AS sige_track_id,
            a.sige_order_id AS sige_order_id,
            a.over_type AS over_type,
           '${job_name}',
           u_current_day,
           '${job_name}',
           TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
           a.dw_batch_number

        FROM
            dwi_bm.dwi_bm_t_repair_track_info a
                JOIN
                    temp_max_all_dwi_bm_t_repair_track_info x
                        ON (
                            a.id = x.id
                            AND a.gmt_modified = x.gmt_modified
                        )
                WHERE
                    TO_CHAR(a.gmt_modified, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;