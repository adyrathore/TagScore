package com.jskgmail.indiaskills.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.jskgmail.indiaskills.VolleyAppController;
import com.jskgmail.indiaskills.util.Constants;

public class NetworkSchedulerService extends JobService implements
        ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = NetworkSchedulerService.class.getSimpleName();

    public ConnectivityReceiver mConnectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        mConnectivityReceiver = new ConnectivityReceiver(this);
    }



    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob" + mConnectivityReceiver);
        registerReceiver(mConnectivityReceiver, new IntentFilter(Constants.CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob");
        unregisterReceiver(mConnectivityReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(VolleyAppController.isActivityVisible()) {
            String message = isConnected ? "Good! Connected to Internet" : "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

    }
}
