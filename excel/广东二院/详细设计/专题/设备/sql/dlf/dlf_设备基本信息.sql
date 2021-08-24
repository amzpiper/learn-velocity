	DECLARE
-- gdeyy script_dm_gdeyy_device_f_dwr_dim 汇聚主题库：设备基本信息
--    目的：    全量分区 增量采集dwi层 设备表 插入到dwd层中 设备基本信息 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_device_f ,   schema : dm_gdeyy_device,   中文名：设备基本信息；
--    源表：  表名：dim_facility_instance_d,   schema : dwr_dim,  中文名：设备表；
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
    DELETE FROM dm_gdeyy_device.dm_gdeyy_device_f;  -- 企业基本信息
    --      WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


--创建临时表
create temp table temp_dim_space_record_d as
     select space_key,
     case when level_code='BUILDING' then level_order end as building,
     case when level_code='FLOOR' then level_order end as floor,
     case when level_code='ROOM' then level_order end as room_number,
  detail_address from dwr_dim.dim_space_record_d;
--创建临时表
create temp table temp_dim_facility_definition_d as
  select definition_name,definition_code,definition_category_name from dwr_dim.dim_facility_definition_d;  
  
-- 2.新增数据
insert into dm_gdeyy_device.dm_gdeyy_device_f(
    record_id,
    device_code,
    device_external_system_id,
    device_name,
    device_system,
    device_type,
    device_longitude,
    device_latitude,
    building,
    floor,
    room_number,
    device_state,
    device_address,
    dw_creation_by,
    dw_creation_date,
    dw_last_update_by,
    dw_last_update_date,
    dw_batch_number
)
select
    a.device_key,
    a.device_code,
    a.external_code,
    a.device_name,
    b.definition_category_name,
    b.definition_name,
    a.longitude,
    a.latitude,
    c.building,
    c.floor,
    c.room_number,
    case when status='UNREG' then '未注册'
			 when status='ACTIVE' then '已启用'
			 when status='INACTIVE' then '未启用'
			 when status='DELETED' then '已废弃'
			end as status,
    c.detail_address,
     --作业名称
     '${job_name}' dw_creation_by,
     --作业调度时间
    '${job_plan_time}' :: timestamp dw_creation_date,
     --作业名称
     '${job_name}' dw_last_update_by,
     --初始值为作业调度时间
     '${job_plan_time}' :: timestamp dw_last_update_date,
     to_char(now(), 'yyyyMMddHH24MISS') dw_batch_number
from dwr_dim.dim_facility_instance_d a
left join temp_dim_facility_definition_d b on a.device_definition_code=b.definition_code
left join temp_dim_space_record_d c on c.space_key=a.space_code;
END;