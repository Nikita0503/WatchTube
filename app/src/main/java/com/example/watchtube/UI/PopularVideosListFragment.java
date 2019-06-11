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
import com.example.watchtube.PopularVideosAdapter;
import com.example.watchtube.PopularVideosListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.model.data.VideoPreviewData;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

public class PopularVideosListFragment extends Fragment implements Contract.View {

    private GoogleAccountCredential mCredential;
    private PopularVideosListPresenter mPresenter;
    private PopularVideosAdapter mAdapter;
    private RecyclerView mRecyclerViewVideos;
    private ProgressBar mProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPresenter = new PopularVideosListPresenter(this);
        mPresenter.onStart();

    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
        Log.d("SUPER", credential.getSelectedAccountName());
    }

    public void addVideos(ArrayList<VideoPreviewData> videos){
        mAdapter.addVideos(videos);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popular_videos, container, false);
        mPresenter.setupCredential(mCredential);
        mAdapter = new PopularVideosAdapter(this, mCredential);
        mRecyclerViewVideos = (RecyclerView) v.findViewById(R.id.popular_list_recycler_view);
        mRecyclerViewVideos.setAdapter(mAdapter);
        mRecyclerViewVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.fetchPopularVideos();
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        return v;
    }

}
