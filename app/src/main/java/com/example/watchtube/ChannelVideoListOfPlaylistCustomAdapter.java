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

import com.example.watchtube.model.data.ChannelVideoPreviewData;

import java.util.ArrayList;

/**
 * Created by Nikita on 14.09.2018.
 */

public class ChannelVideoListOfPlaylistCustomAdapter extends RecyclerView.Adapter<ChannelVideoListOfPlaylistCustomAdapter.ViewHolder>{

    private ArrayList<ChannelVideoPreviewData> mList;
    private ChannelVideoListOfPlaylistFragment mFragment;

    public ChannelVideoListOfPlaylistCustomAdapter(ChannelVideoListOfPlaylistFragment fragment){
        mList = new ArrayList<ChannelVideoPreviewData>();
        mFragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_channel_video_list_preview_item, parent, false);
        return new ChannelVideoListOfPlaylistCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewVideoTitle.setText(mList.get(position).videoTitle);
        holder.imageView.setImageDrawable(mList.get(position).videoImage);
        holder.textViewPublishedAt.setText(mList.get(position).publishedAt);
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

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> list){
        mList.addAll(list);
        for(int i = 0; i < list.size(); i++) {
            Log.d("VIDEONAME", list.get(i).videoTitle);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewVideoTitle;
        ImageView imageView;
        TextView textViewPublishedAt;
        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewPlaylist);
            textViewVideoTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewPublishedAt = (TextView) itemView.findViewById(R.id.textViewPublishedAt);
        }
    }

}
