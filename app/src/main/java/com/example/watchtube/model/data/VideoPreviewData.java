package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.api.client.util.DateTime;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikita on 27.08.2018.
 */

public class VideoPreviewData {

    public String videoId;
    public String channelId;
    public BigInteger viewCount;
    public String videoTitle;
    public String channelTitle;
    public String publishedAt;
    public String duration;
    public Drawable videoImage;
    public Drawable channelImage;

    public VideoPreviewData(String videoId, String videoTitle, BigInteger viewCount, Drawable videoImage, String channelId, String channelTitle, Drawable channelImage, String publishedAt, String duration) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.viewCount = viewCount;
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.publishedAt = publishedAt;
        this.videoImage = videoImage;
        this.channelImage = channelImage;
        this.duration = duration;
    }
}
