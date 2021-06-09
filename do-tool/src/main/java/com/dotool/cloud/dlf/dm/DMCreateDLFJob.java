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



	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-��ҵ-MAPPINGӳ��-V1.1.xlsx";
    public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-��ҵ����-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�ܺ�\\03Mapping\\DWR-�ܺ�-MAPPINGӳ�䣨�ܺģ�-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-�������-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 45 2";*/


	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-�¼�-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-Ӧ��-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200527\\DWR-�豸-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����\\03Mapping\\DWR-����-MAPPINGӳ�䣨���̣�-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����\\03Mapping\\DWR-����-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 40 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-��Դ-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-��Ŀ-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����\\03Mapping\\DWR-����-MAPPINGӳ�䣨���̣�-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-��֯-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 2 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-��Ա-MAPPINGӳ��-V1.1.xlsx";
	public static String jobRunTime = "00 10 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-�ʲ�-MAPPINGӳ��-V1.1.xlsx";
	public static String jobRunTime = "00 20 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-��ҵ-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 01 2";*/

	/*public static String fileName = "C:\\Users\\chenqm\\Desktop\\20200519\\20200601\\�ڶ�����\\DWR-�칫-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\�ܺ�\\03Mapping\\DM-�ܺ�-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 50 2";*/

	/*public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\ҽ��\\03Mapping\\DM-ҽ��-MAPPINGӳ��-V1.0.xlsx";
	public static String jobRunTime = "00 30 2";*/

	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\��ƵѲ��\\03Mapping\\DM-��ƵѲ��-MAPPINGӳ��-V1.0.xlsx";
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
			context.put("dlf_script_des", "�������⣺" + table.getDes());
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
