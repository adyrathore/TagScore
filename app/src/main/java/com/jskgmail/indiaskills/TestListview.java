package com.jskgmail.indiaskills;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.util.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TestListview extends AppCompatActivity {
    ListView listView1;
    DatabaseHelper myDb ;
    String testidval,testuniquekey,schduleidfinal;
    String batchidvalues;
    int valuessuccess = 0;
    String Testidupload = "";
    String testpostuniqueid = "";
    final  Context context = this;
    String isuccess = "0";
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
  //  String ip,password,un,db,name;
  //  Connection connection;
    ResultSet resultSet;
    String query;
    String isconducted="";
    PreparedStatement preparedStatement;
    LocationManager locationManager ;
    boolean GpsStatus;
    ImageButton buttonback;
    private ProgressDialog progressDialog;
    String userid, apikey;
    ProgressDialog Dialog;
    final Context cursor = this;
  TextView   nameuser;
  ImageView imgforconnet;
    public void GPSStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_test_listview);
        nameuser = (TextView) findViewById(R.id.nameuser);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        imgforconnet = (ImageView) findViewById(R.id.imgforconnet);
        if(ActivityMain.online)
        {
            imgforconnet.setImageResource(R.drawable.online);
        }
        else
        {
            imgforconnet.setImageResource(R.drawable.offline);
        }
        listView1 = (ListView)findViewById(R.id.listView_notification);
        menuItems.clear();
        buttonback = (ImageButton) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestListview.this,ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        //new LongOperation().execute();
        progressDialog = ProgressDialog.show(TestListview.this, "", "Loading...");
        nameuser.setText("Welcome (" + Globalclass.name+" )");
        new Thread() {

            public void run() {

                try{

                    sleep(1000);

                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
// dismiss the progress dialog
                progressDialog.dismiss();
            }

        }.start();
        Dialog = new ProgressDialog(TestListview.this);
        Dialog.setMessage("Please wait..");
        Dialog.show();
        apikey = Globalclass.apikeys;
        userid = Globalclass.userids;
        Testdetail(apikey,userid);

       // deleteCache(TestListview.this);
        if(Globalclass.looptestlistviewval.equalsIgnoreCase("1")){
            Globalclass.looptestlistviewval = "0";
            validatevalueupload();
        }
        else {

        }
        if(Globalclass.uplodingflagvalues.equalsIgnoreCase("1")){
            Globalclass.uplodingflagvalues = "0";
            validatevalueuploadtoserver();
        }
    }

    public void  validatevalueuploadtoserver(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMessage("","Test Uploaded Successfully.");
                //  finish();
            }
        }, 2*1000);
    }

    public  void validatevalueupload(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMessage("","Test Downloaded Successfully.");
                //  finish();
            }
        }, 2*1000);
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    protected void Testdetail(String apikey,String userid) {
        if(ActivityMain.online) {
            Map<String, String> params = new HashMap<>();
            params.put("api_key", apikey);
            params.put("userId", Globalclass.id_login);
            params.put("role",Globalclass.roleval);
            Log.e("params :", params.toString());
            String url1 = C.API_TEST_LIST;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url1, new JSONObject(params)
                    //   null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Data response", String.valueOf(response.toString()));

                 //   Toast.makeText(getApplicationContext(), "succes1s", Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray array = response.getJSONArray("testList");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobj_2 = (JSONObject) array.get(i);
                            String test_id = (String) jsonobj_2.get("id");
                            String u_test_id = (String) jsonobj_2.get("uniqueID");
                            String purchasedTime = (String) jsonobj_2.get("purchasedTime");
                            String testName = (String) jsonobj_2.get("testName");
                            String testtype = jsonobj_2.get("test_type").toString();
                            final String schduleid = jsonobj_2.get("schedule_id_pk").toString();
                            String batchid = jsonobj_2.get("batch_id_fk").toString();
                            if (testtype.isEmpty()) {
                                testtype = "0";
                            }
                            map = new HashMap<String, String>();
                            map.put("test_id", test_id);
                            map.put("u_test_id", u_test_id);
                            map.put("purchasedtime", schduleid);
                            map.put("testname", testName);
                            map.put("testtype", testtype);
                            map.put("role",Globalclass.roleval);
                            map.put("batchid",batchid);
                            menuItems.add(map);
                            ListAdapter k;
                            k = new SimpleAdapter(TestListview.this, menuItems, R.layout.new_test,
                                    new String[]{"test_id", "u_test_id", "purchasedtime", "testname", "testtype","batchid"},
                                    new int[]{R.id.ABNNotext, R.id.BatchNo, R.id.Contact_Persontext, R.id.ABNNo, R.id.VTPNametext, R.id.txt_batchid}) {
                                @Override
                                public View getView(final int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    final View readmore = v.findViewById(R.id.Remark);
                                    final View feedback = v.findViewById(R.id.button1);
                                    final View evidence = v.findViewById(R.id.button3);
                                    final View testid = v.findViewById(R.id.ABNNotext);
                                    final View uniqueid = v.findViewById(R.id.BatchNo);
                                    final View purchagetime = v.findViewById(R.id.Contact_Persontext);
                                    final View testname = v.findViewById(R.id.ABNNo);
                                    final View testtype = v.findViewById(R.id.VTPNametext);
                                    final View batchidval = v.findViewById(R.id.txt_batchid);
                                    final View checked = v.findViewById(R.id.answersid);
                                    ImageView checked12 = (ImageView)  checked;
                                    checked12.setVisibility(View.GONE);
                                    TextView tv_Date = (TextView) testid;
                                    final String BatchNo1 = tv_Date.getText().toString();
                                    TextView Batcid = (TextView) batchidval;
                                    final String Batchid = Batcid.getText().toString();
                                    TextView isconducted1 = (TextView) testtype;
                                    final String isconducted12 = isconducted1.getText().toString();
                                    TextView tv_image = (TextView) uniqueid;
                                    final String ABNNo1 = tv_image.getText().toString();
                                    TextView tv_title = (TextView) purchagetime;
                                    final String VTPName1 = tv_title.getText().toString();
                                    TextView tv_Contact_Person = (TextView) testname;
                                    final String Contact_Person1 = tv_Contact_Person.getText().toString();
                                    final ImageButton button1 = (ImageButton) feedback;
                                    final ImageButton buttomm2 = (ImageButton) evidence;
                                    button1.setVisibility(View.GONE);
                                    buttomm2.setVisibility(View.GONE);
                                    final Button Remark = (Button) readmore;
                                    boolean str = myDb.getAllDate_batchid(VTPName1);
                                    if (str == true) {
                                        Remark.setEnabled(false);
                                        checked12.setVisibility(View.GONE);
                                       // Remark.setBackgroundColor(Color.RED);
                                    } else { 
                                        Remark.setEnabled(true);
                                        checked12.setVisibility(View.GONE);
                                       // Remark.setBackgroundColor(Color.GRAY);
                                    }
                                    if (isconducted12.trim().equals("1")) {
                                        Remark.setText("Download");
                                        checked12.setVisibility(View.GONE);
                                      //  Remark.setTextColor(Color.WHITE);
                                    } else {
                                        Remark.setText("Start Test");
                                        button1.setVisibility(View.GONE);
                                        buttomm2.setVisibility(View.GONE);
                                      //  Remark.setTextColor(Color.WHITE);
                                    }
                                    boolean b = myDb.isTestCompletedOrNot(VTPName1);
                                    if(b == true)
                                    {
                                        Remark.setText("Upload");
                                        Remark.setEnabled(true);
                                        button1.setVisibility(View.GONE);
                                        buttomm2.setVisibility(View.GONE);
                                      //  Remark.setBackgroundColor(Color.BLUE);
                                      //  Remark.setTextColor(Color.WHITE);
                                        checked12.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        button1.setVisibility(View.GONE);
                                        buttomm2.setVisibility(View.GONE);
                                    }
                                    if(Remark.getText().toString().equalsIgnoreCase("Start Test")){
                                        buttomm2.setVisibility(View.GONE);
                                    }
                                    if(Globalclass.roleval.equalsIgnoreCase("3"))
                                    {
                                        buttomm2.setVisibility(View.GONE);
                                        button1.setVisibility(View.GONE);
                                    }
                                    buttomm2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            testidval = BatchNo1.toString();
                                            testuniquekey = ABNNo1.toString();
                                            String schduleid = ABNNo1.toString();
                                            Globalclass.batchidoffline = testidval;
                                            Globalclass.schduleid = schduleidfinal;
                                            Testidupload =   BatchNo1;
                                            testpostuniqueid = ABNNo1;
                                            schduleidfinal = VTPName1;
                                            batchidvalues = Batchid;
                                            Globalclass.Testidupload= Testidupload;
                                            Globalclass.schduleidfinal = schduleidfinal;
                                            Globalclass.testpostuniqueid = testpostuniqueid;
                                       //     Intent intent = new Intent(TestListview.this,EvidenceActivity.class);
                                        //    startActivity(intent);
                                            //  finish();
                                        }
                                    });
                                    Remark.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            GPSStatus();
                                            if(GpsStatus == true) {
                                                testidval = BatchNo1.toString();
                                                testuniquekey = ABNNo1.toString();
                                                schduleidfinal = VTPName1;
                                                batchidvalues = Batchid;
                                                Globalclass.batchidvalues = batchidvalues;
                                                //  MyGlobalClass myGlobalClass = new MyGlobalClass();
                                                //  Content cn = new  Content();
                                                // String str = myGlobalClass.checkRemark(BatchNo1);
                                                //  String date = getDateTime();
                                                Dialog = new ProgressDialog(TestListview.this);
                                                //  Dialog.setMessage("Please wait..");
                                                //  Dialog.show();
                                                if (Remark.getText().toString().trim().equals("Download")) {
                                                    //
                                                    new LongOperationsavedetailsfortest().execute();
                                                    //
                                                    //
                                                } else if (Remark.getText().toString().equalsIgnoreCase("Start Test")) {
                                                    // Intent io = new Intent(TestListview.this, PatternActivity.class);
                                                    Testidupload = BatchNo1;
                                                    testpostuniqueid = ABNNo1;
                                                    schduleidfinal = VTPName1;
                                                    batchidvalues = Batchid;
                                                    Globalclass.Testidupload = Testidupload;
                                                    Globalclass.schduleidfinal = schduleidfinal;
                                                    //  Globalclass.testpostuniqueid = testpostuniqueid;
                                                    Globalclass.Testidupload = Testidupload;
                                                    Globalclass.schduleidfinal = schduleidfinal;
                                                    Globalclass.testpostuniqueid = testpostuniqueid;
                                                    Globalclass.schduleid = schduleid;
                                                    getdetailsonline();
                                                    //  io.putExtra("p", "p");
                                                    //  startActivity(io);
                                                    // startActivity(new Intent(MainActivity.this, Main5Activity.class));
                                                    //   startActivity(new Intent(MainActivity.this, BatchListActivity.class));
                                                    //  Log.e("ttttttteeeeeeeeesssst", map.toString());
                                                    // startActivity(intent);
                                                    //  finish();
                                                } else if (Remark.getText().toString().equalsIgnoreCase("Upload")) {
                                                    Testidupload = BatchNo1;
                                                    testpostuniqueid = ABNNo1;
                                                    schduleidfinal = VTPName1;
                                                    batchidvalues = Batchid;
                                                    new LongOperationsavedetailsfortestUploadvalues().execute();
                                                }
                                            }
                                            else {
                                                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);

                                                builder.setMessage("Your GPS is not Enabled Please Enable it for processing further");
                                                builder.setCancelable(false);
                                                builder.setNeutralButton("Enable", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                        startActivity(intent1);

                                                        //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        }
                                    });
                                    return v;
                                }
                            };
                            listView1.setAdapter(k);
                            Dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                   // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);
                  //  builder.setTitle("Seems it's taking more time then usual !");
                    builder.setMessage("Please Try Again");
                    builder.setCancelable(true);
                    builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                            Testdetail(Globalclass.apikeys,Globalclass.id_login);
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
            // Adding request to request queue
            String tag_json_obj = "json_obj_req";
            //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
          //  VolleyAppController.getInstance().addToRequestQueue(request, tag_json_obj);
            int socketTimeout = 900000000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            Volley.newRequestQueue(this).add(request);
        }
        else
        {
            Cursor res = myDb.getAll_test_list();
            if(res.getCount() == 0) {
                // show message
                showMessage("Error","No test downloaded found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                map = new HashMap<String, String>();
                map.put("test_id", res.getString(0));
                map.put("u_test_id", res.getString(4));
                map.put("purchasedtime", res.getString(3));
                map.put("testname", res.getString(2));
                map.put("testtype", "0");
                menuItems.add(map);

                ListAdapter k;
                k = new SimpleAdapter(TestListview.this, menuItems, R.layout.new_test,
                        new String[]{"test_id", "u_test_id", "purchasedtime", "testname", "testtype"},
                        new int[]{R.id.ABNNotext, R.id.BatchNo, R.id.Contact_Persontext, R.id.ABNNo, R.id.VTPNametext}) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        final View readmore = v.findViewById(R.id.Remark);
                        final View testid = v.findViewById(R.id.ABNNotext);
                        final View feedback = v.findViewById(R.id.button1);
                        final View evidence = v.findViewById(R.id.button3);
                        final View uniqueid = v.findViewById(R.id.BatchNo);
                        final View purchagetime = v.findViewById(R.id.Contact_Persontext);
                        final View testname = v.findViewById(R.id.ABNNo);
                        final View testtype = v.findViewById(R.id.VTPNametext);
                        final View checked = v.findViewById(R.id.answersid);
                        ImageView checked12 = (ImageView)  checked;
                        checked12.setVisibility(View.GONE);
                        TextView tv_Date = (TextView) testid;
                        final String BatchNo1 = tv_Date.getText().toString();
                        TextView isconducted1 = (TextView) testtype;
                        final String isconducted12 = isconducted1.getText().toString();
                        TextView tv_image = (TextView) uniqueid;
                        final String ABNNo1 = tv_image.getText().toString();
                        TextView tv_title = (TextView) purchagetime;
                        final String VTPName1 = tv_title.getText().toString();
                        TextView tv_Contact_Person = (TextView) testname;
                        final String Contact_Person1 = tv_Contact_Person.getText().toString();
                        final ImageButton button1 = (ImageButton) feedback;
                        final ImageButton buttomm2 = (ImageButton) evidence;
                        button1.setVisibility(View.GONE);
                        buttomm2.setVisibility(View.GONE);
                        buttomm2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                testidval = BatchNo1.toString();
                                testuniquekey = ABNNo1.toString();
                                String schduleid = ABNNo1.toString();
                                Globalclass.batchidoffline = testidval;
                                Globalclass.schduleid = schduleid;
                              //  Intent intent = new Intent(TestListview.this,EvidenceActivity.class);
                               // startActivity(intent);
                              //  finish();
                            }
                        });
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                testidval = BatchNo1.toString();
                                testuniquekey = ABNNo1.toString();
                                String schduleid = ABNNo1.toString();
                                Globalclass.batchidoffline = testidval;
                                Globalclass.schduleid = schduleid;
                                DatabaseHelper db = new DatabaseHelper(TestListview.this);
                                boolean b = db.getassessorfeedback(Globalclass.batchidoffline,Globalclass.schduleid);
                                if(b == true)
                                {
                                    showMessage("", "Assessor Feedback Already Given");
                                }
                                else {
                                    Intent intent = new Intent(TestListview.this,ActivityAnnexureM.class);
                                    startActivity(intent);
                                }
                              //  Intent intent = new Intent(TestListview.this,Assessorfeedbackform.class);
                               // startActivity(intent);
                                //finish();
                            }
                        });
                        final Button Remark = (Button) readmore;
                        if(VTPName1.equalsIgnoreCase("1")){
                           // Remark.setBackgroundColor(Color.LTGRAY);
                            Remark.setEnabled(false);
                            Remark.setText("Upload");
                            buttomm2.setEnabled(true);
                            button1.setEnabled(true);
                            checked12.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            button1.setEnabled(false);
                            buttomm2.setEnabled(false);
                            Remark.setText("Take Test");
                            Remark.setEnabled(true);
                            checked12.setVisibility(View.GONE);
                        }
                        Remark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GPSStatus();
                                if(GpsStatus == true) {
                                    testidval = BatchNo1.toString();
                                    testuniquekey = ABNNo1.toString();
                                    String schduleid = ABNNo1.toString();
                                    //  MyGlobalClass myGlobalClass = new MyGlobalClass();
                                    //  Content cn = new  Content();
                                    // String str = myGlobalClass.checkRemark(BatchNo1);
                                    //  String date = getDateTime();
                                    Dialog = new ProgressDialog(TestListview.this);
                                    //  Dialog.setMessage("Please wait..");
                                    //  Dialog.show();
                                    if (Remark.getText().toString().trim().equals("Download")) {
                                        //
                                        // new LongOperationsavedetailsfortest().execute();
                                        //
                                        //
                                    } else {
                                        Intent io = new Intent(TestListview.this, TestDetails.class);


                                        io.putExtra("p", testidval);
                                        io.putExtra("q", schduleid);
                                        startActivity(io);
                                        // startActivity(new Intent(MainActivity.this, Main5Activity.class));
                                        //   startActivity(new Intent(MainActivity.this, BatchListActivity.class));
                                        Log.e("ttttttteeeeeeeeesssst", map.toString());
                                        // startActivity(intent);
                                        finish();
                                    }
                                }
                                else {
                                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);

                                    builder.setMessage("Your GPS is not Enabled Please Enable it for processing further");
                                    builder.setCancelable(false);
                                    builder.setNeutralButton("Enable", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(intent1);

                                            //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.show();

                                }
                            }
                        });
                        return v;
                    }

                };

                listView1.setAdapter(k);
                Dialog.dismiss();

            }
        }
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
      //  builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }
    private class LongOperationsavedetailsfortest  extends AsyncTask<String, Void, Void> {
        // Required initialization
        // private final HttpClient Client = new DefaultHttpClient();
       // private String Content1;
       // private String Error = null;
       // private ProgressDialog Dialog1 = new ProgressDialog(TestListview.this);
      //  String  data ="nFlg";
        //TextView uiUpdate = (TextView) findViewById(R.id.output);
        //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;

        //EditText serverText = (EditText) findViewById(R.id.serverText);
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

        }
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            /*****************************************************/
            gettestquestionarydetailsdave();
            return null;
        }
        protected void onPostExecute(Void unused) {
            validateimageupdate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public  void validateimageupdate(){
        progressDialog = ProgressDialog.show(TestListview.this, "", "Downloading... Please Wait.");

        new Thread() {

            public void run() {

                try{

                    sleep(10000 );

                } catch (Exception e) {

                    Log.e("tag", e.getMessage());

                }
                    Globalclass.looptestlistviewval = "1";
                    Intent intent = new Intent(TestListview.this,TestListview.class);
                    startActivity(intent);
                    finish();
        //       showMessage("","Test Saved Successfully...");
            }

        }.start();

    }
    public  void    getdetailsonline(){
        final String successvalue = "done";
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Globalclass.apikeys);
        params.put("userId", Globalclass.id_login);
        params.put("testID",testidval);
        params.put("uniqueId",testuniquekey);
        params.put("languageCode","en");
        Log.e("params :",params.toString());
        String url1 = C.API_TEST_DETAILS_DOWNLOAD;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url1, new JSONObject(params)
                //   null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String batch_id_fk = "";
                // JSONObject jObj = new JSONObject(response);
                Log.e("Data response", String.valueOf(response.toString()));
                //  Toast.makeText(getApplicationContext(),"get All details of test",Toast.LENGTH_SHORT).show();
                try {
                    String res = response.toString();
                    //  JSONParser parser = new JSONParser();
                    //  JSONObject json = (JSONObject) parser.parse(res);
                    JSONObject array = (new JSONObject(res)).getJSONObject("data");
                    //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
                    //JSONObject array=json.getJSONObject("data");

                    JSONObject jsonobj_2 = (JSONObject) array;
                    JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
                    for (int j = 0; j < subObjDetails.length(); j++) {
                        JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                        String test_name = jsonobj_2_answer.get("test_name").toString();
                         Globalclass.onlinjson = res;
                         Globalclass.batchidoffline = testidval;
                         Globalclass.schduleidfinal = schduleidfinal;
                         Globalclass.schduleid = schduleidfinal;
                         if( Globalclass.roleval.equalsIgnoreCase("3"))
                         {
                             Globalclass.activedetails = "0";
                             Globalclass.userids =  Globalclass.tagid;
                             Globalclass.completetype = "T";
                             Globalclass.idcandidate = Globalclass.id_login;
                             Intent io = new Intent(TestListview.this,Newcandidatecapturebefore.class);
                             startActivity(io);
                             finish();
                         }
                         else {
                             Intent io = new Intent(TestListview.this, TestDetails.class);
                             io.putExtra("p", testidval);
                             io.putExtra("q", schduleidfinal);
                             startActivity(io);
                             // startActivity(new Intent(MainActivity.this, Main5Activity.class));
                             //   startActivity(new Intent(MainActivity.this, BatchListActivity.class));
                             Log.e("ttttttteeeeeeeeesssst", map.toString());
                             // startActivity(intent);
                             finish();
                         }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // startActivity(io);

                // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError","Error response", error);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);
            //    builder.setTitle("Seems it's taking more time then usual !");
                builder.setMessage("Please Try Again");
                builder.setCancelable(true);
                builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                       // Testdetail(Globalclass.apikeys,Globalclass.id_login);
                        getdetailsonline();
                    }
                });
                builder.show();
                // Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                String credentials = "tagusp:t@g$c0re";
                String auth = "Basic"+" "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //  headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        String tag_json_obj = "json_obj_req";
        //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
       // VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
        int socketTimeout = 900000000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(request);
    }

    public  String gettestquestionarydetailsdave(){
        //getting test details values in arraylist
        // by suramya
       // private ProgressDialog Dialog1 = new ProgressDialog(TestListview.this);
        //Dialog.show();
       // Dialog.setMessage("Saving Question Details please wait..");
        String successvalue = "done";
        Map<String, String> params = new HashMap<>();
        params.put("api_key", Globalclass.apikeys);
        params.put("userId", Globalclass.id_login);
        params.put("testID",testidval);
        params.put("uniqueId",testuniquekey);
        params.put("languageCode","en");
        Log.e("params :",params.toString());
        String url1 = C.API_TEST_DETAILS_DOWNLOAD;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url1, new JSONObject(params)
                //   null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String batch_id_fk = "";
               // JSONObject jObj = new JSONObject(response);
                Log.e("Data response", String.valueOf(response.toString()));
              //  Toast.makeText(getApplicationContext(),"get All details of test",Toast.LENGTH_SHORT).show();
                try {
                    String res = response.toString();
                  //  JSONParser parser = new JSONParser();
                  //  JSONObject json = (JSONObject) parser.parse(res);
                    JSONObject array = (new JSONObject(res)).getJSONObject("data");
                    //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
                    //JSONObject array=json.getJSONObject("data");

                        JSONObject jsonobj_2 = (JSONObject) array;
                        JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
                        for (int j = 0; j < subObjDetails.length(); j++) {
                            JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                            String test_name = jsonobj_2_answer.get("test_name").toString();
                            boolean already = myDb.getcompletedvaluesflaginserted(testidval, schduleidfinal);
                            if (already == true) {
                            } else {
                                boolean isInserted = myDb.insertJSONFORMAT(testidval, test_name, res, "0", schduleidfinal);
                                if (isInserted == true) {
                                    isuccess = "1";
                                  //  Toast.makeText(TestListview.this, "j:" + j, Toast.LENGTH_LONG).show();
                                } else {
                                   // Toast.makeText(TestListview.this, "j not:" + j, Toast.LENGTH_LONG).show();
                                }
                            }
                            boolean Insertest5 = myDb.insert_table_userlogin(Globalclass.tagid, Globalclass.password, Globalclass.apikeys);
                            if (Insertest5 == true) {
                                isuccess = "2";
                               // Toast.makeText(TestListview.this, "insertedlastpassword", Toast.LENGTH_LONG).show();
                            } else {
                               // Toast.makeText(TestListview.this, "not insertedpassword", Toast.LENGTH_LONG).show();
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // startActivity(io);

                // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError","Error response", error);
               // Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);
           //     builder.setTitle("Seems it's taking more time then usual !");
                builder.setMessage("Please Try Again");
                builder.setCancelable(true);
                builder.setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                        // Testdetail(Globalclass.apikeys,Globalclass.id_login);
                        gettestquestionarydetailsdave();
                    }
                    });
                builder.show();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                String credentials = "tagusp:t@g$c0re";
                String auth = "Basic"+" "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //  headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        String tag_json_obj = "json_obj_req";
        //  VolleyAppController.getInstance().getRequestQueue().getCache().remove(url);
      //  VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
        int socketTimeout = 900000000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(request);
        return successvalue;
    }

    private class LongOperationsavedetailsfortestUploadvalues  extends AsyncTask<String, Void, Void> {
        // Required initialization
        // private final HttpClient Client = new DefaultHttpClient();
        private String Content1;
        private String Error = null;
      //  sdfsdfsdf
        String  data ="nFlg";
        //TextView uiUpdate = (TextView) findViewById(R.id.output);
        //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;

        //EditText serverText = (EditText) findViewById(R.id.serverText);
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message
                getjsonformat();
        }
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            /************ Make Post Call To Web Server ***********/
            /*****************************************************/
            return null;
        }
        protected void onPostExecute(Void unused) {
            progressbarforvalues();
        }
    }

    public  void progressbarforvalues(){
        progressDialog = ProgressDialog.show(TestListview.this, "", "Uploading... Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);

    }


    public  void submitfeedback() {
     //   showMessageprogress("","Uploding Feedback");
        final Cursor cursor = myDb.getAllfeedbackdatavalues(Testidupload,schduleidfinal);
        if(cursor.getCount() > 0) {
            JSONArray array = cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data",(Object)array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tempNames = array.toString();
            String strsddata = sd.toString();
            //String finalData = strsddata.replace("/\\/", "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", Globalclass.id_login);
            params.put("api_key", Globalclass.apikeys);
            params.put("schedule_unique_key",Globalclass.testpostuniqueid);
            params.put("feedback",strsddata.trim());
            Log.e("params :", params.toString());
            // showMessage("message",params.toString());
            String url = C.API_SUBMIT_FEEDBACK_URL;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params)
                    //   null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Data response", String.valueOf(response.toString()));
                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           // Intent intent = new Intent(TestListview.this,Uploadingservice.class);
                           // startService(intent);
                           // DatabaseHelper db = new DatabaseHelper(TestListview.this);
                           // Cursor cursor  = db.getAllImages(Globalclass.Testidupload,Globalclass.schduleidfinal);
                          // String imagetypeval= "",hashcaptureval="",nameimageval="",testidval= "",imageurlval = "",tagidval = "";
                           // while (cursor.moveToNext()) {
                                // Toast.makeText(Uploadingservice.this,cursor.getPosition(),Toast.LENGTH_LONG).show();
                             //   imagetypeval = imagetypeval +"," +cursor.getString(0);
                             // hashcaptureval = hashcaptureval + "," + cursor.getString(1);
                            //  nameimageval = nameimageval +"," + cursor.getString(2);
                             // testidval = testidval +"," + cursor.getString(3);
                             // imageurlval = imageurlval +"," + cursor.getString(4);
                             // tagidval = tagidval +"," +  cursor.getString(5);
                           // }
                           // showMessage("",imagetypeval + hashcaptureval + nameimageval + testidval + imageurlval + tagidval );

                            //Do something after 100ms
                            // deleteCache(TestListview.this);
                              Intent intent = new Intent(TestListview.this,zippingactivity.class);
                              startActivity(intent);
                            //intent.putExtra("Testidupload", Globalclass.Testidupload);
                           // intent.putExtra("schduleidfinal", Globalclass.schduleidfinal);
                           //  startService(intent);
                            finish();
                        }
                    }, 2*1000);
                //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        //showMessage("mess",response.toString());
                      //  String message = String.valueOf(response.get("message"));
                       // showMessage("error",response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           //  Intent intent = new Intent(TestListview.this,Uploadingservice.class);
                           //  startService(intent);
                          //  DatabaseHelper db = new DatabaseHelper(TestListview.this);
                          //  Cursor cursor  = db.getAllImages(Globalclass.Testidupload,Globalclass.schduleidfinal);
                          //  String imagetypeval= "",hashcaptureval="",nameimageval="",testidval= "",imageurlval = "",tagidval = "";
                           // while (cursor.moveToNext()) {
                          // Toast.makeText(Uploadingservice.this,cursor.getPosition(),Toast.LENGTH_LONG).show();
                            //  imagetypeval = imagetypeval +"," +cursor.getString(0);
                            //  hashcaptureval = hashcaptureval + "," + cursor.getString(1);
                            //  nameimageval = nameimageval +"," + cursor.getString(2);
                            //  testidval = testidval +"," + cursor.getString(3);
                            //  imageurlval = imageurlval +"," + cursor.getString(4);
                            //  tagidval = tagidval +"," +  cursor.getString(5);
                           // }
                          // showMessage("",imagetypeval + hashcaptureval + nameimageval + testidval + imageurlval + tagidval );
                            //Do something after 100ms
                            // deleteCache(TestListview.this);
                              Intent intent = new Intent(TestListview.this,zippingactivity.class);
                              startActivity(intent);
                            //  startService(intent);
                              finish();
                        }
                    }, 2*1000);
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
          //  VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
            int socketTimeout = 900000000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            Volley.newRequestQueue(this).add(request);
        }
        else
        {
            //Snackbar snackbar = Snackbar.make(view, "User Details Does Not Match ", Snackbar.LENGTH_LONG);
            // snackbar.show();
            Cursor res = myDb.getdetailsoflluseranswer();
            if(res.getCount() == 0) {
                // show message
                showMessage("Error","Nothing found");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("tagid :"+ res.getString(0)+"\n");
                buffer.append("pass :"+ res.getString(1)+"\n");
                buffer.append("apikey :"+ res.getString(2)+"\n");
                // buffer.append("Marks :"+ res.getString(3)+"\n\n");
            }
            // Show all data
            showMessage("Data",buffer.toString());
        }
    }
    public  void getjsonformat() {
        Globalclass.Testidupload= Testidupload;
        Globalclass.schduleidfinal = schduleidfinal;
        Globalclass.testpostuniqueid = testpostuniqueid;

        final Cursor cursor = myDb.getAllquestiondetilsvaluploadback(Testidupload,schduleidfinal);
        if(cursor.getCount() > 0) {
            JSONArray array = cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data",(Object)array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tempNames = array.toString();
            String strsddata = sd.toString();
            String finalData = strsddata.replace("/\\/", "");
            Map<String, String> params = new HashMap<>();
            params.put("userId", Globalclass.id_login);
            params.put("api_key", Globalclass.apikeys);
            params.put("testID", Testidupload);
            params.put("uniqueId", testpostuniqueid);
            params.put("schedule_id",schduleidfinal);
            params.put("test_data",finalData );
            Log.e("params :", params.toString());
           // showMessage("message",params.toString());
            String url = C.API_TEST_DETAILS_UPLOAD;

            //showMessage("",params.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params)

                    //   null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Data response", String.valueOf(response.toString()));
                   // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        //showMessage("mess",response.toString());
                        submitfeedback();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);
                 //   builder.setTitle("Seems it's taking more time then usual !");
                    builder.setMessage("Check internet connection and press Retry to upload again");
                    builder.setCancelable(true);
                    builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getjsonformat();
                            //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                    // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                   // showMessage("error",error.toString());
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
          //  VolleyAppController.getInstance().addToRequestQueue(request,tag_json_obj);
            int socketTimeout = 900000000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            Volley.newRequestQueue(this).add(request);
        }
        else
        {
            //Snackbar snackbar = Snackbar.make(view, "User Details Does Not Match ", Snackbar.LENGTH_LONG);
           // snackbar.show();
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TestListview.this);
          //  builder.setTitle("All Offline details for this schdule has been removed");
            builder.setMessage("");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                    // Testdetail(Globalclass.apikeys,Globalclass.id_login);
                   // downloadTestQuestionaryDetails();
                }
            });
            builder.show();
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
                    }
                    catch (Exception e) {
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




    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Globalclass.batchidoffline  =  savedInstanceState.getString("batchidoffline");
        Globalclass.schduleid =  savedInstanceState.getString("schduleid");
        Globalclass.id_login =   savedInstanceState.getString("id_login");
        Globalclass.userids =  savedInstanceState.getString("userids");
        Globalclass.roleval =  savedInstanceState.getString("roleval");
        Globalclass.longss =  savedInstanceState.getString("longss");
        Globalclass.latss =  savedInstanceState.getString("latss");
        Globalclass.idcandidate = savedInstanceState.getString("idcandidate");
        Globalclass.testpostuniqueid =  savedInstanceState.getString("testpostuniqueid");
        Globalclass.apikeys =  savedInstanceState.getString("apikeys");
        Globalclass.activedetails = savedInstanceState.getString("activedetails");
        Globalclass.batchidvalues = savedInstanceState.getString("batchidvalues");
        Globalclass.ddusernameselected = savedInstanceState.getString("ddusernameselected");
        Globalclass.schduleidfinal =  savedInstanceState.getString("schduleidfinal");
        Globalclass.onlinjson =  savedInstanceState.getString("onlinjson");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("batchidoffline",Globalclass.batchidoffline);
        savedInstanceState.putString("schduleid",Globalclass.schduleid);
        savedInstanceState.putString("id_login",Globalclass.id_login);
        savedInstanceState.putString("userids",Globalclass.userids);
        savedInstanceState.putString("roleval",Globalclass.roleval);
        savedInstanceState.putString("longss",Globalclass.longss);
        savedInstanceState.putString("latss",Globalclass.latss);
        savedInstanceState.putString("idcandidate",Globalclass.idcandidate);
        savedInstanceState.putString("testpostuniqueid",Globalclass.testpostuniqueid);
        savedInstanceState.putString("apikeys",Globalclass.apikeys);
        savedInstanceState.putString("activedetails",Globalclass.activedetails);
        savedInstanceState.putString("batchidvalues",Globalclass.batchidvalues);
        savedInstanceState.putString("ddusernameselected",Globalclass.ddusernameselected);
        savedInstanceState.putString("schduleidfinal",Globalclass.schduleidfinal);
        savedInstanceState.putString("onlinjson",Globalclass.onlinjson);
        super.onSaveInstanceState(savedInstanceState);
    }
}
