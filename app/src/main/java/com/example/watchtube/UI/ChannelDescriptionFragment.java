package com.example.watchtube.UI;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.model.data.ChannelData;
import com.example.watchtube.ChannelDescriptionPresenter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


public class ChannelDescriptionFragment extends Fragment implements Contract.View {

    private String mChannelId;
    private ChannelDescriptionPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private TextView mTextViewTitle;
    private TextView mTextViewKind;
    private TextView mTextViewSubCount;
    private TextView mTextViewDescription;
    private Button mButtonSubscribe;
    private ImageView mImageViewBanner;
    private ImageView mImageViewIcon;
    private ConstraintLayout mLayout;
    private ProgressBar mProgressBar;

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setChannelId(String channelId){
        mChannelId = channelId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChannelDescriptionPresenter(this);
        mPresenter.onStart();
        setRetainInstance(true);
        Log.d("OnStart", "123");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel_descripiton, container, false);
        mTextViewTitle = v.findViewById(R.id.textViewTitle);
        mTextViewKind = v.findViewById(R.id.textViewKind);
        mTextViewSubCount = v.findViewById(R.id.textViewSubCount);
        mTextViewDescription = v.findViewById(R.id.textViewDescription);
        mTextViewDescription.setMovementMethod(new ScrollingMovementMethod());
        mButtonSubscribe = v.findViewById(R.id.buttonSubscribe);
        mButtonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
            }
        });
        mImageViewBanner = v.findViewById(R.id.imageViewBanner);
        mImageViewIcon = v.findViewById(R.id.imageViewIcon);
        mLayout = v.findViewById(R.id.constraintLayoutChannelDescription);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);

        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Log.d("CHANNEL", "CREATE");

        fetchChannelData();
        return v;
    }

    private void fetchChannelData(){
        mPresenter.setupCredential(mCredential);
        mPresenter.setupChannelId(mChannelId);
        mPresenter.fetchChannelData();
        mPresenter.isSubscribed();
    }

    public void setSubStatus(boolean isSubscribed){
        if(isSubscribed) {
            mButtonSubscribe.setText("Unsubscribe");
        }else{
            mButtonSubscribe.setText("Subscribe!");
        }
    }

    public void showChannelData(ChannelData channelData){

        mLayout.setVisibility(View.VISIBLE);
        mTextViewTitle.setText(channelData.title);
        mTextViewKind.setText(channelData.kind);
        mTextViewSubCount.setText(String.valueOf(channelData.subscriptionsCount)+" subscribers");
        mTextViewDescription.setText(channelData.description);
        mImageViewBanner.setImageDrawable(channelData.imageBanner);
        mImageViewIcon.setImageDrawable(channelData.imageIcon);
        Log.d("CHANNEL", "SHOW");
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
        Log.d("OnStop", "+");
    }


}
