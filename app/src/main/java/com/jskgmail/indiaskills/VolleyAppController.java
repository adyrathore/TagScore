package com.jskgmail.indiaskills;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jskgmail.indiaskills.crashreporter.CreashReportSender;
import com.jskgmail.indiaskills.util.Util;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.io.IOException;

@ReportsCrashes(formUri = "-------",socketTimeout=30000)

public class VolleyAppController extends Application {

    public static final String TAG = VolleyAppController.class
            .getSimpleName();
    public static boolean activityVisible,isNetworkConnected; // Variable that will check the
    // current activity state
    private RequestQueue mRequestQueue;
    private static RequestQueue requestQueue;
    public static OkHttpClient client;
    private static VolleyAppController mInstance;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        client=new OkHttpClient();
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.start();
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        Util.handleSSLHandshake();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(VolleyAppController.this);
        ACRA.getErrorReporter().setReportSender(new CreashReportSender());

    }
    public static synchronized VolleyAppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public static void addRequest(com.android.volley.Request request){
        requestQueue.add(request);
    }
    public static String doPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //   Log.e("MyApplication", "do post request" + json);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static RequestQueue getRequestQueue1(){
        return requestQueue;
    }
    /*public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }*/




    public static boolean isNetworkConnected() {
        return activityVisible; // return true or false
    }
    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }
    public static void networkConnected(boolean network) {
        isNetworkConnected= network;// this will set true when activity resumed

    }
    public static boolean getNetwork() {
        return isNetworkConnected; // return true or false
    }
    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }




}