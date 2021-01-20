package com.heon9u.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class OnDialog extends Activity {

    Button stop;
    View.OnClickListener cancel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_on);

        System.out.println("on Dialog page");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알람 dialog")
                .setMessage("설정하신 알람 시간입니다.")
                .setNeutralButton("알람 해제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDestroy();
                    }
                })
                .setCancelable(true);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, RingtoneService.class);
        serviceIntent.putExtra("state", "alarm off");
        stopService(serviceIntent);
        finish();
    }
}
