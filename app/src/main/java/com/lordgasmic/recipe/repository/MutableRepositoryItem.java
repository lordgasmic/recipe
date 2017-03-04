package com.lordgasmic.recipe.repository;

/**
 * Created by nwellman on 2/16/17.
 */

public interface MutableRepositoryItem extends RepositoryItem {

    void setRepositoryId(String id);
    void setName(String name);
    void setProperty(String id, Object object);

}
