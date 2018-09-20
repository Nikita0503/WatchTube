package com.example.watchtube;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

/**
 * Created by Nikita on 15.09.2018.
 */

public class VideoFragment extends YouTubePlayerFragment {

    private String mVideoId;
    YouTubePlayerFragment videoView;
    YouTubePlayer.OnInitializedListener listener;

    public void setVideoId(String videoId){
        mVideoId = videoId;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = (YouTubePlayerFragment) getActivity().getFragmentManager().findFragmentById(R.id.youtubePlayer);

        listener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(mVideoId);
                youTubePlayer.setShowFullscreenButton(false);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        videoView.initialize("687555550784-tm0hfod9mca6rk0clt5ok4uog6s01kd3.apps.googleusercontent.com", listener);

        return v;
    }
}
