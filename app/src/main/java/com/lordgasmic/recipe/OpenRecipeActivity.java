package com.lordgasmic.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lordgasmic.recipe.constants.ProjectConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenRecipeActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String repositoryId = extras.getString(ProjectConstants.FLAG_OPEN_RECIPE);
            final String itemDescriptor = extras.getString(ProjectConstants.FLAG_ITEM_DESCRIPTOR);

            final TextView txtId = findViewById(R.id.txt_id);
            txtId.setText(repositoryId);

            //   Repository repository = ((RecipeApplication) getApplication()).getRepository();

            final ListView listView = findViewById(R.id.lst_properties);

            try {
                //   RepositoryItem repositoryItem = repository.getItem(repositoryId, itemDescriptor);

                final List<Map<String, String>> data = new ArrayList<>();
//                Iterator<Map.Entry<String, Object>> it = repositoryItem.getAllProperties().entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<String, Object> entry = it.next();
//                    Map<String, String> datum = new HashMap<>();
//                    datum.put("item1", entry.getKey());
//                    datum.put("item2", entry.getValue().toString());
//                    data.add(datum);
//                }

                final String[] header = {"item1", "item2"};
                final int[] to = {R.id.item1, R.id.item2};

                final SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.grid_layout, header, to);
                listView.setAdapter(adapter);
            } catch (final Exception e) {
                final Intent intent = new Intent(OpenRecipeActivity.this, MainActivity.class);
                intent.putExtra(ProjectConstants.FLAG_EXCEPTION, "RepositoryException occured when trying to get " + itemDescriptor + " with id of " + repositoryId);
                startActivity(intent);
            }
        }

    }
}
