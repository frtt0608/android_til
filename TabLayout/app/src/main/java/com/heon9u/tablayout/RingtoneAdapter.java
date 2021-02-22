package com.heon9u.tablayout;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder> {

    public int selectedItem = -1;
    public MediaPlayer mediaPlayer;
    private Context context;
    private ArrayList<Ringtone> ringtoneList;

    public RingtoneAdapter() { }

    public RingtoneAdapter(Context context, ArrayList<Ringtone> ringtoneList) {
        this.context = context;
        this.ringtoneList = ringtoneList;
    }

    @NonNull
    @Override
    public RingtoneAdapter.RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.ringtone_item, parent, false);
        RingtoneViewHolder ringtoneViewHolder = new RingtoneViewHolder(view);

        return ringtoneViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RingtoneAdapter.RingtoneViewHolder holder, int position) {
        String title = ringtoneList.get(position).getTitle();
        holder.title.setText(title);
        holder.radioButton.setChecked(position == selectedItem);
    }

    @Override
    public int getItemCount() {
        if (ringtoneList == null) return 0;
        return ringtoneList.size();
    }

    public class RingtoneViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        RadioButton radioButton;
        TextView title;

        public RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            radioButton = itemView.findViewById(R.id.radioButton);
            title = itemView.findViewById(R.id.title);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = getAdapterPosition();
                    Uri uri = Uri.parse(ringtoneList.get(selectedItem).getUri());
                    if(MediaStoreList.mediaPlayer != null)
                        MediaStoreList.stopMediaPlayer();
                    MediaStoreList.startMediaPlayer(context, uri);
                    notifyDataSetChanged();
                }
            };
            radioButton.setOnClickListener(clickListener);
            title.setOnClickListener(clickListener);
        }
    }


}
