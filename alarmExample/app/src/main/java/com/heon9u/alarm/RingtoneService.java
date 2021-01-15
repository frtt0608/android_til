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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RingtoneService extends Service {
    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    Uri ring;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE),
                "app:myWake_tag");

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1, notification);
        }

    }

    private void startRingtone( Uri uriRingtone ) {
        this.releaseRingtone();

        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uriRingtone );
            if( mediaPlayer == null ) {
                throw new Exception( "Can't create player" );
            }
            // STREAM_VOICE_CALL, STREAM_SYSTEM, STREAM_RING, STREAM_MUSIC, STREAM_ALARM
            // STREAM_NOTIFICATION, STREAM_DTMF
//            mediaPlayer.setAudioStreamType( AudioManager.STREAM_ALARM );
            mediaPlayer.setAudioStreamType( AudioManager.STREAM_MUSIC );
//            mediaPlayer.setAudioAttributes();
            mediaPlayer.start();
        } catch( Exception e ) {
            e.printStackTrace();
        }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String getState = intent.getExtras().getString("state");
        System.out.println("Service: " + getState);
        ring = intent.getParcelableExtra("ring");

        assert getState != null;
        switch (getState) {
            case "alarm on":
                startId = 1;
//                wakeLock.acquire();

                startRingtone(ring);
//                Intent onIntent = new Intent(this, OnAlarm.class);
//                onIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(onIntent);
                break;
            case "alarm off":
                startId = 0;
                releaseRingtone();
//                wakeLock.release();
                break;
            default:
                startId = 0;
                break;
        }

        // 알람음 재생 X , 알람음 시작 클릭
        if(!this.isRunning && startId == 1) {

//            mediaPlayer = MediaPlayer.create(this, R.raw.dudu);
//            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;
        }

        // 알람음 재생 O , 알람음 종료 버튼 클릭
        else if(this.isRunning && startId == 0) {

//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            mediaPlayer.release();
            this.isRunning = false;
            this.startId = 0;
        }

        // 알람음 재생 X , 알람음 종료 버튼 클릭
        else if(!this.isRunning && startId == 0) {

            this.isRunning = false;
            this.startId = 0;

        }

        // 알람음 재생 O , 알람음 시작 버튼 클릭
        else if(this.isRunning && startId == 1){

            this.isRunning = true;
            this.startId = 1;
        }

        else {
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestory() 실행", "서비스 파괴");
    }
}
