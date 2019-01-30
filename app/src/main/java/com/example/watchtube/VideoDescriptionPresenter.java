package com.example.watchtube;

import android.util.Log;

import com.example.watchtube.UI.VideoDescriptionFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.CommentData;
import com.example.watchtube.model.data.VideoDescription;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 14.01.2019.
 */

public class VideoDescriptionPresenter implements Contract.Presenter {

    private String mVideoId;
    private VideoDescriptionFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public VideoDescriptionPresenter(VideoDescriptionFragment fragment){
        mFragment = fragment;
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
        //Log.d("COMMENTSS", mCredential.getSelectedAccountName());
    }

    public void setupVideoId(String videoId){
        mVideoId = videoId;
        mYouTubeAPIUtils.setupVideoId(mVideoId);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchVideoDescription(){
        Disposable disposable = mYouTubeAPIUtils.getVideoDescription.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<VideoDescription>() {
                    @Override
                    public void onSuccess(VideoDescription videoDescriptionData) {
                        mFragment.setVideoDescription(videoDescriptionData);
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
