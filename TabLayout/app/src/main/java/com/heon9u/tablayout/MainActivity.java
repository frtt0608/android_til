package com.heon9u.tablayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button mediaStore;
    TextView mediaName;
    final int REQUEST_CODE_MEDIA = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaStore = findViewById(R.id.mediaStore);
        mediaName = findViewById(R.id.mediaName);

        mediaStore.setOnClickListener(v -> {
            Intent intent = new Intent(this, MediaStoreList.class);
            startActivityForResult(intent, REQUEST_CODE_MEDIA);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MEDIA) {
            if(resultCode == RESULT_OK) {
                Log.d("MainActivity", "get the result");
            }
        }
    }
}