package com.lordgasmic.recipe.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lordgasmic.recipe.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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


    ;

    private static final String INSERT_UOM = "insert into uom values (\"tbl\")";


    public RecipeDbHelper(Context context, Resources resources) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        try {
            CREATE_ID_GENERATOR = readSql(resources.openRawResource(R.raw.id_generator));
            CREATE_UOM = readSql(resources.openRawResource(R.raw.uom));
            CREATE_ITEM = readSql(resources.openRawResource(R.raw.item));
            CREATE_DIRECTION = readSql(resources.openRawResource(R.raw.direction));
            CREATE_DIRECTION_LIST = readSql(resources.openRawResource(R.raw.direction_list));
            CREATE_INGREDIENT = readSql(resources.openRawResource(R.raw.ingredient));
            CREATE_INGREDIENT_LIST = readSql(resources.openRawResource(R.raw.ingredient_list));

            CREATE_RECIPE = readSql(resources.openRawResource(R.raw.recipe));
            CREATE_INVENTORY = readSql(resources.openRawResource(R.raw.inventory));
        }
        catch (IOException e) {
            throw new IllegalStateException("Unable to read sql files");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_UOM);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_INVENTORY);
        db.execSQL(CREATE_RECIPE);
        db.execSQL(CREATE_DIRECTION);
        db.execSQL(CREATE_INGREDIENT);

        db.execSQL(INSERT_UOM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
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
}
