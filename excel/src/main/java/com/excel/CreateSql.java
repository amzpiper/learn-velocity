package com.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateSql {
	public static void main(String[] args) throws Exception {
		String filePath = "E:\\nhjc.xlsx";
		
		
		File f = new File(filePath);
		String p = f.getParent();
		File pf = new File(p+"//sql");
		if(!pf.exists()) {
			pf.mkdirs();
		}
		File file = new File(p+"/sql/sql.txt");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write("".getBytes());
		fos.flush();
		fos.close();
		Workbook wb = new XSSFWorkbook(filePath);
		Sheet st01 = wb.getSheet("数据资源");
		
		int last = st01.getLastRowNum();
		//last = 9;
		for(int i = 2 ; i <= last ; i++){
			
			Row row = st01.getRow(i);
			
			Cell c2 = row.getCell(2);
			
			if(checkCell(c2.getStringCellValue())){
				Cell tableNameCell = row.getCell(9) ;
				System.out.println(c2 + "-" +tableNameCell);
				Sheet sheet = wb.getSheet(c2.getStringCellValue());
				StringBuffer sb = new StringBuffer();
				
				sb.append("\n\n-- "+c2.getStringCellValue()+" --\n");
				sb.append("DROP TABLE IF EXISTS `"+tableNameCell.getStringCellValue()+"`;\n");
				sb.append("CREATE TABLE IF NOT EXISTS `"+tableNameCell.getStringCellValue()+"`\n(\n");
				
				int count = sheet.getLastRowNum();
				for(int a = 4 ;a <= count ; a++){
					Row cr = sheet.getRow(a);
					//System.out.println(a);
					if(null == cr || null == cr.getCell(1) || !checkCell(cr.getCell(1).getStringCellValue())){
						break;
					}
					if(a == 4){
						sb.append("    `"+cr.getCell(1).toString().replaceAll("\\s+", "")+"` "+cr.getCell(3).toString().replaceAll("\\s+", "") + changeType(cr.getCell(4).getNumericCellValue()) + " primary key");
					    sb.append(" COMMENT '"+cr.getCell(0).toString().replaceAll("\\s+", "")+"'");
					}else{
						sb.append(",\n    `"+cr.getCell(1).toString().replaceAll("\\s+", "")+"` "+cr.getCell(3).toString().replaceAll("\\s+", "")+ changeType(cr.getCell(4).getNumericCellValue()));
						 sb.append(" COMMENT '"+cr.getCell(0).toString().replaceAll("\\s+", "")+"'");
					}
				}
				sb.append("\n);\n");
				System.out.println(sb.toString());
				
				
				/*if(!file.exists()){
					file.createNewFile();
				}*/
				
				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.write(sb.toString());
				pw.flush();
				pw.close();
				/*BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				bw.append(sb.toString()+common.toString()).flush();;*/
			}
			
		}
		
		
		/*Sheet sheet = wb.getSheet("污染源基本信息");
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("CREATE TABLE IF NOT EXISTS dwi.en.");
		
		int count = sheet.getLastRowNum();
		for(int i = 2 ;i <= count ; i++){
			Row row = sheet.getRow(i);
			if(i == 2){
				sb.append(row.getCell(arg0));
			}
			System.out.println(row.getCell(3));
		}*/
		//System.out.println(row);
	
	}
	
	public static boolean checkCell(String cell){
		if(null == cell || "".equals(cell)){
			return false;
		}
		
		return true;
	}
	
	public static String changeType(Double cell){
		if(0 == cell || null == cell) {
			return "";
		}
		return "("+cell.intValue()+")";
	}
}
