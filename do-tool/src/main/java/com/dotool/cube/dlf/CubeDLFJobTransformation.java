package com.dotool.cube.dlf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;

import com.dotool.util.FileUtil;
import com.dotool.util.VelocityPubTool;

/**
 * 私有云job创建
 * 
 * @author yuepei
 *
 */
public class CubeDLFJobTransformation {
	
	static Pattern p = Pattern.compile("(?<pre>\\w+?)_dwi_(?<suff>\\w+?)_\\w+");
	static Map<String,String> map = new HashMap<String, String>();
	static {
		map.put("安环监管","ses");
		map.put("办公","office");
		map.put("产业","property");
		map.put("工单","order");
		map.put("能耗","energy");
		map.put("企业","enterprise");
		map.put("企业服务","ser");
		map.put("人员","person");
		map.put("设备","facility");
		map.put("时空","space");
		map.put("事件","event");
		map.put("项目","project");
		map.put("应急","emergency");
		map.put("招商","investment");
		map.put("资产","asset");
		map.put("资源","res");
		map.put("组织","org");
		
	}
	
	public static void main(String[] args) throws Exception {

		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题");
		List<File> listDir = FileUtil.getDirByDirName(file, "dlf_job");
		
		VelocityEngine ve = VelocityPubTool.getVelocityEngineByUtf8();
		Template t = ve.getTemplate("/vm/job_private_cube.vm", "UTF-8");
		for (File dir : listDir) {
			String rootPath = dir.getParentFile().getPath() + "/dlf_job_new/";
			File pf = new File(rootPath);
			if (pf.exists()) {
				pf.delete();
			}
			if (!pf.exists()) {
				pf.mkdirs();
			}

			List<File> listFile = FileUtil.getFileByFileName(dir, "job");
			List<Map<String, String>> list = new ArrayList<>();
			for (File f : listFile) {
				FileInputStream fis = new FileInputStream(f);
				InputStreamReader sr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(sr);
				String temp = null;
				StringBuilder sb = new StringBuilder();
				while((temp = br.readLine()) != null) {
					if(!temp.startsWith("//")) {
						sb.append(temp);
					}
				}
				JSONObject json = new JSONObject(sb.toString());
				Map item = toMap(json);
				list.add(item);
				
				br.close();
				sr.close();
				fis.close();
				
				//System.out.println(sw.toString());

				VelocityContext context = new VelocityContext();
				context.put("item", item);

				StringWriter sw = new StringWriter();
				t.merge(context, sw);



				PrintWriter printWriter = new PrintWriter(dir.getParentFile().getPath() + "/dlf_job_new/" + f.getName());
				printWriter.write(new String(sw.toString().getBytes(),"utf-8"));
				printWriter.flush();
				printWriter.close();
			}

			//System.out.println(dir.getParentFile().getParentFile().getName());
		}
		
	}

	public static Map toMap(JSONObject jsonMap) {
		Map map = new HashMap();
		try {
			String name = jsonMap.get("name").toString();
			System.out.println(name);
			//Matcher mat = p.matcher(name);
			/*
			 * if(mat.find()) { map.put("name",mat.group("pre")+"_"+mat.group("suff")); }
			 */
			map.put("name", name);
			//Matcher matScript = p.matcher(jsonMap.getJSONArray("nodes").getJSONObject(0).getString("name"));
			String scriptName = jsonMap.getJSONArray("nodes").getJSONObject(0).getString("name");
			/*
			 * if(matScript.find()) { scriptName =
			 * matScript.group("pre")+"_"+matScript.group("suff"); }
			 */
			map.put("startTime", jsonMap.getJSONObject("schedule").getJSONObject("cron").get("startTime").toString());
			map.put("interval", "1 days");
			String cronExpression = jsonMap.getJSONObject("schedule").getJSONObject("cron").get("expression").toString();
			System.out.println(cronExpression);
			map.put("cronExpression", cronExpression);

			String schedule_period = "day";
			String current_day_format = "%Y-%m-%d";
//			String current_day_format = "%Y-%m-%d %H:%M:%S";
			if(cronExpression.indexOf("*/15 0-23") != -1) {
				schedule_period = "minute";
			    current_day_format = "%Y-%m-%d %H:%M:%S";
			} else if(cronExpression.indexOf("0-23/1") != -1) {
				schedule_period = "hour";
				current_day_format = "%Y-%m-%d %H:%M:%S";
			}
			System.out.println(schedule_period);
			System.out.println(current_day_format);

			map.put("schedule_period", schedule_period);
			map.put("current_day_format", current_day_format);

//			map.put("cronExpression", "00 20 2 * * ?");

			map.put("uuid", UUID.randomUUID().toString());
			map.put("path", jsonMap.get("directory").toString());
			map.put("pathName", map.get("path") + "/" + scriptName);
			map.put("scriptName", scriptName);
			map.put("planTime", "2020-05-01");
			map.put("job", jsonMap);
		} catch (Exception ce) {
			ce.printStackTrace();
		}
		return map;
	}

}
