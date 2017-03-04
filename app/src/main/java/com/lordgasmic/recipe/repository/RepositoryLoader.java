package com.lordgasmic.recipe.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.util.JsonToken;

import com.lordgasmic.recipe.R;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private Resources resources;

    public RepositoryLoader(Context context, Resources resources) {
        repository = new Repository(this);
        itemDescriptors = new ArrayList<ItemDescriptor>();
        this.context = context;
        this.resources = resources;

        readConfig(resources.openRawResource(R.raw.repository_config));
    }

    protected RepositoryItem getItem(String id, String itemDescriptor) {
        int index = -1;
        for (ItemDescriptor descriptor : itemDescriptors) {
            System.out.println(descriptor.getName());
            if (descriptor.getName().equals(itemDescriptor)) {
                index = itemDescriptors.indexOf(descriptor);
                break;
            }
        }

        System.out.println(index);
        if (index >= 0) {
            ItemDescriptor item = itemDescriptors.get(index);
            RecipeDbHelper dbHelper = new RecipeDbHelper(context, resources);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("Select * from uom", null);

            while (c.moveToNext()) {
                System.out.println(c.getString(0));
            }


            MutableRepositoryItemImpl mri = new MutableRepositoryItemImpl();
            mri.setName(item.getName());



            return mri.convertToRepositoryItem();
        }

        return null;
    }

    private void readConfig(InputStream inputStream) {
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

    private ItemDescriptor readItemDescriptor(JsonReader reader) throws IOException {
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

    private Table readTable(JsonReader reader) throws IOException {
        Table table = new Table();
        boolean readName = false;
        boolean readIdName = false;
        boolean readProperty = false;
        boolean readMultiName = false;
        boolean readDataType = false;
        boolean readItemType = false;

        while (reader.hasNext()) {
            if (readName && readIdName && readProperty && readMultiName && readDataType && readItemType) {
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
            } else if ("multi-column-name".equals(name)) {

            } else if ("data-type".equals(name)) {

            } else if ("item-type".equals(name)) {

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

    private Property readProperty(JsonReader reader) throws IOException {
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

        private static final String CREATE_RECIPE = "";
        private static final String CREATE_RECIPE_DIRECTION = "";
        private static final String CREATE_RECIPE_INGREDIENT = "";
        private static final String CREATE_ITEM = "";
        private static final String CREATE_INVENTORY = "";
        private static final String CREATE_UOM = "create table uom (name varchar(255) not null primary key)";

        private static final String INSERT_UOM = "insert into uom values (\"tbl\")";


        public RecipeDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_UOM);
            db.execSQL(CREATE_ITEM);
            db.execSQL(CREATE_INVENTORY);
            db.execSQL(CREATE_RECIPE);
            db.execSQL(CREATE_RECIPE_DIRECTION);
            db.execSQL(CREATE_RECIPE_INGREDIENT);

            db.execSQL(INSERT_UOM);
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
