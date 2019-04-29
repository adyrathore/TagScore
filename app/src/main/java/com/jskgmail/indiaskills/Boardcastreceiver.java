package com.jskgmail.indiaskills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jskgmail.indiaskills.service.ServiceLocation;

public class Boardcastreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        if (context == null)
            return;

        String action = intent.getAction();
        if (action == null)
            return;
        if(action != null   ) {
           // Log.i(Boardcastreceiver.class.getSimpleName(), "Restarting Service");
            context.startService(new Intent(context, ServiceLocation.class));
        }
    }
}
