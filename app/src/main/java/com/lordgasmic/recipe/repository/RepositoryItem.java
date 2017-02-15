package com.lordgasmic.recipe.repository;

/**
 * Created by bruce on 2/11/17.
 */

public interface RepositoryItem {

    public String getRepositoryId();
    public String getName();

    public Object getProperty(String property);
}
