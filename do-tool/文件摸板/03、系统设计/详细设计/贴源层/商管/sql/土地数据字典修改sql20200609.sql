
--mysql
ALTER TABLE `land` ADD use_begin datetime comment '使用开始时间';
ALTER TABLE `land` ADD use_end datetime comment '使用结束时间';

--高斯
ALTER TABLE dwi_nas.dwi_nas_land ADD use_begin timestamp(6) without time zone ; 
ALTER TABLE dwi_nas.dwi_nas_land ADD use_end timestamp(6) without time zone ; 