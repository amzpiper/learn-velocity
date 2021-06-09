package com.dotool.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

public class FileUtil {
	public static String getString(Cell cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim();
			return str;
		}
		return "";
	}
	
	public static String getString(String cell) {
		if (cell != null) {
			String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
					.replaceAll("\\s+$", "");
			str = str.trim().replaceAll("\\r\\n", "");
			return str;
		}
		return "";
	}
	
	public static String changeType(String cell) {
		cell = cell.replace("??", "(").replace("??", ")");
		cell = getString(cell);
		if (cell.toUpperCase().startsWith("mediumtext".toUpperCase())) {
			return "text";
		}
		else if (cell.toUpperCase().startsWith("smallint".toUpperCase())) {
			return "int";
		}
		else if (cell.toUpperCase().startsWith("VARCHAR")) {
			return cell.toUpperCase().replace("VARCHAR", " character varying");
		} else if (cell.toUpperCase().startsWith("TIMESTAMP")) {
			return cell.toLowerCase() + " without time zone";
		} else if (cell.toUpperCase().startsWith("DATETIME") || cell.toUpperCase().startsWith("DATE")) {
			return "timestamp(6)" + " without time zone";
		} else if (cell.toUpperCase().startsWith("DOUBLE")) {
			return cell.toUpperCase().replace("DOUBLE", "number");
		} else if (cell.toUpperCase().startsWith("INT")) {
			return "integer";
		} else if (cell.toUpperCase().startsWith("BIGINT")) {
			return "bigint";
		} else if (cell.toUpperCase().startsWith("TINYINT")) {
			return "tinyint";
		} else if (cell.toUpperCase().startsWith("FLOAT")) {
			return cell.toUpperCase().replace("FLOAT", "number");
		}

		return cell.toLowerCase();
	}
	
	public static String changeType(String type , String length) {
		switch(type) {
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
	
	public static File copy(String path, String name, boolean reCreate) throws IOException {
		File file = new File(System.getProperty("user.dir") + "/src\\main\\resources\\temp.xlsx");

		File nf = new File(path + File.separator + name);

		new File(path).mkdirs();
		nf.delete();
		//nf.createNewFile();
		OutputStream fos = new FileOutputStream(nf);
		//System.out.println(file.getAbsolutePath());
		Files.copy(file.toPath(), fos);
		fos.flush();
		fos.close();
		return nf;
	}
	
	public static String getLength(String type, String length) {
		switch (type) {
		case "varchar":
			return length;
		case "date":
			return "6";
		case "datetime":
			return "6";
		case "smallint":
			return "";
		case "int":
			return "";
		case "char":
			return length;
		case "double":
			return length;
		case "decimal":
			return length;
		case "float":
			return length;
		case "timestamp":
			return "6";
		case "bigint":
			return "";
		case "tinyint":
			return "";
		case "mediumtext":
			return "text";
		case "longtext":
			return "";
		case "text":
			return "";
		}
		return "";
	}

	public static List<File> getDirByDirName(File file, String fileName) {
		List<File> lf = new ArrayList<>();
		for (File cf : file.listFiles()) {
			if (cf.getName().equals(fileName) && cf.isDirectory()) {
				lf.add(cf);

			} else if (cf.isDirectory()) {
				lf.addAll(getDirByDirName(cf, fileName));
				//return lf;
			}
		}
		return lf;
	}

	public static List<File> getFileByFileName(File file, String suffix) {
		List<File> lf = new ArrayList<>();
		for (File cf : file.listFiles()) {
			if (cf.getName().endsWith(suffix) && cf.isFile()) {
				lf.add(cf);
			} else if (cf.isDirectory()) {
				lf.addAll(getFileByFileName(cf, suffix));
				//return lf;
			}
		}
		return lf;
	}

	public static String changeLength(String str) {
		str = str.toLowerCase();
		final StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(str);
		while (m.find()){
			m.appendReplacement(sb,Integer.parseInt(m.group(1))*3+"");
		}
		m.appendTail(sb);

		return sb.toString();
	}
}
