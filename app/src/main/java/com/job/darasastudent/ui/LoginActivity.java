package com.job.darasastudent.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.job.darasastudent.R;
import com.job.darasastudent.util.AppStatus;
import com.job.darasastudent.util.DoSnack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasastudent.util.Constants.STUDENTDETAILSCOL;

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

    private static final String TAG = "login";


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_xml);
        ButterKnife.bind(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            sendToMain();
        }
    }

    @OnClick(R.id.forgotpass)
    public void onForgotpassClicked() {
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {

        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginButtonClicked();
                }
            });

            return;
        }


        String email = loginEmail.getEditText().getText().toString();
        String password = loginPassword.getEditText().getText().toString();

        if (validate()) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
            pDialog.setTitleText("Logging in...");
            pDialog.setCancelable(false);
            pDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> authtask) {
                            if (authtask.isSuccessful()) {

                                //login successful

                                //update device token

                                String devicetoken = FirebaseInstanceId.getInstance().getToken();
                                String mCurrentUserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                // Set the value of 'Users'
                                DocumentReference usersRef = mFirestore.collection(STUDENTDETAILSCOL).document(mCurrentUserid);

                                usersRef.update("devicetoken", devicetoken)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pDialog.dismissWithAnimation();
                                                sendToMain();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pDialog.dismiss();
                                        doSnack.errorPrompt("Oops...", e.getMessage());
                                    }
                                });


                            } else {
                                pDialog.dismiss();
                                doSnack.UserAuthToastExceptions(authtask);
                            }
                        }
                    });
        }

    }

    @OnClick({R.id.login_via_google,R.id.login_via_google_image})
    public void onLoginViaGoogleClicked() {
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
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
