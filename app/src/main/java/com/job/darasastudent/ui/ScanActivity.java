package com.job.darasastudent.ui;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;
import com.job.darasastudent.R;
import com.job.darasastudent.model.QRParser;
import com.job.darasastudent.scanview.CodeScannerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;
    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrCodeReaderView;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerView;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfinder);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        gson = new Gson();

        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        //qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

        scannerView.setQRCodeReaderView(qrCodeReaderView);

    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        QRParser qrParser = new QRParser().gsonToQRParser(gson, text);

        final SweetAlertDialog pDialog = new SweetAlertDialog(ScanActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));

        failScanLocation(pDialog, qrParser);
    }

    private void successScan(final SweetAlertDialog pDialog, QRParser qrParser) {
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Confirmed :" + qrParser.getUnitname() + " \n" + qrParser.getUnitcode() + "\n Location: proximity ON");
        pDialog.setCancelable(false);
        pDialog.show();

        qrCodeReaderView.stopCamera();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                finish();
            }
        });
    }

    private void failScanLocation(final SweetAlertDialog pDialog, QRParser qrParser) {

        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));

        pDialog.setTitleText("Failed : RESCAN !" + " \n" + "\nYou're not in class!" + "\n Location: proximity OFF");
        pDialog.setCancelable(false);
        pDialog.show();

        qrCodeReaderView.stopCamera();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


    /* DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                        .execute(new Runnable() {
                            @Override
                            public void run() {
                                QRParser qrParser = new QRParser().gsonToQRParser(gson, result.getText());

                                final SweetAlertDialog pDialog = new SweetAlertDialog(ScanActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
                                pDialog.setTitle("Confirmed :"+qrParser.getUnitname());
                                pDialog.setTitleText(qrParser.getUnitcode());
                                pDialog.setCancelable(false);
                                pDialog.show();

                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();


                                    }
                                });

                            }
                        });*/

}
