DECLARE
-- enterprise script_dwd_enterprise_com_res_f_bm 汇聚主题库：企业与资源关系表
--    目的：    全量分区 增量采集dwi层 企业与资源关系表 插入到dwd层中 企业与资源关系表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_enterprise_com_res_f ,   schema : dwr_enterprise,   中文名：企业与资源关系表；
--    源表：  表名：dwi_bm_t_com_res_info,   schema : dwi_bm,  中文名：企业与资源关系表；
--    作者：  renyi
--    创建日期：  2021-05-14
--    审核人：  renyi
--    审核时间：  2021-05-14
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd'),'yyyy-MM-dd');
    -- SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    --EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_enterprise'',''dwd_enterprise_com_res_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_enterprise.dwd_enterprise_com_res_f;  -- 企业与资源关系表




    -- 2.新增数据

    INSERT INTO
        dwr_enterprise.dwd_enterprise_com_res_f (    -- 企业与资源关系表
        res_id,    --资源id
        sig_rel_type,    --资源类型
        sig_obj_id,    --资源ID
        resource_name,    --资源名称
        sig_community_id,    --项目ID
        sig_cust_id,    --客户ID
        sig_up_cust_id,    --四格上级客户ID
        cust_type,    --客户类型
        cust_status,    --客户状态
        decorate_time,    --最新装修时间
        eff_time,    --租赁起始时间
        exp_time,    --租赁结束时间
        in_time,    --入住时间
        join_time,    --入伙时间
        out_time,    --离开时间
        credit_code,    --统一社会信用代码
        space_id,    --空间数据主键
        source_type,    --数据来源
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.res_id AS res_id,
        a.sig_rel_type AS sig_rel_type,
        a.sig_obj_id AS sig_obj_id,
        a.resource_name AS resource_name,
        a.sig_community_id AS sig_community_id,
        a.sig_cust_id AS sig_cust_id,
        a.sig_up_cust_id AS sig_up_cust_id,
        a.cust_type AS cust_type,
        a.cust_status AS cust_status,
        a.decorate_time AS decorate_time,
        a.eff_time AS eff_time,
        a.exp_time AS exp_time,
        a.in_time AS in_time,
        a.join_time AS join_time,
        a.out_time AS out_time,
        a.com_id AS credit_code,
        a.space_id AS space_id,
        a.source_type AS source_type,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_com_res_info a;    -- 企业与资源关系表


END;