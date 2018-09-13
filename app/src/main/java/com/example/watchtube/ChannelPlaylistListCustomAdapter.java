package com.example.watchtube;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.UI.ChannelPlaylistListFragment;
import com.example.watchtube.model.data.ChannelPlaylistPreviewData;

import java.util.ArrayList;

/**
 * Created by Nikita on 13.09.2018.
 */

public class ChannelPlaylistListCustomAdapter extends RecyclerView.Adapter<ChannelPlaylistListCustomAdapter.ViewHolder>{
    private ArrayList<ChannelPlaylistPreviewData> mList;
    private ChannelPlaylistListFragment mFragment;

    public ChannelPlaylistListCustomAdapter(ChannelPlaylistListFragment fragment){
        mList = new ArrayList<ChannelPlaylistPreviewData>();
        mFragment = fragment;
    }

    @NonNull
    @Override
    public ChannelPlaylistListCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_channel_playlist_list_preview_item, parent, false);
        return new ChannelPlaylistListCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelPlaylistListCustomAdapter.ViewHolder holder, int position) {
        holder.textViewPlaylistTitle.setText(mList.get(position).playlistTitle);
        holder.imageView.setImageDrawable(mList.get(position).playlistImage);
        holder.textViewVideoCount.setText(String.valueOf(mList.get(position).videoCount +  " videos"));
        Log.d("Queue", "= " + position);
        if(position == mList.size() - 3){
            mFragment.fetchVideoListData();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearList(){
        mList.clear();
    }

    public void addPlaylistsToList(ArrayList<ChannelPlaylistPreviewData> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewPlaylistTitle;
        ImageView imageView;
        TextView textViewVideoCount;
        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewPlaylist);
            textViewPlaylistTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewVideoCount = (TextView) itemView.findViewById(R.id.textViewVideoCount);
        }
    }

}
