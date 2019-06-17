package com.example.facekilling.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;

public class Index_TwoActivity extends Fragment {

    private Context mContext;
    private View mView;
    private TopBar topBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.activity_index__two,container,false);
        initView();
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                Toast.makeText(getContext(),"FaceKilling",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClicked() {
                Toast.makeText(getContext(),"FaceKilling",Toast.LENGTH_SHORT).show();
            }
        });

        return mView;
    }

    public void initView(){
        topBar = (TopBar) mView.findViewById(R.id.indexTwoTopBar);
    }
}
