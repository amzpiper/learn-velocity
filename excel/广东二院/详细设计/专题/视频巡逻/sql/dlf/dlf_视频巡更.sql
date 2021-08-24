DECLARE
-- gdeyy script_dm_gdeyy_order_video_task_m_f_dwr_patrol 汇聚主题库：视频巡更
--    目的：    全量分区 增量采集dwi层 视频巡更 插入到dwd层中 视频巡更 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_order_video_task_m_f ,   schema : dm_gdeyy_order,   中文名：视频巡更；
--    源表：  表名：dwd_patrol_patrol_task_detail_f,   schema : dwr_patrol,  中文名：视频巡更；
--    作者：  jiahj
--    创建日期：  2021-01-25
--    审核人：  jiahj
--    审核时间：  2021-01-25
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    u_current_day_start_time      VARCHAR;
    u_current_day_end_time      VARCHAR;
BEGIN

        u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;
        u_current_day_start_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') - INTERVAL '1 month','yyyy-MM-01');
        u_current_day_end_time = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd hh24:mi:ss') + INTERVAL '1 month','yyyy-MM-01');

    
    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_order'',''dm_gdeyy_order_video_task_m_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DM表中近两天的数据
    DELETE FROM dm_gdeyy_order.dm_gdeyy_order_video_task_m_f  -- -- 视频巡更
    WHERE statistical_time >=  u_current_day_start_time and  statistical_time < u_current_day_end_time;

    -- 2.新增数据
      -- 2.1上一月
    INSERT INTO
        dm_gdeyy_order.dm_gdeyy_order_video_task_m_f (    -- -- 视频巡更
        record_id,    --顺序号
        statistical_time,
        statistical_number,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '' as record_id,
        to_timestamp(TO_CHAR(a.begin_date, 'yyyy-MM-01 00:00:00')) statistical_time,
        count(a.begin_date) statisticalnumber,
        '${job_name}',
         u_current_day,
        '${job_name}',
        '${job_plan_time}',
        ''
    FROM  dwr_patrol.dwd_patrol_patrol_task_detail_f a    -- 视频巡更
    WHERE  a.begin_date >=  u_current_day_start_time and  a.begin_date < u_current_day_end_time
  GROUP BY TO_CHAR(a.begin_date, 'yyyy-MM-01 00:00:00');

END;