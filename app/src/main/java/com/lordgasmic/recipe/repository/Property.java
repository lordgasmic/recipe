package com.lordgasmic.recipe.repository;

/**
 * Created by bruce on 3/2/17.
 */

class Property {

    private String name;
    private String columnName;
    private String dataType;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Property:");sb.append(System.lineSeparator());
        sb.append("  name: " + name); sb.append(System.lineSeparator());
        sb.append("  columnName: " + columnName); sb.append(System.lineSeparator());
        sb.append("  dataType: " + dataType); sb.append(System.lineSeparator());

        return sb.toString();
    }
}
