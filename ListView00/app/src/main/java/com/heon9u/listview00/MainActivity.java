package com.heon9u.listview00;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = {"이순신", "광개토대왕", "신사임당", "세종대왕", "이성계", "윤봉길",
                        "안중근", "을지문덕", "강감찬", "이황", "이이", "장영실", "에디슨",
                        "슈바이처", "모짜르트", "나폴레옹", "링컨", "공자", "맹자"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        ListView listview = (ListView) findViewById(R.id.ListView);

        listview.setAdapter(adapter);
    }
}