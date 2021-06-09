package com.dotool.initsql;


import com.dotool.util.PublicTools;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateInitSQL {
// public static String fileName = "E:\\cc_project\\nwim\\SmartPark\\广东省二医\\05、上线运行\\第一迭代\\测试数据\\专题\\excel\\gdsyy.xlsx";
 public static String fileName = "D:\\do\\gdsyy.xlsx";


    public static void main(String[] args) throws Exception {
        List<InitSQLTable> tableList = ReadInitSQLExcel.read(fileName);
        System.out.println(tableList.size());

        generateSQL(tableList);

    }

    public static void generateSQL(List<InitSQLTable> tableList){
        try {
            String p = "d:/initsql/" + PublicTools.getCurrentTime("yyyyMMdd");
            File pf = new File(p);
            if (pf.exists()) {
                pf.delete();
            }
            if (!pf.exists()) {
                pf.mkdirs();
            }

            for (InitSQLTable table : tableList) {
                String tableName = table.getName();
                String sechma = table.getSechma();
                int index = 0;
                StringBuffer scriptSB = new StringBuffer();

                String des = table.getDes();
                des = des.replace("-", "_");


                System.out.println(pf.getPath());
                File fileAll = new File(pf.getPath() + "/" + des + ".sql");
                fileAll.createNewFile();
                FileOutputStream fos = new FileOutputStream(fileAll);
                fos.write("".getBytes());
                fos.flush();
                fos.close();

                StringBuffer sb = new StringBuffer();
                sb.append("delete from " + sechma + "." + tableName + ";\n");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

                String currentTime = PublicTools.getCurrentTime();
                String currentDay = PublicTools.getCurrentTime("yyyy-MM-dd");

                for (InitSQLRow row : table.getRowList()) {

                    for (InitSQLField data : row.getFieldList()) {

                    }

                    StringBuilder insert1 = new StringBuilder();
                    StringBuilder insert2 = new StringBuilder();
                    insert1.append("insert into " + sechma + "." + tableName + "(");
                    insert2.append(" values(");
                    int b = 0;
                    for (InitSQLField en : row.getFieldList()) {
                        if (b++ != 0) {
                            insert1.append(",");
                            insert2.append(",");
                        }
                        insert1.append("" + en.getName() + "");
                        String value = en.getValue();
                        if (value.endsWith(".0")) {
                            value = value.substring(0, value.length() - 2);
                        }
                        insert2.append("'" + value + "'");

                    }
                    insert1.append(",dw_batch_number,dw_creation_date,dw_last_update_date)");
                    insert2.append(",'" + sdf1.format(new Date()) + "','" + currentDay + "','" + currentTime + "')");

                    sb.append(insert1.toString() + insert2.toString() + ";\n");
                }

                FileUtils.writeStringToFile(fileAll, sb.toString(), "utf-8");


            }

        }catch (Exception ce) {
            ce.printStackTrace();
        }
    }


}
