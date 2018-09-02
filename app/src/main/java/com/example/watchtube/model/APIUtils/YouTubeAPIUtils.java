package com.example.watchtube.model.APIUtils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.watchtube.ChannelDescriptionPresenter;
import com.example.watchtube.MainPresenter;
import com.example.watchtube.R;
import com.example.watchtube.VideoListPresenter;
import com.example.watchtube.model.data.ChannelData;
import com.example.watchtube.model.data.SubscriptionData;
import com.example.watchtube.model.CircleTransform;
import com.example.watchtube.model.data.VideoPreviewData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by Nikita on 22.08.2018.
 */

public class YouTubeAPIUtils {

    private String mChannelId;
    public String pageToken;
    private MainPresenter mMainPresenter;
    private VideoListPresenter mVideoListPresenter;
    private ChannelDescriptionPresenter mChannelPresenter;
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

    public YouTubeAPIUtils(Context context, ChannelDescriptionPresenter channelPresenter, GoogleAccountCredential credential, String channelId){
        mContext = context;
        mChannelPresenter = channelPresenter;
        mCredential = credential;
        mChannelId = channelId;
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
            subscriptionsInfo.add(new SubscriptionData("0", "Home", image));
            Log.d("descriptionSize", ": " + subscriptions.size());
            for(int i = 0; i < subscriptions.size(); i++){
                Subscription subscription = subscriptions.get(i);
                try {
                    image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(subscription.getSnippet().getThumbnails().getMedium().getUrl())
                            .transform(new CircleTransform(25, 0))
                            .get());
                } catch (IOException d) {
                    d.printStackTrace();
                }
                subscriptionsInfo.add(new SubscriptionData(subscription.getSnippet().getResourceId().getChannelId(), subscription.getSnippet().getTitle(), image));
            }

            e.onSuccess(subscriptionsInfo);
        }
    });

    public Single<ChannelData> getChannelData = Single.create(new SingleOnSubscribe<ChannelData>() {
        @Override
        public void subscribe(SingleEmitter<ChannelData> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            Log.d("ChannelID", mChannelId);
            ChannelListResponse result = mService.channels().list("snippet,contentDetails,brandingSettings,statistics")
                    .setId(mChannelId)
                    .setFields("items(brandingSettings(channel(featuredChannelsTitle,profileColor)," +
                            "image/bannerMobileMediumHdImageUrl),contentDetails/relatedPlaylists/uploads," +
                            "id,snippet(description,publishedAt,thumbnails/medium,title),statistics" +
                            "(subscriberCount)),kind")
                    .execute();
            Channel channel = result.getItems().get(0);
            String id = channel.getId();
            String title = channel.getSnippet().getTitle();
            BigInteger subscriptionsCount = channel.getStatistics().getSubscriberCount(); // поменять на int
            String description = channel.getSnippet().getDescription();
            DateTime publishedAt = channel.getSnippet().getPublishedAt();
            String uploadPlaylist = channel.getContentDetails().getRelatedPlaylists().getUploads();
            String kind = channel.getBrandingSettings().getChannel().getFeaturedChannelsTitle();
            String color = channel.getBrandingSettings().getChannel().getProfileColor();
            Drawable imageIcon = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                    .load(channel.getSnippet().getThumbnails().getMedium().getUrl())
                    .transform(new CircleTransform(25, 0))
                    .get());
            Drawable imageBanner = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                    .load(channel.getBrandingSettings().getImage().getBannerMobileMediumHdImageUrl())
                    .get());
            ChannelData channelData = new ChannelData(id, title, subscriptionsCount, description, publishedAt,
                    uploadPlaylist, kind, color, imageIcon, imageBanner);
            e.onSuccess(channelData);
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
            VideoListResponse result = null;
            String regionCode = Locale.getDefault().getCountry();
            if(pageToken != null){
                result =  mService.videos()
                        .list("snippet,contentDetails,statistics")
                        .setChart("mostPopular").setPageToken(pageToken).setRegionCode(regionCode) // поменять тут
                        .setMaxResults(10L)
                        .setFields("items(statistics(viewCount),contentDetails/duration,id,snippet" +
                                "(channelId,channelTitle,publishedAt,thumbnails/high,title))," +
                                "nextPageToken,tokenPagination")
                        .execute();
            }else{
                result =  mService.videos()
                        .list("snippet,contentDetails,statistics")
                        .setChart("mostPopular")
                        .setRegionCode(regionCode)
                        .setMaxResults(10L)
                        .setFields("items(statistics(viewCount),contentDetails/duration,id,snippet" +
                                "(channelId,channelTitle,publishedAt,thumbnails/high,title))," +
                                "nextPageToken,tokenPagination")
                        .execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
            List<Video> videos = result.getItems();
            Drawable videoImage;
            Drawable channelImage;
            String videoId;
            String channelId;
            BigInteger viewCount;
            String videoTitle;
            String channelTitle;
            String publishedAt;
            String duration;
            Log.d("videosSize", ": " + videos.size());
            for(int i = 0; i < videos.size(); i++){
                Log.d("video",": "+i);
                Video video = videos.get(i);
                videoId = video.getId();
                videoTitle = video.getSnippet().getTitle();
                channelId = video.getSnippet().getChannelId();
                channelTitle = video.getSnippet().getChannelTitle();
                viewCount = video.getStatistics().getViewCount();
                publishedAt =  getTimeDifference(video.getSnippet().getPublishedAt());

                duration = String.valueOf(getDuration(video.getContentDetails().getDuration()));
                videoImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(video.getSnippet().getThumbnails().getHigh().getUrl())
                        .get());
                ChannelListResponse channelListResponse = mService.channels().list("snippet,contentDetails,brandingSettings").setId(channelId).setFields("items/snippet/thumbnails/default").execute();
                Channel channel = channelListResponse.getItems().get(0);
                channelImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(channel.getSnippet().getThumbnails().getDefault().getUrl())
                        .transform(new CircleTransform(15, 0))
                        .get());

                videoPreviewData.add(new VideoPreviewData(videoId, videoTitle, viewCount, videoImage, channelId, channelTitle, channelImage, publishedAt, duration));
            }
            e.onSuccess(videoPreviewData);
        }
    });

    public String getDuration(String oldTime) {
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        Period p = formatter.parsePeriod(oldTime);
        Seconds s = p.toStandardSeconds();
        long hour = s.getSeconds()/3600;
        long min = s.getSeconds()/60 % 60;
        long sec = s.getSeconds()/ 1 % 60;
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


    private String getTimeDifference(DateTime dateTime){
        Date date = new Date(dateTime.getValue());
        Date currentTime = Calendar.getInstance().getTime();
        long seconds = currentTime.getTime() - date.getTime();
        long days = seconds / (24 * 60 * 60 * 1000);
        if(days<1) {
            long hours = seconds / (60 * 60 * 1000);
            if(hours<1) {
                long minutes = seconds / (60 * 1000);
                if(minutes<1){
                    return "moment ago";
                }else{
                    if(minutes == 1){
                        return minutes + " minute ago";
                    }
                    return minutes + " minutes ago";
                }
            }else {
                if(hours == 1){
                    return hours + " hour ago";
                }
                return hours + " hours ago";
            }
        }else{
            if(days == 1){
                return days + " day ago";
            }
            return days + " days ago";
        }
    }


}
