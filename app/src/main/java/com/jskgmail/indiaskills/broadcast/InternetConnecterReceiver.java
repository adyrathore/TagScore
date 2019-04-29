package com.jskgmail.indiaskills.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jskgmail.indiaskills.ActivityTestList;
import com.jskgmail.indiaskills.Globalclass;
import com.jskgmail.indiaskills.VolleyAppController;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class InternetConnecterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(CONNECTIVITY_ACTION)) {
                boolean isVisible = VolleyAppController.isActivityVisible();// Check if
                // activity
                // is
                // visible
                // or not
                Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

                // If it is visible then trigger the task else do nothing
                if (isVisible == true) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager
                            .getActiveNetworkInfo();

                    // Check internet connection and accrding to state change the
                    // text of activity by calling method
                    if (networkInfo != null && networkInfo.isConnected()) {

                        changeTextStatus(context, true);
                    } else {
                        changeTextStatus(context, false);
                    }
                }
            }
            } catch(Exception e){
                e.printStackTrace();
            }

    }

    public void changeTextStatus(Context context, boolean isConnected) {

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
