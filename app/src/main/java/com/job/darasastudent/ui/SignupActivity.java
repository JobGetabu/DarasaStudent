package com.job.darasastudent.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.job.darasastudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.signup_email)
    TextInputLayout signupEmail;
    @BindView(R.id.signup_password)
    TextInputLayout signupPassword;
    @BindView(R.id.signup_button)
    Button signupButton;
    @BindView(R.id.signup_via_google)
    LinearLayout signupViaGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_button)
    public void onSignupButtonClicked() {
    }

    @OnClick({R.id.signup_via_google,R.id.signup_via_google})
    public void onSignupViaGoogleClicked() {
    }
}
