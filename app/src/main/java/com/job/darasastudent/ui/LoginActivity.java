package com.job.darasastudent.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.job.darasastudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    TextInputLayout loginEmail;
    @BindView(R.id.login_password)
    TextInputLayout loginPassword;
    @BindView(R.id.forgotpass)
    TextView forgotpass;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_via_google)
    LinearLayout loginViaGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_xml);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.forgotpass)
    public void onForgotpassClicked() {
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
    }

    @OnClick({R.id.login_via_google,R.id.login_via_google_image})
    public void onLoginViaGoogleClicked() {
    }

    private boolean validate() {
        boolean valid = true;

        String email = loginEmail.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        if (email.isEmpty() || !isEmailValid(email)) {
            loginEmail.setError("enter valid email");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            loginPassword.setError("at least 6 characters");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
