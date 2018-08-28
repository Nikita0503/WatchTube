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

import com.example.watchtube.UI.VideoListFragment;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

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
        holder.textViewVideoTitle.setText(mList.get(position).videoTitle);
        holder.imageView.setImageDrawable(mList.get(position).image);
        Log.d("Queue", "= " + position);
        if(position == mList.size()-1){
            mFragment.fetchVideoListData();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(ArrayList<VideoPreviewData> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewVideoTitle;
        ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            textViewVideoTitle = (TextView) itemView.findViewById(R.id.textView2);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
