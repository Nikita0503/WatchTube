package com.example.watchtube;

import com.example.watchtube.UI.SearchFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.data.VideoDescription;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 30.01.2019.
 */

public class SearchPresenter implements Contract.Presenter {

    private SearchFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;

    public SearchPresenter(SearchFragment fragment) {
        mFragment = fragment;
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
    }

    public void fetchSearchResults(String request){
        mYouTubeAPIUtils.setupRequest(request);
        Disposable disposable = mYouTubeAPIUtils.getSearchResults.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<SearchItemType>>() {
                    @Override
                    public void onSuccess(ArrayList<SearchItemType> results) {
                        mFragment.addSearchResults(results);
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
