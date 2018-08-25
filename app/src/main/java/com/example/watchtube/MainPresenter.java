package com.example.watchtube;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.watchtube.APIUtils.YouTubeAPIUtils;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Nikita on 22.08.2018.
 */

public class MainPresenter implements Contract.Presenter{

    private MainActivity mMainActivity;
    private CompositeDisposable mDisposables;
    private YouTubeAPIUtils mYouTubeAPIUtils;
    private GoogleAccountCredential mCredential;
    private DemandsChecker mDemandsChecker;

    public MainPresenter(MainActivity mainActivity){
        mMainActivity = mainActivity;
        mCredential = GoogleAccountCredential.usingOAuth2(
                mMainActivity.getApplicationContext(), Arrays.asList(MainActivity.SCOPES))
                .setBackOff(new ExponentialBackOff());
        mYouTubeAPIUtils = new YouTubeAPIUtils(mMainActivity.getApplicationContext(),this, mCredential);
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
        setText("");
        Disposable disposable = mYouTubeAPIUtils.getSubscriptionsInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<SomeSubscriptionData>>() {
                    @Override
                    public void onSuccess(ArrayList<SomeSubscriptionData> subscriptions) {
                            //loadImageFromUrl(defaultObject.get(0).URL);
                        mMainActivity.setupNavigationDrawer(subscriptions);
                        hideProgress();
                    }
                    @Override
                    public void onError(Throwable e) {
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
