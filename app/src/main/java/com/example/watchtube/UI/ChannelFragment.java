package com.example.watchtube.UI;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchtube.ChannelPresenter;
import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.model.data.ChannelData;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class ChannelFragment extends Fragment implements Contract.View {

    private String mChannelId;
    private ChannelPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private TextView mTextViewTitle;
    private TextView mTextViewKind;
    private TextView mTextViewSubCount;
    private TextView mTextViewDescription;
    private ImageView mImageViewBanner;
    private ImageView mImageViewIcon;
    private ConstraintLayout mLayout;

    public void setChannelId(String channelId){
        mChannelId = channelId;
        mPresenter = new ChannelPresenter(this, mCredential, mChannelId);
        mPresenter.onStart();
    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel, container, false);
        mTextViewTitle = v.findViewById(R.id.textViewTitle);
        mTextViewKind = v.findViewById(R.id.textViewKind);
        mTextViewSubCount = v.findViewById(R.id.textViewSubCount);
        mTextViewDescription = v.findViewById(R.id.textViewDescription);
        mTextViewDescription.setMovementMethod(new ScrollingMovementMethod());
        mImageViewBanner = v.findViewById(R.id.imageViewBanner);
        mImageViewIcon = v.findViewById(R.id.imageViewIcon);
        mLayout = v.findViewById(R.id.constraintLayoutChannel);
        Log.d("CHANNEL", "CREATE");
        return v;
    }

    public void fetchChannelData(){
        mPresenter.fetchChannelData();
        Log.d("FETCH", "CREATE");
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
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }

}
