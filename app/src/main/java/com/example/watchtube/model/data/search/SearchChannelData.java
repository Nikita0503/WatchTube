package com.example.watchtube.model.data.search;

import android.graphics.drawable.Drawable;

import com.example.watchtube.SearchItemType;

/**
 * Created by Nikita on 30.01.2019.
 */

public class SearchChannelData implements SearchItemType {
    public String channelId;
    public String channelTitle;
    public Drawable channelImage;

    public SearchChannelData(String channelId, String channelTitle, Drawable channelImage) {
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.channelImage = channelImage;
    }


}
