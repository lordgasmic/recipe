package com.lordgasmic.recipe.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 3/2/17.
 */

class ItemDescriptor {

    private String name;
    private Map<String, List<Table>> tables;

    public ItemDescriptor() {
        tables = new HashMap<>();
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public Map<String, List<Table>> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        String type = table.getType();

        List<Table> tableList = tables.get(type);
        if (tableList == null) {
            tableList = new ArrayList<>();
        }

        tableList.add(table);
        tables.put(type, tableList);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ItemDescriptor:");sb.append(System.lineSeparator());
        sb.append("name: " + name);sb.append(System.lineSeparator());

        for(List<Table> tableList : tables.values()) {
            for(Table t : tableList) {
                sb.append("table: " + t.toString());
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
