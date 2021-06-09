package com.dotool.cloud.createtable.dwr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateDWRSQL {
	public static void main(String[] args) throws Exception {
		
		
		File file = new File("E:\\cc_project\\nwim\\SmartPark\\广东省二医\\03、系统设计\\详细设计\\专题");
		List<File> files = getNeedFile(file);
		for(File cf : files) {
			if(cf.getName().indexOf("能耗") != -1) {
				System.out.println("9999999999");
				createDWISQL(cf.getPath());
				continue;
			}
			//createDWISQL(cf.getPath());
			
		}
		/*
		 * for(String path :filePath) { createDWISQL(path); }
		 */
		//createDWISQL("E:\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\主题层\\企业服务\\02物理模型设计\\DWR-企业服务-物理模型设计-V1.0.xlsx");
	}

	public static void createDWISQL(String filePath) throws Exception {
		System.out.println(filePath);
		File f = new File(filePath);
		String p = f.getParentFile().getParent();
		File pf = new File(p + "//sql");
		if (!pf.exists()) {
			pf.mkdirs();
		}
		Pattern pp = Pattern.compile("DWR-(?<name>\\W+?)-");
		Matcher m = pp.matcher(filePath);
		String name = "";
		while(m.find()) {
			name = m.group("name");
		}
		//File file = new File(p + "/sql/dwr_"+name+"_ddl.txt");

		//创建包含分区键的sql
		File file = new File(p + "/sql/dwr_"+name+"_ddl_分区.txt");

		System.out.println(file.getPath());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write("".getBytes());
		fos.flush();
		fos.close();
		Workbook wb = new XSSFWorkbook(filePath);
		Sheet st01 = wb.getSheet("表信息");

		int last = st01.getLastRowNum();
		// last = 9;
		for (int i = 2; i <= last; i++) {

			Row row = st01.getRow(i);
			if(null == row) {
				continue;
			}
			Cell c2 = row.getCell(2);
			//Cell c4 = row.getCell(4);


			if (checkCell(getString(c2))) {
				Cell tableNameCell = row.getCell(1);
				Cell schema = row.getCell(0);
				System.out.println(c2 + "-" + tableNameCell);
				Sheet sheet = wb.getSheet(getString(c2));
				StringBuffer sb = new StringBuffer();
				StringBuffer common = new StringBuffer();

				sb.append("\n\n-- " + getString(c2) + " --\n");
				sb.append("DROP TABLE IF EXISTS \"" + getString(schema).toLowerCase() + "\".\"" + getString(tableNameCell) + "\";\n");
				sb.append("CREATE TABLE IF NOT EXISTS \"" + getString(schema).toLowerCase() + "\".\"" + getString(tableNameCell)
						+ "\"\n(\n");

				int count = sheet.getLastRowNum();

				String id = "";
				//String tableCommon = "";
				List<String> list = new ArrayList<>();
				//List<Map<String,String>> cols = new ArrayList<>();

				for (int a = 4; a <= count; a++) {
					Row cr = sheet.getRow(a);
					// System.out.println(a);
					if (null == cr || null == cr.getCell(3) || !checkCell(cr.getCell(3).getStringCellValue())) {
						break;
					}

					String length = getString(cr.getCell(6));
					if(length.indexOf(".") != -1) {
						length = length.substring(0,length.indexOf("."));
					}

					if("是".equals(getString(cr.getCell(12))) || "Y".equals(getString(cr.getCell(12)).toUpperCase())) {
						list.add(getString(cr.getCell(3)).toLowerCase());
					}

					if (a ==4 ) {

						id =getString(cr.getCell(3)).toLowerCase().replaceAll("\\s+", "");
						//tableCommon = getString(cr.getCell(1));
						sb.append("    \"" + id + "\" " +getType(getString(cr.getCell(5)),length));
					} else {
						sb.append(",\n    \"" + getString(cr.getCell(3)).toLowerCase().replaceAll("\\s+", "") + "\" "
								+ getType(getString(cr.getCell(5)),length));
					}
					common.append("COMMENT ON COLUMN " + schema + "."
							+ tableNameCell.getStringCellValue().toString().replaceAll("\\s+", "").toLowerCase() + "."
							+ getString(cr.getCell(3)).toLowerCase().replaceAll("\\s+", "") + " IS '"
							+ getString(cr.getCell(4)) + "';\n");
				}





				sb.append("\n)WITH(orientation=row,compression=no) ");

				StringBuilder sbDistr = new StringBuilder();
				for(String key : list) {
					if(sbDistr.length() != 0) {
						sbDistr.append(",");
					}
					sbDistr.append("\""+key+"\"");
				}
				if(sbDistr.length() > 0) {
					sb.append("\nDISTRIBUTE BY HASH("+sbDistr.toString()+") ");
				}
				//创建分区键

				sb.append("\nPARTITION BY RANGE (dw_creation_date)" + "\n(\n"+
						"    PARTITION P0 VALUES LESS THAN(to_timestamp('2020-05-01','YYYY-MM-DD HH24:MI:SS')),\n"
						+ "    PARTITION PMAX VALUES LESS THAN(MAXVALUE)" + "\n)" );

				sb.append(";\n");
				sb.append("COMMENT ON TABLE \"" + schema + "\".\""
						+ tableNameCell.getStringCellValue().replaceAll("\\s+", "") + "\" IS '" + getString(c2) + "';\n");
				System.out.println(sb.toString() + common.toString());


				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.write(sb.toString() + common.toString());
				pw.flush();
				pw.close();
				wb.close();


			}
		}
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
		else if (cell.toUpperCase().startsWith("VARCHAR")) {
			return cell.toUpperCase().replace("VARCHAR", " character varying");
		} else if (cell.toUpperCase().startsWith("TIMESTAMP")) {
			return cell.toLowerCase() + " without time zone";
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

	public static String getType(String type,String length) {
		if("".equals(length) || null == length) {
			return type;
		}else {
			return type+"("+length+")";
		}
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

	public static boolean checkCell(String cell) {
		if (null == cell || "".equals(cell)) {
			return false;
		}

		return true;
	}

	public static List<File> getNeedFile(File file) {
		List<File> lf = new ArrayList<File>();
		if(file.isDirectory()) {
			for(File f : file.listFiles()) {
				lf.addAll(getNeedFile(f)) ;
			}
		}else {
			if(file.getName().indexOf("物理") != -1) {
				System.out.println(file.getPath());
				lf.add(file);
			}
		}
		return lf;
	}
}

