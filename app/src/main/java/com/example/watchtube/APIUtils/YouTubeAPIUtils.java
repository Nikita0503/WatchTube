package com.example.watchtube.APIUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.watchtube.MainPresenter;
import com.example.watchtube.R;
import com.example.watchtube.SomeSubscriptionData;
import com.example.watchtube.model.CircleTransform;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Nikita on 22.08.2018.
 */

public class YouTubeAPIUtils {

    private MainPresenter mMainPresenter;
    private GoogleAccountCredential mCredential;
    private Context mContext;

    public YouTubeAPIUtils(Context context, MainPresenter mainPresenter, GoogleAccountCredential credential){
        mContext = context;
        mMainPresenter = mainPresenter;
        mCredential = credential;
    }

    public Single<ArrayList<SomeSubscriptionData>> getSubscriptionsInfo = Single.create(new SingleOnSubscribe<ArrayList<SomeSubscriptionData>>() {
        @Override
        public void subscribe(SingleEmitter<ArrayList<SomeSubscriptionData>> e) throws Exception {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.youtube.YouTube mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName("WatchTube")
                    .build();
            ArrayList<SomeSubscriptionData> subscriptionsInfo = new ArrayList<SomeSubscriptionData>();
            SubscriptionListResponse result = mService.subscriptions().list("snippet,contentDetails").setMine(true).setMaxResults(50L).execute();
            List<Subscription> subscriptions =  result.getItems();
            Drawable image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                    .load(R.drawable.home)
                    .get());
            subscriptionsInfo.add(new SomeSubscriptionData("Home", image));
            for(int i = 0; i < subscriptions.size(); i++){
                Subscription subscription = subscriptions.get(i);
                try {
                    image = new BitmapDrawable(mContext.getResources(), Picasso.with(mContext)
                            .load(subscription.getSnippet().getThumbnails().getMedium().getUrl())
                            .transform(new CircleTransform(25, 0)).get());
                } catch (IOException d) {
                    d.printStackTrace();
                }
                subscriptionsInfo.add(new SomeSubscriptionData(subscription.getSnippet().getTitle(), image));
            }

            e.onSuccess(subscriptionsInfo);
        }
    });

    /*public DisposableObserver<List<SomeSubscriptionData>> getSubscriptions(){
        return new DisposableObserver<List<SomeSubscriptionData>>() {
            @Override
            public void onNext(List<SomeSubscriptionData> defaultObject) {
                if (defaultObject == null || defaultObject.size() == 0) {
                    mMainPresenter.setText("No results returned.");
                } else {

                    String s = "";
                    for(int i = 0; i < defaultObject.size(); i++) {
                        s += defaultObject.get(i).title;
                    }
                    //loadImageFromUrl(defaultObject.get(0).URL);
                    mMainPresenter.setText(s);
                    onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mMainPresenter.hideProgress();
            }
        };
    }*/
}
