package com.example.watchtube.model.APIUtils;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.watchtube.R;
import com.example.watchtube.VideoDescriptionPresenter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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
    private VideoDescriptionPresenter mPresenter;
    private Context mContext;

    public YouTubeMP3DownloaderRxJava(Context context, VideoDescriptionPresenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
    }

    public Completable startDownload = Completable.create(e -> {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.convertmp3.io/fetch/?format=JSON&video=https://www.youtube.com/watch?v="+mVideoId)
                .build();
        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();
        Log.d("stringResponse", stringResponse);

            JSONObject jsonObject = new JSONObject(stringResponse);
            String downloadLink = jsonObject.getString("link");
            String downloadTitle = jsonObject.getString("title");
            DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(downloadLink);
            DownloadManager.Request requestDownload = new DownloadManager.Request(uri);
            requestDownload.setTitle(downloadTitle);
            requestDownload.setDescription("Downloading");
            requestDownload.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            requestDownload.setDestinationUri(Uri.parse(("file://" + Environment
                    .getExternalStorageDirectory().toString()
                    + "/PUDGE/" + downloadTitle + ".mp3")));
            downloadmanager.enqueue(requestDownload);


            /*
            OkHttpClient client2 = new OkHttpClient();
            Request request2 = new Request.Builder()
                    .url(downloadLink)
                    .build();
            Response response2 = client2.newCall(request2).execute();
            String fileName = downloadTitle;
            File folder = new File(Environment.getExternalStorageDirectory(), "/PUDGE");
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
            }*/

            /*
        int count;
        URL url = new URL(downloadLink);
        URLConnection conection = url.openConnection();
        conection.connect();

        // this will be useful so that you can show a tipical 0-100%
        // progress bar
        int lenghtOfFile = conection.getContentLength();

        // download the file
        InputStream input = new BufferedInputStream(url.openStream(),
                8192);

        // Output stream
        OutputStream output = new FileOutputStream(Environment
                .getExternalStorageDirectory().toString()
                + "/PUDGE/" + downloadTitle + ".mp3");

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
            total += count;
            // publishing the progress....
            // After this onProgressUpdate will be called
            Log.d("DOWNLOADER", "" + (int) ((total * 100) / lenghtOfFile));
            //mPresenter.setProgress(Integer.parseInt("" + (int) ((total * 100) / lenghtOfFile)));
            e.onNext((int) ((total * 100) / lenghtOfFile));
            // writing data to file
            output.write(data, 0, count);
        }*/

        e.onComplete();
    });

    private Dialog getProgressDialog(String url){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setTitle("Downloading...");
        WebView webView = (WebView) dialog.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        //mCircularProgressBar = (CircularProgressBar) dialog.findViewById(R.id.progress_bar);
        //mTextViewProgress = (TextView) dialog.findViewById(R.id.textViewProgress);
        return dialog;
    }

    /*public Completable startDownloadRx = Completable.create(new CompletableOnSubscribe() {
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
            File folder = new File(Environment.getExternalStorageDirectory(), "/PUDGE");
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
            int count;
            URL url = new URL(downloadLink);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/PUDGE/" + downloadTitle + ".mp3");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                Log.d("DOWNLOADER", "" + (int) ((total * 100) / lenghtOfFile));
                mPresenter.setProgress(Integer.parseInt("" + (int) ((total * 100) / lenghtOfFile)));
                // writing data to file
                output.write(data, 0, count);
            }
            e.onComplete();
        }
    });*/

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
