package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.watchtube.UI.ChannelDescriptionFragment;
import com.example.watchtube.UI.ChannelVideoListFragment;
import com.example.watchtube.R;
import com.example.watchtube.UI.RootFragment;
import com.example.watchtube.ViewPagerAdapter;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


/**
 * Created by Nikita on 24.12.2018.
 */

public class ChannelFragment extends Fragment {

    private String mChannelId;
    private GoogleAccountCredential mCredential;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public void setChannelId(String mChannelId) {
        this.mChannelId = mChannelId;
    }

    public void setCredential(GoogleAccountCredential mCredential) {
        this.mCredential = mCredential;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel, container, false);
        mTabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        ChannelDescriptionFragment fragmentDescription = new ChannelDescriptionFragment();
        fragmentDescription.setCredential(mCredential);
        fragmentDescription.setChannelId(mChannelId);
        adapter.addFragment(fragmentDescription, "Description");
        ChannelVideoListFragment fragmentVideos = new ChannelVideoListFragment();
        fragmentVideos.setCredential(mCredential);
        fragmentVideos.setChannelId(mChannelId);
        adapter.addFragment(fragmentVideos, "Videos");
        RootFragment fragmentPlaylists = new RootFragment();
        fragmentPlaylists.setCredential(mCredential);
        fragmentPlaylists.setChannelId(mChannelId);
        adapter.addFragment(fragmentPlaylists, "Playlists");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return v;
    }
}
