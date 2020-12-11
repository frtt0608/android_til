package com.heon9u.englishdictionary;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class ActivityStudy extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordstudy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "sound on");
        menu.add(0, 2, 0, "sound off");
        menu.add(0, 3, 0, "");
        menu.add(0, 4, 0, "");
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1:
                StudyView.soundOk = 1;
                break;
            case 2:
                StudyView.soundOk = 0;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK) {
            finish();
            return false;
        }

        return false;
    }

    public void exitProgram() {
        finish();
    }
}
