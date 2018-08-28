package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 27.08.2018.
 */

public class VideoPreviewData {

    public Drawable image;
    public String videoTitle;
    public String channelTitle;

    public VideoPreviewData(String videoTitle, String channelTitle, Drawable image){
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.image = image;
    }


}
