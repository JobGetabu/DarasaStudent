package com.job.darasastudent.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasastudent.R;
import com.job.darasastudent.model.StudentDetails;
import com.job.darasastudent.util.AppStatus;
import com.job.darasastudent.util.DoSnack;
import com.job.darasastudent.viewmodel.AccountSetupViewModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasastudent.util.Constants.STUDENTDETAILSCOL;


public class AccountSetupActivity extends AppCompatActivity {

    @BindView(R.id.setup_toolbar)
    Toolbar setupToolbar;
    @BindView(R.id.setup_firstname)
    TextInputLayout setupFirstname;
    @BindView(R.id.setup_lastname)
    TextInputLayout setupLastname;
    @BindView(R.id.setup_school)
    TextInputLayout setupSchool;
    @BindView(R.id.setup_department)
    TextInputLayout setupDepartment;
    @BindView(R.id.setup_btn)
    TextView setupBtn;
    @BindView(R.id.setup_regno)
    TextInputLayout setupRegno;
    @BindView(R.id.setup_course_btn)
    MaterialButton setupCourseBtn;
    @BindView(R.id.setup_course)
    TextInputLayout setupCourse;

    private DoSnack doSnack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private AccountSetupViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
        ButterKnife.bind(this);

        setSupportActionBar(setupToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, AccountSetupActivity.this);

        // View model
        AccountSetupViewModel.Factory factory = new AccountSetupViewModel.Factory(
                AccountSetupActivity.this.getApplication(), mAuth, mFirestore);

        model = ViewModelProviders.of(AccountSetupActivity.this, factory)
                .get(AccountSetupViewModel.class);

        //ui observer
        uiObserver();
    }

    @OnClick(R.id.setup_btn)
    public void onViewSetupClicked() {

        if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {

            doSnack.showSnackbar("You're offline", "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onViewSetupClicked();
                }
            });

            return;
        }

        if (validate()) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
            pDialog.setTitleText("Just a moment...");
            pDialog.setCancelable(true);
            pDialog.show();

            String fname = setupFirstname.getEditText().getText().toString();
            String lname = setupLastname.getEditText().getText().toString();
            String school = setupSchool.getEditText().getText().toString();
            String dept = setupDepartment.getEditText().getText().toString();
            String regno = setupRegno.getEditText().getText().toString();

            Map<String, Object> studMap = new HashMap<>();
            studMap.put("firstname", fname);
            studMap.put("lastname", lname);
            studMap.put("school", school);
            studMap.put("department", dept);
            studMap.put("regnumber", regno);

            // Set the value of 'Users'
            DocumentReference usersRef = mFirestore.collection(STUDENTDETAILSCOL).document(mAuth.getCurrentUser().getUid());

            usersRef.update(studMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.setCancelable(true);
                            pDialog.setTitleText("Saved Successfully");
                            pDialog.setContentText("You're now set");
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    sendToMain();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                    doSnack.errorPrompt("Oops...", e.getMessage());
                }
            });
        }
    }

    private void sendToMain() {
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    private boolean validate() {
        boolean valid = true;

        String fname = setupFirstname.getEditText().getText().toString();
        String lname = setupLastname.getEditText().getText().toString();
        String school = setupSchool.getEditText().getText().toString();
        String dept = setupDepartment.getEditText().getText().toString();
        String regno = setupRegno.getEditText().getText().toString();
        String course = setupCourse.getEditText().getText().toString();

        if (fname.isEmpty()) {
            setupFirstname.setError("enter name");
            valid = false;
        } else {
            setupFirstname.setError(null);
        }

        if (lname.isEmpty()) {
            setupLastname.setError("enter name");
            valid = false;
        } else {
            setupLastname.setError(null);
        }

        if (school.isEmpty()) {
            setupSchool.setError("enter school");
            valid = false;
        } else {
            setupSchool.setError(null);
        }

        if (dept.isEmpty()) {
            setupDepartment.setError("enter dept");
            valid = false;
        } else {
            setupDepartment.setError(null);
        }

        if (regno.isEmpty()) {
            setupRegno.setError("enter reg no");
            valid = false;
        } else {
            setupDepartment.setError(null);
        }

        if (course.isEmpty()) {
            setupCourse.setError("enter course");
            valid = false;
        } else {
            setupCourse.setError(null);
        }

        return valid;
    }

    private void uiObserver() {
        model.getLecUserMediatorLiveData().observe(this, new Observer<StudentDetails>() {
            @Override
            public void onChanged(@Nullable StudentDetails studUser) {
                if (studUser != null) {
                    setupFirstname.getEditText().setText(studUser.getFirstname());
                    setupLastname.getEditText().setText(studUser.getLastname());
                    setupSchool.getEditText().setText(studUser.getSchool());
                    setupDepartment.getEditText().setText(studUser.getDepartment());
                    setupRegno.getEditText().setText(studUser.getDepartment());
                    setupCourse.setVisibility(View.VISIBLE);
                    setupCourse.getEditText().setText(studUser.getCourse());
                }
            }
        });
    }

    @OnClick(R.id.setup_course_btn)
    public void onViewClicked() {
    }
}
