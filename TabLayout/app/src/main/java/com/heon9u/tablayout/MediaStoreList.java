package com.heon9u.tablayout;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MediaStoreList extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_list);
        mediaPlayer = null;
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // tab title
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("기본 벨소리");
        arrayList.add("저장된 음악");

        prepareViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mediaPlayer != null)
                    stopMediaPlayer();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void prepareViewPager(ViewPager viewPager) {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mainAdapter.addFragment(new MainFragment(), "기본 벨소리");
        mainAdapter.addFragment(new MyMediaFragment(), "저장된 음악");

        viewPager.setAdapter(mainAdapter);
    }

    public static void startMediaPlayer(Context context, Uri uri) {
        try {
            Log.d("MediaPlayer", uri.toString());
            if(mediaPlayer == null)
                mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
            mediaPlayer.setOnCompletionListener(mp -> mp.release());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMediaPlayer() {
        if(mediaPlayer != null) {
            Log.d("MediaPlayer", "stopMedia");
//            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
