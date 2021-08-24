DECLARE
-- gdeyy script_dm_gdeyy_energy_electric_m_f_dm_gdeyy_energy 汇聚主题库：用电数据汇总（月）
--    目的：    全量分区 增量采集dwi层 用电数据汇总（天） 插入到dwd层中 用电数据汇总（月） 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_energy_electric_m_f ,   schema : dm_gdeyy_energy,   中文名：用电数据汇总（月）；
--    源表：  表名：dm_gdeyy_energy_electric_d_f,   schema : dm_gdeyy_energy,  中文名：用电数据汇总（天）；
--    作者：  lixj
--    创建日期：  2021-01-22
--    审核人：  lixj
--    审核时间：  2021-01-22
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    u_current_day_start_time      VARCHAR;
    u_current_day_end_time      VARCHAR;
BEGIN

        u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
        u_current_day_start_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM-01');
        u_current_day_end_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') + INTERVAL '1 month','yyyy-MM-01');
        

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_energy'',''dm_gdeyy_energy_electric_d_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中近两月的数据
    DELETE FROM dm_gdeyy_energy.dm_gdeyy_energy_electric_m_f  -- 用电数据汇总（月）
    WHERE acquisition_time >=  u_current_day_start_time and  acquisition_time <  u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        dm_gdeyy_energy.dm_gdeyy_energy_electric_m_f (    -- 用电数据汇总（月）
        record_id,    --顺序号
        acquisition_time,
        acquisition_device,
        building,
        floor,
        consumption,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-MM-01 00:00:00')) acquisition_time,
        acquisition_device,
        building,
        floor,
        sum(consumption) consumption,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  dm_gdeyy_energy.dm_gdeyy_energy_electric_d_f a    -- 用电数据汇总（天）
    WHERE  a.acquisition_time >=  u_current_day_start_time and  a.acquisition_time <  u_current_day_end_time
    group by building, floor,acquisition_device, TO_CHAR(acquisition_time, 'yyyy-MM-01 00:00:00')
    ;

END;