package com.heon9u.alarm;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;
    int startId;
    boolean isRunning;
    Uri ring;
    String state;
    NotificationManager NM;
    Notification.Builder builder;
    Notification notifi;

    Dialog dialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service 접근");
        setNotification();
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
                    .setContentIntent(pendingIntent);

            notifi = builder.build();
            notifi.flags = Notification.FLAG_AUTO_CANCEL;
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

    public void onPage() {
        Intent onIntent = new Intent(this, OnAlarm.class);
        onIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(onIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, notifi);

//        if(intent.getStringExtra("state") == null) {
//            System.out.println("state is null");
//            return START_NOT_STICKY;
//        }

        state = intent.getStringExtra("state");
        ring = intent.getParcelableExtra("ring");

        System.out.println("Service: " + state);

        assert state != null;

        switch (state) {
            case "alarm on":
                startId = 1;
                Toast.makeText(this, "~~~alarm~~~", Toast.LENGTH_LONG).show();
//                onDialog();
//                onPage();
//                startRingtone(ring);
                break;
            case "alarm off":
            default:
                startId = 0;
                stopService(intent);
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

        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("on Destroy() 실행", "서비스 파괴");
        NM.cancelAll();
        releaseRingtone();
    }
}
