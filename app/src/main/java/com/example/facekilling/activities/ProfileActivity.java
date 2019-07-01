package com.example.facekilling.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.GetSysTime;
import com.example.facekilling.util.OkHttpUtils;
import com.example.facekilling.util.StaticConstant;

import static com.example.facekilling.util.GetBitmap.getBitmapFromSD;

public class ProfileActivity extends AppCompatActivity {

    private ImageView userAvatar;
    private TextView userName;
    private TextView changeAvatar;
    private ImageView backImg;
    private Button saveButt;
    private EditText input_userName;
    private EditText input_userAddress;
    private EditText input_email;
    private EditText input_mobile;
    private EditText input_password;
    private String changed_userName;
    private String changed_userAddress;
    private String changed_email;
    private String changed_mobile;
    private String changed_password;

    private ProfileHandler profileHandler;

    private static final String OLD_ADDRESS = "原地址：";
    private static final String OLD_PASSWORD = "原密码：";
    private static final String OLD_MOBILE = "原号码：";
    private static final String OLD_EMAIL = "原邮箱：";
    private static final String OLD_NAME = "原用户名：";
    private static final int AVATAR_WHAT = 4;
    private static final int BASE_WHAT = 5;
    private String compressImagePath = GetBitmap.DEFAULT_TEMP_PIC_BASEPATH + GetSysTime.getCurrTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        profileHandler = new ProfileHandler(this);
    }

    public void initView(){
        userAvatar = (ImageView) findViewById(R.id.activity_profile_avatar);
        userName = (TextView) findViewById(R.id.activity_profile_name);
        changeAvatar = (TextView) findViewById(R.id.activity_profile_changeAvatar);
        backImg = (ImageView) findViewById(R.id.activity_profile_back);
        saveButt = (Button) findViewById(R.id.avatar_profile_btn_save);
        input_userName = (EditText) findViewById(R.id.avatar_profile_input_name);
        input_email = (EditText) findViewById(R.id.avatar_profile_input_email);
        input_mobile = (EditText) findViewById(R.id.avatar_profile_input_mobile);
        input_password = (EditText) findViewById(R.id.avatar_profile_input_password);
        input_userAddress = (EditText) findViewById(R.id.avatar_profile_input_address);

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
                changeAvatar();
            }
        });
        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBaseInfo();
            }
        });

        input_userName.setHint(OLD_NAME + MainUser.getInstance().getUser_name());
        input_userAddress.setHint(OLD_ADDRESS + MainUser.getInstance().getAddress());
        input_email.setHint(OLD_EMAIL + MainUser.getInstance().getEmail());
        input_password.setHint(OLD_PASSWORD + MainUser.getInstance().getPassword());
        input_mobile.setHint(OLD_MOBILE + MainUser.getInstance().getPhone());

    }

    public void changeBaseInfo(){
        changed_userName = input_userName.getText().toString();
        changed_email = input_email.getText().toString();
        changed_mobile = input_mobile.getText().toString();
        changed_password = input_password.getText().toString();
        changed_userAddress = input_userAddress.getText().toString();

        if (changed_userName.equals(MainUser.getInstance().getUser_name())){
            changed_userName = "";
        }
        else if(!changed_userName.equals(""))
            MainUser.getInstance().setUser_name(changed_userName);
        if (changed_email.equals(MainUser.getInstance().getEmail())){
            changed_email = "";
        }
        else if(!changed_email.equals(""))
            MainUser.getInstance().setEmail(changed_email);
        if (changed_mobile.equals(MainUser.getInstance().getPhone())){
            changed_mobile = "";
        }
        else if(!changed_mobile.equals(""))
            MainUser.getInstance().setUser_name(changed_mobile);
        if (changed_password.equals(MainUser.getInstance().getPassword())){
            changed_password = "";
        }
        else if (!changed_password.equals(""))
            MainUser.getInstance().setPassword(changed_password);
        if (changed_userAddress.equals(MainUser.getInstance().getAddress())){
            changed_userAddress = "";
        }
        else if (!changed_userAddress.equals(""))
            MainUser.getInstance().setAddress(changed_userAddress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                 profileHandler.obtainMessage(BASE_WHAT,OkHttpUtils.changeAccountInfo(MainUser.getInstance().getUser_id(),MainUser.getInstance().getEmail(),
                         MainUser.getInstance().getPassword(),changed_email,changed_password,changed_userName,"",
                         "",changed_mobile,changed_userAddress)).sendToTarget();
            }
        }).start();
    }

    public void changeAvatar(){
        //从本地图库寻找
        Intent intent_2 = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent_2, StaticConstant.GETBITMAP_FROM_SD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                final String imagePath = c.getString(columnIndex);
                final Bitmap imgOriginBitmap = GetBitmap.getBitmapFromSD(imagePath);
                c.close();
                if (imgOriginBitmap!=null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GetBitmap.transImage(imgOriginBitmap, compressImagePath, 480, 640, 50);
                            profileHandler.obtainMessage(AVATAR_WHAT,OkHttpUtils.changeAccountInfo(MainUser.getInstance().getUser_id(),
                                    MainUser.getInstance().getEmail(), MainUser.getInstance().getPassword(),
                                    "", "", "", compressImagePath,
                                    "", "", "")).sendToTarget();
                        }
                    }).start();
                }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private class ProfileHandler extends Handler{
        private ProfileActivity profileActivity;

        public ProfileHandler(ProfileActivity profileActivity){
            this.profileActivity = profileActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case AVATAR_WHAT: {
                    if ((boolean) msg.obj) {
                        MainUser.getInstance().setImageBitMap(GetBitmap.getBitmapFromSD(compressImagePath));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userAvatar.setImageBitmap(MainUser.getInstance().getImageBitMap());
                            }
                        });
                        IndexActivity.getIndexActivity().monitorSidebar();
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                }
                break;
                case BASE_WHAT:{
                    if ((boolean) msg.obj){
                        IndexActivity.getIndexActivity().monitorSidebar();
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                }
                break;
                default: break;
            }
        }
    }

}
