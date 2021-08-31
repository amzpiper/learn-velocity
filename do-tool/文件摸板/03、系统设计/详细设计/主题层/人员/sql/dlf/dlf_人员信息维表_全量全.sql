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
    DELETE FROM dwr_dim.dim_person_detail_info_d;  -- 人员信息维表
        --   WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');

    -- 2.新增数据
    --员工
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
     FROM  dwi_bm.dwi_bm_t_staff_info a;    -- 员工
    -- WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

    --访客
    INSERT INTO
        dwr_dim.dim_person_detail_info_d (    -- 人员信息维表
        person_id,    --人员id
        person_name,    --人员姓名
        person_phone,    --人员手机号
        person_status,    --人员状态
        person_gender,    --人员性别
        person_pic,
        person_certi_nbr,    --人员证件号
        person_certi_type,    --证件类型
        create_by,    --创建者
        create_time,    --创建时间
        update_by,    --更新者
        update_time,    --更新时间
        del_flag,    --删除标志
        inviter_id_pic,    --所属企业id
        visit_com_name,    --审批人标记
        visit_com_id,    --职位
        visit_man_name,    --四格员工信息id
        visit_man_phone,    --出生日期
        visit_time,    --四格客户编码
        visit_addr,    --地址
        visit_reason,
        invite_status,
        instance_id,
        invite_type,
        business_key,
        building_id,
        area_code,
        validate_start,
        validate_end,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.invite_id AS person_id,
        a.inviter_name AS person_name,
        a.inviter_phone AS person_phone,
        '' AS person_status,
        '' AS person_gender,
        inviter_pic AS person_pic,
        a.inviter_idno AS person_certi_nbr,
        a.inviter_idtype AS person_certi_type,
        a.create_by AS create_by,
        a.create_time AS create_time,
        '' AS update_by,
        a.update_time AS update_time,
        a.delete_flag AS del_flag,
        a.inviter_id_pic AS inviter_id_pic,
        a.visit_com_name AS visit_com_name,
        a.visit_com_id AS visit_com_id,
        a.visit_man_name AS visit_man_name,
        a.visit_man_phone AS visit_man_phone,
        a.visit_time AS visit_time,
        a.visit_addr AS visit_addr,
        a.visit_reason AS visit_reason,
        a.invite_status AS invite_status,
        a.instance_id AS instance_id,
        a.invite_type AS invite_type,
        a.business_key AS business_key,
        a.building_id AS building_id,
        a.area_code AS area_code,
        a.validate_start AS validate_start,
        a.validate_end AS validate_end,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_invite_info a;  
    -- WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

    INSERT INTO
        dwr_dim.dim_person_detail_info_d (    -- 人员信息维表
        person_id,    --人员id
        person_name,    --人员姓名
        person_phone,    --人员手机号
        person_status,    --人员状态
        person_gender,    --人员性别
        person_pic,
        create_by,    --创建者
        create_time,    --创建时间
        update_by,    --更新者
        update_time,    --更新时间
        del_flag,    --删除标志
        dept_id,
        nick_name,
        user_type,
        email,
        password,
        login_ip,
        login_date,
        staff_id,
        salt,
        u_id,
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.user_id AS person_id,
        a.user_name AS person_name,
        a.phonenumber AS person_phone,
        a.status AS person_status,
        a.sex AS person_gender,
        a.avatar AS person_pic,
        a.create_by AS create_by,
        a.create_time AS create_time,
        a.update_by AS update_by,
        a.update_time AS update_time,
        a.del_flag AS del_flag,
        a.dept_id AS dept_id,
        a.nick_name AS nick_name,
        a.user_type AS user_type,
        a.email AS email,
        a.password AS password,
        a.login_ip AS login_ip,
        a.login_date AS login_date,
        a.staff_id AS staff_id,
        a.salt AS salt,
        a.u_id AS u_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_sys_user_info a;    -- 员工
    -- WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;


END;