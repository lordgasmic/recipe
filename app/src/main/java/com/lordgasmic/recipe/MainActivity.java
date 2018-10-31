package com.lordgasmic.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lordgasmic.recipe.constants.ProjectConstants;
import com.lordgasmic.recipe.constants.UomConstants;
import com.lordgasmic.recipe.repository.Repository;
import com.lordgasmic.recipe.repository.RepositoryApplication;
import com.lordgasmic.recipe.repository.RepositoryException;
import com.lordgasmic.recipe.repository.RepositoryItem;
import com.lordgasmic.recipe.repository.RepositoryLoader;

import java.io.File;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepositoryApplication repositoryApplication = (RepositoryApplication) getApplication();
                Repository repository = repositoryApplication.getRepository();
                try {
                    RepositoryItem item = repository.getItem("tsp", UomConstants.ITEM_DESCRIPTOR);
                    System.out.println(item.toString());
                }
                catch (RepositoryException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupMenu();
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String newRecipeCreated = extras.getString(ProjectConstants.FLAG_NEW_RECIPE_CREATED);
            if (newRecipeCreated != null) {
                Toast myToast = Toast.makeText(getApplicationContext(), newRecipeCreated, Toast.LENGTH_SHORT);
                myToast.show();
                getIntent().removeExtra(ProjectConstants.FLAG_NEW_RECIPE_CREATED);
            }

            String exception = extras.getString(ProjectConstants.FLAG_EXCEPTION);
            if (exception != null) {
                Toast myToast = Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_SHORT);
                myToast.show();
                getIntent().removeExtra(ProjectConstants.FLAG_EXCEPTION);
            }
        }

        File file = new File(getBaseContext().getFilesDir(),ProjectConstants.INTERNAL_STORAGE_DIR);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_main) {
            // do nothing
        }
         else if (id == R.id.nav_newRecipe) {
            System.out.println("new recipe nav");
            Intent intent = new Intent(MainActivity.this, NewRecipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_listRecipes) {
            System.out.println("list recipes nav");
            Intent intent = new Intent(MainActivity.this, ListRecipesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
