package com.example.facekilling.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int LOGIN_WHAT = 1;

    private String[] user_gender = {"male","female","unknown"};

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;
    private LogInHandle logInHandle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setDefaultAvatarsFile(getApplicationContext());
        logInHandle = new LogInHandle(this);
        initView();
    }

    public void initView(){
        this.emailText = (EditText) findViewById(R.id.input_email);
        this.passwordText = (EditText) findViewById(R.id.input_password);
        this.loginButton = (Button) findViewById(R.id.btn_login);
        this.signupLink = (TextView) findViewById(R.id.link_signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void setDefaultAvatarsFile(Context context){
        for (String s : user_gender){
            String baseFilePath = getFilesDir().getAbsolutePath() + File.separator + s +".jpg";
            File baseFile = new File(baseFilePath);
            if (!baseFile.exists()){
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(s+".jpg",MODE_PRIVATE);
                    if (s.equals(user_gender[0])) {
                        Bitmap male_bit = BitmapFactory.decodeResource(getResources(), R.drawable.male_avatar);
                        male_bit.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    } else if (s.equals(user_gender[1])) {
                        Bitmap female_bit = BitmapFactory.decodeResource(getResources(), R.drawable.female_avatar);
                        female_bit.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    } else if (s.equals(user_gender[2])) {
                        Bitmap unknown_bit = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
                        unknown_bit.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    }
                }catch (IOException ie){
                    ie.printStackTrace();
                }
            }
        }
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setEnabled(false);
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = OkHttpUtils.logIn(email,password);
                OkHttpUtils.getMainUserInfo(result);
                if (MainUser.getInstance()!=null)
                    MainUser.getInstance().setPassword(password);
                logInHandle.obtainMessage(LOGIN_WHAT,result).sendToTarget();
            }
        }).start();
    }

    private static class LogInHandle extends Handler{
        private LoginActivity loginActivity;
        private int loginResult = -1;
        public LogInHandle(LoginActivity loginActivity){
            this.loginActivity = loginActivity;
        }
        @Override
        public void handleMessage(Message msg){
            if(msg.what==LOGIN_WHAT){
                loginResult = (int) msg.obj;
                Log.i("Mlogin",String.valueOf(loginResult));
                if (loginResult > 0){
                    loginActivity.onLoginSuccess();
                }else if (loginResult == -1){
                    loginActivity.onLoginFailed("password wrong!");
                }else if (loginResult == -2){
                    loginActivity.onLoginFailed("email not found!");
                }else loginActivity.onLoginFailed();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "registered!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
            }
        }
    }


    //点击返回键
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    //登陆成功
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
        startActivity(intent);
        finish();
    }

    //登陆失败
    public void onLoginFailed(String note) {
        Toast.makeText(getBaseContext(), note, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "login failed!", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    //有效性验证
    public boolean validate() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 50) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}