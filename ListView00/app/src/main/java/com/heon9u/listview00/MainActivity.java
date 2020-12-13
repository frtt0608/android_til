package com.heon9u.listview00;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    ArrayList<String> people;
    String[] explain = {"조선 중기의 명장", "거란의 침략을 막아냄", "훈민정음 창제",
                        "일제강점기의 독립 운동가", "이쁜 공주1", "이쁜 공주2"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        people = new ArrayList<>();
        people.add("이순신");
        people.add("강감찬");
        people.add("세종대왕");
        people.add("윤봉길");
        people.add("조단비");
        people.add("조은비");

        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        setListAdapter(mAdapter);
    }

    public void onListItemClick(ListView lv, View v, int position, long id) {
        String message = people.get(position) + ": " + explain[position];
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}