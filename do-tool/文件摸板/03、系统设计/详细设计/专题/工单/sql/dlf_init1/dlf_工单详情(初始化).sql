DECLARE
-- order script_init_dm_order_detials_f_dwr_order 汇聚主题库：工单详情
--    目的：    全量分区  初始化采集dwi层 报事报修工单表 插入到dwd层中 工单详情 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_order_detials_f ,   schema : dm_order,   中文名：工单详情；
--    源表：  表名：dwd_order_t_repairs_order_f,   schema : dwr_order,  中文名：报事报修工单表；
--    作者：  lixj
--    创建日期：  2021-05-16
--    审核人：  lixj
--    审核时间：  2021-05-16
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
        dm_order.dm_order_detials_f INTO records;

    IF (records = 0)
    THEN

         --创建分区
        EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_order'',''dm_order_detials_f'',''dw_creation_date'',''p'',current_partition_name)');

        --创建临时表，简化SQL
        CREATE temp TABLE temp_max_all_dwd_order_t_repairs_order_f
        AS
        SELECT
            b.id,
            MAX(b.acquisition_time) AS acquisition_time
        FROM
            dwr_order.dwd_order_t_repairs_order_f b
        GROUP BY
            b.id;


        INSERT INTO
            dm_order.dm_order_detials_f (
            order_no, --工单号
            sig_order_subtype, --四格问题子类型
            order_type, --问题类型
            addr, --问题地点
            description, --问题描述
            connect, --联系方式
            connector, --联系人
            level, --紧急程度
            create_time, --创建事件
            source_type, --来源类型
            order_time, --报修时间
            order_status, --工单状态
            del_flag, --删除标识
            outtime_status, --超时状态
            sig_order_subtype, --四格问题子类型
            evaluate_level, --评价等级
            dw_creation_by,
            dw_creation_date,
            dw_last_update_by,
            dw_last_update_date,
            dw_batch_number
        )
        SELECT
            a.order_no AS order_no,
            a.sige_order_id AS sig_order_subtype,
            a.order_type AS order_type,
            a.addr AS addr,
            a.desc AS description,
            a.connect AS connect,
            a.connector AS connector,
            a.level AS level,
            a.create_time AS create_time,
            a.source_type AS source_type,
            a.order_time AS order_time,
            a.order_status AS order_status,
            a.del_flag AS del_flag,
            a.outtime_status AS outtime_status,
            a.sig_order_subtype AS sig_order_subtype,
            a.evaluate_level AS evaluate_level,
           '${job_name}',
           u_current_day,
           '${job_name}',
           TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd hh24:mi:ss'),
           a.dw_batch_number

        FROM
            dwr_order.dwd_order_t_repairs_order_f a
                JOIN
                    temp_max_all_dwd_order_t_repairs_order_f x
                        ON (
                            a.id = x.id
                            AND a.acquisition_time = x.acquisition_time
                        )
                WHERE
                    TO_CHAR(a.acquisition_time, 'yyyy-MM-dd') <= u_current_day;

    END IF;

END;