package com.heon9u.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class OnAlarm extends Activity {

    Button stop;
    TextView time;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);
        System.out.println("on Alarm page");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Calendar calendar = Calendar.getInstance();
        registerWakeLock();
        wakeLock.acquire();

        time = findViewById(R.id.time);
        time.setText(calendar.getTime().toString());

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> {
            Intent serviceIntent = new Intent(this, RingtoneService.class);
            serviceIntent.putExtra("state", "alarm off");
            stopService(serviceIntent);
            wakeLock.release();
            finish();
        });
    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    private void registerWakeLock() {
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE),
                "app:myWake_tag");
    }
}


//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        // set title
//        alertDialogBuilder.setTitle("Your Title");
//        // set dialog message
//        alertDialogBuilder
//                .setMessage("Your Message")
//                .setCancelable(false)
//                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, just close
//                        // the dialog box and do nothing
//                        stopService(getIntent());
//                        dialog.cancel();
//                        finish();
//                    }
//                });
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        // show it
//        alertDialog.show();
