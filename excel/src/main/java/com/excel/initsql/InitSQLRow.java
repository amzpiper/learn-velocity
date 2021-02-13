package com.excel.initsql;

import java.util.ArrayList;
import java.util.List;

public class InitSQLRow {


    private List<InitSQLField> fieldList = new ArrayList<>();

    public List<InitSQLField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<InitSQLField> fieldList) {
        this.fieldList = fieldList;
    }

    public void addField(InitSQLField field) {
        fieldList.add(field);
    }
}
