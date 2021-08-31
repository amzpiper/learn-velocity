DECLARE
-- space script_dwd_space_detial_f_bm 汇聚主题库：空间信息表
--    目的：    全量分区 增量采集dwi层 房屋 插入到dwd层中 空间信息表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dwd_space_detial_f ,   schema : dwr_space,   中文名：空间信息表；
--    源表：  表名：dwi_bm_t_house_info,   schema : dwi_bm,  中文名：房屋；
--    作者：  renyi
--    创建日期：  2021-05-13
--    审核人：  renyi
--    审核时间：  2021-05-13
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dwr_space'',''dwd_space_detial_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dwr_space.dwd_space_detial_f  -- 空间信息表
          WHERE dw_creation_date =  to_timestamp(u_current_day , 'yyyy-MM-dd');



    -- 2.新增数据

    INSERT INTO
        dwr_space.dwd_space_detial_f (    -- 空间信息表
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
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
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
        a.house_del_flag AS del_flag,
        a.house_source_type AS source_type,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
     FROM  dwi_bm.dwi_bm_t_house_info a    -- 房屋
    WHERE  TO_CHAR(a.dw_creation_date, 'yyyy-MM-dd') = u_current_day;

END;