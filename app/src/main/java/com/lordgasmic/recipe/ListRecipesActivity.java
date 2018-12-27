package com.lordgasmic.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListRecipesActivity extends AbstractActivity {


    public ListRecipesActivity() {
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        final ListView listView = findViewById(R.id.lst_recipe);

//        List<RepositoryItem> repositoryItems = repository.getAllItemsByDescriptor(RecipeConstants.ITEM_DESCRIPTOR);
//        ArrayAdapter<RepositoryItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, repositoryItems);

        // listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                //      RepositoryItem item = (RepositoryItem) parent.getItemAtPosition(position);

                final Intent intent = new Intent(ListRecipesActivity.this, OpenRecipeActivity.class);
//                intent.putExtra(ProjectConstants.FLAG_OPEN_RECIPE, item.getRepositoryId());
//                intent.putExtra(ProjectConstants.FLAG_ITEM_DESCRIPTOR, item.getName());
                startActivity(intent);
            }
        });

        setupMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_main) {
            System.out.println("main menu nav");
            final Intent intent = new Intent(ListRecipesActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_newRecipe) {
            System.out.println("new recipe nav");
            final Intent intent = new Intent(ListRecipesActivity.this, NewRecipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_listRecipes) {
            // do nothing
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
