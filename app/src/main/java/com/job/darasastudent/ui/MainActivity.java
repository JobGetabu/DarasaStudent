package com.job.darasastudent.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.job.darasastudent.R;
import com.job.darasastudent.util.DoSnack;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;
    @BindView(R.id.main_bar)
    BottomAppBar bar;
    @BindView(R.id.main_no_class)
    View noClassView;
    @BindView(R.id.main_toolbartitle)
    TextView mainToolbartitle;

    private Date mDate;

    private static final String TAG = "main";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirestoreRecyclerAdapter adapter;
    private Query mQuery = null;

    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(bar);

        //subtitle
        showDateOfClasses(Calendar.getInstance());

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    sendToLogin();
                    finish();
                } else {

                    userId = mAuth.getCurrentUser().getUid();

                    //classListQuery(Calendar.getInstance());

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        doSnack = new DoSnack(this, MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    finish();
                    sendToLogin();
                } else {
                    userId = mAuth.getCurrentUser().getUid();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @OnClick(R.id.main_fab)
    public void onFabClicked() {
        startActivity(new Intent(this, ScanActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calender, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavDrawerFragment.show(getSupportFragmentManager(), BottomNavigationDrawerFragment.TAG);
                break;

            case R.id.menu_calender:

                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        mDate = myCalendar.getTime();
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);


                        Toast.makeText(MainActivity.this, sdf.format(myCalendar.getTime()), Toast.LENGTH_SHORT).show();
                    }
                };

                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
        }

        return true;
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void showDateOfClasses(Calendar c) {

        DateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM d, ''yy"); //Wed, Jul 4, '18

        mainToolbartitle.setText("Showing: " + dateFormat2.format(c.getTime()));
        //mainToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);

        int day = c.get(Calendar.DAY_OF_WEEK);
        int daydate = c.get(Calendar.DAY_OF_MONTH);
    }
}
