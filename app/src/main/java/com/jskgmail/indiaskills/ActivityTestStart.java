package com.jskgmail.indiaskills;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTestStart extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    int count = 0;
    Button complete, practical, theory;
    CheckBox checked;
    TestList testList;
    LinearLayout llCompleteBatch,llannexureM;
    String json;
    boolean annexureMRequired;
    ImageView anxvalidation;
    ImageButton btnAssessorfeedback;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teststart);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        json = Util.getJson(ActivityTestStart.this, testList);

        databaseHelper = new DatabaseHelper(this);
        theory = findViewById(R.id.fab1);
        llannexureM = (LinearLayout)findViewById(R.id.llannexureM);
        btnAssessorfeedback = (ImageButton) findViewById(R.id.btn_assessorfeedback);
        llCompleteBatch = (LinearLayout)findViewById(R.id.llCompleteBatch);
        anxvalidation = (ImageView)findViewById(R.id.anxvalidation);
        practical = findViewById(R.id.fab2);

        btnAssessorfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAnnexureM();
            }
        });
        llannexureM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAnnexureM();
            }
        });
        checked = (CheckBox) findViewById(R.id.checked);
        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if (count == 1) {
                        complete.setEnabled(true);
                    } else {
                        complete.setEnabled(false);
                        checked.setChecked(false);
                        showMessage("", "Please complete at least one Test for completing batch");
                    }
                }

            }
        });
        complete = findViewById(R.id.fab3);
        new GetQuestiontypepresent().execute();

        theory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                new Thread() {

                    public void run() {

                        try {

                            sleep(1000);

                        } catch (Exception e) {

                            Log.e("tag", e.getMessage());

                        }
                    }

                }.start();
                Intent i = (new Intent(ActivityTestStart.this, ActivityInstruction.class));
                i.putExtra(C.TEST, testList);
                i.putExtra(C.ACTIVE_DETAILS, "0");
                startActivity(i);
                finish();
            }
        });

        practical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                new Thread() {

                    public void run() {

                        try {

                            sleep(1000);

                        } catch (Exception e) {
                            Log.e("tag", e.getMessage());
                        }
                    }

                }.start();

                Intent i = (new Intent(ActivityTestStart.this, ActivityInstruction.class));
                i.putExtra(C.ACTIVE_DETAILS, "1");
                i.putExtra(C.TEST, testList);
                startActivity(i);
                finish();
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(ActivityTestStart.this);
                boolean succ = db.update_JSONFORMAT(testList.getId(), "1", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());

                if (succ == true) {
                    if (Util.ONLINE) {

                        deleteddatafromstorage();
                    } else {
                        Intent i = new Intent(ActivityTestStart.this, ActivityTestList.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    showMessage("", "Something went wrong ...");
                }
            }
        });

        if(Util.ONLINE){
            llCompleteBatch.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ActivityTestStart.this, ActivityTestList.class);
            intent.putExtra(C.TEST, testList);
            startActivity(intent);
            finish();        }
        return super.onOptionsItemSelected(item);
    }

    public void completebatch() {
        Map<String, String> params = new HashMap<>();
        ResponseLogin responseLogin = SharedPreference.getInstance(ActivityTestStart.this).getUser(C.LOGIN_USER);
        params.put("userId", responseLogin.getUserID());
        params.put("api_key", responseLogin.getApiKey());
        params.put("test_id", testList.getId());
        params.put("uniqueID", testList.getUniqueID());
        params.put("schedule_id", testList.getScheduleIdPk());
        params.put("latLone", Globalclass.latss);
        params.put("location", Globalclass.longss);
        params.put("batch_id", testList.getBatchIdFk());
        Log.e("params :", params.toString());
        String url = C.API_BATCH_COMPLETE;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params)
                //   null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        Intent i = new Intent(ActivityTestStart.this, ActivityTestList.class);
                        startActivity(i);
                        finish();
                    }
                }, 1 * 1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error response", error);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTestStart.this);
                //  builder.setTitle("Seems it's taking more time then usual !");
                builder.setMessage("Please Try Again");
                builder.setCancelable(true);
                builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completebatch();
                    }
                });
                builder.show();

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
        String tag_json_obj = "json_obj_req";
     //   VolleyAppController.getInstance().addToRequestQueue(request, tag_json_obj);
        RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request).setTag(tag_json_obj);
    }


    public void deleteddatafromstorage() {
        DatabaseHelper myDb = new DatabaseHelper(ActivityTestStart.this);
        boolean success = myDb.delete_byID(testList.getId(), testList.getScheduleIdPk());
        if (success == true) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityTestStart.this);
            View mView = layoutInflater.inflate(R.layout.loading_ques, null);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ActivityTestStart.this);
            builder.setView(mView);
            builder
                    .setCancelable(false)
                    .setPositiveButton("Complete Batch", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here
                            completebatch();
                            //showMessage("",response.toString());

                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                    // deletedatafromlocal();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms
                                            Intent i = new Intent(ActivityTestStart.this, ActivityTestList.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, 7 * 1000);
                                    finish();
                                }
                            });
            android.app.AlertDialog alertDialogAndroid = builder.create();
            alertDialogAndroid.show();
        }
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }

    private class GetQuestiontypepresent extends AsyncTask<String, Void, Void> {
        protected void onPreExecute() {
            getquestionarrycountforpractricaltheory();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            /************ Make Post Call To Web Server ***********/
            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
        }
    }

    public void getquestionarrycountforpractricaltheory() {
        DatabaseHelper mydb = new DatabaseHelper(this);
        String json = "";

        if (Util.ONLINE) {
            json = SharedPreference.getInstance(ActivityTestStart.this).getString(C.ONLINE_TEST_LIST);
        } else {

            Cursor resss = mydb.gettest_details_json_string(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
                return;
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        try {
            String res = json.toString();
            //  JSONParser parser = new JSONParser();
            //  JSONObject json = (JSONObject) parser.parse(res);
            JSONObject array = (new JSONObject(res)).getJSONObject("data");
            //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
            //JSONObject array=json.getJSONObject("data");
            JSONObject jsonobj_2 = (JSONObject) array;
            JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
            JSONArray subObjDetailsquestiontheory = subObjDetails.getJSONArray("theoryQuestions");

            String strtheory = String.valueOf(subObjDetailsquestiontheory.length());

            JSONArray subObjDetailsquestionpractrical = subObjDetails.getJSONArray("practical_questions");

            String strpractrical = String.valueOf(subObjDetailsquestionpractrical.length());

            boolean success = mydb.insert_completedpart(testList.getId(), strpractrical, strtheory, "0", "0", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (success == true) {
                if (strpractrical.equalsIgnoreCase("0")) {
                    practical.setEnabled(false);
                    practical.setBackgroundColor(Color.RED);
                    practical.setVisibility(View.GONE);
                } else {
                    practical.setEnabled(true);
                    // practical.setBackgroundColor(Color.blue());
                }
                if (strtheory.equalsIgnoreCase("0")) {
                    theory.setEnabled(false);
                    theory.setBackgroundColor(Color.RED);
                    theory.setVisibility(View.GONE);
                } else {
                    theory.setEnabled(true);
                }
            }
            Cursor cursor = mydb.getcompletedvaluesflag(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            cursor.moveToNext();
            String strprac = cursor.getString(4);
            String strtheoryflag = cursor.getString(3);
            if (strpractrical.equalsIgnoreCase("0")) {
            } else {
                if (strprac.equalsIgnoreCase("1")) {
                    practical.setEnabled(false);
                    practical.setBackgroundColor(Color.LTGRAY);
                    count = 1;
                }
            }
            if (strtheory.equalsIgnoreCase("0")) {
            } else {
                if (strtheoryflag.equalsIgnoreCase("1")) {
                    theory.setEnabled(false);
                    theory.setBackgroundColor(Color.LTGRAY);
                    count = 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    finish();
                }
            }
        });
    }


    void gotoAnnexureM(){
        databaseHelper = new DatabaseHelper(ActivityTestStart.this);
        boolean b = databaseHelper.getassessorfeedback(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        if (b == true) {
            Util.showMessage(ActivityTestStart.this, getString(R.string.already_given));
        } else {
            Intent intent = new Intent(ActivityTestStart.this, ActivityAnnexureM.class);
            intent.putExtra(C.TEST, testList);
            startActivityForResult(intent, 3);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        isAnnexureMRequired();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("DEBUG","");

       if (requestCode == 3 && resultCode == RESULT_OK) {
            anxvalidation.setVisibility(View.VISIBLE);
        }
    }

    void isAnnexureMRequired(){
        try {
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String am = scheduleSettings.getString("annexure_m");
            if (am != null) {
                if (am.equalsIgnoreCase("0") || am.equalsIgnoreCase("1")) {
                    llannexureM.setVisibility(View.GONE);
                    annexureMRequired = false;

                } else if (am.equalsIgnoreCase("2")) {
                    llannexureM.setVisibility(View.VISIBLE);
                    annexureMRequired = true;
                    anxvalidation.setVisibility(View.GONE);
                } else {
                    llannexureM.setVisibility(View.GONE);
                    annexureMRequired = false;
                }
            }
            databaseHelper = new DatabaseHelper(ActivityTestStart.this);
            boolean b = databaseHelper.getassessorfeedback(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (b) {
                anxvalidation.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
