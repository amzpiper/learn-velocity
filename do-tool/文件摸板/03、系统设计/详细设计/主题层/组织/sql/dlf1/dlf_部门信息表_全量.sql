DECLARE
-- dim script_dim_org_sys_dept_d_bm 汇聚主题库：部门信息表
--    目的：    全量分区 增量采集dwi层 部门 插入到dwd层中 部门信息表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dim_org_sys_dept_d ,   schema : dwr_dim,   中文名：部门信息表；
--    源表：  表名：dwi_bm_sys_dept_info,   schema : dwi_bm,  中文名：部门；
--    作者：  renyi
--    创建日期：  2021-05-13
--    审核人：  renyi
--    审核时间：  2021-05-13
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd'),'yyyy-MM-dd');
    -- SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_dim'',''dim_org_sys_dept_d'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_dim.dim_org_sys_dept_d  -- 部门信息表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');



    -- 2.新增数据

    INSERT INTO
        dwr_dim.dim_org_sys_dept_d (    -- 部门信息表
        dept_id,    --部门id
        parent_id,    --父部门id
        ancestors,    --祖级列表
        dept_name,    --部门名称
        order_num,    --显示顺序
        leader,    --负责人
        phone,    --联系电话
        email,    --邮箱
        status,    --部门状态（0正常 1停用）
        del_flag,    --删除标志（0代表存在 2代表删除）
        create_by,    --创建者
        create_time,    --创建时间
        update_by,    --更新者
        update_time,    --更新时间
        dept_type,    --组织机构类型（33组，01集团，02公司，03分公司，04子公司，05专业公司，06部门，07管理中心，08管理处，09办事处，10项目）
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.dept_id AS dept_id,
        a.parent_id AS parent_id,
        a.ancestors AS ancestors,
        a.dept_name AS dept_name,
        a.order_num AS order_num,
        a.leader AS leader,
        a.phone AS phone,
        a.email AS email,
        a.status AS status,
        a.del_flag AS del_flag,
        a.create_by AS create_by,
        a.create_time AS create_time,
        a.update_by AS update_by,
        a.update_time AS update_time,
        a.dept_type AS dept_type,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_sys_dept_info a    -- 部门
    WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;