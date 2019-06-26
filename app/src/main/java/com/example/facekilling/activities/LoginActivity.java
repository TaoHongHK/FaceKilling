package com.example.facekilling.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.javabean.MainUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
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
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void initView(){
        this.emailText = (EditText) findViewById(R.id.input_email);
        this.passwordText = (EditText) findViewById(R.id.input_password);
        this.loginButton = (Button) findViewById(R.id.btn_login);
        this.signupLink = (TextView) findViewById(R.id.link_signup);
    }


    public void login() {
        Log.d(TAG, "Login");
        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        //创建登录用户类
//        MainUser mainUser = MainUser.getInstance(email,password);
        MainUser mainUser = MainUser.newInstance("帅锅",R.drawable.picture_13,"12345678@163.com");
        loginButton.setEnabled(false);
        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO: authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/

        onLoginSuccess();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
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
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
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