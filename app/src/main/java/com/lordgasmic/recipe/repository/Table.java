package com.lordgasmic.recipe.repository;

import com.lordgasmic.recipe.constants.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 3/2/17.
 */

class Table {

    private String name;
    private String idColumn;
    private List<Property> properties;
    private String multiColumnName;
    private DataType dataType;
    private String itemType;
    private String type;

    public Table() {
        properties = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String getMultiColumnName() {
        return multiColumnName;
    }

    public void setMultiColumnName(String multiColumnName) {
        this.multiColumnName = multiColumnName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name); sb.append(System.lineSeparator());
        sb.append("idColumn: " + idColumn); sb.append(System.lineSeparator());
        sb.append("itemType: " + itemType); sb.append(System.lineSeparator());
        sb.append("multiColumnName: " + multiColumnName); sb.append(System.lineSeparator());
        sb.append("dataType: " + dataType); sb.append(System.lineSeparator());
        sb.append("type: " + type);

        for (Property p : properties) {
            sb.append("property: " + p.toString());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
