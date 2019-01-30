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
import com.example.watchtube.UI.ChannelVideoListFragment;
import com.example.watchtube.UI.VideoFragment;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 06.09.2018.
 */

public class ChannelVideoListCustomAdapter extends RecyclerView.Adapter<ChannelVideoListCustomAdapter.ViewHolder> {

    private GoogleAccountCredential mCredential;
    private ArrayList<ChannelVideoPreviewData> mList;
    private ChannelVideoListFragment mFragment;

    public ChannelVideoListCustomAdapter(ChannelVideoListFragment fragment, GoogleAccountCredential credential){
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
                Log.d("TAG112", mCredential.getSelectedAccountName()+" "+mList.get(position).videoId);
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                /*mFragment.chooseVideo(mList.get(position).videoId);
                Log.d("ERROR", mList.get(position).videoTitle);
                ChannelVideoListOfPlaylistFragment fragment = new ChannelVideoListOfPlaylistFragment();
                fragment.setCredentials(mCredential);
                fragment.setPlaylistId(mList.get(position).playlistId);
                fragment.fetchVideoListData();*/

            }
        });
        Log.d("Queue", "= " + position);
        /*if(position == mList.size() - 3){
            mFragment.fetchVideoListData();
        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /*public void clearList(){
        mList.clear();
    }*/

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> list){
        mList.addAll(list);
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
