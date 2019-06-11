package com.example.watchtube;

import com.example.watchtube.UI.PopularVideosListFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PopularVideosListPresenter implements Contract.Presenter {

    private CompositeDisposable mDisposable;
    private YouTubeAPIUtils mYouTubeAPIUtils;
    private GoogleAccountCredential mCredential;
    private PopularVideosListFragment mFragment;

    public PopularVideosListPresenter(PopularVideosListFragment fragment) {
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);
        mFragment = fragment;
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchPopularVideos(){
        Disposable disposable = mYouTubeAPIUtils.getPopularVideos
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<VideoPreviewData>>() {
                    @Override
                    public void onSuccess(ArrayList<VideoPreviewData> list) {
                        mFragment.addVideos(list);
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
