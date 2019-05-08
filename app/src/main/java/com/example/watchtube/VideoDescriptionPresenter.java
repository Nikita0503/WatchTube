package com.example.watchtube;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.watchtube.UI.VideoDescriptionFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.model.APIUtils.YouTubeMP3Downloader;
import com.example.watchtube.model.APIUtils.YouTubeMP3DownloaderRxJava;
import com.example.watchtube.model.data.CommentData;
import com.example.watchtube.model.data.VideoDescription;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Nikita on 14.01.2019.
 */

public class VideoDescriptionPresenter implements Contract.Presenter {

    private int mStart;
    private int mEnd;
    private String mTimings;
    private String mVideoId;
    private VideoDescriptionFragment mFragment;
    private CompositeDisposable mDisposable;
    private GoogleAccountCredential mCredential;
    private YouTubeAPIUtils mYouTubeAPIUtils;
    private YouTubeMP3DownloaderRxJava mYouTubeMP3Downloader;

    public VideoDescriptionPresenter(VideoDescriptionFragment fragment){
        mFragment = fragment;
        mYouTubeAPIUtils = new YouTubeAPIUtils(fragment.getContext(), this);
        mYouTubeMP3Downloader = new YouTubeMP3DownloaderRxJava(mFragment.getContext(), this);
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
        mYouTubeAPIUtils.setupCredential(mCredential);
        //Log.d("COMMENTSS", mCredential.getSelectedAccountName());
    }

    public void setProgress(int progress){
        mFragment.setProgress(progress);
    }

    public void setupVideoId(String videoId){
        mVideoId = videoId;
        mYouTubeAPIUtils.setupVideoId(mVideoId);
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchVideoDescription(){
        Disposable disposable = mYouTubeAPIUtils.getVideoDescription.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<VideoDescription>() {
                    @Override
                    public void onSuccess(VideoDescription videoDescriptionData) {
                        mFragment.setVideoDescription(videoDescriptionData);
                        mFragment.setChannelId(videoDescriptionData.authorId);
                        Log.d("durationD1", videoDescriptionData.duration+"");
                        mFragment.setVideoDuration(videoDescriptionData.duration);
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(disposable);
    }



    public void fetchMP3FileData(String videoId, String videoName, int duration){
        mYouTubeMP3Downloader.setVideoId(videoId);
        Log.d("durationD", duration+"");
        Dialog dialog = getSetTimeDialog(videoName, duration);
        dialog.show();
    }

    private Dialog getSetTimeDialog(String videoName, int duration){
        mStart = 0;
        mEnd = duration;
        mTimings = "";
        Log.d("durationD", duration+"");
        final Dialog dialog = new Dialog(mFragment.getContext());
        dialog.setContentView(R.layout.set_time_dialog);
        dialog.setTitle("Downloading...");
        TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
        CheckBox checkBoxFullTrack = (CheckBox) dialog.findViewById(R.id.checkBoxFullTrack);
        EditText editTextFrom = (EditText) dialog.findViewById(R.id.editTextFrom);
        EditText editTextTo = (EditText) dialog.findViewById(R.id.editTextTo);
        CrystalRangeSeekbar crystalRangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar);
        Button buttonDownload = (Button) dialog.findViewById(R.id.buttonDownload);
        textViewTitle.setText(videoName);
        checkBoxFullTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextFrom.setText("");
                    editTextFrom.setEnabled(false);
                    editTextTo.setText("");
                    editTextTo.setEnabled(false);
                }else{
                    editTextFrom.setEnabled(true);
                    editTextFrom.setText(makeReadable(mStart));
                    editTextTo.setEnabled(true);
                    editTextTo.setText(makeReadable(mEnd));
                }
            }
        });
        crystalRangeSeekbar.setMaxValue(duration);
        crystalRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if(checkBoxFullTrack.isChecked()) {
                    editTextFrom.setText("");
                    editTextTo.setText("");
                }else {
                    editTextFrom.setText(makeReadable(minValue.intValue()));
                    editTextTo.setText(makeReadable(maxValue.intValue()));
                    mStart = minValue.intValue();
                    mEnd = maxValue.intValue();
                }
            }
        });
        checkBoxFullTrack.setChecked(true);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBoxFullTrack.isChecked()){
                    mStart = getTimeInSeconds(editTextFrom.getText().toString());
                    mEnd = getTimeInSeconds(editTextTo.getText().toString());
                    mTimings = "&start=" + mStart + "&end=" + mEnd;
                }else{
                    mTimings = "";
                }
                mYouTubeMP3Downloader.setTimings(mTimings);
                Disposable disposable = mYouTubeMP3Downloader.startDownload.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onStart() {
                                //mFragment.showProgress();
                                Toast.makeText(mFragment.getContext(), "Loading started...", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(Throwable t) {
                                //mFragment.showProgress();
                                Toast.makeText(mFragment.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                Dialog dialog = getProgressDialog("https://www.convertmp3.io/fetch/?format=JSON&video=https://www.youtube.com/watch?v="+mVideoId+mTimings, videoName);
                                //dialog.setCancelable(false);
                                dialog.show();
                                t.printStackTrace();
                            }
                            @Override
                            public void onComplete() {
                                //mFragment.hideProgress();
                                Toast.makeText(mFragment.getContext(), "Downloaded!", Toast.LENGTH_SHORT).show();
                            }
                        });
                mDisposable.add(disposable);
            }
        });
        //mCircularProgressBar = (CircularProgressBar) dialog.findViewById(R.id.progress_bar);
        //mTextViewProgress = (TextView) dialog.findViewById(R.id.textViewProgress);
        return dialog;
    }

    private int getTimeInSeconds(String time){
        int inSeconds = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int twoDotsCount = twoDotsCount(time,':');
        String[] timeInStr = time.split(":");
        if(twoDotsCount == 1){
            minutes = Integer.parseInt(timeInStr[0]);
            seconds = Integer.parseInt(timeInStr[1]);
            inSeconds = minutes*60 + seconds;
        }else{
            hours = Integer.parseInt(timeInStr[0]);
            minutes = Integer.parseInt(timeInStr[1]);
            seconds = Integer.parseInt(timeInStr[2]);
            inSeconds = hours*60*60 + minutes*60 + seconds;
        }
        return inSeconds;
    }

    public int twoDotsCount(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        return count;
    }

    private String makeReadable(int seconds) {
        int SS = 0; int MM = 0; int HH = 0;
        String response = "";
        HH = seconds / 3600;
        MM = (seconds - HH * 3600) / 60;
        SS = seconds - HH * 3600 - MM * 60;

        if(HH>0){
            response += HH+":";
        }
        response += MM+":";
        response += SS;
        return response;
    }

    private Dialog getProgressDialog(String url, String videoName){
        final Dialog dialog = new Dialog(mFragment.getContext());
        dialog.setContentView(R.layout.webview_dialog);
        dialog.setTitle("Downloading...");
        WebView webView = (WebView) dialog.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(url);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url));
                //request.allowScanningByMediaScanner();
                //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoName);
                //DownloadManager dm = (DownloadManager) mFragment.getContext().getSystemService(DOWNLOAD_SERVICE);
                //dm.enqueue(request);
                DownloadManager downloadmanager = (DownloadManager) mFragment.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request requestDownload = new DownloadManager.Request(uri);
                requestDownload.setTitle(videoName);
                requestDownload.setDescription("Downloading");
                requestDownload.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                requestDownload.setDestinationUri(Uri.parse(("file://" + Environment
                        .getExternalStorageDirectory().toString()
                        + "/PUDGE/" + videoName + ".mp3")));
                downloadmanager.enqueue(requestDownload);
            }
        });
        //mCircularProgressBar = (CircularProgressBar) dialog.findViewById(R.id.progress_bar);
        //mTextViewProgress = (TextView) dialog.findViewById(R.id.textViewProgress);
        return dialog;
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
