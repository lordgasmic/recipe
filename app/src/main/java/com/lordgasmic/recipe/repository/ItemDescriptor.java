package com.lordgasmic.recipe.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce on 3/2/17.
 */

class ItemDescriptor {

    private String name;
    private List<Table> tables;

    public ItemDescriptor() {
        tables = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addTable(Table table) {
        tables.add(table);
    }
}
