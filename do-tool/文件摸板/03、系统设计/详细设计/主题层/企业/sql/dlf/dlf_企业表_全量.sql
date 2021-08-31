DECLARE
-- enterprise script_dwd_enterprise_t_com_f_bm 汇聚主题库：企业表
--    目的：    全量分区 增量采集dwi层 企业 插入到dwd层中 企业表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_enterprise_t_com_f ,   schema : dwr_enterprise,   中文名：企业表；
--    源表：  表名：dwi_bm_t_com_info,   schema : dwi_bm,  中文名：企业；
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


    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_enterprise.dwd_enterprise_t_com_f;  -- 企业表


    -- 2.新增数据

    INSERT INTO
        dwr_enterprise.dwd_enterprise_t_com_f (    -- 企业表
        com_id,    --企业主键
        com_name,    --企业名称
        com_type,    --公司类型
        level_no,    --企业等级
        del_flag,    --删除标记
        create_time,    --创建时间
        update_time,    --更新时间
        update_by,    --更新者
        create_by,    --创建者
        cust_code,    --客户编码
        credit_code,    --统一社会信用代码
        company_address,    --地址
        email,    --电子邮箱
        establish_date,    --成立日期
        company_legal,    --法人代表
        listed_comp_code,    --上市公司代码
        listing_type,    --上市类型
        regist_authority,    --登记机关
        regist_number,    --工商注册号
        registered_capital,    --注册资本
        taxpayer_code,    --纳税人编号
        taxpayer_id,    --纳税人识别号
        website,    --网站地址
        annual_output,    --年产值
        annual_tax,    --年纳税额
        employees_num,    --员工数量
        end_time,    --停业日期
        open_time,    --开始日期
        operation_scope,    --经营范围
        patents_number,    --专利数量
        approval_date,    --核准日期
        com_status,    --企业状态
        industry,    --所属行业
        registered_addr,    --注册地址
        sig_cust_id,    --四格企业id
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.com_id AS com_id,
        a.com_name AS com_name,
        a.com_type AS com_type,
        a.level_no AS level_no,
        a.del_flag AS del_flag,
        a.create_time AS create_time,
        a.update_time AS update_time,
        a.update_by AS update_by,
        a.create_by AS create_by,
        a.cust_code AS cust_code,
        a.credit_code AS credit_code,
        a.company_address AS company_address,
        a.email AS email,
        a.establish_date AS establish_date,
        a.company_legal AS company_legal,
        a.listed_comp_code AS listed_comp_code,
        a.listing_type AS listing_type,
        a.regist_authority AS regist_authority,
        a.regist_number AS regist_number,
        a.registered_capital AS registered_capital,
        a.taxpayer_code AS taxpayer_code,
        a.taxpayer_id AS taxpayer_id,
        a.website AS website,
        a.annual_output AS annual_output,
        a.annual_tax AS annual_tax,
        a.employees_num AS employees_num,
        a.end_time AS end_time,
        a.open_time AS open_time,
        a.operation_scope AS operation_scope,
        a.patents_number AS patents_number,
        a.approval_date AS approval_date,
        a.com_status AS com_status,
        a.industry AS industry,
        a.registered_addr AS registered_addr,
        a.sig_cust_id AS sig_cust_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_com_info a;    -- 企业

END;