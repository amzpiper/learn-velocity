package com.excel.dlfMapping;


import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadMappingExcel {


	public static List<MappingTable> read(String fileName) throws Exception {
		Workbook wb = new XSSFWorkbook(fileName);

		List<MappingTable> tableList = new ArrayList<>();

		try {

			Sheet st01 = wb.getSheet("数据资源");

			int last = st01.getLastRowNum();
			// last = 9;
			for (int i = 1; i <= last; i++) {

				Row row = st01.getRow(i);

				String tableDes = row.getCell(2).getStringCellValue().trim();
				String sechma = row.getCell(3).getStringCellValue().trim();
				String tableName = row.getCell(4).getStringCellValue().trim();

				MappingTable mappingTable = new MappingTable();
				tableList.add(mappingTable);
				mappingTable.setDes(tableDes);
				mappingTable.setSechma(sechma);
				mappingTable.setName(tableName);

				Sheet sheet = wb.getSheet(tableDes);
				System.out.println(tableDes);

				for (int b = 2; b <= sheet.getLastRowNum(); b++) {
					Row r = sheet.getRow(b);
					if (r == null) {
						continue;
					}

					String enName = getString(r.getCell(2));
					String cnName = getString(r.getCell(3));


					String type = getString(r.getCell(3)).toLowerCase();
					String length = getString(r.getCell(4));
					try {
						length = Double.valueOf(length).intValue() + "";
					} catch (NumberFormatException e) {

					}

					String fromSechma = getString(r.getCell(12));
					String fromTableName = getString(r.getCell(13));
					String fromTableNameDes = getString(r.getCell(14));

					String fromName = getString(r.getCell(15));

					String fromCNName = getString(r.getCell(16));
					String fromType = getString(r.getCell(17));
					String fromLength = getString(r.getCell(18));

					MappingField field = new MappingField();

					mappingTable.addField(field);

					field.setName(enName);
					field.setCnName(cnName);
					field.setType(type);
					field.setLength(length);

					field.setFromSechma(fromSechma);
					field.setFromTableName(fromTableName);
					field.setFromTableNameDes(fromTableNameDes);

					field.setFromName(fromName);
					field.setFromCNName(fromCNName);
					field.setFromType(fromType);
					field.setFromLength(fromLength);
					if (fromName == null || fromName.equals("")) {
						field.setFromFlag(false);
					}


				}
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
