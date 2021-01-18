package com.heon9u.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView remainTime, ringtone;
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Context context;
    PendingIntent pendingIntent;
    long curTime;
    Intent my_intent;
    Button select, alarm_off, check;
    final int REQUESTCODE_RINGTONE_PICKER = 1000;
    String ringtoneUri;
    MediaPlayer mediaPlayer;
    Uri ring;
    int recode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        recode = 0;
        this.context = this;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.time_picker);

        Calendar calendar = Calendar.getInstance();
        my_intent = new Intent(this.context, AlarmReceiver.class);

        // 알람 시작 버튼
        Button alarm_on = findViewById(R.id.btn_start);
        alarm_off = findViewById(R.id.btn_finish);
        alarm_off.setOnClickListener(this);
        select = findViewById(R.id.select);
        select.setOnClickListener(this);
        check = findViewById(R.id.check);
        check.setOnClickListener(this);

        remainTime = findViewById(R.id.remainTime);
        ringtone = findViewById(R.id.ringtone);

        Uri tempRing = Uri.parse("content://media/external_primary/audio/media/60?title=Variations&canonical=1");
        ringtone.setText( tempRing.toString() );
        my_intent.putExtra("uri", tempRing);

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // 시간 가져옴
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

//                // calendar에 시간 셋팅
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);
//                long cur = calendar.getTimeInMillis();
//
//                long diff = (cur - curTime)/1000;
//                if(diff < 0) {
//                    cur += 24*60*60*1000;
//                    calendar.setTimeInMillis(cur);
//                }
//
//                System.out.println("남은시간은: " + (cur-curTime)/1000 + "초 입니다.");
//                int diffHour = (int) diff/(60*60);
//                int diffMin = (int) (diff/60)%60;
//
//                remainTime.setText("남은시간은 " +
//                        Integer.toString(diffHour) +
//                        ":" +
//                        Integer.toString(diffMin) + "입니다.");

                Toast.makeText(MainActivity.this,"Alarm 예정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state","alarm on");
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // 알람셋팅
                System.out.println("알람 등록");
                alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 6500,
                        pendingIntent);
            }
        });

    }

    public void checkAlarm() {
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent == null) {
            System.out.println("현재 알람은 없습니다.");
        } else {
            System.out.println("등록된 알람이 있습니다.");
        }
    }

    public void cancel() {

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent == null) {
            System.out.println("no alarm");

        } else {
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarm_manager.cancel(pendingIntent);
            pendingIntent.cancel();

            my_intent.putExtra("state","alarm off");
            System.out.println("cancel alarm");
            sendBroadcast(my_intent);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_finish:
                cancel();
                releaseRingtone();
                break;

            case R.id.select:
                showRingtoneChooser();
                break;

            case R.id.check:
                checkAlarm();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "State: on Resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "State: on Destroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "State: on Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "State: on Stop");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUESTCODE_RINGTONE_PICKER ) {
            if (resultCode == RESULT_OK) {
                // -- 알림음 재생하는 코드 -- //
                ring = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (ring != null) {
                    ringtoneUri = ring.toString();
                    ringtone.setText( ring.toString() );
                    my_intent.putExtra("uri", ring);
                } else {
                    ringtoneUri = null;
                    ringtone.setText( "Choose ringtone" );
                }
            }
        } else if(requestCode == 1) {
            if (!Settings.canDrawOverlays(this)) {
                // 허용 못했을 경우 처리
            } else {
                startService(new Intent(MainActivity.this, RingtoneService.class));
            }
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            } else {
                startService(new Intent(MainActivity.this, RingtoneService.class));
            }
        } else {
            startService(new Intent(MainActivity.this, RingtoneService.class));
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

    public void showRingtoneChooser() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose Ringtone!" );
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);

        //-- 알림 선택창이 떴을 때, 기본값으로 선택되어질 ringtone설정
         if( ringtoneUri != null && ringtoneUri.isEmpty() ) {
             intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                     Uri.parse(ringtoneUri));
         }

         this.startActivityForResult( intent, REQUESTCODE_RINGTONE_PICKER );
    }
}