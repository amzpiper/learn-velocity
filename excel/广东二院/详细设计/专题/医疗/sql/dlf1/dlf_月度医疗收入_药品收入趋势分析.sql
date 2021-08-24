DECLARE
-- gdeyy script_dm_gdeyy_hospital_income_m_f_dwr_hospital 汇聚主题库：月度医疗收入_药品收入趋势分析
--    目的：    全量分区 增量采集dwi层 月度医疗收入_药品收入 插入到dwd层中 月度医疗收入_药品收入趋势分析 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_hospital_income_m_f ,   schema : dm_gdeyy_hospital,   中文名：月度医疗收入_药品收入趋势分析；
--    源表：  表名：dwd_hospital_hospital_income_f,   schema : dwr_hospital,  中文名：月度医疗收入_药品收入；
--    作者：  lixj
--    创建日期：  2021-01-29
--    审核人：  lixj
--    审核时间：  2021-01-29
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_hospital'',''dm_gdeyy_hospital_income_m_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f  -- 月度医疗收入_药品收入趋势分析
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 跑批将前一天的数据插入到目标表
    INSERT INTO
        dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f (    -- 月度医疗收入_药品收入趋势分析
        record_id,    --顺序号
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.record_id  AS record_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM
        dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f a    -- 月度医疗收入_药品收入趋势分析
    WHERE
        a.dw_creation_date = (to_timestamp(u_current_day, 'yyyy-MM-dd') + interval '-1 D');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f  -- 月度医疗收入_药品收入趋势分析
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND record_id in (
        SELECT a.id
        FROM dwr_hospital.dwd_hospital_hospital_income_f a     -- 月度医疗收入_药品收入
        JOIN dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f b     -- 月度医疗收入_药品收入趋势分析
        ON (a.id = b.record_id)
        WHERE TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = u_current_day
    );

    -- 2.新增数据

    INSERT INTO
        dm_gdeyy_hospital.dm_gdeyy_hospital_income_m_f (    -- 月度医疗收入_药品收入趋势分析
        record_id,    --顺序号
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.id AS record_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwr_hospital.dwd_hospital_hospital_income_f a    -- 月度医疗收入_药品收入
    WHERE  TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') = u_current_day;

END;