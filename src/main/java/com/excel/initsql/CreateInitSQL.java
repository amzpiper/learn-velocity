package com.excel.initsql;


import com.excel.util.PublicTools;
import com.excel.util.VelocityPubTool;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成测试数据
 */
public class CreateInitSQL {
/*public static String author = "chenqm";
public static String adtecDwiTimestampField = "marking_update";*/

/*public static String author = "zhouxu";
public static String adtecDwiTimestampField = "marking_update";*/

/*public static String author = "yuepei";
public static String adtecDwiTimestampField = "last_update_time";*/

/*public static String author = "renyi";
public static String adtecDwiTimestampField = "last_update_time";*/

    public static String author = "guoyh";
    public static String adtecDwiTimestampField = "actual_sign_time";

//	public static String adtecDwiTimestampField = "marking_update";
//	public static String adtecDwiTimestampField = "adtecDwiTimestampField";

    //all 全量切片  add 增量切片
//		public static String dataPartitionModel = "all";
public static String dataPartitionModel = "add";

//	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\企业\\03Mapping\\DWR-企业-MAPPING映射-V1.1.xlsx";

//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-企业服务-MAPPING映射-V1.0.xlsx";

//	public static String fileName = "E:\\ cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\能耗\\03Mapping\\DWR-能耗-MAPPING映射（能耗）-V1.0.xlsx";

//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-应急-MAPPING映射-V1.1.xlsx";



//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-安环监管-MAPPING映射-V1.0.xlsx";

//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-设备-MAPPING映射-V1.1.xlsx";

//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-事件-MAPPING映射-V1.0.xlsx";

//	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\工单\\03Mapping\\DWR-工单-MAPPING映射-V1.0.xlsx";

//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-企业-MAPPING映射-V1.1.xlsx";

//		public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-招商-MAPPING映射（招商）-V1.0.xlsx";
//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-资源-MAPPING映射-V1.3.xlsx";
//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-项目-MAPPING映射-V1.1.xlsx";
//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-组织-MAPPING映射-V1.1.xlsx";
//	public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-人员-MAPPING映射-V1.1.xlsx";
//public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-资产-MAPPING映射-V1.1.xlsx";
//public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-办公-MAPPING映射-V1.0.xlsx";
//public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DM-企业-MAPPING映射-V1.0.xlsx";
//public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-产业-MAPPING映射-V1.0.xlsx";


//		public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\能耗\\03Mapping\\DM-能耗-MAPPING映射-V1.0.xlsx";
//		public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\主题\\医疗\\03Mapping\\DWR-医疗-MAPPING映射-V1.0.xlsx";
    public static String fileName = "C:\\Users\\guoyh\\OneDrive\\桌面\\gdsyy用电信息 - 副本 - 副本.xlsx";


public static void main(String[] args) throws Exception {
        List<InitSQLTable> tableList = ReadInitSQLExcel.read(fileName);
        System.out.println(tableList.size());

    String p = "d:/Project/生成DLF代码/" + PublicTools.getCurrentTime("yyyyMMdd");
    File pf = new File(p);
    if (pf.exists()) {
        pf.delete();
    }
    if (!pf.exists()) {
        pf.mkdirs();
    }


    for (InitSQLTable table : tableList) {
        String tableName = table.getName();
        String sechma = table.getSechma();
        int index = 0;
        StringBuffer scriptSB = new StringBuffer();

        String des = table.getDes();
        des = des.replace("-", "_");

        String desSuffix = "";
        if (dataPartitionModel.equals("add")) {
            desSuffix = "_增量";
        }

        System.out.println(pf.getPath());
        File fileAll = new File(pf.getPath() + "/" + des + ".sql");
        fileAll.createNewFile();
        FileOutputStream fos = new FileOutputStream(fileAll);
        fos.write("".getBytes());
        fos.flush();
        fos.close();

        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

        String currentTime = PublicTools.getCurrentTime();
        String currentDay = PublicTools.getCurrentTime("yyyy-MM-dd");

        for (InitSQLRow row : table.getRowList()) {

            for (InitSQLField data : row.getFieldList()) {

            }

            StringBuilder insert1 = new StringBuilder();
            StringBuilder insert2 = new StringBuilder();
            insert1.append("insert into " + sechma + "." + tableName +"(");
            insert2.append(" values(");
            int b = 0;
            for(InitSQLField en : row.getFieldList()) {
                if(b++!=0) {
                    insert1.append(",");
                    insert2.append(",");
                }
                insert1.append("\"" + en.getName() + "\"");
                insert2.append("'" + en.getValue() + "'");

            }
            insert1.append(",\"dw_batch_number\",\"dw_creation_date\",\"dw_last_update_date\")");
            insert2.append(",'" + sdf1.format(new Date()) + "','" + currentDay + "','" + currentTime + "')");

            sb.append( insert1.toString() + insert2.toString() +";\n");
        }

        FileUtils.writeStringToFile(fileAll, sb.toString(), "utf-8");
/*

        context.put("table", table);
        context.put("fileds", table.getFieldNeedList());
        context.put("dwrTableName", table.getSechma() + "." + table.getName());
        context.put("dlf_script_dic", table.getSechma().split("_")[1]);
        context.put("dlf_script_des", "汇聚主题库：" + table.getDes());




        String dwiTableName = "";
        String dlf_script_name = "";
        String dlf_init_script_name = "";
        String dlf_job_name = "";
        String dwiId = "";
        String dwrId = "";
        StringBuffer dwrInsertSQLField = new StringBuffer("");
        StringBuffer dwiInsertSQLField = new StringBuffer("");
        for (InitSQLField field : table.getFieldList()) {

            index++;
            String cnName = field.getCnName();
            String enName = field.getName();

            String type = field.getType();
            String length = field.getLength();

            boolean fromFlag = field.isFromFlag();
            String fromSechma = field.getFromSechma();
            String fromTableName = field.getFromTableName();

            String fromName = field.getFromName();
            String fromType = field.getFromType();

            if (fromFlag && dwiTableName.equals("")) {
                String sourceName = fromSechma.replaceAll("dwi_", "");
                dwiTableName = fromSechma + "." + fromTableName;
                dlf_script_name = "script_" + tableName + "_" + sourceName;
                dlf_init_script_name = "script_init_" + tableName + "_" + sourceName;
                dlf_job_name = "job_" + tableName + "_" + sourceName;
                if(dlf_script_name.length() > 64) {
                    System.out.println("error==" + dlf_script_name);
                }
                if(dlf_init_script_name.length() > 64) {
                    System.out.println("error==" + dlf_init_script_name);
                }
                context.put("dwiTableName", dwiTableName);
                context.put("dwiTableNameNoSechma", fromTableName);
                context.put("dlf_script_name", dlf_script_name);
                context.put("dlf_init_script_name", dlf_init_script_name);
                context.put("dlf_job_name", dlf_job_name);
            }

            if (fromFlag) {
                dwrInsertSQLField.append(enName + ", ");
                dwiInsertSQLField.append("a." + fromName + ", ");
            }

            if (index == 2 ) {
                dwiId = fromName;
                dwrId = enName;
            }



        }

        dwrInsertSQLField.append("dw_creation_by,dw_creation_date,dw_last_update_by,dw_last_update_date,dw_batch_number");
        dwiInsertSQLField.append("'${job_name}','${job_plan_time}','${job_name}','${job_plan_time}',a.dw_batch_number");

        context.put("dwiId", dwiId);
        //能耗
        //context.put("dwiId", "concat(a.business_id, '_', a.device_code, '_', a.collect_time)");

        context.put("dwrId", dwrId);


        context.put("dwrInsertSQLField", dwrInsertSQLField);
        context.put("dwiInsertSQLField", dwiInsertSQLField);

        StringWriter sw = new StringWriter();
        Template dlfAll = ve.getTemplate( "/vm/dlf_" + dataPartitionModel + ".sql" , "UTF-8");
        dlfAll.merge(context,sw);
        String scriptContent = sw.toString();
        FileUtils.writeStringToFile(fileAll, scriptContent, "utf-8");

        StringWriter swAllInit = new StringWriter();
        Template dlfAllInit = ve.getTemplate( "/vm/dlf_" + dataPartitionModel + "_init.sql" , "UTF-8");
        dlfAllInit.merge(context,swAllInit);
        scriptContent = swAllInit.toString();
        FileUtils.writeStringToFile(fileAllInit, scriptContent, "utf-8");*/

/*
        VelocityContext context = new VelocityContext();

        context.put("author", author);
        context.put("script_create_date", PublicTools.getCurrentTime("yyyy-MM-dd"));
        context.put("adtecDwiTimestampField", adtecDwiTimestampField);*/


        /*PrintWriter pw = new PrintWriter(new FileWriter(fileAll, true));
        pw.write(scriptContent);
        pw.flush();
        pw.close();*/

    }

}




}
