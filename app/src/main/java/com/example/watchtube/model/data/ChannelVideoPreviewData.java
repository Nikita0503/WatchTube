package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 07.09.2018.
 */

public class ChannelVideoPreviewData {

    public String videoId;
    public String videoTitle;
    public String publishedAt;
    public Drawable videoImage;

    public ChannelVideoPreviewData(String videoId, String videoTitle, String publishedAt, Drawable videoImage) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.publishedAt = publishedAt;
        this.videoImage = videoImage;
    }
}
