package com.example.watchtube;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;

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
import java.util.Locale;

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

    private MainActivity mMainActivity;
    private CompositeDisposable mDisposables;
    private YouTubeAPIUtils mYouTubeAPIUtils;
    public GoogleAccountCredential mCredential;
    private DemandsChecker mDemandsChecker;

    public MainPresenter(MainActivity mainActivity){
        mMainActivity = mainActivity;
        mCredential = GoogleAccountCredential.usingOAuth2(
                mMainActivity.getApplicationContext(), Arrays.asList(MainActivity.SCOPES))
                .setBackOff(new ExponentialBackOff());
        mYouTubeAPIUtils = new YouTubeAPIUtils(mMainActivity.getApplicationContext(),
                this, mCredential);
    }

    @Override
    public void onStart() {
        mDisposables = new CompositeDisposable();
    }

    public void checkDemands(){
         mDemandsChecker = new DemandsChecker(mMainActivity, this);
        if (!mDemandsChecker.isGooglePlayServicesAvailable()) {
            mDemandsChecker.acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!mDemandsChecker.isDeviceOnline()) {
            setText("No network connection available.");
        } else {
            getResultsFromApi();
        }
    }

    private void getResultsFromApi() {
        //String lCode = Locale.getDefault().getCountry();
        setText("");
        Disposable disposable = mYouTubeAPIUtils.getSubscriptionsInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<SubscriptionData>>() {
                    @Override
                    public void onSuccess(ArrayList<SubscriptionData> subscriptions) {
                            //loadImageFromUrl(defaultObject.get(0).URL);
                        mMainActivity.setupNavigationDrawer(subscriptions);
                        if(mYouTubeAPIUtils.pageToken != null){
                            getResultsFromApi();
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
                mMainActivity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        MainActivity.REQUEST_ACCOUNT_PICKER);
        }
    }

    public void makeGooglePlayServicesAvailabilityErrorDialog(GoogleApiAvailability apiAvailability,
            final int connectionStatusCode) {
        Dialog dialog = apiAvailability.getErrorDialog(
                mMainActivity,
                connectionStatusCode,
                DemandsChecker.REQUEST_GOOGLE_PLAY_SERVICES);
        mMainActivity.showDialog(dialog);
    }

    public void makePagerAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(mMainActivity.getSupportFragmentManager());
        VideoListFragment recommendedVideoListFragment = new VideoListFragment();
        recommendedVideoListFragment.setCredentials(mCredential);
        adapter.addFragment(recommendedVideoListFragment, "Trending");
        adapter.addFragment(new VideoListFragment(), "Search");
        adapter.addFragment(new VideoListFragment(), "Settings");
        mMainActivity.setupPagerAdapter(adapter);
        hideProgress();
    }

    public void showProgress(){
        mMainActivity.showProgress();
    }

    public void hideProgress(){
        mMainActivity.hideProgress();
    }

    public void setText(String text){
        mMainActivity.mOutputText.setText(text);
    }

    @Override
    public void onStop() {
        mDisposables.clear();
    }
}
