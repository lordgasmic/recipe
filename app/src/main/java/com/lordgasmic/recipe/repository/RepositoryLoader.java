package com.lordgasmic.recipe.repository;

import android.util.JsonReader;
import android.util.JsonToken;

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
    private static List<ItemDescriptor> itemDescriptors;

    public RepositoryLoader() {
        repository = new Repository(this);
        itemDescriptors = new ArrayList<ItemDescriptor>();
    }

    protected RepositoryItem getItem(String id, String itemDescriptor) {
        int index = itemDescriptors.contains(itemDescriptor) ? itemDescriptors.indexOf(itemDescriptor) : -1;

        if (index >= 0) {
            ItemDescriptor item = itemDescriptors.get(index);

            MutableRepositoryItemImpl mri = new MutableRepositoryItemImpl();
            return mri.convertToRepositoryItem();
        }

        return null;
    }

    public void readConfig(InputStream inputStream) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

            readJson(reader);

        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read config", e);
        }
    }

    private void readJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.BEGIN_OBJECT) {
            reader.beginObject();

            String name = reader.nextName();
            if ("item-descriptor".equals(name)) {
                reader.beginArray();

                while (reader.hasNext() && reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();

                    itemDescriptors.add(readItemDescriptor(reader));

                    reader.endObject();
                }

                reader.endArray();
            }

            reader.endObject();
        }
    }

    public ItemDescriptor readItemDescriptor(JsonReader reader) throws IOException {
        ItemDescriptor itemDescriptor = new ItemDescriptor();
        boolean readName = false;
        boolean readTable = false;

        while (reader.hasNext()) {
            if (readName && readTable) {
                break;
            }

            String name = reader.nextName();
            if ("name".equals(name)) {
                String mName = reader.nextString();
                itemDescriptor.setName(mName);

                readName = true;
            } else if ("table".equals(name)) {
                reader.beginArray();

                while (reader.hasNext() && reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();

                    itemDescriptor.setTable(readTable(reader));

                    reader.endObject();
                }

                reader.endArray();

                readTable = true;
            }
        }

        return itemDescriptor;
    }

    public Table readTable(JsonReader reader) throws IOException {
        Table table = new Table();
        boolean readName = false;
        boolean readIdName = false;
        boolean readProperty = false;

        while (reader.hasNext()) {
            if (readName && readIdName && readProperty) {
                break;
            }

            String name = reader.nextName();
            if ("name".equals(name)) {
                String mName = reader.nextString();
                table.setName(mName);
                readName = true;
            } else if ("id-column-name".equals(name)) {
                String idColumnName = reader.nextString();
                table.setIdColumn(idColumnName);
                readIdName = true;
            } else if ("properties".equals(name)) {
                reader.beginArray();

                while (reader.hasNext() && reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();

                    table.addProperty(readProperty(reader));

                    reader.endObject();
                }

                reader.endArray();

                readProperty = true;
            }
        }

        return table;
    }

    public Property readProperty(JsonReader reader) throws IOException {
        Property property = new Property();
        boolean readName = false;
        boolean readColumn = false;
        boolean readData = false;

        while (reader.hasNext()) {
            if (readName && readColumn && readData) {
                break;
            }

            String name = reader.nextName();
            if ("name".equals(name)) {
                String mName = reader.nextString();
                readName = true;
            } else if ("column-name".equals(name)) {
                String columnName = reader.nextString();
                property.setColumnName(columnName);
                readColumn = true;
            } else if ("data-type".equals(name)) {
                String dataType = reader.nextString();
                property.setDataType(dataType);
                readData = false;
            }
        }

        return property;
    }

    private class RepositoryItemImpl implements RepositoryItem {
        private String repositoryId;
        private String name;
        private Map<String, Object> properties;

        public RepositoryItemImpl(MutableRepositoryItemImpl mri) {
            repositoryId = mri.getRepositoryId();
            name = mri.getName();
            properties = mri.getProperties();
        }

        @Override
        public String getRepositoryId() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Object getProperty(String property) {
            return null;
        }
    }

    private class MutableRepositoryItemImpl implements MutableRepositoryItem {

        private Map<String, Object> properties;

        public MutableRepositoryItemImpl() {
            properties = new HashMap<>();
        }

        public RepositoryItem convertToRepositoryItem() {
            return new RepositoryItemImpl(this);
        }

        @Override
        public void setProperty(String id, Object object) {

        }

        @Override
        public String getRepositoryId() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Object getProperty(String property) {
            return null;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    private class ItemDescriptor {

        private String name;
        private Table table = new Table();

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public Table getTable() {
            return table;
        }

        public void setTable(Table table) {
            this.table = table;
        }
    }

    private class Table {

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

    private class Property {

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
    }
}
