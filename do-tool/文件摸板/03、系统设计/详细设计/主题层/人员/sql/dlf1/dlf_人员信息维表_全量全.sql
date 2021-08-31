DECLARE
-- dim script_dim_person_detail_info_d_bm 汇聚主题库：人员信息维表
--    目的：    全量分区 增量采集dwi层 员工 插入到dwd层中 人员信息维表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dim_person_detail_info_d ,   schema : dwr_dim,   中文名：人员信息维表；
--    源表：  表名：dwi_bm_t_staff_info,   schema : dwi_bm,  中文名：员工；
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
    --EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_dim'',''dim_person_detail_info_d'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_dim.dim_person_detail_info_d  -- 人员信息维表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');



    -- 2.新增数据

    INSERT INTO
        dwr_dim.dim_person_detail_info_d (    -- 人员信息维表
        person_id,    --人员id
        person_name,    --人员姓名
        person_phone,    --人员手机号
        person_status,    --人员状态
        person_gender,    --人员性别
        person_certi_nbr,    --人员证件号
        person_certi_type,    --证件类型
        create_by,    --创建者
        create_time,    --创建时间
        update_by,    --更新者
        update_time,    --更新时间
        del_flag,    --删除标志
        com_id,    --所属企业id
        audit_flag,    --审批人标记
        post_name,    --职位
        sig_cust_id,    --四格员工信息id
        birthday,    --出生日期
        cust_code,    --四格客户编码
        addr,    --地址
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.staff_id AS person_id,
        a.staff_name AS person_name,
        a.staff_phone AS person_phone,
        a.staff_status AS person_status,
        a.gender AS person_gender,
        a.certi_nbr AS person_certi_nbr,
        a.certi_type AS person_certi_type,
        a.create_by AS create_by,
        a.create_time AS create_time,
        a.update_by AS update_by,
        a.update_time AS update_time,
        a.del_flag AS del_flag,
        a.com_id AS com_id,
        a.audit_flag AS audit_flag,
        a.post_name AS post_name,
        a.sig_cust_id AS sig_cust_id,
        a.birthday AS birthday,
        a.cust_code AS cust_code,
        a.addr AS addr,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_staff_info a    -- 员工
    WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;