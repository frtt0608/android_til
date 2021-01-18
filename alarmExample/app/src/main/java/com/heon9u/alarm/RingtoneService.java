package com.heon9u.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RingtoneService extends Service {
    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    Uri ring;
    String state;
    NotificationManagerCompat notificationManagerCompat;
    NotificationCompat.Builder builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service 접근");

        registerWakeLock();

        if (Build.VERSION.SDK_INT >= 26) {

            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Alarm Noti")
                    .setContentText("content Text")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManagerCompat = NotificationManagerCompat.from(this);
        }
    }

    private void registerWakeLock() {
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE),
                "app:myWake_tag");
    }

    private void startRingtone( Uri uriRingtone ) {
        this.releaseRingtone();

        new Thread(() -> {
            try {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uriRingtone);
                if (mediaPlayer == null) {
                    throw new Exception("Can't create player");
                }
                // STREAM_VOICE_CALL, STREAM_SYSTEM, STREAM_RING, STREAM_MUSIC, STREAM_ALARM
                // STREAM_NOTIFICATION, STREAM_DTMF
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.start();
                System.out.println(uriRingtone.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void releaseRingtone() {
        if( mediaPlayer != null ) {
            if( mediaPlayer.isPlaying() ) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, builder.build());
        state = intent.getExtras().getString("state");
        ring = intent.getParcelableExtra("ring");


        System.out.println("Service: " + state);

        assert state != null;

        switch (state) {
            case "alarm on":
                startId = 1;
                Toast.makeText(this, "~~~alarm~~~", Toast.LENGTH_LONG).show();
//                onPage();
//                startRingtone(ring);
                wakeLock.acquire();
                break;
            case "alarm off":
            default:
                startId = 0;
//                onDestroy();
                break;
        }

        if(!this.isRunning && startId == 1) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), ring);
            mediaPlayer.start();
            System.out.println(ring.toString());

            isRunning = true;
            this.startId = 0;
        }
        else if(isRunning && startId == 0) {
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

        Intent onIntent = new Intent(this, OnAlarm.class);
        onIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(onIntent);

        return START_NOT_STICKY;
    }

//    public void onPage() {
//        if(state.equals("alarm on")) {
//            Intent onIntent = new Intent(this, OnAlarm.class);
//            onIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(onIntent);
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("on Destroy() 실행", "서비스 파괴");
        stopForeground(true);
//        releaseRingtone();
//        wakeLock.release();
    }
}
