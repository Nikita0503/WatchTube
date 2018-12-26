package com.example.watchtube;

import android.util.Log;

import com.example.watchtube.Contract;
import com.example.watchtube.UI.ChannelVideoListOfPlaylistFragment;
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

    public ChannelVideoListOfPlaylistPresenter(ChannelVideoListOfPlaylistFragment fragment){
        mFragment = fragment;
        Log.d("IS", "norm");
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);


    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
    }

    public void setupPlaylistId(String playlistId){
        mPlaylistId = playlistId;
        mYouTubeAPIUtils.setupPlaylistId(mPlaylistId);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
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
    public void onStop() {
        mDisposable.clear();
    }
}
