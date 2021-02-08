package com.excel.dlfMapping;

import org.apache.poi.ss.usermodel.Cell;

public class Field {
    private String name;
    private String cnName;
    private String des;

    private String type;
    private String length;
    private String decimal;

    //物理模型名称
    private String physicsName;
    //是否入主题
    private String inDBThemeFlag;
    private String physicsType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        if ("text".equalsIgnoreCase(type)) {
            length = "";
        }
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDecimal() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public String getPhysicsName() {
        if (physicsName == null || physicsName.trim().equals("")) {
            physicsName = name;
        }
        return physicsName;
    }

    public void setPhysicsName(String physicsName) {
        this.physicsName = physicsName;
    }

    public String getInDBThemeFlag() {
        if (inDBThemeFlag == null || inDBThemeFlag.trim().equals("")) {
            inDBThemeFlag = "Y";
        }
        return inDBThemeFlag;
    }

    public void setInDBThemeFlag(String inDBThemeFlag) {
        this.inDBThemeFlag = inDBThemeFlag;
    }

    public String getPhysicsType() {
        physicsType = changeType(type);
        return physicsType;
    }

    public void setPhysicsType(String physicsType) {
        this.physicsType = physicsType;
    }

    public static String changeType(String cell) {
        cell = cell.replace("（", "(").replace("）", ")");
        cell = getString(cell);
        if (cell.toUpperCase().startsWith("mediumtext".toUpperCase())) {
            return "text";
        }
        else if (cell.toUpperCase().startsWith("smallint".toUpperCase())) {
            return "int";
        }
        else if (cell.toUpperCase().startsWith("VARCHAR2")) {
            return cell.toUpperCase().replace("VARCHAR2", " character varying");
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

    public static String getString(String cell) {
        if (cell != null) {
            String str = cell.toString().replace(String.valueOf((char) 160), " ").replaceAll("^\\s+", "")
                    .replaceAll("\\s+$", "");
            str = str.trim().replaceAll("\\r\\n", "");
            return str;
        }
        return "";
    }
}
