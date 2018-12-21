package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

/**
 * Created by Nikita on 21.10.2018.
 */

public class CommentData {
    public String comment;
    public Drawable image;

    public CommentData(String comment, Drawable image) {
        this.comment = comment;
        this.image = image;
    }
}
