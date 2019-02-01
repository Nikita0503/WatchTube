package com.example.watchtube.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.watchtube.Contract;
import com.example.watchtube.R;
import com.example.watchtube.SearchAdapter;
import com.example.watchtube.SearchItemType;
import com.example.watchtube.SearchPresenter;
import com.example.watchtube.VideoCommentsPresenter;
import com.example.watchtube.model.VideoCommentsListCustomAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

/**
 * Created by Nikita on 30.01.2019.
 */

public class SearchFragment extends Fragment implements Contract.View {

    private String mRequest;
    private SearchPresenter mPresenter;
    private GoogleAccountCredential mCredential;
    private RecyclerView mRecyclerViewSearch;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchAdapter mSearchAdapter;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPresenter(this);
        mPresenter.onStart();
        //mPresenter = new VideoFragmentPresenter(this);
        //mPresenter.onStart();
        Log.d("SearchFragment", "fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mPresenter.setupCredential(mCredential);
        mRecyclerViewSearch = (RecyclerView) v.findViewById(R.id.RecyclerVIewSearch);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewSearch.setLayoutManager(mLayoutManager);
        mSearchAdapter = new SearchAdapter(SearchFragment.this, mCredential);
        mRecyclerViewSearch.setAdapter(mSearchAdapter);
        mPresenter.fetchSearchResults(mRequest);
        mProgressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        Sprite cubeGrid = new CubeGrid();
        mProgressBar.setIndeterminateDrawable(cubeGrid);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        return v;
    }

    public void setCredential(GoogleAccountCredential credential){
        mCredential = credential;
    }

    public void setRequest(String request){
        mRequest = request;
    }

    public void addSearchResults(ArrayList<SearchItemType> items){
        mSearchAdapter.addSearchResults(items);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }
}
