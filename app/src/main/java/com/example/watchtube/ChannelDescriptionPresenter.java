package com.example.watchtube;

import com.example.watchtube.Contract;
import com.example.watchtube.UI.ChannelDescriptionFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.ChannelData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 31.08.2018.
 */

public class ChannelDescriptionPresenter implements Contract.Presenter {

    private ChannelDescriptionFragment mFragment;
    private CompositeDisposable mDisposables;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public ChannelDescriptionPresenter(ChannelDescriptionFragment fragment, GoogleAccountCredential credential, String channelId) {
        mCredential = credential;
        mFragment = fragment;
        mYouTubeAPIUtils = new YouTubeAPIUtils(mFragment.getContext(), this, mCredential, channelId);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
    }

    public void fetchChannelData(){
        Disposable disposable = mYouTubeAPIUtils.getChannelData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ChannelData>() {
                    @Override
                    public void onSuccess(ChannelData channelData) {
                        mFragment.showChannelData(channelData);
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposables.add(disposable);
        //mYouTubeAPIUtils...(channelId);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
