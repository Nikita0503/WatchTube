package com.example.watchtube;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
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
    private ChannelPlaylistListCustomAdapter mChannelPlaylistAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    public void setCredentials(GoogleAccountCredential credential){
        mCredential = credential;
        mPresenter.setCredential(mCredential);
    }

    public void setStartAdapter(ChannelPlaylistListCustomAdapter adapter){
        mChannelPlaylistAdapter = adapter;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void setPlaylistId(String playlistId){
        mPlaylistId = playlistId;
        mPresenter.setPlaylistId(mPlaylistId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChannelVideoListOfPlaylistPresenter(this, mContext);
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
        mAdapter = new ChannelVideoListOfPlaylistCustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        fetchVideoListData();
        return v;
    }

    public void addVideosToList(ArrayList<ChannelVideoPreviewData> data){
        mAdapter.addVideosToList(data);
    }

    public void fetchVideoListData(){
        Log.d("VideoListPlay", "fetch");
        setCredentials(mChannelPlaylistAdapter.getCredential());
        setPlaylistId(mChannelPlaylistAdapter.getPlaylistId());
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
