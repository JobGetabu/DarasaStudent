package com.job.darasastudent.ui;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.job.darasastudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifActivity extends AppCompatActivity {

    @BindView(R.id.ce_imgBtnClose)
    ImageButton ceImgBtnClose;
    @BindView(R.id.ce_dismiss)
    MaterialButton ceDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notif);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ce_imgBtnClose)
    public void onCeImgBtnCloseClicked() {
        finish();
    }

    @OnClick(R.id.ce_dismiss)
    public void onCeDismissClicked() {
        finish();
    }
}
