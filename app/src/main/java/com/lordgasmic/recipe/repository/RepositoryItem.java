package com.lordgasmic.recipe.repository;

import java.util.Map;

/**
 * Created by bruce on 2/11/17.
 */
public interface RepositoryItem {

    String getRepositoryId();
    String getName();

    Object getProperty(String property);
    Map<String, Object> getAllProperties();
}
