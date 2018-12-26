package com.example.watchtube;

import android.util.Log;

import com.example.watchtube.UI.VideoCommentsFragment;
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
 * Created by Nikita on 26.12.2018.
 */

public class VideoCommentsPresenter implements Contract.Presenter {

    private String mVideoId;
    private VideoCommentsFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public VideoCommentsPresenter(VideoCommentsFragment fragment){
        mFragment = fragment;
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
        Log.d("COMMENTSS", mCredential.getSelectedAccountName());
    }

    public void setupVideoId(String videoId){
        mVideoId = videoId;
        mYouTubeAPIUtils.setupVideoId(mVideoId);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchVideoCommentsList(){
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
