DECLARE
-- ${dlf_script_dic} ${dlf_init_script_name} ${dlf_script_des}
--    目的：     增量分区 初始化采集dwi层 ${table.fromTableNameDes} 插入到dwd层中 ${table.des} 表中，同时对数据进行清洗、去重
--    目标表：  表名：${table.name} ,   schema : ${table.sechma},   中文名：${table.des}；
--    源表：  表名：${table.fromTableName},   schema : ${table.fromSechma},  中文名：${table.fromTableNameDes}；
--    作者：  ${author}
--    创建日期：  ${script_create_date}
--    审核人：  ${author}
--    审核时间：  ${script_create_date}
--    历史修改记录：  修改时间      修改作者         备注


    records            BIGINT;
    u_num              BIGINT;
    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;


    --查询出dwr表中是否有数据,没有则全量插入
    SELECT
           COUNT(1)
    FROM
         ${dwrTableName} INTO records;

    IF
        (records = 0)
    THEN

        --创建分区
       -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''${table.sechma}'',''${table.name}'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表
        CREATE temp TABLE temp_max_all_${table.fromTableName}
        AS
        SELECT
            b.id,
            MAX(b.${adtecDwiTimestampField}) AS ${adtecDwiTimestampField}
        FROM
            ${dwiTableName} b
        GROUP BY
            b.id;

        INSERT INTO
            ${dwrTableName} (
#foreach($filed in $fileds)
            $filed.name, --$filed.cnName
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
            TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
            a.dw_batch_number
        FROM
            ${dwiTableName} a
                JOIN
                    temp_max_all_${table.fromTableName} x
                        ON (
                                a.id = x.id
                                AND a.${adtecDwiTimestampField} = x.${adtecDwiTimestampField}
                        )
                WHERE
                        TO_CHAR(a.${adtecDwiTimestampField}, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;