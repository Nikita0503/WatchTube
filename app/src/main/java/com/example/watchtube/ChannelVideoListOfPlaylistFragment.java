package com.example.watchtube;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 14.09.2018.
 */

public class ChannelVideoListOfPlaylistFragment extends Fragment implements Contract.View{

    private ChannelVideoListOfPlaylistPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private String mPlaylistId;
    private RecyclerView mRecyclerView;
    private ChannelVideoListOfPlaylistCustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    public void setCredentials(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void setPlaylistId(String playlistId){
        mPlaylistId = playlistId;
        mPresenter = new ChannelVideoListOfPlaylistPresenter(this, mCredential, mPlaylistId, mContext);
        mPresenter.onStart();
        try {
            mAdapter.clearList(); // переделать!!!
        }catch (Exception c){}
         //всеровно экземпляр не пересоздается. Сделать так чтобы не пересоздавало заново, а тупо обращалось к старому
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("VideoList", "fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CreateFragment", "created");
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChannelVideoListOfPlaylistCustomAdapter(this);
        mAdapter.clearList();
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> data){
        mAdapter.addVideosToList(data);
    }

    public void fetchVideoListData(){
        mPresenter.fetchVideoList();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
