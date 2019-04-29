package com.jskgmail.indiaskills.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jskgmail.indiaskills.ActivityTestList;
import com.jskgmail.indiaskills.Globalclass;
import com.jskgmail.indiaskills.VolleyAppController;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

public class ConnectivityReceiver extends BroadcastReceiver {

    public ConnectivityReceiverListener mConnectivityReceiverListener;
public  ConnectivityReceiver() {

    }

    ConnectivityReceiver(ConnectivityReceiverListener listener) {
        mConnectivityReceiverListener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
       // mConnectivityReceiverListener.onNetworkConnectionChanged(isConnected(context));
       if(VolleyAppController.isActivityVisible()) {
           changeTextStatus(context, isConnected(context));
       }
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    public void changeTextStatus(Context context, boolean isConnected) {
        if(VolleyAppController.getNetwork()!=isConnected) {
            // Change status according to boolean value
            String username = SharedPreference.getInstance(context).getString(C.USERNAME);

            String password = SharedPreference.getInstance(context).getString(C.PASSWORD);

            if (!isConnected) {

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                boolean str = databaseHelper.checkUser(username, password);
                if (str) {
                    Util.ONLINE = false;
                    Intent accountsIntent = new Intent(context, ActivityTestList.class);
                    accountsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(accountsIntent);

                }
            } else {
                ResponseLogin responseLogin = SharedPreference.getInstance(context).getUser(C.LOGIN_USER);
                if (responseLogin != null) {
                    Util.ONLINE = true;
                    Globalclass.userids = responseLogin.getUserID();
                    Globalclass.roleval = responseLogin.getProfile().get(0).getRole();
                    Intent accountsIntent = new Intent(context, ActivityTestList.class);
                    accountsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(accountsIntent);
                }
            }
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
