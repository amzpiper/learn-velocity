DECLARE
-- gdeyy script_dm_gdeyy_park_car_traffic_h_f_park 汇聚主题库：小时车流信息
--    目的：  增量分区 增量采集dwi层 车辆入场信息 插入到dwd层中 小时车流信息 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_park_car_traffic_h_f ,   schema : dm_gdeyy_park,   中文名：小时车流信息；
--    源表：  表名：dwi_park_car_traffic_in,   schema : dwi_park,  中文名：车辆入场信息；
--    作者：  huanghh
--    创建日期：  2021-02-20
--    审核人：  huanghh
--    审核时间：  2021-02-20
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
    DELETE FROM dm_gdeyy_park.dm_gdeyy_park_car_traffic_h_f  -- -- 小时车流信息
    WHERE statistical_time >=  u_current_day_start_time  and  statistical_time <=  u_current_day_end_time and entrance_exit_signs = '进场';
	
	--创建临时表，简化SQL
	CREATE temp TABLE temp_all_dwi_park_car_traffic_in
	AS
	select
	 
		id,    --顺序号
		parkcode,    --停车场编号
		parkname,    --停车场名称
		acquisition_time,    --入场时间
		carnumber,    --车牌
		vehicleinfo,    --车辆信息
		(
		 CASE WHEN position('GREEN' in vehicleinfo)=0 THEN '汽油车' ELSE '新能源车' END
		) AS   enterprise_name,    --车辆类型
		(
		 CASE WHEN position('粤' in carnumber)=0 THEN '省外' ELSE '省内' END
		) AS  vehicle_source    --车辆来源

	from dwi_park.dwi_park_car_traffic_in
	WHERE acquisition_time >=  u_current_day_start_time  and  acquisition_time <=  u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        dm_gdeyy_park.dm_gdeyy_park_car_traffic_h_f (    -- -- 小时车流信息
        record_id,    --顺序号
        statistical_time,
        enterprise_name,
        vehicle_source,
		entrance_exit_signs,
		statistical_number,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-MM-dd hh24:00:00')) acquisition_time,
        a.enterprise_name,
		a.vehicle_source,
		'进场' as entrance_exit_signs,
        count(1) as statistical_number,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  temp_all_dwi_park_car_traffic_in a    -- 车辆入场信息
    group by a.enterprise_name,a.vehicle_source,TO_CHAR(a.acquisition_time, 'yyyy-MM-dd hh24:00:00')
    ;

END;