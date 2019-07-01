package com.example.facekilling.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.OkHttpUtils;

import org.json.JSONException;

import java.io.IOException;

public class AddFriendsActivity extends AppCompatActivity {

    private TopBar topBar;
    private EditText emailEdit;
    private TextView addButtTv;

    private AddFriendHandler addFriendHandler;

    private static final int ADDFRIEND_WHAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        addFriendHandler = new AddFriendHandler(this);
        initView();
    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.activity_add_friend_topBar);
        emailEdit = (EditText) findViewById(R.id.activity_add_friend_editText);
        addButtTv = (TextView) findViewById(R.id.activity_add_friend_addButt);

        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
               onBackPressed();
            }

            @Override
            public void rightClicked() {
            }
        });

        addButtTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }

    public void addFriend(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String email = emailEdit.getText().toString();
                if(!email.equals("")){
                    try {
                        addFriendHandler.obtainMessage(ADDFRIEND_WHAT,
                                OkHttpUtils.postAddFriend(MainUser.getInstance(),email)).sendToTarget(); ;
                    }catch (IOException ie){
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private class AddFriendHandler extends Handler {
        private AddFriendsActivity addFriendsActivity;

        public AddFriendHandler(AddFriendsActivity addFriendsActivity){
            this.addFriendsActivity = addFriendsActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ADDFRIEND_WHAT){
                int result = (int) msg.obj;
                if(result==0){
                    Toast toast = Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                    emailEdit.setText("");
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"failed!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                    emailEdit.setText("");
                }
            }
        }
    }
}
