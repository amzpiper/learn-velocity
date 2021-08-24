DECLARE
-- gdeyy script_dm_gdeyy_hospital_income_m_f_dwr_hospital 汇聚主题库：月度医疗收入_药品收入趋势分析
--    目的：    全量分区 增量采集dwi层 月度医疗收入_药品收入 插入到dwd层中 月度医疗收入_药品收入趋势分析 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_hospital_income_m_f ,   schema : dm_gdeyy_hospital,   中文名：月度医疗收入_药品收入趋势分析；
--    源表：  表名：dwd_hospital_hospital_income_f,   schema : dwr_hospital,  中文名：月度医疗收入_药品收入；
--    作者：  guoyh
--    创建日期：  2021-01-23
--    审核人：  guoyh
--    审核时间：  2021-01-23
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    u_current_day_start_time      VARCHAR;
    u_current_day_end_time      VARCHAR;
BEGIN

        u_current_day_input = '${current_day}';
        SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
        u_current_day_start_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') - INTERVAL '1 month','yyyy-MM-01');
        u_current_day_end_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') + INTERVAL '1 month','yyyy-MM-01');
    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_hospital'',''dm_gdeyy_hospital_income_m_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中近两天的数据
    DELETE FROM dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f;  -- -- 月度医疗收入_药品收入趋势分析
    --WHERE acquisition_time >=  u_current_day_start_time and  acquisition_time < u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f (    -- -- 月度医疗收入_药品收入趋势分析
        record_id,    --顺序号
        acquisition_time,
        medical_income,
        medicine_income,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-01-01 00:00:00')) acquisition_time,
        a.medical_income as medical_income,
        a.medicine_income as medicine_income,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  dwr_hospital.dwd_hospital_hospital_income_f a    -- 月度医疗收入_药品收入
    WHERE       TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = TO_CHAR(now(), 'yyyy-01-01');
    --group by TO_CHAR(acquisition_time, 'yyyy-MM-01 00:00:00')
    

END;