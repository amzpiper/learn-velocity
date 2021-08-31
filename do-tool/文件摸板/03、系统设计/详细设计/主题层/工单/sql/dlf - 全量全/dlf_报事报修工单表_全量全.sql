DECLARE
-- order script_dwd_order_t_repairs_order_f_bm 汇聚主题库：报事报修工单表
--    目的：    全量分区 增量采集dwi层 报事报修工单 插入到dwd层中 报事报修工单表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repairs_order_f ,   schema : dwr_order,   中文名：报事报修工单表；
--    源表：  表名：dwi_bm_t_repairs_order_info,   schema : dwi_bm,  中文名：报事报修工单；
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
    --EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repairs_order_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_order.dwd_order_t_repairs_order_f  -- 报事报修工单表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');


    -- 跑批将前一天的数据插入到目标表
    INSERT INTO
        dwr_order.dwd_order_t_repairs_order_f (    -- 报事报修工单表
        order_id,    --
        order_no,    --工单号
        order_type,    --问题类型
        addr,    --问题地点
        description,    --问题描述
        connect,    --联系方式
        connector,    --联系人
        level,    --紧急程度
        building_id,    --关联楼宇
        create_time,    --
        create_by,    --
        source_type,    --来源类型
        voice_file,    --语音描述
        order_time,    --报修时间
        order_status,    --工单状态
        version,    --版本 锁
        credit_code,    --统一社会信用代码
        sige_order_id,    --四格服务工单id
        order_create_name,    --录单人姓名
        sige_transport_user_id,    --录单人ID
        sige_transport_euser_id,    --外部录单人ID
        del_flag,    --删除标识
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        sig_community_id,    --四格项目id
        evalue_status,    --评价状态
        proj_id,    --项目id
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_id  AS order_id,
        a.order_no  AS order_no,
        a.order_type  AS order_type,
        a.addr  AS addr,
        a.description  AS description,
        a.connect  AS connect,
        a.connector  AS connector,
        a.level  AS level,
        a.building_id  AS building_id,
        a.create_time  AS create_time,
        a.create_by  AS create_by,
        a.source_type  AS source_type,
        a.voice_file  AS voice_file,
        a.order_time  AS order_time,
        a.order_status  AS order_status,
        a.version  AS version,
        a.credit_code  AS credit_code,
        a.sige_order_id  AS sige_order_id,
        a.order_create_name  AS order_create_name,
        a.sige_transport_user_id  AS sige_transport_user_id,
        a.sige_transport_euser_id  AS sige_transport_euser_id,
        a.del_flag  AS del_flag,
        a.outtime_status  AS outtime_status,
        a.sig_order_subtype  AS sig_order_subtype,
        a.sig_community_id  AS sig_community_id,
        a.evalue_status  AS evalue_status,
        a.proj_id  AS proj_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM
        dwr_order.dwd_order_t_repairs_order_f a    -- 报事报修工单表
    WHERE
        a.dw_creation_date = (to_timestamp(u_current_day, 'yyyy-MM-dd') + interval '-1 D');


    -- 去重处理，删除新增数据中包含前一天的数据
    DELETE
    FROM dwr_order.dwd_order_t_repairs_order_f 
    WHERE dw_creation_date = to_timestamp(u_current_day, 'yyyy-MM-dd')
      AND order_id in (
        SELECT a.order_id
        FROM dwi_bm.dwi_bm_t_repairs_order_info a
        JOIN dwr_order.dwd_order_t_repairs_order_f b
        ON (a.order_id = b.order_id)
        WHERE TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day
    );


    -- 2.新增数据

    INSERT INTO
        dwr_order.dwd_order_t_repairs_order_f (    -- 报事报修工单表
        order_id,    --
        order_no,    --工单号
        order_type,    --问题类型
        addr,    --问题地点
        description,    --问题描述
        connect,    --联系方式
        connector,    --联系人
        level,    --紧急程度
        building_id,    --关联楼宇
        create_time,    --
        create_by,    --
        source_type,    --来源类型
        voice_file,    --语音描述
        order_time,    --报修时间
        order_status,    --工单状态
        version,    --版本 锁
        credit_code,    --统一社会信用代码
        sige_order_id,    --四格服务工单id
        order_create_name,    --录单人姓名
        sige_transport_user_id,    --录单人ID
        sige_transport_euser_id,    --外部录单人ID
        del_flag,    --删除标识
        outtime_status,    --超时状态
        sig_order_subtype,    --四格问题子类型
        sig_community_id,    --四格项目id
        evalue_status,    --评价状态
        proj_id,    --项目id
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_id AS order_id,
        a.order_no AS order_no,
        a.order_type AS order_type,
        a.addr AS addr,
        a.description AS description,
        a.connect AS connect,
        a.connector AS connector,
        a.level AS level,
        a.building_id AS building_id,
        a.create_time AS create_time,
        a.create_by AS create_by,
        a.source_type AS source_type,
        a.voice_file AS voice_file,
        a.order_time AS order_time,
        a.order_status AS order_status,
        a.version AS version,
        b.credit_code AS credit_code,
        a.sige_order_id AS sige_order_id,
        a.order_create_name AS order_create_name,
        a.sige_transport_user_id AS sige_transport_user_id,
        a.sige_transport_euser_id AS sige_transport_euser_id,
        a.del_flag AS del_flag,
        a.outtime_status AS outtime_status,
        a.sig_order_subtype AS sig_order_subtype,
        a.sig_community_id AS sig_community_id,
        a.evalue_status AS evalue_status,
        a.proj_id AS proj_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
    FROM  dwi_bm.dwi_bm_t_repairs_order_info a    -- 报事报修工单
    left join dwi_bm.dwi_bm_t_com_info b on a.com_id = b.com_id
    WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;
    
END;
