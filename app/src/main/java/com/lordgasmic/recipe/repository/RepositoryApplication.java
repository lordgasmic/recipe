package com.lordgasmic.recipe.repository;

import android.app.Application;

/**
 * Global Application to hold a single RepositoryLoader
 *
 * Created by neo on 4/14/17.
 */
public class RepositoryApplication extends Application {

    private static RepositoryLoader repositoryLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        repositoryLoader = new RepositoryLoader(getApplicationContext(), getResources());
    }

    public Repository getRepository() {
        return repositoryLoader.getRepository();
    }
}
