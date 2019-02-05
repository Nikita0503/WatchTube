package com.example.watchtube;

import android.support.v4.app.Fragment;

import com.example.watchtube.UI.MusicListFragment;
import com.example.watchtube.model.APIUtils.MusicListProvider;
import com.example.watchtube.model.data.SongData;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 05.02.2019.
 */

public class MusicListPresenter implements Contract.Presenter{

    private MusicListProvider mMusicListProvider;
    private CompositeDisposable mDisposable;
    private MusicListFragment mFragment;

    public MusicListPresenter(MusicListFragment fragment) {
        mFragment = fragment;
        mMusicListProvider = new MusicListProvider(fragment.getContext());
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchSongList(){
        ArrayList<SongData> songDataList = new ArrayList<SongData>();
        Disposable disposable = mMusicListProvider.getSongList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SongData>() {
                    @Override
                    public void onNext(SongData songData) {
                        songDataList.add(songData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mFragment.addSongs(songDataList);
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
