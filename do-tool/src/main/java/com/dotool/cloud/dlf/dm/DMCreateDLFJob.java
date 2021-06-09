package com.dotool.cloud.dlf.dm;

import com.dotool.cloud.dlf.tool.MappingField;
import com.dotool.cloud.dlf.tool.MappingTable;
import com.dotool.cloud.dlf.tool.ReadMappingExcel;
import com.dotool.util.VelocityPubTool;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.List;

public class DMCreateDLFJob {



	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-企业-MAPPING映射-V1.1.xlsx";
    public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-企业服务-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\能耗\\03Mapping\\DWR-能耗-MAPPING映射（能耗）-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-安环监管-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 45 2";*/


	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-事件-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-应急-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-设备-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\招商\\03Mapping\\DWR-招商-MAPPING映射（招商）-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\工单\\03Mapping\\DWR-工单-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 40 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-资源-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-项目-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\招商\\03Mapping\\DWR-招商-MAPPING映射（招商）-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-组织-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 2 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-人员-MAPPING映射-V1.1.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-资产-MAPPING映射-V1.1.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-产业-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 01 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\第二批次\\DWR-办公-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\能耗\\03Mapping\\DM-能耗-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\医疗\\03Mapping\\DM-医疗-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\视频巡逻\\03Mapping\\DM-视频巡逻-MAPPING映射-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";

	public static void main(String[] args) throws Exception {




		List<MappingTable> tableList = ReadMappingExcel.read(fileName);
			System.out.println(tableList.size());



		File f = new File(fileName);
		String p = f.getParent();
		File pf = new File(p + "//..//sql//dlf_job_new1");
		if (!pf.exists()) {
			pf.mkdirs();
		}

		VelocityEngine ve = VelocityPubTool.getVelocityEngineByUtf8();
		Template t = ve.getTemplate( "/vm/dlf_job_dm.vm" , "UTF-8");;

		for (MappingTable table : tableList) {
			String tableName = table.getName();
			String sechma = table.getSechma();
			int index = 0;
			StringBuffer scriptSB = new StringBuffer();

			String des = table.getDes();
			des = des.replace("-", "_");



			VelocityContext context = new VelocityContext();

			context.put("table", table);
			context.put("dwrTableName", table.getSechma() + "." + table.getName());
			context.put("dlf_script_dic", table.getSechma().split("_")[1]);
			context.put("dlf_script_des", "汇聚主题库：" + table.getDes());
			context.put("jobRunTime", jobRunTime);




			String dwiTableName = "";
			String dlf_script_name = "";
			String dlf_job_name = "";
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
					dlf_job_name = "job_" + tableName + "_" + sourceName;
					context.put("dwiTableName", dwiTableName);
					context.put("dlf_script_name", dlf_script_name);
					context.put("dlf_job_name", dlf_job_name);
				}

				if (fromFlag) {
					dwrInsertSQLField.append(enName + ", ");
					dwiInsertSQLField.append("a." + fromName + ", ");
				}



			}

			File file = new File(p + "/../sql/dlf_job_new1/" + dlf_job_name + ".job");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write("".getBytes());
			fos.flush();
			fos.close();

			dwrInsertSQLField.append("dw_creation_by,dw_creation_date,dw_last_update_by,dw_last_update_date,dw_batch_number");
			dwiInsertSQLField.append("'${job_name}','${job_plan_time}','${job_name}','${job_plan_time}',a.dw_batch_number");

			context.put("dwrInsertSQLField", dwrInsertSQLField);
			context.put("dwiInsertSQLField", dwiInsertSQLField);

			StringWriter sw = new StringWriter();
			t.merge(context,sw);
			String scriptContent = sw.toString();


			PrintWriter pw = new PrintWriter(new FileWriter(file, true));
			pw.write(scriptContent);
			pw.flush();
			pw.close();

		}

	}

}
