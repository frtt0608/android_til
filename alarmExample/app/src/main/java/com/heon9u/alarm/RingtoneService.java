package com.heon9u.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    int startId, volume;
    boolean isRunning, screenOn;
    String ring;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notifi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        setWakeLock();
//        wakeLock.acquire();

        setNotification();
    }

    private void releaseRingtone() {
        if( mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onPage() {
        Intent onIntent = new Intent(getApplicationContext(), OnAlarm.class);
        onIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(onIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, notifi);
        ring = intent.getStringExtra("ring");
        volume = Integer.parseInt(intent.getStringExtra("volume"));

        if(ring == null) {
            Log.d("Service", "ring is null");
            return START_NOT_STICKY;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                setAudioManager();
                setRingtone();
            }
        }).start();

        onPage();

        return START_NOT_STICKY;
    }

    public void setAudioManager() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        am.setStreamVolume(AudioManager.STREAM_ALARM, maxVol*volume/100,
                AudioManager.FLAG_PLAY_SOUND);
    }

    public void setRingtone() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ring));

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//
//            mediaPlayer.setAudioAttributes(audioAttributes);
//        } else {
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//        }

        mediaPlayer.start();
    }

    private void setWakeLock() {
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE),
                "app:myWake_tag");

        screenOn = powerManager.isScreenOn();
    }

    public void setNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            Intent intent = new Intent(this, OnAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("channelId",
                        "channelName", NotificationManager.IMPORTANCE_DEFAULT);
                NM.createNotificationChannel(notificationChannel);
                builder = new Notification.Builder(this, "channelId");
            } else {
                builder = new Notification.Builder(this);
            }

            builder.setContentTitle("알람")
                    .setContentText("Notification + Ringtone + pending(dialog)")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setTicker("Alarm on!")
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.alert_light_frame, "알람 해제하기", pendingIntent);

            notifi = builder.build();
            notifi.flags = Notification.FLAG_AUTO_CANCEL;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("on Destroy() 실행", "서비스 파괴");
        NM.cancelAll();
        releaseRingtone();

//        if(wakeLock != null) {
//            wakeLock.release();
//            wakeLock = null;
//        }
    }

}

