package com.jskgmail.indiaskills.webservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by aditya.singh on 4/22/2017.
 */

public class VolleyService {

    IResult mResultCallback = null;
    Context context;

    public VolleyService(Context context) {
        this.context = context;
    }


    public void postDataVolley(IResult resultCallback, final String requestType, String url, final Map<String, String> headers, JSONObject sendObj) {
        mResultCallback = resultCallback;
        try {

            if (BuildConfig.DEBUG)
                Log.e("REQUEST=", sendObj.toString());
            JsonObjectRequest jsonObj = new JsonObjectRequest(url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (BuildConfig.DEBUG)
                        Log.e("RESPONSE=", response.toString());
                    if (mResultCallback != null) {

                        mResultCallback.notifySuccess(requestType, response.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (BuildConfig.DEBUG)
                        Log.e("RESPONSE=", error.toString());
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };


            RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObj.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObj);


        } catch (Exception e) {
            if (mResultCallback != null)
                mResultCallback.notifyError(requestType, e.toString());
        }
    }

    public void getRequest(IResult resultCallback, final int requestType, String url, final Map<String, String> headers) {
        mResultCallback = resultCallback;
        System.out.println("Header : " + headers.toString());
        try {

            StringRequest stringRequest = new StringRequest(requestType, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null) {
                        Log.e("RESPONSE=", response.toString());

                        mResultCallback.notifySuccess(requestType + "", response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType + "", volleyError.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return headers;

                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            if (mResultCallback != null)
                mResultCallback.notifyError(requestType + "", e.toString());
        }
    }


}