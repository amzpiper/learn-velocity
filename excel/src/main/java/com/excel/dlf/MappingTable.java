package com.excel.dlf;

import java.util.ArrayList;
import java.util.List;

public class MappingTable {
    private String sechma;
    private String name;
    private String des;

    private String fromSechma;
    private String fromTableName;
    private String fromTableNameDes;

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

    public List<MappingField> getFieldNeedList() {
        List<MappingField> needFieldList = new ArrayList<>();
        for (MappingField mappingField : fieldList) {
            if (mappingField.isFromFlag()) {
                needFieldList.add(mappingField);
            }
        }
        return needFieldList;
    }

    public void setFieldList(List<MappingField> fieldList) {
        this.fieldList = fieldList;
    }

    public void addField(MappingField field) {
        fieldList.add(field);
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
