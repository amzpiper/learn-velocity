package com.excel.cube.dlf.dm;

import com.excel.util.ZipUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

public class CubeDMDLFScriptTransformation {

	public static void main(String[] args) throws Exception {
		File file = new File("E:\\SVN仓库\\广东省二医\\03、系统设计\\详细设计\\专题\\能耗");
		
//		createDwrScriptInit(file);
		createDwrScript(file);
		
	}
	public static void createDwrScriptInit(File file) throws Exception {
		for(File pf : getFileByFileName(file, "dlf_init")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\dwr_"+pf.getParentFile().getParentFile().getName()+"_dlf_init.zip"),Charset.forName("utf8"));
			for(File f : pf.listFiles()) {
				BASE64Encoder encoder = new BASE64Encoder();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf8"));

				String line = br.readLine();
				String lineHeader = br.readLine();

				Pattern p = Pattern.compile("--\\s+(?<g1>\\w+)\\s+(?<g2>\\w+)\\s+\\S+");
				Matcher matcher = p.matcher(lineHeader);

				StringBuilder content = new StringBuilder();

				//int i = 0;
				content.append(line);
				while(( line= br.readLine()) != null) {
					content.append("\\n");
					content.append(line);
				}
				br.close();
				while(matcher.find()) {

					String s = content.toString();
					String encodedText = encoder.encode(s.getBytes());

					BASE64Decoder decoder = new BASE64Decoder();
					System.out.println("=====================");
					System.out.println(new String(decoder.decodeBuffer(encodedText), "utf8"));
					System.out.println("=====================");


					StringBuilder sb = new StringBuilder();
					sb.append("{\n");
					sb.append("    \"canvasVersion\":\"630\",\n");
					sb.append("    \"content\":\""+ s +"\",\n");
					sb.append("    \"dbName\":\"bishan_dws\",\n");
					sb.append("    \"dwName\":\"GaussDB_200_2383\",\n");
					sb.append("    \"description\":\"组织11201520\",\n");
					sb.append("    \"id\":\"\",\n");
					sb.append("    \"param\":{},\n");
					sb.append("    \"purviewsLevel\":\"0\",\n");
					sb.append("    \"scriptName\":\"/dws/ini/"+matcher.group("g1")+"/" + matcher.group("g2") + "\",\n");
					sb.append("    \"scriptSubType\":\"DWS\",\n");
					sb.append("    \"type\":\"DWSSQL\"\n");
					sb.append("}");
					System.out.println(content.toString().replaceAll("\"", "\\\\\""));
					
					ZipUtils.stringToZip("scripts/dws/init/dm/"+matcher.group("g1")+"/"+matcher.group("g2")+".json", zos, sb.toString());
					
				}
				
			}
			zos.close();
		}
	}
	public static void createDwrScript(File file) throws Exception {
		for(File pf : getFileByFileName(file, "dlf")) {
			System.out.println(pf);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pf.getParent() + "\\cube_dm_"+pf.getParentFile().getParentFile().getName()+"_dlf.zip"),Charset.forName("utf8"));
			for(File f : pf.listFiles()) {
				System.out.println(f.getPath());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf8"));

				String line = br.readLine();
				String lineHeader = br.readLine();

				Pattern p = Pattern.compile("--\\s+(?<g1>\\w+)\\s+(?<g2>\\w+)\\s+\\S+");
				Matcher matcher = p.matcher(lineHeader);

				StringBuilder content = new StringBuilder();

				//int i = 0;
				content.append(line);
				while(( line= br.readLine()) != null) {
					content.append("\\n");
					content.append(line);
				}
				br.close();
				while(matcher.find()) {
					StringBuilder sb = new StringBuilder();
					sb.append("{\n");
//					sb.append("    \"configuration\":{},\n");
					sb.append("    \"connectionName\":\"smart_campus_data_warehouse\",\n");
					sb.append("    \"content\":\"" + content +"\",\n");
					sb.append("    \"database\":\"DOGAUSSDB\",\n");
//					sb.append("    \"description\":\"\",\n");
					sb.append("    \"directory\":\"/dws/dm/"+matcher.group("g1")+"\",\n");
					sb.append("    \"name\":\"" + matcher.group("g2") + "\",\n");
//					sb.append("    \"templateVersion\":\"1.0\",\n");
					sb.append("    \"type\":\"gauss100\"\n");
					sb.append("}");
					System.out.println(content.toString().replaceAll("\"", "\\\\\""));
					
					ZipUtils.stringToZip("scripts/dws/dm/"+matcher.group("g1")+"/"+matcher.group("g2")+".script", zos, sb.toString());
					
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
