package com.excel.dlfMapping;

import java.util.ArrayList;
import java.util.List;

public class MappingTable {
    private String sechma;
    private String name;
    private String des;

    private List<MappingField> fieldList = new ArrayList<>();

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

    public List<MappingField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<MappingField> fieldList) {
        this.fieldList = fieldList;
    }

    public void addField(MappingField field) {
        fieldList.add(field);
    }
}
