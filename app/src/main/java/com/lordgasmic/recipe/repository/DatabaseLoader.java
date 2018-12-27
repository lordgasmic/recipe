package com.lordgasmic.recipe.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lordgasmic.recipe.constants.IdGeneratorConstants;
import com.lordgasmic.recipe.constants.IngredientConstants;
import com.lordgasmic.recipe.constants.ItemConstants;
import com.lordgasmic.recipe.constants.Quantity;
import com.lordgasmic.recipe.constants.RecipeConstants;
import com.lordgasmic.recipe.constants.UnitOfMesure;
import com.lordgasmic.recipe.model.Ingredient;
import com.lordgasmic.recipe.model.Inventory;
import com.lordgasmic.recipe.model.Item;
import com.lordgasmic.recipe.model.Recipe;
import com.lordgasmic.recipe.model.Steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Main repository loader.  The layer between the Repository and the database.
 * <p>
 * Created by bruce on 2/11/17.
 */
public class DatabaseLoader {

    private final SQLiteDatabase db;
    private final Map<String, Integer> idGenerator;
    private final List<Item> items;
    private final List<Recipe> recipes;
    private final List<Inventory> inventories;

    public DatabaseLoader(final Context context, final Resources resources) {
        final RecipeDbHelper dbHelper = new RecipeDbHelper(context, resources);
        db = dbHelper.getWritableDatabase();

        idGenerator = loadIdGenerator();
        items = loadItems();
        recipes = loadRecipes();
        inventories = loadInventories();
    }

    private Map<String, Integer> loadIdGenerator() {
        final Map<String, Integer> idGenerator = new HashMap<>();

        try (final Cursor c = db.rawQuery("select * from id_generator", null)) {
            while (c.moveToNext()) {
                idGenerator.put(c.getString(c.getColumnIndex(IdGeneratorConstants.ID_SPACE_NAME)), c.getInt(c.getColumnIndex(IdGeneratorConstants.ID_SPACE)));
            }
        }

        return idGenerator;
    }

    private List<Item> loadItems() {
        final List<Item> items = new ArrayList<>();

        try (final Cursor c = db.rawQuery("select * from item", null)) {
            while (c.moveToNext()) {
                final Item item = new Item();
                item.setId(c.getString(c.getColumnIndex(ItemConstants.PROP_ID)));
                item.setName(c.getString(c.getColumnIndex(ItemConstants.PROP_NAME)));
                items.add(item);
            }
        }

        return items;
    }

    private List<Recipe> loadRecipes() {
        final List<Recipe> recipes = new ArrayList<>();

        try (final Cursor c = db.rawQuery("select * from recipe", null)) {
            while (c.moveToNext()) {
                final Recipe recipe = new Recipe();
                recipe.setId(c.getString(c.getColumnIndex(RecipeConstants.PROP_ID)));
                recipe.setName(c.getString(c.getColumnIndex(RecipeConstants.PROP_NAME)));
                recipes.add(recipe);
            }
        }

        for (final Recipe recipe : recipes) {
            final List<Ingredient> ingredients = new ArrayList<>();
            final List<Steps> steps = new ArrayList<>();
            try (final Cursor c = db.rawQuery("select * from recipe_ingredient where recipe_id = '" + recipe.getId() + "'", null)) {
                while (c.moveToNext()) {
                    final Ingredient ingredient = new Ingredient();
                    ingredient.setRecipeId(c.getString(c.getColumnIndex(IngredientConstants.PROP_RECIPE_ID)));
                    ingredient.setSequence(c.getInt(c.getColumnIndex(IngredientConstants.PROP_SEQUENCE)));
                    ingredient.setQuantity(c.getInt(c.getColumnIndex(IngredientConstants.PROP_QUANTITY_WHOLE)));
                    ingredient.setItem(getItem(c));
                    ingredient.setQuantityCode(getQuantity(c));
                    ingredient.setUom(getUnitOfMesure(c));
                    ingredients.add(ingredient);
                }
            }
            try (final Cursor c = db.rawQuery("select * from recipe_steps where recipe_id = '" + recipe.getId() + "'", null)) {
                while (c.moveToNext()) {
                    final Steps step = new Steps();
                    step.setRecipeId(c.getString(c.getColumnIndex(RecipeConstants.PROP_ID)));
                    step.setStep(c.getString(c.getColumnIndex(RecipeConstants.PROP_NAME)));
                    step.setSequence(c.getInt(c.getColumnIndex(RecipeConstants.PROP_NAME)));
                    steps.add(step);

                }
            }

            recipe.setIngredients(ingredients.stream()
                                             .sorted(comparing(Ingredient::getSequence))
                                             .collect(toList()));
            recipe.setSteps(steps.stream()
                                 .sorted(comparing(Steps::getSequence))
                                 .collect(toList()));
        }

        return recipes;
    }

    private List<Inventory> loadInventories() {
        final List<Inventory> inventories = new ArrayList<>();

        try (final Cursor c = db.rawQuery("select * from inventory", null)) {
            while (c.moveToNext()) {
                idGenerator.put(c.getString(c.getColumnIndex(IdGeneratorConstants.ID_SPACE_NAME)), c.getInt(c.getColumnIndex(IdGeneratorConstants.ID_SPACE)));
            }
        }

        return inventories;
    }

    private Item getItem(final Cursor c) {
        final String itemId = c.getString(c.getColumnIndex(IngredientConstants.PROP_ITEM_ID));
        return items.stream()
                    .filter(item -> item.getId()
                                        .equals(itemId))
                    .findFirst()
                    .get();
    }

    private static Quantity getQuantity(final Cursor c) {
        final String code = c.getString(c.getColumnIndex(IngredientConstants.PROP_QUANTITY_CODE));
        return Quantity.valueOf(code);
    }

    private static UnitOfMesure getUnitOfMesure(final Cursor c) {
        final String code = c.getString(c.getColumnIndex(IngredientConstants.PROP_UOM_CODE));
        return UnitOfMesure.valueOf(code);
    }
}
