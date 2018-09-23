package com.example.watchtube;

import android.content.Context;
import android.util.Log;

import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 14.09.2018.
 */

public class ChannelVideoListOfPlaylistPresenter implements Contract.Presenter {

    private String mPlaylistId;
    private ChannelVideoListOfPlaylistFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public ChannelVideoListOfPlaylistPresenter(ChannelVideoListOfPlaylistFragment fragment, Context context){
        mFragment = fragment;
        Log.d("IS", "norm");
        mYouTubeAPIUtils = new YouTubeAPIUtils(context, this);


    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
    }

    public void setPlaylistId(String playlistId){
        mPlaylistId = playlistId;
        mYouTubeAPIUtils.setupPlaylistId(mPlaylistId);
    }

    public void fetchVideoList(){
        Disposable disposable = mYouTubeAPIUtils.getChannelVideoPreviewOfPlaylistData.subscribeOn(Schedulers.io())
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
        mDisposable.add(disposable);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
