package com.example.watchtube;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watchtube.model.data.ChannelPlaylistPreviewData;
import com.example.watchtube.model.data.CommentData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 21.10.2018.
 */

public class CommentListCustomAdapter extends RecyclerView.Adapter<CommentListCustomAdapter.ViewHolder> {

    private ArrayList<CommentData> mList;
    private VideoFragment mFragment;
    private GoogleAccountCredential mCredential;

    public CommentListCustomAdapter(VideoFragment fragment){
        mList = new ArrayList<CommentData>();
        mFragment = fragment;
        //Log.d("123", mCredential.getSelectedAccountName());
    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public GoogleAccountCredential getCredential(){
        return mCredential;
    }

    @NonNull
    @Override
    public CommentListCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentListCustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListCustomAdapter.ViewHolder holder, final int position) {
        holder.textViewComment.setText(mList.get(position).comment);
        holder.imageView.setImageDrawable(mList.get(position).image);
        Log.d("Queue", "= " + position);
        if(position == mList.size() - 3){
            mFragment.fetchVideoComments();
        }
    }

    public void addCommentsToList(ArrayList<CommentData> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewComment;
        ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewComment = (TextView) itemView.findViewById(R.id.textViewComment);
        }
    }
}
