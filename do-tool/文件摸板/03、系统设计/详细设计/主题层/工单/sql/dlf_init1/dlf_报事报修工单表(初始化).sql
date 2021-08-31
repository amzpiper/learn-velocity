DECLARE
-- order script_init_dwd_order_t_repairs_order_f_bm 汇聚主题库：报事报修工单表
--    目的：    全量分区  初始化采集dwi层 报事报修工单 插入到dwd层中 报事报修工单表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_order_t_repairs_order_f ,   schema : dwr_order,   中文名：报事报修工单表；
--    源表：  表名：dwi_bm_t_repairs_order_info,   schema : dwi_bm,  中文名：报事报修工单；
--    作者：  lihq
--    创建日期：  2021-08-31
--    审核人：  lihq
--    审核时间：  2021-08-31
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
        dwr_order.dwd_order_t_repairs_order_f INTO records;

    IF (records = 0)
    THEN

         --创建分区
        EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_order'',''dwd_order_t_repairs_order_f'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表，简化SQL
        CREATE temp TABLE temp_max_all_dwi_bm_t_repairs_order_info
        AS
        SELECT
            b.id,
            MAX(b.gmt_modified) AS gmt_modified
        FROM
            dwi_bm.dwi_bm_t_repairs_order_info b
        GROUP BY
            b.id;


        INSERT INTO
            dwr_order.dwd_order_t_repairs_order_f (
            order_id, --
            order_no, --工单号
            order_type, --问题类型
            addr, --问题地点
            description, --问题描述
            connect_no, --联系方式
            connector, --联系人
            level_no, --紧急程度
            building_id, --关联楼宇
            create_time, --
            create_by, --
            source_type, --来源类型
            voice_file, --语音描述
            order_time, --报修时间
            order_status, --工单状态
            version, --版本 锁
            credit_code, --统一社会信用代码
            sige_order_id, --四格服务工单id
            order_create_name, --录单人姓名
            sige_transport_user_id, --录单人ID
            sige_transport_euser_id, --外部录单人ID
            del_flag, --删除标识
            outtime_status, --超时状态
            sig_order_subtype, --四格问题子类型
            sig_community_id, --四格项目id
            evalue_status, --评价状态
            proj_id, --项目id
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
            a.connect_no AS connect_no,
            a.connector AS connector,
            a.level_no AS level_no,
            a.building_id AS building_id,
            a.create_time AS create_time,
            a.create_by AS create_by,
            a.source_type AS source_type,
            a.voice_file AS voice_file,
            a.order_time AS order_time,
            a.order_status AS order_status,
            a.version AS version,
            a.credit_code AS credit_code,
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
           TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
           a.dw_batch_number

        FROM
            dwi_bm.dwi_bm_t_repairs_order_info a
                JOIN
                    temp_max_all_dwi_bm_t_repairs_order_info x
                        ON (
                            a.id = x.id
                            AND a.gmt_modified = x.gmt_modified
                        )
                WHERE
                    TO_CHAR(a.gmt_modified, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;