DECLARE
-- space script_dwd_space_detial_f_bm 汇聚主题库：空间信息表
--    目的：    全量分区 增量采集dwi层 房屋 插入到dwd层中 空间信息表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_space_detial_f ,   schema : dwr_space,   中文名：空间信息表；
--    源表：  表名：dwi_bm_t_house_info,   schema : dwi_bm,  中文名：房屋；
--    源表：  表名：dwi_bm_t_floor_info,   schema : dwi_bm,  中文名：楼层；
--    源表：  表名：dwi_bm_t_unit_info,   schema : dwi_bm,  中文名：单元；
--    源表：  表名：dwi_bm_t_buliding_info,   schema : dwi_bm,  中文名：楼宇；
--    源表：  表名：dwi_bm_t_project_info,   schema : dwi_bm,  中文名：项目；
--    从房屋表取的数据：若resource_type字段值为0，则本字段值为1；
--    若resource_type字段值为1，则本字段值为3
--    0 项目 1 房屋，2 土地，3 车位，4 楼宇，5 单元，6 楼层 
--    作者：  renyi
--    创建日期：  2021-05-13
--    审核人：  renyi
--    审核时间：  2021-05-13
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
    resource_type_flage      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd'),'yyyy-MM-dd');
    --SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    -- EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_space'',''dwd_space_detial_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_space.dwd_space_detial_f;  -- 空间信息表

    -- 2.新增数据
    -- 房屋
    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
        res_type,    --资源类型
        res_id,    --资源id
        res_parent_id,    --父资源id
        sig_res_id,    --四格资源id
        sig_res_parent_id,    --四格父资源id
        res_name,    --资源名称
        res_status,    --资源状态
        res_code,    --资源业态编码
        build_area,    --建筑面积
        bill_area,    --计费面积
        house_no,    --房号
        house_type,    --房屋类型编码
        use_area,    --使用面积
        garden_area,    --花园面积
        share_area,    --分摊面积
        life_cycle,    --房屋生命周期
        
        create_time,    --
        create_by,    --创建者
        update_time,    --
        update_by,    --
        del_flag,    --删除标记
        source_type,    --数据来源
        floor_id,
        unit_id,
        building_id,
        proj_id,

        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '1' AS res_type,
        a.house_id AS res_id,
        a.floor_id AS res_parent_id,
        a.sig_house_id AS sig_res_id,
        a.sig_floor_id AS sig_res_parent_id,
        a.house_name AS res_name,
        a.house_status AS res_status,
        a.house_code AS res_code,
        a.build_area AS build_area,
        a.bill_area AS bill_area,
        a.house_no AS house_no,
        a.house_type AS house_type,
        a.use_area AS use_area,
        a.garden_area AS garden_area,
        a.share_area AS share_area,
        a.life_cycle AS life_cycle,
        
        a.create_time AS create_time,
        a.create_by AS create_by,
        a.update_time AS update_time,
        a.update_by AS update_by,
        a.del_flag AS del_flag,
        a.source_type AS source_type,
        a.floor_id AS floor_id,  
        a.unit_id AS unit_id,
        a.building_id AS building_id,
        a.proj_id AS proj_id,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_house_info a;    -- 房屋
    
    -- 楼层
    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
        res_type,    --资源类型
        res_id,    --资源id
        res_parent_id,    --父资源id
        sig_res_id,    --四格资源id
        sig_res_parent_id,    --四格父资源id
        res_name,    --资源名称
        res_status,    --资源状态
        res_code,    --资源业态编码
        description,    --资源描述
        build_area,    --建筑面积
        bill_area,    --计费面积
        floor_index,   --楼层顺序必填且唯一
        house_no,    --房号
        house_type,    --房屋类型编码
        use_area,    --使用面积
        garden_area,    --花园面积
        share_area,    --分摊面积
        life_cycle,    --房屋生命周期

        create_time,    --
        create_by,    --创建者
        update_time,    --
        update_by,    --
        del_flag,    --删除标记
        source_type,    --数据来源
        unit_id,
        building_id,
        proj_id,

        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '6' AS res_type,
           b.floor_id AS res_id,
        b.unit_id AS res_parent_id,
        b.sig_floor_id AS sig_res_id,
        b.sig_unit_id AS sig_res_parent_id,
        b.floor_name AS res_name,
        b.floor_status AS res_status,
        b.floor_code AS res_code,
        b.description AS description,
        b.build_area AS build_area,
        b.bill_area AS bill_area,

        '' AS house_no,
        '' AS house_type,
        '' AS use_area,
        '' AS garden_area,
        '' AS share_area,
        '' AS floor_index,
        '' AS life_cycle,
        
        b.create_time AS create_time,
        b.create_by AS create_by,
        b.update_time AS update_time,
        b.update_by AS update_by,
        b.del_flag AS del_flag,
        b.source_type AS source_type,
        b.unit_id AS unit_id,
        b.building_id AS building_id,
        b.proj_id AS proj_id,

        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        b.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_floor_info b;    -- 楼层
    
    -- 单元
    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
        res_type,    --资源类型
        res_id,    --资源id
        res_parent_id,    --父资源id
        sig_res_id,    --四格资源id
        sig_res_parent_id,    --四格父资源id
        res_name,    --资源名称
        res_status,    --资源状态
        res_code,    --资源业态编码

        create_time,    --
        create_by,    --创建者
        update_time,    --
        update_by,    --
        del_flag,    --删除标记
        source_type,    --数据来源
        building_id,
        proj_id,

        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '5' AS res_type,
           b.unit_id AS res_id,
        b.building_id AS res_parent_id,
        b.sig_unit_id AS sig_res_id,
        b.sig_build_id AS sig_res_parent_id,
        b.unit_name AS res_name,
        b.unit_status AS res_status,
        b.unit_code AS res_code,
        
        b.create_time AS create_time,
        b.create_by AS create_by,
        b.update_time AS update_time,
        b.update_by AS update_by,
        b.del_flag AS del_flag,
        b.source_type AS source_type,
        b.building_id AS building_id,
        b.proj_id AS proj_id,

        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        b.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_unit_info b;    -- 单元
    
    
    -- 楼宇
    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
        res_type,    --资源类型
        res_id,    --资源id
        res_parent_id,    --父资源id
        sig_res_id,    --四格资源id
        sig_res_parent_id,    --四格父资源id
        res_name,    --资源名称
        res_status,    --资源状态
        res_code,    --资源业态编码
        
        building_type,    --资源状态
        build_floor,    --资源业态编码
        
        create_time,    --
        create_by,    --创建者
        update_time,    --
        update_by,    --
        del_flag,    --删除标记
        source_type,    --数据来源
        proj_id,

        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '4' AS res_type,
           d.building_id AS res_id,
        d.proj_id AS res_parent_id,
        d.sig_build_id AS sig_res_id,
        d.sig_organ_id AS sig_res_parent_id,
        d.building_name AS res_name,
        '' AS res_status,
        d.building_code AS res_code,
        
        d.building_type AS building_type,
        d.build_floor AS build_floor,
        
        d.create_time AS create_time,
        d.create_by AS create_by,
        d.update_time AS update_time,
        d.update_by AS update_by,
        d.delete_flag AS del_flag,
        d.source_type AS source_type,
        d.proj_id,

        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        d.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_buliding_info d;    -- 楼宇
    
    -- 项目
    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
        res_type,    --资源类型
        res_id,    --资源id
        res_parent_id,    --父资源id
        sig_res_id,    --四格资源id
        sig_res_parent_id,    --四格父资源id
        res_name,    --资源名称
        res_status,    --资源状态
        res_code,    --资源业态编码
        description,    --资源描述
        
        organ_py,    --项目拼音名称
        charge_type,    --收费方式
        organ_address,    --地址
        organ_region,    --机构所属地区
        complete_date,    --竣工时间
        checkin_date,    --入伙时间 
        total_area,    --占地面积
        province,    --省
        city,    --市
        region,    --区
        street,    --街道
        plot_ratio,    --容积率
        green_ratio,    --绿化率

        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        '0' AS res_type,
           d.proj_id AS res_id,
        '' AS res_parent_id,
        d.sig_organ_id AS sig_res_id,
        '' AS sig_res_parent_id,
        d.organ_name AS res_name,
        d.organ_status AS res_status,
        d.organ_code AS res_code,
        d.description AS description,
        
        d.organ_py AS organ_py,
        d.charge_type AS charge_type,
        d.organ_address AS organ_address,
        d.organ_region AS organ_region,
        d.complete_date AS complete_date,
        d.checkin_date AS checkin_date,
        d.total_area AS total_area,
        d.province AS province,
        d.city AS city,
        d.region AS region,
        d.street AS street,
        d.plot_ratio AS plot_ratio,
        d.green_ratio AS green_ratio,

        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        d.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_project_info d;    -- 项目

END;