package com.jskgmail.indiaskills;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jskgmail.indiaskills.pojo.RequestLogin;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.version.VersionCodeResponse;
import com.jskgmail.indiaskills.pojo.version.VersionRequest;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;
import com.jskgmail.indiaskills.webservice.IResult;
import com.jskgmail.indiaskills.webservice.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActivitySplash extends AppCompatActivity {
    ImageView img;
    public static final int RequestPermissionCode = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    TextView tvVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img = (ImageView) findViewById(R.id.imgLogo);
        tvVersionCode = (TextView) findViewById(R.id.tvVersionCode);
        if (BuildConfig.FLAVOR_app.equals(C.MEPSCLogo)) {
            img.setImageResource(R.drawable.mepsc_logo);
        } else {
            img.setImageResource(R.drawable.logo);

        }
        try {
            tvVersionCode.setText("Version " + Util.getVersionInfo(this));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (isGpsEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermission()) {
                            gotoMainActivity();
                        } else {
                            requestPermission();
                        }
                    } else {
                        gotoMainActivity();
                    }
                } else {
                    displayLocationSettingsRequest();
                }
                // close this activity
            }
        }, 100);

    }

    void gotoMainActivity() {
        if (Util.isNetworkAvailable(this)) {
            getVersion();
        } else {
            Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
       /* else {
            Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && ReadContactsPermission && ReadPhoneStatePermission) {
                        //  Toast.makeText(ActivitySplash.this, "Permission has been Successfully Granted", Toast.LENGTH_LONG).show();
                    } else {
                        //  Toast.makeText(ActivitySplash.this, "Permission has been Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int Fourth = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int fifth = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int sixth = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int seven = ContextCompat.checkSelfPermission(getApplicationContext(),RECORD_AUDIO );


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                Fourth == PackageManager.PERMISSION_GRANTED &&
                fifth == PackageManager.PERMISSION_GRANTED &&
                sixth == PackageManager.PERMISSION_GRANTED && seven == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ActivitySplash.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        READ_PHONE_STATE,
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        INTERNET,
                        RECORD_AUDIO
                }, RequestPermissionCode);

    }

    boolean isGpsEnabled() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void displayLocationSettingsRequest() {

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(ActivitySplash.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ActivitySplash.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    protected void getVersion() {

        VersionRequest versionRequest = new VersionRequest();
        Gson gson = new Gson();
        String json = gson.toJson(versionRequest);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyService volleyService = new VolleyService(this);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {


                try {
                    Log.e("DEBUG", "version=" + response);
                    Gson gson = new Gson();
                    VersionCodeResponse versionCodeResponse = gson.fromJson(response, VersionCodeResponse.class);
                    if (Integer.parseInt(versionCodeResponse.getVersionCode()) != Util.getVersionCode(ActivitySplash.this)) {
                        showVersionAlert();
                    } else {
                        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }, "version", C.API_VERSION, Util.getHeader(), obj);

    }

    private void showVersionAlert() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivitySplash.this);
        //checking version api
        builder1.setMessage("Please update your TAGScore app");
        builder1.setCancelable(false);
        builder1.setTitle("Version update");
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ActivitySplash.this.finish();

                        Uri uri = Uri.parse("market://details?id=com.jskgmail.indiaskills");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                        /*if( dbVersion > getVersionInfo() && isMandatory )
                        {
                            //showVersionAlert();
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }*/


                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
