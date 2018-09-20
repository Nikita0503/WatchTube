package com.example.watchtube;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.UI.ChannelPlaylistListFragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by Nikita on 16.09.2018.
 */

public class RootFragment extends Fragment {

    private static final String TAG = "RootFragment";
    ChannelPlaylistListFragment channelPlaylistListFragment;
    GoogleAccountCredential mCredential;
    String mChannelId;

    public void setCredentials(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;
    }

    public GoogleAccountCredential getCredential(){
        return mCredential;
    }

    public String getChannelId(){
        return mChannelId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*channelPlaylistListFragment = new ChannelPlaylistListFragment();
        channelPlaylistListFragment.setRootFragment(this);
        transaction.replace(R.id.root_frame, channelPlaylistListFragment);*/
        return view;
    }

    public void fetch(){
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        channelPlaylistListFragment = new ChannelPlaylistListFragment();
        channelPlaylistListFragment.setRootFragment(this);
        transaction.replace(R.id.root_frame, channelPlaylistListFragment);
        transaction.commit();

        Log.d("123456", mCredential.getSelectedAccountName());
        //channelPlaylistListFragment.setCredentials(mCredential);
        //channelPlaylistListFragment.setChannelId(mChannelId);
    }
}
