package com.dotool.initsql;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ReadInitSQLExcel {


	public static List<InitSQLTable> read(String fileName) throws Exception {
		Workbook wb = new XSSFWorkbook(fileName);

		List<InitSQLTable> tableList = new ArrayList<>();

		try {

			Sheet st01 = wb.getSheet("数据资源");

			int last = st01.getLastRowNum();
			// last = 9;
			for (int i = 1; i <= last; i++) {

				Row row = st01.getRow(i);

				String tableDes = row.getCell(2).getStringCellValue().trim();
				String sechma = row.getCell(3).getStringCellValue().trim();
				String tableName = row.getCell(4).getStringCellValue().trim();

				InitSQLTable mappingTable = new InitSQLTable();
				tableList.add(mappingTable);
				mappingTable.setDes(tableDes);
				mappingTable.setSechma(sechma);
				mappingTable.setName(tableName);

				Sheet sheet = wb.getSheet(tableDes);
				System.out.println(tableDes);

				for (int b = 2; b <= sheet.getLastRowNum(); b++) {
					Row r = sheet.getRow(0);
					Row cnR = sheet.getRow(1);
					Row data = sheet.getRow(b);
					if (data == null) {
						continue;
					}

					InitSQLRow rowData = new InitSQLRow();
					mappingTable.addRow(rowData);
					short lastCellNum = r.getLastCellNum();
					for (int j = 0; j < lastCellNum; j++) {
						String enName = getString(r.getCell(j));
						String cnName = getString(cnR.getCell(j));
						String value = getString(data.getCell(j));
						System.out.println(value);


						InitSQLField field = new InitSQLField();

						rowData.addField(field);

					field.setName(enName);
					field.setCnName(cnName);
					field.setValue(value);


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
		/*if (cell != null) {
			System.out.println(cell.getCellType().compareTo(CellType.NUMERIC));
			if(cell.getCellType().compareTo(CellType.) == 0) {

			}
		}*/
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
