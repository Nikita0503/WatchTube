package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.example.watchtube.Contract;
import com.example.watchtube.MusicListAdapter;
import com.example.watchtube.MusicListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.VideoDescriptionPresenter;
import com.example.watchtube.model.data.SongData;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import java.util.ArrayList;

/**
 * Created by Nikita on 05.02.2019.
 */

public class MusicListFragment extends Fragment implements Contract.View {

    private MusicListPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MusicListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MusicListPresenter(this);
        mPresenter.onStart();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("VideoListPlay", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_music_list, container, false);
        mRecyclerView = v.findViewById(R.id.song_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MusicListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPresenter.fetchSongList();
        return v;
    }

    public void addSongs(ArrayList<SongData> list){
        mAdapter.addSongs(list);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
