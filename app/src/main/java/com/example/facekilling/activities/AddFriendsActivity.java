package com.example.facekilling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;

public class AddFriendsActivity extends AppCompatActivity {

    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initView();
    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.activity_add_friend_topBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
               onBackPressed();
            }

            @Override
            public void rightClicked() {

            }
        });
    }
}
