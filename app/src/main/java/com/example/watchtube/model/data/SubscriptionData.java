package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 22.08.2018.
 */

public class SubscriptionData {

    public String title;
    public Drawable image;

    public SubscriptionData(String title, Drawable image){
        this.title = title;
        this.image = image;
    }

}
