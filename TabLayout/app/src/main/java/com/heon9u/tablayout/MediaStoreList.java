package com.heon9u.tablayout;

import android.media.MediaPlayer;
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
                switch (tab.getPosition()) {
                    case 0:
                        MyMediaAdapter myMediaAdapter = new MyMediaAdapter();
                        myMediaAdapter.stopMediaPlayer();
                        break;
                    case 1:
                        RingtoneAdapter ringtoneAdapter = new RingtoneAdapter();
                        ringtoneAdapter.stopMediaPlayer();
                        break;
                }
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
}
