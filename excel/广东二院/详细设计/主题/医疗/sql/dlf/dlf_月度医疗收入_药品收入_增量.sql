DECLARE
-- hospital script_dwd_hospital_hospital_income_f_hospital 汇聚主题库：月度医疗收入_药品收入
--    目的：  增量分区 增量采集dwi层 月度医疗收入_药品收入 插入到dwd层中 月度医疗收入_药品收入 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_hospital_hospital_income_f ,   schema : dwr_hospital,   中文名：月度医疗收入_药品收入；
--    源表：  表名：dwi_hospital_hospital_income,   schema : dwi_hospital,  中文名：月度医疗收入_药品收入；
--    作者：  guoyh
--    创建日期：  2021-01-22
--    审核人：  guoyh
--    审核时间：  2021-01-22
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    --增量更新

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_hospital'',''dwd_hospital_hospital_income_f'',''dw_creation_date'',''p'',current_partition_name)');


    --去重处理，删除数据
    DELETE  FROM  dwr_hospital.dwd_hospital_hospital_income_f ; -- 月度医疗收入_药品收入
           --WHERE  dw_creation_date =  TO_TIMESTAMP(u_current_day , 'yyyy-MM-dd');

     -- 2.新增数据

    INSERT INTO
        dwr_hospital.dwd_hospital_hospital_income_f (  -- 月度医疗收入_药品收入
        record_id,    --顺序号
        acquisition_time,    --采集时间
        medical_income,    --医疗收入
        medicine_income,    --药品收入
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.id AS record_id,
        a.acquisition_time AS acquisition_time,
        a.medical_income AS medical_income,
        a.medicine_income AS medicine_income,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM       dwi_hospital.dwi_hospital_hospital_income a    -- 月度医疗收入_药品收入
    WHERE       TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = TO_CHAR(now(), 'yyyy-01-01');

END;