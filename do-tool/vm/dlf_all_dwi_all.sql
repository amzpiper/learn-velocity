DECLARE
-- ${dlf_script_dic} ${dlf_script_name} ${dlf_script_des}
--    目的：    全量分区 增量采集dwi层 ${table.fromTableNameDes} 插入到dwd层中 ${table.des} 表中，同时对数据进行清洗、去重
--    目标表：  表名：${table.name} ,   schema : ${table.sechma},   中文名：${table.des}；
--    源表：  表名：${table.fromTableName},   schema : ${table.fromSechma},  中文名：${table.fromTableNameDes}；
--    作者：  ${author}
--    创建日期：  ${script_create_date}
--    审核人：  ${author}
--    审核时间：  ${script_create_date}
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''${table.sechma}'',''${table.name}'',''dw_creation_date'',''p'',current_partition_name)');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM ${dwrTableName}  -- ${table.des}
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND record_id in (
        SELECT a.id
        FROM ${dwiTableName} a     -- ${table.fromTableNameDes}
        JOIN ${dwrTableName} b     -- ${table.des}
        ON (a.id = b.record_id)
        WHERE TO_CHAR(a.${adtecDwiTimestampField}, 'yyyy-MM-dd') = u_current_day
    );

    -- 2.新增数据

    INSERT INTO
        ${dwrTableName} (    -- ${table.des}
#foreach($filed in $fileds)
        $filed.name,    --$filed.cnName
#end
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
#foreach($filed in $fileds)
        a.$filed.fromName AS $filed.name,
#end
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  ${dwiTableName} a    -- ${table.fromTableNameDes}
    WHERE  TO_CHAR(a.u_current_day, 'yyyy-MM-dd') = u_current_day;

END;