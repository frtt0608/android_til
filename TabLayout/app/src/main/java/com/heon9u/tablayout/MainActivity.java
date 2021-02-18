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
import android.webkit.MimeTypeMap;
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
                    getDownload();
                    afterQ();
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void afterQ() {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Downloads.MIME_TYPE, "audio/*");
//        values.put(MediaStore.Downloads.DOWNLOAD_URI, "uri");
//
//        ContentResolver contentResolver = getContentResolver();
//        Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA };

        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] selectionArgs = new String[] {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3")
        };

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if(cursor.getCount() == 0) {
            Log.e("afterQ", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                int idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                long id = cursor.getLong(idColumn);
                Uri contentUri = Uri.withAppendedPath(
                        MediaStore.Files.getContentUri("external"),
                        String.valueOf(id));
                System.out.println("Content Uri : " + contentUri);
                System.out.println("Content Uri toString :" + contentUri.toString());
                System.out.println("Content Uri getPath :" + contentUri.getPath());
                String realUri = uri + File.separator + cursor.getString(0);

                System.out.println(realUri);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getDownload() {
        Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] {
                MediaStore.Downloads.TITLE,
                MediaStore.Downloads.DOWNLOAD_URI
        };

        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        if(cursor.getCount() == 0) {
            Log.e("getDownload", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                int pos = cursor.getPosition();
                System.out.println(pos);
            }
        }
    }

    private void getMediaStore() {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection,
                null, null,
                MediaStore.Audio.Media.TITLE + " ASC");

        if (cursor.getCount() == 0) {
            Log.e("getMediaStore", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                String contentUrl = externalUri.toString() + "/" + cursor.getString(0);
                try {
                    InputStream is = getContentResolver().openInputStream(Uri.parse(contentUrl));
                    int data = 0;
                    StringBuilder sb = new StringBuilder();

                    while ((data = is.read()) != -1) {
                        sb.append((char) data);
                        System.out.println(data);
                    }

                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) ||
                                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MEDIA) {
            if(resultCode == RESULT_OK) {
                Log.d("MainActivity", "get the result");
                Ringtone ringtone = (Ringtone) data.getSerializableExtra("Ringtone");
                mediaName.setText(ringtone.getTitle());
            }
        }
    }
}