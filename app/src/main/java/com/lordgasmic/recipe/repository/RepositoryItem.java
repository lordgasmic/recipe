package com.lordgasmic.recipe.repository;

/**
 * Created by bruce on 2/11/17.
 */

public interface RepositoryItem {

    String getRepositoryId();
    String getName();

    Object getProperty(String property);
}
