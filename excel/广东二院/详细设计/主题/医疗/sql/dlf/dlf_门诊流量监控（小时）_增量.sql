DECLARE
-- hospital script_dwd_hospital_hospital_patient_flow_f_hospital 汇聚主题库：门诊流量监控（小时）
--    目的：  增量分区 增量采集dwi层 门诊流量监控（小时） 插入到dwd层中 门诊流量监控（小时） 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_hospital_hospital_patient_flow_f ,   schema : dwr_hospital,   中文名：门诊流量监控（小时）；
--    源表：  表名：dwi_hospital_hospital_patient_flow,   schema : dwi_hospital,  中文名：门诊流量监控（小时）；
--    作者：  guoyh
--    创建日期：  2021-01-22
--    审核人：  guoyh
--    审核时间：  2021-01-22
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    --SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') - INTERVAL '29 min','yyyy-MM-dd');    --15分钟运行一次 当前时间减去29分钟，解决跨天问题

    --增量更新

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_hospital'',''dwd_hospital_hospital_patient_flow_f'',''dw_creation_date'',''p'',current_partition_name)');


    --去重处理，删除数据
    DELETE  FROM  dwr_hospital.dwd_hospital_hospital_patient_flow_f  -- 门诊流量监控（小时）
    WHERE  dw_creation_date =  TO_TIMESTAMP(u_current_day , 'yyyy-MM-dd');

     -- 2.新增数据

    INSERT INTO
        dwr_hospital.dwd_hospital_hospital_patient_flow_f (  -- 门诊流量监控（小时）
        record_id,    --顺序号
        acquisition_time,    --采集时间
        filing_person_num,    --建档人次
        outpatient_service_person_num,    --门诊人次
        emergency_services_person_num,    --急诊人次
        take_medicine_person_num,    --取药人次
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.id AS record_id,
        a.acquisition_time AS acquisition_time,
        a.filing_person_num AS filing_person_num,
        a.outpatient_service_person_num AS outpatient_service_person_num,
        a.emergency_services_person_num AS emergency_services_person_num,
        a.take_medicine_person_num AS take_medicine_person_num,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM       dwi_hospital.dwi_hospital_hospital_patient_flow a    -- 门诊流量监控（小时）
    WHERE       TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = u_current_day;

END;