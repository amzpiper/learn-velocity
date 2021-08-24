DECLARE
-- gdeyy script_dm_gdeyy_hospital_patient_flow_h_f_dwr_hospital 汇聚主题库：门诊流量监控（小时）
--    目的：    全量分区 增量采集dwi层 门诊流量监控（小时） 插入到dwd层中 门诊流量监控（小时） 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_hospital_patient_flow_h_f ,   schema : dm_gdeyy_hospital,   中文名：门诊流量监控（小时）；
--    源表：  表名：dwd_hospital_hospital_patient_flow_f,   schema : dwr_hospital,  中文名：门诊流量监控（小时）；
--    作者：  guoyh
--    创建日期：  2021-01-22
--    审核人：  guoyh
--    审核时间：  2021-01-22
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
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_hospital'',''dm_gdeyy_hospital_patient_flow_h_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中近两天的数据
    DELETE FROM dm_gdeyy_hospital.dm_gdeyy_hospital_patient_flow_h_f  -- -- 门诊流量监控（小时）
    WHERE acquisition_time >=  u_current_day_start_time and  acquisition_time <=  u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        dm_gdeyy_hospital.dm_gdeyy_hospital_patient_flow_h_f (    -- -- 门诊流量监控（小时）
        record_id,    --顺序号
        acquisition_time,
        filing_person_num,
        outpatient_service_person_num,
        emergency_services_person_num,
        take_medicine_person_num,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-MM-dd HH24:00:00')) acquisition_time,
        sum(filing_person_num) sufiling_person_num,
        sum(outpatient_service_person_num) outpatient_service_person_num,
        sum(emergency_services_person_num) emergency_services_person_num,
        sum(take_medicine_person_num) take_medicine_person_num,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  dwr_hospital.dwd_hospital_hospital_patient_flow_f a    -- 门诊流量监控（小时）
    WHERE  a.acquisition_time >=  u_current_day_start_time and  a.acquisition_time <=  u_current_day_end_time
    group by TO_CHAR(acquisition_time, 'yyyy-MM-dd HH24:00:00')
    ;

END;


