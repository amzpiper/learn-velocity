DECLARE
-- order script_dm_successrate_week_emp_f_dwr_order 汇聚主题库：完成率部门周报表
--    目的：    全量分区 增量采集dwi层 工单详情 插入到dm层中 完成率部门周报表 表中，同时对数据进行清洗、去重
--    目标表：  表名：dm_successrate_week_emp_f ,   schema : dm_order,   中文名：完成率部门周报表；
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
    DELETE FROM dm_order.dm_successrate_week_emp_f;  -- 完成率部门周报表

    -- 2.新增数据

    INSERT INTO
        dm_order.dm_successrate_week_emp_f (    -- 完成率部门周报表
        order_type_name,
        sig_order_subtype,
        total,
        successrate,
        timelyrate,
        paidantimelyrate,
        jiedantimelyrate,
        chulitimelyrate,
        shenhetimelyrate,
        huifangtimelyrate,
        
        dw_creation_by,
        dw_creation_date,
        dw_last_update_by,
        dw_last_update_date,
        dw_batch_number
    )
    SELECT
        a.order_type_name as order_type_name,
        a.sig_order_subtype as sig_order_subtype,
        a.total as total,
        a.successrate as successrate,
        a.order_timelyrate as timelyrate,
        b.paidan as paidantimelyrate,
        b.jiedan as jiedantimelyrate,
        b.chuli as chulitimelyrate,
        b.shenhe as shenhetimelyrate,
        b.huifang  as huifangtimelyrate,
        '${job_name}',
        u_current_day,
        '${job_name}',
        '${job_plan_time}',
        '1'
    FROM
      (
        SELECT
          a.order_type_name as order_type_name,
          a.sig_order_subtype as sig_order_subtype,
          coalesce(b.total, 0) as total,
          coalesce(b.successrate, 0) as successrate,
          coalesce(b.timelyrate, 0) as order_timelyrate
        FROM
          dm_order.dm_sig_order_subtype_f a
          LEFT JOIN (
            select
              a.order_type,
              a.sig_order_subtype,
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
              end as timelyrate
            FROM
              (
                --1.完成率-总数-除数-ok
                -- 公式: 完成数/总工单数
                --所有工单个数
                SELECT
                  count(*) as ordercount,
                  order_type,
                  sig_order_subtype
                FROM
                  (
                    SELECT
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                    FROM
                      dm_order.dm_order_detials_f
                    GROUP BY
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                  )
                WHERE
                  order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                  and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                GROUP BY
                  order_type,
                  sig_order_subtype
              ) a,(
                --1.完成率-总数-被除数-ok
                --2.及时率-总数-除数
                --所有完成的工单个数
                --总完成数
                SELECT
                  count(*) as successcount,
                  order_type,
                  sig_order_subtype
                FROM
                  (
                    SELECT
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                    FROM
                      dm_order.dm_order_detials_f
                    GROUP BY
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                  )
                WHERE
                  -- 完成
                  order_status = 'FF' -- 周
                  AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                  and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                GROUP BY
                  order_type,
                  sig_order_subtype
              ) b,(
                --2.及时率-超时总数-被除数-ok
                -- 公式: 未超时数/总完成数
                SELECT
                  count(*) as outtimecount,
                  order_type,
                  sig_order_subtype
                FROM
                  (
                    SELECT
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                    FROM
                      dm_order.dm_order_detials_f
                    GROUP BY
                      order_id,
                      order_type,
                      sig_order_subtype,
                      order_time,
                      outtime_status,
                      order_status
                  )
                WHERE
                  order_status = 'FF' -- 工单超时标记
                  AND outtime_status = '1' -- 周
                  AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                  and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                GROUP BY
                  order_type,
                  sig_order_subtype
              ) c
            WHERE
              a.order_type = b.order_type
              and b.order_type = c.order_type
              and a.sig_order_subtype = b.sig_order_subtype
              and b.sig_order_subtype = c.sig_order_subtype
          ) b ON CONCAT(b.order_type, '_', b.sig_order_subtype) = a.id
        ORDER BY
          id
      ) a,
      (
        select
          order_type_name as order_type_name,
          sig_order_subtype as sig_order_subtype,
          string_agg(
            (
              case
                track_name
                when '派单' then timelyrate
              end
            ),
            ''
          ) as paidan,
          string_agg(
            (
              case
                track_name
                when '接单' then timelyrate
              end
            ),
            ''
          ) as jiedan,
          string_agg(
            (
              case
                track_name
                when '处理' then timelyrate
              end
            ),
            ''
          ) as chuli,
          string_agg(
            (
              case
                track_name
                when '审核' then timelyrate
              end
            ),
            ''
          ) as shenhe,
          string_agg(
            (
              case
                track_name
                when '回访' then timelyrate
              end
            ),
            ''
          ) as huifang
        from
          (
            SELECT
              a.order_type_name as order_type_name,
              a.sig_order_subtype as sig_order_subtype,
              b.track_name as track_name,
              coalesce(b.timelyrate, 0) as timelyrate
            FROM
              dm_order.dm_sig_order_subtype_f a
              LEFT JOIN(
                select
                  l.order_type,
                  l.sig_order_subtype,
                  l.track_name,
                  CAST(
                    (l.outtimecount / m.successcount) * 100 AS DECIMAL(18, 0)
                  ) as timelyrate
                FROM
                  (
                    --7.回访环节及时率-超时总数-被除数
                    -- 公式: 1-(回访环节未超时数/回访环节总完成数)
                    SELECT
                      count(*) as outtimecount,
                      order_type,
                      sig_order_subtype,
                      track_name
                    FROM
                      dm_order.dm_order_detials_f
                    WHERE
                      order_status = 'FF' -- 工单超时标记
                      AND outtime_status = '1' -- 回访环节
                      --  AND track_name = '回访'
                      -- 周
                      AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      order_type,
                      sig_order_subtype,
                      track_name
                  ) as l,(
                    --7.回访环节及时率-总数-除数
                    SELECT
                      count(*) as successcount,
                      order_type,
                      sig_order_subtype,
                      track_name
                    FROM
                      dm_order.dm_order_detials_f
                    WHERE
                      order_status = 'FF' -- 回访环节
                      --  AND track_name = '回访'
                      -- 周
                      AND order_time > TO_CHAR(now() - INTERVAL '7 day', 'yyyy-MM-dd 00:00:00')
                      and order_time < TO_CHAR(now(), 'yyyy-MM-dd 00:00:00')
                    GROUP BY
                      order_type,
                      sig_order_subtype,
                      track_name
                  ) as m
                WHERE
                  l.order_type = m.order_type
                  and l.sig_order_subtype = m.sig_order_subtype
                  and l.track_name = m.track_name
                ORDER BY
                  order_type,
                  sig_order_subtype,
                  track_name
              ) b ON CONCAT(b.order_type, '_', b.sig_order_subtype) = a.id
            ORDER BY
              id
          )
        GROUP BY
          order_type_name,
          sig_order_subtype
      ) b
    where
      a.order_type_name = b.order_type_name
      and a.sig_order_subtype = b.sig_order_subtype;

END;