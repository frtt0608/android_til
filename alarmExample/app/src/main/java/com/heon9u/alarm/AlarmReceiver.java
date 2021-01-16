package com.heon9u.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        String state = intent.getStringExtra("state");
        System.out.println("Receiver: " + state);

        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(context, RingtoneService.class);
        Uri ring = intent.getParcelableExtra("uri");

        // RingtonePlayinService로 extra string값 보내기
        service_intent.putExtra("state", intent.getExtras().getString("state"));
        service_intent.putExtra("ring", ring);

        // start the ringtone service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            System.out.println("startForegroundService");
            context.startForegroundService(service_intent);
        }else{
            System.out.println("startService");
            context.startService(service_intent);
        }
    }
}