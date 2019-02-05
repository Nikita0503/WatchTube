package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 05.02.2019.
 */

public class SongData {
    public String songTitle;
    public String singer;
    public Drawable songImage;

    public SongData(String songTitle, String singer, Drawable songImage) {
        this.songTitle = songTitle;
        this.singer = singer;
        this.songImage = songImage;
    }
}
