DECLARE
-- energy script_dwd_energy_electric_data_f_ecm 汇聚主题库：用电信息
--    目的：  增量分区 增量采集dwi层 用电信息 插入到dwd层中 用电信息 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_energy_electric_data_f ,   schema : dwr_energy,   中文名：用电信息；
--    源表：  表名：dwi_ecm_electric_data,   schema : dwi_ecm,  中文名：用电信息；
--    作者：  guoyh
--    创建日期：  2021-01-26
--    审核人：  guoyh
--    审核时间：  2021-01-26
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    --SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') - INTERVAL '29 min','yyyy-MM-dd');    --15分钟运行一次 当前时间减去29分钟，解决跨天问题

    --增量更新

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_energy'',''dwd_energy_electric_data_f'',''dw_creation_date'',''p'',current_partition_name)');


    --去重处理，删除数据
    DELETE  FROM  dwr_energy.dwd_energy_electric_data_f  -- 用电信息
           WHERE  dw_creation_date =  TO_TIMESTAMP(u_current_day , 'yyyy-MM-dd');

     -- 2.新增数据

    INSERT INTO
        dwr_energy.dwd_energy_electric_data_f (  -- 用电信息
        record_id,    --顺序号
        device_id,    --设备编号
        collect_time,    --采集时间
        voltage,    --电压
        current,    --电流
        power,    --功率
        power_factor,    --功率因数
        electric_quantity,    --电量
        power_quality,    --电能质量
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.id AS record_id,
--        a.device_code AS device_id,
        f.device_id AS device_id,
        a.collect_time AS collect_time,
        a.voltage AS voltage,
        a.current AS current,
        a.power AS power,
        a.power_factor AS power_factor,
        a.electric_quantity AS electric_quantity,
        a.power_quality AS power_quality,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM       dwi_ecm.dwi_ecm_electric_data a    -- 用电信息
    left join dwr_dim.dim_facility_instance_d f on f.external_code=a.device_code    -- 设备id from 主题设备表
    WHERE       TO_CHAR(a.collect_time, 'yyyy-MM-dd') = u_current_day;

END;