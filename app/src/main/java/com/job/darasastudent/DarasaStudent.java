package com.job.darasastudent;

import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Created by Job on Friday : 8/10/2018.
 */
public class DarasaStudent extends MultiDexApplication {

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate() {
        super.onCreate();
        //firebase init

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // do stuff
                    // Check if user is signed in (non-null) and update UI accordingly.

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

    }
}

