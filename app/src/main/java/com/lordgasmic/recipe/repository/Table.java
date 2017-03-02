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
}
