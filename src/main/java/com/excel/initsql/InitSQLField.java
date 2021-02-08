package com.excel.initsql;

public class InitSQLField {
    private String name;
    private String cnName;

    private String type;
    private String length;

    /**
     * ÊÇ·ñÐèÒªmapping
     */
    private boolean fromFlag = true;
    private String fromSechma;
    private String fromTableName;
    private String fromTableNameDes;

    private String fromName;
    private String fromCNName;
    private String fromType;
    private String fromLength;

    private String value;

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



    public boolean isFromFlag() {
        return fromFlag;
    }

    public void setFromFlag(boolean fromFlag) {
        this.fromFlag = fromFlag;
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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromCNName() {
        return fromCNName;
    }

    public void setFromCNName(String fromCNName) {
        this.fromCNName = fromCNName;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromLength() {
        return fromLength;
    }

    public void setFromLength(String fromLength) {
        this.fromLength = fromLength;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String changeType(String cell) {
        cell = cell.replace("£¨", "(").replace("£©", ")");
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
