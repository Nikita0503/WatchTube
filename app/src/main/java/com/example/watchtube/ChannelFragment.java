package com.example.watchtube;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchtube.UI.ChannelDescriptionFragment;
import com.example.watchtube.UI.ChannelVideoListFragment;
import com.example.watchtube.UI.TwoFragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class ChannelFragment extends Fragment {

    private String mChannelId;
    private TabLayout mTabLayout;
    private ViewPager mViewPagerChannel;
    private GoogleAccountCredential mCredential;
    private ChannelDescriptionFragment mChannelDescriptionFragment;
    private ChannelVideoListFragment fragmentOne;
    private TwoFragment twoFragment;
    private ConstraintLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;

    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void fetchChannelData(){
        mChannelDescriptionFragment.setCredential(mCredential);
        mChannelDescriptionFragment.setChannelId(mChannelId);
        mChannelDescriptionFragment.fetchChannelData();
        fragmentOne.setCredentials(mCredential);
        fragmentOne.setChannelId(mChannelId);
        fragmentOne.fetchVideoListData();
        mLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel, container, false);
        mTabLayout = v.findViewById(R.id.tabsFragment);
        mViewPagerChannel = v.findViewById(R.id.viewpagerChannel);
        mTabLayout.setupWithViewPager(mViewPagerChannel);
        mLayout = v.findViewById(R.id.constraintLayoutChannel);
        setupPagerAdapter();
        return v;
    }


    public void setupPagerAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        mChannelDescriptionFragment = new ChannelDescriptionFragment();
        adapter.addFragment(mChannelDescriptionFragment, "Description");
        fragmentOne = new ChannelVideoListFragment();
        adapter.addFragment(fragmentOne, "Videos");
        twoFragment = new TwoFragment();
        adapter.addFragment(twoFragment, "Playlists");
        mViewPagerChannel.setAdapter(adapter);
        mViewPagerChannel.setOffscreenPageLimit(2);
    }
}
