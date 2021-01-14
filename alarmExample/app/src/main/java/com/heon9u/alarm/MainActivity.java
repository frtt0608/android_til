package com.heon9u.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView remainTime;
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;
    long curTime;
    Intent my_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // 알람매니저 설정
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 타임피커 설정
        alarm_timepicker = findViewById(R.id.time_picker);

        // Calendar 객체 생성
        final Calendar calendar = Calendar.getInstance();
        curTime = calendar.getTimeInMillis();
        // 알람리시버 intent 생성
        my_intent = new Intent(this.context, AlarmReceiver.class);

        // 알람 시작 버튼
        Button alarm_on = findViewById(R.id.btn_start);
        remainTime = findViewById(R.id.remainTime);
        remainTime.setText("남은시간 00:00");

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // 시간 가져옴
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // calendar에 시간 셋팅
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                long diff = (calendar.getTimeInMillis() - curTime)/1000;
                if(diff < 0) {
                    int today = calendar.get(Calendar.DAY_OF_WEEK) + 1;
                    if(today == 8) today = 1;

                    calendar.set(Calendar.DAY_OF_WEEK, today);
                    diff += 24*60*60;
                }

                System.out.println(diff);
                int diffHour = (int) diff/(60*60);
                int diffMin = (int) (diff/60)%60;

                remainTime.setText("남은시간은 " +
                        Integer.toString(diffHour) +
                        ":" +
                        Integer.toString(diffMin) + "입니다.");

                Toast.makeText(MainActivity.this,"Alarm 예정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state","alarm on");
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // 알람셋팅
                alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent);
            }
        });

        // 알람 정지 버튼
        Button alarm_off = findViewById(R.id.btn_finish);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public void cancel() {

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent == null) {
            System.out.println("no alarm");
        } else {

            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pendingIntent);
            pendingIntent.cancel();

            my_intent.putExtra("state","alarm off");
            System.out.println("cancel alarm");
            listAlarms();
            // 알람취소
            sendBroadcast(my_intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void listAlarms() {
        System.out.println("alarm list: ");
        for (AlarmManager.AlarmClockInfo aci = alarm_manager.getNextAlarmClock();
             aci != null;
             aci = alarm_manager.getNextAlarmClock()) {
                System.out.println(aci.getTriggerTime());
                remainTime.setText(Long.toString(aci.getTriggerTime()));
        }
    }
}