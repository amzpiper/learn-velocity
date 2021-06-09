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


		//		File file = new File("E:\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\");

//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�������\\");
//				File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����");
//				File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�ܺ�\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��ҵ\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��ҵ����");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�豸");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�¼�");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\Ӧ��");



//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��֯");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��Ա");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��Դ");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�ʲ�");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��ҵ");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�칫");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��Ŀ");

//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\�ܺ�");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\ҽ��");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\�豸");
		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�㶫ʡ��ҽ\\03��ϵͳ���\\��ϸ���\\ר��\\��ƵѲ��");



		createDwrScriptInit(file);
		createDwrScript(file);

	}
	public static void createDwrScriptInit(File file) throws Exception {
		for(File pf : getFileByFileName(file, "dlf_init")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\dm_"+pf.getParentFile().getParentFile().getName()+"(��ʼ��)_dlf.zip"),Charset.forName("utf8"));
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

		//�Բ��������ģ���������
		//dlf_Ӧ������(����ƽ̨)_Ӧ��װ��
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

		//�Բ��������ģ���������
		//dlf_Ӧ������(����ƽ̨)_Ӧ��װ��
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