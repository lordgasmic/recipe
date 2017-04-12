package com.lordgasmic.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.lordgasmic.recipe.constants.ProjectConstants;

public class NewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_input_content);
    }

    public void newRecipe(View view) {
        EditText txtName = (EditText) findViewById(R.id.txtName);
        EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ProjectConstants.FLAG_NEW_RECIPE_CREATED, txtName.getText() + " has been created");
        startActivity(intent);
    }

}
