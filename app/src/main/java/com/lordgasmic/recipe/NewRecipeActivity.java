package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lordgasmic.recipe.constants.ProjectConstants;

public class NewRecipeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_input_content);
    }

    public void newRecipe(View view) {
        EditText txtName = (EditText) findViewById(R.id.txtName);
        EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

        Intent intent = new Intent(NewRecipeActivity.this, MainActivity.class);
        intent.putExtra(ProjectConstants.FLAG_NEW_RECIPE_CREATED, txtName.getText() + " has been created");
        startActivity(intent);
    }

}
