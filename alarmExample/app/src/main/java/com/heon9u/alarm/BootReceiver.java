package com.heon9u.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BootReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)) {
            Intent alarmService = new Intent(context, RingtoneService.class);
            context.startForegroundService(alarmService);
        }
    }
}
