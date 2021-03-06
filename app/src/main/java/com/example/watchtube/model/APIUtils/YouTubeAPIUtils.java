package com.example.watchtube.model.APIUtils;

import android.accounts.Account;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.watchtube.ChannelDescriptionPresenter;
import com.example.watchtube.ChannelPlaylistListPresenter;
import com.example.watchtube.ChannelVideoListOfPlaylistPresenter;
import com.example.watchtube.ChannelVideoListPresenter;
import com.example.watchtube.MainPresenter;
import com.example.watchtube.PopularVideosListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.SearchItemType;
import com.example.watchtube.SearchPresenter;
import com.example.watchtube.UI.MainActivity;
import com.example.watchtube.VideoCommentsPresenter;
import com.example.watchtube.VideoDescriptionPresenter;
import com.example.watchtube.model.data.ChannelData;
import com.example.watchtube.model.data.ChannelPlaylistPreviewData;
import com.example.watchtube.model.data.ChannelVideoPreviewData;
import com.example.watchtube.model.data.CommentData;
import com.example.watchtube.model.data.SubscriptionData;
import com.example.watchtube.model.CircleTransform;
import com.example.watchtube.model.data.VideoDescription;
import com.example.watchtube.model.data.VideoPreviewData;
import com.example.watchtube.model.data.search.SearchChannelData;
import com.example.watchtube.model.data.search.SearchVideoData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.api.services.youtube.model.SubscriptionSnippet;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nikita on 22.08.2018.
 */

public class YouTubeAPIUtils {
    private String mChannelId;
    private String mPlaylistId;
    private String mVideoId;
    private String mRequest;
    public String pageToken;
    private MainPresenter mMainPresenter;
    //private VideoListPresenter mVideoListPresenter;
    private ChannelDescriptionPresenter mChannelPresenter;
    private ChannelVideoListPresenter mChannelVideoListPresenter;
    private ChannelPlaylistListPresenter mChannelPlaylistListPresenter;
    private ChannelVideoListOfPlaylistPresenter mChannelVideoListOfPlaylistPresenter;
    private VideoCommentsPresenter mVideoCommentsPresenter;
    private VideoDescriptionPresenter mVideoDescriptionPresenter;
    private SearchPresenter mSearchPresenter;
    private PopularVideosListPresenter mPopularVideosListPresenter;
    //private VideoFragmentPresenter mVideoFragmentPresenter;
    private GoogleAccountCredential mCredential;
    private Context mContext;

    public YouTubeAPIUtils(Context context, MainPresenter mainPresenter){
        mContext = context;
        mMainPresenter = mainPresenter;
    }

    /*public YouTubeAPIUtils(Context context, VideoListPresenter videoListPresenter){
        mContext = context;
        mVideoListPresenter = videoListPresenter;
    }*/

    public YouTubeAPIUtils(Context context, ChannelDescriptionPresenter channelPresenter){
        mContext = context;
        mChannelPresenter = channelPresenter;
    }

    public YouTubeAPIUtils(Context context, ChannelVideoListPresenter channelVideoListPresenter){
        mContext = context;
        mChannelVideoListPresenter = channelVideoListPresenter;
    }

    public YouTubeAPIUtils(Context context, ChannelPlaylistListPresenter channelVideoListPresenter){
        mContext = context;
        mChannelPlaylistListPresenter = channelVideoListPresenter;
    }

    public YouTubeAPIUtils(Context context, ChannelVideoListOfPlaylistPresenter channelVideoListOfPlaylistPresenter){
        mContext = context;
        mChannelVideoListOfPlaylistPresenter = channelVideoListOfPlaylistPresenter;     //лагает NextToken везде
    }

    public YouTubeAPIUtils(Context context, VideoCommentsPresenter videoCommentsPresenter){
        mContext = context;
        mVideoCommentsPresenter = videoCommentsPresenter;
    }

    public YouTubeAPIUtils(Context context, VideoDescriptionPresenter videoDescriptionPresenter){
        mContext = context;
        mVideoDescriptionPresenter = videoDescriptionPresenter;
    }

    public YouTubeAPIUtils(Context context, SearchPresenter searchPresenter){
        mContext = context;
        mSearchPresenter = searchPresenter;
    }

    public YouTubeAPIUtils(Context context, PopularVideosListPresenter popularVideosListPresenter){
        mContext = context;
        mPopularVideosListPresenter = popularVideosListPresenter;
    }

    /*public YouTubeAPIUtils(Context context, VideoFragmentPresenter videoFragmentPresenter){
        mContext = context;
        mVideoFragmentPresenter = videoFragmentPresenter;
    }*/

    public void setupChannelId(String channelId){
        mChannelId = channelId;
    }

    public void setupCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setupPlaylistId(String playlistId){
        mPlaylistId = playlistId;
    }

    public void setupVideoId(String videoId){
        mVideoId = videoId;
    }

    public void setupRequest(String request){
        mRequest = request;
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



    public Single<ArrayList<ChannelPlaylistPreviewData>>  getChannelPlaylistPreviewData = Single.create(new SingleOnSubscribe<ArrayList<ChannelPlaylistPreviewData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<ChannelPlaylistPreviewData>> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            Log.d("ChannelPlaylistAPI", mChannelId);
            ArrayList<ChannelPlaylistPreviewData> channelVideoPreviewData = new ArrayList<ChannelPlaylistPreviewData>();
            PlaylistListResponse result;
            if(pageToken != null) {
                result = mService.playlists().list("snippet,contentDetails")
                        .setChannelId(mChannelId)
                        .setPageToken(pageToken)
                        .setMaxResults(10L)
                        .execute();
            }else{
                result = mService.playlists().list("snippet,contentDetails")
                        .setChannelId(mChannelId)
                        .setMaxResults(10L)
                        .execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
            String id;
            String title;
            long itemCount;
            Drawable image;
            List<Playlist> playlists = result.getItems();
            Log.d("ChannelListAPI", "size = " + playlists.size());
            for(int i = 0; i < playlists.size(); i++){
                Playlist playlist = playlists.get(i);
                id = playlist.getId();
                itemCount = playlist.getContentDetails().getItemCount();
                title = playlist.getSnippet().getTitle();

                image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(playlist.getSnippet().getThumbnails().getMedium().getUrl())
                        .get());
                channelVideoPreviewData.add(new ChannelPlaylistPreviewData(id, title, itemCount, image));
            }
            e.onSuccess(channelVideoPreviewData);
        }
    });

    public Single<ArrayList<ChannelVideoPreviewData>> getChannelVideoPreviewOfPlaylistData = Single.create(new SingleOnSubscribe<ArrayList<ChannelVideoPreviewData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<ChannelVideoPreviewData>> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            Log.d("videoListOfPL", "WAS CREATED");
            Log.d("videoListOfPL", mCredential.getSelectedAccountName());
            ArrayList<ChannelVideoPreviewData> channelVideoPreviewData = new ArrayList<ChannelVideoPreviewData>();
            PlaylistItemListResponse result = null;
            if(pageToken != null){
                result = mService.playlistItems()
                        .list("snippet,contentDetails")
                        .setPlaylistId(mPlaylistId)
                        .setPageToken(pageToken)
                        .setMaxResults(10L)
                        .setFields("items(contentDetails(videoId,videoPublishedAt),snippet(thumbnails/medium,title)),nextPageToken")
                        .execute();

            }else{
                result = mService.playlistItems()
                        .list("snippet,contentDetails")
                        .setPlaylistId(mPlaylistId)
                        .setMaxResults(10L)
                        .setFields("items(contentDetails(videoId,videoPublishedAt),snippet(thumbnails/medium,title)),nextPageToken")
                        .execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
            Log.d("NORMALIN", "!");
            List<PlaylistItem> playlistItems = result.getItems();
            Log.d("NORMALIN", playlistItems.size()+"");
            String id;
            String title;
            String publishedAt;
            Drawable imageVideo;

            for(int i = 0; i < playlistItems.size(); i++){
                Log.d("VideoLISTof", "item = " + i);
                try {
                    PlaylistItem item = playlistItems.get(i);
                    id = item.getContentDetails().getVideoId();
                    title = item.getSnippet().getTitle();
                    publishedAt = getTimeDifference(item.getContentDetails().getVideoPublishedAt());
                    imageVideo = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(item.getSnippet().getThumbnails().getMedium().getUrl())
                            .get());

                    channelVideoPreviewData.add(new ChannelVideoPreviewData(id, i + ":" + title, publishedAt, imageVideo));
                }catch (Exception c){
                    c.printStackTrace();
                }
            }
            e.onSuccess(channelVideoPreviewData);
        }
    });

    public Single<ArrayList<SearchItemType>> getSearchResults = Single.create(new SingleOnSubscribe<ArrayList<SearchItemType>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<SearchItemType>> e) throws Exception {
            ArrayList<SearchItemType> resultsList = new ArrayList<SearchItemType>();
            /*resultsList.add(new SearchVideoData("Title", "Channel", "puslishedAt", mContext.getDrawable(R.drawable.ic_patient)));
            resultsList.add(new SearchChannelData("ChannelTitle", mContext.getDrawable(R.drawable.ic_doctor)));
            resultsList.add(new SearchVideoData("Title", "Channel", "puslishedAt", mContext.getDrawable(R.drawable.ic_patient)));
            resultsList.add(new SearchVideoData("Title", "Channel", "puslishedAt", mContext.getDrawable(R.drawable.ic_patient)));
            resultsList.add(new SearchChannelData("ChannelTitle", mContext.getDrawable(R.drawable.ic_doctor)));*/
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            SearchListResponse searchListResponse = mService.search().list("snippet")
                    .setType("channel, video")
                    .setQ(mRequest)
                    .setMaxResults(25L)
                    .execute();
            List<SearchResult> results = searchListResponse.getItems();
            for(int i = 0; i < results.size(); i++){
                if(results.get(i).getId().getKind().equals("youtube#channel")){
                    String channelId = results.get(i).getId().getChannelId();
                    String channelTitle = results.get(i).getSnippet().getChannelTitle();
                    String imageURL = results.get(i).getSnippet().getThumbnails().getMedium().getUrl();
                    Drawable imageChannel = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(imageURL)
                            .transform(new CircleTransform(50, 0))
                            .get());
                    resultsList.add(new SearchChannelData(channelId, channelTitle, imageChannel));
                }
                if(results.get(i).getId().getKind().equals("youtube#video")){
                    String videoId = results.get(i).getId().getVideoId();
                    String videoTitle = results.get(i).getSnippet().getTitle();
                    String channelTitle = results.get(i).getSnippet().getChannelTitle();
                    String publishedAt = getTimeDifference(results.get(i).getSnippet().getPublishedAt());
                    String imageURL = results.get(i).getSnippet().getThumbnails().getMedium().getUrl();
                    Drawable imageVideo = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(imageURL)
                            .get());
                    resultsList.add(new SearchVideoData(videoId, videoTitle, channelTitle, publishedAt, imageVideo));
                }
            }
            e.onSuccess(resultsList);
        }
    });

    public Single<ArrayList<ChannelVideoPreviewData>> getChannelVideoPreviewData = Single.create(new SingleOnSubscribe<ArrayList<ChannelVideoPreviewData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<ChannelVideoPreviewData>> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            Log.d("videoList", "WAS CREATED");
            ArrayList<ChannelVideoPreviewData> channelVideoPreviewData = new ArrayList<ChannelVideoPreviewData>();
            Log.d("Video ChannelID123", mChannelId);
            ChannelListResponse resultPlaylist = mService.channels().list("contentDetails")
                    .setId(mChannelId)
                    .setFields("items/contentDetails/relatedPlaylists/uploads")
                    .execute();
            Channel channel = resultPlaylist.getItems().get(0);
            mPlaylistId = channel.getContentDetails().getRelatedPlaylists().getUploads();
            PlaylistItemListResponse result = null;
            if(pageToken != null){
                result = mService.playlistItems()
                        .list("snippet,contentDetails")
                        .setPlaylistId(mPlaylistId)
                        .setPageToken(pageToken)
                        .setMaxResults(10L)
                        .setFields("items(contentDetails(videoId,videoPublishedAt),snippet(thumbnails/medium,title)),nextPageToken")
                        .execute();

            }else{
                result = mService.playlistItems()
                        .list("snippet,contentDetails")
                        .setPlaylistId(mPlaylistId)
                        .setMaxResults(10L)
                        .setFields("items(contentDetails(videoId,videoPublishedAt),snippet(thumbnails/medium,title)),nextPageToken")
                        .execute();
            }
            if(result.getNextPageToken() != null){
                pageToken = result.getNextPageToken();
            }else {
                pageToken = null;
            }
                Log.d("NORMAL", "!");
            List<PlaylistItem> playlistItems = result.getItems();
            String id;
            String title;
            String publishedAt;
            Drawable imageVideo;

            for(int i = 0; i < playlistItems.size(); i++){
                PlaylistItem item = playlistItems.get(i);
                id = item.getContentDetails().getVideoId();
                title = item.getSnippet().getTitle();
                publishedAt = getTimeDifference(item.getContentDetails().getVideoPublishedAt());
                imageVideo = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(item.getSnippet().getThumbnails().getMedium().getUrl())
                        .get());
                Log.d("VideoLIST", "item = " + i);
                channelVideoPreviewData.add(new ChannelVideoPreviewData(id, i+":"+title, publishedAt, imageVideo));
            }
            e.onSuccess(channelVideoPreviewData);
        }
    });

    public Single<VideoDescription> getVideoDescription = Single.create(new SingleOnSubscribe<VideoDescription>() {
        @Override
        public void subscribe(SingleEmitter<VideoDescription> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            VideoDescription videoDescription;
            VideoListResponse result = mService.videos().list("snippet,contentDetails,statistics")
                    .setId(mVideoId)
                    .execute();

            BigInteger countLikes;
            BigInteger countDislikes;
            String videoTitle;
            String authorName;
            String description;
            String publishedAt;
            String duration;
            Drawable authorImage;
            duration = result.getItems().get(0).getContentDetails().getDuration();
            Log.d("durationD", duration);
            countLikes = result.getItems().get(0).getStatistics().getLikeCount();
            countDislikes = result.getItems().get(0).getStatistics().getDislikeCount();
            videoTitle = result.getItems().get(0).getSnippet().getTitle();
            authorName = result.getItems().get(0).getSnippet().getChannelTitle();
            description = result.getItems().get(0).getSnippet().getDescription();
            SimpleDateFormat dateFormatAfter = new SimpleDateFormat("d MMM yyyy");
            Date date = new Date(result.getItems().get(0).getSnippet().getPublishedAt().getValue());
            publishedAt = dateFormatAfter.format(date);


            String channelId = result.getItems().get(0).getSnippet().getChannelId();

            ChannelListResponse resultImage = mService.channels().list("snippet,contentDetails,statistics")
                    .setId(channelId)
                    .setFields("items/snippet/thumbnails/default/url")
                    .execute();

            authorImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                    .load(resultImage.getItems().get(0).getSnippet().getThumbnails().getDefault().getUrl())
                    .transform(new CircleTransform(25, 0))
                    .get());
            int durationInt = getDurationInSeconds(duration);
            Log.d("durationD", durationInt+"");
            videoDescription = new VideoDescription(countLikes, countDislikes, videoTitle, authorName, description, publishedAt, channelId, durationInt, authorImage);
            e.onSuccess(videoDescription);
        }
    });

    public Single<ArrayList<CommentData>> getVideoComments = Single.create(new SingleOnSubscribe<ArrayList<CommentData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<CommentData>> e) throws Exception {
            /*
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();

            ArrayList<CommentData> videoComments = new ArrayList<CommentData>();
            Log.d("COMMENTS1", mVideoId);
            Log.d("COMMENTS1", mCredential.getSelectedAccountName());
            try {
                CommentThreadListResponse result = mService.commentThreads()
                        .list("snippet").setVideoId(mVideoId).setTextFormat("plainText").execute();
                List<CommentThread> comments = result.getItems();
                Drawable authorImage;
                String comment;
                Log.d("COMMENTS1", comments.size() + "");
                for (int i = 0; i < comments.size(); i++) {
                    CommentThread commentThread = comments.get(i);
                    comment = commentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay();
                    authorImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(commentThread.getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl())
                            .transform(new CircleTransform(25, 0))
                            .get());
                    //comment.getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl()
                    CommentData data = new CommentData(comment, authorImage);
                    videoComments.add(data);
                }

            }catch (Exception c){
                c.printStackTrace();
            }*/

            /*try {
               SubscriptionListResponse result = mService.subscriptions().list("snippet,contentDetails").setMine(true).setMaxResults(50L).execute();
               List<Subscription> subscriptions = result.getItems();
               for(int i = 0; i < subscriptions.size(); i++){
                   Drawable authorImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                           .load(subscriptions.get(i).getSnippet().getThumbnails().getMedium().getUrl())
                           .transform(new CircleTransform(25, 0))
                           .get());
                   videoComments.add(new CommentData(subscriptions.get(i).getSnippet().getTitle(), authorImage));
               }
            }catch (Exception c){
                c.printStackTrace();
            }*/


            /*videoComments.add(new CommentData("Good Video!", mContext.getResources().getDrawable(R.drawable.ic_doctor)));
            videoComments.add(new CommentData("Great Video!", mContext.getResources().getDrawable(R.drawable.ic_patient)));
            e.onSuccess(videoComments);*/
            ArrayList<CommentData> videoComments = new ArrayList<CommentData>();
            OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://www.googleapis.com/youtube/v3/commentThreads?part=snippet%2Creplies&order=relevance&textFormat=plainText&videoId="+mVideoId+"&fields=items(snippet(topLevelComment(snippet(authorChannelId%2CauthorDisplayName%2CauthorProfileImageUrl%2CtextDisplay))))&key=AIzaSyA8QOXpXSM1Q6g3haiSS_PBV5TZBmOdlkU ")
                        .build();

                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray commentsArray = jsonObject.getJSONArray("items");
                for(int i = 0; i < commentsArray.length(); i++) {
                    JSONObject postObject = commentsArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet");
                    /*Request requestImageURL = new Request.Builder()
                            .url("https://www.googleapis.com/youtube/v3/channels?part=snippet%2CcontentDetails%2Cstatistics&id="
                                    +postObject.getJSONObject("authorChannelId").getString("value")
                                    +"&fields=items%2Fsnippet%2Fthumbnails%2Fdefault%2Furl&key=AIzaSyA8QOXpXSM1Q6g3haiSS_PBV5TZBmOdlkU")
                            .build();
                    Response responseImageURL = client.newCall(requestImageURL).execute();
                    JSONObject jsonObjectImageURL = new JSONObject(responseImageURL.body().string());
                    */
                    String imageURL = postObject.getString("authorProfileImageUrl");
                    Drawable authorImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(imageURL)
                            .transform(new CircleTransform(25, 0))
                            .get());



                    String comment = postObject.getString("textDisplay");
                    videoComments.add(new CommentData(comment, authorImage));
                    Log.d("COMMENTS", comment);
                }
                e.onSuccess(videoComments);


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
                            "image/bannerMobileMediumHdImageUrl)," +
                            "id,snippet(description,publishedAt,thumbnails/medium,title),statistics" +
                            "(subscriberCount)),kind")
                    .execute();
            Channel channel = result.getItems().get(0);
            String id = channel.getId();
            String title = channel.getSnippet().getTitle();
            BigInteger subscriptionsCount = channel.getStatistics().getSubscriberCount(); // поменять на int
            String description = channel.getSnippet().getDescription();
            DateTime publishedAt = channel.getSnippet().getPublishedAt();
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
                     kind, color, imageIcon, imageBanner);
            e.onSuccess(channelData);
        }
    });

    public Single<Boolean> isSubscribed = Single.create(new SingleOnSubscribe<Boolean>() {
        @Override
        public void subscribe(SingleEmitter<Boolean> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            SubscriptionListResponse result = mService.subscriptions().list("snippet,contentDetails")
                    .setMine(true).setForChannelId(mChannelId).execute();
            if(result.getItems().size() == 0){
                e.onSuccess(false);
            }else{
                e.onSuccess(true);
            }
        }
    });



    //public Completable subscribe(){
    //    return Completable.create(new CompletableOnSubscribe() {
    //        @Override
    //        public void subscribe(CompletableEmitter e) throws Exception {
    //            HttpTransport transport = AndroidHttp.newCompatibleTransport();
    //            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    //            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
    //                    transport, jsonFactory, mCredential)
    //                    .setApplicationName("WatchTube")
    //                    .build();
//
    //            // Define the Subscription object, which will be uploaded as the request body.
    //            Subscription subscription = new Subscription();
//
    //            // Add the snippet object property to the Subscription object.
    //            SubscriptionSnippet snippet = new SubscriptionSnippet();
    //            ResourceId resourceId = new ResourceId();
    //            resourceId.setChannelId(mChannelId);
    //            resourceId.setKind("youtube#channel");
    //            snippet.setResourceId(resourceId);
    //            subscription.setSnippet(snippet);
    //            Log.d("subscribe_channel", mChannelId);
    //            Log.d("subscribe_channel", mCredential.getSelectedAccountName());
    //            // Define and execute the API request
    //            YouTube.Subscriptions.Insert request = mService.subscriptions()
    //                    .insert("snippet", subscription);
    //            Subscription response = request.execute();
    //        }
    //    });
    //}

    public Single<ArrayList<VideoPreviewData>> getPopularVideos = Single.create(new SingleOnSubscribe<ArrayList<VideoPreviewData>>() {
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
                        .setChart("mostPopular").setPageToken(pageToken).setRegionCode("UA") // поменять тут
                        .setMaxResults(10L)
                        .setFields("items(statistics(viewCount),contentDetails/duration,id,snippet" +
                                "(channelId,channelTitle,publishedAt,thumbnails/medium,title))," +
                                "nextPageToken,tokenPagination")
                        .execute();
            }else{
                result =  mService.videos()
                        .list("snippet,contentDetails,statistics")
                        .setChart("mostPopular")
                        .setRegionCode("UA")
                        .setMaxResults(10L)
                        .setFields("items(statistics(viewCount),contentDetails/duration,id,snippet" +
                                "(channelId,channelTitle,publishedAt,thumbnails/medium,title))," +
                                "nextPageToken,tokenPagination")
                        .execute();
            }
            if(result.getNextPageToken() != null){
                //pageToken = result.getNextPageToken();
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
                        .load(video.getSnippet().getThumbnails().getMedium().getUrl())
                        .transform(new CircleTransform(5, 0))
                        .get());
                ChannelListResponse channelListResponse = mService.channels().list("snippet,contentDetails,brandingSettings").setId(channelId).setFields("items/snippet/thumbnails/default").execute();
                Channel channel = channelListResponse.getItems().get(0);
                channelImage = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                        .load(channel.getSnippet().getThumbnails().getDefault().getUrl())
                        .transform(new CircleTransform(45, 0))
                        .get());

                videoPreviewData.add(new VideoPreviewData(videoId, videoTitle, viewCount, videoImage, channelId, channelTitle, channelImage, publishedAt, duration));
            }
            e.onSuccess(videoPreviewData);
        }
    });

    public int getDurationInSeconds(String oldTime) {
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        Period p = formatter.parsePeriod(oldTime);
        Seconds s = p.toStandardSeconds();
        return s.getSeconds();
    }

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
