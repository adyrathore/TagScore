package com.jskgmail.indiaskills;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.jskgmail.indiaskills.adpater.AdapterExamList;
import com.jskgmail.indiaskills.broadcast.InternetConnecterReceiver;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.HistoryTestLogRequest;
import com.jskgmail.indiaskills.pojo.LogHistoryTest;
import com.jskgmail.indiaskills.pojo.Logs;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.ResponseTestList;
import com.jskgmail.indiaskills.pojo.TestDetailRequest;
import com.jskgmail.indiaskills.pojo.TestFeedbackRequest;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.TestListRequest;
import com.jskgmail.indiaskills.pojo.TestUploadRequest;
import com.jskgmail.indiaskills.pojo.test.QuestionData;
import com.jskgmail.indiaskills.pojo.test.TestResponse;
import com.jskgmail.indiaskills.service.NetworkSchedulerService;
import com.jskgmail.indiaskills.service.ServiceLocation;
import com.jskgmail.indiaskills.util.Api;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;
import com.jskgmail.indiaskills.webservice.IResult;
import com.jskgmail.indiaskills.webservice.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class ActivityTestList extends AppCompatActivity {

    @BindView(R.id.imgforconnet)
    ImageView imgforconnet;

    @BindView(R.id.nameuser)
    AppCompatTextView nameuser;
    @BindView(R.id.textViewName)
    AppCompatTextView textViewName;
    @BindView(R.id.lvTestList)
    ListView lvTestList;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    boolean isDeviceConnected = true;
    private Dialog progressDialog;
    ResponseTestList responseTestList;
    TestResponse testResponse;
    DatabaseHelper databaseHelper;
    private AdapterExamList adapterExamList;
    InternetConnecterReceiver receiver;
    DownloadZipFileTask downloadZipFileTask;
    ProgressBar progressBar;
    private ProgressDialog downloadingZipProgress;
    boolean isOnline;
    String onlineRes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DatabaseHelper(this);
        if (Util.ONLINE) {
            imgforconnet.setImageResource(R.drawable.online);
            getTestListOnline();
        } else {
            imgforconnet.setImageResource(R.drawable.offline);
            getTestListFromDB();
        }
        if (!Util.isMyServiceRunning(ServiceLocation.class, ActivityTestList.this)) {
            startService(new Intent(ActivityTestList.this, ServiceLocation.class));
        }
        isDeviceConnected = Util.isNetworkAvailable(ActivityTestList.this);
        VolleyAppController.networkConnected(isDeviceConnected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            scheduleJob();
        }
        try {
            nameuser.setText("Welcome (" + SharedPreference.getInstance(ActivityTestList.this).getUser(C.LOGIN_USER).getProfile().get(0).getName() + ")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(false)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
            startService(startServiceIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopService(new Intent(this, NetworkSchedulerService.class));
        }
    }

    private void getTestListFromDB() {

        Cursor res = databaseHelper.getAll_test_list();
        if (res.getCount() == 0) {
            Util.showMessage(ActivityTestList.this, R.string.no_test_found);
            return;
        }
        ArrayList<TestList> testLists = new ArrayList<>();
        while (res.moveToNext()) {
            TestList testList = new TestList();
            testList.setId(res.getString(0));
            testList.setUniqueID(res.getString(4));
            testList.setScheduleIdPk(res.getString(4));
            testList.setPurchasedTime(res.getString(3));
            testList.setTestName(res.getString(2));
            testList.setTestType("0");
            testLists.add(testList);
        }
        adapterExamList = new AdapterExamList(ActivityTestList.this, testLists);
        lvTestList.setAdapter(adapterExamList);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void registerReceiver() {
        receiver = new InternetConnecterReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);

        //registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreference.getInstance(this).setString(C.ANNEXURE_DATA, null);
        VolleyAppController.activityResumed();
        // registerReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        VolleyAppController.activityPaused();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ActivityTestList.this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void getTestListOnline() {
        progressDialog = Util.getProgressDialog(this, R.string.Loading);
        progressDialog.show();
        ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
        final TestListRequest testListRequest = new TestListRequest();
        testListRequest.setApiKey(responseLogin.getApiKey());
        testListRequest.setUserId(responseLogin.getUserID());
        testListRequest.setRole(responseLogin.getProfile().get(0).getRole());
        Gson gson = new Gson();
        String json = gson.toJson(testListRequest);
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
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Gson gson = new Gson();
                    responseTestList = gson.fromJson(response.toString(), ResponseTestList.class);
                    adapterExamList = new AdapterExamList(ActivityTestList.this, responseTestList.getTestList());
                    lvTestList.setAdapter(adapterExamList);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
            }
        }, "getTestListOnline", C.API_TEST_LIST, Util.getHeader(), obj);
    }

    public void startTestOnline(final TestList testList) {

        if (Util.getInternalStorageSpaceInMB() > 1024) {
            isOnline = true;
            progressDialog = Util.getProgressDialog(this, R.string.please_wait);
            progressDialog.show();
            final ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
            final TestDetailRequest testDetailRequest = new TestDetailRequest();
            testDetailRequest.setApiKey(responseLogin.getApiKey());
            testDetailRequest.setUserId(responseLogin.getUserID());
            testDetailRequest.setTestID(testList.getId());
            testDetailRequest.setUniqueId(testList.getUniqueID());
            testDetailRequest.setLanguageCode(C.LANGUAGE);
            Gson gson = new Gson();
            String json = gson.toJson(testDetailRequest);
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

                   // progressDialog.dismiss();

                    try {
                        Log.e("Data response", String.valueOf(response.toString()));
                        onlineRes = response;
                        SharedPreference.getInstance(ActivityTestList.this).setString(C.TEST_DATA, onlineRes);
/*
                    try {
                        String res = response.toString();
                        SharedPreference.getInstance(ActivityTestList.this).setString(C.ONLINE_TEST_LIST, res);
                        JSONObject array = (new JSONObject(res)).getJSONObject("data");
                        //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
                        //JSONObject array=json.getJSONObject("data");

                        JSONObject jsonobj_2 = (JSONObject) array;
                        JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
                        for (int j = 0; j < subObjDetails.length(); j++) {

                            if (Globalclass.roleval.equalsIgnoreCase("3")) {
                                Globalclass.completetype = "T";
                                Globalclass.userids =  SharedPreference.getInstance(ActivityTestList.this).getString(C.USERNAME);
                                Globalclass.idcandidate = responseLogin.getUserID();
                                Intent io = new Intent(ActivityTestList.this, Newcandidatecapturebefore.class);
                                io.putExtra(C.TEST, testList);
                                io.putExtra(C.ACTIVE_DETAILS, "0");
                                io.putExtra(C.SELECTED_USERNAME, "non");
                                io.putExtra(C.TAG_ID, SharedPreference.getInstance(ActivityTestList.this).getString(C.USERNAME));

                                startActivity(io);
                                finish();
                            } else {
                                Intent io = new Intent(ActivityTestList.this, TestDetails.class);
                                io.putExtra(C.TEST_ID, testList.getId());
                                io.putExtra(C.SCHEDULE_ID_PK, testList.getScheduleIdPk());
                                io.putExtra(C.TEST, testList);
                                startActivity(io);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
*/
                        zipFileDownload(response, testList);
                    } catch (Exception e) {

                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(String requestType, String error) {
                    progressDialog.dismiss();
                }
            }, "getTestDetails", C.API_TEST_DETAILS, Util.getHeader(), obj);
        } else {
            Util.showMessage(ActivityTestList.this, R.string.space_issue_mb);

        }
    }

    public void downloadTestQuestionaryDetails(final TestList testList) {
        if (Util.getInternalStorageSpaceInMB() > 1000) {
            isOnline = false;
            progressDialog = Util.getProgressDialog(this, R.string.Downloading);
            progressDialog.show();
            ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
            final TestDetailRequest testDetailRequest = new TestDetailRequest();
            testDetailRequest.setApiKey(responseLogin.getApiKey());
            testDetailRequest.setUserId(responseLogin.getUserID());
            testDetailRequest.setTestID(testList.getId());
            testDetailRequest.setUniqueId(testList.getUniqueID());
            testDetailRequest.setLanguageCode(C.LANGUAGE);
            Gson gson = new Gson();
            String json = gson.toJson(testDetailRequest);
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

//                    progressDialog.dismiss();

                    try {

                        Log.e("Data response", String.valueOf(response.toString()));
                        try {
                            String res = response;
                            SharedPreference.getInstance(ActivityTestList.this).setString(C.TEST_DATA, res);
                            JSONObject array = (new JSONObject(res)).getJSONObject("data");
                            JSONObject jsonobj_2 = (JSONObject) array;
                            JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
                            for (int j = 0; j < subObjDetails.length(); j++) {
                                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                                String test_name = jsonobj_2_answer.get("schedule_name").toString();

                          /*  JSONObject scheduleSettings = Util.getScheduleSettings(res);
                            String qA = scheduleSettings.getString("question_Appearence");
                            String qoA="";
                            if(scheduleSettings.getString("questionoptions_Appearence")!=null) {
                                     qoA = scheduleSettings.getString("questionoptions_Appearence");
                            }*/
                                boolean already = databaseHelper.getcompletedvaluesflaginserted(testList.getId(), testList.getScheduleIdPk());
                                if (already) {
                                } else {

                              /*  Gson gson = new Gson();
                                testResponse = gson.fromJson(response, TestResponse.class);
                                if (qA.equalsIgnoreCase("random")) {
                                    res=getRandomQuestions(res);
                                } else if (qA.equalsIgnoreCase("suffle")) {
                                    res=  questionSuffle(res);
                                }
                                if (qoA.equalsIgnoreCase("shuffle") || qoA.equalsIgnoreCase("suffle")) {
                                    res=questionOptionSuffle(res);
                                }*/

                                    databaseHelper.insertJSONFORMAT(testList.getId(), test_name, res, "0", testList.getScheduleIdPk());
                                }
                                ResponseLogin responseLogin = SharedPreference.getInstance(ActivityTestList.this).getUser(C.LOGIN_USER);
                                databaseHelper.insert_table_userlogin(SharedPreference.getInstance(ActivityTestList.this).getString(C.USERNAME), SharedPreference.getInstance(ActivityTestList.this).getString(C.PASSWORD), responseLogin.getApiKey());

                            }


                            zipFileDownload(response, testList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(String requestType, String error) {
                    progressDialog.dismiss();
                }
            }, "Login", C.API_TEST_DETAILS_DOWNLOAD, Util.getHeader(), obj);
        } else {
            Util.showMessage(ActivityTestList.this, R.string.space_issue_mb);

        }
    }

    public void zipFileDownload(final String res, final TestList testList) {
        if (progressDialog == null)
            progressDialog = Util.getProgressDialog(this, R.string.please_wait);
        if (!progressDialog.isShowing())
            progressDialog.show();
        ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
        final TestDetailRequest testDetailRequest = new TestDetailRequest();
        testDetailRequest.setApiKey(responseLogin.getApiKey());
        testDetailRequest.setUserId(responseLogin.getUserID());
        testDetailRequest.setTestID(testList.getId());
        testDetailRequest.setUniqueId(testList.getUniqueID());
        //  testDetailRequest.setLanguageCode(C.LANGUAGE);
        Gson gson = new Gson();
        String json = gson.toJson(testDetailRequest);
        Log.e("DEBUG", "REQUEST=" + json);
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
                if (!isOnline)
                    progressDialog.dismiss();

                try {

                    Log.e("Data response", String.valueOf(response.toString()));
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.getInt("responseCode") == 200 && !jsonObject.getString("zipUrl").equals("")) {
                            downloadZipFile(jsonObject.getString("zipUrl"), testList);
                        } else {
                            if (isOnline) {
                                gotoNextScreen(res, testList);
                            } else {
                                showMessage();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
            }
        }, "Login", C.API_ZIP_DOWNLOAD_URL, Util.getHeader(), obj);

    }

    public void uploadRecords(final TestList testList) {
        final Cursor cursor = databaseHelper.getAllquestiondetilsvaluploadback(testList.getId(), testList.getScheduleIdPk());
        if (cursor.getCount() > 0) {
            JSONArray array = Util.cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data", (Object) array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tempNames = array.toString();
            String strsddata = sd.toString();
            String finalData = strsddata.replace("/\\/", "");
            progressDialog = Util.getProgressDialog(this, R.string.Uploading);
            progressDialog.show();
            ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
            final TestUploadRequest testUploadRequest = new TestUploadRequest();
            testUploadRequest.setApiKey(responseLogin.getApiKey());
            testUploadRequest.setUserId(responseLogin.getUserID());
            testUploadRequest.setTestID(testList.getId());
            testUploadRequest.setUniqueId(testList.getUniqueID());
            testUploadRequest.setScheduleId(testList.getScheduleIdPk());
            testUploadRequest.setTestData(finalData);
            Gson gson = new Gson();
            String json = gson.toJson(testUploadRequest);
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

                    progressDialog.dismiss();

                    try {
                        //submitfeedback(testList);
                        submitLogHistory(testList);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(String requestType, String error) {
                    progressDialog.dismiss();
                }
            }, "Login", C.API_TEST_DETAILS_UPLOAD, Util.getHeader(), obj);

        } else {
            Util.showMessage(ActivityTestList.this, R.string.all_offline_details_are_removed);
        }
    }

    public void submitLogHistory(final TestList testList) {
        progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
        progressDialog.show();

        final DatabaseHelper myDb = new DatabaseHelper(ActivityTestList.this);
        List<LogHistoryTest> historyTests = myDb.getLogHistory(testList.getId());
        HistoryTestLogRequest historyTestLogRequest = new HistoryTestLogRequest();
        Logs logs = new Logs();
        logs.setData(historyTests);
        historyTestLogRequest.setLog(logs);
        historyTestLogRequest.setApiKey(SharedPreference.getInstance(ActivityTestList.this).getUser(C.LOGIN_USER).getApiKey());
        historyTestLogRequest.setUserId(SharedPreference.getInstance(ActivityTestList.this).getUser(C.LOGIN_USER).getUserID());

        historyTestLogRequest.setScheduleUniqueKey(testList.getUniqueID());
        historyTestLogRequest.setApiKey(SharedPreference.getInstance(ActivityTestList.this).getUser(C.LOGIN_USER).getApiKey());


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
                , new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Data response", String.valueOf(response.toString()));
                progressDialog.dismiss();
                boolean bx = myDb.deleteLogHistoryOfUser(testList.getId());

                submitfeedback(testList);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error response", error);
                progressDialog.dismiss();
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


    protected void submitfeedback(final TestList testList) {
        final Cursor cursor = databaseHelper.getAllfeedbackdatavalues(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        if (cursor.getCount() > 0) {
            JSONArray array = Util.cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data", (Object) array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String strsddata = sd.toString();
            progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
            progressDialog.show();
            ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
            final TestFeedbackRequest testFeedbackRequest = new TestFeedbackRequest();
            testFeedbackRequest.setApiKey(responseLogin.getApiKey());
            testFeedbackRequest.setUserId(responseLogin.getUserID());
            testFeedbackRequest.setScheduleUniqueKey(testList.getUniqueID());
            testFeedbackRequest.setFeedback(strsddata.trim());
            Gson gson = new Gson();
            String json = gson.toJson(testFeedbackRequest);
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

                    progressDialog.dismiss();

                    try {
                        final Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ActivityTestList.this, zippingactivity.class);
                                intent.putExtra(C.TEST, testList);
                                startActivity(intent);
                                finish();
                            }
                        }, 2 * 1000);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(String requestType, String error) {

                    progressDialog.dismiss();
                    try {
                        final Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ActivityTestList.this, zippingactivity.class);
                                intent.putExtra(C.TEST, testList);
                                startActivity(intent);
                                finish();
                            }
                        }, 2 * 1000);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }, "Login", C.API_SUBMIT_FEEDBACK_URL, Util.getHeader(), obj);

        } else {
            Cursor res = databaseHelper.getdetailsoflluseranswer();
            if (res.getCount() == 0) {
                // show message
                Util.showMessage(ActivityTestList.this, "Nothing found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("tagid :" + res.getString(0) + "\n");
                buffer.append("pass :" + res.getString(1) + "\n");
                buffer.append("apikey :" + res.getString(2) + "\n");
                // buffer.append("Marks :"+ res.getString(3)+"\n\n");
            }
            Util.showMessage(ActivityTestList.this, buffer.toString());
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            changeTextStatus(ActivityTestList.this, isOnline(context));
            Log.e("TAG", "Network REceiver Executed");
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
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


    private void downloadZipFile(String url, final TestList testList) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL_ZIP)
                .client(okHttpClient)
                .build();
        Api downloadService = retrofit.create(Api.class);
        Call<ResponseBody> call = downloadService.downloadFileByUrl(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("DEBUG", "Got the body for the file");

                    if (!isOnline)
                        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();

                    downloadZipFileTask = new DownloadZipFileTask(testList);
                    downloadZipFileTask.execute(response.body());

                } else {
                    Log.d("DEBUG", "Connection failed " + response.errorBody());
                    gotoNextScreen(onlineRes, testList);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("DEBUG", t.getMessage());
            }
        });
    }


    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {
        TestList testList;
        File zipFileDestination, unZipFileDestination;

        public DownloadZipFileTask(TestList testList) {
            this.testList = testList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbarforvalues();
        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            zipFileDestination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), testList.getId() + ".zip");

            unZipFileDestination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), testList.getId());
            String result = saveToDisk(urls[0], zipFileDestination);

            return result;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            if (!isOnline) {
                Log.d("API123", progress[0].second + " ");

                if (progress[0].first == 100) {
                    if (!isOnline) {
                        Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();

                    }
                    dismissProgressDialog();
                }

                if (progress[0].second > 0) {
                    int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
                    downloadingZipProgress.setProgress(currentProgress);

                    //   txtProgressPercent.setText("Progress " + currentProgress + "%");

                }
            }
            if (progress[0].first == -1) {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (downloadingZipProgress != null && downloadingZipProgress.isShowing())
                    downloadingZipProgress.dismiss();

                if (result.equals("done")) {
                    new unZipFileTask(testList, zipFileDestination.getPath(), unZipFileDestination.getPath()).execute();
                } else if (result.equals("sizeIssue")) {
                    boolean success = databaseHelper.delete_byID(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                    Util.showMessage(ActivityTestList.this, R.string.space_issue);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class unZipFileTask extends AsyncTask<Void, Void, String> {
        TestList testList;
        String destinationPath, source;

        public unZipFileTask(TestList testList, String source, String destinationPath) {
            this.testList = testList;
            this.source = source;
            this.destinationPath = destinationPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = Util.getProgressDialog(ActivityTestList.this, isOnline ? R.string.please_wait : R.string.unziping_file);
            }
            else
            {
                TextView text = (TextView) progressDialog.findViewById(R.id.tvMsg);
                text.setText(isOnline ? R.string.please_wait : R.string.unziping_file);
            }
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... urls) {
            //Copy you logic to calculate progress and call
            Util.unzip(source, destinationPath);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            if (adapterExamList != null) {
                adapterExamList.notifyDataSetChanged();
                //   Util.showMessage(ActivityTestList.this,R.string.successfully_download);
            }
            if (isOnline) {
                gotoNextScreen(onlineRes, testList);
            } else {
                showMessage();
            }
        }
    }


    void showMessage() {
        Util.showMessage(ActivityTestList.this, R.string.successfully_download);
    }

    void gotoNextScreen(String response, TestList testList) {

        try {
            progressDialog.dismiss();
            final ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);

            String res = response.toString();
/*            JSONObject scheduleSettings = Util.getScheduleSettings(res);
            String qA = scheduleSettings.getString("question_Appearence");
            String qoA="";

                if (scheduleSettings.has("questionoptions_Appearence") && scheduleSettings.getString("questionoptions_Appearence") != null) {
                    qoA = scheduleSettings.getString("questionoptions_Appearence");
                }

            Gson gson = new Gson();
            testResponse = gson.fromJson(response, TestResponse.class);
            if (qA.equalsIgnoreCase("random")) {
                res=getRandomQuestions(res);
            } else if (qA.equalsIgnoreCase("suffle")) {
                res=  questionSuffle(res);
            }
            if (qoA.equalsIgnoreCase("shuffle") || qoA.equalsIgnoreCase("suffle")) {
                res=questionOptionSuffle(res);
            }*/
            SharedPreference.getInstance(ActivityTestList.this).setString(C.ONLINE_TEST_LIST, res);
            JSONObject array = (new JSONObject(res)).getJSONObject("data");
            //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
            //JSONObject array=json.getJSONObject("data");

            JSONObject jsonobj_2 = (JSONObject) array;
            JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
            for (int j = 0; j < subObjDetails.length(); j++) {

                if (Globalclass.roleval.equalsIgnoreCase("3")) {
                    Globalclass.completetype = "T";
                    Globalclass.userids = SharedPreference.getInstance(ActivityTestList.this).getString(C.USERNAME);
                    Globalclass.idcandidate = responseLogin.getUserID();
                    Intent io = new Intent(ActivityTestList.this, Newcandidatecapturebefore.class);
                    io.putExtra(C.TEST, testList);
                    io.putExtra(C.ACTIVE_DETAILS, "0");
                    io.putExtra(C.SELECTED_USERNAME, "non");
                    io.putExtra(C.TAG_ID, SharedPreference.getInstance(ActivityTestList.this).getString(C.USERNAME));

                    startActivity(io);
                    finish();
                } else {
                    Intent io = new Intent(ActivityTestList.this, TestDetails.class);
                    io.putExtra(C.TEST_ID, testList.getId());
                    io.putExtra(C.SCHEDULE_ID_PK, testList.getScheduleIdPk());
                    io.putExtra(C.TEST, testList);
                    startActivity(io);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String saveToDisk(ResponseBody body, File filename) {
        try {


            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(filename);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.e("DEBUG", "File Size=" + fileSize);
                if (Util.getInternalStorageSpace() > (fileSize + 20000000)) {
                    while ((count = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, count);
                        progress += count;
                        Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                        downloadZipFileTask.doProgress(pairs);
                        Log.d("DEBUG", "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                    }

                    outputStream.flush();

                    Log.e("DEBUG", filename.getParent());
                    Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                    downloadZipFileTask.doProgress(pairs);
                } else {
                    return "sizeIssue";
                }
                return "done";
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d("DEBUG", "Failed to save the file!");
                return "done";
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DEBUG", "Failed to save the file!");
            return "done";
        }
    }


    public void progressbarforvalues() {
        downloadingZipProgress = new ProgressDialog(ActivityTestList.this);
        downloadingZipProgress.setMax(100);
        downloadingZipProgress.setCanceledOnTouchOutside(false);
        downloadingZipProgress.setTitle((!isOnline ? "Downloading..." : "") + "Please wait...");
        downloadingZipProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (!isOnline)
            downloadingZipProgress.show();

    }

    String questionSuffle(String json) {

        try {


            Collections.shuffle(testResponse.getData().getQuestions().getTheoryQuestions());

            Collections.shuffle(testResponse.getData().getQuestions().getPracticalQuestions());

           /* int mval = 0;
            JSONObject jsonObject=new JSONObject(json);
            JSONObject array = (jsonObject).getJSONObject("data");
            JSONObject jsonobj_2 = (JSONObject) array;
            JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
            JSONArray subObjDetailsquestion = null;
           List<JSONObject> listdata = new ArrayList<JSONObject>();

            JSONArray otherlanguagejson = null;
            if(subObjDetails.getJSONArray("theoryQuestions")!=null) {
                subObjDetailsquestion = subObjDetails.getJSONArray("theoryQuestions");
                for (int i = 0; i < subObjDetailsquestion.length(); i++) {

                    listdata.add(subObjDetailsquestion.getJSONObject(i));

                }
                Collections.shuffle(listdata);
                JsonArray jsonArray=    convertListToJsonArray(listdata);
                subObjDetails.put("theoryQuestions",jsonArray);
            }
            if(subObjDetails.getJSONArray("practical_questions")!=null) {
                subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                listdata.clear();
                for (int i = 0; i < subObjDetailsquestion.length(); i++) {

                    listdata.add(subObjDetailsquestion.getJSONObject(i));

                }
                Collections.shuffle(listdata);
                JsonArray jsonArray=   convertListToJsonArray(listdata);
                subObjDetails.put("practical_questions",jsonArray);
            }
            jsonobj_2.put("questions",subObjDetails);
            array.put("data",jsonobj_2);*/
            Gson gson = new Gson();
            return gson.toJson(testResponse);
            /*else {
                if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                    subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    String questionOptionSuffle(String json) {

        try {
            if (testResponse.getData().getQuestions().getTheoryQuestions() != null && testResponse.getData().getQuestions().getTheoryQuestions().size() > 0) {
                for (int i = 0; i < testResponse.getData().getQuestions().getTheoryQuestions().size(); i++) {
                    Collections.shuffle(testResponse.getData().getQuestions().getTheoryQuestions().get(i).getAnswers());
                }
            }
            if (testResponse.getData().getQuestions().getTheoryQuestions() != null && testResponse.getData().getQuestions().getPracticalQuestions().size() > 0) {
                for (int i = 0; i < testResponse.getData().getQuestions().getPracticalQuestions().size(); i++) {
                    Collections.shuffle(testResponse.getData().getQuestions().getPracticalQuestions().get(i).getAnswers());
                }
            }

           /* int mval = 0;
            JSONObject jsonObject=new JSONObject(json);
            JSONObject array = (jsonObject).getJSONObject("data");
            JSONObject jsonobj_2 = (JSONObject) array;
            JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
            JSONArray subObjDetailsquestion = null;
           List<JSONObject> listdata = new ArrayList<JSONObject>();

            JSONArray otherlanguagejson = null;
            if(subObjDetails.getJSONArray("theoryQuestions")!=null) {
                subObjDetailsquestion = subObjDetails.getJSONArray("theoryQuestions");
                for (int i = 0; i < subObjDetailsquestion.length(); i++) {

                    listdata.add(subObjDetailsquestion.getJSONObject(i));

                }
                Collections.shuffle(listdata);
                JsonArray jsonArray=    convertListToJsonArray(listdata);
                subObjDetails.put("theoryQuestions",jsonArray);
            }
            if(subObjDetails.getJSONArray("practical_questions")!=null) {
                subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                listdata.clear();
                for (int i = 0; i < subObjDetailsquestion.length(); i++) {

                    listdata.add(subObjDetailsquestion.getJSONObject(i));

                }
                Collections.shuffle(listdata);
                JsonArray jsonArray=   convertListToJsonArray(listdata);
                subObjDetails.put("practical_questions",jsonArray);
            }
            jsonobj_2.put("questions",subObjDetails);
            array.put("data",jsonobj_2);*/
            Gson gson = new Gson();
            return gson.toJson(testResponse);
            /*else {
                if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                    subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    String getRandomQuestions(String json) {

        try {
            Collections.shuffle(testResponse.getData().getQuestions().getTheoryQuestions());
            String r = testResponse.getData().getScheduleSetttings().getRandomQuestions();
            if (r != null && !r.equalsIgnoreCase("")) {
                int ra = Integer.parseInt(r);
                if (testResponse.getData().getQuestions().getTheoryQuestions().size() >= ra) {
                    while (testResponse.getData().getQuestions().getTheoryQuestions().size() > 10) {
                        testResponse.getData().getQuestions().getTheoryQuestions().remove(testResponse.getData().getQuestions().getTheoryQuestions().size() - 1);
                    }
                }
            }
            Gson gson = new Gson();
            return gson.toJson(testResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    JsonArray convertListToJsonArray(List<JSONObject> jsonObjectArrayList) {
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(jsonObjectArrayList, new TypeToken<List<JSONObject>>() {
        }.getType());

        if (!element.isJsonArray()) {
// fail appropriately
            return null;
        }

        return element.getAsJsonArray();
    }

    private void dismissProgressDialog() {
        if (downloadingZipProgress != null && downloadingZipProgress.isShowing()) {
            downloadingZipProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
}

