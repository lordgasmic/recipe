package com.lordgasmic.recipe.repository;

/**
 * Created by atguser on 2/16/17.
 */

public interface MutableRepositoryItem extends RepositoryItem {

    public void setProperty(String id, Object object);
}
