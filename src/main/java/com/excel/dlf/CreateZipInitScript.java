package com.excel.dlf;

import com.excel.util.ZipUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;


public class CreateZipInitScript {
	public static void main(String[] args) throws Exception {
//		File file = new File("E:\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�ܺ�\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�������\\");
		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��ҵ\\");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\��ҵ����");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�¼�");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\Ӧ��");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\�豸");
//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����");

//		File file = new File("E:\\cc_project\\nwim\\SmartPark\\�����ɽ\\03��ϵͳ���\\��ϸ���\\�����\\����");

		for(File pf : getFileByFileName(file, "dlf_init")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\dwr_"+pf.getParentFile().getParentFile().getName()+"(��ʼ��)_dlf.zip"),Charset.forName("utf8"));
			for(File f : pf.listFiles()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf8"));
				
				String line = br.readLine();
				
				Pattern p = Pattern.compile("--\\s+(?<g1>\\w+)\\s+(?<g2>\\w+)\\s+\\S+");
				Matcher matcher = p.matcher(line);
				
				StringBuilder content = new StringBuilder();
				
				int i = 0;
				while(( line= br.readLine()) != null) {
					if(i++ != 0) {
						content.append("\\n");
					}
					content.append(line);
					
				}
				br.close();
				while(matcher.find()) {
					StringBuilder sb = new StringBuilder();
					sb.append("{\n");
					sb.append("    \"configuration\":{},\n");
					sb.append("    \"connectionName\":\"smart_campus_data_warehouse\",\n");
					sb.append("    \"content\":\""+content+"\",\n");
					sb.append("    \"currentDatabase\":\"data_service_db\",\n");
					sb.append("    \"description\":\"\",\n");
					sb.append("    \"directory\":\"/adtec/init/dwr/"+matcher.group("g1")+"\",\n");
					sb.append("    \"name\":\"" + matcher.group("g2") + "\",\n");
					sb.append("    \"templateVersion\":\"1.0\",\n");
					sb.append("    \"type\":\"DWSSQL\"\n");
					sb.append("}");
					System.out.println(content);
					
					ZipUtils.stringToZip("scripts/adtec/init/dwr/"+matcher.group("g1")+"/"+matcher.group("g2")+".script", zos, sb.toString());
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
}
