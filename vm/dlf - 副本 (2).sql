-- ${dlf_script_dic} ${dlf_script_name} ${dlf_script_des}
DECLARE
    records            BIGINT;
    u_last_update_time TIMESTAMP;
    u_num              BIGINT;
    u_current_day      VARCHAR;
    current_partition_name      VARCHAR;
BEGIN

    u_current_day = '${current_day}';
    SELECT public.verification_job_current_day(u_current_day) INTO u_current_day;


    --查询出dwr表中是否有数据,没有则全量插入
    SELECT count(1), max(marking_update)
    FROM ${dwrTableName} INTO records,u_last_update_time;

    IF (records = 0)
    THEN

        --创建临时表，简化SQL
        create temp table temp_max_all_${dwiTableNameNoSechma}
        as
        SELECT
            b.id,
            MAX(b.marking_update) AS marking_update
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
            dw_batch_number,
            dw_data_partition_date
        )
        SELECT
               a.id,
               a.enterprise_name,
               a.enterprise_type,
               a.is_filiale,
               a.province,
               a.city,
               a.area,
               a.used_name,
               a.source_way,
               a.source_type,
               a.enterprise_population,
               a.investment_attraction_mode,
               a.investment_attraction_scale,
               a.belong_to_zone,
               a.belong_to_industry,
               a.industry_level,
               a.industry_broad_heading,
               a.industry_subclass,
               a.establish_date,
               a.enter_zone_year,
               a.is_operate,
               a.is_regulatory_industry,
               a.is_big_business,
               a.registered_capital,
               a.registered_type,
               a.credit_code,
               a.legal_person,
               a.legal_person_phone,
               a.legal_person_idcard,
               a.stock_name,
               a.stock_code,
               a.listing,
               a.president,
               a.president_phone,
               a.purchasing_person,
               a.purchasing_person_phone,
               a.sale_person,
               a.sale_person_phone,
               a.project_apply_person,
               a.project_apply_person_phone,
               a.enterprise_registered_address,
               a.enterprise_address,
               a.constract_appendix,
               a.investment_attraction_manager,
               a.marking_update,
               '${job_name}',
               '${job_plan_time}',
               '${job_name}',
               '${job_plan_time}',
               a.dw_batch_number,
               u_current_day
        FROM
            ${dwiTableName} a
                JOIN
                    temp_max_all_${dwiTableNameNoSechma} x
                        ON (
                            a.id = x.id
                            AND a.marking_update = x.marking_update
                        );

    ELSE

        -- 删除DWR表中当天的数据
        DELETE
        FROM ${dwrTableName}
        WHERE TO_CHAR(dw_data_partition_date, 'yyyy-MM-dd') = u_current_day;

        -- 将昨天的数据复制成当天的数据
        INSERT INTO
            ${dwrTableName} (
            record_id,
            enterprise_name,
            enterprise_type,
            is_filiale,
            province,
            city,
            area,
            used_name,
            source_way,
            source_type,
            enterprise_population,
            investment_attraction_mode,
            investment_attraction_scale,
            belong_to_zone,
            belong_to_industry,
            industry_level,
            industry_broad_heading,
            industry_subclass,
            establish_date,
            enter_zone_year,
            is_operate,
            is_regulatory_industry,
            is_big_business,
            registered_capital,
            registered_type,
            credit_code,
            legal_person,
            legal_person_phone,
            legal_person_idcard,
            stock_name,
            stock_code,
            listing,
            president,
            president_phone,
            purchasing_person,
            purchasing_person_phone,
            sale_person,
            sale_person_phone,
            project_apply_person,
            project_apply_person_phone,
            enterprise_registered_address,
            enterprise_address,
            constract_appendix,
            investment_attraction_manager,
            marking_update,
            dw_creation_by,
            dw_creation_date,
            dw_last_update_by,
            dw_last_update_date,
            dw_batch_number,
            dw_data_partition_date
        )
        SELECT
            record_id,
            enterprise_name,
            enterprise_type,
            is_filiale,
            province,
            city,
            area,
            used_name,
            source_way,
            source_type,
            enterprise_population,
            investment_attraction_mode,
            investment_attraction_scale,
            belong_to_zone,
            belong_to_industry,
            industry_level,
            industry_broad_heading,
            industry_subclass,
            establish_date,
            enter_zone_year,
            is_operate,
            is_regulatory_industry,
            is_big_business,
            registered_capital,
            registered_type,
            credit_code,
            legal_person,
            legal_person_phone,
            legal_person_idcard,
            stock_name,
            stock_code,
            listing,
            president,
            president_phone,
            purchasing_person,
            purchasing_person_phone,
            sale_person,
            sale_person_phone,
            project_apply_person,
            project_apply_person_phone,
            enterprise_registered_address,
            enterprise_address,
            constract_appendix,
            investment_attraction_manager,
            marking_update,
            '${job_name}',
            '${job_plan_time}',
            '${job_name}',
            '${job_plan_time}',
            a.dw_batch_number,
            u_current_day
        FROM ${dwrTableName} a
        WHERE TO_CHAR((dw_data_partition_date + interval '1 D'), 'yyyy-MM-dd') = u_current_day;

        --查询出dwi表中是否有新增数据,有则增量更新
        SELECT count(1)
        FROM ${dwiTableName}
        WHERE TO_CHAR(marking_update, 'yyyy-MM-dd') = u_current_day INTO u_num;

        IF (u_num <> 0)
        THEN

            -- 删除DWR表中待修改的数据
            DELETE FROM
                ${dwrTableName}
            WHERE
                    TO_CHAR(dw_data_partition_date, 'yyyy-MM-dd') = u_current_day
              AND record_id in (
                SELECT
                    a.id
                FROM
                    ${dwiTableName} a
                        JOIN ${dwrTableName} b ON (a.id = b.record_id)
                WHERE
                        TO_CHAR(a.marking_update, 'yyyy-MM-dd') = u_current_day
            );

            -- 2.新增数据

            --创建临时表，简化SQL
            create temp table temp_max_current_${dwiTableNameNoSechma}
            as
            SELECT
                b.id,
                MAX(b.marking_update) AS marking_update
            FROM
                ${dwiTableName} b
            WHERE
                    TO_CHAR(b.marking_update, 'yyyy-MM-dd') = u_current_day
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
                dw_batch_number,
                dw_data_partition_date
            )
            SELECT a.id,
                    a.enterprise_name,
                    a.enterprise_type,
                    a.is_filiale,
                    a.province,
                    a.city,
                    a.area,
                    a.used_name,
                    a.source_way,
                    a.source_type,
                    a.enterprise_population,
                    a.investment_attraction_mode,
                    a.investment_attraction_scale,
                    a.belong_to_zone,
                    a.belong_to_industry,
                    a.industry_level,
                    a.industry_broad_heading,
                    a.industry_subclass,
                    a.establish_date,
                    a.enter_zone_year,
                    a.is_operate,
                    a.is_regulatory_industry,
                    a.is_big_business,
                    a.registered_capital,
                    a.registered_type,
                    a.credit_code,
                    a.legal_person,
                    a.legal_person_phone,
                    a.legal_person_idcard,
                    a.stock_name,
                    a.stock_code,
                    a.listing,
                    a.president,
                    a.president_phone,
                    a.purchasing_person,
                    a.purchasing_person_phone,
                    a.sale_person,
                    a.sale_person_phone,
                    a.project_apply_person,
                    a.project_apply_person_phone,
                    a.enterprise_registered_address,
                    a.enterprise_address,
                    a.constract_appendix,
                    a.investment_attraction_manager,
                    a.marking_update,
                    '${job_name}',
                    '${job_plan_time}',
                    '${job_name}',
                    '${job_plan_time}',
                    a.dw_batch_number,
                    u_current_day
            FROM
                ${dwiTableName} a
                    JOIN
                        temp_max_current_${dwiTableNameNoSechma} x
                            ON (
                                a.id = x.id
                                AND a.marking_update = x.marking_update
                            )
            WHERE
                    TO_CHAR(a.marking_update, 'yyyy-MM-dd') = u_current_day;

        END IF;
    END IF;

    --创建分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''${table.sechma}'',''${table.name}'',''dw_data_partition_date'',''p'',current_partition_name)');

END;