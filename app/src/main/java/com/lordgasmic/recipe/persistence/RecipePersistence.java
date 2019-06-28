package com.lordgasmic.recipe.persistence;

import android.app.Application;
import android.util.JsonReader;

import com.lordgasmic.recipe.model.Recipe;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import lombok.Data;

@Data
public class RecipePersistence extends Application {

    private List<Recipe> recipes;

    @Override
    public void onCreate(){
        super.onCreate();
        File file = new File(getFilesDir(), "recipe.json");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        try (JsonWriter jw = new JsonWriter(new FileWriter(file))) {
//            jw.beginArray();
//            jw.beginObject();
//            jw.name("name");
//            jw.value("derp");
//            jw.endObject();
//            jw.endArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            JsonReader jr = new JsonReader(new FileReader(file));
            String s = jr.toString();
            System.out.println(s);
            recipes = JsonConvert.deserialize(Recipe.class, jr);
            System.out.println(recipes.size());
            jr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
