package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lordgasmic.recipe.constants.ProjectConstants;

public class NewRecipeActivity extends Activity {


    public NewRecipeActivity() {
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_input_content);
    }

    public void newRecipe(final View view) {
        final EditText txtName = findViewById(R.id.txtName);
        final EditText txtDescription = findViewById(R.id.txtDescription);

        final Intent intent = new Intent(NewRecipeActivity.this, MainActivity.class);
        intent.putExtra(ProjectConstants.FLAG_NEW_RECIPE_CREATED, txtName.getText() + " has been created");
        startActivity(intent);

    }

}
