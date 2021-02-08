package com.excel.chenqm.fq;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateFQCodeKindExcel {
//	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\ÖØÇìèµÉ½\\tmp\\20200520\\1.txt";
//	public static String kindPre = "PSP_FQ_MYSQL";

	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\ÖØÇìèµÉ½\\tmp\\20200520\\1_ÕÐÉÌ.txt";
	public static String kindPre = "INVESTMENT";



	public static void main(String[] args) throws Exception {
			List<Table> tableList = createCodeKindTableList();
			System.out.println(tableList.size());


		String[] col = new String[] {"´úÂë","´úÂëº¬Òå"};

//		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook(new FileInputStream("d:\\test.xlsx"));
		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook();

		CellStyle style = wbnew.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// ×óÓÒ¾ÓÖÐ

		//±ß¿òÏÂ
		style.setBorderBottom(BorderStyle.THIN);
		//±ß¿ò×ó
		style.setBorderLeft(BorderStyle.THIN);
		//±ß¿òÓÒ
		style.setBorderRight(BorderStyle.THIN);
		//±ß¿òÉÏ
		style.setBorderTop(BorderStyle.THIN);

		CellStyle titleStyle = wbnew.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);// ×óÓÒ¾ÓÖÐ

		//±ß¿òÏÂ
		titleStyle.setBorderBottom(BorderStyle.THIN);
		//±ß¿ò×ó
		titleStyle.setBorderLeft(BorderStyle.THIN);
		//±ß¿òÓÒ
		titleStyle.setBorderRight(BorderStyle.THIN);
		//±ß¿òÉÏ
		titleStyle.setBorderTop(BorderStyle.THIN);

		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//		titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		titleStyle.setFillForegroundColor((short) 51);

		try {

			int kindIndex = 0;
			for (Table table : tableList) {
				kindIndex++;
				Sheet  st  = wbnew.createSheet(table.getDes());


				String kindNo = generateId(kindIndex);

				System.out.println(kindNo + "," + table.getDes());

				int sc = 2;
				Row row = st.createRow(0);

				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#Ä¿Â¼!A1" + "\",\"" + "·µ»Ø" + "\")");

				row = st.createRow(1);

				row.createCell(0).setCellValue(table.getDes() + "-" + kindNo + "");
				CellRangeAddress ca = new CellRangeAddress(1, 1, 0, 1);
				st.addMergedRegion(ca);
				row.getCell(0).setCellStyle(titleStyle);
				for (int j = 0; j < col.length; j++) {
					if (row.getCell(j) == null) {
						row.createCell(j).setCellValue("");
					}
					{
						row.getCell(j).setCellStyle(titleStyle);
					}

				}

				row = st.createRow(2);

				int a = 0;
				for(String c : col) {
					row.createCell(a++).setCellValue(c);

					row.getCell(0).setCellStyle(style);
					for (int j = 0; j < col.length; j++) {
						if (row.getCell(j) == null) {
							row.createCell(j).setCellValue("");
						}

						row.getCell(j).setCellStyle(titleStyle);

					}
				}
				String tableName = table.getName();



				int index = -1;
			    for (Field field : table.getFieldList()) {

			    	index++;
					String cnName = field.getDes();
					String enName = field.getName();



					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(enName);
					rr.createCell(1).setCellValue(cnName);




					//rr.getCell(0).getArrayFormulaRange().
					for (int j = 0; j < col.length; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}

					}


				}


				for (int j = 0; j < col.length; j++) {
					st.setColumnWidth(j, 5000);

				}
				
			}


		}


		catch (Exception e) {
			e.printStackTrace();
		}
		wbnew.write(new FileOutputStream("d:\\test_fq_kind.xlsx"));
		wbnew.close();

	}
	
	public static String getString(Cell cell) {
		if(cell != null) {
			String str=cell.toString().replace(String.valueOf((char)160)," ").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
			str=str.trim();
			return str;
		}
		return "";
	}
	
	public static String getString(String cell) {
		if(cell != null) {
			String str=cell.toString().replace(String.valueOf((char)160)," ").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
			str=str.trim();
			return str;
		}
		return "";
	}
	
	public static String changeType(String type , String length) {
		type = type.toLowerCase();
		switch(type) {
			case "varchar2":
				return "character varying("+length+")";
		   case "varchar":
			   return "character varying("+length+")";
		   case "date":
			   return "timestamp(6)";
		   case "datetime":
			   return "timestamp(6)";
		   case "smallint":
			   return "smallint";
		   case "int":
			   return "integer";
		   case "char":
			   return "char("+length+")";
		   case "number":
			   return "number";
		   case "double":
			   return "number"+ ((length !=null&& !"".equals(length) && !"0".equals(length))?"("+length+")":"") ;
		   case "decimal":
			   return "number"+ ((length !=null&& !"".equals(length) && !"0".equals(length))?"("+length+")":"") ;
		   case "float":
			   return "number"+ ((length !=null&& !"".equals(length) && !"0".equals(length))?"("+length+")":"") ;
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
		return "character varying(100)";
	}
	
	public static String getTypeString1(String type,String length) {
		return type + ((length !=null&& !"".equals(length) && !"0".equals(length))?"("+length+")":"") ;
	}

	public static List<Table> createCodeKindTableList() {
		List<Table> tableList = new ArrayList<>();

		try {
			List<String> kindList = FileUtils.readLines(new File(fileName));

			for (String s : kindList) {
				String[] kindArray = s.split("@@");

				String tableInfo = kindArray[0];
				String filedInfo = kindArray[1];

				String[] tableInfoArray = tableInfo.split(",");
				Table table = new Table();
				tableList.add(table);
				table.setDes(tableInfoArray[0]);
				//table.setDes(tableInfoArray[1]);

				String[] filedListArray = filedInfo.split(";");
				for (String filedData : filedListArray) {
					String[] filedArray = filedData.split(",");
					Field field = new Field();
					table.addField(field);
					field.setName(filedArray[0]);
					field.setDes(filedArray[1]);
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


		return tableList;
	}

	public static String generateId(Integer size) {
		String pattern = "#0000";
		NumberFormat nf = new DecimalFormat(pattern);

		String str = kindPre + nf.format(size.intValue());
		return str;
	}



}
