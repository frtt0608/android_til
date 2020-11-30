package com.example.mywork00;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonListener1(View v) {
        Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_SHORT).show();
    }

    public void buttonListener2(View v) {
        Toast.makeText(getApplicationContext(), "Life gose on", Toast.LENGTH_SHORT).show();
    }
}