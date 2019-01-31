package com.example.watchtube.UI;

import com.example.watchtube.Contract;
import com.example.watchtube.MainPresenter;
import com.example.watchtube.R;
import com.example.watchtube.ViewPagerAdapter;
import com.example.watchtube.model.data.SubscriptionData;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.app.Dialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
        import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, Contract.View {
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final String BUTTON_TEXT = "Call YouTube Data API";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };
    public MainPresenter mPresenter;
    public CompositeDisposable disposables;
    private Drawer.Result drawerResult;
    public TextView mOutputText;
    private Toolbar mToolbar;
    //private ViewPager mViewPager;
    public ProgressDialog mProgress;
    //private TabLayout mTabLayout;
    private FragmentTransaction mTrans;
    private VideoFragment mVideoFragment;
    private View mBottomLayout;
    private TextView mTextViewVideoTitleBottom;
    public void setBottom(final String videoId, String videoTitle){
        mTextViewVideoTitleBottom.setText(videoTitle);
        mTrans = getSupportFragmentManager().beginTransaction();
        mVideoFragment = new VideoFragment();
        mVideoFragment.setVideoId(videoId);
        mVideoFragment.setCredential(mPresenter.getCredential());
        mTrans.replace(R.id.frgmCont, mVideoFragment);
        mTrans.commit();

        Log.d("TAG1234", videoId);
        /*fragment2.setCredential(mPresenter.getCredential());
        fragment2.setVideoId(videoId);

        fragment.setCredential(mPresenter.getCredential());
        fragment.setVideoId(videoId);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(fragment, "Info");
        adapter.addFragment(fragment2, "Comments");
        mViewPager.setAdapter(adapter);*/
       // mTabLayout.setupWithViewPager(mViewPager);
        //mVideoFragment.setVideoId(videoId);

//        mPlayer.loadVideo(videoId);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mBottomLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) { //NEXT PAGE TOKEN (Subscriptions)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomLayout = (View) findViewById(R.id.bottom_fragment);
        mTextViewVideoTitleBottom = (TextView) findViewById(R.id.panel);

        //mVideoView = (YouTubePlayerSupportFragment) mBottomLayout(R.id.youtube_fragment);
        /*ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment = new VideoDescriptionFragment();
        fragment2 = new VideoCommentsFragment();
        adapter.addFragment(fragment, "Info");
        adapter.addFragment(fragment2, "Comments");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);*/


        mOutputText = findViewById(R.id.textView);
        mOutputText.setText("In developing...");
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling YouTube Data API ...");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //mViewPager.setOffscreenPageLimit(2);
        //mTabLayout = (TabLayout) findViewById(R.id.tabs);
        //mTabLayoutTabLayout.setupWithViewPager(mViewPager);

        disposables = new CompositeDisposable();

        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Home")
                                .withIcon(R.drawable.home)
                                .withIdentifier(1),
                        new DividerDrawerItem())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if(position!=0){
                            //Toast.makeText(getApplicationContext(), String.valueOf(position-1), Toast.LENGTH_SHORT).show();
                            /*ChannelDescriptionFragment fragment = new ChannelDescriptionFragment();
                            fragment.setCredential(mPresenter.getCredential());
                            fragment.setChannelId(mPresenter.getSubscriptions().get(position-1).channelId);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();*/

                            /*ChannelVideoListFragment fragment = new ChannelVideoListFragment();
                            fragment.setCredential(mPresenter.getCredential());
                            fragment.setChannelId(mPresenter.getSubscriptions().get(position-1).channelId);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();*/

                            /*ChannelPlaylistListFragment fragment = new ChannelPlaylistListFragment();
                            fragment.setCredential(mPresenter.getCredential());
                            fragment.setChannelId(mPresenter.getSubscriptions().get(position-1).channelId);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();*/

                            /*RootFragment fragment = new RootFragment();
                            fragment.setCredential(mPresenter.getCredential());
                            fragment.setChannelId(mPresenter.getSubscriptions().get(position-1).channelId);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();*/
                            ChannelFragment fragment = new ChannelFragment();
                            fragment.setCredential(mPresenter.getCredential());
                            fragment.setChannelId(mPresenter.getSubscriptions().get(position-1).channelId);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                            /*VideoFragment fragment = new VideoFragment();
                            fragment.setVideoId("mmDj8b6u9G0");
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();*/
                        }
                        Log.d("POSITION", position-1+"");
                    }
                })
                .build();

        //mPresenter.onStart();
        mPresenter = new MainPresenter(this);
       /*RootFragment fragment1 = new RootFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment1, "one");
        transaction.commit();*/
        mPresenter.onStart();
        checkDemands();
        //mPresenter.makePagerAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO:
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText("This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                } else {
                    checkDemands();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    mPresenter.checkAccountName(data);
                }else{
                    hideProgress();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    checkDemands();
                }
                break;
        }
    }

    public void checkDemands(){
        mPresenter.checkDemands();
//        showProgress();
    }

    /*public void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.tab_trending);
        mTabLayout.getTabAt(1).setIcon(R.drawable.tab_subscriptions);
        mTabLayout.getTabAt(2).setIcon(R.drawable.tab_home);

    }*/

    /*public void setupPagerAdapter(ViewPagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
    }*/

    public void setupNavigationDrawer(ArrayList<SubscriptionData> someSubscriptionData){
        for(int i = 1; i < someSubscriptionData.size(); i++){
            drawerResult.addItem(new SecondaryDrawerItem()
                    .withName(someSubscriptionData.get(i).title)
                    .withIcon(someSubscriptionData.get(i).image));
        }
    }

    public void showProgress(){
       // mProgress.show();
    }

    public void hideProgress(){
        mProgress.hide();
    }

    /*public void setPage(){
        mViewPager.setCurrentItem(3);
    }*/

    /*public void turnOnViewPagerSwipe(){
        mViewPager.setOnTouchListener(null);
    }*/

    /*public void turnOffViewPagerSwipe(){
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    public void showDialog(Dialog dialog){
        dialog.show();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }


}