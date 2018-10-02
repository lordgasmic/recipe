package com.lordgasmic.recipe.repository;

import com.lordgasmic.recipe.constants.DataType;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by bruce on 3/2/17.
 */
@Data
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

    public void addProperty(Property property) {
        properties.add(property);
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
