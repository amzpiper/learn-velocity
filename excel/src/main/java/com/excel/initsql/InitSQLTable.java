package com.excel.initsql;

import java.util.ArrayList;
import java.util.List;

public class InitSQLTable {
    private String sechma;
    private String name;
    private String des;

    private String fromSechma;
    private String fromTableName;
    private String fromTableNameDes;

    private List<InitSQLRow> rowList = new ArrayList<>();

    public String getSechma() {
        return sechma;
    }

    public void setSechma(String sechma) {
        this.sechma = sechma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<InitSQLRow> getRowList() {
        return rowList;
    }

    public void setRowList(List<InitSQLRow> rowList) {
        this.rowList = rowList;
    }

    public void addRow(InitSQLRow row) {
        rowList.add(row);
    }

    public String getFromSechma() {
        return fromSechma;
    }

    public void setFromSechma(String fromSechma) {
        this.fromSechma = fromSechma;
    }

    public String getFromTableName() {
        return fromTableName;
    }

    public void setFromTableName(String fromTableName) {
        this.fromTableName = fromTableName;
    }

    public String getFromTableNameDes() {
        return fromTableNameDes;
    }

    public void setFromTableNameDes(String fromTableNameDes) {
        this.fromTableNameDes = fromTableNameDes;
    }
}
