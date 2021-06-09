package com.dotool.document.tool;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ReadFQExcel {


	public static List<Table> read(String fileName) throws Exception {
		Workbook wb = new XSSFWorkbook(fileName);

		List<Table> tableList = new ArrayList<>();

		try {
			Sheet sheet = wb.getSheetAt(0);

			Table table = null;

			for(int b = 0; b <= sheet.getLastRowNum() ; b++) {
				Row r = sheet.getRow(b);
				if(r == null) {
					continue;
				}
				String enName = getString(r.getCell(0)).toLowerCase().trim();
				if(enName.equals("")) {
					table = null;
					continue;
				}
				if (enName.indexOf("fe_app5") != -1) {
					table = new Table();
					enName = enName.replace(".", ",");
					enName = enName.replace("(", ",");
					enName = enName.replace(")", "");
					String[] tmpEnName = enName.split(",");
					table.setSechma(tmpEnName[0]);
					table.setName(tmpEnName[1]);
					String des = tmpEnName[2];
					des = des.replace("-", "_").trim();
					des = des.replace("&", "_");
					table.setDes(des);

					tableList.add(table);
					continue;
				}
				if (enName.equals("×Ö¶ÎÃû")) {
					continue;
				}
				String cnName = getString(r.getCell(1));
				cnName = cnName.replace("-", "_").trim();


				String type = getString(r.getCell(2)).toLowerCase();
				String length = getString(r.getCell(3));
				try {
					length = Double.valueOf(length).intValue() + "";
				} catch (NumberFormatException e) {

				}
				String decimal = getString(r.getCell(4));
				int decimalNum = 0 ;
				try {
					decimalNum = Double.valueOf(decimal).intValue() ;
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
				decimal = decimalNum + "";
				if (decimalNum == 0 && type.equalsIgnoreCase("numeric")) {
					type = "int";
					length = "";
				} else if (type.equalsIgnoreCase("datetime")) {
					length = "";
				}


				String des = getString(r.getCell(5));
				String physicsName = getString(r.getCell(6));
				String inDBThemeFlag = getString(r.getCell(7));

				Field field = new Field();

				table.addField(field);

				field.setName(enName);
				field.setCnName(cnName);
				field.setType(type);
				field.setLength(length);
				field.setDecimal(decimal);
				field.setDes(des);
				field.setPhysicsName(physicsName);
				field.setInDBThemeFlag(inDBThemeFlag);

			}

		}


		catch (Exception e) {
			e.printStackTrace();
		}


		return tableList;

	}

	public static String getString(Cell cell) {
		if(cell != null && cell.toString() != null) {

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



}
