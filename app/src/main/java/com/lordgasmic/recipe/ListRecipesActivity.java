package com.lordgasmic.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ListRecipesActivity extends AbstractActivity {

    public ListRecipesActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        ListView listView = findViewById(R.id.lst_recipe);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListRecipesActivity.this, OpenRecipeActivity.class);
            startActivity(intent);
        });

        setupMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            System.out.println("main menu nav");
            Intent intent = new Intent(ListRecipesActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_newRecipe) {
            System.out.println("new recipe nav");
            Intent intent = new Intent(ListRecipesActivity.this, NewRecipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_listRecipes) {
            // do nothing
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
