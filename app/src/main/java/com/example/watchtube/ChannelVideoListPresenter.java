package com.example.watchtube;

import android.util.Log;

import com.example.watchtube.UI.ChannelVideoListFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 06.09.2018.
 */

public class ChannelVideoListPresenter implements Contract.Presenter{

    private ChannelVideoListFragment mFragment;
    private CompositeDisposable mDisposables;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public ChannelVideoListPresenter(ChannelVideoListFragment fragment, GoogleAccountCredential credential, String channelId){
        mFragment = fragment;
        mCredential = credential;
        Log.d("VideoListPresenter", channelId);
        mYouTubeAPIUtils = new YouTubeAPIUtils(mFragment.getContext(), this, mCredential, channelId);
    }

    public void fetchVideoList(){
        Disposable disposable = mYouTubeAPIUtils.getChannelVideoPreviewData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<ChannelVideoPreviewData>>() {
                    @Override
                    public void onSuccess(ArrayList<ChannelVideoPreviewData> channelVideoPreviewData) {
                        mFragment.addVideosToList(channelVideoPreviewData);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }


}
