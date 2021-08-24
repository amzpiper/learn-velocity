DECLARE
-- gdeyy script_dm_gdeyy_device_status_f_dm_gdeyy_device 汇聚主题库：设备状态情况
--    目的：    全量分区 增量采集dwi层 设备表 插入到dwd层中 设备状态情况 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_device_status_f ,   schema : dm_gdeyy_device,   中文名：设备状态情况；
--    源表：  表名：dm_gdeyy_device_f,   schema : dm_gdeyy_device,  中文名：设备表；
--    作者：  lixj
--    创建日期：  2021-01-28
--    审核人：  lixj
--    审核时间：  2021-01-28
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    u_current_day_start_time      VARCHAR;
    u_current_day_end_time      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    --   EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_company'',''dm_company_enterprise_info_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中跑批当天的数据
    DELETE FROM dm_gdeyy_device.dm_gdeyy_device_status_f;  -- 企业基本信息
    --      WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');

    -- 2.新增数据

    INSERT INTO dm_gdeyy_device.dm_gdeyy_device_status_f (    -- -- 设备状态情况
        record_id,    --顺序号
        device_name,
        device_address,
        device_state,
        status_code,
        incident_code,
        level_code,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.record_id,
        a.device_name,
        a.device_address,
        a.device_state,
        '正常',
        null,
        '未告警',
         --作业名称
        '${job_name}' dw_creation_by,
        --作业调度时间
        '${job_plan_time}' :: timestamp dw_creation_date,
        --作业名称
        '${job_name}' dw_last_update_by,
        --初始值为作业调度时间
        '${job_plan_time}' :: timestamp dw_last_update_date,
        to_char(now(), 'yyyyMMddHH24MISS') dw_batch_number
    FROM  dm_gdeyy_device.dm_gdeyy_device_f a;    -- 设备表
        
    update dm_gdeyy_device.dm_gdeyy_device_status_f a 
    set a.status_code=b.status_code,
    a.incident_code=b.incident_code,
    a.level_code=b.level_code
    from dwr_event.dwd_event_incident_records_f b
    where a.record_id=b.device_key;

END;