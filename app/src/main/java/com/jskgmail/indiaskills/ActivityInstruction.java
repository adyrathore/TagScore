package com.jskgmail.indiaskills;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jskgmail.indiaskills.adpater.AdapterCusmtomSpinner;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.test.TestResponse;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityInstruction extends AppCompatActivity {
    Spinner spinner;
    int value = -1;
    String iduser = "";
    DatabaseHelper db;
    Button btnStart;
    TextView view;
    ArrayList<String> Student;
    ArrayList<String> tagid;
    ArrayList<String> useridfk;
    HashMap<Integer, String> spinnerMap;
    HashMap<Integer, String> spinneriduser;
    HashMap<String, String> userid_pk;
    int count = 0;
    final Context c = this;
    CardView cardviewinstruction;
    CheckBox checked;
    Button completebatch;
    String theoryflag = "0", practricalflag = "0";
    ArrayList<String> issubmited;
    ArrayList<String> issubmittedpractical;
    HashMap<String, String> iscompletedpractrical;
    HashMap<String, String> iscompletedtheory;
    TestList testList;
    String activeDetails, usernameSelected;
    String name;
    @BindView(R.id.llComplete)
    LinearLayout llComplete;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    String json;
    TestResponse testResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        json = SharedPreference.getInstance(ActivityInstruction.this).getString(C.TEST_DATA);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        name = SharedPreference.getInstance(ActivityInstruction.this).getString(C.USERNAME);

        view = (TextView) findViewById(R.id.txt_instruction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        spinner = (Spinner) findViewById(R.id.dd_batchlist);
        completebatch = (Button) findViewById(R.id.completebatch);
        cardviewinstruction = (CardView) findViewById(R.id.cardviewinstruction);
        checked = (CheckBox) findViewById(R.id.checked);

        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Util.ONLINE) {

                    completebatch.setEnabled(true);
                } else {
                    DatabaseHelper db = new DatabaseHelper(ActivityInstruction.this);
                    boolean d = db.getusercount(Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId());
                    if (d == true) {
                        completebatch.setEnabled(true);
                    } else {
                        checked.setChecked(false);
                        completebatch.setEnabled(false);
                        showMessage("", "Please complete at least one Test for completing batch");
                    }
                }
            }
        });
        db = new DatabaseHelper(ActivityInstruction.this);
        Student = new ArrayList<>();
        btnStart = (Button) findViewById(R.id.btn_question);
        spinner.setEnabled(true);
        spinner.setVisibility(View.VISIBLE);

        completebatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateAfterTest();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (value == -1) {
                    Toast.makeText(ActivityInstruction.this, "Please select candidate.", Toast.LENGTH_LONG).show();
                    return;
                }
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.custompopup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);
                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.txt_tagid);
                if (BuildConfig.DEBUG) {
                    userInputDialogEditText.setText("mukesh.pandey@indiaskills.edu.in");
                    userInputDialogEditText.setText("swati.gaur@npglobal.in");

                }
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                                String user = userInputDialogEditText.getText().toString();
                                Globalclass.tempQuestionNo = 0;
                                Globalclass.guestioncount = 0;

                                if (Util.ONLINE) {
                                    if (user.equalsIgnoreCase(name)) {
//                                        int value = spinner.getSelectedItemPosition();
                                        String str = tagid.get(value);
                                        String str2 = Student.get(value);
                                        usernameSelected = str2;

                                        //   String iduservalues = spinneriduser.get(spinner.getSelectedItemId());
                                        String idcandidate = userid_pk.get(str);
                                        Globalclass.idcandidate = idcandidate.trim();
                                        Globalclass.userids = str;

                                        Boolean getuservalues = db.getdetailsforbatch(testList.getId(), str, activeDetails);
                                        if (getuservalues == true) {
                                            count = 1;
                                            showMessage("", "User already given Test");
                                        } else {
                                            String completed = "";
                                            if (activeDetails.equalsIgnoreCase("0")) {
                                                completed = iscompletedtheory.get(str);
                                            } else {
                                                completed = iscompletedpractrical.get(str);
                                            }
                                            if (completed.equalsIgnoreCase("0") || completed.equalsIgnoreCase("") || completed.equalsIgnoreCase("null")) {

                                                Globalclass.userids = str;
                                                Globalclass.capture = "0";
                                                if (activeDetails.equalsIgnoreCase("0")) {
                                                    Globalclass.completetype = "T";
                                                } else {
                                                    Globalclass.completetype = "P";
                                                }

                                                int isValidateTheory = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "1");
                                                int isValidatePractical = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "0");
                                                if (isValidateTheory > 1 && activeDetails.equals("0")) {
                                                    gotoStudentInst();
                                                } else if (isValidatePractical > 1 && activeDetails.equals("1")) {
                                                    gotoStudentInst();
                                                } else {
                                                    gotoCandidateValidation(str);
                                                }
                                            } else {
                                                count = 1;
                                                showMessage("", "User already given Test");
                                            }
                                        }
                                    } else {
                                        dialogBox.cancel();
                                        showMessage("VALIDATION ERROR", "TAGID DOES NOT MATCH");
                                    }
                                } else {
                                    boolean b = db.checkUserdropdown(user, user);
                                    if (b == true) {
//                                        int value = spinner.getSelectedItemPosition();
                                        String str = tagid.get(value);
                                        String str2 = Student.get(value);
                                        usernameSelected = str2;
                                        String idcandidate = userid_pk.get(str);
                                        Globalclass.idcandidate = idcandidate.trim();
                                        Globalclass.userids = str;
                                        Boolean getuservalues = db.getdetailsforbatch(testList.getId(), str, activeDetails);
                                        if (getuservalues == true) {
                                            count = 1;
                                            showMessage("", "User already given Test");
                                        } else {
                                            Cursor cursor = db.getdetailsforbatchAbsent(testList.getId(), str);
                                            int cursorCount = cursor.getCount();
                                            if (cursorCount > 0) {
                                                cursor.moveToNext();
                                                String str12 = cursor.getString(6);
                                                if (str12.equalsIgnoreCase("0")) {
                                                    Globalclass.userids = str;
                                                    Globalclass.capture = "0";
                                                    int isValidateTheory = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "0");
                                                    int isValidatePractical = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "1");
                                                    if (isValidateTheory > 0 && activeDetails.equals("0")) {
                                                        gotoStudentInst();
                                                    } else if (isValidatePractical > 0 && activeDetails.equals("1")) {
                                                        gotoStudentInst();
                                                    } else {
                                                        gotoCandidateValidation(str);

                                                    }
                                                } else {
                                                    showMessage("", "User has been marked absent please select another user");
                                                }
                                            } else {
                                                Globalclass.userids = str;
                                                Globalclass.capture = "0";
                                                int isValidateTheory = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "0");
                                                int isValidatePractical = db.getTotalAnsweredGiven(Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "1");
                                                if (isValidateTheory > 0 && activeDetails.equals("0")) {
                                                    gotoStudentInst();
                                                } else if (isValidatePractical > 0 && activeDetails.equals("1")) {
                                                    gotoStudentInst();
                                                } else {
                                                    gotoCandidateValidation(str);

                                                }
                                            }
                                        }
                                    } else {
                                        dialogBox.cancel();
                                        showMessage("VALIDATION ERROR", "TAGID DOES NOT MATCH");
                                    }
                                }
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }

        });
        tagid = new ArrayList<>();
        useridfk = new ArrayList<>();
        issubmited = new ArrayList<>();
        issubmittedpractical = new ArrayList<>();
        new LongOperationsavedetailsdf().execute();
        new Thread() {

            public void run() {

                try {

                    sleep(1000);

                } catch (Exception e) {

                    Log.e("tag", e.getMessage());

                }
                // dismiss the progress dialog
                //  progressDialog.dismiss();

            }

        }.start();

        if (Util.ONLINE) {
            llComplete.setVisibility(View.GONE);
            completebatch.setVisibility(View.GONE);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    return;
                value = i - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ActivityInstruction.this, ActivityTestStart.class);
            intent.putExtra(C.TEST, testList);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void gotoCandidateValidation(String str) {
        suffleQuestion(json);
        String json = Util.getJson(ActivityInstruction.this, testList);

        try {
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String cv = scheduleSettings.getString("candidate_validation");


            if (cv != null) {
                if (cv.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(ActivityInstruction.this, StudentInstruction.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);

                } else if (cv.equalsIgnoreCase("1")) {
                    gotoValidationScreen(str);
                } else {
                    gotoValidationScreen(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void suffleQuestion(String response) {
        try {
            String test_name = "";
            JSONObject array = (new JSONObject(response)).getJSONObject("data");
            JSONObject jsonobj_2 = (JSONObject) array;
            JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                test_name = jsonobj_2_answer.get("schedule_name").toString();

                JSONObject scheduleSettings = Util.getScheduleSettings(response);
                String qA = scheduleSettings.getString("question_Appearence");
                String qoA = "";
                if (scheduleSettings.has("questionoptions_Appearence") && scheduleSettings.getString("questionoptions_Appearence") != null) {
                    qoA = scheduleSettings.getString("questionoptions_Appearence");
                }
                Gson gson = new Gson();
                testResponse = gson.fromJson(response, TestResponse.class);
                if (qA.equalsIgnoreCase("random")) {
                    response = getRandomQuestions(response);
                } else if (qA.equalsIgnoreCase("suffle")) {
                    response = questionSuffle(response);
                }
                if (qoA.equalsIgnoreCase("shuffle") || qoA.equalsIgnoreCase("suffle")) {
                    response = questionOptionSuffle(response);
                }
            }

            if (Util.ONLINE) {
                SharedPreference.getInstance(ActivityInstruction.this).setString(C.ONLINE_TEST_LIST, response);
            } else {
                boolean succ = db.updateJSONFORMAT(testList.getId(), test_name, response, "0", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void gotoValidationScreen(String str) {
        Intent intent = new Intent(ActivityInstruction.this, Newcandidatecapturebefore.class);
        intent.putExtra(C.TAG_ID, str);
        intent.putExtra(C.SELECTED_USERNAME, usernameSelected);
        intent.putExtra(C.TEST, testList);
        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
        startActivity(intent);
        finish();
    }

    void gotoStudentInst() {
        suffleQuestion(json);
        Intent intent = new Intent(ActivityInstruction.this, StudentInstruction.class);
        intent.putExtra(C.TEST, testList);
        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
        startActivity(intent);
    }

    private class LongOperationsavedetailsdf extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {
            if (Util.ONLINE) {
                getstudentonline();
            } else {
                getStudents();
            }
        }

        protected Void doInBackground(String... urls) {
            return null;
        }

        protected void onPostExecute(Void unused) {
            getinstruction();

        }
    }

    public void getinstruction() {
        String json = "";
        if (Util.ONLINE) {
            json = SharedPreference.getInstance(ActivityInstruction.this).getString(C.ONLINE_TEST_LIST);
        } else {
            DatabaseHelper mydb = new DatabaseHelper(this);
            Cursor resss = mydb.gettest_details_json_string(testList.getId(), testList.getUniqueID());
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
            JSONObject array = (new JSONObject(res)).getJSONObject("data");

            JSONObject jsonobj_2 = (JSONObject) array;
            String instructions = "";
            JSONArray subObjDetails = jsonobj_2.getJSONArray("Instructions");
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                instructions = jsonobj_2_answer.get("Instruction").toString();
                view.setText(instructions);
            }
            if (instructions.equalsIgnoreCase("")) {
                view.setText("");
                cardviewinstruction.setVisibility(View.GONE);
            } else {
                view.setText(instructions);
                cardviewinstruction.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void validateAfterTest() {
        {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.custompopup, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.txt_tagid);
            if (BuildConfig.DEBUG) {
                userInputDialogEditText.setText("mukesh.pandey@indiaskills.edu.in");
                userInputDialogEditText.setText("swati.gaur@npglobal.in");


            }
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here
                            String user = userInputDialogEditText.getText().toString();
                            if (Util.ONLINE) {
                                if (user.equalsIgnoreCase(name)) {

                                    completeBatch();

                                } else {
                                    dialogBox.cancel();
                                    showMessage("VALIDATION ERROR", "TAGID DOES NOT MATCH");
                                }
                            } else {
                                boolean b = db.checkUserdropdown(user, user);
                                if (b == true) {
                                    completeBatch();
                                } else {
                                    dialogBox.cancel();
                                    showMessage("VALIDATION ERROR", "TAGID DOES NOT MATCH");
                                }
                            }
                        }
                    })

                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
    }


    void completeBatch() {
        if (activeDetails.equalsIgnoreCase("0")) {
            theoryflag = "1";
        } else if (activeDetails.equalsIgnoreCase("1")) {
            practricalflag = "1";
        }
        DatabaseHelper db = new DatabaseHelper(ActivityInstruction.this);
        boolean success = db.update_record_completedpart(activeDetails, testList.getId(), practricalflag, theoryflag, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        if (success == true) {
            Intent intent = new Intent(ActivityInstruction.this, ActivityTestStart.class);
            intent.putExtra(C.TEST, testList);
            startActivity(intent);
            finish();
        } else {
            showMessage("", "Something went Wrong while completing batch");
        }
    }

    public void getStudents() {
        String json = "";

        if (Util.ONLINE) {
            json = SharedPreference.getInstance(ActivityInstruction.this).getString(C.ONLINE_TEST_LIST);
        } else {
            DatabaseHelper mydb = new DatabaseHelper(this);
            Cursor resss = mydb.gettest_details_json_string(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        try {
            String res = json.toString();

            JSONObject array = (new JSONObject(res)).getJSONObject("data");

            JSONObject jsonobj_2 = (JSONObject) array;
            JSONArray subObjDetails = jsonobj_2.getJSONArray("batch_details");
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                String tagids = jsonobj_2_answer.get("username").toString();
                String name = jsonobj_2_answer.get("name").toString();
                iduser = jsonobj_2_answer.get("user_id_fk").toString();
                Student.add(j, name);
                tagid.add(j, tagids);
                useridfk.add(j, iduser);
            }
            String[] spinnerArray = new String[Student.size()];
            String[] spinnerTitles = new String[Student.size()];
            String[] spinnerPopulation = new String[Student.size()];
            int[] spinnerImages = new int[Student.size()];
            spinnerMap = new HashMap<Integer, String>();
            spinneriduser = new HashMap<Integer, String>();
            userid_pk = new HashMap<String, String>();

            for (int i = 0; i < tagid.size(); i++) {
                spinnerMap.put(i, tagid.get(i));
                spinnerArray[i] = Student.get(i) + "-" + tagid.get(i);
                userid_pk.put(tagid.get(i), useridfk.get(i));
                spinnerTitles[i] = Student.get(i) + "-" + tagid.get(i);
                String flag = null;
                boolean getuservalues = db.getdetailsforbatch(testList.getId(), tagid.get(i).toString(), activeDetails);
                if (getuservalues == true) {
                    Cursor cursor = db.getdetailsforbatchAbsent(testList.getId(), tagid.get(i).toString());
                    int cursorCount = cursor.getCount();
                    if (cursorCount > 0) {
                        cursor.moveToNext();
                        String strvalue = cursor.getString(6);
                        if (strvalue.equalsIgnoreCase("1")) {
                            flag = "3";
                            spinnerPopulation[i] = "2";
                        } else {

                            flag = "1";
                            spinnerPopulation[i] = "1";
                        }
                    } else {

                        flag = "1";
                        spinnerPopulation[i] = "1";
                    }
                } else {
                    Cursor cursor = db.getdetailsforbatchAbsent(testList.getId(), tagid.get(i).toString());
                    int cursorCount = cursor.getCount();
                    if (cursorCount > 0) {
                        cursor.moveToNext();
                        String strvalue = cursor.getString(6);
                        if (strvalue.equalsIgnoreCase("1")) {
                            flag = "3";
                            spinnerPopulation[i] = "2";
                        } else {
                            flag = "2";
                            spinnerPopulation[i] = "0";
                        }
                    } else {
                        flag = "2";
                        spinnerPopulation[i] = "0";
                    }
                }
                if (flag.equalsIgnoreCase("1")) {
                    spinnerImages[i] = R.drawable.checked_24dp;

                } else {
                    spinnerImages[i] = R.drawable.ic_cancel_black_24dp;
                }

                //  spinneriduser.put(i,iduser);
            }

            AdapterCusmtomSpinner mCustomAdapter = new AdapterCusmtomSpinner(ActivityInstruction.this, spinnerTitles, spinnerImages, spinnerPopulation);
            spinner.setAdapter(mCustomAdapter);
            spinner.setSelection(0);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //   builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }

    public void getstudentonline() {
        final Dialog dialog = Util.getProgressDialog(ActivityInstruction.this, R.string.please_wait);
        dialog.show();
        ResponseLogin responseLogin = SharedPreference.getInstance(ActivityInstruction.this).getUser(C.LOGIN_USER);

        Map<String, String> params = new HashMap<>();
        params.put("userId", responseLogin.getUserID());
        params.put("api_key", responseLogin.getApiKey());
        params.put("unique_id", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        params.put("batch_id", testList.getBatchIdFk());
        Log.e("params :", params.toString());
        String url = C.API_GET_BATCH_LIST;
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
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("Data response", String.valueOf(response.toString()));
                // Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                try {
                    //  JSONArray array=response.getJSONArray("testList");
                    JSONArray subObjDetails = response.getJSONArray("Batch_status");
                    for (int j = 0; j < subObjDetails.length(); j++) {
                        JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                        String tagids = jsonobj_2_answer.get("tagId").toString();
                        String name = jsonobj_2_answer.get("Name").toString();
                        String iduserval = jsonobj_2_answer.get("user_id_fk").toString();
                        String issub = jsonobj_2_answer.get("theory").toString();
                        String issubmittedprat = jsonobj_2_answer.get("practical").toString();
                        // String test_descriptions = jsonobj_2_answer.get("test_descriptions").toString();
                        Student.add(j, name);
                        tagid.add(j, tagids);
                        useridfk.add(j, iduserval);
                        issubmited.add(j, issub);
                        issubmittedpractical.add(j, issubmittedprat);
                    }
                    adddropdown();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("VolleyError", "Error response", error);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
        //  VolleyAppController.getInstance().addToRequestQueue(request, tag_json_obj);
        RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request).setTag(tag_json_obj);
    }

    public void adddropdown() {
        String[] spinnerArray = new String[Student.size()];
        String[] spinnerTitles = new String[Student.size()];
        String[] spinnerPopulation = new String[Student.size()];
        int[] spinnerImages = new int[Student.size()];
        spinnerMap = new HashMap<Integer, String>();
        iscompletedpractrical = new HashMap<>();
        iscompletedtheory = new HashMap<>();
        spinneriduser = new HashMap<Integer, String>();
        userid_pk = new HashMap<String, String>();
        for (int i = 0; i < tagid.size(); i++) {
            spinnerMap.put(i, tagid.get(i));
            spinnerArray[i] = Student.get(i) + "-" + tagid.get(i);
            userid_pk.put(tagid.get(i), useridfk.get(i));
            iscompletedtheory.put(tagid.get(i), issubmited.get(i));
            iscompletedpractrical.put(tagid.get(i), issubmittedpractical.get(i));
            //  spinneriduser.put(i,iduser);
            spinnerTitles[i] = Student.get(i) + "-" + tagid.get(i);
            if (activeDetails.equalsIgnoreCase("0")) {
                spinnerPopulation[i] = issubmited.get(i);
            } else {
                spinnerPopulation[i] = issubmittedpractical.get(i);
            }
            if (activeDetails.equalsIgnoreCase("0")) {
                String str = issubmited.get(i).toString();
                if (str == null || str.equalsIgnoreCase("0")) {
                    spinnerImages[i] = R.drawable.ic_cancel_black_24dp;
                    spinnerPopulation[i] = "1";
                } else if (str != null && str.equalsIgnoreCase("1")) {
                    spinnerImages[i] = R.drawable.checked_24dp;
                    spinnerPopulation[i] = "0";
                } else {

                    spinnerImages[i] = R.drawable.ic_cancel_black_24dp;
                    spinnerPopulation[i] = "1";
                }
            }
            if (activeDetails.equalsIgnoreCase("1")) {
                String str = issubmittedpractical.get(i).toString();
                if (str == null || str.equalsIgnoreCase("0")) {
                    spinnerImages[i] = R.drawable.ic_cancel_black_24dp;
                    spinnerPopulation[i] = "1";
                } else if (str != null && str.equalsIgnoreCase("1")) {
                    spinnerImages[i] = R.drawable.checked_24dp;
                    spinnerPopulation[i] = "0";
                } else {

                    spinnerImages[i] = R.drawable.ic_cancel_black_24dp;
                    spinnerPopulation[i] = "1";
                }
            }

        }
        AdapterCusmtomSpinner mCustomAdapter = new AdapterCusmtomSpinner(ActivityInstruction.this, spinnerTitles, spinnerImages, spinnerPopulation);
        spinner.setAdapter(mCustomAdapter);
        spinner.setSelection(0);

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


    String questionSuffle(String json) {

        try {


            Collections.shuffle(testResponse.getData().getQuestions().getTheoryQuestions());

            Collections.shuffle(testResponse.getData().getQuestions().getPracticalQuestions());


            Gson gson = new Gson();
            return gson.toJson(testResponse);
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


            Gson gson = new Gson();
            return gson.toJson(testResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    String getRandomQuestions(String json) {

        try {
            Collections.shuffle(testResponse.getData().getQuestions().getTheoryQuestions());
            // String r = testResponse.getData().getScheduleSetttings().getRandomQuestions();
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String r = scheduleSettings.getString("randomQuestions");
            if (r != null && !r.equalsIgnoreCase("")) {
                int ra = Integer.parseInt(r);
                if (testResponse.getData().getQuestions().getTheoryQuestions().size() >= ra) {
                    while (testResponse.getData().getQuestions().getTheoryQuestions().size() > ra) {
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

}
