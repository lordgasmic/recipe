package com.lordgasmic.recipe.repository;

import android.util.JsonReader;
import android.util.JsonToken;


import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bruce on 2/11/17.
 */

public class RepositoryLoader {

    private Repository repository;
    private List<String> itemDescriptors;

    public RepositoryLoader() {
        repository = new Repository();
        itemDescriptors = new ArrayList<>();
    }

    protected static RepositoryItem getItem(String id, String itemDescriptor) {
        return null;
    }

    public void readConfig(InputStream inputStream) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

            readJson(reader, "");
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Unable to read config", e);
        }
    }

    private void readJson(JsonReader reader, String whoCalledMe) throws IOException {
        String name = reader.nextName();
        System.out.println(name);
        if ("repository-template".equals(name)) {
            reader.beginArray();
            reader.beginObject();

            readJson(reader, name);

            reader.endObject();
            reader.endArray();
        }
        else if ("item-descriptor".equals(name)) {
            reader.beginObject();

            readJson(reader, name);

            reader.endObject();
        }
        else if ("table".equals(name)) {
            reader.beginObject();

            readJson(reader, name);

            reader.endObject();
        }
        else if ("id-column-name".equals(name)) {

        }
        else if ("property".equals(name)) {
            reader.beginObject();

            readJson(reader, name);

            reader.endObject();
        }
        else if ("column-name".equals(name)) {

        }
        else if ("data-type".equals(name)) {

        }
        else if ("name".equals(name)) {
            if (whoCalledMe.equals("item-descriptor")) {

            }
            else if (whoCalledMe.equals("table")) {

            }
            else if (whoCalledMe.equals("property")) {

            }
        }


    }

    private class ItemDescriptor {

        private String name;
        private Table table;

        public ItemDescriptor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setTable(String tableName) {
            table = new Table(tableName);
        }

        public Table getTable() {
            return table;
        }
    }

    private class Table {

        private String name;
        private String idColumn;
        private List<Property> properties;

        public Table(String name) {
            this.name = name;

            properties = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setIdColumn(String idColumn) {
            this.idColumn = idColumn;
        }

        public String getIdColumn() {
            return idColumn;
        }

        public void addProperty(Property property) {
            properties.add(property);
        }

        public List<Property> getProperties() {
            return properties;
        }
    }

    private class Property {

        private String name;
        private String columnName;
        private String dataType;

        public Property(String name) {
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
    }
}
