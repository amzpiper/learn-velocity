package com.dotool.cloud.dlf.dwr;

import com.dotool.cloud.dlf.tool.MappingField;
import com.dotool.cloud.dlf.tool.MappingTable;
import com.dotool.cloud.dlf.tool.ReadMappingExcel;
import com.dotool.util.PublicTools;
import com.dotool.util.VelocityPubTool;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.List;

	public class CreateDLFScript {
	/*public static String author = "chenqm";
	public static String adtecDwiTimestampField = "marking_update";*/

	/*public static String author = "zhouxu";
	public static String adtecDwiTimestampField = "marking_update";*/

	/*public static String author = "yuepei";
	public static String adtecDwiTimestampField = "last_update_time";*/

	public static String author = "lihq";
	public static String adtecDwiTimestampField = "gmt_modified";

		/*public static String author = "guoyh";
		public static String adtecDwiTimestampField = "actual_sign_time";*/

//	public static String adtecDwiTimestampField = "marking_update";
//	public static String adtecDwiTimestampField = "adtecDwiTimestampField";

		//all 全量切片  add 增量切片
		public static String dataPartitionModel = "all";
// public static String dataPartitionModel = "add";

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
public static String fileName = "D:\\programing\\learn-velocity\\do-tool\\文件摸板\\03、系统设计\\详细设计\\主题层\\工单\\03Mapping\\DWR-工单-MAPPING映射-V1.0.xlsx";


//		public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\能耗\\03Mapping\\DM-能耗-MAPPING映射-V1.0.xlsx";
//		public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\主题\\医疗\\03Mapping\\DWR-医疗-MAPPING映射-V1.0.xlsx";
//		public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\主题\\工单\\03Mapping\\DWR-工单-MAPPING映射-V1.0.xlsx";



	public static void main(String[] args) throws Exception {
			List<MappingTable> tableList = ReadMappingExcel.read(fileName);
			System.out.println(tableList.size());

		File f = new File(fileName);
		String p = f.getParent();
		File pf = new File(p + "//..//sql//dlf1");
		if (pf.exists()) {
			pf.delete();
		}
		if (!pf.exists()) {
			pf.mkdirs();
		}

		File pf1 = new File(p + "//..//sql//dlf_init1");
		if (pf1.exists()) {
			pf1.delete();
		}
		if (!pf1.exists()) {
			pf1.mkdirs();
		}

		VelocityEngine ve = VelocityPubTool.getVelocityEngineByUtf8();


		for (MappingTable table : tableList) {
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

			File fileAll = new File(p + "/../sql/dlf1/dlf_" + des + desSuffix+ ".sql");
			fileAll.createNewFile();
			FileOutputStream fos = new FileOutputStream(fileAll);
			fos.write("".getBytes());
			fos.flush();
			fos.close();

			File fileAllInit = new File(p + "/../sql/dlf_init1/dlf_" + des + "(初始化)" + desSuffix + ".sql");
			fileAllInit.createNewFile();
			FileOutputStream fosInit = new FileOutputStream(fileAllInit);
			fosInit.write("".getBytes());
			fosInit.flush();
			fosInit.close();

			VelocityContext context = new VelocityContext();

			context.put("author", author);
			context.put("script_create_date", PublicTools.getCurrentTime("yyyy-MM-dd"));
			context.put("adtecDwiTimestampField", adtecDwiTimestampField);


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
			for (MappingField field : table.getFieldList()) {

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
			FileUtils.writeStringToFile(fileAllInit, scriptContent, "utf-8");


			/*PrintWriter pw = new PrintWriter(new FileWriter(fileAll, true));
			pw.write(scriptContent);
			pw.flush();
			pw.close();*/

		}

	}
	



}
