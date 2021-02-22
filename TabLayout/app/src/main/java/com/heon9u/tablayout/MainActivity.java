package com.heon9u.tablayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button mediaStore, search, mediaAudio, mediaDownload, mediaFile;
    TextView mediaName, basicCnt, audioCnt, downloadCnt;
    final int REQUEST_CODE_MEDIA = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaName = findViewById(R.id.mediaName);
        mediaStore = findViewById(R.id.mediaStore);
        search = findViewById(R.id.search);

        mediaAudio = findViewById(R.id.mediaAudio);
        mediaDownload = findViewById(R.id.mediaDownload);
        mediaFile = findViewById(R.id.mediaFile);

        basicCnt = findViewById(R.id.basicCnt);
        audioCnt = findViewById(R.id.audioCnt);
        downloadCnt = findViewById(R.id.downloadCnt);
        
        mediaStore.setOnClickListener(v -> {
            boolean flag = true;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag = isExternalStorageReadable();
            }

            if(flag) {
                Intent intent = new Intent(this, MediaStoreList.class);
                startActivityForResult(intent, REQUEST_CODE_MEDIA);
            }
        });

        search.setOnClickListener(v -> {
            getBasicAlarm();
        });

        mediaAudio.setOnClickListener(v -> {
            boolean flag = true;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag = isExternalStorageReadable();
            }

            if(flag) { getMediaAudio(); }
        });

        mediaDownload.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                boolean flag = isExternalStorageReadable();
                if(flag) { getMediaDownload(); }
            }
        });

        mediaFile.setOnClickListener(v -> {
            boolean flag = true;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flag = isExternalStorageReadable();
            }
            if(flag) {
                getFiles();
            }
        });

    }

    private void getBasicAlarm() {
        // content://media/external_primary/audio/media/21?title=Castle&canonical=1
        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALL);
        Cursor cursor = ringtoneManager.getCursor();

        basicCnt.setText(cursor.getCount()+"");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void getMediaDownload() {
        Uri externalUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Downloads._ID,
                MediaStore.Downloads.TITLE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection,
                null, null,
                MediaStore.Downloads.TITLE + " ASC");

        downloadCnt.setText(cursor.getCount()+"");
    }

    public void getMediaAudio() {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection,
                null, null,
                MediaStore.Audio.Media.TITLE + " ASC");

        audioCnt.setText(cursor.getCount()+"");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isExternalStorageReadable() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                showDialogForExternalStorage();
                return false;
            }
        }

        return true;
    }

    public void showDialogForExternalStorage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 요청")
                .setMessage("스마트폰에 저장된 mp3파일에 접근을 허용해주세요.")
                .setNeutralButton("권한 설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                        startActivity(intent);
                    }
                })
                .setCancelable(true);
                builder.show();
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

    public void getFiles() {
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" +
                MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID
        };

        Cursor cursor = getContentResolver().query(uri,
                null,
                selection,null,null);

        Toast.makeText(getApplicationContext(), cursor.getCount()+"", Toast.LENGTH_SHORT).show();
    }
}