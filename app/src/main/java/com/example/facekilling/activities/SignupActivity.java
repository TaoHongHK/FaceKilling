package com.example.facekilling.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.OkHttpUtils;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int SIGN_WHAT = 2;

    private EditText nameText;
    private EditText emailText;
    private EditText addressText;
    private EditText passwordText;
    private EditText mobileText;
    private EditText reEnterPasswordText;
    private EditText genderText;
    private Button signupButton;
    private TextView loginLink;
    private SignUpHandle signUpHandle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpHandle = new SignUpHandle(this);
        initView();
    }

    private void initView(){
        this.nameText = (EditText) findViewById(R.id.input_name);
        this.emailText = (EditText) findViewById(R.id.input_email);
        this.addressText = (EditText) findViewById(R.id.input_address);
        this.mobileText = (EditText) findViewById(R.id.input_mobile);
        this.genderText = (EditText) findViewById(R.id.input_gender);
        this.passwordText = (EditText) findViewById(R.id.input_password);
        this.reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        this.signupButton = (Button) findViewById(R.id.btn_signup);
        this.loginLink = (TextView) findViewById(R.id.link_login);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);

        final String name = nameText.getText().toString();
        final String address = addressText.getText().toString();
        final String email = emailText.getText().toString();
        final String mobile = mobileText.getText().toString();
        final String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        final String gender = genderText.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                signUpHandle.obtainMessage(SIGN_WHAT,
                        OkHttpUtils.SignUp( name,email,password,mobile,gender,address,
                                GetBitmap.getDefaultHead(getApplicationContext(),gender))).sendToTarget();
            }
        }).start();

    }

    private static class SignUpHandle extends Handler {
        private SignupActivity signupActivity;
        private int signUpResult = -1;
        public SignUpHandle(SignupActivity signupActivity){
            this.signupActivity = signupActivity;
        }
        @Override
        public void handleMessage(Message msg){
            if(msg.what==SIGN_WHAT){
                signUpResult = (int) msg.obj;
                Log.d("Mlogin",String.valueOf(signUpResult));
                if (signUpResult>0){
                    signupActivity.onSignupSuccess();
                }else if(signUpResult == -1) {
                    signupActivity.onSignupFailed("please choose gender from 'male','female' and 'unknown'");
                }
                else if (signUpResult==-2){
                    signupActivity.onSignupFailed("email has been used");
                }else signupActivity.onSignupFailed();
            }

        }
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        SignupActivity.this.setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "register failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public void onSignupFailed(String note) {
        Toast.makeText(getBaseContext(), note, Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        String gender = genderText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            addressText.setError(null);
        }

        if (gender.isEmpty()||!(gender.equals("male")||gender.equals("female")||gender.equals("unknown"))){
            genderText.setError("Enter Valid gender");
            valid = false;
        } else {
            genderText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=11) {
            mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 50) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 50 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
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