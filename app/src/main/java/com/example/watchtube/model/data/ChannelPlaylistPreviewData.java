package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 13.09.2018.
 */

public class ChannelPlaylistPreviewData {
    public String playlistId;
    public String playlistTitle;
    public long videoCount;
    public Drawable playlistImage;

    public ChannelPlaylistPreviewData(String playlistId, String playlistTitle, long videoCount, Drawable playlistImage) {
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.videoCount = videoCount;
        this.playlistImage = playlistImage;
    }
}
