package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.example.watchtube.ChannelVideoListCustomAdapter;
import com.example.watchtube.ChannelVideoListPresenter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 06.09.2018.
 */

public class ChannelVideoListFragment extends Fragment implements Contract.View {
    private String mChannelId;
    private ChannelVideoListPresenter mPresenter;
    private ChannelVideoListCustomAdapter mAdapter;
    private GoogleAccountCredential mCredential;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    //private MainPresenter mMainPresenter;

    /*public void setMainPresenter(MainPresenter mainPresenter){
        mMainPresenter = mainPresenter;
    }*/

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;
       // mAdapter.clearList(); //всеровно экземпляр не пересоздается. Сделать так чтобы не пересоздавало заново, а тупо обращалось к старому
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = new ChannelVideoListPresenter(this);
        mPresenter.onStart();
        setRetainInstance(true);
        Log.d("VideoList", "fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CreateFragment", "created");
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);
        mAdapter = new ChannelVideoListCustomAdapter(this, mCredential);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        fetchVideoListData();
        return v;
    }

    /*public void chooseVideo(String videoId){
        mMainPresenter.chooseVideo(videoId);
    }*/

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> data){
        mAdapter.addVideosToList(data);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void fetchVideoListData(){
        mPresenter.setupCredential(mCredential);
        mPresenter.setupChannelId(mChannelId);
        mPresenter.fetchVideoList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }


}
