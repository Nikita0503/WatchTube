package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 22.08.2018.
 */

public class SubscriptionData {

    public String channelId;
    public String title;
    public Drawable image;

    public SubscriptionData(String channelId, String title, Drawable image){
        this.channelId = channelId;
        this.title = title;
        this.image = image;
    }

}
