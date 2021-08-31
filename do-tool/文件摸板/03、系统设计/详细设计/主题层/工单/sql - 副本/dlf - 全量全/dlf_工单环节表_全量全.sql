DECLARE
-- order script_dwd_order_t_repair_track_f_bm 汇聚主题库：工单环节表
--    目的：    全量分区 增量采集dwi层 环节 插入到dwd层中 工单环节表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repair_track_f ,   schema : dwr_order,   中文名：工单环节表；
--    源表：  表名：dwi_bm_t_repair_track_info,   schema : dwi_bm,  中文名：环节；
--    作者：  renyi
--    创建日期：  2021-05-14
--    审核人：  renyi
--    审核时间：  2021-05-14
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day=TO_CHAR((to_date(u_current_day_input)), 'yyyy-MM-dd');

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repair_track_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_order.dwd_order_t_repair_track_f  -- 工单环节表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 跑批将前一天的数据插入到目标表
    INSERT INTO
        dwr_order.dwd_order_t_repair_track_f (    -- 工单环节表
        track_id,    --环节主键
        track_name,    --环节名称
        status_name,    --状态名称
        track_code,    --环节编码
        status,    --环节状态
        order_id,    --工单id
        track_desc,    --轨迹描述
        current_user_id,    --处理人ID（四格）
        current_euser_id,    --第三方处理人ID（四格）
        current_user_name,    --待处理人员姓名
        current_user_type,    --待处理人员类型
        current_user_tel,    --待处理人员电话（四格）
        review_result,    --审核结果
        create_time,    --创建时间
        create_by,    --创建人
        update_time,    --更新时间
        update_by,    --更新人
        version,    --版本 锁
        sige_track_id,    --四格环节ID
        sige_order_id,    --四格服务工单id
        over_type,    --工单结束类型
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.track_id  AS track_id,
        a.track_name  AS track_name,
        a.status_name  AS status_name,
        a.track_code  AS track_code,
        a.status  AS status,
        a.order_id  AS order_id,
        a.track_desc  AS track_desc,
        a.current_user_id  AS current_user_id,
        a.current_euser_id  AS current_euser_id,
        a.current_user_name  AS current_user_name,
        a.current_user_type  AS current_user_type,
        a.current_user_tel  AS current_user_tel,
        a.review_result  AS review_result,
        a.create_time  AS create_time,
        a.create_by  AS create_by,
        a.update_time  AS update_time,
        a.update_by  AS update_by,
        a.version  AS version,
        a.sige_track_id  AS sige_track_id,
        a.sige_order_id  AS sige_order_id,
        a.over_type  AS over_type,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM
        dwr_order.dwd_order_t_repair_track_f a    -- 工单环节表
    WHERE
        a.dw_creation_date = (to_timestamp(u_current_day, 'yyyy-MM-dd') + interval '-1 D');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM dwr_order.dwd_order_t_repair_track_f  -- 工单环节表
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND track_id in (
        SELECT a.track_id
        FROM dwi_bm.dwi_bm_t_repair_track_info a     -- 环节
        JOIN dwr_order.dwd_order_t_repair_track_f b     -- 工单环节表
        ON (a.track_id = b.track_id)
        WHERE TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day
    );

    -- 2.新增数据

    INSERT INTO
        dwr_order.dwd_order_t_repair_track_f (    -- 工单环节表
        track_id,    --环节主键
        track_name,    --环节名称
        status_name,    --状态名称
        track_code,    --环节编码
        status,    --环节状态
        order_id,    --工单id
        track_desc,    --轨迹描述
        current_user_id,    --处理人ID（四格）
        current_euser_id,    --第三方处理人ID（四格）
        current_user_name,    --待处理人员姓名
        current_user_type,    --待处理人员类型
        current_user_tel,    --待处理人员电话（四格）
        review_result,    --审核结果
        create_time,    --创建时间
        create_by,    --创建人
        update_time,    --更新时间
        update_by,    --更新人
        version,    --版本 锁
        sige_track_id,    --四格环节ID
        sige_order_id,    --四格服务工单id
        over_type,    --工单结束类型
		
		deal_advise,    --处理意见
		deal_photos,    --处理拍照
		deal_sign_photos,    --签名照片
			
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.track_id AS track_id,
        a.track_name AS track_name,
        a.status_name AS status_name,
        a.track_code AS track_code,
        a.status AS status,
        a.order_id AS order_id,
        a.track_desc AS track_desc,
        a.current_user_id AS current_user_id,
        a.current_euser_id AS current_euser_id,
        a.current_user_name AS current_user_name,
        a.current_user_type AS current_user_type,
        a.current_user_tel AS current_user_tel,
        a.review_result AS review_result,
        a.create_time AS create_time,
        a.create_by AS create_by,
        a.update_time AS update_time,
        a.update_by AS update_by,
        a.version AS version,
        a.sige_track_id AS sige_track_id,
        a.sige_order_id AS sige_order_id,
        a.over_type AS over_type,
		
		b.deal_advise AS deal_advise,
		b.deal_photos AS deal_photos,
		b.deal_sign_photos AS deal_sign_photos,
		
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_repair_track_info a    -- 环节
    left join dwi_bm.dwi_bm_t_repair_track_detail_info b on a.track_id = b.track_id
    WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;