DECLARE
-- patrol script_dwd_patrol_patrol_task_detail_f_abc 汇聚主题库：视频巡更
--    目的：  增量分区 增量采集dwi层 视频巡更 插入到dwd层中 视频巡更 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_patrol_patrol_task_detail_f ,   schema : dwr_patrol,   中文名：视频巡更；
--    源表：  表名：dwi_abc_patrol_task_detail,   schema : dwi_abc,  中文名：视频巡更；
--    作者：  guoyh
--    创建日期：  2021-01-23
--    审核人：  guoyh
--    审核时间：  2021-01-23
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    --增量更新

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_patrol'',''dwd_patrol_patrol_task_detail_f'',''dw_creation_date'',''p'',current_partition_name)');


    --去重处理，删除数据
    DELETE  FROM  dwr_patrol.dwd_patrol_patrol_task_detail_f  -- 视频巡更
           WHERE  dw_creation_date =  TO_TIMESTAMP(u_current_day , 'yyyy-MM-dd');

     -- 2.新增数据

    INSERT INTO
        dwr_patrol.dwd_patrol_patrol_task_detail_f (  -- 视频巡更
        record_id,    --顺序号
        actual_play_time,    --实际打卡时间
        begin_date,    --开始时间
        camera_code,    --摄像头编码
        camera_name,    --摄像头名称
        end_date,    --结束时间
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.id AS record_id,
        a.actual_play_time AS actual_play_time,
        a.begin_date AS begin_date,
        a.camera_code AS camera_code,
        a.camera_name AS camera_name,
        a.end_date AS end_date,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM       dwi_abc.dwi_abc_patrol_task_detail a    -- 视频巡更
    WHERE       TO_CHAR(a.begin_date, 'yyyy-MM-dd') = u_current_day;

END;