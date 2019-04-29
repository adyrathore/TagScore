package com.jskgmail.indiaskills.camera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CemeraService extends Service {
    public CemeraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CameraManager mk = CameraManager.getInstance(CemeraService.this);
        mk.takePhoto();
        //CameraManager mgr = CameraManager.getInstance(Cemeraservice.this);
        //mgr.takePhoto();
        Log.e("Photo","Back");
        return START_NOT_STICKY;
    }
}
