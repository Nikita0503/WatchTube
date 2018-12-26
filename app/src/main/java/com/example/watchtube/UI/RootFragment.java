package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.R;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Created by Nikita on 16.09.2018.
 */

public class RootFragment extends Fragment {

    private static final String TAG = "RootFragment";
    private String mChannelId;
    private GoogleAccountCredential mCredential;


    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;
    }

    /*public GoogleAccountCredential getCredential(){
        return mCredential;
    }*/

    /*public String getChannelId(){
        return mChannelId;
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);
        ChannelPlaylistListFragment fragment = new ChannelPlaylistListFragment();
        fragment.setCredential(mCredential);
        fragment.setChannelId(mChannelId);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.root_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        /*Button button;
        button = view.findViewById(R.id.button2);*/

        /*channelPlaylistListFragment = new ChannelPlaylistListFragment();
        channelPlaylistListFragment.setRootFragment(this);
        transaction.replace(R.id.root_frame, channelPlaylistListFragment);*/
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction1 = manager.beginTransaction();
                TwoFragment fragment2 = new TwoFragment();
                transaction1.replace(R.id.container, fragment2, "two");
                transaction1.addToBackStack(null);
                transaction1.commit();
            }
        });*/
        return view;
    }



    /*public void fetch(){
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        channelPlaylistListFragment = new ChannelPlaylistListFragment();
        channelPlaylistListFragment.setRootFragment(this);
        transaction.replace(R.id.root_frame, channelPlaylistListFragment).addToBackStack(null);
        transaction.commit();

        Log.d("123456", mCredential.getSelectedAccountName());
        //channelPlaylistListFragment.setCredentials(mCredential);
        //channelPlaylistListFragment.setChannelId(mChannelId);
    }*/
}
