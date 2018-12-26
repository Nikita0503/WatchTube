package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.example.watchtube.ChannelVideoListOfPlaylistCustomAdapter;
import com.example.watchtube.ChannelVideoListOfPlaylistPresenter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 14.09.2018.
 */

public class ChannelVideoListOfPlaylistFragment extends Fragment implements Contract.View{

    private String mPlaylistId;
    private ChannelVideoListOfPlaylistPresenter mPresenter;
    private ChannelVideoListOfPlaylistCustomAdapter mAdapter;
    private GoogleAccountCredential mCredential;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    //private ChannelPlaylistListCustomAdapter mChannelPlaylistAdapter;


    public void setCredentials(GoogleAccountCredential credential){
        mCredential = credential;
        Log.d("SUPER", credential.getSelectedAccountName());
    }

    public void setPlaylistId(String playlistId){
        mPlaylistId = playlistId;
        Log.d("SUPER", playlistId);
    }

    /*public void setStartAdapter(ChannelPlaylistListCustomAdapter adapter){
        mChannelPlaylistAdapter = adapter;
    }*/

    /*public void setContext(Context context){
        mContext = context;
    }*/



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChannelVideoListOfPlaylistPresenter(this);
        mPresenter.onStart();
        Log.d("VideoListPlay", "create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("VideoListPlay", "view");
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChannelVideoListOfPlaylistCustomAdapter(this, mCredential);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        fetchVideoListData();
        return v;
    }

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> data){
        mAdapter.addVideosToList(data);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void fetchVideoListData(){
        Log.d("VideoListPlay", "fetch");
        mPresenter.setupCredential(mCredential);
        mPresenter.setupPlaylistId(mPlaylistId);
        mPresenter.fetchVideoList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }
}
