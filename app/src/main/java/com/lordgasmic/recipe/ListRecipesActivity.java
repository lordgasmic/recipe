package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lordgasmic.recipe.constants.ProjectConstants;
import com.lordgasmic.recipe.repository.Repository;
import com.lordgasmic.recipe.repository.RepositoryApplication;
import com.lordgasmic.recipe.repository.RepositoryItem;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListRecipesActivity extends AbstractActivity {

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

                Intent intent = new Intent(ListRecipesActivity.this, OpenRecipeActivity.class);
                intent.putExtra(ProjectConstants.FLAG_OPEN_RECIPE, item.getRepositoryId());
                intent.putExtra(ProjectConstants.FLAG_ITEM_DESCRIPTOR, item.getName());
                startActivity(intent);
            }
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

        if(id == R.id.nav_main) {
            System.out.println("main menu nav");
            Intent intent = new Intent(ListRecipesActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_newRecipe) {
            System.out.println("new recipe nav");
            Intent intent = new Intent(ListRecipesActivity.this, NewRecipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_listRecipes) {
            // do nothing
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
