

-- chenqm add 2020-05-31
CREATE OR REPLACE FUNCTION public.verification_job_current_day_return_last_date(u_current_day VARCHAR(100))  RETURNS VARCHAR
AS $$
BEGIN
	IF (u_current_day is NULL)
  THEN
      u_current_day = TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd');
  END IF;
  
  return TO_CHAR((to_date(u_current_day) + interval '-1 D'), 'yyyy-MM-dd');
END;
$$ LANGUAGE plpgsql;


-- chenqm add 2020-05-31
CREATE OR REPLACE FUNCTION public.verification_job_current_day(u_current_day VARCHAR(100))  RETURNS VARCHAR
AS $$
BEGIN
	IF (u_current_day is NULL)
  THEN
      u_current_day = TO_CHAR(CURRENT_TIMESTAMP, 'yyyy-MM-dd');
  END IF;
  
  return TO_CHAR((to_date(u_current_day)), 'yyyy-MM-dd');
END;
$$ LANGUAGE plpgsql;

