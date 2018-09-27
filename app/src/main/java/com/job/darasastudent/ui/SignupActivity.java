package com.job.darasastudent.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasastudent.R;
import com.job.darasastudent.model.StudentDetails;
import com.job.darasastudent.util.DoSnack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasastudent.util.Constants.STUDENTDETAILSCOL;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.signup_email)
    TextInputLayout signupEmail;
    @BindView(R.id.signup_password)
    TextInputLayout signupPassword;
    @BindView(R.id.signup_button)
    Button signupButton;
    @BindView(R.id.signup_via_google)
    LinearLayout signupViaGoogle;

    private static final String TAG = "login";
    public static final int RC_SIGN_IN = 1002;


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private DoSnack doSnack;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        doSnack = new DoSnack(this, SignupActivity.this);
    }

    @OnClick(R.id.signup_button)
    public void onSignupButtonClicked() {
    }

    @OnClick({R.id.signup_via_google,R.id.signup_via_google})
    public void onSignupViaGoogleClicked() {
    }

    private void writingToStudUsers(final SweetAlertDialog pDialog, String device_token, FirebaseUser user, String mCurrentUserid) {

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setDevicetoken(device_token);
        // Set the value of 'Users'
        DocumentReference usersRef = mFirestore.collection(STUDENTDETAILSCOL).document(mCurrentUserid);

        usersRef.set(studentDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismissWithAnimation();
                        sendToAccountSetup();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                doSnack.errorPrompt("Oops...", e.getMessage());
            }
        });
    }

    private void sendToAccountSetup() {
        Intent aIntent = new Intent(this, AccountSetupActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(aIntent);
        finish();
    }
}
