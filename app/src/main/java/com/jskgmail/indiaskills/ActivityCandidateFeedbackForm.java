package com.jskgmail.indiaskills;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.HistoryTestLogRequest;
import com.jskgmail.indiaskills.pojo.LogHistoryTest;
import com.jskgmail.indiaskills.pojo.Logs;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityCandidateFeedbackForm extends AppCompatActivity {

    RatingBar r1, r2, r3, r4, r5;
    Button btn;
    ProgressDialog Dialog1;
    TextView t1, t2, t3, t4, t5, txt_main;
    String activeDetails;
    TestList testList;
    View llFeedback;
    String json;
    boolean candidateFeedbackrequired = true;
    private DatabaseHelper databaseHelper;
    private String sId;
    private String showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatefeedbackform);
        databaseHelper = new DatabaseHelper(this);

        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        sId = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();

        btn = (Button) findViewById(R.id.submit);
        r1 = (RatingBar) findViewById(R.id.rate_Question23);
        r2 = (RatingBar) findViewById(R.id.rate_Question24);
        r3 = (RatingBar) findViewById(R.id.rate_question25);
        r4 = (RatingBar) findViewById(R.id.rate_question26);
        r5 = (RatingBar) findViewById(R.id.rate_question27);
        t1 = (TextView) findViewById(R.id.Question23);
        t2 = (TextView) findViewById(R.id.question24);
        t3 = (TextView) findViewById(R.id.question25);
        t4 = (TextView) findViewById(R.id.question_26);
        t5 = (TextView) findViewById(R.id.question27);
        txt_main = (TextView) findViewById(R.id.txt_main);
        llFeedback = (View) findViewById(R.id.llFeedback);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedetailsfeedbackform();
            }
        });
        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
            btn.setText(R.string.SUBMIThn);
            t1.setText(R.string.candidatequs1);
            t2.setText(R.string.candidatequest2);
            t3.setText(R.string.candidatequest3);
            t4.setText(R.string.cadidatequest4);
            t5.setText(R.string.candidatequest5);
            txt_main.setText(R.string.submitfeedbackformhn);
        } else {
            btn.setText(R.string.SUBMIT);
        }
        json = Util.getJson(ActivityCandidateFeedbackForm.this, testList);
        try {
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String cf = scheduleSettings.getString("candidate_feedback");
            showResult = scheduleSettings.getString("result_on_screen");


            if (cf != null) {
                if (cf.equalsIgnoreCase("0")) {
                    llFeedback.setVisibility(View.GONE);
                    txt_main.setText("");
                    candidateFeedbackrequired = false;
                    if (Util.ONLINE) {
                        submitLogHistory();
                    } else {
                        Intent intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
                        intent.putExtra(C.TEST, testList);
                        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                        startActivity(intent);
                        finish();
                    }

                } else if (cf.equalsIgnoreCase("1")) {
                    llFeedback.setVisibility(View.VISIBLE);
                    candidateFeedbackrequired = true;
                } else {
                    llFeedback.setVisibility(View.VISIBLE);
                    candidateFeedbackrequired = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savedetailsfeedbackform() {
        if (activeDetails.equalsIgnoreCase("1")) {
            Globalclass.bookmardepractrical = "0";

        }

        String usrID =Globalclass.idcandidate;
        float rating = r1.getRating();
        DatabaseHelper db = new DatabaseHelper(ActivityCandidateFeedbackForm.this);
        boolean cu = db.insert_feedbackform("N", usrID, "23", String.valueOf(rating), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), "");
        if (cu == true) {
            //Toast.makeText(candidatefeedbackform.this,"Save feedback",Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(candidatefeedbackform.this,"not Save feedback",Toast.LENGTH_LONG).show();
        }

        float rating2 = r2.getRating();

        boolean cu2 = db.insert_feedbackform("N", usrID, "24", String.valueOf(rating2), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), "");
        if (cu2 == true) {
            // Toast.makeText(candidatefeedbackform.this,"Save feedback",Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(candidatefeedbackform.this,"not Save feedback",Toast.LENGTH_LONG).show();
        }

        float rating3 = r3.getRating();

        boolean cu3 = db.insert_feedbackform("N", usrID, "25", String.valueOf(rating3), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), "");
        if (cu3 == true) {
            // Toast.makeText(candidatefeedbackform.this,"Save feedback",Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(candidatefeedbackform.this,"not Save feedback",Toast.LENGTH_LONG).show();
        }

        float rating4 = r4.getRating();

        boolean cu4 = db.insert_feedbackform("N", usrID, "26", String.valueOf(rating4), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), "");
        if (cu4 == true) {
            // Toast.makeText(candidatefeedbackform.this,"Save feedback",Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(candidatefeedbackform.this,"not Save feedback",Toast.LENGTH_LONG).show();
        }

        float rating5 = r5.getRating();

        boolean cu5 = db.insert_feedbackform("N", usrID, "27", String.valueOf(rating5), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), "");
        if (cu == true) {
            //Toast.makeText(candidatefeedbackform.this,"Save feedback",Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(candidatefeedbackform.this,"not Save feedback",Toast.LENGTH_LONG).show();
        }
        if (Util.ONLINE) {
            //new LongOperationsubmittailsfortest().execute();
            Globalclass.lastpicturecandidate = "0";
            //  getjsonformat();
            submitLogHistory();
            //  new LongOperationsubmittailsfortest().execute();

        } else {
            Globalclass.userids = "";
            Globalclass.lastpicturecandidate = "0";
            Intent intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
            intent.putExtra(C.TEST, testList);
            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
            startActivity(intent);
            finish();
        }
    }

    private class LongOperationsubmittailsfortest extends AsyncTask<String, Void, Void> {
        // Required initialization
        // private final HttpClient Client = new DefaultHttpClient();

        //TextView uiUpdate = (TextView) findViewById(R.id.output);
        //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;

        //EditText serverText = (EditText) findViewById(R.id.serverText);
        protected void onPreExecute() {
            Dialog1 = new ProgressDialog(ActivityCandidateFeedbackForm.this);
            String data = "nFlg";
            Dialog1.setMessage("Saving Details please wait ...");
            Dialog1.show();
        }
        // Call after onPreExecute method

        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            /*****************************************************/

            submitLogHistory();


            return null;
        }

        protected void onPostExecute(Void unused) {
            if (Dialog1 != null && Dialog1.isShowing()) {
                Dialog1.dismiss();
            }
            Dialog1.dismiss();
            final DatabaseHelper myDb = new DatabaseHelper(ActivityCandidateFeedbackForm.this);

            boolean bx = myDb.delete_feedbackbyid(testList.getId(), testList.getScheduleIdPk(), Globalclass.idcandidate);
            //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

            //showMessage("mess",response.toString());
            //  String message = String.valueOf(response.get("message"));
            // showMessage("error",response.toString());
            Globalclass.userids = "";
            Intent intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
            intent.putExtra(C.TEST, testList);
            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
            startActivity(intent);
            finish();
            //  validateimageupdate();
        }
    }

    public void submitLogHistory() {
        Dialog1 = new ProgressDialog(ActivityCandidateFeedbackForm.this);
        String data = "nFlg";
        Dialog1.setMessage("Saving Details please wait ...");
        Dialog1.show();
        final DatabaseHelper myDb = new DatabaseHelper(ActivityCandidateFeedbackForm.this);
        List<LogHistoryTest> historyTests = myDb.getLogHistory(testList.getId(), Globalclass.idcandidate, activeDetails);
        HistoryTestLogRequest historyTestLogRequest = new HistoryTestLogRequest();
        Logs logs = new Logs();
        logs.setData(historyTests);
        historyTestLogRequest.setLog(logs);
        historyTestLogRequest.setApiKey(SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getApiKey());
        historyTestLogRequest.setUserId(SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getUserID());

        historyTestLogRequest.setScheduleUniqueKey(testList.getUniqueID());
        historyTestLogRequest.setApiKey(SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getApiKey());


        Gson gson = new GsonBuilder().create();
        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(historyTestLogRequest));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = C.API_SUBMIT_QUESTION_LOG;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj
                //   null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Data response", String.valueOf(response.toString()));
                Dialog1.dismiss();
                boolean bx = myDb.deleteLogHistoryOfUser(testList.getId(), Globalclass.idcandidate, activeDetails);

                getjsonformat();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error response", error);
                Dialog1.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                String credentials = "tagusp:t@g$c0re";
                String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //  headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        // Adding request to request queue
        String tag_json_obj = "json_obj_req";
        //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
        // VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
        RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request).setTag(tag_json_obj);

    }


    public void submitfeedback() {
        Dialog1 = new ProgressDialog(ActivityCandidateFeedbackForm.this);
        String data = "nFlg";
        Dialog1.setMessage("Saving Details please wait ...");
        Dialog1.show();
        final DatabaseHelper myDb = new DatabaseHelper(ActivityCandidateFeedbackForm.this);
        String usrID =Globalclass.idcandidate;

        final Cursor cursor = myDb.getAllfeedbackdatavaluesforonline(testList.getId(), testList.getScheduleIdPk(),usrID );
        if (cursor.getCount() > 0) {
            JSONArray array = cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data", (Object) array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tempNames = array.toString();
            String strsddata = sd.toString();
            //String finalData = strsddata.replace("/\\/", "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getUserID());
            params.put("api_key", SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getApiKey());
            params.put("schedule_unique_key", testList.getUniqueID());
            params.put("feedback", strsddata.trim());
            Log.e("params :", params.toString());
            // showMessage("message",params.toString());

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(params.toString());
                Log.e("jsonObject :", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = C.API_SUBMIT_FEEDBACK_URL;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject
                    //   null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Dialog1.dismiss();
                    Log.e("Data response", String.valueOf(response.toString()));
                    final DatabaseHelper myDb = new DatabaseHelper(ActivityCandidateFeedbackForm.this);

                    boolean bx = myDb.delete_feedbackbyid(testList.getId(), testList.getScheduleIdPk(), Globalclass.idcandidate);
                    //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    //showMessage("mess",response.toString());
                    //  String message = String.valueOf(response.get("message"));
                    // showMessage("error",response.toString());
                    Globalclass.userids = "";
                    Intent intent;
                    if (activeDetails.equals("0") && Util.ONLINE && Globalclass.roleval.equals("3")) {
                        intent = new Intent(ActivityCandidateFeedbackForm.this, showResult.equals(C.YES) ? ActivityResult.class : ActivityThankyou.class);
                    } else {
                        intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
                    }
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                    Dialog1.dismiss();
                    // Log.e("Data response", String.valueOf(response.toString()));
                    boolean bx = myDb.delete_feedbackbyid(testList.getId(), testList.getScheduleIdPk(), Globalclass.userids);
                    //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    //showMessage("mess",response.toString());
                    //  String message = String.valueOf(response.get("message"));
                    // showMessage("error",response.toString());
                    Globalclass.userids = "";
                    Intent intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                    finish();
                    // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    // showMessage("error",error.toString());
                    //  showMessage("error",error.toString());
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    String credentials = "tagusp:t@g$c0re";
                    String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    //  headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            // Adding request to request queue
            String tag_json_obj = "json_obj_req";
            //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
            // VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
            RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request).setTag(tag_json_obj);
        } else {
        }
    }

    public void getjsonformat() {


        Dialog1 = new ProgressDialog(ActivityCandidateFeedbackForm.this);
        String data = "nFlg";
        Dialog1.setMessage("Saving Details please wait ...");
        Dialog1.show();

        final DatabaseHelper myDb = new DatabaseHelper(ActivityCandidateFeedbackForm.this);
        final Cursor cursor = myDb.getAllquestiondetilsvaluploadbackonline(testList.getId(), testList.getScheduleIdPk(), Globalclass.userids);
        if (cursor.getCount() >= 0) {
            JSONArray array = new JSONArray();
            JSONObject sd = new JSONObject();
            try {
                if (cursor.getCount() == 0) {
                    sd.put("data", new JSONArray());
                } else {
                    array = cur2Json(cursor);
                    sd.put("data", (Object) array);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tempNames = array.toString();
            String strsddata = sd.toString();
            String finalData = strsddata.replace("/\\/", "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getUserID());
            params.put("api_key", SharedPreference.getInstance(ActivityCandidateFeedbackForm.this).getUser(C.LOGIN_USER).getApiKey());
            params.put("testID", testList.getId());
            params.put("uniqueId", testList.getUniqueID());
            params.put("schedule_id", testList.getScheduleIdPk());
            params.put("complete_type", Globalclass.completetype);
            params.put("test_data", finalData);
            Log.e("params :", params.toString());
            // showMessage("message",params.toString());
            String url = C.API_TEST_DETAILS_UPLOAD;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(params.toString());
                Log.e("jsonObject :", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject
                    //   null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Dialog1.dismiss();
                    Log.e("Data response", String.valueOf(response.toString()));
                    // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    try {


                        if (!candidateFeedbackrequired) {
                            Globalclass.userids = "";
                            Globalclass.lastpicturecandidate = "0";
                            Intent intent;
                            if (activeDetails.equals("0") && Util.ONLINE && Globalclass.roleval.equals("3")) {
                                intent = new Intent(ActivityCandidateFeedbackForm.this, showResult.equals(C.YES) ? ActivityResult.class : ActivityThankyou.class);
                            } else {
                                intent = new Intent(ActivityCandidateFeedbackForm.this, ActivityThankyou.class);
                            }
                            intent.putExtra(C.TEST, testList);
                            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                            startActivity(intent);
                            finish();
                        } else {
                            boolean b = myDb.deleteuseranswer(testList.getId(), testList.getScheduleIdPk(), Globalclass.userids);
                            if (b == true) {
                                submitfeedback();
                            } else {
                                submitfeedback();
                            }
                        }
                        //showMessage("mess",response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                    // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    // showMessage("error",error.toString());

                    getjsonformat();

                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    String credentials = "tagusp:t@g$c0re";
                    String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    //  headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            // Adding request to request queue
            String tag_json_obj = "json_obj_req";
            //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
            // VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
            RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request).setTag(tag_json_obj);
        } else {
            //Snackbar snackbar = Snackbar.make(view, "User Details Does Not Match ", Snackbar.LENGTH_LONG);
            // snackbar.show();
            Cursor res = myDb.getdetailsoflluseranswer();
            if (res.getCount() == 0) {
                // show message
                //   showMessage("Error","Nothing found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("tagid :" + res.getString(0) + "\n");
                buffer.append("pass :" + res.getString(1) + "\n");
                buffer.append("apikey :" + res.getString(2) + "\n");
                // buffer.append("Marks :"+ res.getString(3)+"\n\n");
            }
            // Show all data
            // showMessage("Data",buffer.toString());
        }


    }

    public JSONArray cur2Json(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        // cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("TAG", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            //cursor.moveToNext();
        }

        cursor.close();
        return resultSet;
    }


}
