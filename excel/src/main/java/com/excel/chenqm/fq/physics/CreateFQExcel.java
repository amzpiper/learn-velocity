package com.excel.chenqm.fq.physics;

import com.excel.chenqm.fq.Field;
import com.excel.chenqm.fq.ReadFQExcel;
import com.excel.chenqm.fq.Table;
import com.excel.util.PublicTools;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateFQExcel {
	/*
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\tmp\\20200520\\重庆璧山项目数据字典整理0521.xlsx";
	public static String domainName = "招商";
	public static String sourceSystemName = "园区管理平台";
	public static String dbPre = "dwd_investment";
	public static String dwiDBPre = "dwi_pmp";
	public static String needTableString = "pub_information,zhyq_project_hegh_seas,zhyq_intention_register,zhyq_investment_contarct,zhyq_housing,zhyq_housing_total,ft_1_0ac574209a195932e60b,dzyq_performance_records,dzyq_performance_type,zhyq_zsyz_milestone_zhixdatas,zhyq_enterprise_exit,zhyq_zsyz_targets,zhyq_zsyz_targets_plans,zhyq_fixed_asset_investment";
	*/

	/*
	public static String fileName = "d:\\重庆璧山项目数据字典整理+样例数据05221127-更新日期20201030_V1.0.xlsx";
	public static String fileName = "d:\\gd2yy_医疗.xlsx";
	public static String needTableString = "hw_zhyq_enterprise_main_info";
	*/

	/*
	public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\重庆璧山\\tmp\\20200520\\环保0525.xlsx";
	public static String domainName = "安环监控";
	public static String sourceSystemName = "环保";
	public static String dbPre = "dwd_ses";
	public static String dwiDBPre = "dwi_ems";
	public static String needTableString = "iotconnect_factor_info,iotconnect_factor_type";
	*/

	//初始文件文件目录
	public static String needTableString = "";
	public static String fileName = "d:\\gd2yy_视频巡更.xlsx";
	public static String domainName = "视频巡更";
	public static String sourceSystemName = "ABC";
	public static String name = "patrol";
	public static String dbSchemaPre = "dwr_" + name;
	public static String dbPre = "dwd_" + name;
	public static String dwiDBPre = "dwi_abc";

	/**
	 * 生成源系统和主题相关的excel
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<Table> tableList = ReadFQExcel.read(fileName);
		System.out.println(tableList.size());
		String[] col = new String[] {"属性中文名称","属性英文名称","备注","类型","长度","新增字段","是否入主题","源系统属性名称"};

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

		//titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		titleStyle.setFillForegroundColor((short) 27);

		try {
			String[] needTableArray = needTableString.split(",");
			Map<String, String> needMap = new HashMap<>();
			for (String s : needTableArray) {
				if(PublicTools.isNotNull(s)) {
					needMap.put(s, s);
				}
			}

			for (Table table : tableList) {
				String name = table.getName();
				if (needMap.isEmpty() == false && needMap.containsKey(name) == false) {
						continue;
				}

				Sheet  st  = wbnew.createSheet(table.getDes());
				int sc = 2;
				Row row = st.createRow(0);

				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#主题范围划分!A1" + "\",\"" + "返回" + "\")");

				row = st.createRow(1);

				row.createCell(0).setCellValue(table.getDes() + "（" + table.getName() + "）");
				CellRangeAddress ca = new CellRangeAddress(1, 1, 0, col.length - 1);
				st.addMergedRegion(ca);
				row.getCell(0).setCellStyle(style);
				for (int j = 0; j < col.length; j++) {
					if (row.getCell(j) == null) {
						row.createCell(j).setCellValue("");
					}
					{
						row.getCell(j).setCellStyle(style);
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
					String cnName = field.getCnName();
					String enName = field.getName();

					String type = field.getType();
					String length = field.getLength();
					String decimal = field.getDecimal();
					if (decimal.equalsIgnoreCase("0")) {
						decimal = "";
					}
					String des = field.getDes();


					String pk = "";
					if(index == 0) {
						pk = "PRI";
					}

					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(cnName);
					rr.createCell(1).setCellValue(field.getPhysicsName());
					rr.createCell(2).setCellValue(des);
					rr.createCell(3).setCellValue(type);
					rr.createCell(4).setCellValue(length);
					rr.createCell(5).setCellValue("");
					rr.createCell(6).setCellValue(field.getInDBThemeFlag());
					rr.createCell(7).setCellValue(enName);


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
		wbnew.write(new FileOutputStream("d:\\test_fq.xlsx"));
		wbnew.close();

		CreateFQPhysicsExcel.main(args);
		CreateFQMappingExcel.main(args);
		com.excel.chenqm.fq.CreateFQExcel.main(args);
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



}
