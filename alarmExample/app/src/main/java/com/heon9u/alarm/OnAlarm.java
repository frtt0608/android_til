package com.heon9u.alarm;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class OnAlarm extends AppCompatActivity {

    Button stop;
    TextView time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.alarm_on);
        Calendar calendar = Calendar.getInstance();

        time = findViewById(R.id.time);
        time.setText(calendar.getTime().toString());

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> {
            finish();
        });
    }
}
