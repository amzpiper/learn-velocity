DECLARE
-- order script_dm_successrate_week_people_f_dwr_order 汇聚主题库：完成率人员周报表
--    目的：    全量分区 增量采集dwi层 工单详情 插入到dm层中 完成率人员周报表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_successrate_week_people_f ,   schema : dm_order,   中文名：完成率人员周报表；
--    源表：  表名：dm_sig_order_subtype_f,   schema : dm_order,  中文名：工单类型表；
--    源表：  表名：dm_order_detials_f,   schema : dm_order,  中文名：工单详情表；
--    作者：  renyi
--    创建日期：  2021-05-20
--    审核人：  renyi
--    审核时间：  2021-05-20
--    历史修改记录：  修改时间      修改作者         备注


    u_current_day      VARCHAR;
    u_current_day_input      VARCHAR;
BEGIN

    u_current_day_input = '${current_day}';
    u_current_day = TO_CHAR(TO_TIMESTAMP(u_current_day_input , 'yyyy-MM-dd'),'yyyy-MM-dd');

    -- 支持重跑，删除DWR表中跑批当天的数据
    DELETE FROM dm_order.dm_successrate_week_people_f;  -- 完成率人员周报表

    -- 2.新增数据

    INSERT INTO
        dm_order.dm_successrate_week_people_f (    -- 完成率人员周报表
        order_type_name,
        current_user_name,
        current_user_id,
        total,
        usetime,
        successrate,
        timelyrate,
        outtimecount,
        
        qiangdian,
        ruodian,
        nuantong,
        zongxiu,
        qingjie,
        lvhua,
        anbao,
        other,
        
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )select
    a.order_type_name as order_type_name,
    b.current_user_name as current_user_name,
    b.current_user_id as current_user_id,
    b.total as total,
    b.usetime as usetime,
    b.successrate as successrate,
    b.timelyrate as timelyrate,
    b.outtimecount as outtimecount,

    b.qiangdian as qiangdian,
    b.ruodian as ruodian,
    b.nuantong as nuantong,
    b.zongxiu as zongxiu,
    b.qingjie  as qingjie,
    b.lvhua as lvhua,
    b.anbao as anbao,
    b.other  as other,
    '${job_name}',
    u_current_day,
    '${job_name}',
    '${job_plan_time}',
    '1'
    FROM
          dm_order.dm_sig_order_subtype_f a
          LEFT JOIN 
            (
            SELECT
                a.order_type as order_type,
                a.current_user_id as current_user_id,
                a.current_user_name as current_user_name,
                a.total as total,
                a.successrate as successrate,
                a.timelyrate as timelyrate,
                a.outtimecount as outtimecount,
                a.usetime as usetime,

                b.qiangdian as qiangdian,
                b.ruodian as ruodian,
                b.nuantong as nuantong,
                b.zongxiu as zongxiu,
                b.qingjie  as qingjie,
                b.lvhua as lvhua,
                b.anbao as anbao,
                b.other  as other
            FROM
            (

                select
                  a.current_user_id as current_user_id,
                  a.current_user_name as current_user_name,
                  e.order_type as order_type,
                  --总共完成单数
                  case
                    when b.successcount = 0 then 0
                    else b.successcount
                  end as total,
                  --完成率
                  case
                    when a.ordercount = 0 then 0
                    else CAST(
                      (b.successcount / a.ordercount) * 100 AS DECIMAL(18, 0)
                    )
                  end as successrate,
                  --及时率
                  case
                    when b.successcount = 0 then 0
                    else CAST(
                      (c.outtimecount / b.successcount) * 100 AS DECIMAL(18, 0)
                    )
                  end as timelyrate,
                  --超时完成数
                  case
                    when d.outtimecount = 0 then 0
                    when d.outtimecount is null then 0
                    else d.outtimecount
                  end as outtimecount, 
                  e.usetime as usetime
                FROM
                  (
                    --所有工单个数
                    SELECT
                      count(*) as ordercount,
                      min(order_type),
                      current_user_id,
                      current_user_name
                    FROM
                      (
                        SELECT
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          order_status
                        FROM
                          dm_order.dm_order_detials_f
                        GROUP BY
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          order_status
                      )
                    WHERE
                      -- 周
                      order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      current_user_id,
                      current_user_name
                  ) a,(
                  
                    --所有完成的工单个数
                    SELECT
                      count(*) as successcount,
                      min(order_type),
                      current_user_id,
                      current_user_name
                    FROM
                      (
                        SELECT
                        order_type,
                        current_user_id,
                        current_user_name,
                        order_time,
                        order_status
                        FROM
                        dm_order.dm_order_detials_f
                        GROUP BY
                        order_type,
                        current_user_id,
                        current_user_name,
                        order_time,
                        order_status
                      )
                    WHERE
                      -- 完成
                      order_status = 'FF' -- 周
                      AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      current_user_id,
                      current_user_name
                  ) b,(
                    --2.及时率-超时总数-被除数-ok
                    -- 公式: 未超时数/总完成数
                    SELECT
                      count(*) as outtimecount,
                      min(order_type),
                      current_user_id,
                      current_user_name
                    FROM
                      (
                        SELECT
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          outtime_status,
                          order_status
                        FROM
                          dm_order.dm_order_detials_f
                        GROUP BY
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          outtime_status,
                          order_status
                      )
                    WHERE
                      -- 完成
                      order_status = 'FF'
                      -- 未超时
                      AND outtime_status = '1' -- 周
                      AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      current_user_id,
                      current_user_name
                  ) c,
                  (
                    SELECT
                      a.current_user_name as current_user_name,
                      a.current_user_id as current_user_id,
                      min(b.outtimecount) as outtimecount,
                      min(a.order_type) as order_type 
                    FROM
                      (
                        select current_user_id,current_user_name,order_type,sig_order_subtype
                        from 
                            dm_order.dm_order_detials_f
                        GROUP BY
                            current_user_id,current_user_name,order_type,sig_order_subtype
                      ) a
                      LEFT JOIN(
                        -- 公式: 超时完成数
                        SELECT
                          coalesce(count(*), 0) as outtimecount,
                          min(order_type) as order_type,
                          current_user_id,
                          current_user_name
                        FROM
                          (
                            SELECT
                              order_type,
                              current_user_id,
                              current_user_name,
                              order_time,
                              outtime_status,
                              order_status
                            FROM
                              dm_order.dm_order_detials_f
                            GROUP BY
                              order_type,
                              current_user_id,
                              current_user_name,
                              order_time,
                              outtime_status,
                              order_status
                          )
                        WHERE
                          -- 完成
                          order_status = 'FF'
                          -- 未超时
                          AND outtime_status = '2' -- 周
                          AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                          and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                        GROUP BY
                          current_user_id,
                          current_user_name
                    ) b ON 
                      b.current_user_id = a.current_user_id 
                      and 
                      b.current_user_name = a.current_user_name
                      GROUP BY
                          a.current_user_id,
                          a.current_user_name
                  ) d,
                  (
                    --2.总用时
                    SELECT
                      sum(track_update_time-track_create_time) as usetime,
                      min(order_type) as order_type,
                      current_user_id,
                      current_user_name
                    FROM
                      (
                        SELECT
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          outtime_status,
                          order_status,
                          min(track_update_time) as track_update_time,
                          min(track_create_time) as track_create_time
                        FROM
                          dm_order.dm_order_detials_f
                        GROUP BY
                          order_type,
                          current_user_id,
                          current_user_name,
                          order_time,
                          outtime_status,
                          order_status
                      )
                    WHERE
                      -- 完成
                      order_status = 'FF'
                      AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      current_user_id,
                      current_user_name
                  ) e
                WHERE
                  a.current_user_id = b.current_user_id
                  and b.current_user_id = c.current_user_id
                  and d.current_user_id = c.current_user_id
                  and d.current_user_id = e.current_user_id
                  and a.current_user_name = b.current_user_name
                  and b.current_user_name = c.current_user_name
                  and d.current_user_name = c.current_user_name
                  and d.current_user_name = e.current_user_name
                  
                  
                  
            ) a ,


            (
                   select
                      order_type,
                      current_user_name,
                      current_user_id,
                      qiangdian,
                      ruodian,
                      nuantong,
                      zongxiu,
                      qingjie,
                      lvhua,
                      anbao,
                      (case
                       when to_number(other) = 0 then 0
                       when to_number(other) is null then 0
                       else to_number(other)
                      end )+
                      (  
                      case
                       when to_number(other1) = 0 then 0
                       when to_number(other1) is null then 0
                       else to_number(other1)
                      end ) as other
                    FROM
                 (
                    select
                     min(order_type) as order_type,
                     current_user_name as current_user_name,
                     current_user_id as current_user_id,
                     string_agg(
                        (
                          case
                             id
                             when '2_8' then total
                          end
                        ),
                        ''
                     ) as qiangdian,
                     string_agg(
                        (
                          case
                             id
                             when '2_6' then total
                          end
                        ),
                        ''
                     ) as ruodian,
                     string_agg(
                        (
                          case
                             id
                             when '2_5' then total
                          end
                        ),
                        ''
                     ) as nuantong,
                     string_agg(
                        (
                          case
                             id
                             when '2_1' then total
                          end
                        ),
                        ''
                     ) as zongxiu,
                     string_agg(
                        (
                          case
                             id
                             when '1_1' then total
                          end
                        ),
                        ''
                     ) as qingjie,
                     string_agg(
                        (
                          case
                             id
                             when '1_2' then total
                          end
                        ),
                        ''
                     ) as lvhua,
                     string_agg(
                        (
                          case
                             id
                             when '3_2' then total
                          end
                        ),
                        ''
                     ) as anbao,
                     string_agg(
                        (
                          case
                             id
                             when '1_3' then total
                          end
                        ),
                        ''
                     ) as other,
                     string_agg(
                        (
                          case
                             id
                             when '3_3' then total
                          end
                        ),
                        ''
                     ) as other1
                   from
                   (
                        --- 右边-工单子类
                        SELECT
                          b.order_type as order_type,
                          b.sig_order_subtype as sig_order_subtype,
                          a.current_user_name as current_user_name,
                          a.current_user_id as current_user_id,
                          b.total as total,
                          concat(b.order_type,'_',b.sig_order_subtype) as id
                        FROM
                          (
                             select current_user_id,current_user_name,order_type,sig_order_subtype
                             from 
                                  dm_order.dm_order_detials_f
                             GROUP BY
                                  current_user_id,current_user_name,order_type,sig_order_subtype
                          ) a
                          LEFT JOIN( 
                             --工单子服务分类总工单数
                             SELECT
                               count(*) as total,
                               order_type,
                               sig_order_subtype,
                               current_user_id,
                               current_user_name
                             FROM
                               (
                                 select current_user_id,current_user_name,order_type,sig_order_subtype,order_id
                                 from 
                                      dm_order.dm_order_detials_f
                                 WHERE
                                   order_status = 'FF' -- 工单超时标记
                                   -- 周
                                   AND order_time > TO_CHAR(now() - INTERVAL '30 day', 'yyyy-MM-dd 00:00:00')
                                   and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                               )
                             GROUP BY
                               order_type,
                               sig_order_subtype,
                               current_user_id,
                               current_user_name
                          ) b 
                          ON 
                          b.current_user_id = a.current_user_id 
                          and 
                          b.current_user_name = a.current_user_name 
                          and 
                          b.order_type = a.order_type
                          and 
                          b.sig_order_subtype = a.sig_order_subtype          
                        ORDER BY
                             a.current_user_id,a.current_user_name 
                        )
                     GROUP BY
                        current_user_id,
                        current_user_name
                 )
               )b


            where
            b.current_user_id = a.current_user_id 
            and 
            b.current_user_name = a.current_user_name 
            and 
            b.order_type = a.order_type 
            ) b
            
    ON b.order_type = a.order_type    
    GROUP BY
        b.current_user_id,
        b.current_user_name,
        a.order_type_name,
        b.total,
        b.successrate,
        b.timelyrate,
        b.outtimecount,
        b.usetime,

        b.qiangdian,
        b.ruodian,
        b.nuantong,
        b.zongxiu,
        b.qingjie,
        b.lvhua,
        b.anbao,
        b.other
    ORDER BY
        a.order_type_name;

END;