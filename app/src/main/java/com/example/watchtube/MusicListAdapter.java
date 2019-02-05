package com.example.watchtube;

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

    public MusicListAdapter( ) {
        mSongList = new ArrayList<SongData>();
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
        holder.textViewSinger.setText(mSongList.get(position).singer);
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
        public TextView textViewSinger;

        public SongViewHolder(View itemView) {
            super(itemView);
            imageViewSong = (ImageView) itemView.findViewById(R.id.imageViewSongImage);
            textViewSongTitle = (TextView) itemView.findViewById(R.id.textViewSongTitle);
            textViewSinger = (TextView) itemView.findViewById(R.id.textViewSinger);
        }
    }
}
