/*
 * Created on 2004-4-6
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.dotool.util;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

/**
 * @author 陈雀明
 *
 * @version 1.0
 *
 * 时间工具类
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateUtil {


    /**
     * chenqm
     * test
     */
    public DateUtil() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static Date parse(String strDate){
        Date date;
        SimpleDateFormat sdf=new SimpleDateFormat();

        
        if(strDate.indexOf("-") != -1){
        	
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        try{        	
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        	
        }

        sdf.applyPattern("yyyy-MM-dd");
        try{        	
            date=sdf.parse(strDate);
            System.out.println("" + date);
            return date;
        }catch(Exception e){
        	
        }
        }

//        sdf.applyPattern("yyyy/MM/dd");
//        try{
//            date=sdf.parse(strDate);
//            System.out.println("" + date);
//            return date;
//        }catch(Exception e){
//        }
        
        if(strDate.indexOf("/") != -1){
        
        sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
        try{
        	System.out.println("999999999");
           String xx =  strDate.substring(strDate.lastIndexOf("/"));
            if(xx.length() > 2){
                sdf.applyPattern("yyyy/MM/dd HH:mm:ss");
                date=sdf.parse(strDate);
            }else{
                date=sdf.parse(strDate);
            }

            System.out.println("" + date);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy/MM/dd");
        try{
           String xx =  strDate.substring(strDate.lastIndexOf("/"));
            if(xx.length() > 2){
                sdf.applyPattern("dd/MM/yyyy");
                date=sdf.parse(strDate);
            }else{
                date=sdf.parse(strDate);
            }

            System.out.println("" + date);
            return date;
        }catch(Exception e){
        }
        }

        sdf.applyPattern("yyyyMMdd");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy年MM月dd日");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy年MM月");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy年WW周");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy年");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }





        sdf.applyPattern("yyyyMM");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }

        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
        try{
            date=sdf.parse(strDate);
            return date;
        }catch(Exception e){
        }


        return null;
    }

    public static Date parseDate(String strDate , String pattern){
        Date date;
        SimpleDateFormat sdf=new SimpleDateFormat();


        sdf.applyPattern(pattern);
        try{
            date=sdf.parse(strDate);
            //System.out.println("" + date);
            return date;
        }catch(Exception e){
        }


        return null;
    }


    /**
     * @author 陈雀明
     * 格式化日期
     * @param date
     * @return
     */
    public  static String parse(Date date,String pattern)
    {
        String strDate;
        SimpleDateFormat sdf=new SimpleDateFormat();

        sdf.applyPattern(pattern);
        try{

            strDate = sdf.format(date);
            return strDate;
        }catch(Exception e){
        }

        return null;
    }


    /**
     * @author 陈雀明
     * 格式化日期
     * @param date
     * @return
     */
    public  static String parse(String date,String pattern)
    {
        try
        {
            return parse(parse(date),pattern);
        }
        catch (Exception e)
        {

        }

        return null;
    }



    public static Date composeDate(String year, String month, String day) throws Exception{
        int y=0,m=0,d=0;
        try{
            y=Integer.parseInt(year);
            m=Integer.parseInt(month);
            d=Integer.parseInt(day);

            Calendar c=Calendar.getInstance();
            c.set(y,m,d,0,0,0);
            return c.getTime();
        }catch(Exception e){
            throw new Exception("时间格式不正确");
        }
    }

    public static String composeDate(String pattern, String year, String month, String day) throws Exception{
        int y=0,m=0,d=0;
        String sy, sm, sd;
        try{
            y=Integer.parseInt(year);
            m=Integer.parseInt(month)-1;
            d=Integer.parseInt(day);

            Calendar c=Calendar.getInstance();
            c.set(y,m,d,0,0,0);
            c.getTime();//let calendar to recalculate

            if(pattern=="YYYYMMDD"){
                sy=String.valueOf(c.get(Calendar.YEAR));
                sm=String.valueOf(c.get(Calendar.MONTH)+1);
                if(sm.length()==1)sm="0"+sm;

                sd=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                if(sd.length()==1)sd="0"+sd;

                System.out.println(sy+sm+sd);

                return sy+sm+sd;
            }
            return "";
        }catch(Exception e){
            throw new Exception("时间格式不正确");
        }
    }

    public static Date increase(Date sdate, String UnitType , int length){
        Date rdate=null;

        UnitType=UnitType.trim();
        Calendar c=Calendar.getInstance();
        c.setTime(sdate);

        if((UnitType.compareToIgnoreCase("年")==0) || UnitType.compareToIgnoreCase("year")==0){
            c.set(Calendar.YEAR,c.get(Calendar.YEAR)+length);
            return c.getTime();
        }else if((UnitType.compareToIgnoreCase("月")==0) || (UnitType.compareToIgnoreCase("month")==0)){
            c.set(Calendar.MONTH,c.get(Calendar.MONTH)+length);
            return c.getTime();
        }else if((UnitType.compareToIgnoreCase("周")==0) || (UnitType.compareToIgnoreCase("week")==0)){
            c.set(Calendar.WEEK_OF_MONTH,c.get(Calendar.WEEK_OF_MONTH)+length);
            return c.getTime();
        }else if((UnitType.compareToIgnoreCase("日")==0) || (UnitType.compareToIgnoreCase("day")==0)){
            c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+length);
            return c.getTime();
        }else if((UnitType.compareToIgnoreCase("时")==0) || (UnitType.compareToIgnoreCase("hour")==0)){
            c.set(Calendar.HOUR,c.get(Calendar.HOUR)+length);
            return c.getTime();
        }
        return rdate;
    }

    /**
     * 添加一个单位
     * @param sdate
     * @param UnitType
     * @return
     */
    public static Date increase(Date sdate, String UnitType){

        return increase(sdate , UnitType , 1);
    }

    public static boolean isTime(String stime){
        if(parse(stime)==null)return false;
        else return true;
    }


    /**
     * @author 孙业海
     * @version 1.0
     *
     * 根据开始时间和结束时间的标识，构造按照月来统计
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public static String composeDate(String pattern, String year, String month, String day,String type)
            throws Exception{
        int y=0,m=0,d=0;
        String sy, sm, sd;
        try{
            Calendar c=Calendar.getInstance();
            y=Integer.parseInt(year);
            m=Integer.parseInt(month)-1;
            if (day==null || day.trim().compareTo("")==0)
            {
                if (type=="start")
                {
                    day="1";
                }else if (type=="end")
                {
                    c.set(y,m,1);
                    day=String.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
            }
            d=Integer.parseInt(day);

            c.set(y,m,d,0,0,0);
            c.getTime();//let calendar to recalculate

            if(pattern=="YYYYMMDD"){
                sy=String.valueOf(c.get(Calendar.YEAR));
                sm=String.valueOf(c.get(Calendar.MONTH)+1);
                if(sm.length()==1)sm="0"+sm;

                sd=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                if(sd.length()==1)sd="0"+sd;

                System.out.println(sy+sm+sd);

                return sy+sm+sd;
            }
            return "";
        }catch(Exception e){
            throw new Exception("时间格式不正确");
        }
    }


    public static final  String formatDate(String dateStr)
    {
        String temp = "";
        if(dateStr.length()>8)
        {
            try{
                temp =  dateStr.substring(0,10);
                temp = temp.substring(0,4) + temp.substring(5,7) + temp.substring(8,10);
            }
            catch(Exception ce)
            {

            }
        }

        return temp;

    }



    //得到日期类型的第一天
    public static Date getFirst(Date sdate ,  String UnitType , String date)
    {
        return getFirst(sdate , UnitType , 1 , Integer.parseInt(date));
    }

    //得到日期类型的第一天
    public static Date getFirst(Date sdate ,  String UnitType , int num , int date)
    {
        try
        {
            sdate = increase(sdate , UnitType , num);
            Calendar c = Calendar.getInstance();
            c.setTime(sdate);


            if((UnitType.compareToIgnoreCase("周")==0) || (UnitType.compareToIgnoreCase("week")==0)){
                c.set(Calendar.DAY_OF_WEEK,date);
                sdate = c.getTime();
            }
            else if((UnitType.compareToIgnoreCase("月")==0) || (UnitType.compareToIgnoreCase("month")==0)){
                c.set(Calendar.DAY_OF_MONTH,date);
                sdate = c.getTime();
            }
        }
        catch(Exception ce)
        {

        }

        return sdate ;
    }



    //返回给定日期的月份,从0开始.
    public static int getMonth(Date sdate)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(sdate);
        return c.get(Calendar.MONTH);
    }

    //返回给定日期的天,从1开始.
    public static int getDate(Date sdate)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(sdate);
        return c.get(Calendar.DATE);
    }


    public static Date setTime(Date sdate , String time)
    {
        if(time == null || time.indexOf(":") == -1)
        {
            return sdate;
        }

        Calendar c=Calendar.getInstance();
        c.setTime(sdate);
        String[] tmp = time.split(":");
        c.set(Calendar.HOUR_OF_DAY , Integer.parseInt(tmp[0]));
        c.set(Calendar.MINUTE , Integer.parseInt(tmp[1]));
        c.set(Calendar.SECOND , Integer.parseInt(tmp[2]));

        return c.getTime();
    }


    public static String parseDate (String date){
        String year =  "";
        String month =  "";
        String day =  "";
        String retDate = date;
        String temptimes[] = date.split("/");
        if(temptimes.length > 0){
            if(temptimes.length == 3){
                 year =  temptimes[2];
                 month =  temptimes[0];
                 day =  temptimes[1];
                 retDate = year+"/"+month+"/"+day;
            }
        }
        return retDate;
    }

    public static boolean checkIsThisWeek(String date){
        boolean b = false;
        Date thisDate = parse(date);
        Date nowDate = new Date();

        Calendar cThisDate = Calendar.getInstance();
        cThisDate.setTime(thisDate);
        Calendar cNowDate = Calendar.getInstance();
        cNowDate.setTime(nowDate);

        int wThisDate = cThisDate.get(Calendar.WEEK_OF_YEAR);
        int wNowDate = cNowDate.get(Calendar.WEEK_OF_YEAR);

        int yearThisDate = cThisDate.get(Calendar.YEAR);
        int yearNowDate = cNowDate.get(Calendar.YEAR);

        if(yearThisDate == yearNowDate && wThisDate <= wNowDate){
            b = true;
        }

        return b;
    }

    public static ArrayList<String> getDateList(String startDateStr , String endDateStr){
        ArrayList<String> list = new ArrayList<String>();

        Date startDate = parseDate(startDateStr , "yyyy-MM-dd");
        Date endDate = parseDate(endDateStr , "yyyy-MM-dd");



        Date tempDate = startDate;
        while(endDate.compareTo(tempDate) >= 0){
           list.add(parse(tempDate , "yyyy-MM-dd"));

           tempDate = increase(tempDate , "day");
        }

        

        return list;
    }


    public static void main(String []args){
        //System.out.println (DateUtil.getWorkDayNum(DateUtil.parse("2006年4月7日"),DateUtil.parse("2006年4月17日")));
        System.out.println("" + parse(parse("2005-02-01"),"yy/MM/dd"));
    }






}

