package com.example.facekilling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.javabean.MainUser;

public class ProfileActivity extends AppCompatActivity {

    private ImageView userAvatar;
    private TextView userName;
    private TextView changeAvatar;
    private ImageView backImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    public void initView(){
        userAvatar = (ImageView) findViewById(R.id.activity_profile_avatar);
        userName = (TextView) findViewById(R.id.activity_profile_name);
        changeAvatar = (TextView) findViewById(R.id.activity_profile_changeAvatar);
        backImg = (ImageView) findViewById(R.id.activity_profile_back);

        //userAvatar.setImageDrawable(getResources().getDrawable(MainUser.getInstance().getImageId()));
        userAvatar.setImageBitmap(MainUser.getInstance().getImageBitMap());
        userName.setText(MainUser.getInstance().getUser_name());
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"更换头像",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
