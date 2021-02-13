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

public class RunMain {
	public static void main(String[] args) throws Exception {
		String filePath = "E:\\SmartPark\\重庆璧山\\03、系统设计\\详细设计\\贴源层\\能耗监测\\2.xlsx";


		File f = new File(filePath);
		String p = f.getParent();
		File pf = new File(p+"//sql");
		if(!pf.exists()) {
			pf.mkdirs();
		}
		File file = new File(p+"/sql/dwi_sql.txt");
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
				Cell tableNameCell = row.getCell(18) ;
				Cell schema = row.getCell(17) ;
				System.out.println(c2 + "-" +tableNameCell);
				Sheet sheet = wb.getSheet(c2.getStringCellValue());
				StringBuffer sb = new StringBuffer();
				StringBuffer common = new StringBuffer();

				sb.append("\n\n-- "+c2.getStringCellValue()+" --\n");
				sb.append("DROP TABLE IF EXISTS \""+schema+"\".\""+tableNameCell.getStringCellValue()+"\";\n");
				sb.append("CREATE TABLE IF NOT EXISTS \""+schema+"\".\""+tableNameCell.getStringCellValue()+"\"\n(\n");

				int count = sheet.getLastRowNum();

				String id = "";
				String tableCommon = "";
				for(int a = 2 ;a <= count ; a++){
					Row cr = sheet.getRow(a);
					//System.out.println(a);
					if(null == cr || null == cr.getCell(3) || !checkCell(cr.getCell(3).getStringCellValue())){
						break;
					}
					if(a == 2){
						id = cr.getCell(3).getStringCellValue().toString().replaceAll("\\s+", "");
						tableCommon = cr.getCell(1).getStringCellValue().toString().replaceAll("\\s+", "");
						sb.append("    \""+cr.getCell(3).toString().replaceAll("\\s+", "")+"\" " + changeType(cr.getCell(4).getStringCellValue().replaceAll("\\s+", "")));
					}else{
						sb.append(",\n    \""+cr.getCell(3).toString().replaceAll("\\s+", "")+"\" " + changeType(cr.getCell(4).getStringCellValue().replaceAll("\\s+", "")));
					}
					common.append("COMMENT ON COLUMN "+schema+"."+tableNameCell.getStringCellValue().toString().replaceAll("\\s+", "")+"."+cr.getCell(3).toString().replaceAll("\\s+", "")+" IS '"+cr.getCell(2).toString().replaceAll("\\s+", "")+"';\n");
				}

				sb.append(",\n    \"dwi_creation_date\" timestamp(6) without time zone default now()");
				sb.append(")WITH(orientation=row,compression=no) ");
				sb.append("DISTRIBUTE BY HASH(\""+id+"\") ;\n");
				sb.append("COMMENT ON TABLE \""+schema+"\".\""+tableNameCell.getStringCellValue().replaceAll("\\s+", "")+"\" IS '"+tableCommon+"';\n");
				System.out.println(sb.toString()+common.toString());
				
				
				/*if(!file.exists()){
					file.createNewFile();
				}*/

				PrintWriter pw = new PrintWriter(new FileWriter(file, true));
				pw.write(sb.toString()+common.toString());
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

	public static String changeType(String cell){
		cell = cell.replace("（","(").replace("）", ")");
		if(cell.toUpperCase().startsWith("VARCHAR")){
			return cell.toUpperCase().replace("VARCHAR", " character varying");
		}else if(cell.toUpperCase().startsWith("TIMESTAMP")){
			return cell.toLowerCase() + " without time zone";
		}else if(cell.toUpperCase().startsWith("DATETIME") || cell.toUpperCase().startsWith("DATE")) {
			return "timestamp(6)" + " without time zone";
		}else if(cell.toUpperCase().startsWith("DOUBLE")) {
			return cell.toUpperCase().replace("DOUBLE", "number") ;
		}else if(cell.toUpperCase().startsWith("INT")) {
			return "integer" ;
		}else if(cell.toUpperCase().startsWith("BIGINT")) {
			return "bigint";
		}else if(cell.toUpperCase().startsWith("TINYINT")) {
			return "tinyint";
		}else if(cell.toUpperCase().startsWith("FLOAT")) {
			return cell.toUpperCase().replace("FLOAT", "number");
		}

		return cell.toLowerCase();
	}
}
