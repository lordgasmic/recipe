package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lordgasmic.recipe.constants.ProjectConstants;
import com.lordgasmic.recipe.repository.Repository;
import com.lordgasmic.recipe.repository.RepositoryApplication;

public class NewRecipeActivity extends Activity {

    private Repository repository;

    public NewRecipeActivity() {
        repository = ((RepositoryApplication) getApplication()).getRepository();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_input_content);
    }

    public void newRecipe(View view) {
        EditText txtName = findViewById(R.id.txtName);
        EditText txtDescription = findViewById(R.id.txtDescription);

        Intent intent = new Intent(NewRecipeActivity.this, MainActivity.class);
        intent.putExtra(ProjectConstants.FLAG_NEW_RECIPE_CREATED, txtName.getText() + " has been created");
        startActivity(intent);

        repository.
    }

}
