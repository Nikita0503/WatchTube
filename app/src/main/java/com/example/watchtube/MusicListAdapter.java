package com.example.watchtube;

import android.content.ContentUris;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.UI.MusicListFragment;
import com.example.watchtube.model.data.SongData;

import java.util.ArrayList;

/**
 * Created by Nikita on 05.02.2019.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.SongViewHolder> {

    private ArrayList<SongData> mSongList;
    private MusicListFragment mFragment;

    public MusicListAdapter(MusicListFragment fragment) {
        mSongList = new ArrayList<SongData>();
        mFragment = fragment;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.music_list_item, parent, false);
        return new MusicListAdapter.SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.imageViewSong.setImageDrawable(mSongList.get(position).songImage);
        holder.textViewSongTitle.setText(mSongList.get(position).songTitle);
        holder.textViewSongDuration.setText(mSongList.get(position).songDuration);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.playSong(ContentUris
                        .withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                mSongList.get(position).songId), mSongList.get(position).songTitle);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public void addSongs(ArrayList<SongData> list){
        mSongList.addAll(list);
        notifyDataSetChanged();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewSong;
        public TextView textViewSongTitle;
        public TextView textViewSongDuration;

        public SongViewHolder(View itemView) {
            super(itemView);
            imageViewSong = (ImageView) itemView.findViewById(R.id.imageViewSongImage);
            textViewSongTitle = (TextView) itemView.findViewById(R.id.textViewSongTitle);
            textViewSongDuration = (TextView) itemView.findViewById(R.id.textViewDuration);
        }
    }
}
