package com.example.watchtube;

import com.google.api.services.youtube.YouTubeScopes;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import android.accounts.AccountManager;
import android.app.Dialog;
        import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
        import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, Contract.View {
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final String BUTTON_TEXT = "Call YouTube Data API";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };
    public MainPresenter mMainPresenter;
    public CompositeDisposable disposables;
    private Drawer.Result drawerResult;
    public TextView mOutputText;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    public ProgressDialog mProgress;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //NEXT PAGE TOKEN (Subscriptions)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOutputText = findViewById(R.id.textView);
        mOutputText.setText(
                "Click the \'" + BUTTON_TEXT +"\' button to test the API.");
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling YouTube Data API ...");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

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
                .build();
        mMainPresenter = new MainPresenter(this);
        mMainPresenter.onStart();
        checkDemands();
    }

    @Override
    public void onStart() {
        super.onStart();
     //
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
                             mMainPresenter.checkAccountName(data);
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
        mMainPresenter.checkDemands();
        showProgress();
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.tab_home);
        mTabLayout.getTabAt(1).setIcon(R.drawable.tab_subscriptions);
        mTabLayout.getTabAt(2).setIcon(R.drawable.tab_trending);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Home");
        adapter.addFragment(new OneFragment(), "Trending");
        adapter.addFragment(new OneFragment(), "Subscriptions");
        viewPager.setAdapter(adapter);
    }

     public void setupNavigationDrawer(ArrayList<SomeSubscriptionData> someSubscriptionData){

         for(int i = 1; i < someSubscriptionData.size(); i++){
             drawerResult.addItem(new SecondaryDrawerItem()
                .withName(someSubscriptionData.get(i).title)
                .withIcon(someSubscriptionData.get(i).image));
         }


     }

    public void showProgress(){
        mProgress.show();
    }

    public void hideProgress(){
        mProgress.hide();
    }

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
        mMainPresenter.onStop();
    }


}
