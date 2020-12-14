package com.heon9u.baseadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    MyBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyBaseAdapter(this);

        Resources res = getResources();
        final String[] nation = getResources().getStringArray(R.array.nation);
        String[] explain = getResources().getStringArray(R.array.explain);
        String[] population = getResources().getStringArray(R.array.population);
        final String[] capital = getResources().getStringArray(R.array.capital);

        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation00), nation[0], explain[0], population[0]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation01), nation[1], explain[1], population[1]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation02), nation[2], explain[2], population[2]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation03), nation[3], explain[3], population[3]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation04), nation[4], explain[4], population[4]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation05), nation[5], explain[5], population[5]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation06), nation[6], explain[6], population[6]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation07), nation[7], explain[7], population[7]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation08), nation[8], explain[8], population[8]));
        adapter.addItem(new ItemDataBox(res.getDrawable(R.drawable.nation09), nation[9], explain[9], population[9]));

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                ItemDataBox currentItem = (ItemDataBox) adapter.getItem(pos);
                String[] currentData = currentItem.getData();

                Toast.makeText(getApplicationContext(), nation[pos] + ": " + capital[pos], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}