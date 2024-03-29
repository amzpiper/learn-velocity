package com.dotool.cube.createtable.dwi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 贴源湖表和测试sql生成
 * @author yuepe
 *
 */
public class CubeCreateDWISQL {
	static HashMap<String,String> map = new HashMap<String, String>();
	static List<String> list = new ArrayList<String>();
	static{
		list.add("dw_creation_by");
		list.add("dw_creation_date");
		list.add("dw_last_update_by");
		list.add("dw_last_update_date");
		list.add("dw_batch_number");
		list.add("dw_data_source_id");
		list.add("dw_data_source");
	}
	static {
		map.put("园区管理平台","1");
		map.put("园区服务平台","2");
		map.put("视频安防","3");
		map.put("智慧安防","3");
		map.put("环保监测","4");
		map.put("能耗监测","5");
		map.put("智慧消防","6");
		map.put("国有资产","7");
		map.put("安全巡检","8");
		map.put("信息发布","9");
		map.put("物联网平台","10");
		map.put("GIS","11");
		map.put("视频存储","12");
		map.put("共享交换","13");
		map.put("大数据","14");
		map.put("市级对接","15");
		map.put("统一认证","16");
		map.put("数据总线","17");
		map.put("云计算","18");
		map.put("安全系统","19");
		map.put("指挥中心","20");
		map.put("IOC","21");

	}
	public static void main(String[] args) throws Exception {

		String filePath = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\服务平台\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（服务平台）-V1.0.xlsx";


		String[] paths = {
//				"E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\贴源层\\医疗\\数据及集成服务-DWI设计汇总表（医疗BI）-V1.0.xlsx",
//				"E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\贴源层\\ABC\\数据及集成服务-DWI设计汇总表（安防）-V1.0.xlsx",
//				"E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\贴源层\\能耗\\数据及集成服务-DWI设计汇总表（能耗）-V1.0.xlsx",
				"E:\\SVN仓库\\301医院\\03、系统设计\\详细设计\\贴源层\\一碑\\融通地产-一碑-数据及集成服务-DWI设计汇总表-V1.0.xlsx"
//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\服务平台\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（园区服务平台）-V1.1.xlsx",

//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\管理平台（飞企）\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（园区管理平台_飞企）-V1.1.xlsx"
//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\管理平台（普天）\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（园区管理平台_普天）-V1.2.xlsx",
//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\国有资产\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（国有资产）-V1.1.xlsx",
				//"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\环保\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（环保监测）-V1.0.xlsx",
//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\能耗监测\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（能耗监测）-V1.0.xlsx"
//				"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\消防\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（智慧消防）-V1.2.xlsx",
				//"E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\智慧安防\\重庆璧山高新区智慧园区-数据及集成服务-DWI设计汇总表（智慧安防）-V1.0.xlsx"
		};

		for (String path : paths) {
			System.out.println(path);
			createDWISQL(path);
			createSQL(path);
		}

	}

	public static void createDWISQL(String filePath) throws Exception {
		File f = new File(filePath);
		String p = f.getParent();
		File pf = new File(p + "//sql");
		if (!pf.exists()) {
			pf.mkdirs();
		}

		File fileInsert = new File(p + "/sql/dwr_test.txt");
		if(fileInsert.exists()) {
			fileInsert.delete();
		}

		File file = new File(p + "/sql/dwi_sql.txt");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write("".getBytes());
		fos.flush();
		fos.close();
		Workbook wb = new XSSFWorkbook(filePath);
		Sheet st01 = wb.getSheet("数据资源");

		int last = st01.getLastRowNum();
		// last = 9;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		for (int i = 2; i <= last; i++) {

			Row row = st01.getRow(i);

			Cell c2 = row.getCell(2);
			Cell c4 = row.getCell(4);
			if (!"是".equals(c4.toString().replaceAll("\\s+", ""))) {
				continue;
			}

			if (checkCell(c2.getStringCellValue())) {
				Cell tableNameCell = row.getCell(20);
				Cell schema = row.getCell(19);
				System.out.println(c2 + "-" + tableNameCell);
				Sheet sheet = wb.getSheet(c2.getStringCellValue());
				StringBuffer sb = new StringBuffer();
				StringBuffer common = new StringBuffer();

				sb.append("\n\n-- " + c2.getStringCellValue() + " --\n");
				sb.append("DROP TABLE IF EXISTS " + schema + "." + getString(tableNameCell).toLowerCase() + ";\n");
				sb.append("CREATE TABLE IF NOT EXISTS " + schema + "." + getString(tableNameCell).toLowerCase()
						+ "\n(\n");

				int count = sheet.getLastRowNum();

				String id = "";
				String tableCommon = "";
				Map<String,String> map1 = new LinkedHashMap<String, String>();
				for (int a = 2; a <= count; a++) {
					Row cr = sheet.getRow(a);
					// System.out.println(a);
					if (null == cr || null == cr.getCell(2) || !checkCell(cr.getCell(2).getStringCellValue())) {
						break;
					}
					if(list.contains(getString(cr.getCell(2)).replaceAll("\\s+", "").toLowerCase())) {
						continue;
					}

					String length = getString(cr.getCell(5));
					if(length.indexOf(".") != -1) {
						length = length.substring(0,length.indexOf("."));
					}

					String lengthStr = "("+length+")";
					if (cr.getCell(4).getStringCellValue().equalsIgnoreCase("timestamp")){
						lengthStr = "";
					}else if(cr.getCell(4).getStringCellValue().equalsIgnoreCase("datetime")){
						lengthStr = "";
					}else if(cr.getCell(4).getStringCellValue().equalsIgnoreCase("date")){
						lengthStr = "";
					}

					if (a == 2) {

						id =getString(cr.getCell(2)).replaceAll("\\s+", "");
						tableCommon = cr.getCell(1).getStringCellValue().toString().replaceAll("\\s+", "");
						sb.append("    " + id.toLowerCase() + " "
								+ changeType(cr.getCell(4).getStringCellValue().replaceAll("^\\s+", ""))+lengthStr);
						map1.put(id, changeType(cr.getCell(4).getStringCellValue().replaceAll("^\\s+", ""))+lengthStr);
					} else {
						sb.append(",\n    " + getString(cr.getCell(2)).replaceAll("\\s+", "").toLowerCase() + " "
								+ changeType(cr.getCell(4).getStringCellValue().replaceAll("^\\s+", ""))+lengthStr);
						map1.put(getString(cr.getCell(2)).toLowerCase().replaceAll("\\s+", ""), changeType(cr.getCell(4).getStringCellValue().replaceAll("^\\s+", ""))+lengthStr);
					}
					common.append("COMMENT ON COLUMN " + schema + "."
							+ tableNameCell.getStringCellValue().toString().replaceAll("\\s+", "").toLowerCase() + "."
							+ getString(cr.getCell(2)) + " IS '"
							+ getString(cr.getCell(3)) + "';\n");
				}
				//企业服务临时加
				//sb.append(",\n    \"for_last_edittime\" timestamp(6)");

				sb.append(",\n    dw_creation_by  character varying(100)");
				sb.append(",\n    dw_creation_date timestamp(6) without time zone default now()");
				sb.append(",\n    dw_last_update_by  character varying(100)");
				sb.append(",\n    dw_last_update_date timestamp(6) without time zone default now()");
				sb.append(",\n    dw_batch_number  bigint");


				for(Map.Entry<String, String> en : map.entrySet()) {
					if(filePath.indexOf(en.getKey()) !=-1) {
						sb.append(",\n    \"dw_data_source_id\"  character varying(5) default '"+en.getValue()+"'");
						sb.append(",\n    \"dw_data_source\"  character varying(100) default'"+en.getKey()+"'");
					}
				}






				sb.append("\n) ;\n");
				/*sb.append(")WITH(orientation=row,compression=no) ");
				sb.append("DISTRIBUTE BY HASH(\"" + id + "\") ;\n");*/
				sb.append("COMMENT ON TABLE " + schema + "."
						+ tableNameCell.getStringCellValue().replaceAll("\\s+", "") + " IS '" + tableCommon + "';\n");
				System.out.println(sb.toString() + common.toString());


				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.write(sb.toString() + common.toString());
				pw.flush();
				pw.close();
				wb.close();


				StringBuilder insert = new StringBuilder();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String table =  getString(schema).toLowerCase() + "." + getString(tableNameCell);
				for(int a = 0 ; a < 10 ; a++) {
					StringBuilder insert1 = new StringBuilder();
					StringBuilder insert2 = new StringBuilder();
					insert1.append("insert into " + table +"(");
					insert2.append(" values(");
					int b = 0;
					for(Map.Entry<String, String> en : map1.entrySet()) {
						if(b++!=0) {
							insert1.append(",");
							insert2.append(",");
						}
						insert1.append("\""+en.getKey()+"\"");
						if(en.getValue().startsWith("time")) {
							insert2.append("'"+sdf.format(new Date())+"'");
						}
						else if(en.getValue().startsWith("tinyint")) {
							insert2.append(100);
						}
						else {
							insert2.append(Integer.parseInt(""+a+b));
						}

					}
					insert1.append(",\"dw_batch_number\")");
					insert2.append(",'"+sdf1.format(new Date())+"')");

					insert.append( insert1.toString() + insert2.toString() +";\n");
				}
				PrintWriter pw1 = new PrintWriter(new FileWriter(fileInsert, true));
				pw1.write(sb.toString() + common.toString()+"\n"+ insert.toString());
				pw1.flush();
				pw1.close();
				System.out.println(insert);
			}
		}
	}

	public static void createSQL(String filePath) throws Exception {
		File f = new File(filePath);
		String p = f.getParent();
		File pf = new File(p + "//sql");
		if (!pf.exists()) {
			pf.mkdirs();
		}

		File fileInsert = new File(p + "/sql/sql_test.txt");
		if(fileInsert.exists()) {
			fileInsert.delete();
		}

		File file = new File(p + "/sql/mysql.txt");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write("".getBytes());
		fos.flush();
		fos.close();
		Workbook wb = new XSSFWorkbook(filePath);
		Sheet st01 = wb.getSheet("数据资源");

		int last = st01.getLastRowNum();
		// last = 9;


		for (int i = 2; i <= last; i++) {

			Row row = st01.getRow(i);

			Cell c2 = row.getCell(2);
			Cell c4 = row.getCell(4);
			if (!"是".equals(c4.toString().replaceAll("\\s+", ""))) {
				continue;
			}
			if (checkCell(c2.getStringCellValue())) {
				Cell tableNameCell = row.getCell(11);
				System.out.println(c2 + "-" + tableNameCell);
				Sheet sheet = wb.getSheet(getString(c2));
				StringBuffer sb = new StringBuffer();

				sb.append("\n\n-- " + c2.getStringCellValue() + " --\n");
				sb.append("DROP TABLE IF EXISTS `" + getString(tableNameCell) + "`;\n");
				sb.append("CREATE TABLE IF NOT EXISTS `" + getString(tableNameCell) + "`\n(\n");

				int count = sheet.getLastRowNum();

				Map<String,String> map1 = new LinkedHashMap<String, String>();
				for (int a = 2; a <= count; a++) {
					Row cr = sheet.getRow(a);
					if (null == cr || null == cr.getCell(1) || !checkCell(cr.getCell(1).getStringCellValue())) {
						break;
					}

					if(list.contains(getString(cr.getCell(3)).replaceAll("\\s+", "").toLowerCase())) {
						if(getString(tableNameCell).indexOf("sls_task_subsequent_process") != -1) {
							System.out.println(0);
						}
						continue;
					}

					if (a == 2) {
						sb.append("    `" + getString(cr.getCell(14)).replaceAll("\\s+", "").toLowerCase() + "` "
								+ changeMySQLType(getString(cr.getCell(16)).replaceAll("\\s+", "") + " primary key"));
						sb.append(" COMMENT '" + cr.getCell(15).toString().replaceAll("\\s+", "") + "'");
						map1.put(getString(cr.getCell(14)).replaceAll("\\s+", "").toLowerCase(), changeMySQLType(getString(cr.getCell(16)).replaceAll("\\s+", "")));
					} else {
						sb.append(",\n    `" + getString(cr.getCell(14)).replaceAll("\\s+", "").toLowerCase() + "` "
								+ changeMySQLType(getString(cr.getCell(16)).replaceAll("\\s+", "")));
						sb.append(" COMMENT '" + (getString(cr.getCell(15)).replaceAll("\\s+", "") + "'"));
						map1.put(getString(cr.getCell(14)).replaceAll("\\s+", "").toLowerCase(), changeMySQLType(getString(cr.getCell(16)).replaceAll("\\s+", "")));
					}
				}
				//企业服务临时加
				//sb.append(",\n    `for_last_edittime` datetime");
				sb.append("\n);\n");
				System.out.println(sb.toString());


				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.write(sb.toString());
				pw.flush();
				pw.close();
				wb.close();

				StringBuilder insert = new StringBuilder();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String table =  getString(tableNameCell);
				for(int a = 0 ; a < 10 ; a++) {
					StringBuilder insert1 = new StringBuilder();
					StringBuilder insert2 = new StringBuilder();
					insert1.append("insert into " + table +"(");
					insert2.append(" values(");
					int b = 0;
					for(Map.Entry<String, String> en : map1.entrySet()) {
						if(b++!=0) {
							insert1.append(",");
							insert2.append(",");
						}
						insert1.append("`"+en.getKey()+"`");
						if(en.getValue().indexOf("time") != -1) {
							insert2.append("\""+sdf.format(new Date())+"\"");
						}
						else if(en.getValue().startsWith("tinyint")) {
							insert2.append(100);
						}
						else {
							insert2.append(a);
						}

					}
					insert1.append(")");
					insert2.append(")");

					insert.append( insert1.toString() + insert2.toString() +";\n");
				}
				PrintWriter pw1 = new PrintWriter(new FileWriter(fileInsert, true));
				pw1.write(sb.toString() +"\n"+ insert.toString());
				pw1.flush();
				pw1.close();
				System.out.println(insert);
			}
		}
	}

	public static boolean checkCell(String cell) {
		if (null == cell || "".equals(cell)) {
			return false;
		}

		return true;
	}

	public static String changeType(String cell) {
		cell = cell.replace("（", "(").replace("）", ")");
		cell = getString(cell);
		if (cell.toUpperCase().startsWith("mediumtext".toUpperCase())) {
			return "text";
		}
		else if (cell.toUpperCase().startsWith("smallint".toUpperCase())) {
			return "int";
		}
		else if (cell.toUpperCase().startsWith("VARCHAR2")) {
			return cell.toUpperCase().replace("VARCHAR2", " character varying");
		}
		else if (cell.toUpperCase().startsWith("VARCHAR")) {
			return cell.toUpperCase().replace("VARCHAR", " character varying");
		} else if (cell.toUpperCase().startsWith("TIMESTAMP")) {
			return cell.toLowerCase() + "(6) without time zone";
		} else if (cell.toUpperCase().startsWith("DATETIME") || cell.toUpperCase().startsWith("DATE")) {
			return "timestamp(6)" + " without time zone";
		} else if (cell.toUpperCase().startsWith("DOUBLE")) {
			return cell.toUpperCase().replace("DOUBLE", "number");
		} else if (cell.toUpperCase().startsWith("INT")) {
			return "integer";
		} else if (cell.toUpperCase().startsWith("BIGINT")) {
			return "bigint";
		} else if (cell.toUpperCase().startsWith("TINYINT")) {
			return "tinyint";
		} else if (cell.toUpperCase().startsWith("FLOAT")) {
			return cell.toUpperCase().replace("FLOAT", "number");
		}

		return cell.toLowerCase();
	}

	public static String getString(Cell cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim().replaceAll("\\r\\n", "");
			return str;
		}
		return "";
	}

	public static String getString(String cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim().replaceAll("\\r\\n", "");
			return str;
		}
		return "";
	}

	public static String changeMySQLType(String type) {
		return type.toLowerCase()
				.replace("varchar2", "varchar")
				.replace("number", "int")
				.replaceAll("date$", "datetime")
				.replaceAll("clob$", "text")
				.replaceAll("timestamp\\(\\d+?\\)", "timestamp")
				.replaceAll("date\\(\\d+?\\)", "datetime")
				.replaceAll("datetime\\(\\d+?\\)", "datetime");
	}
}