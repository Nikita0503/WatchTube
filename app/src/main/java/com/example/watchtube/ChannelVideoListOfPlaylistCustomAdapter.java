package com.example.watchtube;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.R;
import com.example.watchtube.UI.ChannelVideoListOfPlaylistFragment;
import com.example.watchtube.UI.VideoFragment;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 14.09.2018.
 */

public class ChannelVideoListOfPlaylistCustomAdapter extends RecyclerView.Adapter<ChannelVideoListOfPlaylistCustomAdapter.ViewHolder>{

    private GoogleAccountCredential mCredential;
    private ArrayList<ChannelVideoPreviewData> mList;
    private ChannelVideoListOfPlaylistFragment mFragment;

    public ChannelVideoListOfPlaylistCustomAdapter(ChannelVideoListOfPlaylistFragment fragment, GoogleAccountCredential credential){
        mList = new ArrayList<ChannelVideoPreviewData>();
        mFragment = fragment;
        mCredential = credential;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_channel_video_list_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textViewVideoTitle.setText(mList.get(position).videoTitle);
        holder.imageView.setImageDrawable(mList.get(position).videoImage);
        holder.textViewPublishedAt.setText(mList.get(position).publishedAt);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoFragment fragment = new VideoFragment();
                fragment.setCredential(mCredential);
                fragment.setVideoId(mList.get(position).videoId);
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Log.d("Queue", "= " + position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
