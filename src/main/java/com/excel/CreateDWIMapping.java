package com.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

public class CreateDWIMapping {
	public static void main(String[] args) throws Exception {
		Workbook wb = new XSSFWorkbook(
				CreateDWIExcel.fileName);

		String[] col = new String[] {"������","ʵ������","ʵ������","ʵ�������ֶ�","�ֶ�����","�Ƿ�����","�ֲ���","�Ƿ�ʱ���","��/�д洢","������������","ϵͳ���","Schema��ʶ","����",
				"��������","�ֶ���","�ֶ�������","�ֶ�����","����ֵ��","ӳ�����","ӳ�䱸ע","ϵͳ���","Schema��ʶ","����",
				"��������","�ֶ���","�ֶ�������","�ֶ�����","����ֵ��","ӳ�����","ӳ�䱸ע"};

//		XSSFWorkbook wbnew = XSSFWorkbookFactory.createWorkbook(new FileInputStream("d:\\test.xlsx"));
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

//		titleStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.index);
		titleStyle.setFillForegroundColor((short) 27);

		try {
			Sheet s8 = wb.getSheet("������Դ");
			String sche = getString(s8.getRow(2).getCell(8));
			for (int i = 4; i < 100; i++) {
				Sheet sheet = wb.getSheetAt(i);
				Sheet  st  = wbnew.createSheet(sheet.getSheetName());

				int sc = 1;
				Row row = st.createRow(0);

				//row.createCell(0).setCellValue("");
				row.createCell(0).setCellFormula("HYPERLINK(\"" + "#Mappingӳ���ܱ�!A1" + "\",\"" + "����" + "\")");

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
					tableName = tableName.split("��")[1];
				} catch (Exception e) {
					//e.printStackTrace();
				}
				tableName = tableName.replace(")", "");
				tableName = tableName.replace("��", "");
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
					String timeFlag = getString(r.getCell(10));
					String isnull = getString(r.getCell(7));

					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(CreateDWIExcel.domainName);
					rr.createCell(1).setCellValue(getString(sheet.getSheetName()));
					rr.createCell(2).setCellValue(cnName);
					rr.createCell(3).setCellValue(enName.toLowerCase());
					rr.createCell(4).setCellValue(changeType(type, length));
					rr.createCell(5).setCellValue("��");
					if("PRI".equals(pk) || "Y".equals(pk) || "��".equals(pk)) {
						rr.createCell(5).setCellValue("��");
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

				auditDataList.add("���ݴ�����,dw_creation_by,character varying(100)");
				auditDataList.add("���ݴ���ʱ��,dw_creation_date,timestamp(6)");
				auditDataList.add("������������,dw_last_update_by,character varying(100)");
				auditDataList.add("����������ʱ��,dw_last_update_date,timestamp(6)");
				auditDataList.add("�������κ�,dw_batch_number,bigint");
				auditDataList.add("Դϵͳ���,dw_data_source_id,character varying(5)");
				auditDataList.add("Դϵͳ����,dw_data_source,character varying(100)");
				for (String data : auditDataList) {
					String[] strArray = data.split(",");

					Row rr = st.createRow(++sc);
					rr.createCell(0).setCellValue(CreateDWIExcel.domainName);
					rr.createCell(1).setCellValue(getString(sheet.getSheetName()));
					rr.createCell(2).setCellValue(strArray[0]);
					rr.createCell(3).setCellValue(strArray[1].toLowerCase());
					rr.createCell(4).setCellValue(strArray[2]);
					rr.createCell(5).setCellValue("��");
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
		wbnew.write(new FileOutputStream("d:\\test_mapping.xlsx"));
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
