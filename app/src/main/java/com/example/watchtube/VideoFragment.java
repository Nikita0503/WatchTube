package com.example.watchtube;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.model.data.CommentData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

/**
 * Created by Nikita on 15.09.2018.
 */

public class VideoFragment extends Fragment {

    private String mVideoId;
    YouTubePlayerSupportFragment videoView;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    YouTubePlayer mPlayer;
    CommentListCustomAdapter mAdapter;
    private VideoFragmentPresenter mPresenter;

    public void setVideoId(String videoId){
        mVideoId = videoId;
        mPlayer.loadVideo(videoId);
        mAdapter = new CommentListCustomAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("ERROR", "1 "+videoId);
        mPresenter.setVideoId(mVideoId);
        fetchVideoComments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VideoFragmentPresenter(this);
        mPresenter.onStart();
        Log.d("VideoFragment", "fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setHasFixedSize(true);
        videoView = (YouTubePlayerSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.youtube_fragment);

        if (videoView == null) {
            Log.d("ERROR", "null");
        }else{
            Log.d("ERROR", "not null");
        }
        videoView.initialize("687555550784-tm0hfod9mca6rk0clt5ok4uog6s01kd3.apps.googleusercontent.com", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        mPlayer = youTubePlayer;
                        mPlayer.setShowFullscreenButton(false);
                        Log.d("ERROR", "2");
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("ERROR", "123");
                    }
                }
        );

        return v;
    }

    public void fetchVideoComments(){
        mPresenter.fetchVideoComments();
    }

    public void addCommentsToList(ArrayList<CommentData> data){
        mAdapter.addCommentsToList(data);
    }
}
