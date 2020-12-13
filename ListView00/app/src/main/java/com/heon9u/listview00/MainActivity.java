package com.heon9u.listview00;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.R.layout.simple_list_item_1;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] listExplain = getResources().getStringArray(R.array.achievement);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.people, simple_list_item_1);

        ListView listview = (ListView) findViewById(R.id.ListView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText() + listExplain[i], Toast.LENGTH_LONG).show();
            }
        });
    }
}