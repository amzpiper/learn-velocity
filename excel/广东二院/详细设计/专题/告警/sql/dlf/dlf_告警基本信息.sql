DECLARE
-- gdeyy script_dm_gdeyy_alarm_info_f_dwr_event 汇聚主题库：告警基本信息
--    目的：    全量分区 增量采集dwi层 告警表 插入到dwd层中 告警基本信息 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_alarm_info_f ,   schema : dm_gdeyy_alarm,   中文名：告警基本信息；
--    源表：  表名：dwd_event_incident_records_f,   schema : dwr_event,  中文名：告警表；
--    作者：  lixj
--    创建日期：  2021-01-25
--    审核人：  lixj
--    审核时间：  2021-01-25
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    --   EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_company'',''dm_company_enterprise_info_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中跑批当天的数据
    DELETE FROM dm_gdeyy_alarm.dm_gdeyy_alarm_info_f;  -- 企业基本信息
    --      WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 2.新增数据

    insert into dm_gdeyy_alarm.dm_gdeyy_alarm_info_f(
        record_id,
        acquisition_time,
        device_id,
        device_name,
        device_address,
        alarm_status,
        alarm_data,
        alarm_source_sys,
        alarm_type,
        alarm_level,
        alarm_longitude,
        alarm_latitude,
        occurrence_place,
        alarm_mode,
        scene_photo,
        video,
        consumption,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number)
    select 
        a.incident_code,
        to_char(now(),'yyyy-MM-dd HH:mm:ss'),
        a.device_key,
        b.device_name, 
        b.device_address,
        case when a.status_code='CREATED' then '已创建'
		   when a.status_code='CONFIRMED' then '已确认'
		   when a.status_code='CLOSED' then '已关闭'
		   when a.status_code='PENDING' then '挂起中'
		   when a.status_code='CANCELED' then '取消' end
		   as status_name_cn,
        a.occur_date,
        case when c.def_category_name='消防告警' then '消防'
			when c.def_category_name='安全告警' then '安防'
			else c.def_category_name end
			as def_category_name, 
        e.def_name,  
		case when level_code='INFO' then '提示'
		 when level_code='NORMAL' then '普通'
		 when level_code='MAJOR' then '重要'
		 when level_code='CRITICAL' then '严重'end
		 as level_name,
        a.longitude,
        a.latitude,
        b.device_address,
        a.incident_code,
        null,--scene_photo dwi_alarm.dwi_alarm_attachment/dwi_alarm_attachment_history
        null,--video dwi_alarm.dwi_alarm_attachment/dwi_alarm_attachment_history
        b.device_state,  
        --作业名称
        '${job_name}' dw_creation_by,
        --作业调度时间
        '${job_plan_time}' :: timestamp dw_creation_date,
        --作业名称
        '${job_name}' dw_last_update_by,
        --初始值为作业调度时间
        '${job_plan_time}' :: timestamp dw_last_update_date,
        to_char(now(), 'yyyyMMddHH24MISS') dw_batch_number
    from dwr_event.dwd_event_incident_records_f a
    left join dwr_dim.dim_alarm_def_category_d c on a.incident_type=c.def_category_code
    left join dm_gdeyy_device.dm_gdeyy_device_f b on a.device_key=b.record_id
	left join dwr_dim.dim_alarm_def_alarm_d e on a.incident_subtype=e.alarm_code;

END;