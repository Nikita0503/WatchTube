package com.example.watchtube.model.APIUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.watchtube.MusicListPresenter;
import com.example.watchtube.R;
import com.example.watchtube.model.data.SongData;

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
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.d("MUSIC", Uri.fromFile(new File(sdcard+"/PUDGE/")).toString());
        Log.d("MUSIC", Uri.parse(sdcard+"/PUDGE/").toString());
        Log.d("MUSIC", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
        Cursor musicCursor = musicResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                e.onNext(new SongData(thisTitle, thisArtist, mContext.getDrawable(R.drawable.download)));
                counter++;
                Log.d("MUSIC", thisTitle);
            }
            while (musicCursor.moveToNext());
        }
        Log.d("MUSIC", counter+"");
        e.onComplete();
    });

    public ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFilesWithSubFolders(file));
            else
                files.add(file);
        }
        return files;
    }
}
