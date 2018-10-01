package com.job.darasastudent.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.button.MaterialButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.Gson;
import com.job.darasastudent.R;
import com.job.darasastudent.model.QRParser;
import com.job.darasastudent.scanview.CodeScannerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

import static com.job.darasastudent.util.Constants.COMPLETED_GIF_PREF_NAME;

public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener, OnLocationUpdatedListener {

    private static final int LOCATION_PERMISSION_ID = 1001;
    private static final String TAG = "ScanActivity";


    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;
    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrCodeReaderView;
    @BindView(R.id.scanner_view)
    CodeScannerView scannerView;
    @BindView(R.id.scan_gif)
    View scanGifView;
    @BindView(R.id.gif_gotit_btn)
    MaterialButton gifGotitBtn;


    private Gson gson;
    private LocationGooglePlayServicesProvider provider;
    private Location mLocation;
    private int locationcount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfinder);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our GIF
        if (!sharedPreferences.getBoolean(
                COMPLETED_GIF_PREF_NAME, false)) {
            // The user hasn't seen the GIF yet, so show it
            scanGifView.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        gson = new Gson();

        // Check if the location services are enabled
        checkLocationOn();
        SmartLocation.with(this).location().state().locationServicesEnabled();
        // Location permission not granted
        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();

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

        if (qrParser == null) {
            Log.d(TAG, "onQRCodeRead: " + text);
            unauthScanLocation(pDialog);
            return;
        }

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

    private void unauthScanLocation(final SweetAlertDialog pDialog) {

        pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));

        pDialog.setTitleText("Not Allowed !" + "\n\nScan code from" + "\n Darasa Lecturer App");
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
    protected void onStart() {
        super.onStart();

        //register location change broadcast
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onResume() {
        qrCodeReaderView.startCamera();
        //register location change broadcast
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        qrCodeReaderView.stopCamera();
        stopLocation();
        //unregister location change broadcast
        try {
            unregisterReceiver(mGpsSwitchStateReceiver);
        } catch (IllegalArgumentException e) {
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopLocation();
        //unregister location change broadcast
        try {
            unregisterReceiver(mGpsSwitchStateReceiver);
        } catch (IllegalArgumentException e) {
        }

        super.onDestroy();
    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);

        //register location change broadcast
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    private void checkLocationOn() {

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.location);  // GPS not found
        builder.setMessage(R.string.permission_rationale_location); // Want to enable?
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    ScanActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dd = builder.create();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dd.show();
            setUpLocationUi(false, scannerView.getmAutoFocusButton());

        } else {
            dd.dismiss();
            setUpLocationUi(true, scannerView.getmAutoFocusButton());

        }
    }

    /**
     * Following broadcast receiver is to listen the Location button toggle state in Android.
     */
    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                // Make an action or refresh an already managed state.
                checkLocationOn();
            }
        }
    };

    @Override
    public void onLocationUpdated(Location location) {
        //showLocation(location);

        mLocation = location;
    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();
        SmartLocation.with(this).geocoding().stop();

    }

    private void setUpLocationUi(Boolean on_off, ImageView scanLocImg) {
        if (on_off) {

       /*     DrawableHelper
                    .withContext(this)
                    .withColor(R.color.darkbluish)
                    .withDrawable(R.drawable.ic_location_on)
                    .tint()
                    .applyTo(scanLocImg);*/

            scanLocImg.setImageResource(R.drawable.ic_loc_on);
        } else {

            scanLocImg.setImageResource(R.drawable.ic_loc_off);
        }
    }

    @OnClick(R.id.gif_gotit_btn)
    public void gifBtnOnclick(){

        // User has seen GIF, so mark our SharedPreferences
        // flag as completed so that we don't show our GIF
        // the next time the user launches the app.
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        sharedPreferencesEditor.putBoolean(
                COMPLETED_GIF_PREF_NAME, true);
        sharedPreferencesEditor.apply();
        scanGifView.setVisibility(View.GONE);
    }

}
