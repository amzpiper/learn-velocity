DECLARE
-- access script_dwd_access_t_pass_data_f_bm 汇聚主题库：通行信息表
--    目的：  增量分区 增量采集dwi层 通行 插入到dwd层中 通行信息表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_access_t_pass_data_f ,   schema : dwr_access,   中文名：通行信息表；
--    源表：  表名：dwi_bm_t_pass_data_info,   schema : dwi_bm,  中文名：通行；
--    作者：  renyi
--    创建日期：  2021-05-14
--    审核人：  renyi
--    审核时间：  2021-05-14
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    u_current_day_start_time      VARCHAR;
    u_current_day_end_time      VARCHAR;
BEGIN

        u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
        u_current_day_start_time = TO_CHAR((to_date(u_current_day)), 'yyyy-MM-dd 00:00:00');
        u_current_day_end_time = TO_CHAR((to_date(u_current_day_input)), 'yyyy-MM-dd 23:59:59');

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_energy'',''dm_gdeyy_energy_electric_type_d_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中近两天的数据
    DELETE FROM dwr_access.dwd_access_t_pass_data_f  -- -- 通行信息表
    WHERE acquisition_time >=  u_current_day_start_time and  acquisition_time <=  u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        dwr_access.dwd_access_t_pass_data_f (    -- -- 通行信息表
        record_id,    --顺序号
        acquisition_time,
        type,
        consumption,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-MM-dd 00:00:00')) acquisition_time,
        type,
        sum(consumption) consumption,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  dwi_bm.dwi_bm_t_pass_data_info a    -- 通行
    WHERE  a.acquisition_time >=  u_current_day_start_time and  a.acquisition_time <=  u_current_day_end_time
    group by type,TO_CHAR(acquisition_time, 'yyyy-MM-dd 00:00:00')
    ;

END;