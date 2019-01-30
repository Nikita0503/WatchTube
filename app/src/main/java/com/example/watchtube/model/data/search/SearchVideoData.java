package com.example.watchtube.model.data.search;

import android.graphics.drawable.Drawable;

import com.example.watchtube.SearchItemType;

/**
 * Created by Nikita on 30.01.2019.
 */

public class SearchVideoData implements SearchItemType {
    public String videoTitle;
    public String channelTitle;
    public String publishedAt;
    public Drawable videoImage;

    public SearchVideoData(String videoTitle, String channelTitle, String publishedAt, Drawable videoImage) {
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.publishedAt = publishedAt;
        this.videoImage = videoImage;
    }
}
