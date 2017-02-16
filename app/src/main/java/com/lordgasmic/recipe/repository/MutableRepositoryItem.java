package com.lordgasmic.recipe.repository;

/**
 * Created by nwellman on 2/16/17.
 */

public interface MutableRepositoryItem extends RepositoryItem {

    public void setRepositoryId(String id);
    public void setName(String name);
    public void setProperty(String id, Object object);

}
