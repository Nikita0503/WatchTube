package com.example.watchtube;

import com.example.watchtube.Contract;
import com.example.watchtube.UI.ChannelVideoListFragment;
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
 * Created by Nikita on 06.09.2018.
 */

public class ChannelVideoListPresenter implements Contract.Presenter {

    private String mChannelId;
    private ChannelVideoListFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public ChannelVideoListPresenter(ChannelVideoListFragment fragment){
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setupChannelId(String channelId){
        mChannelId = channelId;
        prepareYouTubeAPIUtils();
    }

    private void prepareYouTubeAPIUtils(){
        mYouTubeAPIUtils = new YouTubeAPIUtils(mFragment.getContext(), this);
        mYouTubeAPIUtils.setupCredential(mCredential);
        mYouTubeAPIUtils.setupChannelId(mChannelId);
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
        mDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }


}
