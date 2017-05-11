package com.lordgasmic.recipe.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;
import android.util.JsonToken;

import com.lordgasmic.recipe.R;
import com.lordgasmic.recipe.constants.DataType;
import com.lordgasmic.recipe.constants.IdGeneratorConstants;
import com.lordgasmic.recipe.constants.ItemConstants;
import com.lordgasmic.recipe.constants.UomConstants;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Main repository loader.  The layer between the Repository and the database.
 *
 * Created by bruce on 2/11/17.
 */

public class RepositoryLoader {

    private Repository repository;
    private List<ItemDescriptor> itemDescriptors;
    private SQLiteDatabase db;

    public RepositoryLoader(Context context, Resources resources) {
        repository = new Repository(this);
        itemDescriptors = new ArrayList<>();

        RecipeDbHelper dbHelper = new RecipeDbHelper(context, resources);
        db = dbHelper.getWritableDatabase();

        readConfig(resources.openRawResource(R.raw.repository_config));

        repository.loadRepository(itemDescriptors);

        initializeIdGenerator();
    }

    public ItemDescriptor findItemDescriptor(String itemDescriptor) {
        for (ItemDescriptor descriptor : itemDescriptors) {
            if (descriptor.getName().equals(itemDescriptor)) {
                return descriptor;
            }
        }

        return null;
    }

    public RepositoryItem getItem(String id, String itemDescriptor) {
        ItemDescriptor item = findItemDescriptor(itemDescriptor);

        if (item != null) {

            Map<String, List<Table>> tables = item.getTables();

            List<Table> primaryTable = tables.get("primary");
            if (primaryTable == null || primaryTable.isEmpty()) {
                throw new IllegalStateException("No primary table found for itemDescriptor: " + item.getName());
            }
            if (primaryTable.size() > 1) {
                throw new IllegalStateException("Multiple primary tables found for itemDescriptor: " + item.getName());
            }

            Table t = primaryTable.get(0);
            Cursor c = db.rawQuery("select * from " + t.getName() + " where " + t.getIdColumn() + " = " + "'" + id + "'", null);
            MutableRepositoryItemImpl mri = new MutableRepositoryItemImpl();
            if (c.moveToFirst()) {
                    mri.setName(item.getName());
                    mri.setRepositoryId(c.getString(c.getColumnIndex(t.getIdColumn())));
                    for (Property p: t.getProperties()) {
                        setProperty(mri, c, p);
                    }
                c.close();
            }
            else {
                return null;
            }

            List<Table> auxilaryTables = tables.get("auxilary");
            if (auxilaryTables != null) {
                for (Table auxTable : auxilaryTables) {
                    processAuxilaryTable(auxTable, mri);
                }
            }

            System.out.println(getId("uom"));
            return mri.convertToRepositoryItem();
        }


        return null;
    }

    public List<RepositoryItem> getAllItemsByDescriptor(String itemDescriptor) {
        List<RepositoryItem> repositoryItems = new ArrayList<>();

        ItemDescriptor item = findItemDescriptor(itemDescriptor);

        if (item != null) {

            Map<String, List<Table>> tables = item.getTables();

            List<Table> primaryTable = tables.get("primary");
            if (primaryTable == null || primaryTable.isEmpty()) {
                throw new IllegalStateException("No primary table found for itemDescriptor: " + item.getName());
            }
            if (primaryTable.size() > 1) {
                throw new IllegalStateException("Multiple primary tables found for itemDescriptor: " + item.getName());
            }

            Table t = primaryTable.get(0);
            Cursor c = db.rawQuery("select * from " + t.getName(), null);
            try {
                while (c.moveToNext()) {
                    MutableRepositoryItemImpl mri = new MutableRepositoryItemImpl();
                    mri.setName(item.getName());
                    mri.setRepositoryId(c.getString(c.getColumnIndex(t.getIdColumn())));
                    for (Property p : t.getProperties()) {
                        setProperty(mri, c, p);
                    }

                    List<Table> auxilaryTables = tables.get("auxilary");
                    if (auxilaryTables != null) {
                        for (Table auxTable : auxilaryTables) {
                            processAuxilaryTable(auxTable, mri);
                        }
                    }

                    repositoryItems.add(mri.convertToRepositoryItem());
                }
            } finally {
                c.close();
            }
        }

        return repositoryItems;
    }

    private void setProperty(MutableRepositoryItem mri, Cursor cursor, Property property) {
        switch (property.getDataType()) {
            case STRING:
                mri.setProperty(property.getName(), cursor.getString(cursor.getColumnIndex(property.getColumnName())));
                break;
            case INT:
                mri.setProperty(property.getName(), cursor.getInt(cursor.getColumnIndex(property.getColumnName())));
                break;
            case ITEM:
                RepositoryItem itemItem =  getItem(cursor.getString(cursor.getColumnIndex(property.getColumnName())), ItemConstants.ITEM_DESCRIPTOR);
                mri.setProperty(ItemConstants.ITEM_DESCRIPTOR, itemItem);
                break;
            case UOM:
                RepositoryItem uomItem =  getItem(cursor.getString(cursor.getColumnIndex(property.getColumnName())), UomConstants.ITEM_DESCRIPTOR);
                mri.setProperty(UomConstants.ITEM_DESCRIPTOR, uomItem);
                break;
            default:
                throw new IllegalArgumentException("Unable to find data type for " + property.getDataType());
        }
    }

    private void processAuxilaryTable(Table auxTable, MutableRepositoryItemImpl mutableRepositoryItem) {
        switch (auxTable.getDataType()) {
            case LIST:
                List<RepositoryItem> items = new ArrayList<>();
                Cursor c = db.rawQuery("select * from " + auxTable.getName() + " where " + auxTable.getIdColumn() + " = " + mutableRepositoryItem.repositoryId, null);
                if (c.moveToFirst()) {
                    do {
                        Property auxProperty = auxTable.getProperties().get(0);
                        RepositoryItem auxItem = getItem(c.getString(c.getColumnIndex(auxProperty.getColumnName())), auxTable.getItemType());
                        items.add(c.getInt(c.getColumnIndex(auxTable.getMultiColumnName())), auxItem);
                    } while (c.moveToNext());

                    c.close();
                }
                mutableRepositoryItem.setProperty(auxTable.getItemType(), items);
                break;
            default:
                throw new UnsupportedOperationException("DataType not implemented yet for: " + auxTable.getDataType());
        }
    }

    private void saveItem(RepositoryItem repositoryItem) {

    }

    private String getId(String itemDescriptor) {
        String[] where = { itemDescriptor };
        String retVal = "-1";
        int idSpaceInt = -1;
        try (Cursor cursor = db.query(IdGeneratorConstants.TABLE_NAME, null,IdGeneratorConstants.GET_ID_WHERE, where, null, null, null)) {
            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                String prefix = cursor.getString(cursor.getColumnIndex(IdGeneratorConstants.PREFIX));
                String suffix = cursor.getString(cursor.getColumnIndex(IdGeneratorConstants.SUFFIX));
                String idSpace = cursor.getString(cursor.getColumnIndex(IdGeneratorConstants.ID_SPACE));

                if (StringUtils.isNotEmpty(prefix)) {
                    sb.append(prefix);
                }

                if (StringUtils.isNotEmpty(idSpace)) {
                    sb.append(idSpace);
                    idSpaceInt = Integer.parseInt(idSpace);
                }

                if (StringUtils.isNotEmpty(suffix)) {
                    sb.append(suffix);
                }

                retVal = sb.toString();
            }
        }

        idSpaceInt++;
        ContentValues cv = new ContentValues();
        cv.put(IdGeneratorConstants.ID_SPACE,  idSpaceInt);
        db.update(IdGeneratorConstants.TABLE_NAME, cv, IdGeneratorConstants.GET_ID_WHERE, where);

        return retVal;
    }

    private void initializeIdGenerator() {
        List<String> idSpaceNames = new ArrayList<>();
        List<String> addSpaceNames = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("select id_space_name from id_generator", null)) {
            while (cursor.moveToNext()) {
                idSpaceNames.add(cursor.getString(0));
            }
        }

        for (ItemDescriptor itemDescriptor : itemDescriptors) {
            if (!idSpaceNames.contains(itemDescriptor.getName())) {
                addSpaceNames.add(itemDescriptor.getName());
            }
        }

        for (String s : addSpaceNames) {
            ContentValues cv = new ContentValues();
            cv.put(IdGeneratorConstants.ID_SPACE_NAME, s);
            cv.put(IdGeneratorConstants.ID_SPACE, "1");
            db.insert(IdGeneratorConstants.TABLE_NAME, null, cv);
        }
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
        boolean readType = false;

        while (reader.hasNext()) {
            if (readName && readIdName && readProperty && readMultiName && readDataType && readItemType && readType) {
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
                String multiName = reader.nextString();
                table.setMultiColumnName(multiName);
                readMultiName = true;
            } else if ("data-type".equals(name)) {
                String dataType = reader.nextString();
                table.setDataType(DataType.fromValue(dataType));
                readDataType = true;
            } else if ("item-type".equals(name)) {
                String itemType = reader.nextString();
                table.setItemType(itemType);
                readItemType = true;
            } else if ("type".equals(name)) {
                String type = reader.nextString();
                table.setType(type);
                readType = true;
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
                property.setName(mName);
                readName = true;
            } else if ("column-name".equals(name)) {
                String columnName = reader.nextString();
                property.setColumnName(columnName);
                readColumn = true;
            } else if ("data-type".equals(name)) {
                String dataType = reader.nextString();
                property.setDataType(DataType.fromValue(dataType));
                readData = true;
            }
        }

        return property;
    }

    private class RepositoryItemImpl implements RepositoryItem {
        private String repositoryId;
        private String name;
        private Map<String, Object> properties;

        private RepositoryItemImpl(MutableRepositoryItemImpl mri) {
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

        @Override
        public String toString() {
            return repositoryId;
        }
    }

    private class MutableRepositoryItemImpl implements MutableRepositoryItem {
        private String repositoryId;
        private String name;
        private Map<String, Object> properties;

        private MutableRepositoryItemImpl() {
            properties = new HashMap<>();
        }

        private RepositoryItem convertToRepositoryItem() {
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

    public Repository getRepository() {
        return repository;
    }
}
