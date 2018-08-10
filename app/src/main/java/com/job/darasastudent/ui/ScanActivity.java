package com.job.darasastudent.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.job.darasastudent.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanActivity extends AppCompatActivity {

    //TODO create layout for different size phones :activity_viewfinder

    @BindView(R.id.cameraView)
    SurfaceView cameraView;
    @BindView(R.id.scannerBar)
    View scannerBar;
    @BindView(R.id.scannerLayout)
    LinearLayout scannerLayout;
    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;

    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfinder);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        animateLine();
    }

    private void animateLine() {

        animator = null;

        ViewTreeObserver vto = scannerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                scannerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                scannerLayout.getViewTreeObserver().
                        removeOnGlobalLayoutListener(this);

                float destination = (float) (scannerLayout.getY() +
                        scannerLayout.getHeight());

                animator = ObjectAnimator.ofFloat(scannerBar, "translationY",
                        scannerLayout.getY(),
                        destination);

                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(3000);
                animator.start();

            }
        });
    }
}
