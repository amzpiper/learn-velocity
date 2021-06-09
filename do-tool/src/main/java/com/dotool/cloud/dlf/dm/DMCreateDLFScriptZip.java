package com.dotool.cloud.dlf.dm;

import com.dotool.util.ZipUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

public class DMCreateDLFScriptZip {
	public static void main(String[] args) throws Exception {


		//		File file = new File("E:\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\");

//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\安环监管\\");
//				File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\工单");
//				File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\能耗\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\企业\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\企业服务");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\设备");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\事件");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\应急");



//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\招商");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\组织");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\人员");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\资源");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\资产");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\产业");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\办公");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\项目");

//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\能耗");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\医疗");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\设备");
		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题\\视频巡逻");



		createDwrScriptInit(file);
		createDwrScript(file);

	}
	public static void createDwrScriptInit(File file) throws Exception {
		for(File pf : getFileByFileName(file, "dlf_init")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\dm_"+pf.getParentFile().getParentFile().getName()+"(初始化)_dlf.zip"),Charset.forName("utf8"));
			for(File f : pf.listFiles()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf8"));

				String line = br.readLine();
				String lineHeader = br.readLine();

				Pattern p = Pattern.compile("--\\s+(?<g1>\\w+)\\s+(?<g2>\\w+)\\s+\\S+");
				Matcher matcher = p.matcher(lineHeader);

				StringBuilder content = new StringBuilder();

				//int i = 0;
				content.append(line);
				content.append("\\n");
				content.append(lineHeader);
				while(( line= br.readLine()) != null) {
					content.append("\\n");
					content.append(line);
				}
				br.close();
				while(matcher.find()) {
					StringBuilder sb = new StringBuilder();
					sb.append("{\n");
					sb.append("    \"configuration\":{},\n");
					sb.append("    \"connectionName\":\"smart_campus_data_warehouse\",\n");
					sb.append("    \"content\":\""+content.toString().replaceAll("\"", "\\\\\"")+"\",\n");
					sb.append("    \"currentDatabase\":\"data_service_db\",\n");
					sb.append("    \"description\":\"\",\n");
					sb.append("    \"directory\":\"/dws/ini/"+matcher.group("g1")+"\",\n");
					String scriptName = matcher.group("g2");
					scriptName = parseScriptName(scriptName);
					sb.append("    \"name\":\"" + scriptName + "\",\n");
					sb.append("    \"templateVersion\":\"1.0\",\n");
					sb.append("    \"type\":\"DWSSQL\"\n");
					sb.append("}");
					System.out.println(content.toString().replaceAll("\"", "\\\\\""));

					ZipUtils.stringToZip("scripts/dws/ini/"+matcher.group("g1")+"/"+ scriptName +".script", zos, sb.toString());

				}

			}
			zos.close();
		}
	}
	public static void createDwrScript(File file) throws Exception {
		for(File pf : getFileByFileName(file, "dlf")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\dm_"+pf.getParentFile().getParentFile().getName()+"_dlf.zip"),Charset.forName("utf8"));
			for(File f : pf.listFiles()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf8"));

				String line = br.readLine();
				String lineHeader = br.readLine();

				Pattern p = Pattern.compile("--\\s+(?<g1>\\w+)\\s+(?<g2>\\w+)\\s+\\S+");
				Matcher matcher = p.matcher(lineHeader);

				StringBuilder content = new StringBuilder();

				//int i = 0;
				content.append(line);
				content.append("\\n");
				content.append(lineHeader);
				while(( line= br.readLine()) != null) {
					content.append("\\n");
					content.append(line);

				}
				br.close();
				while(matcher.find()) {
					StringBuilder sb = new StringBuilder();
					sb.append("{\n");
					sb.append("    \"configuration\":{},\n");
					sb.append("    \"connectionName\":\"smart_campus_data_warehouse\",\n");
					sb.append("    \"content\":\""+content.toString().replaceAll("\"", "\\\\\"")+"\",\n");
					sb.append("    \"currentDatabase\":\"data_service_db\",\n");
					sb.append("    \"description\":\"\",\n");
					sb.append("    \"directory\":\"/dws/dm/"+matcher.group("g1")+"\",\n");
					String scriptName = matcher.group("g2");
					scriptName = parseScriptName(scriptName);
					sb.append("    \"name\":\"" + scriptName + "\",\n");
					sb.append("    \"templateVersion\":\"1.0\",\n");
					sb.append("    \"type\":\"DWSSQL\"\n");
					sb.append("}");
					System.out.println(content.toString().replaceAll("\"", "\\\\\""));

					ZipUtils.stringToZip("scripts/dws/dm/"+matcher.group("g1")+"/"+ scriptName +".script", zos, sb.toString());

				}

			}
			zos.close();
		}
	}
	public static List<File> getFileByFileName(File file,String fileName) {
		List<File> lf = new ArrayList<>();
		for(File cf : file.listFiles()) {
			if(cf.getName().equals(fileName) && cf.isDirectory()) {
				lf.add(cf);
				return lf;
			}else if(cf.isDirectory()) {
				lf.addAll(getFileByFileName(cf, fileName));
			}
		}
		return lf;
	}

	public static String parseScriptName(String scriptName) {

		//对不满足规则的，单独处理
		//dlf_应急物资(管理平台)_应急装备
		if ("script_dwd_emergency_emergency_material_f_dwi_pmp_m_emergency_equip".equalsIgnoreCase(scriptName)) {
			return "script_dwd_emergency_emergency_material_f_pmp_equip";
		} else if ("script_init_dwd_emergency_emergency_material_f_dwi_pmp_m_emergency_equip".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_emergency_emergency_material_f_pmp_equip";
		} else if ("script_init_dwd_enterprise_zhyq_enterprise_main_stockholder_f_dwi_pmp_zhyq_enterprise_main_stockholder".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_enterprise_zhyq_enterprise_main_stockholder_f_pm";
		} else if ("script_init_dwd_enterprise_zhyq_technology_innovation_policy_f_dwi_pmp_zhyq_technology_innovation_policy".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_enterprise_zhyq_technology_innovation_policy_f_p";
		}




		return scriptName;
	}

	/*public static String parseScriptName(String scriptName) {

		//对不满足规则的，单独处理
		//dlf_应急物资(管理平台)_应急装备
		if ("script_dwd_emergency_emergency_material_f_dwi_pmp_m_emergency_equip".equalsIgnoreCase(scriptName)) {
			return "script_dwd_emergency_emergency_material_f_pmp_equip";
		} else if ("script_init_dwd_emergency_emergency_material_f_dwi_pmp_m_emergency_equip".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_emergency_emergency_material_f_pmp_equip";
		} else if ("script_init_dwd_enterprise_zhyq_enterprise_main_stockholder_f_dwi_pmp_zhyq_enterprise_main_stockholder".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_enterprise_zhyq_enterprise_main_stockholder_f_pm";
		} else if ("script_init_dwd_enterprise_zhyq_technology_innovation_policy_f_dwi_pmp_zhyq_technology_innovation_policy".equalsIgnoreCase(scriptName)) {
			return "script_init_dwd_enterprise_zhyq_technology_innovation_policy_f_p";
		}


		String[] dwi_s = scriptName.split("_dwi_");
		String s = dwi_s[0];
		String e = dwi_s[1];
		scriptName = s + "_" + e.split("_")[0];

		return scriptName;
	}*/
}