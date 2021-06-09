package com.dotool.document.tool;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String sechma;
    private String name;
    private String des;

    private List<Field> fieldList = new ArrayList<>();

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

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public void addField(Field field) {
        fieldList.add(field);
    }
}
