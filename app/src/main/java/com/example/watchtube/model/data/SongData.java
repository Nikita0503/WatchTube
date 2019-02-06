package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 05.02.2019.
 */

public class SongData {
    public long songId;
    public String songDuration;
    public String songTitle;
    public String singer;
    public Drawable songImage;

    public SongData(long songId, String songDuration, String songTitle, String singer, Drawable songImage) {
        this.songId = songId;
        this.songDuration = songDuration;
        this.songTitle = songTitle;
        this.singer = singer;
        this.songImage = songImage;
    }
}
