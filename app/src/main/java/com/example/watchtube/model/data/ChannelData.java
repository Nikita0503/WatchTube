package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

import com.google.api.client.util.DateTime;

import java.math.BigInteger;

/**
 * Created by Nikita on 31.08.2018.
 */

public class ChannelData {
    public String id;
    public String title;
    public BigInteger subscriptionsCount;
    public String description;
    public DateTime publishedAt;
    public String uploadPlaylist;
    public String kind;
    public String color;
    public Drawable imageIcon;
    public Drawable imageBanner;

    public ChannelData(String id, String title, BigInteger subscriptionsCount, String description, DateTime publishedAt,
                       String uploadPlaylist, String kind, String color, Drawable imageIcon, Drawable imageBanner) {
        this.id = id;
        this.title = title;
        this.subscriptionsCount = subscriptionsCount;
        this.description = description;
        this.publishedAt = publishedAt;
        this.uploadPlaylist = uploadPlaylist;
        this.kind = kind;
        this.color = color;
        this.imageIcon = imageIcon;
        this.imageBanner = imageBanner;
    }
}
