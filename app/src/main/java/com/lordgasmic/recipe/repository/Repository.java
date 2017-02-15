package com.lordgasmic.recipe.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce on 2/11/17.
 */

public class Repository {

    private Map<String,List<RepositoryItem>> repoMap;

    public Repository(){
        repoMap = new HashMap<>();
    }

    public RepositoryItem getItem(String id, String itemDescriptor) throws RepositoryException {
        ArrayList<RepositoryItem> items = (ArrayList<RepositoryItem>) repoMap.get(itemDescriptor);

        if (items == null) {
            throw new RepositoryException("Item Descriptor not found for: " + itemDescriptor);
        }

        for (RepositoryItem item : items) {
            if (id.equals(item.getRepositoryId())) {
                return item;
            }
        }

        RepositoryItem item = RepositoryLoader.getItem(id, itemDescriptor);
        if (item != null) {
            items.add(item);
            return item;
        }

        return null;
    }

    public void deleteItem(String id, String itemDescriptor) {

    }

    protected void loadRepository(List<String> itemDescriptors) {
        for (String itemDescriptor : itemDescriptors) {
            repoMap.put(itemDescriptor, new ArrayList<RepositoryItem>());
        }
    }
}
