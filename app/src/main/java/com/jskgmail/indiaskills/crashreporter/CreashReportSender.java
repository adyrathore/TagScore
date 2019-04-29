package com.jskgmail.indiaskills.crashreporter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.VolleyAppController;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreashReportSender implements ReportSender {
    //	private static String BASE_URL = "https://rink.hockeyapp.net/api/2/apps/";
//	private static String CRASHES_PATH = "/crashes";
//	private static String BASE_URL = "http://192.168.1.213:8080/emcrashreporter";
    private static String BASE_URL = "http://216.144.210.172:8484/AndroidCrashReporter/crashreporter";
    private static String CRASHES_PATH = "/reportcrash";
    private String packageName = "default";
    private static String TAG = "DEBUG";

    private String createCrashLog(CrashReportData report) {
        Date now = new Date();
        StringBuilder log = new StringBuilder();
        packageName = report.get(ReportField.PACKAGE_NAME);
        log.append("Package: " + packageName + "<br/>");
        log.append("Version: " + report.get(ReportField.APP_VERSION_CODE) + "<br/>");
        log.append("Android: " + report.get(ReportField.ANDROID_VERSION) + "<br/>");
        log.append("Manufacturer: " + android.os.Build.MANUFACTURER + "<br/>");
        log.append("Model: " + report.get(ReportField.PHONE_MODEL) + "<br/>");
        log.append("Date: " + now + "<br/>");
        log.append("<br/>");
        String[] logString = report.get(ReportField.STACK_TRACE).split("at ");
        log.append("StackTrace: ");
        for (String trace : logString) {
            if (trace.contains(packageName)) {
                log.append("<b>***************************************************</b><br/><br/>");
                log.append("<b style=\"background-color:#F3F781\"><font color=\"#8A0808\">at " + trace + "</font></b><br/><br/>");
                log.append("<b>***************************************************</b><br/>");
            } else {
                log.append("at " + trace + "<br/>");
            }
//            Log.d("test", "log :: "+log.toString());
        }
        //log.append("StackTrace: "+report.get(ReportField.STACK_TRACE));
        //report.get(ReportField.STACK_TRACE).

        return log.toString();
    }

    @Override
    public void send(Context context, CrashReportData errorContent) throws ReportSenderException {
        String log = createCrashLog(errorContent);
        String url = BASE_URL + CRASHES_PATH;

        try {

            StringBuilder builder = new StringBuilder();
            builder.append("userID :" + errorContent.get(ReportField.INSTALLATION_ID));
            builder.append("<br/>contact :" + errorContent.get(ReportField.USER_EMAIL));
            builder.append("<br/>description :" + errorContent.get(ReportField.USER_COMMENT));
            builder.append("<br/>" + log);

            JSONObject parameters = new JSONObject();
            parameters.put("app_name", context.getResources().getString(R.string.app_name));
            parameters.put("crash_report", builder.toString());
            sendLog(url, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendLog(String url, JSONObject report) throws IOException {

        JsonObjectRequest objectRequest = new JsonObjectRequest(url, report, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG,"Response "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error "+error.toString());
            }
        });
        VolleyAppController.getRequestQueue1().add(objectRequest);
    }


    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (Exception e) {

            }

        }
        Log.e(TAG, "Encoded " + result.toString());
        return result.toString();
    }
}
