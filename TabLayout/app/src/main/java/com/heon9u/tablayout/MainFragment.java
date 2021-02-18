package com.heon9u.tablayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    AppCompatButton save;
    RecyclerView recyclerView;
    ArrayList<Ringtone> ringtoneList;
    RingtoneAdapter ringtoneAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ringtoneAdapter = new RingtoneAdapter(getContext(), ringtoneList);
        recyclerView.setAdapter(ringtoneAdapter);

        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ringtoneAdapter.selectedItem;
                if(index == -1) {
                    Log.e("MainFragment", "index is -1, you don't choice anything");
                } else {
                    Ringtone ringtone = ringtoneList.get(index);
                    Intent intent = new Intent();
                    intent.putExtra("Ringtone", ringtone);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(ringtoneAdapter.mediaPlayer != null) {
            if(ringtoneAdapter.mediaPlayer.isPlaying()) {
                ringtoneAdapter.mediaPlayer.stop();
                ringtoneAdapter.mediaPlayer.release();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("dd", "stop");
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ringtoneList = new ArrayList<>();
        getBasicAlarm();
    }

    private void getBasicAlarm() {
        // content://media/external_primary/audio/media/21?title=Castle&canonical=1

        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();

        if(cursor.getCount() == 0) {
            Log.e("TAG", "cursor null or cursor is empty");
        } else {
            while(cursor.moveToNext()) {
                int pos = cursor.getPosition();
                String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                Uri uri = ringtoneManager.getRingtoneUri(pos);

                ringtoneList.add(new Ringtone(title, uri.toString()));
            }
        }
    }
}