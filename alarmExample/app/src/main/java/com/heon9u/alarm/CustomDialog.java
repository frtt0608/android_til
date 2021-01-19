package com.heon9u.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class CustomDialog extends Dialog {

    Button stop;
    TextView time;
    Context context;
    View.OnClickListener cancel;

    public CustomDialog(@NonNull Context context, View.OnClickListener cancel) {
        super(context);
        this.context = context;
        this.cancel = cancel;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_on);

        System.out.println("on Alarm page");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Calendar calendar = Calendar.getInstance();

        time = findViewById(R.id.time);
        time.setText(calendar.getTime().toString());

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(cancel);
//        stop.setOnClickListener(view -> {
//            Intent serviceIntent = new Intent(context, RingtoneService.class);
//            serviceIntent.putExtra("state", "alarm off");
//            context.startForegroundService(serviceIntent);
//        });
    }
}
