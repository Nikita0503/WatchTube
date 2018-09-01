package com.example.watchtube;

import android.support.v7.widget.RecyclerView;

import com.example.watchtube.UI.VideoListFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.SubscriptionData;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 28.08.2018.
 */

public class VideoListPresenter implements Contract.Presenter {

    private VideoListFragment mFragment;
    private CompositeDisposable mDisposables;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public VideoListPresenter(VideoListFragment fragment, GoogleAccountCredential credential){
        mFragment = fragment;
        mCredential = credential;
        mYouTubeAPIUtils = new YouTubeAPIUtils(mFragment.getContext(), this, mCredential);
    }

    public void fetchVideoList(){
        Disposable disposable = mYouTubeAPIUtils.getVideoPreviewData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<VideoPreviewData>>() {
                    @Override
                    public void onSuccess(ArrayList<VideoPreviewData> subscriptions) {
                        mFragment.addVideosToList(subscriptions);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposables.add(disposable);
        //Disposable;
        //mFragment.updateData(...);
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
