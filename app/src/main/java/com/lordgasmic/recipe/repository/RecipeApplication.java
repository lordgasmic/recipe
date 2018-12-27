package com.lordgasmic.recipe.repository;

import android.app.Application;

/**
 * Global Application to hold a single RepositoryLoader
 * <p>
 * Created by neo on 4/14/17.
 */
public class RecipeApplication extends Application {

    private static DatabaseLoader databaseLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseLoader = new DatabaseLoader(getApplicationContext(), getResources());
    }

}
