package com.job.darasastudent.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasastudent.R;
import com.job.darasastudent.model.Notif;
import com.job.darasastudent.util.NotifViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasastudent.util.Constants.COURSE_PREF_NAME;
import static com.job.darasastudent.util.Constants.CURRENT_YEAROFSTUDY_PREF_NAME;
import static com.job.darasastudent.util.Constants.NOTIFICATIONCOL;

public class NotifActivity extends AppCompatActivity {

    private static final String TAG = "notif";

    @BindView(R.id.ce_imgBtnClose)
    ImageButton ceImgBtnClose;
    @BindView(R.id.ce_dismiss)
    MaterialButton ceDismiss;
    @BindView(R.id.ce_list)
    RecyclerView ceList;
    @BindView(R.id.ce_emptynotif)
    ConstraintLayout ceEmptynotif;
    @BindView(R.id.ce_notif_listview)
    ConstraintLayout ceNotifListview;

    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onStart() {
        super.onStart();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);

        setUpList();
    }

    @OnClick(R.id.ce_imgBtnClose)
    public void onCeImgBtnCloseClicked() {
        finish();
    }

    @OnClick(R.id.ce2_imgBtnClose)
    public void onCe2ImgBtnCloseClicked() {
        finish();
    }

    @OnClick(R.id.ce_dismiss)
    public void onCeDismissClicked() {
        finish();
    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ceList.setLayoutManager(layoutManager);
        ceList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        ceList.addItemDecoration(itemDecoration);
        ceList.setItemAnimator(new DefaultItemAnimator());
    }

    private void setUpList() {

        initRecycler();

        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        String topicname = preferences.getString(COURSE_PREF_NAME, "").replace(" ", "")
                + preferences.getString(CURRENT_YEAROFSTUDY_PREF_NAME, "");

        Query query = FirebaseFirestore.getInstance().collection(NOTIFICATIONCOL)
                .whereEqualTo("topic", topicname)
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(7);

        FirestoreRecyclerOptions<Notif> options = new FirestoreRecyclerOptions.Builder<Notif>()
                .setQuery(query, Notif.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Notif, NotifViewHolder>(options) {

            @NonNull
            @Override
            public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_notif, parent, false);

                return new NotifViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NotifViewHolder holder, int position, @NonNull Notif model) {

                holder.setUpUI(model,NotifActivity.this);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();

                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    ceNotifListview.setVisibility(View.GONE);
                    ceEmptynotif.setVisibility(View.VISIBLE);
                } else {
                    ceNotifListview.setVisibility(View.VISIBLE);
                    ceEmptynotif.setVisibility(View.GONE);
                }
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        ceList.setAdapter(adapter);
    }
}
