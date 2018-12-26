package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.watchtube.ChannelVideoListOfPlaylistCustomAdapter;
import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.VideoCommentsPresenter;
import com.example.watchtube.model.VideoCommentsListCustomAdapter;
import com.example.watchtube.model.data.CommentData;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 25.12.2018.
 */

public class VideoCommentsFragment extends Fragment implements Contract.View {

    private String mVideoId;
    private VideoCommentsPresenter mPresenter;
    private VideoCommentsListCustomAdapter mAdapter;
    private GoogleAccountCredential mCredential;
    private EditText mEditTextComment;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VideoCommentsPresenter(this);
        mPresenter.onStart();
        //mPresenter = new VideoFragmentPresenter(this);
        //mPresenter.onStart();
        Log.d("VideoCommentsFragment", "fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("VideoListPlay", "view");
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);
        mEditTextComment = (EditText) v.findViewById(R.id.editTextComment);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideoCommentsListCustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        fetchVideoCommentsList();
        return v;
    }

    public void addCommentsToList(ArrayList<CommentData> data){
        mAdapter.addCommentsToList(data);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void fetchVideoCommentsList(){
        Log.d("VideoListPlay", "fetch");
        mPresenter.setupCredential(mCredential);
        Log.d("COMENTS2", mCredential.getSelectedAccountName());
        mPresenter.setupVideoId(mVideoId);
        mPresenter.fetchVideoCommentsList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
