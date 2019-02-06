package com.example.watchtube.model.APIUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.watchtube.MusicListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.model.data.SongData;

import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Nikita on 05.02.2019.
 */

public class MusicListProvider {

    private Context mContext;

    public MusicListProvider(Context context) {
        mContext = context;
    }

    public Observable<SongData> getSongList = Observable.create(e -> {


        /*ArrayList<File> files = listFilesWithSubFolders(new File(Environment.getExternalStorageDirectory(), "/PUDGE"));
        for(int i = 0; i < files.size(); i++){
            String songTitle;
            String singer;
            Drawable songImage;
            MediaStore
            SongData songData = new SongData();

            e.onNext(songData);
        }*/

        ContentResolver musicResolver = mContext.getContentResolver();
        int counter = 0;
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath()+"/PUDGE";

        Log.d("MUSIC", Uri.fromFile(new File("sdcard/PUDGE")).toString());
        Log.d("MUSIC", Uri.parse(sdcard+"/PUDGE/").toString());
        Cursor musicCursor = musicResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                long thisDuration = musicCursor.getLong(durationColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                Drawable thisImage = mContext.getDrawable(R.drawable.download);
                Log.d("MUSIC", thisDuration+"");
                /*MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                Log.d("MUSIC_PATH", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()+"/"+thisTitle+".mp3");
                metaRetriver.setDataSource(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()+"/"+thisTitle+".mp3");

                byte[] cover = metaRetriver.getEmbeddedPicture();
                if (cover != null) {
                    Bitmap image = BitmapFactory.decodeByteArray(cover, 0, cover.length);
                    thisImage = new BitmapDrawable(mContext.getResources(), image);
                }*/

                e.onNext(new SongData(thisId, getDuration(thisDuration), thisTitle, thisArtist, thisImage));
                counter++;
                Log.d("MUSIC", thisTitle);
            }
            while (musicCursor.moveToNext());
        }
        Log.d("MUSIC", counter+"");
        e.onComplete();
    });

    /*public ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFilesWithSubFolders(file));
            else
                files.add(file);
        }
        return files;
    }*/

    public String getDuration(long oldTime) {
        oldTime=oldTime/1000;
        long hour = oldTime/3600;
        long min = oldTime/60 % 60;
        long sec = oldTime/ 1 % 60;
        if(hour<1){
            if(min<10){
                return String.format("%2d:%02d", min, sec);
            }else{
                return String.format("%02d:%02d", min, sec);
            }
        }else{
            if(hour<10){
                return String.format("%2d:%02d:%02d", hour, min, sec);
            }else{
                return String.format("%02d:%02d:%02d", hour, min, sec);
            }

        }
    }

}
