package com.heon9u.tablayout;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button mediaStore, search, searchDownload;
    TextView mediaName;
    final int REQUEST_CODE_MEDIA = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaStore = findViewById(R.id.mediaStore);
        search = findViewById(R.id.search);
        searchDownload = findViewById(R.id.searchDownload);
        mediaName = findViewById(R.id.mediaName);

        mediaStore.setOnClickListener(v -> {
            Intent intent = new Intent(this, MediaStoreList.class);
            startActivityForResult(intent, REQUEST_CODE_MEDIA);
        });

        search.setOnClickListener(v -> {
            getBasicAlarm();
        });

        searchDownload.setOnClickListener(v -> {
            boolean flag = isExternalStorageReadable();
            if(flag) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    getDownloads();
                } else {
                    getMediaStore();
                }
            }
        });
    }

    private void getBasicAlarm() {
        // content://media/external_primary/audio/media/21?title=Castle&canonical=1

        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALL);
        Cursor cursor = ringtoneManager.getCursor();

        if(cursor.getCount() == 0) {
            Log.e("TAG", "cursor null or cursor is empty");
        } else {
            ArrayList<Ringtone> ringtoneList = new ArrayList<>();

            while(cursor.moveToNext()) {
                int pos = cursor.getPosition();
                String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                Uri uri = ringtoneManager.getRingtoneUri(pos);

                ringtoneList.add(new Ringtone(title, uri.toString()));
            }

            Log.d("BasicAlarm", "success" + ringtoneList.size());
        }
    }

//    /data/media/0/Download/
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getDownloads() {
        Uri uri = MediaStore.Files.getContentUri("internal");
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" +
                MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID
        };

        Cursor cursor = getContentResolver().query(uri,
                null,
                selection,null,null);

        if(cursor.getCount() == 0) {
            Log.e("downloads", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                String title = cursor.getString(0);
                Log.d("downloads", title);
            }
        }
    }

    public void getMediaStore() {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection,
                null, null,
                MediaStore.Audio.Media.TITLE + " ASC");
        Log.d("getMediaStore", cursor.getCount()+"");

        if (cursor.getCount() == 0) {
            Log.e("audio", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                String str = cursor.getString(0);
                String title = cursor.getString(1);
                String contentUrl = externalUri.toString() + "/" + cursor.getString(0);
                Log.d("title", title);
                Log.d("contentUrl", contentUrl);
            }
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) ||
                                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("외부저장소", "접근 true");
            return true;
        }
        Log.d("외부저장소", "접근 false");
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MEDIA) {
            if(resultCode == RESULT_OK) {
                Ringtone ringtone = (Ringtone) data.getSerializableExtra("Ringtone");
                if(ringtone != null) {
                    mediaName.setText(ringtone.getTitle());
                    Log.d("MainActivity", ringtone.getUri());
                }
            }
        }
    }
}