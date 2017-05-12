package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lordgasmic.recipe.constants.ProjectConstants;
import com.lordgasmic.recipe.repository.Repository;
import com.lordgasmic.recipe.repository.RepositoryApplication;
import com.lordgasmic.recipe.repository.RepositoryItem;

import java.util.List;

public class ListRecipesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        Repository repository = ((RepositoryApplication) getApplication()).getRepository();

        ListView listView = (ListView) findViewById(R.id.lst_recipe);

        List<RepositoryItem> repositoryItems = repository.getAllItemsByDescriptor("uom");
        ArrayAdapter<RepositoryItem> adapter = new ArrayAdapter<RepositoryItem>(this, android.R.layout.simple_list_item_1, repositoryItems);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                RepositoryItem item = (RepositoryItem) parent.getItemAtPosition(position);

                System.out.println("open recipe nav");
                Intent intent = new Intent(ListRecipesActivity.this, OpenRecipeActivity.class);
                intent.putExtra(ProjectConstants.FLAG_OPEN_RECIPE, item.getRepositoryId());
                intent.putExtra(ProjectConstants.FLAG_ITEM_DESCRIPTOR, (String) item.getProperty("itemDescriptor"));
                startActivity(intent);
            }
        });
    }
}
