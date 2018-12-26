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
import com.example.watchtube.model.data.ChannelPlaylistPreviewData;
import com.example.watchtube.ChannelPlaylistListCustomAdapter;
import com.example.watchtube.ChannelPlaylistListPresenter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 13.09.2018.
 */

public class ChannelPlaylistListFragment extends Fragment implements Contract.View {
    private boolean mFirstCreating;
    private String mChannelId;
    private ChannelPlaylistListPresenter mPresenter;
    private ChannelPlaylistListCustomAdapter mAdapter;
    private GoogleAccountCredential mCredential;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    //private RootFragment mRootFragment;

    /*public void setRootFragment(RootFragment rootFragment){
        mRootFragment = rootFragment;
    }*/

    public void setCredential(GoogleAccountCredential credential){
        Log.d("ROOT", "setCredentials()");
        mCredential = credential;
        //mPresenter.setCredential(mCredential);
    }

    public void setChannelId(String channelId){
        Log.d("ROOT", "setChannelId()");
        mChannelId = channelId;
        //mPresenter.setChannelId(mChannelId);
//        mAdapter.clearList(); //всеровно экземпляр не пересоздается. Сделать так чтобы не пересоздавало заново, а тупо обращалось к старому
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ROOT", "onCreate()");
        mFirstCreating = true;
        mPresenter = new ChannelPlaylistListPresenter(this);
        mPresenter.onStart();
        //setCredentials(mRootFragment.getCredential());
        //setChannelId(mRootFragment.getChannelId());
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
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
//        Log.d("1234", mCredential.getSelectedAccountName());
        if(mFirstCreating) {
            mPresenter.setCredential(mCredential);
            mPresenter.setChannelId(mChannelId);
            mAdapter = new ChannelPlaylistListCustomAdapter(this);
            mAdapter.setCredential(mCredential);
            mRecyclerView.setAdapter(mAdapter);

            Sprite cubeGrid = new CubeGrid();
            mProgressBar.setIndeterminateDrawable(cubeGrid);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            fetchPlaylistListData();
            mFirstCreating = false;
        }else{
            mRecyclerView.setAdapter(mAdapter);
        }
        return v;
    }

    public void addPlaylistsToList(ArrayList<ChannelPlaylistPreviewData> data){
        mAdapter.addPlaylistsToList(data);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }


    public void fetchPlaylistListData() {
        //mAdapter.setCredential(mCredential);
        mPresenter.fetchPlaylistList();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }


}
