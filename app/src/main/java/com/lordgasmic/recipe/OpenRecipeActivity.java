package com.lordgasmic.recipe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lordgasmic.recipe.constants.ProjectConstants;
import com.lordgasmic.recipe.repository.Repository;
import com.lordgasmic.recipe.repository.RepositoryApplication;
import com.lordgasmic.recipe.repository.RepositoryException;
import com.lordgasmic.recipe.repository.RepositoryItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OpenRecipeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_recipe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String repositoryId = extras.getString(ProjectConstants.FLAG_OPEN_RECIPE);
            String itemDescriptor = extras.getString(ProjectConstants.FLAG_ITEM_DESCRIPTOR);

            TextView txtId = (TextView) findViewById(R.id.txt_id);
            txtId.setText(repositoryId);

            Repository repository = ((RepositoryApplication) getApplication()).getRepository();

            ListView listView = (ListView) findViewById(R.id.lst_properties);

            try {
                RepositoryItem repositoryItem = repository.getItem(repositoryId, itemDescriptor);

                List<Map<String, String>> data = new ArrayList<>();
                Iterator<Map.Entry<String, Object>> it = repositoryItem.getAllProperties().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    Map<String, String> datum = new HashMap<>();
                    datum.put("property", entry.getKey());
                    datum.put("value", entry.getValue().toString());
                    data.add(datum);
                }

                String[] header = {"property", "value"};
                int[] to = {R.id.txt_property, R.id.txt_value};

                SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.grid_layout, header, to);
                listView.setAdapter(adapter);
            }
            catch (RepositoryException e) {

            }
        }


    }
}
