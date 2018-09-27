package com.job.darasastudent.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.job.darasastudent.R;
import com.job.darasastudent.util.WebViewController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqActivity extends AppCompatActivity {

    @BindView(R.id.faq_toolbar)
    Toolbar faqToolbar;
    @BindView(R.id.faq_progressBar)
    ProgressBar faqProgressBar;
    @BindView(R.id.faq_webView)
    WebView faqWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);


        setSupportActionBar(faqToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        //String prourl = getIntent().getStringExtra(PRODUCTURL);
        String url = "http://jobgetabu.me/FAQ/";

        if (url == null){
            finish();
        }

        WebViewController webViewController = new WebViewController(faqProgressBar);

        faqWebView.setWebViewClient(webViewController);
        faqWebView.getSettings().setJavaScriptEnabled(true);
        faqWebView.loadUrl(url);

    }
}
