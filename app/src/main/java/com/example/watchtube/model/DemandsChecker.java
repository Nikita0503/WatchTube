package com.example.watchtube.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.watchtube.MainPresenter;
import com.example.watchtube.UI.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.services.youtube.YouTubeScopes;

/**
 * Created by Nikita on 22.08.2018.
 */

public class DemandsChecker{

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String BUTTON_TEXT = "Call YouTube Data API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };

    MainPresenter mMainPresenter;
    MainActivity mMainActivity;

    public DemandsChecker(MainActivity mainActivity, MainPresenter mainPresenter){
        mMainActivity = mainActivity;
        mMainPresenter = mainPresenter;
    }

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mMainActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(mMainActivity.getApplicationContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(mMainActivity.getApplicationContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            mMainPresenter.makeGooglePlayServicesAvailabilityErrorDialog(apiAvailability, connectionStatusCode);
        }
    }

    public void rememberSelectedAccountName(String accountName){
        SharedPreferences settings =
                mMainActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MainActivity.PREF_ACCOUNT_NAME, accountName);
        editor.apply();
    }

    public String getSelectedAccountName(){
        String accountName = mMainActivity.getPreferences(Context.MODE_PRIVATE)
                .getString(MainActivity.PREF_ACCOUNT_NAME, null);
        return accountName;
    }




}
