package com.lordgasmic.recipe.repository;

import com.lordgasmic.recipe.constants.DataType;

/**
 * A property mapping between item descriptor and column
 *
 * Created by bruce on 3/2/17.
 */

class Property {

    private String name;
    private String columnName;
    private DataType dataType;

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

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
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
