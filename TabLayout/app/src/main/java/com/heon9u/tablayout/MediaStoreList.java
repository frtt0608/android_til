package com.heon9u.tablayout;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_list);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // tab title
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("벨소리");
        arrayList.add("내 음악");

        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        MainFragment fragment = new MainFragment();
        
        for(int i=0; i<arrayList.size(); i++) {
            // bundle init
            Bundle bundle = new Bundle();
            bundle.putString("title", arrayList.get(i));
            fragment.setArguments(bundle);
            mainAdapter.addFragment(fragment, arrayList.get(i));
            fragment = new MainFragment();
        }

        viewPager.setAdapter(mainAdapter);
    }
}
