package com.example.watchtube.model.data;

import android.graphics.drawable.Drawable;

import java.math.BigInteger;

/**
 * Created by Nikita on 14.01.2019.
 */

public class VideoDescription {

    public BigInteger countLikes;
    public BigInteger countDislikes;
    public String videoTitle;
    public String authorName;
    public String videoDescription;
    public String publishedAt;
    public String authorId;
    public int duration;
    public Drawable authorImage;

    public VideoDescription(BigInteger countLikes, BigInteger countDislikes, String videoTitle, String authorName, String videoDescription, String publishedAt, String authorId, int duration, Drawable authorImage) {
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
        this.videoTitle = videoTitle;
        this.authorName = authorName;
        this.videoDescription = videoDescription;
        this.publishedAt = publishedAt;
        this.duration = duration;
        this.authorId = authorId;
        this.authorImage = authorImage;
    }
}
