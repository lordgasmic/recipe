package com.lordgasmic.recipe.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 3/2/17.
 */

class Table {

    private String name;
    private String idColumn;
    private List<Property> properties;
    private String multiIdColumn;
    private String dataType;
    private String itemType;


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

    public String getMultiIdColumn() {
        return multiIdColumn;
    }

    public void setMultiIdColumn(String multiIdColumn) {
        this.multiIdColumn = multiIdColumn;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
