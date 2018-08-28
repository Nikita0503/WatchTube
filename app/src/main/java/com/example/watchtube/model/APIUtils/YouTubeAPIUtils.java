package com.example.watchtube.model.APIUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.watchtube.Contract;
import com.example.watchtube.MainPresenter;
import com.example.watchtube.R;
import com.example.watchtube.VideoListPresenter;
import com.example.watchtube.model.data.SubscriptionData;
import com.example.watchtube.model.CircleTransform;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by Nikita on 22.08.2018.
 */

public class YouTubeAPIUtils {

    public String pageToken;
    private MainPresenter mMainPresenter;
    private VideoListPresenter mVideoListPresenter;
    private GoogleAccountCredential mCredential;
    private Context mContext;

    public YouTubeAPIUtils(Context context, MainPresenter mainPresenter, GoogleAccountCredential credential){
        mContext = context;
        mMainPresenter = mainPresenter;
        mCredential = credential;
    }

    public YouTubeAPIUtils(Context context, VideoListPresenter videoListPresenter, GoogleAccountCredential credential){
        mContext = context;
        mVideoListPresenter = videoListPresenter;
        mCredential = credential;
    }

    public Single<ArrayList<SubscriptionData>> getSubscriptionsInfo = Single.create(new SingleOnSubscribe<ArrayList<SubscriptionData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<SubscriptionData>> e) throws Exception {
            Log.d("LOG", "CALL");
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            ArrayList<SubscriptionData> subscriptionsInfo = new ArrayList<SubscriptionData>();
            SubscriptionListResponse result = null;
            if(pageToken != null){
                result = mService.subscriptions().list("snippet,contentDetails").setMine(true).setPageToken(pageToken).setMaxResults(50L).execute();
            }else{
                result = mService.subscriptions().list("snippet,contentDetails").setMine(true).setMaxResults(50L).execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
            List<Subscription> subscriptions = result.getItems();
            Drawable image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                    .load(R.drawable.home)
                    .get());
            subscriptionsInfo.add(new SubscriptionData("Home", image));
            Log.d("SIZE", "size = " + subscriptions.size());
            for(int i = 0; i < subscriptions.size(); i++){
                Subscription subscription = subscriptions.get(i);
                try {
                    image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(subscription.getSnippet().getThumbnails().getMedium().getUrl())
                            .transform(new CircleTransform(25, 0)).get());
                } catch (IOException d) {
                    d.printStackTrace();
                }
                subscriptionsInfo.add(new SubscriptionData(subscription.getSnippet().getTitle(), image));
            }

            e.onSuccess(subscriptionsInfo);
        }
    });

    public Single<ArrayList<VideoPreviewData>> getVideoPreviewData = Single.create(new SingleOnSubscribe<ArrayList<VideoPreviewData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<VideoPreviewData>> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            ArrayList<VideoPreviewData> videoPreviewData = new ArrayList<VideoPreviewData>();
            ActivityListResponse result = null;
            if(pageToken != null){
                result =  mService.activities().list("snippet,contentDetails").setMine(true).setPageToken(pageToken).setMaxResults(10L).execute();
            }else{
                result =  mService.activities().list("snippet,contentDetails").setMine(true).setMaxResults(10L).execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
            List<Activity> activities = result.getItems();
            Drawable image;
            String videoTitle;
            String channelTitle;
            for(int i = 0; i < activities.size(); i++){
                Activity activity = activities.get(i);
                videoTitle = activity.getSnippet().getTitle();
                channelTitle = activity.getSnippet().getChannelTitle();
                image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(activity.getSnippet().getThumbnails().getMedium().getUrl()).get());
                videoPreviewData.add(new VideoPreviewData(videoTitle, channelTitle, image));
            }
            e.onSuccess(videoPreviewData);
        }
    });



}
