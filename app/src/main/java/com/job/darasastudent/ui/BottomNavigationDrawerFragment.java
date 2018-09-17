package com.job.darasastudent.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasastudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Job on Monday : 9/3/2018.
 */
public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BtmNavFrag";
    private static final int REQUEST_INVITE = 34655;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    Unbinder unbinder;

    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    private ProgressDialog progressDialog;
    //private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_main_bottomsheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //firebase
        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        /* View model
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getActivity().getApplication(), auth, mFirestore);

        mViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(MainViewModel.class);

        */
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    int id = item.getItemId();
                    switch (id) {
                        case R.id.nav_home:
                            dismiss();
                            return true;

                        case R.id.nav_logout:
                            Toast.makeText(getContext(), "Signing you out", Toast.LENGTH_SHORT).show();

                            // user is now signed out
                            auth.signOut();
                            dismiss();
                            return true;


                        case R.id.nav_share:
                            sendToInviteScreen();
                            dismiss();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //Todo share intent with any app

    private void sendToInviteScreen() {

    }
}