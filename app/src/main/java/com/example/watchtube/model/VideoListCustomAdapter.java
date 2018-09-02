package com.example.watchtube.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.R;
import com.example.watchtube.UI.VideoListFragment;
import com.example.watchtube.model.data.VideoPreviewData;

import java.util.ArrayList;

/**
 * Created by Nikita on 27.08.2018.
 */

public class VideoListCustomAdapter extends RecyclerView.Adapter<VideoListCustomAdapter.ViewHolder> {

    private ArrayList<VideoPreviewData> mList;
    private VideoListFragment mFragment;

    public VideoListCustomAdapter(VideoListFragment fragment){
        mList = new ArrayList<VideoPreviewData>();
        mFragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_text_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewVideoTitle.setText(position+": "+mList.get(position).videoTitle);
        holder.imageView.setImageDrawable(mList.get(position).videoImage);
        holder.textViewChannelTitle.setText(mList.get(position).channelTitle);
        holder.imageViewChannel.setImageDrawable(mList.get(position).channelImage);
        holder.textViewPublishedAt.setText(mList.get(position).publishedAt);
        holder.textViewViewCount.setText(String.valueOf(mList.get(position).viewsCount) + " views");
        holder.textViewDuration.setText(mList.get(position).duration);
        Log.d("Queue", "= " + position);
        if(position == mList.size() - 3){
            mFragment.fetchVideoListData();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addVideosToList(ArrayList<VideoPreviewData> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewVideoTitle;
        ImageView imageView;
        TextView textViewChannelTitle;
        ImageView imageViewChannel;
        TextView textViewPublishedAt;
        TextView textViewViewCount;
        TextView textViewDuration;
        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewVideo);
            textViewVideoTitle = (TextView) itemView.findViewById(R.id.textViewVideoTitle);
            textViewChannelTitle = (TextView) itemView.findViewById(R.id.textViewChannelTitle);
            imageViewChannel = (ImageView) itemView.findViewById(R.id.imageViewChannel);
            textViewPublishedAt = (TextView) itemView.findViewById(R.id.textViewPublishedAt);
            textViewViewCount = (TextView) itemView.findViewById(R.id.textViewViewCount);
            textViewDuration = (TextView) itemView.findViewById(R.id.textViewDuration);
        }
    }
}
