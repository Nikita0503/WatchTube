package com.example.watchtube.UI;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.chibde.visualizer.CircleBarVisualizer;
import com.chibde.visualizer.CircleVisualizer;
import com.example.watchtube.Contract;
import com.example.watchtube.MusicListAdapter;
import com.example.watchtube.MusicListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.VideoDescriptionPresenter;
import com.example.watchtube.model.data.SongData;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nikita on 05.02.2019.
 */

public class MusicListFragment extends Fragment implements Contract.View {

    private boolean mPlay;
    private MusicListPresenter mPresenter;
    private ImageView mImageViewPlay;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MusicListAdapter mAdapter;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private CircleBarVisualizer mCircleVisualizer;
    private TextView mTextViewSongTitle;
    private TextView mTextViewCurrentTime;
    private TextView mTextViewAllTime;
    private final Handler handler = new Handler();
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
        mPlay = false;
        mCircleVisualizer = (CircleBarVisualizer) v.findViewById(R.id.visualizer);
        mTextViewCurrentTime = (TextView) v.findViewById(R.id.textViewCurrent);
        mTextViewAllTime = (TextView) v.findViewById(R.id.textViewAllTime);
        mTextViewSongTitle = (TextView) v.findViewById(R.id.textViewSongTitle);
        mSeekBar = (SeekBar) v.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekChange(v);
            }
        });
        mRecyclerView = v.findViewById(R.id.song_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mImageViewPlay = (ImageView) v.findViewById(R.id.imageViewPlay);
        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlay){
                    mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    if(mMediaPlayer!=null){
                        mMediaPlayer.pause();
                    }
                    mPlay = false;
                }else{
                    mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    if(mMediaPlayer!=null){
                        mMediaPlayer.start();
                        startPlayProgressUpdater();
                    }
                    mPlay = true;
                }
            }
        });
        mAdapter = new MusicListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPresenter.fetchSongList();

        return v;
    }

    private void seekChange(View v){
        mMediaPlayer.seekTo(mSeekBar.getProgress());
        mTextViewCurrentTime.setText(String.valueOf(getDuration(mSeekBar.getProgress())));
    }

    public void startPlayProgressUpdater() {
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

        if (mMediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                    mTextViewCurrentTime.setText(String.valueOf(getDuration(mSeekBar.getProgress())));
                }
            };
            handler.postDelayed(notification,1000);
        }else{
            mMediaPlayer.pause();
        }
    }

    public void playSong(Uri uri, String title){
        mTextViewSongTitle.setText(title);
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mPlay = true;

        mImageViewPlay.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(getContext(), uri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mSeekBar.setMax(mMediaPlayer.getDuration());
            startPlayProgressUpdater();
            mTextViewAllTime.setText(String.valueOf(getDuration(mMediaPlayer.getDuration())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // set custom color to the line.
        mCircleVisualizer.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

// Customize the size of the circle. by default multipliers is 1.

// set the line with for the visualizer between 1-10 default 1.

// Set you media player to the visualizer.
        mCircleVisualizer.setPlayer(mMediaPlayer.getAudioSessionId());
        //mMediaPlayer.start();

    }

    public void addSongs(ArrayList<SongData> list){
        mAdapter.addSongs(list);
    }

    public String getDuration(long oldTime) {
        oldTime=oldTime/1000;
        long hour = oldTime/3600;
        long min = oldTime/60 % 60;
        long sec = oldTime/ 1 % 60;
        if(hour<1){
            if(min<10){
                return String.format("%2d:%02d", min, sec);
            }else{
                return String.format("%02d:%02d", min, sec);
            }
        }else{
            if(hour<10){
                return String.format("%2d:%02d:%02d", hour, min, sec);
            }else{
                return String.format("%02d:%02d:%02d", hour, min, sec);
            }

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
