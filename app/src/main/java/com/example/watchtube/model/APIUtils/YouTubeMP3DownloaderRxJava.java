package com.example.watchtube.model.APIUtils;

import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Nikita on 04.02.2019.
 */

public class YouTubeMP3DownloaderRxJava {

    private String mVideoId;



    public void setVideoId(String videoId){
        mVideoId = videoId;
    }

    public Completable startDownloadRx = Completable.create(new CompletableOnSubscribe() {
        @Override
        public void subscribe(CompletableEmitter e) throws Exception {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.convertmp3.io/fetch/?format=JSON&video=https://www.youtube.com/watch?v="+mVideoId)
                    .build();
            Response response = client.newCall(request).execute();
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            String downloadLink = jsonObject.getString("link");
            String downloadTitle = jsonObject.getString("title");


            OkHttpClient client2 = new OkHttpClient();
            Request request2 = new Request.Builder()
                    .url(downloadLink)
                    .build();
            Response response2 = client2.newCall(request2).execute();
            String fileName = downloadTitle;
            File folder = new File(Environment.getExternalStorageState(), "/PUDGE");
            if (!folder.exists()) {
                boolean folderCreated = folder.mkdir();
                Log.v("folderCreated", folderCreated + "");
            }
            File mp3File = new File(folder.getPath() + "/" + fileName + ".mp3");
            Log.d("DOWNLOADER", mp3File.getPath());
            try {
                boolean fileCreated = mp3File.createNewFile();
                Log.v("fileCreated", fileCreated + "");
                BufferedSink sink = Okio.buffer(Okio.sink(mp3File));
                sink.writeAll(response2.body().source());
                sink.close();
                Log.d("DOWNLOADER", mp3File.getPath());
                e.onComplete();
            } catch (Exception error) {
                e.onError(error);
            }
        }
    });

    /*public Single<String> saveMP3 = Single.create(new SingleOnSubscribe<String>() {

        @Override
        public void subscribe(SingleEmitter<String> e) throws Exception {
            String link = mSongData.get("link");
            String title = mSongData.get("title");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(link)
                    .build();
            Response response = client.newCall(request).execute();
            String fileName = title;
            File folder = new File(Environment.getExternalStorageState(), "/PUDGE");
            if (!folder.exists()) {
                boolean folderCreated = folder.mkdir();
                Log.v("folderCreated", folderCreated + "");
            }
            File mp3File = new File(folder.getPath() + "/" + fileName + ".mp3");
            try {
                boolean fileCreated = mp3File.createNewFile();
                Log.v("fileCreated", fileCreated + "");
                BufferedSink sink = Okio.buffer(Okio.sink(mp3File));
                sink.writeAll(response.body().source());
                sink.close();
                Log.d("DOWNLOADER", mp3File.getPath());
                e.onSuccess(mp3File.getPath());
            } catch (Exception error) {
                e.onError(error);
            }
        }
    });*/
}
