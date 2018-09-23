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
import android.widget.Toast;

import com.example.watchtube.UI.ChannelFragment;
import com.example.watchtube.UI.ChannelPlaylistListFragment;
import com.example.watchtube.model.data.ChannelPlaylistPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 13.09.2018.
 */

public class ChannelPlaylistListCustomAdapter extends RecyclerView.Adapter<ChannelPlaylistListCustomAdapter.ViewHolder>{
    private ArrayList<ChannelPlaylistPreviewData> mList;
    private ChannelPlaylistListFragment mFragment;
    private GoogleAccountCredential mCredential;
    private String mPlaylistId;

    public ChannelPlaylistListCustomAdapter(ChannelPlaylistListFragment fragment){
        mList = new ArrayList<ChannelPlaylistPreviewData>();
        mFragment = fragment;
        //Log.d("123", mCredential.getSelectedAccountName());
    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    @NonNull
    @Override
    public ChannelPlaylistListCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_channel_playlist_list_preview_item, parent, false);
        return new ChannelPlaylistListCustomAdapter.ViewHolder(view);
    }

    public GoogleAccountCredential getCredential(){
        return mCredential;
    }

    public String getPlaylistId(){
        return mPlaylistId;
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelPlaylistListCustomAdapter.ViewHolder holder, final int position) {
        holder.textViewPlaylistTitle.setText(mList.get(position).playlistTitle);
        holder.imageView.setImageDrawable(mList.get(position).playlistImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFragment.getContext(), mList.get(position).playlistId, Toast.LENGTH_SHORT).show();
                ChannelVideoListOfPlaylistFragment fragment = new ChannelVideoListOfPlaylistFragment();
                FragmentTransaction trans = mFragment.getFragmentManager()
                        .beginTransaction();
				/*
				 * IMPORTANT: We use the "root frame" defined in
				 * "root_fragment.xml" as the reference to replace fragment
				 */
                trans.replace(R.id.root_frame, fragment);
				/*
				 * IMPORTANT: The following lines allow us to add the fragment
				 * to the stack and return to it later, by pressing back
				 */
                fragment.setContext(mFragment.getContext());
                fragment.setStartAdapter(ChannelPlaylistListCustomAdapter.this);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
                mPlaylistId = mList.get(position).playlistId;


            }
        });
        holder.textViewVideoCount.setText(String.valueOf(mList.get(position).videoCount +  " videos"));
        Log.d("Queue", "= " + position);
        if(position == mList.size() - 3){
            mFragment.fetchPlaylistListData();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
