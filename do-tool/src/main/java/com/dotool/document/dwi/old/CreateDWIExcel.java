package com.dotool.document.dwi.old;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateDWIExcel {
	/*public static String domainName = "环保";
	public static String sourceSystemName = "环保_污染源";
	public static String dbPre = "dwi_ems";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\环保\\重庆璧山高新区智慧园区-数据及集成服务-数据源需求分析（环保）V1.0.xlsx";
*/

	/*public static String domainName = "服务平台";
	public static String sourceSystemName = "园区服务平台";
	public static String dbPre = "dwi_psp";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\服务平台\\重庆璧山高新区智慧园区-数据及集成服务-数据源数据分析表（服务平台）-V1.1.xlsx";
*/


	/*public static String domainName = "01_02 管理平台_协同办公_招商_经济";
	public static String sourceSystemName = "园区管理平台";
	public static String dbPre = "dwi_pmp";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\管理平台（飞企）\\重庆璧山高新区智慧园区-数据及集成服务-数据源数据分析(管理平台_飞企)-V2.0.0.xlsx";
*/
	/*public static String domainName = "01_02 01_01 消防_消防";
	public static String sourceSystemName = "消防";
	public static String dbPre = "dwi_inf";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\消防\\重庆璧山高新区智慧园区-数据及集成服务-数据源需求分析（消防）-V1.2.xlsx";
*/
	/*public static String domainName = "01_01 能耗_能耗采集";
	public static String sourceSystemName = "能耗";
	public static String dbPre = "dwi_ecm";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\能耗监测\\重庆璧山高新区智慧园区-数据及集成服务-数据源需求分析（能耗监测）-V1.0 - 副本.xlsx";
*/

	/*public static String domainName = "01_01 管理平台_园区管理平台";
	public static String sourceSystemName = "园区管理平台";
	public static String dbPre = "dwi_pmp";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\管理平台（普天）\\重庆璧山高新区智慧园区-数据及集成服务-数据源数据分析(管理平台)-V1.1.0.xlsx";
*/
	/*public static String domainName = "01_01 资产-国有资产";
	public static String sourceSystemName = "国有资产";
	public static String dbPre = "dwi_nas";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\02、项目需求\\数据采集（源系统）\\国有资产\\重庆璧山高新区智慧园区-数据及集成服务-数据源需求分析（国有资产）-V1.1.xlsx";
*/

	/*public static String domainName = "医疗BI";
	public static String sourceSystemName = "医疗BI";
	public static String dbPre = "dwi_hospital";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\02、项目需求\\数据采集（源系统）\\医疗BI\\数据及集成服务-数据源数据分析(医疗BI)-V1.0.0.xlsx";
*/
	/*public static String domainName = "ABC";
	public static String sourceSystemName = "ABC";
	public static String dbPre = "dwi_abc";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\02、项目需求\\数据采集（源系统）\\ABC\\数据及集成服务-数据源需求分析（安防）-V1.0.xlsx";
*/

	public static String domainName = "01_01 能耗_能耗采集";
	public static String sourceSystemName = "能耗";
	public static String dbPre = "dwi_ecm";
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\02、项目需求\\数据采集（源系统）\\能耗\\数据及集成服务-数据源数据分析(能耗)-V1.0.0.xlsx";


	public static void main(String[] args) throws Exception {
		Workbook wb = new XSSFWorkbook(CreateDWIExcel.fileName);


		String[] col = new String[] {"主题域","实体名称","实体属性","实体属性字段","字段类型","是否主键","分布键","是否时间戳","行/列存储","属性所属域组","系统简称","Schema标识","表名",
				"表中文名","字段名","字段中文名","字段类型","所属值域","映射代码","映射备注","系统简称","Schema标识","表名",
				"表中文名","字段名","字段中文名","字段类型","所属值域","映射代码","映射备注"};

//		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook(new FileInputStream("d:\\test.xlsx"));
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
			Sheet s8 = wb.getSheet("数据资源");
			String sche = getString(s8.getRow(2).getCell(8));
			for (int i = 4; i < 100; i++) {
				Sheet sheet = wb.getSheetAt(i);
				Sheet  st  = wbnew.createSheet(sheet.getSheetName());





				int sc = 1;
				Row row = st.createRow(0);

				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#数据资源!A1" + "\",\"" + "返回" + "\")");

				row = st.createRow(1);

				int a = 0;
				for(String c : col) {
					row.createCell(a++).setCellValue(c);

					row.getCell(0).setCellStyle(style);
					for (int j = 0; j < 30; j++) {
						if (row.getCell(j) == null) {
							row.createCell(j).setCellValue("");
						}

						row.getCell(j).setCellStyle(titleStyle);

					}
				}
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

				for(int b = 4; b <=sheet.getLastRowNum() ; b++) {
					Row r = sheet.getRow(b);
					String cnName = getString(r.getCell(0));
					String enName = getString(r.getCell(1));
					String des = getString(r.getCell(2));
					String type = getString(r.getCell(3));
					String length ="";
					try {
						length = "".equals(getString(r.getCell(4)))?"":Double.valueOf(getString(r.getCell(4))).intValue()+"";
					}catch (Exception e) {
						length = getString(r.getCell(4));
					}
					
					String sp = getString(r.getCell(5));
					String pk = getString(r.getCell(6));
					String isnull = getString(r.getCell(7));
					String timeFlag = getString(r.getCell(10));
					
					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(CreateDWIExcel.domainName);
					rr.createCell(1).setCellValue(getString(sheet.getSheetName()));
					rr.createCell(2).setCellValue(cnName);
					rr.createCell(3).setCellValue(enName.toLowerCase());
					rr.createCell(4).setCellValue(changeType(type, length));
					rr.createCell(5).setCellValue("否");
					if("PRI".equals(pk) || "Y".equals(pk) || "是".equals(pk)) {
						rr.createCell(5).setCellValue("是");
						rr.createCell(6).setCellValue("Y");
					}else {
						rr.createCell(6).setCellValue("N");
					}
					
					rr.createCell(7).setCellValue(timeFlag);
					rr.createCell(8).setCellValue("row");
					rr.createCell(10).setCellValue(CreateDWIExcel.sourceSystemName);
					rr.createCell(11).setCellValue(sche);
					rr.createCell(12).setCellValue(tableName);
					rr.createCell(13).setCellValue(getString(sheet.getSheetName()));
					rr.createCell(14).setCellValue(enName);
					rr.createCell(15).setCellValue(cnName);

					rr.createCell(16).setCellValue(getTypeString1(type,length));

					//rr.getCell(0).getArrayFormulaRange().
					for (int j = 0; j < 30; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}

					}


				}

				List<String> auditDataList = new ArrayList<>();

				auditDataList.add("数据创建者,dw_creation_by,character varying(100)");
				auditDataList.add("数据创建时间,dw_creation_date,timestamp(6)");
				auditDataList.add("数据最后更新者,dw_last_update_by,character varying(100)");
				auditDataList.add("数据最后更新时间,dw_last_update_date,timestamp(6)");
				auditDataList.add("数据批次号,dw_batch_number,bigint");
				auditDataList.add("源系统编号,dw_data_source_id,character varying(5)");
				auditDataList.add("源系统名称,dw_data_source,character varying(100)");
				for (String data : auditDataList) {
					String[] strArray = data.split(",");

					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(CreateDWIExcel.domainName);
					rr.createCell(1).setCellValue(getString(sheet.getSheetName()));
					rr.createCell(2).setCellValue(strArray[0]);
					rr.createCell(3).setCellValue(strArray[1].toLowerCase());
					rr.createCell(4).setCellValue(strArray[2]);
					rr.createCell(5).setCellValue("否");
					rr.createCell(6).setCellValue("N");

					rr.createCell(8).setCellValue("row");

					for (int j = 0; j < 30; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}

					}

				}


				for (int j = 0; j < 30; j++) {
					st.setColumnWidth(j, 5000);

				}
				
			}


		}


		catch (Exception e) {
			e.printStackTrace();
		}
		wbnew.write(new FileOutputStream("d:\\test（汇总）.xlsx"));
		wbnew.close();

		CreateDWIMappingExcel.main(args);
		CreateDWIPhysicsExcel.main(args);
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
			case "numeric":
				return "numeric"+ ((length !=null&& !"".equals(length) && !"0".equals(length))?"("+length+")":"") ;
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
