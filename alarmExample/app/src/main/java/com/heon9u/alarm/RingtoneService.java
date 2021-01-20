package com.heon9u.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
    Uri ring;
    String state;
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
        System.out.println("Service on Create");
//        setWakeLock();
//        wakeLock.acquire();

        setNotification();
    }

    private void releaseRingtone() {
        if( mediaPlayer != null ) {
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
        state = intent.getStringExtra("state");
        ring = intent.getParcelableExtra("ring");
        volume = Integer.parseInt(intent.getStringExtra("volume"));

        System.out.println("Service: " + state);

        assert state != null;

        switch (state) {
            case "alarm on":
                startId = 1;
                Toast.makeText(this, "~~~alarm~~~", Toast.LENGTH_LONG).show();
                onPage();
                break;
            case "alarm off":
            default:
                startId = 0;
                stopService(intent);
                break;
        }

        if(!this.isRunning && startId == 1) {
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
            Log.d("volume", "최대 볼륨: " + maxVol);

            am.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
                    AudioManager.FLAG_PLAY_SOUND);


            mediaPlayer = MediaPlayer.create(getApplicationContext(), ring);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            int curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.d("volume", "현재 볼륨: " + curVol);
            mediaPlayer.start();

            System.out.println(ring.toString());

            isRunning = true;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 0) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            isRunning = false;
            this.startId = 0;
        }
        else if(!this.isRunning && startId == 0) {
            this.isRunning = false;
            this.startId = 0;
        }
        else if(this.isRunning && startId == 1) {
            this.isRunning = true;
            this.startId = 1;
        }

        return START_NOT_STICKY;
    }

    private void setWakeLock() {
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE),
                "app:myWake_tag");

        screenOn = powerManager.isScreenOn();
        System.out.println("현재 화면 상태: " + screenOn);
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
