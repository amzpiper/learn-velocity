DECLARE
-- access script_init_dwd_access_t_pass_data_f_bm 汇聚主题库：通行信息表
--    目的：     增量分区 初始化采集dwi层 通行 插入到dwd层中 通行信息表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_access_t_pass_data_f ,   schema : dwr_access,   中文名：通行信息表；
--    源表：  表名：dwi_bm_t_pass_data_info,   schema : dwi_bm,  中文名：通行；
--    作者：  renyi
--    创建日期：  2021-05-14
--    审核人：  renyi
--    审核时间：  2021-05-14
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
         dwr_access.dwd_access_t_pass_data_f INTO records;

    IF
        (records = 0)
    THEN

        --创建分区
       -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_access'',''dwd_access_t_pass_data_f'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表
        CREATE temp TABLE temp_max_all_dwi_bm_t_pass_data_info
        AS
        SELECT
            b.record_id,
            MAX(b.snap_time) AS snap_time
        FROM
            dwi_bm.dwi_bm_t_pass_data_info b
        GROUP BY
            b.record_id;

        INSERT INTO
            dwr_access.dwd_access_t_pass_data_f (
            record_id, --记录唯一标识
            code, --企业
            card, --卡号
            name, --姓名
            area, --通行区域
            snap_time, --记录时间
            direction, --进出门
            pass_type, --通行人员类型
            owner_id, --通行数据所有者主键
            dw_creation_by,
            dw_creation_date,
            dw_last_update_by,
            dw_last_update_date,
            dw_batch_number
        )
        SELECT
            a.record_id AS record_id,
            a.code AS code,
            a.card AS card,
            a.name AS name,
            a.area AS area,
            a.snap_time AS snap_time,
            a.direction AS direction,
            a.pass_type AS pass_type,
            a.owner_id AS owner_id,
            '${job_name}',
            u_current_day,
            '${job_name}',
            TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
            a.dw_batch_number
        FROM
            dwi_bm.dwi_bm_t_pass_data_info a
                JOIN
                    temp_max_all_dwi_bm_t_pass_data_info x
                        ON (
                                a.record_id = x.record_id
                                AND a.snap_time = x.snap_time
                        )
                WHERE
                        TO_CHAR(a.snap_time, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;