package com.lordgasmic.recipe.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private List<ItemDescriptor> itemDescriptors;
    private Context context;

    public RepositoryLoader(Context context) {
        repository = new Repository(this);
        itemDescriptors = new ArrayList<ItemDescriptor>();
        this.context = context;
    }

    protected RepositoryItem getItem(String id, String itemDescriptor) {
        int index = itemDescriptors.contains(itemDescriptor) ? itemDescriptors.indexOf(itemDescriptor) : -1;

        if (index >= 0) {
            ItemDescriptor item = itemDescriptors.get(index);
            RecipeDbHelper dbHelper = new RecipeDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            // https://developer.android.com/training/basics/data-storage/databases.html#WriteDbRow
            // https://developer.android.com/reference/android/database/sqlite/SQLiteQueryBuilder.html


            MutableRepositoryItemImpl mri = new MutableRepositoryItemImpl();
            mri.setName(item.getName());



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

                    itemDescriptor.addTable(readTable(reader));

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
        private String repositoryId;
        private String name;
        private Map<String, Object> properties;

        public MutableRepositoryItemImpl() {
            properties = new HashMap<>();
        }

        public RepositoryItem convertToRepositoryItem() {
            return new RepositoryItemImpl(this);
        }

        @Override
        public void setRepositoryId(String id) {
            repositoryId = id;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setProperty(String id, Object object) {
            properties.put(id, object);
        }

        @Override
        public String getRepositoryId() {
            return repositoryId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getProperty(String property) {
            return properties.get(property);
        }

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    private class RecipeDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Recipe.db";

        public RecipeDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private class ItemDescriptor {

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
