package com.example.watchtube;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.UI.ChannelFragment;
import com.example.watchtube.UI.MainActivity;
import com.example.watchtube.UI.SearchFragment;
import com.example.watchtube.model.data.search.SearchChannelData;
import com.example.watchtube.model.data.search.SearchVideoData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 30.01.2019.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    private SearchFragment mFragment;
    private ArrayList<SearchItemType> mDataSet;
    private GoogleAccountCredential mCredential;
    public SearchAdapter(SearchFragment fragment, GoogleAccountCredential credential) {
        mFragment = fragment;
        mCredential = credential;
        mDataSet = new ArrayList<SearchItemType>();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.get(position) instanceof SearchChannelData) {
            return SearchItemType.CHANNEL;
        }else if (mDataSet.get(position) instanceof SearchVideoData) {
            return SearchItemType.VIDEO;
        }else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SearchItemType.CHANNEL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_channel_item, parent, false);
            return new ChannelViewHolder(view);
        } else if (viewType == SearchItemType.VIDEO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_video_item, parent, false);
            return new VideoViewHolder(view);
        }  else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChannelViewHolder) {
            ((ChannelViewHolder) holder).textViewTitle
                    .setText(((SearchChannelData) mDataSet.get(position)).channelTitle);
            ((ChannelViewHolder) holder).imageViewChannel
                    .setImageDrawable(((SearchChannelData) mDataSet.get(position)).channelImage);
            ((ChannelViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChannelFragment fragment = new ChannelFragment();
                    fragment.setCredential(mCredential);
                    fragment.setChannelId(((SearchChannelData) mDataSet.get(position)).channelId);
                    FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).imageViewVideo
                    .setImageDrawable(((SearchVideoData) mDataSet.get(position)).videoImage);
            ((VideoViewHolder) holder).textViewVideoTitle
                    .setText(((SearchVideoData) mDataSet.get(position)).videoTitle);
            ((VideoViewHolder) holder).textViewChannelTitle
                    .setText(((SearchVideoData) mDataSet.get(position)).channelTitle);
            ((VideoViewHolder) holder).textViewPublishedAt
                    .setText(((SearchVideoData) mDataSet.get(position)).publishedAt);
            ((VideoViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)mFragment.getActivity();
                    activity.setBottom(((SearchVideoData) mDataSet.get(position)).videoId, ((SearchVideoData) mDataSet.get(position)).videoTitle);
                }
            });
        }
    }

    public void addSearchResults(ArrayList<SearchItemType> items){
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ChannelViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewChannel;
        public TextView textViewTitle;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            imageViewChannel = (ImageView) itemView.findViewById(R.id.imageViewChannel);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewVideo;
        public TextView textViewVideoTitle;
        public TextView textViewChannelTitle;
        public TextView textViewPublishedAt;

        public VideoViewHolder(View itemView) {
            super(itemView);
            imageViewVideo = (ImageView) itemView.findViewById(R.id.imageViewVideo);
            textViewVideoTitle = (TextView) itemView.findViewById(R.id.textViewVideoTitle);
            textViewChannelTitle = (TextView) itemView.findViewById(R.id.textViewChannelTitle);
            textViewPublishedAt = (TextView) itemView.findViewById(R.id.textViewPublishedAt);
        }
    }
}
