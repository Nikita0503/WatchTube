package com.example.watchtube;

import android.content.Context;
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
import com.example.watchtube.UI.PopularVideosListFragment;
import com.example.watchtube.model.data.VideoPreviewData;
import com.example.watchtube.model.data.search.SearchChannelData;
import com.example.watchtube.model.data.search.SearchVideoData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

public class PopularVideosAdapter extends RecyclerView.Adapter {

    private GoogleAccountCredential mCredential;
    private ArrayList<VideoPreviewData> mVideos;
    private PopularVideosListFragment mFragment;

    public PopularVideosAdapter(PopularVideosListFragment fragment, GoogleAccountCredential credential) {
        mFragment = fragment;
        mCredential = credential;
        mVideos = new ArrayList<VideoPreviewData>();
    }

    public void addVideos(ArrayList<VideoPreviewData> videos){
        mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popular_video_item, viewGroup, false);
        return new PopularVideosAdapter.PopularVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PopularVideoViewHolder holder = (PopularVideoViewHolder) viewHolder;
        holder.textViewTitle.setText(mVideos.get(position).videoTitle);
        holder.textViewDuration.setText(mVideos.get(position).duration);
        holder.imageViewPicture.setImageDrawable(mVideos.get(position).videoImage);
        holder.imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)mFragment.getActivity();
                activity.setBottom(mVideos.get(position).videoId, mVideos.get(position).videoTitle);
            }
        });
        holder.imageViewChannelPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelFragment fragment = new ChannelFragment();
                fragment.setCredential(mCredential);
                fragment.setChannelId((mVideos.get(position)).channelId);
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        holder.imageViewChannelPicture.setImageDrawable(mVideos.get(position).channelImage);
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    @Override
    public int getItemViewType(int i){
        return i;
    }

    public static class PopularVideoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDuration;
        ImageView imageViewPicture;
        ImageView imageViewChannelPicture;

        public PopularVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDuration = (TextView) itemView.findViewById(R.id.textViewDuration);
            imageViewPicture = (ImageView) itemView.findViewById(R.id.imageViewPicture);
            imageViewChannelPicture = (ImageView) itemView.findViewById(R.id.imageViewChannelPicture);

        }
    }
}
