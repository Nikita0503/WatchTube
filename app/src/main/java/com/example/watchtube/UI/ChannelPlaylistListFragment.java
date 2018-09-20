package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.ChannelPlaylistListCustomAdapter;
import com.example.watchtube.ChannelPlaylistListPresenter;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.RootFragment;
import com.example.watchtube.model.data.ChannelPlaylistPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 13.09.2018.
 */

public class ChannelPlaylistListFragment extends Fragment implements Contract.View {
    private ChannelPlaylistListPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private String mChannelId;
    private RecyclerView mRecyclerView;
    private ChannelPlaylistListCustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RootFragment mRootFragment;

    public void setRootFragment(RootFragment rootFragment){
        mRootFragment = rootFragment;
    }

    public void setCredentials(GoogleAccountCredential credential){
        Log.d("ROOT", "setCredentials()");
        mCredential = credential;
        mPresenter.setCredential(mCredential);
        //mPresenter.setCredential(mCredential);
    }

    public void setChannelId(String channelId){
        Log.d("ROOT", "setChannelId()");
        mChannelId = channelId;
        mPresenter.setChannelId(mChannelId);
        //mPresenter.setChannelId(mChannelId);
//        mAdapter.clearList(); //всеровно экземпляр не пересоздается. Сделать так чтобы не пересоздавало заново, а тупо обращалось к старому
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ROOT", "onCreate()");
        mPresenter = new ChannelPlaylistListPresenter(this);
        mPresenter.onStart();
        setCredentials(mRootFragment.getCredential());
        setChannelId(mRootFragment.getChannelId());
        Log.d("ChannelList", "fragment");
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
//        Log.d("1234", mCredential.getSelectedAccountName());
        mAdapter = new ChannelPlaylistListCustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        fetchPlaylistListData();
        return v;
    }

    public void addPlaylistsToList(ArrayList<ChannelPlaylistPreviewData> data){
        mAdapter.addPlaylistsToList(data);
    }


    public void fetchPlaylistListData(){
        mAdapter.setCredential(mCredential);
        mPresenter.fetchPlaylistList();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
