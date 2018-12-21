package com.example.watchtube;

import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.CommentData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 21.10.2018.
 */

public class VideoFragmentPresenter implements Contract.Presenter {

    String mVideoId;
    VideoFragment mFragment;
    CompositeDisposable mDisposable;
    GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;


    public VideoFragmentPresenter(VideoFragment videoFragment){
        mFragment = videoFragment;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
        mYouTubeAPIUtils = new YouTubeAPIUtils(mFragment.getContext(), this);
        mYouTubeAPIUtils.setupCredential(mCredential);
        mYouTubeAPIUtils.setupVideoId(mVideoId);
    }

    public void fetchVideoComments(){
        Disposable disposable = mYouTubeAPIUtils.getVideoComments.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<CommentData>>() {
                    @Override
                    public void onSuccess(ArrayList<CommentData> channelVideoPreviewData) {
                        mFragment.addCommentsToList(channelVideoPreviewData);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
