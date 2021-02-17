package com.heon9u.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmReceiver extends BroadcastReceiver {
    Context context;
    String ring, volume;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        ring = intent.getStringExtra("ring");
        volume = intent.getStringExtra("volume");
        System.out.println("AlarmReceiver ring: " + ring);

        setTime();

        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(context, RingtoneService.class);
        service_intent.putExtra("ring", ring);
        service_intent.putExtra("volume", volume);

        // start the ringtone service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            repeat();
            context.startForegroundService(service_intent);
        }else{
            context.startService(service_intent);
        }
    }

    public void repeat() {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent repeatIntent = new Intent(context, AlarmReceiver.class);

        repeatIntent.putExtra("ring", ring);
        repeatIntent.putExtra("volume", volume);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                1,
                repeatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + 10000,
                    pendingIntent);

        }
    }

    public void setTime() {
        long dt = System.currentTimeMillis();
        Date date = new Date(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 EE요일 hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        System.out.println("Receiver: " + sdf.format(date));
    }
}
