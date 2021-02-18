package com.heon9u.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView remainTime, ringtone, volumeInt;
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    PendingIntent pendingIntent;
    Intent my_intent;
    Button btn_start, btn_finish, select, check, mediaStore;
    final int REQUESTCODE_RINGTONE_PICKER = 1000;
    final int recode = 1;
    String ringtoneUri;
    Uri ring;

    SeekBar volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionCheck permissionCheck = new PermissionCheck(this);
        if(!permissionCheck.isCheck()) {
            onAlertDialog();
        }

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.time_picker);

        my_intent = new Intent(this, AlarmReceiver.class);

        // 알람 시작 버튼
        btn_start = findViewById(R.id.btn_start);
        btn_finish = findViewById(R.id.btn_finish);
        select = findViewById(R.id.select);
        check = findViewById(R.id.check);
        volume = findViewById(R.id.volume);
        mediaStore = findViewById(R.id.mediaStore);

        btn_start.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        select.setOnClickListener(this);
        check.setOnClickListener(this);
        mediaStore.setOnClickListener(this);

        setVolumeChanged();
        volumeInt = findViewById(R.id.volumeInt);

        remainTime = findViewById(R.id.remainTime);
        ringtone = findViewById(R.id.ringtone);

        String ring = "content://settings/system/ringtone";
        ringtone.setText("기본음");
        my_intent.putExtra("ring", ring);
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

    public void cancelAlarm() {
        Log.d("MainActivity", "알람 삭제하기");

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent == null) {
            System.out.println("no alarm");
        } else {
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent, 0);
            alarm_manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_start:
                managedAlarm();
                break;

            case R.id.btn_finish:
                cancelAlarm();
                break;

            case R.id.select:
                showRingtoneChooser();
                break;

            case R.id.check:
                checkAlarm();
                break;

            case R.id.mediaStore:
                showMediaStore();
                break;
        }
    }

    public void showMediaStore() {
//        Intent intent = new Intent(this, RingtoneListActivity.class);
//        startActivity(intent);
    }

    public void managedAlarm() {
        Log.d("AlarmManager", "alarm 초기 등록");
        Calendar calendar = Calendar.getInstance();

        my_intent.putExtra("state", "alarm on");
        my_intent.putExtra("volume", volumeInt.getText());
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, recode, my_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarm_manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + 10000,
                    pendingIntent);
        }

        Toast.makeText(MainActivity.this, "10초후 알람", Toast.LENGTH_SHORT).show();
    }

    public void setVolumeChanged() {
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeInt.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void decodingUri(String uri) {
        String uriStr = Uri.decode(uri);
        int s = uriStr.indexOf("=") + 1;
        int e = uriStr.indexOf("&");
        if(s == 0 || e == 0) return;

        String subStr = uriStr.substring(s, e);
        Log.d("subStr", subStr);
    }

    public void onAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알람 해제 창이 안보일 수 있어요!")
                .setMessage("알람이 울릴 때, 화면 위로 알람 해제 창이 보이도록" +
                        "다른 앱 위에 표시 권한'을 허용해주세요.")
                .setNeutralButton("설정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingPermission();
                    }
                });
//                .setCancelable(false);
        builder.show();
    }

    public void settingPermission() {
        Uri uri = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE_RINGTONE_PICKER) {
            if (resultCode == RESULT_OK) {
                // -- 알림음 재생하는 코드 -- //
                ring = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (ring != null) {
                    ringtoneUri = ring.toString();
                    ringtone.setText( ring.toString() );
                    my_intent.putExtra("ring", ringtoneUri);
                    Log.d("MainActivity", ring+"");
                    decodingUri(ringtoneUri);
                } else {
                    ringtoneUri = null;
                    ringtone.setText( "Choose ringtone" );
                }
            }
        }
    }

    public void showRingtoneChooser() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Choose Ringtone!" );
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);

         this.startActivityForResult( intent, REQUESTCODE_RINGTONE_PICKER );
    }

    public void getRingtonePath() {

    }

//    public void setTime() {
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
//    }
}