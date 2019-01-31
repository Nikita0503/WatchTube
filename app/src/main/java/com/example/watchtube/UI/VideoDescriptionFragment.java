package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.VideoDescriptionPresenter;
import com.example.watchtube.model.data.VideoDescription;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


/**
 * Created by Nikita on 14.01.2019.
 */

public class VideoDescriptionFragment extends Fragment implements Contract.View{

    private String mVideoId;
    private VideoDescriptionPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private TextView mTextViewLikes;
    private TextView mTextViewDislikes;
    private TextView mTextViewVideoTitle;
    private TextView mTextViewAuthorName;
    private TextView mTextViewDescription;
    private TextView mTextViewPublishedAt;
    private ImageView mImageViewAuthor;
    private ImageView mImageViewLike;
    private ImageView mImageViewDislike;
    private SeekBar mSeekBar;

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
        Log.d("VideoListPlay", "setCredential");
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
        Log.d("VideoListPlay", "setVideoId");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VideoDescriptionPresenter(this);
        mPresenter.onStart();
        Log.d("VideoListPlay", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("VideoListPlay", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_video_description, container, false);
        mSeekBar = (SeekBar) v.findViewById(R.id.seekBar);
        mTextViewLikes = (TextView) v.findViewById(R.id.textViewLike);
        mTextViewDislikes = (TextView) v.findViewById(R.id.textViewDislike);
        mTextViewVideoTitle = (TextView) v.findViewById(R.id.textViewVideoTitle);
        mTextViewAuthorName = (TextView) v.findViewById(R.id.textViewAuthorName);
        mTextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        mTextViewPublishedAt = (TextView) v.findViewById(R.id.textViewPublishedAt);
        mImageViewAuthor = (ImageView) v.findViewById(R.id.imageViewAuthor);
        mImageViewLike = (ImageView) v.findViewById(R.id.imageViewLike);
        mImageViewDislike = (ImageView) v.findViewById(R.id.imageViewDislike);
        mTextViewDescription.setMovementMethod(new ScrollingMovementMethod());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if(seekBar.getProgress()>20 && seekBar.getProgress()<80){
                    seekBar.setProgress(50);
                    Toast.makeText(getContext(), "neutral", Toast.LENGTH_SHORT).show();
                }
                if(seekBar.getProgress()<20){
                    seekBar.setProgress(0);
                    Toast.makeText(getContext(), "disliked", Toast.LENGTH_SHORT).show();
                }
                if(seekBar.getProgress()>80){
                    seekBar.setProgress(100);
                    Toast.makeText(getContext(), "liked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fetchVideoDescription();
        return v;
    }

    public void fetchVideoDescription(){
        Log.d("VideoListPlay", "fetchVideoDescription");
        mPresenter.setupCredential(mCredential);
        mPresenter.setupVideoId(mVideoId);
        mPresenter.fetchVideoDescription();
    }

    public void setVideoDescription(VideoDescription videoDescriptionData){
        mTextViewDescription.setText(videoDescriptionData.videoDescription);
        mTextViewLikes.setText(String.valueOf(videoDescriptionData.countLikes));
        mTextViewDislikes.setText(String.valueOf(videoDescriptionData.countDislikes));
        mTextViewAuthorName.setText(videoDescriptionData.authorName);
        mTextViewVideoTitle.setText(videoDescriptionData.videoTitle);
        mTextViewPublishedAt.setText(videoDescriptionData.publishedAt);
        mImageViewAuthor.setImageDrawable(videoDescriptionData.authorImage);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
