package com.heon9u.englishdictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartCsc extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.startstudy);

        findViewById(R.id.main1).setOnClickListener(myClick);
    }

    Button.onClickListener myClick = new Button.onClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.main1:
                    startActivity(new Intent(StartCsc.this, ActivityStudy.class));
                    break;
                case R.id.main2:
                    startActivity(new Intent(StartCsc.this, ActivityStudy2.class));
                    break;
                case R.id.main3:
                    startActivity(new Intent(StartCsc.this, ActivityStudy3.class));
                    break;
                case R.id.main4:
                    startActivity(new Intent(StartCsc.this, ActivityStudy4.class));
                    break;
                case R.id.main5:
                    startActivity(new Intent(StartCsc.this, ActivityStudy5.class));
                    break;
                case R.id.main6:
                    startActivity(new Intent(StartCsc.this, About.class));
                    break;
                case R.id.main7:
                    finish();
                    break;
            }
        }
    };
}
