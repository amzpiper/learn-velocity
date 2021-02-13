package com.excel.chenqm.fq;

import com.excel.CreateDWIMapping;
import com.excel.CreateDWIPPPP;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ʹ�ó�ʼ�ļ�����Դ�ļ�Excel:test_fq.xlsx
 */
public class CreateFQExcel {
	//��ʼ�ļ�·��
	public static String fileName = "d:\\gd2yy_��ƵѲ��.xlsx";

	public static void main(String[] args) throws Exception {
		List<Table> tableList = ReadFQExcel.read(fileName);
		System.out.println(tableList.size());
		String[] col = new String[] {"������������","����Ӣ������","��ע","����","����","�����ֶθ�ʽ","��������/��","�Ƿ�Ϊ��","�������/��","��Ӧ�����","ʱ����ֶΣ���/��","������"};
		//XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook(new FileInputStream("d:\\test.xlsx"));
		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook();

		CellStyle style = wbnew.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);// ���Ҿ���
		//�߿���
		style.setBorderBottom(BorderStyle.THIN);
		//�߿���
		style.setBorderLeft(BorderStyle.THIN);
		//�߿���
		style.setBorderRight(BorderStyle.THIN);
		//�߿���
		style.setBorderTop(BorderStyle.THIN);
		CellStyle titleStyle = wbnew.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);// ���Ҿ���
		//�߿���
		titleStyle.setBorderBottom(BorderStyle.THIN);
		//�߿���
		titleStyle.setBorderLeft(BorderStyle.THIN);
		//�߿���
		titleStyle.setBorderRight(BorderStyle.THIN);
		//�߿���
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		titleStyle.setFillForegroundColor((short) 27);

		try {
			for (Table table : tableList) {
				Sheet  st  = wbnew.createSheet(table.getDes());
				int sc = 3;
				Row row = st.createRow(0);
				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#������Դ!A1" + "\",\"" + "����" + "\")");
				row = st.createRow(1);
				row.createCell(0).setCellValue(table.getDes() + "��" + table.getName() + "��");
				CellRangeAddress ca = new CellRangeAddress(1, 1, 0, 11);
				st.addMergedRegion(ca);
				row.getCell(0).setCellStyle(style);
				for (int j = 0; j < 12; j++) {
					if (row.getCell(j) == null) {
						row.createCell(j).setCellValue("");
					}
					{
						row.getCell(j).setCellStyle(style);
					}
				}

				row = st.createRow(3);
				int a = 0;
				for(String c : col) {
					row.createCell(a++).setCellValue(c);

					row.getCell(0).setCellStyle(style);
					for (int j = 0; j < 12; j++) {
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
					rr.createCell(1).setCellValue(enName);
					rr.createCell(2).setCellValue(des);
					rr.createCell(3).setCellValue(type);
					rr.createCell(4).setCellValue(length);
					rr.createCell(5).setCellValue(decimal);
					rr.createCell(6).setCellValue("N");
					if("PRI".equals(pk)) {
						rr.createCell(6).setCellValue("Y");
						rr.createCell(7).setCellValue("N");
					}else {
						rr.createCell(7).setCellValue("Y");
					}

					if (enName.equalsIgnoreCase("marking_update")) {
						rr.createCell(10).setCellValue("Y");
					}
					//rr.getCell(0).getArrayFormulaRange().
					for (int j = 0; j < 12; j++) {
						if (rr.getCell(j) == null) {
							rr.createCell(j).setCellValue("");
						}
						{
							rr.getCell(j).setCellStyle(style);
						}
					}
				}
				for (int j = 0; j < 12; j++) {
					st.setColumnWidth(j, 5000);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		wbnew.write(new FileOutputStream("d:\\test_fq.xlsx"));
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



}
