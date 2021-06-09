package com.dotool.initsql.gd2yy;

import com.dotool.initsql.CreateInitSQL;
import com.dotool.initsql.InitSQLField;
import com.dotool.initsql.InitSQLRow;
import com.dotool.initsql.InitSQLTable;
import com.dotool.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class gd2yy {

    public static void main(String[] args) {
        //小时车流信息
        dm_gdeyy_park_car_traffic_h_f();
        //人流
        dm_gdeyy_person_traffic_h_f();
        //用电数据采集（小时）
        dm_gdeyy_energy_electric_h_f();
        //用电数据汇总（月）
        dm_gdeyy_energy_electric_m_f();
        //用电数据采集_设备类型（小时）
        dm_gdeyy_energy_electric_type_h_f();
        //用电数据汇总_设备类型（月）
        dm_gdeyy_energy_electric_type_m_f();
        //用水数据采集（小时）
        dm_gdeyy_energy_water_h_f();
        //用水数据汇总（月）
        dm_gdeyy_energy_water_m_f();
        //门诊流量监控（小时）
        dm_gdeyy_hospital_patient_flow_h_f();
        //月度医疗收入_药品收入趋势分析
        dm_gdeyy_hospital_income_m_f();

    }

    /**
     * 小时车流信息
     * dm_gdeyy_park
     * dm_gdeyy_park_car_traffic_h_f
     */
    public static void dm_gdeyy_park_car_traffic_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_park");
        table.setName("dm_gdeyy_park_car_traffic_h_f");
        table.setDes("小时车流信息");

//        Date day = new Date();
        Date day = DateUtil.parse("2021-02-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] type = "新能源车,汽油车".split(",");
        String[] source = "省内,省外".split(",");
        String[] in = "进场,出场".split(",");

        for (int i = 0; i < 1000; i++) {

            for (int j = 0; j < 2 ; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "statistical_time=" + (statistical_time + ",");

                String typeStr = type[getRandom(0, 1)];
                System.out.print(typeStr + ",");
                row += "enterprise_name=" + (typeStr + ",");

                String sourceStr = source[getRandom(0, 1)];
                System.out.print(sourceStr + ",");
                row += "vehicle_source=" + (sourceStr + ",");

                String inStr = in[j];
                System.out.print(inStr + ",");
                row += "entrance_exit_signs=" + (inStr + ",");

                int hours = day.getHours();
                if (hours >= 7 && hours < 23) {
                    int consumption = getRandom(200, 500);
                    System.out.print(consumption + ",");
                    row += "statistical_number=" + (consumption + ",");
                } else {
                    int consumption = getRandom(100, 200);
                    System.out.print(consumption + ",");
                    row += "statistical_number=" + (consumption + ",");
                }

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "hour");
        }
        CreateInitSQL.generateSQL(tableList);
    }


    /**
     * 人流
     * dm_gdeyy_person
     * dm_gdeyy_person_traffic_h_f
     */
    public static void dm_gdeyy_person_traffic_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_person");
        table.setName("dm_gdeyy_person_traffic_h_f");
        table.setDes("人流");

//        Date day = new Date();
        Date day = DateUtil.parse("2021-02-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

//        String[] type = "新能源车,汽油车".split(",");
//        String[] source = "省内,省外".split(",");
//        String[] in = "进场,出场".split(",");

        for (int i = 0; i < 1000; i++) {

            String row = "";

            String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
            System.out.print(id + i + ",");
            row += "record_id=" + (id + i + ",");

            System.out.print(statistical_time + ",");
            row += "record_time=" + (statistical_time + ",");

            int hours = day.getHours();
            if (hours >= 7 && hours < 23) {
                int consumption = getRandom(500, 2000);
                System.out.print(consumption + ",");
                row += "record_number=" + (consumption + ",");
            } else {
                int consumption = getRandom(100, 200);
                System.out.print(consumption + ",");
                row += "record_number=" + (consumption + ",");
            }

            System.out.println("");
            transformRow(table, row);

            day = DateUtil.increase(day, "hour");
        }
        CreateInitSQL.generateSQL(tableList);
    }

    /**
     * 用电数据采集（小时）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_electric_h_f
     */
    public static void dm_gdeyy_energy_electric_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_electric_h_f");
        table.setDes("用电数据采集（小时）");

//        Date day = new Date("2020-08-01");
        Date day = DateUtil.parse("2021-01-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,1".split(",");
        String[] floor = "1,1,1".split(",");

        String[] xx = "1,1,1;2,1,1".split(";");

        for (int i = 0; i < 2000; i++) {

            for (int j = 0; j < 2; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

//                System.out.print(xx[j] + ",");

                System.out.print(acquisition_device[j] + ",");
                row += "acquisition_device=" + (acquisition_device[j] + ",");

                System.out.print(building[j] + ",");
                row += "building=" + (building[j] + ",");

                System.out.print(floor[j] + ",");
                row += "floor=" + (floor[j] + ",");

                int hours = day.getHours();
                if (hours >= 7 && hours < 23) {
                    int consumption = getRandom(500, 2000);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                } else {
                    int consumption = getRandom(100, 200);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                }

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "hour");
        }
        CreateInitSQL.generateSQL(tableList);
    }

    /**
     * 用电数据汇总（月）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_electric_m_f
     */
    public static void dm_gdeyy_energy_electric_m_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_electric_m_f");
        table.setDes("用电数据汇总（月）");

        Date day = DateUtil.parse("2020-08-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,1".split(",");
        String[] floor = "1,1,1".split(",");

        String[] xx = "1,1,1;2,1,1".split(";");

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 2; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

//                System.out.print(xx[j] + ",");

                System.out.print(acquisition_device[j] + ",");
                row += "acquisition_device=" + (acquisition_device[j] + ",");

                System.out.print(building[j] + ",");
                row += "building=" + (building[j] + ",");

                System.out.print(floor[j] + ",");
                row += "floor=" + (floor[j] + ",");

                int consumption = getRandom(520903, 750903);
                System.out.print(consumption + ",");
                row += "consumption=" + (consumption + ",");

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "month");
        }
        CreateInitSQL.generateSQL(tableList);
    }


    /**
     * 用电数据采集_设备类型（小时）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_electric_type_h_f
     */
    public static void dm_gdeyy_energy_electric_type_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_electric_type_h_f");
        table.setDes("用电数据采集_设备类型（小时）");

//        Date day = new Date();
        Date day = DateUtil.parse("2021-01-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,2".split(",");
        String[] floor = "1,1,1".split(",");

        String[] xx = "照明插座,暖通空调,特殊功能设备,动力设备".split(",");

        for (int i = 0; i < 2000; i++) {

            for (int j = 0; j < 4; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");
                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

                System.out.print(xx[j] + ",");
                row += "type=" + (xx[j] + ",");

                int hours = day.getHours();
                if (hours >= 7 && hours < 23) {
                    int consumption = getRandom(500, 2000);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                } else {
                    int consumption = getRandom(100, 200);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                }

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "hour");
        }

        CreateInitSQL.generateSQL(tableList);

    }


    /**
     * 用电数据汇总_设备类型（月）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_electric_type_m_f
     */
    public static void dm_gdeyy_energy_electric_type_m_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_electric_type_m_f");
        table.setDes("用电数据汇总_设备类型（月）");

        Date day = DateUtil.parse("2020-08-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,2".split(",");
        String[] floor = "1,1,1".split(",");

        String[] xx = "照明插座,暖通空调,特殊功能设备,动力设备".split(",");

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 4; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

                System.out.print(xx[j] + ",");
                row += "type=" + (xx[j] + ",");


                int consumption = getRandom(600000, 700000);
                System.out.print(consumption + ",");
                row += "consumption=" + (consumption + ",");


                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "month");
        }

        CreateInitSQL.generateSQL(tableList);

    }


    /**
     * 用水数据采集（小时）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_water_h_f
     */
    public static void dm_gdeyy_energy_water_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_water_h_f");
        table.setDes("用水数据采集（小时）");

//        Date day = new Date();
        Date day = DateUtil.parse("2021-01-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,1".split(",");
        String[] floor = "1,1,1".split(",");

        String[] xx = "1,1,1;2,1,1".split(";");

        for (int i = 0; i < 2000; i++) {

            for (int j = 0; j < 2; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

//                System.out.print(xx[j] + ",");

                System.out.print(acquisition_device[j] + ",");
                row += "acquisition_device=" + (acquisition_device[j] + ",");

                System.out.print(building[j] + ",");
                row += "building=" + (building[j] + ",");

                System.out.print(floor[j] + ",");
                row += "floor=" + (floor[j] + ",");

                int hours = day.getHours();
                if (hours >= 7 && hours < 23) {
                    int consumption = getRandom(200, 500);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                } else {
                    int consumption = getRandom(100, 200);
                    System.out.print(consumption + ",");
                    row += "consumption=" + (consumption + ",");
                }

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "hour");
        }
        CreateInitSQL.generateSQL(tableList);
    }

    /**
     * 用水数据汇总（月）
     * dm_gdeyy_energy
     * dm_gdeyy_energy_water_m_f
     */
    public static void dm_gdeyy_energy_water_m_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_energy");
        table.setName("dm_gdeyy_energy_water_m_f");
        table.setDes("用水数据汇总（月）");

        Date day = DateUtil.parse("2020-08-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,1".split(",");
        String[] floor = "1,1".split(",");

        String[] xx = "1,1,1;2,1,1".split(";");

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 2; j++) {

                String row = "";

                String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
                System.out.print(id + i + ",");
                row += "record_id=" + (id + i + ",");

                System.out.print(statistical_time + ",");
                row += "acquisition_time=" + (statistical_time + ",");

               /* System.out.print(acquisition_device[getRandom(0, 1)] + ",");
                System.out.print(building[getRandom(0, 1)] + ",");*/
//                System.out.print("1" + ",");

                System.out.print(acquisition_device[j] + ",");
                row += "acquisition_device=" + (acquisition_device[j] + ",");

                System.out.print(building[j] + ",");
                row += "building=" + (building[j] + ",");

                System.out.print(floor[j] + ",");
                row += "floor=" + (floor[j] + ",");

                int consumption = getRandom(190000, 220000);
                System.out.print(consumption + ",");
                row += "consumption=" + (consumption + ",");
//                System.out.print(getRandom(3000, 50000) + ",");

                System.out.println("");
                transformRow(table, row);
            }

            day = DateUtil.increase(day, "month");
        }
        CreateInitSQL.generateSQL(tableList);
    }


    /**
     * 门诊流量监控（小时）
     * dm_gdeyy_hospital
     * dm_gdeyy_hospital_patient_flow_h_f
     */
    public static void dm_gdeyy_hospital_patient_flow_h_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_hospital");
        table.setName("dm_gdeyy_hospital_patient_flow_h_f");
        table.setDes("门诊流量监控（小时）");

//        Date day = new Date();
        Date day = DateUtil.parse("2021-02-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

//        String[] acquisition_device = "1,2".split(",");
//        String[] building = "1,1".split(",");
//        String[] floor = "1,1,1".split(",");


        for (int i = 0; i < 1000; i++) {

            String row = "";

            String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
            System.out.print(id + i + ",");
            row += "record_id=" + (id + i + ",");

            System.out.print(statistical_time + ",");
            row += "acquisition_time=" + (statistical_time + ",");


            int hours = day.getHours();
            if (hours >= 7 && hours < 23) {
                int filing_person_num = getRandom(200, 500);
                System.out.print(filing_person_num + ",");
                row += "filing_person_num=" + (filing_person_num + ",");
                int outpatient_service_person_num = getRandom(300, 1000);
                System.out.print(outpatient_service_person_num + ",");
                row += "outpatient_service_person_num=" + (outpatient_service_person_num + ",");
                int emergency_services_person_num = getRandom(300, 500);
                System.out.print(emergency_services_person_num + ",");
                row += "emergency_services_person_num=" + (emergency_services_person_num + ",");
                int take_medicine_person_num = getRandom(300, 500);
                System.out.print(take_medicine_person_num + ",");
                row += "take_medicine_person_num=" + (take_medicine_person_num + ",");
            } else {
                int filing_person_num = getRandom(200, 300);
                System.out.print(filing_person_num + ",");
                row += "filing_person_num=" + (filing_person_num + ",");
                int outpatient_service_person_num = getRandom(200, 300);
                System.out.print(outpatient_service_person_num + ",");
                row += "outpatient_service_person_num=" + (outpatient_service_person_num + ",");
                int emergency_services_person_num = getRandom(200, 300);
                System.out.print(emergency_services_person_num + ",");
                row += "emergency_services_person_num=" + (emergency_services_person_num + ",");
                int take_medicine_person_num = getRandom(200, 300);
                System.out.print(take_medicine_person_num + ",");
                row += "take_medicine_person_num=" + (take_medicine_person_num + ",");
            }

            System.out.println("");
            transformRow(table, row);

            day = DateUtil.increase(day, "hour");
        }
        CreateInitSQL.generateSQL(tableList);
    }


    /**
     * 月度医疗收入_药品收入趋势分析
     * dm_gdeyy_hospital
     * dm_gdeyy_hospital_income_m_f
     */
    public static void dm_gdeyy_hospital_income_m_f() {
        List<InitSQLTable> tableList = new ArrayList<>();
        InitSQLTable table = new InitSQLTable();
        tableList.add(table);
        table.setSechma("dm_gdeyy_hospital");
        table.setName("dm_gdeyy_hospital_income_m_f");
        table.setDes("月度医疗收入_药品收入趋势分析");

        Date day = DateUtil.parse("2020-08-01");
        String id = "R" + DateUtil.parse(day, "yyyyMMddHHmmss");

        String[] acquisition_device = "1,2".split(",");
        String[] building = "1,1".split(",");
        String[] floor = "1,1,1".split(",");


        for (int i = 0; i < 7; i++) {

            String row = "";

            String statistical_time = DateUtil.parse(day, "yyyy-MM-dd HH:00:00");
//                System.out.print(id + i + ",");
            System.out.print(id + i + ",");
            row += "record_id=" + (id + i + ",");

            System.out.print(statistical_time + ",");
            row += "acquisition_time=" + (statistical_time + ",");


            int hours = day.getHours();

            int medical_income = getRandom(20000000, 50000000);
            System.out.print("medical_income=" + (medical_income + ","));
            row += "medical_income=" + (medical_income + ",");

            int medicine_income = getRandom(20000000, 50000000);
            System.out.print("medicine_income=" + (medicine_income + ","));
            row += "medicine_income=" + (medicine_income + ",");

            System.out.println("");
            transformRow(table, row);

            day = DateUtil.increase(day, "month");
        }
        CreateInitSQL.generateSQL(tableList);
    }

    private static int getRandom(int start, int end) {
        int i = (int) (Math.random() * (end - start + 1) + start);
        //System.out.println(i);
        return i;
    }

    private static void transformRow(InitSQLTable table, String rowStr) {
        InitSQLRow row = new InitSQLRow();
        table.addRow(row);

        String[] rowArray = rowStr.split(",");
        for (String s : rowArray) {
            if(s.trim().equals("")) {
                continue;
            }
            String[] sArray = s.split("=");
            InitSQLField field = new InitSQLField();
            row.addField(field);
            field.setName(sArray[0]);
            field.setCnName(sArray[0]);
            field.setValue(sArray[1]);
        }
    }
}
