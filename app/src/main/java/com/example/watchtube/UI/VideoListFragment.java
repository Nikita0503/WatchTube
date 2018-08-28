package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.VideoListPresenter;
import com.example.watchtube.VideoListCustomAdapter;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;


public class VideoListFragment extends Fragment implements Contract.View {

    private VideoListPresenter mPresenter;
    private GoogleAccountCredential mCredential;

    private RecyclerView mRecyclerView;
    private VideoListCustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public void setCredentials(GoogleAccountCredential credential){
        mCredential = credential;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VideoListPresenter(this, mCredential);
        mPresenter.onStart();
        mPresenter.fetchVideoListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideoListCustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public void updateData(ArrayList<VideoPreviewData> data){
        mAdapter.updateData(data);
    }

    public void fetchVideoListData(){
        mPresenter.fetchVideoListData();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }

}
