DECLARE
-- ${dlf_script_dic} ${dlf_script_name} ${dlf_script_des}
--    目的：  增量分区 增量采集dwi层 ${table.fromTableNameDes} 插入到dwd层中 ${table.des} 表中，同时对数据进行清洗、去重
--    目标表：  表名：${table.name} ,   schema : ${table.sechma},   中文名：${table.des}；
--    源表：  表名：${table.fromTableName},   schema : ${table.fromSechma},  中文名：${table.fromTableNameDes}；
--    作者：  ${author}
--    创建日期：  ${script_create_date}
--    审核人：  ${author}
--    审核时间：  ${script_create_date}
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
    DELETE FROM ${dwrTableName}  -- -- ${table.des}
    WHERE acquisition_time >=  u_current_day_start_time and  acquisition_time <=  u_current_day_end_time;

    -- 2.新增数据

    INSERT INTO
        ${dwrTableName} (    -- -- ${table.des}
        record_id,    --顺序号
        acquisition_time,
        type,
        consumption,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' AS record_id,
        to_date(TO_CHAR(a.acquisition_time, 'yyyy-MM-dd 00:00:00')) acquisition_time,
        type,
        sum(consumption) consumption,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  ${dwiTableName} a    -- ${table.fromTableNameDes}
    WHERE  a.acquisition_time >=  u_current_day_start_time and  a.acquisition_time <=  u_current_day_end_time
    group by type,TO_CHAR(acquisition_time, 'yyyy-MM-dd 00:00:00')
    ;

END;