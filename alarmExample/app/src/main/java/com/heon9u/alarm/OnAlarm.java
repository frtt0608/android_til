package com.heon9u.alarm;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class OnAlarm extends AppCompatActivity {

    Button stop;
    TextView time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);

        System.out.println("on Alarm page");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Calendar calendar = Calendar.getInstance();

        time = findViewById(R.id.time);
        time.setText(calendar.getTime().toString());

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> {
            Intent serviceIntent = new Intent(this, RingtoneService.class);
            serviceIntent.putExtra("state", "alarm off");
            startForegroundService(serviceIntent);
            finish();
        });
    }
}
