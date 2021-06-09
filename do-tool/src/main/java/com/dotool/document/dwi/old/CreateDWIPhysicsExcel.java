package com.dotool.document.dwi.old;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateDWIPhysicsExcel {
	public static void main(String[] args) throws Exception {
		Workbook wb = new XSSFWorkbook(
				CreateDWIExcel.fileName);
		String pre = CreateDWIExcel.dbPre;

		String[] col = new String[] { "SCHEMA主键", "表名", "字段名", "字段描述", "字段类型", "字段长度", "字段精度", "默认值", "是否可空", "是否主键",
				"是否分区键", "是否分布建", "表行存(列存) 标识", "备注" };

		//XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook(new FileInputStream("d:\\test.xlsx"));
		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook();

		CellStyle style = wbnew.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// 左右居中

		//边框下
		style.setBorderBottom(BorderStyle.THIN);
		//边框左
		style.setBorderLeft(BorderStyle.THIN);
		//边框右
		style.setBorderRight(BorderStyle.THIN);
		//边框上
		style.setBorderTop(BorderStyle.THIN);

		CellStyle titleStyle = wbnew.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中

		//边框下
		titleStyle.setBorderBottom(BorderStyle.THIN);
		//边框左
		titleStyle.setBorderLeft(BorderStyle.THIN);
		//边框右
		titleStyle.setBorderRight(BorderStyle.THIN);
		//边框上
		titleStyle.setBorderTop(BorderStyle.THIN);

		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//		titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		titleStyle.setFillForegroundColor((short) 27);

		try {
			for (int i = 4; i < 100; i++) {
				Sheet sheet = wb.getSheetAt(i);
				Sheet st = wbnew.createSheet(sheet.getSheetName());

				String tableName = sheet.getRow(1).getCell(0).toString();
				/*tableName = tableName.replaceAll("^\\W+", "").replaceAll("\\W+$", "").replaceAll("\\(", "")
						.replaceAll("\\)", "");*/
				System.out.println(tableName);

				try {
					tableName = tableName.split("\\(")[1];
				} catch (Exception e) {
					//e.printStackTrace();
				}
				try {
					tableName = tableName.split("（")[1];
				} catch (Exception e) {
					//e.printStackTrace();
				}
				tableName = tableName.replace(")", "");
				tableName = tableName.replace("）", "");
				tableName = tableName.replace(".", "_");
				tableName = tableName.toLowerCase();

				int a = 0;

				Row row = st.createRow(0);

				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#表信息汇总!A1" + "\",\"" + "返回" + "\")");

				row = st.createRow(1);

				row.createCell(0).setCellValue(sheet.getSheetName() + "（" + CreateDWIExcel.dbPre + "_" + tableName + "）");
				CellRangeAddress ca = new CellRangeAddress(1, 1, 0, 13);
				st.addMergedRegion(ca);
				row.getCell(0).setCellStyle(style);
				for (int j = 0; j < 14; j++) {
					if (row.getCell(j) == null) {
						row.createCell(j).setCellValue("");
					}
					{
						row.getCell(j).setCellStyle(style);
					}

				}


				a = 0;
				row = st.createRow(2);

				for(String c : col) {
					row.createCell(a++).setCellValue(c);

					row.getCell(0).setCellStyle(style);
					for (int j = 0; j < 14; j++) {
						if (row.getCell(j) == null) {
							row.createCell(j).setCellValue("");
						}

						row.getCell(j).setCellStyle(titleStyle);

					}
				}


				int sc = 3;
				row = st.createRow(sc);



				/*
				 * int a = 0; for (String c : col) { row.createCell(a++).setCellValue(c); }
				 */

				for (int b = 4; b <= sheet.getLastRowNum(); b++) {
					Row r = sheet.getRow(b);
					String cnName = getString(r.getCell(0));
					String enName = getString(r.getCell(1));
					enName = enName.toLowerCase();
					String des = getString(r.getCell(2));
					String type = getString(r.getCell(3));
					String length = getString(r.getCell(4));
					String sp = getString(r.getCell(5));
					String pk = getString(r.getCell(6));
					String isnull = getString(r.getCell(7));
					String timeFlag = getString(r.getCell(10));

					Row rr = st.createRow(sc++);

					rr.createCell(0).setCellValue(pre);
					rr.createCell(1).setCellValue(pre + "_" + tableName);
					rr.createCell(2).setCellValue(enName);
					rr.createCell(3).setCellValue(cnName);
					rr.createCell(4).setCellValue(changeType1(type, ""));
					String len = length;
					try {
						if (length != null && length.indexOf(".") != -1) {
							len = length.split("\\.")[0];
						}
					} catch (Exception ce) {
						ce.printStackTrace();
					}
					rr.createCell(5).setCellValue(len);

					rr.createCell(7).setCellValue(timeFlag);
					rr.createCell(8).setCellValue("YES".equals(isnull) ? "是" : "否");
					if("PRI".equals(pk) || "Y".equals(pk) || "是".equals(pk)) {
						rr.createCell(9).setCellValue("是");
						rr.createCell(11).setCellValue("是");
					} else {
						rr.createCell(9).setCellValue("否");
						rr.createCell(11).setCellValue("否");
					}
					rr.createCell(12).setCellValue("row");
					rr.createCell(13).setCellValue(des);

					for (int j = 0; j < 14; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}

					}

				}

				List<String> auditDataList = new ArrayList<>();

				auditDataList.add("数据创建者,dw_creation_by,character varying,100");
				auditDataList.add("数据创建时间,dw_creation_date,timestamp,6");
				auditDataList.add("数据最后更新者,dw_last_update_by,character varying,100");
				auditDataList.add("数据最后更新时间,dw_last_update_date,timestamp,6");
				auditDataList.add("数据批次号,dw_batch_number,bigint,");
				auditDataList.add("源系统编号,dw_data_source_id,character varying,5");
				auditDataList.add("源系统名称,dw_data_source,character varying,100");
				for (String data : auditDataList) {
					String[] strArray = data.split(",");

					Row rr = st.createRow(sc++);
					rr.createCell(0).setCellValue(pre);
					rr.createCell(1).setCellValue(pre + "_" + tableName);
					rr.createCell(2).setCellValue(strArray[1].toLowerCase());
					rr.createCell(3).setCellValue(strArray[0]);

					rr.createCell(4).setCellValue(strArray[2]);
					String s = "";

					if (strArray.length == 4) {
						s = strArray[3];
					}
					rr.createCell(5).setCellValue(s);

					rr.createCell(8).setCellValue("否");
					rr.createCell(9).setCellValue("否");
					rr.createCell(11).setCellValue("否");
					rr.createCell(12).setCellValue("row");

					for (int j = 0; j < 14; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}

					}

				}

				for (int j = 0; j < 14; j++) {
					st.setColumnWidth(j, 5000);

				}

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		wbnew.write(new FileOutputStream("d:\\test（物理）.xlsx"));
		wbnew.close();
	}

	public static String getString(Cell cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim();
			return str;
		}
		return "";
	}

	public static String getString(String cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim();
			return str;
		}
		return "";
	}

	public static String changeType(String type, String length) {
		switch (type) {
		case "varchar":
			return "character varying(" + length + ")";
		case "date":
			return "timestamp(6)";
		case "datetime":
			return "timestamp(6)";
		case "smallint":
			return "smallint";
		case "int":
			return "integer";
		case "char":
			return "char(" + length + ")";
		case "double":
			return "number" + ((length != null && !"".equals(length) && !"0".equals(length)) ? "(" + length + ")" : "");
		case "decimal":
			return "number" + ((length != null && !"".equals(length) && !"0".equals(length)) ? "(" + length + ")" : "");
		case "float":
			return "number" + ((length != null && !"".equals(length) && !"0".equals(length)) ? "(" + length + ")" : "");
		case "timestamp":
			return "timestamp(6)";
		case "bigint":
			return "bigint";
		case "tinyint":
			return "tinyint";
		case "mediumtext":
			return "text";
		case "longtext":
			return "text";
		case "text":
			return "text";
		}
		return "";
	}

	public static String changeType1(String type, String length) {
		type = type.toLowerCase();
		switch (type) {
			case "varchar2":
				return "character varying";
			case "varchar":
				return "character varying";
			case "date":
				return "timestamp";
			case "datetime":
				return "timestamp";
			case "smallint":
				return "smallint";
			case "int":
				return "integer";
			case "char":
				return "char";
			case "double":
				return "number";
			case "decimal":
				return "number";
			case "float":
				return "number" ;
			case "timestamp":
				return "timestamp";
			case "bigint":
				return "bigint";
			case "tinyint":
				return "tinyint";
			case "mediumtext":
				return "text";
			case "longtext":
				return "text";
			case "text":
				return "text";
		}
		return "";
	}

	public static String getTypeString1(String type, String length) {
		return type + ((length != null && !"".equals(length) && !"0".equals(length)) ? "(" + length + ")" : "");
	}
}
