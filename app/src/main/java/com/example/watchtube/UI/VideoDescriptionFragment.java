package com.example.watchtube.UI;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.ajts.androidmads.youtubemp3.YTubeMp3Service;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.VideoDescriptionPresenter;
import com.example.watchtube.model.APIUtils.YouTubeMP3Downloader;
import com.example.watchtube.model.data.VideoDescription;
import com.example.watchtube.model.data.search.SearchChannelData;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.File;
import java.util.ArrayList;



/**
 * Created by Nikita on 14.01.2019.
 */

public class VideoDescriptionFragment extends Fragment implements Contract.View{

    private String mVideoId;
    private String mChannelId;
    private VideoDescriptionPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private TextView mTextViewLikes;
    private TextView mTextViewDislikes;
    private TextView mTextViewVideoTitle;
    private TextView mTextViewAuthorName;
    private TextView mTextViewDescription;
    private TextView mTextViewPublishedAt;
    private TextView mTextViewProgress;
    private ImageView mImageViewAuthor;
    private ImageView mImageViewDownload;
    private ProgressBar mProgressBar;
    private CircularProgressBar mCircularProgressBar;
    private Dialog mProgressDialog;
    private SparkButton mSparkButtonLike;
    private SparkButton mSparkButtonDislike;

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
        Log.d("VideoListPlay", "setCredential");
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
        Log.d("VideoListPlay", "setVideoId");
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VideoDescriptionPresenter(this);
        mPresenter.onStart();
        setRetainInstance(true);
        Log.d("VideoListPlay", "onCreate");
        /*ArrayList<File> files = listFilesWithSubFolders(new File(getActivity().getApplicationContext().getFilesDir(), "/PUDGE"));
        for(int i = 0; i < files.size(); i++){
            Log.d("FILE", files.get(i).getName());
        }*/
        //TODO: сделать кнопку с плееором
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("VideoListPlay", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_video_description, container, false);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mCircularProgressBar = (CircularProgressBar) v.findViewById(R.id.progress_bar);
        mTextViewLikes = (TextView) v.findViewById(R.id.textViewLike);
        mTextViewDislikes = (TextView) v.findViewById(R.id.textViewDislike);
        mTextViewVideoTitle = (TextView) v.findViewById(R.id.textViewVideoTitle);
        mTextViewAuthorName = (TextView) v.findViewById(R.id.textViewAuthorName);
        mTextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        mTextViewPublishedAt = (TextView) v.findViewById(R.id.textViewPublishedAt);
        mImageViewDownload = (ImageView) v.findViewById(R.id.imageViewDownload);
        mImageViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.fetchMP3FileData(mVideoId);
                /*Toast.makeText(getContext(), "Download...", Toast.LENGTH_SHORT).show();
                new YouTubeMP3Downloader.Builder(getActivity())
                        .setDownloadUrl(mVideoId)
                        .setFolderPath(new File(getActivity().getApplicationContext().getFilesDir()Environment.getExternalStorageState(), "/PUDGE").getPath())
                        .setOnDownloadListener(new YouTubeMP3Downloader.Builder.DownloadListener() {
                            @Override
                            public void onSuccess(String savedPath) {
                                Log.v("exce1", savedPath);
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadStarted() {
                                mProgressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                                Log.v("exce2", e.getMessage());
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }).build();*/
            }
        });
        mImageViewAuthor = (ImageView) v.findViewById(R.id.imageViewAuthor);
        mImageViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelFragment fragment = new ChannelFragment();
                fragment.setCredential(mCredential);
                fragment.setChannelId(mChannelId);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                MainActivity activity = (MainActivity) getActivity();
                activity.hideBottom();
            }
        });
        mSparkButtonLike = (SparkButton) v.findViewById(R.id.spark_button_like);
        mSparkButtonLike.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){
                    mSparkButtonDislike.setChecked(false);
                    Log.d("SPARK_BUTTON_L", 1+"");
                }else{
                    Log.d("SPARK_BUTTON_L", 0+"");
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        mSparkButtonDislike = (SparkButton) v.findViewById(R.id.spark_button_dislike);
        mSparkButtonDislike.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState){
                    mSparkButtonLike.setChecked(false);
                    Log.d("SPARK_BUTTON_D", 1+"");
                }else{
                    Log.d("SPARK_BUTTON_D", 0+"");
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        //mImageViewLike = (ImageView) v.findViewById(R.id.imageViewLike);
        mTextViewDescription.setMovementMethod(new ScrollingMovementMethod());
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
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showProgress(){
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void hideProgress(){
        mProgressDialog.hide();
    }

    public void setProgress(int progress) {
        mCircularProgressBar.setProgress(progress);
        mTextViewProgress.setText(String.valueOf(progress));
    }

    private Dialog getProgressDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.download_dialog);
        dialog.setTitle("Downloading...");
        mCircularProgressBar = (CircularProgressBar) dialog.findViewById(R.id.progress_bar);
        mTextViewProgress = (TextView) dialog.findViewById(R.id.textViewProgress);
        return dialog;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }

    public ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFilesWithSubFolders(file));
            else
                files.add(file);
        }
        return files;
    }
}
