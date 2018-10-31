package com.lordgasmic.recipe.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lordgasmic.recipe.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by atguser on 3/2/17.
 */
class RecipeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipe.db";

    private final String CREATE_RECIPE;
    private final String CREATE_DIRECTION;
    private final String CREATE_DIRECTION_LIST;
    private final String CREATE_INGREDIENT;
    private final String CREATE_INGREDIENT_LIST;
    private final String CREATE_ITEM;
    private final String CREATE_INVENTORY;
    private final String CREATE_UOM;
    private final String CREATE_ID_GENERATOR;
    private final List<String> INSERT_UOM;
    private final List<String> DROP_ALL;

    public RecipeDbHelper(Context context, Resources resources) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        try {
            CREATE_ID_GENERATOR = readSql(resources.openRawResource(R.raw.create_id_generator));

            CREATE_UOM = readSql(resources.openRawResource(R.raw.create_uom));
            CREATE_ITEM = readSql(resources.openRawResource(R.raw.create_item));
            CREATE_DIRECTION = readSql(resources.openRawResource(R.raw.create_direction));
            CREATE_DIRECTION_LIST = readSql(resources.openRawResource(R.raw.create_direction_list));
            CREATE_INGREDIENT = readSql(resources.openRawResource(R.raw.create_ingredient));
            CREATE_INGREDIENT_LIST = readSql(resources.openRawResource(R.raw.create_ingredient_list));

            CREATE_RECIPE = readSql(resources.openRawResource(R.raw.create_recipe));
            CREATE_INVENTORY = readSql(resources.openRawResource(R.raw.create_inventory));

            INSERT_UOM = readSqlMultiLine(resources.openRawResource(R.raw.insert_uom));

            DROP_ALL = readSqlMultiLine(resources.openRawResource(R.raw.drop_all));
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to read sql files");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ID_GENERATOR);

        db.execSQL(CREATE_UOM);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_DIRECTION);
        db.execSQL(CREATE_DIRECTION_LIST);
        db.execSQL(CREATE_INGREDIENT);
        db.execSQL(CREATE_INGREDIENT_LIST);

        db.execSQL(CREATE_INVENTORY);
        db.execSQL(CREATE_RECIPE);

        execSqlMultiLine(db, INSERT_UOM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        execSqlMultiLine(db, DROP_ALL);
        onCreate(db);
    }

    private String readSql(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }

    private List<String> readSqlMultiLine(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            return br.lines().collect(toList());
        }
    }

    private void execSqlMultiLine(SQLiteDatabase db, List<String> commands) {
        commands.forEach(db::execSQL);
    }

}
