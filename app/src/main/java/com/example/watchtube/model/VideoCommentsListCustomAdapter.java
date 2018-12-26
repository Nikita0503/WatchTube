package com.example.watchtube.model;

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

import com.example.watchtube.ChannelVideoListOfPlaylistCustomAdapter;
import com.example.watchtube.R;
import com.example.watchtube.UI.VideoCommentsFragment;
import com.example.watchtube.UI.VideoFragment;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.example.watchtube.model.data.CommentData;

import java.util.ArrayList;

/**
 * Created by Nikita on 26.12.2018.
 */

public class VideoCommentsListCustomAdapter extends RecyclerView.Adapter<VideoCommentsListCustomAdapter.ViewHolder> {

    private ArrayList<CommentData> mList;
    private VideoCommentsFragment mFragment;

    public VideoCommentsListCustomAdapter(VideoCommentsFragment fragment){
        mList = new ArrayList<CommentData>();
        mFragment = fragment;
    }

    @NonNull
    @Override
    public VideoCommentsListCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_comment_item, parent, false);
        return new VideoCommentsListCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCommentsListCustomAdapter.ViewHolder holder, final int position) {
        holder.textViewComment.setText(mList.get(position).comment);
        holder.imageView.setImageDrawable(mList.get(position).image);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoFragment fragment = new VideoFragment();
                fragment.setVideoId(mList.get(position).videoId);
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Log.d("Queue", "= " + position);*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addCommentsToList(ArrayList<CommentData> list){
        mList.addAll(list);
        for(int i = 0; i < list.size(); i++) {
            Log.d("VIDEONAME", list.get(i).comment);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewComment;
        ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewAuthor);
            textViewComment = (TextView) itemView.findViewById(R.id.textViewComment);

        }
    }
}
