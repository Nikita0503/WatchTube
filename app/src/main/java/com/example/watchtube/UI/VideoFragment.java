package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.R;
import com.example.watchtube.UI.TwoFragment;
import com.example.watchtube.ViewPagerAdapter;
import com.example.watchtube.model.data.CommentData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 15.09.2018.
 */

public class VideoFragment extends Fragment {

    private String mVideoId;
    private GoogleAccountCredential mCredential;
    private YouTubePlayerSupportFragment mVideoView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    //RecyclerView mRecyclerView;
    //private RecyclerView.LayoutManager mLayoutManager;

    //CommentListCustomAdapter mAdapter;
    //private VideoFragmentPresenter mPresenter;

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;

        //mAdapter = new CommentListCustomAdapter(this);
        //mRecyclerView.setAdapter(mAdapter);
        Log.d("ERROR", "1 "+videoId);
        //mPresenter.setVideoId(mVideoId);
        //fetchVideoComments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter = new VideoFragmentPresenter(this);
        //mPresenter.onStart();
        Log.d("VideoFragment", "fragment");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putString(STATE_USER, mUser);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        mTabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        VideoCommentsFragment fragment2 = new VideoCommentsFragment();
        fragment2.setCredential(mCredential);
        fragment2.setVideoId(mVideoId);
        VideoDescriptionFragment fragment = new VideoDescriptionFragment();
        fragment.setCredential(mCredential);
        fragment.setVideoId(mVideoId);
        adapter.addFragment(fragment, "Info");
        adapter.addFragment(fragment2, "Comments");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //mLayoutManager = new LinearLayoutManager(getContext());
        //mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setHasFixedSize(true);
        mVideoView = (YouTubePlayerSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.youtube_fragment);
        mVideoView.initialize("687555550784-tm0hfod9mca6rk0clt5ok4uog6s01kd3.apps.googleusercontent.com", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        //youTubePlayer.setShowFullscreenButton(false);
                        youTubePlayer.setFullscreenControlFlags(0);
                        youTubePlayer.loadVideo(mVideoId);
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

    /*public void fetchVideoComments(){
        mPresenter.fetchVideoComments();
    }

    public void addCommentsToList(ArrayList<CommentData> data){
        mAdapter.addCommentsToList(data);
    }*/
}
