package com.example.watchtube;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;

import com.example.watchtube.UI.ChannelFragment;
import com.example.watchtube.model.APIUtils.YouTubeAPIUtils;
import com.example.watchtube.UI.MainActivity;
import com.example.watchtube.UI.VideoListFragment;
import com.example.watchtube.model.DemandsChecker;
import com.example.watchtube.model.data.SubscriptionData;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;

/**
 * Created by Nikita on 22.08.2018.
 */

public class MainPresenter implements Contract.Presenter{

    private List<SubscriptionData> mSubscriptions;
    private MainActivity mActivity;
    private ChannelFragment mChannelFragment;
    private CompositeDisposable mDisposables;
    private YouTubeAPIUtils mYouTubeAPIUtils;
    public GoogleAccountCredential mCredential;
    private DemandsChecker mDemandsChecker;

    public MainPresenter(MainActivity mainActivity){
        mActivity = mainActivity;
        mCredential = GoogleAccountCredential.usingOAuth2(
                mActivity.getApplicationContext(), Arrays.asList(MainActivity.SCOPES))
                .setBackOff(new ExponentialBackOff());
        mYouTubeAPIUtils = new YouTubeAPIUtils(mActivity.getApplicationContext(),
                this, mCredential);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
    }

    public void checkDemands(){
         mDemandsChecker = new DemandsChecker(mActivity, this);
        if (!mDemandsChecker.isGooglePlayServicesAvailable()) {
            mDemandsChecker.acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!mDemandsChecker.isDeviceOnline()) {
            setText("No network connection available.");
        } else {
            fetchSubscribesList();
        }
    }

    private void fetchSubscribesList() {
        setText("");
        Disposable disposable = mYouTubeAPIUtils.getSubscriptionsInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<SubscriptionData>>() {
                    @Override
                    public void onSuccess(ArrayList<SubscriptionData> subscriptions) {
                            //loadImageFromUrl(defaultObject.get(0).URL);

                        mActivity.setupNavigationDrawer(subscriptions);
                        mSubscriptions = subscriptions;
                        if(mYouTubeAPIUtils.pageToken != null){
                            fetchSubscribesList();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        setText("Error");
                    }
                });
        mDisposables.add(disposable);
    }

    public void fetchSelectedChannelData(int position){
        mChannelFragment.setCredential(mCredential);
        mChannelFragment.setChannelId(mSubscriptions.get(position).channelId);
        mChannelFragment.fetchChannelData();
        //mActivity.startActivity(intent);
        //startActivity(Intent) channelActivity
    }

    public void checkAccountName(Intent data){
        String accountName =
                data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (accountName != null) {
            setSelectedAccountName(accountName);
        }
    }

    public void setSelectedAccountName(String accountName){
        mDemandsChecker.rememberSelectedAccountName(accountName);
        mCredential.setSelectedAccountName(accountName);
        checkDemands();
    }

    @AfterPermissionGranted(MainActivity.REQUEST_PERMISSION_GET_ACCOUNTS)
    public void chooseAccount() {
            String accountName = mDemandsChecker.getSelectedAccountName();
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                checkDemands();
            } else {
                mActivity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        MainActivity.REQUEST_ACCOUNT_PICKER);
        }
    }

    public void makeGooglePlayServicesAvailabilityErrorDialog(GoogleApiAvailability apiAvailability,
            final int connectionStatusCode) {
        Dialog dialog = apiAvailability.getErrorDialog(
                mActivity,
                connectionStatusCode,
                DemandsChecker.REQUEST_GOOGLE_PLAY_SERVICES);
        mActivity.showDialog(dialog);
    }

    public void makePagerAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager());
        VideoListFragment recommendedVideoListFragment = new VideoListFragment();
        recommendedVideoListFragment.setCredentials(mCredential);
        adapter.addFragment(recommendedVideoListFragment, "Trending");
        mChannelFragment = new ChannelFragment();
        adapter.addFragment(mChannelFragment, "Subscribe");
        adapter.addFragment(new VideoListFragment(), "Settings");
        mActivity.setupPagerAdapter(adapter);
        mActivity.setupTabIcons();
        hideProgress();
    }

    public void showProgress(){
        mActivity.showProgress();
    }

    public void hideProgress(){
        mActivity.hideProgress();
    }

    public void setText(String text){
        mActivity.mOutputText.setText(text);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
