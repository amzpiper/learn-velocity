DECLARE
-- gdeyy script_dm_gdeyy_person_blacklist_f_dwr_dim 汇聚主题库：布控人员
--    目的：    全量分区 增量采集dwi层 布控人员 插入到dwd层中 布控人员 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_gdeyy_person_blacklist_f ,   schema : dm_gdeyy_person,   中文名：布控人员；
--    源表：  表名：dim_person_detail_info_d,   schema : dwr_dim,  中文名：布控人员；
--    作者：  jiahj
--    创建日期：  2021-01-26
--    审核人：  jiahj
--    审核时间：  2021-01-26
--    历史修改记录：  修改时间 2021-02-26     修改作者   lixjc    备注  添加关注类型


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    SELECT PUBLIC.VERIFICATION_JOB_CURRENT_DAY_RETURN_LAST_DATE(u_current_day_input) INTO u_current_day;

    -- 如果存在不建分区，如果不存在新增分区
    --EXECUTE IMMEDIATE ('call public.sp_add_day_partitions(''dm_gdeyy_person'',''dm_gdeyy_person_blacklist_f'',''dw_creation_date'',''p'',current_partition_name)');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dm_gdeyy_person.dm_gdeyy_person_blacklist_f;  -- 布控人员



-- 2.新增数据

INSERT INTO
    dm_gdeyy_person.dm_gdeyy_person_blacklist_f (    -- 布控人员
        record_id,    --顺序号
        name,    --姓名
        sex,    --性别
        age,    --年龄
        idcard,    --证件号码
        img_url,    --照片
        attention_type,    --关注类型
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
SELECT
        a.person_id AS record_id,
        a.person_name AS name,
        case when a.gender='MALE' then '男'
			 when a.gender='FEMALE' then '女'
			 else '-' end
		AS sex,
        TO_CHAR(NOW(),'yyyy')-TO_CHAR(to_date(date_of_birth),'yyyy') - (case when TO_CHAR(NOW(),'MM-dd')>TO_CHAR(to_date(date_of_birth),'MM-dd') then 0 else 1 end) AS age,
        a.identity_type_code_proof_value AS idcard,
        a.attachment_urls AS img_url,
        e.groupname as attention_type,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        a.dw_batch_number
FROM  dwr_dim.dim_person_detail_info_d a    -- 布控人员
left join (select distinct f.id,f.groupname,d.bingoPeopleId 
from dwi_abc.dwi_abc_security_person_group f
left join (select distinct b.bingoGroupId,c.bingoPeopleId from dwi_abc.dwi_abc_tbl_persongroup_relation b
left join dwi_abc.dwi_abc_tbl_personinstance_relation c on b.vcmGroupId=c.vcmGroupId) d
on f.id=d.bingoGroupId) e on a.person_id=e.bingoPeopleId
WHERE  person_type_codes like '%UNKNOWN%' and status<>'DELETED';

update dm_gdeyy_person.dm_gdeyy_person_blacklist_f set age='-' where age is null;
END;