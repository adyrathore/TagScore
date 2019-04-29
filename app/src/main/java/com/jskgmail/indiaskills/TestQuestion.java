package com.jskgmail.indiaskills;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.jskgmail.indiaskills.camera.CameraManager;
import com.jskgmail.indiaskills.camera.CemeraService;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.ClientSSLSocketFactory;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestQuestion extends AppCompatActivity implements SurfaceHolder.Callback {

    // for testing image capturing random or not
    // original
    JSONArray Questionanswerotheranswerlan;
    TextView tvTimer, txtquestion, txt_name;
    String valuesbookmark;
    @BindView(R.id.btnSubmitTest)
    Button btnSubmitTest;
    boolean isSubmit = false;
    @BindView(R.id.btnPause)
    Button btnPause;
    @BindView(R.id.tvRec)
    TextView tvRec;
    private String VIDEO_PATH_NAME = "/storage/emulated/0/Pictures/MyCameraApp/VID_20180929_153403.mp4";
    int stopflag = 0;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private View mToggleButton;
    private boolean mInitSuccesful;
    String filename;
    Spinner dd_tagid;
    int bookmarkreviewcountvalue = 0;
    FrameLayout framelayout1;
    int lastquestiontoview = 0;
    String questiontype;
    Button next, privious, bookmark;
    ArrayList<String> optionid;
    String answergivenornot = "";
    ArrayList<String> hashmapquestion;
    String answeroptionval = "";
    String answeroptionnoval = "";
    String isbookmark = "0";
    String reviewquestion = "0";
    String currentbookmark = "";
    String currentcount = "1";
    String bookmarkcount = "0";
    String runningbookmark = "0";
    boolean isAnyQuestionBookMarked = false;
    ArrayAdapter<String> spinnerArrayAdapter;
    ImageView imgvalues;
    int total_no_of_quest, currIndex;
    LinearLayout linearLayout, linvalie;
    TextView testquestionid, Qustionnumber;
    ArrayList<String> bookmarkreview;
    RadioGroup hour_radio_group;
    ArrayList<String> Student;
    ArrayList<String> tagid;
    //  Spinner spinnerlan;
    HashMap<Integer, String> spinnerMap;
    HashMap<Integer, String> spinneriduser;
    Dialog progressDialog;
    Button btn_ReviewBookmark;
    // String rangevalues="0";
    ImageButton camera1, vedio;
    RadioGroup hour_radio_group1;
    LinearLayout rdiobuttonview;
    LinearLayout yesnoaligment;
    LinearLayout llQuestion2View;

    TestList testList;
    String activeDetails, sId;
    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    DatabaseHelper databaseHelper;
    private boolean isbookmarkReviewed = false;
    private String test_duration;
    private CountDownTimer countDownTimer;
    boolean isCheckedradio=false;
    RadioButton radioButtonO;
    EditText editTextType4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);
        ButterKnife.bind(this);
        progressDialog = Util.getProgressDialog(this, R.string.loading);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        databaseHelper = new DatabaseHelper(TestQuestion.this);
        sId = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
        Globalclass.guestioncount = Globalclass.tempQuestionNo;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        txt_name = (TextView) findViewById(R.id.txt_name);

        txt_name.setText(SharedPreference.getInstance(TestQuestion.this).getTest(C.ONGOING_TEST).getTestName());
        framelayout1 = (FrameLayout) findViewById(R.id.framelayout1);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        hour_radio_group1 = (RadioGroup) findViewById(R.id.hour_radio_group1);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mCamera.pa
            }
        });
        // for testing image random
        Student = new ArrayList<>();
        tagid = new ArrayList<>();
        bookmarkreview = new ArrayList<String>();
        // rangequestionanswer = new HashMap<Integer, String>();
        camera1 = (ImageButton) findViewById(R.id.ibnt_image);
        vedio = (ImageButton) findViewById(R.id.ibtn_vedio);
        yesnoaligment = (LinearLayout) findViewById(R.id.yesnoaligment);
        // original
        // HiddenCameraUtils.openDrawOverPermissionSetting(this);
        rdiobuttonview = (LinearLayout) findViewById(R.id.rdiobuttonview);
        llQuestion2View = (LinearLayout) findViewById(R.id.llQuestion2View);

        if (activeDetails.equalsIgnoreCase("0")) {
            CameraManager cr = new CameraManager(TestQuestion.this);
            // cr.takePhoto();
            setupAlarmManager();
            camera1.setVisibility(View.GONE);
            vedio.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.GONE);
            framelayout1.setVisibility(View.GONE);
        } else {
            framelayout1.setVisibility(View.VISIBLE);
            mSurfaceView.setVisibility(View.VISIBLE);

            // mMediaRecorder.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Do something after 100ms
                        // deleteCache(TestListview.this);
                        mMediaRecorder.start();
                        mCamera.setDisplayOrientation(90);
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, 1 * 1000);

            //  getOutputMediaFile();
            camera1.setVisibility(View.GONE);
            vedio.setVisibility(View.GONE);
            camera1.setOnClickListener(new View.OnClickListener(
            ) {
                @Override
                public void onClick(View v) {
                    savevedio();

                }
            });
            vedio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Intent intent = new Intent(TestQuestion.this,VedioRecording.class);
                    //  startActivity(intent);
                }
            });
        }

        btn_ReviewBookmark = (Button) findViewById(R.id.btn_ReviewBookmark);
        btn_ReviewBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour_radio_group1.removeAllViews();
                // getquestionarrylist();
                savebookmarkdetails();
            }
        });

        tvTimer = (TextView) findViewById(R.id.textViewtimer);
        Qustionnumber = (TextView) findViewById(R.id.Qustionnumber);
        linvalie = (LinearLayout) findViewById(R.id.linvalie);
        progressDialog.show();
        new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                // dismiss the progress dialog
                progressDialog.dismiss();
            }

        }.start();
        dd_tagid = (Spinner) findViewById(R.id.dd_tagid);
        dd_tagid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Globalclass.countlanguage.equalsIgnoreCase("0")) {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        if (Globalclass.hindipresent.equalsIgnoreCase("1")) {
                            dd_tagid.setSelection(spinnerArrayAdapter.getPosition("Hindi"));
                        } else {
                            dd_tagid.setSelection(spinnerArrayAdapter.getPosition("English"));
                        }
                        Globalclass.countlanguage = "1";
                    }
                }
                String strval = dd_tagid.getSelectedItem().toString();
                if (strval.equalsIgnoreCase("English")) {
                    next.setText(R.string.Next);
                    privious.setText(R.string.PREVIOUS);
                    btn_ReviewBookmark.setText(R.string.reviewbookmark);
                    bookmark.setText(R.string.BOOKMARK);
                    Globalclass.spinnerstringlang = "en";
                } else {
                    Globalclass.spinnerstringlang = "hn";
                    next.setText(R.string.Nexthn);
                    bookmark.setText(R.string.reviewbookmarkhn);
                    privious.setText(R.string.PREVIOUShn);
                    //Globalclass.languagecode = "hn";
                    btn_ReviewBookmark.setText(R.string.reviewbookmarkhn);
                }
                String item = dd_tagid.getSelectedItem().toString();
                boolean success = getlanguagevalues(item);
                if (success == true) {
                    //   getquestionforotherlanguage();+
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    linearLayout.removeAllViews();
                    linearLayout.removeAllViews();
                    hour_radio_group1.removeAllViews();
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    linearLayout.removeAllViews();
                    llQuestion2View.removeAllViews();
                    new LongOperationgetquestiondetails().execute();
                } else {
                    showMessage("", "Unable to get Other language");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtquestion = (TextView) findViewById(R.id.txtquestion);
        testquestionid = (TextView) findViewById(R.id.testquestionid);
        hour_radio_group = (RadioGroup) findViewById(R.id.hour_radio_group);
        optionid = new ArrayList<>();
        imgvalues = (ImageView) findViewById(R.id.imgvalues);
        //  centerLockHorizontalScrollview.setCenter(Globalclass.guestioncount,1);
        //  centerLockHorizontalScrollview = (CenterLockHorizontalScrollview) findViewById(R.id.scrollView);

        linearLayout = findViewById(R.id.optionvalues);
        next = (Button) findViewById(R.id.btn_nextquestion);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globalclass.tempQuestionNo = Globalclass.guestioncount + 1;

                next();
            }
        });
        privious = (Button) findViewById(R.id.btn_priviousquestion);
        privious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globalclass.capture.equalsIgnoreCase("1")) {
                    Globalclass.capture = "0";
                    mMediaRecorder.start();
                }
                linearLayout.removeView((View) linearLayout.getParent());
                hour_radio_group.removeAllViews();
                linearLayout.removeAllViews();
                Globalclass.tempQuestionNo = Globalclass.tempQuestionNo - 1;
                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                linearLayout.removeAllViews();
                linearLayout.removeView((View) linearLayout.getParent());
                hour_radio_group.removeAllViews();
                linearLayout.removeAllViews();
                hour_radio_group1.removeAllViews();
                llQuestion2View.removeAllViews();
                new LongOperationgetquestiondetails().execute();
            }
        });
        bookmark = (Button) findViewById(R.id.btn_bookmarkquestion);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globalclass.capture.equalsIgnoreCase("1")) {
                    Globalclass.capture = "0";
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            // deleteCache(TestListview.this);
                            mMediaRecorder.start();
                        }
                    }, 1 * 1000);
                }
                //
                // centerLockHorizontalScrollview.setBookmark(currIndex, 1);
                isAnyQuestionBookMarked = true;
                currentbookmark = "1";
                isbookmark = "1";
                reviewquestion = "1";
                if (activeDetails.equalsIgnoreCase("1")) {
                    Globalclass.bookmardepractrical = "1";
                }
                // updatePhotos();
            }
        });
        //   progressDialog.setCancelable(false);

        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
            next.setText(R.string.Nexthn);
            bookmark.setText(R.string.reviewbookmarkhn);
            privious.setText(R.string.PREVIOUShn);
            //Globalclass.languagecode = "hn";
            btn_ReviewBookmark.setText(R.string.reviewbookmarkhn);
        } else {
            next.setText(R.string.Next);
            privious.setText(R.string.PREVIOUS);
            btn_ReviewBookmark.setText(R.string.reviewbookmark);
            bookmark.setText(R.string.BOOKMARK);
        }
        // hour_radio_group1.removeAllViews();
        getquestionarrylist();
    }


    public void next() {
        try {
            if (Globalclass.capture.equalsIgnoreCase("1")) {
                Globalclass.capture = "0";
                mMediaRecorder.start();
            }
            //  getquestionarrylist();
            // hour_radio_group1.removeAllViews();
            if (testList.getId().equalsIgnoreCase("") || testList.getId().isEmpty() || activeDetails.equalsIgnoreCase("") || sId.equalsIgnoreCase("") || Globalclass.userids.equalsIgnoreCase("")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TestQuestion.this);
                builder.setMessage("Opsss Somthing went wrong please login again");
                builder.setCancelable(false);
                builder.setNeutralButton("Login Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(TestQuestion.this, ActivityMain.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        finish();
                        //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            } else {
                if (activeDetails.equals("0") && questiontype.equals("4")) {
                    answeroptionval = editTextType4.getText().toString();
                }

                new savinganswerdetails().execute();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void savevedio() {
        try {
            DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
            if (Util.ONLINE) {

                uploadBitmapvideoinbetween(VIDEO_PATH_NAME, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), filename, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "video", filename);

            } else {

                boolean success = db.insert_imagesval(Globalclass.evidencecapture, "non", filename, testList.getId(), VIDEO_PATH_NAME, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                if (success == true) {
                    Globalclass.capture = "1";
                    Intent intent = new Intent(TestQuestion.this, Picturecapture.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                } else {
                    Globalclass.capture = "1";
                    Intent intent = new Intent(TestQuestion.this, Picturecapture.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);

                    startActivity(intent);
                    Toast.makeText(TestQuestion.this, "Not able to save ", Toast.LENGTH_LONG).show();
                }
            }
            mMediaRecorder.reset();
            mCamera.release();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // for testing image random
    public boolean getlanguagevalues(String values) {
        Boolean success = false;
        try {

            DatabaseHelper mydb = new DatabaseHelper(this);

            String json = "";
            if (Util.ONLINE) {
                json = SharedPreference.getInstance(TestQuestion.this).getString(C.ONLINE_TEST_LIST);
            } else {

                Cursor resss = mydb.gettest_details_json_string(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                if (resss.getCount() == 0) {
                    // show message
                    showMessage("Error", "Nothing found");
                    success = false;
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
                // JSONObject jsonobj_2 = (JSONObject) array;
                JSONArray subjsonlanguage = jsonobj_2.getJSONArray("test_languages");
                for (int j = 0; j < subjsonlanguage.length(); j++) {
                    JSONObject jsonobj_2_language = (JSONObject) subjsonlanguage.get(j);
                    String languagenameval = jsonobj_2_language.get("language_name").toString();
                    String language_code = jsonobj_2_language.get("language_code").toString();
                    if (languagenameval.equalsIgnoreCase(values)) {
                        Globalclass.languagecode = language_code;
                        success = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    // original
    private void setupAlarmManagerstop() {
        try {

            PendingIntent pi = PendingIntent.getService(this, 101, new Intent(this, CemeraService.class),
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pi);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupAlarmManager() {
        try {
            PendingIntent pi = PendingIntent.getService(
                    this,
                    101,
                    new Intent(this, CemeraService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60000, pi);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initRecorder(Surface surface) throws Exception {
        try {
            if (mCamera == null) {
                mCamera = Camera.open();
                mCamera.unlock();
            }
            // String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String timeStamp = Util.getCurrentTimeStamp();
            VIDEO_PATH_NAME = "/storage/emulated/0/Pictures/" + sId + "/" + sId + "-" + Globalclass.idcandidate + "-Video-" + timeStamp + ".mp4";
            filename = sId + "-" + Globalclass.idcandidate + "-Video-" + timeStamp + ".mp4";
            if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setPreviewDisplay(surface);
            //mSurfaceView.setRotation(90);
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            //       mMediaRecorder.setOutputFormat(8);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
            mMediaRecorder.setVideoFrameRate(30);
            //   mMediaRecorder.setOrientationHint(180);
            mMediaRecorder.setVideoSize(640, 480);
            mMediaRecorder.setOutputFile(VIDEO_PATH_NAME);
            // getOutputMediaFile(MEDIA_TYPE_VIDEO);
            try {
                mMediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    private void shutdown() {
        try {
            if (mCamera != null) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mCamera.release();
                mMediaRecorder = null;
                mCamera = null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnSubmitTest)
    public void onViewClicked() {
        try {
            if (activeDetails.equals("0") && questiontype.equals("4")) {
                answeroptionval = editTextType4.getText().toString();
            }
            if ((answeroptionval.equalsIgnoreCase("") ||
                    answeroptionval.equalsIgnoreCase("0")) && (answeroptionnoval.equals("") || answeroptionnoval.equals(""))) {


                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                    showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                } else {
                    showMessage("", "Please Give Answer to Move Next");
                }
                return;
            }
            isSubmit = true;
            next();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    class  savinganswerdetails extends AsyncTask<Void, Void,String>{
         ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(TestQuestion.this);
                pd.setMessage("Please wait...");
                pd.setCancelable(false);
                pd.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            savesetails();
            return "";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getDateTime() {
       /* Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy_MM_dd-hh-mm-ss");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;*/
       return Util.getCurrentTimeStamp();
    }

    public void savesetails() {
        // if(activeDetails.equalsIgnoreCase("1")) {
        //   savevedioperquestion();
        // }
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
                    String str = tvTimer.getText().toString();

                    Cursor cursor = db.gettimereamining(testList.getId(), sId, Globalclass.userids, activeDetails);
                    if (cursor.getCount() > 0) {
                        boolean success = db.update_record_testtimeining(testList.getId(), Globalclass.userids, sId, str, activeDetails);
                    } else {
                        boolean success = db.insert_timeremaining(testList.getId(), Globalclass.userids, sId, str, activeDetails);
                    }
                    if (currentbookmark.equalsIgnoreCase("1")) {
                        reviewquestion = "1";
                        //  savevalues();
                        //DatabaseHelper db = new DatabaseHelper(TestQuestion.this);

                        boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                        if (b == true) {
                            savevalues();
                        } else {
                            savevalues();
                        }
                    } else {
                        if (activeDetails.equalsIgnoreCase("0")) {
                            if (answeroptionval.equalsIgnoreCase("")) {
                    /*if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                    } else {
                        showMessage("", "Please Give Answer to Move Next");
                    }*/
                                linearLayout.removeView((View) linearLayout.getParent());
                                hour_radio_group.removeAllViews();
                                linearLayout.removeAllViews();
                                llQuestion2View.removeAllViews();
                                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                linearLayout.removeAllViews();
                                linearLayout.removeView((View) linearLayout.getParent());
                                hour_radio_group.removeAllViews();
                                hour_radio_group1.removeAllViews();
                                linearLayout.removeAllViews();
                                new LongOperationgetquestiondetails().execute();
                            } else {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                if (b == true) {
                                    savevalues();
                                } else {
                                    savevalues();
                                }
                            }
                        } else if (activeDetails.equalsIgnoreCase("1")) {

                            if (answeroptionval.equalsIgnoreCase("")) {
                                if (answeroptionnoval.equalsIgnoreCase("")) {
                        /*if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                            showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                        } else {
                            showMessage("", "Please Give Answer to Move Next");
                        }*/
                                    linearLayout.removeView((View) linearLayout.getParent());
                                    hour_radio_group.removeAllViews();
                                    linearLayout.removeAllViews();
                                    llQuestion2View.removeAllViews();
                                    Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                    linearLayout.removeAllViews();
                                    linearLayout.removeView((View) linearLayout.getParent());
                                    hour_radio_group.removeAllViews();
                                    hour_radio_group1.removeAllViews();
                                    linearLayout.removeAllViews();
                                    new LongOperationgetquestiondetails().execute();
                                } else {
                                    savevalues();
                                }

                            } else {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                if (b == true) {
                                    savevalues();
                                } else {
                                    savevalues();
                                }
                            }
                        }

                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void savevalues() {
        try {
            String strid = testquestionid.getText().toString();
            String strdate = getDateTime();
            DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
            boolean alreadyhave = db.getdetailsvaluesquestion(testList.getId(), Globalclass.userids, strid);
            if (alreadyhave == true) {
                int bookmarkflag = 0;
                if (currentbookmark.equalsIgnoreCase("1")) {
                    bookmarkflag = 1;
                }
                boolean succ;
                if (activeDetails.equalsIgnoreCase("0")) {
                    succ = db.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "{100}", "0", activeDetails, answeroptionnoval);
                } else {
                    String rangemark = "";
                    String strquestion[] = answeroptionval.split(",");
                    for (int i = 0; i < strquestion.length; i++) {
                        rangemark = "100" + "," + rangemark;
                    }
                    succ = db.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "rangemark", "1", activeDetails, answeroptionnoval);

                }
                if (succ == true) {
                    //  Toast.makeText(TestQuestion.this,"Update:-" +testList.getId()+strid+ answeroptionval+ Globalclass.userids+ strdate+ "0"+ "0"+ String.valueOf(Globalclass.guestioncount) , Toast.LENGTH_LONG).show();
                    String str = next.getText().toString();
                    //    if (str.equalsIgnoreCase("submit") || str.equalsIgnoreCase("जमा करें")) {
                    if (isSubmit) {
                        if (activeDetails.equalsIgnoreCase("1")) {
                            DatabaseHelper dbvalues = new DatabaseHelper(TestQuestion.this);
                            //final boolean bookmarked = dbvalues.getbookmarkedvalues(idfinal,testList.getId(),Globalclass.userids,activeDetails);
                            if (Globalclass.bookmardepractrical.equalsIgnoreCase("1")) {
                                LayoutInflater layoutInflater = LayoutInflater.from(TestQuestion.this);
                                View mView = null;
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkhindi, null);
                                } else {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkquestion, null);
                                }
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestQuestion.this);
                                builder.setView(mView);
                                builder
                                        .setCancelable(false)
                                        .setNegativeButton("Review Question",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogBox, int id) {
                                                        dialogBox.cancel();
                                                        getbookmarkquestionvalues();
                                                    }
                                                });
                                android.app.AlertDialog alertDialogAndroid = builder.create();
                                alertDialogAndroid.show();
                            } else {
                                DatabaseHelper db1 = new DatabaseHelper(TestQuestion.this);
                                Boolean success = db1.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                if (success == true) {
                                    boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {
                                        Globalclass.tempQuestionNo = 0;
                                        Globalclass.guestioncount = Globalclass.tempQuestionNo;

                                        Globalclass.total_no_of_quest_val = 0;
                                        //  Globalclass.userids = "";
                                        currentbookmark = "";
                                        isbookmark = "";
                                        reviewquestion = "";
                                        isbookmark = "0";
                                        //         isAnyQuestionBookMarked = false;
                                        setupAlarmManagerstop();
                                        Globalclass.lastpicturecandidate = "1";
                                        // CameraManager cr = new CameraManager(TestQuestion.this);
                                        // cr.takePhoto();
                                        currentbookmark = "0";
                                        currentcount = "1";
                                        bookmarkcount = "0";
                                        openPhotoCaptureActivity();
                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                } else {
                                    showMessage("Oppps", "Somthing went wrong ...");
                                }
                            }
                        } else {
                            if (!isAnyQuestionBookMarked) {
                                if (reviewquestion.equalsIgnoreCase("1")) {
                                    showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
                                } else {
                                    // DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
                                    Boolean success = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                    if (success == true) {
                                        boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                        if (successval == true) {
                                            Globalclass.tempQuestionNo = 0;
                                            Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                            Globalclass.total_no_of_quest_val = 0;
                                            //  Globalclass.userids = "";
                                            if (activeDetails.equalsIgnoreCase("1")) {
                                                if (Util.ONLINE) {

                                                    uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);

                                                } else {

                                                    success = db.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                                    if (success == true) {
                                                    } else {
                                                        Toast.makeText(TestQuestion.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                mMediaRecorder.reset();
                                                mCamera.release();
                                            }
                                            currentbookmark = "";
                                            isbookmark = "";
                                            reviewquestion = "";
                                            isbookmark = "0";
                                            //          isAnyQuestionBookMarked = false;
                                            setupAlarmManagerstop();
                                            Globalclass.lastpicturecandidate = "1";
                                            // CameraManager cr = new CameraManager(TestQuestion.this);
                                            // cr.takePhoto();
                                            currentbookmark = "0";
                                            bookmarkcount = "0";
                                            currentcount = "1";
                                            openPhotoCaptureActivity();


                                        } else {
                                            showMessage("", "Unable to submit test");
                                        }
                                    } else {
                                        showMessage("Oppps", "Somthing went wrong ...");
                                    }
                                }
                            } else {
                                LayoutInflater layoutInflater = LayoutInflater.from(TestQuestion.this);
                                View mView = null;
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkhindi, null);
                                } else {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkquestion, null);
                                }
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestQuestion.this);
                                builder.setView(mView);
                                builder
                                        .setCancelable(false)
//                                    .setPositiveButton("Complete Test", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialogBox, int id) {
//                                            // ToDo get user input here
//                                            if (reviewquestion.equalsIgnoreCase("1")) {
//                                                showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
//                                            } else {
//                                                DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
//                                                Boolean success = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
//                                                if (success == true) {
//                                                    boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
//                                                    if (successval == true) {
//                                                        Globalclass.tempQuestionNo = 0;
//                                                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
//                                                        Globalclass.total_no_of_quest_val = 0;
//                                                        //  Globalclass.userids = "";
//                                                        currentbookmark = "";
//                                                        isbookmark = "";
//                                    //                    isAnyQuestionBookMarked = false;
//                                                        reviewquestion = "";
//                                                        isbookmark = "0";
//                                                        setupAlarmManagerstop();
//                                                        Globalclass.lastpicturecandidate = "1";
////                                                        CameraManager cr = new CameraManager(TestQuestion.this);
////                                                        cr.takePhoto();
//                                                        currentbookmark = "0";
//                                                        currentcount = "1";
//                                                        bookmarkcount = "0";
//                                                        openPhotoCaptureActivity();
//
//                                                    } else {
//                                                        showMessage("", "Unable to submit test");
//                                                    }
//                                                } else {
//                                                    showMessage("Oppps", "Somthing went wrong ...");
//                                                }
//                                            }
//                                        }
//                                    })

                                        .setNegativeButton("Review Question",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogBox, int id) {
                                                        dialogBox.cancel();
                                                        getbookmarkquestionvalues();
                                                    }
                                                });
                                android.app.AlertDialog alertDialogAndroid = builder.create();
                                alertDialogAndroid.show();
                            }
                        }
                    } else {
                        //   Globalclass.guestioncount = Globalclass.guestioncount + 1;
                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
                        progressDialog.show();
                        linearLayout.removeView((View) linearLayout.getParent());
                        hour_radio_group.removeAllViews();
                        linearLayout.removeAllViews();
                        hour_radio_group1.removeAllViews();
                        llQuestion2View.removeAllViews();
                        // isbookmark= "0";
                        currentbookmark = "0";
                        currentcount = "1";
                        bookmarkcount = "0";
                        answeroptionval = "";
                        new Thread() {
                            public void run() {
                                try {
                                    sleep(1000);
                                } catch (Exception e) {
                                    Log.e("tag", e.getMessage());
                                }
                                // dismiss the progress dialog
                                progressDialog.dismiss();
                            }
                        }.start();
                        if (activeDetails.equalsIgnoreCase("0")) {
                            new LongOperationgetquestiondetails().execute();
                        } else if (activeDetails.equalsIgnoreCase("1")) {
                            if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                                if (activeDetails.equalsIgnoreCase("1")) {
                                    Globalclass.countlanguage = "0";
                                    Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                    intent.putExtra(C.TEST, testList);
                                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    Globalclass.countlanguage = "0";
                                    new LongOperationgetquestiondetails().execute();
                                }
                            } else {
                                if (Util.ONLINE) {
                                    if (activeDetails.equalsIgnoreCase("1")) {
                                        uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                                    }
                                } else {
                                    if (activeDetails.equalsIgnoreCase("1")) {
                                        boolean success = db.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                        if (success == true) {

                                        } else {
                                            Toast.makeText(TestQuestion.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                        }
                                        mMediaRecorder.reset();
                                        mCamera.release();
                                    }
                                }
                            }
                            if (activeDetails.equalsIgnoreCase("1")) {
                                if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                                    Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                    intent.putExtra(C.TEST, testList);
                                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Globalclass.countlanguage = "0";
                                    Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                    intent.putExtra(C.TEST, testList);
                                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            } else {

                String str = next.getText().toString();
                int bookmarkflag = 0;

                if (currentbookmark.equalsIgnoreCase("1")) {
                    bookmarkflag = 1;
                }
                boolean success;
                if (activeDetails.equalsIgnoreCase("0")) {
                    success = db.insert_useranswer(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "{100}", "0", activeDetails, answeroptionnoval);
                } else {
                    String rangemark = "";
                    String strquestion[] = answeroptionval.split(",");
                    for (int i = 0; i < strquestion.length; i++) {
                        rangemark = "100" + "," + rangemark;
                    }
                    success = db.insert_useranswer(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, rangemark, "1", activeDetails, answeroptionnoval);
                }
                if (success == true) {
                    //  Toast.makeText(TestQuestion.this,testList.getId()+strid+ answeroptionval+ Globalclass.userids+ strdate+ "0"+ "0"+ String.valueOf(Globalclass.guestioncount) , Toast.LENGTH_LONG).show();
                    // if (str.equalsIgnoreCase("submit") || str.equalsIgnoreCase("जमा करें")) {
                    if (isSubmit) {
                        if (activeDetails.equalsIgnoreCase("1")) {
                            DatabaseHelper dbvalues = new DatabaseHelper(TestQuestion.this);
                            // final boolean bookmarked = dbvalues.getbookmarkedvalues(idfinal,testList.getId(),Globalclass.userids,activeDetails);
                            if (Globalclass.bookmardepractrical.equalsIgnoreCase("1")) {
                                LayoutInflater layoutInflater = LayoutInflater.from(TestQuestion.this);
                                View mView = null;
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkhindi, null);
                                } else {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkquestion, null);
                                }
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestQuestion.this);
                                builder.setView(mView);
                                builder
                                        .setCancelable(false)
//                                    .setPositiveButton("Complete Test", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialogBox, int id) {
//                                            // ToDo get user input here
//                                            if (Globalclass.bookmardepractrical.equalsIgnoreCase("1")) {
//                                                showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
//                                            } else {
//                                                DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
//                                                Boolean success = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
//                                                if (success == true) {
//                                                    boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
//                                                    if (successval == true) {
//                                                        Globalclass.tempQuestionNo = 0;
//                                                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
//                                                        Globalclass.total_no_of_quest_val = 0;
//                                                        //  Globalclass.userids = "";
//                                                        currentbookmark = "";
//                                                        isbookmark = "";
//                                                        reviewquestion = "";
//                                                        isbookmark = "0";
//                                  //                      isAnyQuestionBookMarked = false;
//                                                        setupAlarmManagerstop();
//                                                        Globalclass.lastpicturecandidate = "1";
////                                                        CameraManager cr = new CameraManager(TestQuestion.this);
////                                                        cr.takePhoto();
//                                                        currentbookmark = "0";
//                                                        currentcount = "1";
//                                                        bookmarkcount = "0";
//                                                        openPhotoCaptureActivity();
//
//                                                    } else {
//                                                        showMessage("", "Unable to submit test");
//                                                    }
//                                                } else {
//                                                    showMessage("Oppps", "Somthing went wrong ...");
//                                                }
//                                            }
//                                        }
//                                    })

                                        .setNegativeButton("Review Question",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogBox, int id) {
                                                        dialogBox.cancel();
                                                        getbookmarkquestionvalues();
                                                    }
                                                });
                                android.app.AlertDialog alertDialogAndroid = builder.create();
                                alertDialogAndroid.show();
                            } else {
                                DatabaseHelper db1 = new DatabaseHelper(TestQuestion.this);
                                Boolean success1 = db1.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                if (success1 == true) {
                                    boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {
                                        Globalclass.tempQuestionNo = 0;
                                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                        Globalclass.total_no_of_quest_val = 0;
                                        //  Globalclass.userids = "";
                                        currentbookmark = "";
                                        isbookmark = "";
                                        reviewquestion = "";
                                        isbookmark = "0";
                                        //      isAnyQuestionBookMarked = false;
                                        setupAlarmManagerstop();
                                        Globalclass.lastpicturecandidate = "1";
//                                    CameraManager cr = new CameraManager(TestQuestion.this);
//                                    cr.takePhoto();
                                        currentbookmark = "0";
                                        currentcount = "1";
                                        bookmarkcount = "0";
                                        openPhotoCaptureActivity();

                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                } else {
                                    showMessage("Oppps", "Somthing went wrong ...");
                                }
                            }
                        } else {


                            if (!isAnyQuestionBookMarked) {
                                if (reviewquestion.equalsIgnoreCase("1")) {
                                    showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
                                } else {
                                    // DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
                                    setupAlarmManagerstop();
                                    Globalclass.lastpicturecandidate = "1";
//                                CameraManager cr1 = new CameraManager(TestQuestion.this);
//                                cr1.takePhoto();

                                    boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {
                                        Globalclass.tempQuestionNo = 0;
                                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                        Globalclass.total_no_of_quest_val = 0;
                                        // Globalclass.userids = "";
                                        currentbookmark = "";
                                        if (Util.ONLINE) {
                                            if (activeDetails.equalsIgnoreCase("1")) {
                                                uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                                            }
                                        } else {
                                            if (activeDetails.equalsIgnoreCase("1")) {
                                                success = db.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                                if (success == true) {

                                                } else {
                                                    Toast.makeText(TestQuestion.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                                }
                                                mMediaRecorder.reset();
                                                mCamera.release();
                                            }

                                        }

                                        currentbookmark = "";
                                        isbookmark = "";
                                        reviewquestion = "";
                                        isbookmark = "0";
                                        //     isAnyQuestionBookMarked = false;
                                        setupAlarmManagerstop();
                                        Globalclass.lastpicturecandidate = "1";
//                                    CameraManager cr = new CameraManager(TestQuestion.this);
//                                    cr.takePhoto();
                                        currentbookmark = "0";
                                        currentcount = "1";
                                        bookmarkcount = "0";
                                        openPhotoCaptureActivity();

                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                }

                            } else {
                                LayoutInflater layoutInflater = LayoutInflater.from(TestQuestion.this);
                                View mView = null;
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkhindi, null);
                                } else {
                                    mView = layoutInflater.inflate(R.layout.validatebookmarkquestion, null);
                                }
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestQuestion.this);
                                builder.setView(mView);
                                builder
                                        .setCancelable(false)
//                                    .setPositiveButton("Complete Test", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialogBox, int id) {
//                                            // ToDo get user input here
//                                            if (reviewquestion.equalsIgnoreCase("1")) {
//                                                showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
//                                            } else {
//                                                DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
//                                                setupAlarmManagerstop();
//                                                Globalclass.lastpicturecandidate = "1";
////                                                CameraManager cr = new CameraManager(TestQuestion.this);
////                                                cr.takePhoto();
//                                                boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
//                                                if (successval == true) {
//                                                    Globalclass.tempQuestionNo = 0;
//                                                    Globalclass.guestioncount = Globalclass.tempQuestionNo;
//                                                    Globalclass.total_no_of_quest_val = 0;
//                                                    // Globalclass.userids = "";
//                                                    currentbookmark = "";
//                                                    isbookmark = "";
//                                                    reviewquestion = "";
//                                                    isbookmark = "0";
//                               //                     isAnyQuestionBookMarked = false;
//                                                    currentbookmark = "0";
//                                                    currentcount = "1";
//                                                    bookmarkcount = "0";
//                                                    openPhotoCaptureActivity();
//
//                                                } else {
//                                                    showMessage("", "Unable to submit test");
//                                                }
//                                            }
//                                        }
//                                    })
                                        .setNegativeButton("Review Question",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogBox, int id) {
                                                        dialogBox.cancel();
                                                        getbookmarkquestionvalues();
                                                    }
                                                });
                                android.app.AlertDialog alertDialogAndroid = builder.create();
                                alertDialogAndroid.show();
                            }
                        }
                    } else {
                        //    Globalclass.guestioncount = Globalclass.guestioncount + 1;
                        Globalclass.guestioncount = Globalclass.tempQuestionNo;


//                        progressDialog.show();

                        linearLayout.removeView((View) linearLayout.getParent());
                        // Toast.makeText(TestQuestion.this, testquestionid.getText().toString(), Toast.LENGTH_SHORT).show();
                        hour_radio_group.removeAllViews();
                        hour_radio_group1.removeAllViews();
                        llQuestion2View.removeAllViews();
                        linearLayout.removeAllViews();
                        isbookmark = "0";
                        //  isAnyQuestionBookMarked = false;
                        currentbookmark = "0";
                        currentcount = "1";
                        answeroptionval = "";
                        new Thread() {
                            public void run() {

                                try {
                                    sleep(1000);
                                } catch (Exception e) {
                                    Log.e("tag", e.getMessage());
                                }
                                // dismiss the progress dialog
                                progressDialog.dismiss();
                            }
                        }.start();
                        if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                            if (activeDetails.equalsIgnoreCase("1")) {
                                Globalclass.countlanguage = "0";
                                Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                intent.putExtra(C.TEST, testList);
                                intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                                startActivity(intent);
                                finish();
                            } else {
                                Globalclass.countlanguage = "0";
                                new LongOperationgetquestiondetails().execute();
                            }
                        } else {
                            if (activeDetails.equalsIgnoreCase("0")) {
                                new LongOperationgetquestiondetails().execute();
                            } else if (activeDetails.equalsIgnoreCase("1")) {
                                if (Util.ONLINE) {
                                    if (activeDetails.equalsIgnoreCase("1")) {
                                        uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                                    }
                                } else {
                                    if (activeDetails.equalsIgnoreCase("1")) {
                                        boolean success12 = db.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                        if (success12 == true) {

                                        } else {
                                            Toast.makeText(TestQuestion.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                        }
                                        mMediaRecorder.reset();
                                        mCamera.release();
                                    }

                                }
                            }
                            if (activeDetails.equalsIgnoreCase("1")) {
                                if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                                    Globalclass.countlanguage = "0";
                                    Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                    intent.putExtra(C.TEST, testList);
                                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Globalclass.countlanguage = "0";
                                    Intent intent = new Intent(TestQuestion.this, TestQuestion.class);
                                    intent.putExtra(C.TEST, testList);
                                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openPhotoCaptureActivity() {

        try {
            long time = getTotalTime(test_duration + ":00", tvTimer.getText().toString());

            boolean isSuccess = databaseHelper.updateTotalTimeTaken(testList.getId(), Globalclass.userids, sId, activeDetails, time + "");

            shutdown();
            Intent intent = new Intent(TestQuestion.this, ActivityLastImageCapture.class);
            intent.putExtra(C.TEST, testList);
            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
            startActivity(intent);
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    long getTotalTime(String startTime, String endTime) {

        long difference = 0;
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        try {
            Date date1 = format.parse(startTime);
            Date date2 = format.parse(endTime);
            difference = (date1.getTime() - date2.getTime()) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return difference;
    }

    String getRecTime(String startTime, String endTime) {

        long difference = 0;
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        try {
            Date date1 = format.parse(startTime);
            Date date2 = format.parse(endTime);
            difference = date1.getTime() - date2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date(difference);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

    public void getbookmarkquestionvalues() {
        try {
            // linearLayout.removeAllViews();
            //  isbookmark= "0";
            isbookmarkReviewed = true;
            currentbookmark = "1";
            bookmarkcount = "0";
            currentcount = "2";
            hour_radio_group1.removeAllViews();
            llQuestion2View.removeAllViews();
            answeroptionval = "";
            runningbookmark = "1";
            DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
            next.setVisibility(View.GONE);
            bookmark.setVisibility(View.GONE);
            privious.setVisibility(View.GONE);
            btn_ReviewBookmark.setVisibility(View.VISIBLE);
            Cursor cursor = db.getbookmarkquestionvalues(Globalclass.userids, sId, "1", activeDetails);
            lastquestiontoview = cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                isSubmit = false;
                btnSubmitTest.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                cursor.moveToNext();
                bookmarkreview.add(i, cursor.getString(8));
            }
            if (lastquestiontoview > 0) {
                linearLayout.removeView((View) linearLayout.getParent());
                hour_radio_group.removeAllViews();
                llQuestion2View.removeAllViews();
                linearLayout.removeAllViews();
                int id = Integer.parseInt(bookmarkreview.get(0));
                Globalclass.tempQuestionNo = id;
                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                linearLayout.removeAllViews();
                linearLayout.removeView((View) linearLayout.getParent());
                hour_radio_group.removeAllViews();
                hour_radio_group1.removeAllViews();
                linearLayout.removeAllViews();
                if (bookmarkreviewcountvalue == lastquestiontoview - 1) {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        btn_ReviewBookmark.setText(R.string.SUBMIThn);
                    } else {
                        btn_ReviewBookmark.setText("Submit");
                    }
                }
                new LongOperationgetquestiondetails().execute();
            } else {
                openPhotoCaptureActivity();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    class LongOperationgetquestiondetails extends  AsyncTask<Void, Void, String>{

        ProgressDialog pro;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pro = new ProgressDialog(TestQuestion.this);
                pro.setMessage("Please wait...");
                pro.setCancelable(false);
                pro.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {
            getquestiondetails();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (pro != null && pro.isShowing()) {
                    pro.dismiss();
                }
                if (Globalclass.guestioncount == 0) {
                    if (runningbookmark.equalsIgnoreCase("1")) {
                    } else {
                        privious.setEnabled(false);
                        privious.setVisibility(View.GONE);
                        // privious.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (runningbookmark.equalsIgnoreCase("1")) {
                    } else {
                        privious.setEnabled(true);
                        privious.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void getquestiondetails() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                    radioButtonO = null;
                    String given = "";
                    answeroptionval = "";
                    LinearLayout ll = null;
                    ViewGroup hourButtonLayout = (ViewGroup) findViewById(R.id.hour_radio_group1);  // This is the id of the RadioGroup we defined
                    for (int i = 0; i < Globalclass.total_no_of_quest_val; i++) {

                        if (i % 2 == 0) {
                            ll = new LinearLayout(TestQuestion.this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ll.setWeightSum(2f);
                            ll.setLayoutParams(layoutParams);
                            hourButtonLayout.addView(ll);
                        }

                        final RadioButton button = new RadioButton(TestQuestion.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, hourButtonLayout.getWidth() / 4, 1f);
                        params.setMargins(15, 15, 15, 15);
                        button.setLayoutParams(params);
                        button.setId(i);
                        button.setPadding(8, 8, 8, 8);
                        button.setText(Integer.toString(i + 1));
                        button.setGravity(Gravity.CENTER);

                        // button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        button.setButtonDrawable(R.drawable.null_selector);
                        button.setChecked(i == Globalclass.guestioncount);// Only select button with same index as currently selected number of hours
                        DatabaseHelper dbs = new DatabaseHelper(TestQuestion.this);
                        int vlues = dbs.getdetailsofvaluesbookmarkedornot(sId, testList.getId(), String.valueOf(i), Globalclass.userids, activeDetails);
                        if (vlues == 0) {
                            button.setBackgroundResource(R.drawable.rectangle);
                        } else if (vlues == 1) {
                            button.setBackgroundResource(R.drawable.rectangle_green);
                        } else if (vlues == 2) {
                            button.setBackgroundResource(R.drawable.rectanglebookmark);
                        }
                        if (i == Globalclass.guestioncount) {
                            button.setBackgroundResource(R.drawable.rectanglecurrent);
                            // hour_scroll_view.scrollTo(,0);
                        }
                        //else if( Globalclass.guestioncount < i){
                        // }
                        // else {

                        // This is a custom button drawable, defined in XML
                        // }

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                 /*   DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
                    int idjson = button.getId();
                    boolean success = db.getquestiongivenornot(sId, testList.getId(), String.valueOf(idjson), Globalclass.userids, activeDetails);
//                    if (success == true) {
                    ((RadioGroup) view.getParent().getParent()).check(view.getId());
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    linearLayout.removeAllViews();
                    int id = view.getId();
                    Globalclass.guestioncount = id;
                    linearLayout.removeAllViews();
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    hour_radio_group1.removeAllViews();
                    linearLayout.removeAllViews();
                    new LongOperationgetquestiondetails().execute();*/
                                Globalclass.tempQuestionNo = view.getId();
                                next();
//                    } else {
//                        showMessage("", "You can not move to Un-Answered Question");
//                    }
                            }

                        });
                        if (ll != null)
                            ll.addView(button);
                        if (i == Globalclass.total_no_of_quest_val - 1 && Globalclass.total_no_of_quest_val % 2 == 1) {
                            RadioButton button1 = new RadioButton(TestQuestion.this);
                            button1.setLayoutParams(params);
                            button1.setEnabled(false);
                            button1.setVisibility(View.INVISIBLE);
                            ll.addView(button1);

                        }
//            hourButtonLayout.addView(button);
                    }
                    int questionid = Integer.parseInt(String.valueOf(Globalclass.guestioncount));
                    int questiontoset = questionid + 1;
                    // centerLockHorizontalScrollview.setCenter(Globalclass.guestioncount,0);
                    Qustionnumber.setText(String.valueOf("Question:- " + questiontoset));
                    if (runningbookmark.equalsIgnoreCase("1")) {
                        privious.setVisibility(View.GONE);
                    } else {
                        privious.setVisibility(View.VISIBLE);
                    }
                    if (Globalclass.guestioncount == total_no_of_quest - 1) {

                        next.setVisibility(View.GONE);
                    } else {
                        if (!isbookmarkReviewed) {
                            next.setVisibility(View.VISIBLE);
                        }
                    }


                    String json = "";
                    DatabaseHelper mydb = new DatabaseHelper(TestQuestion.this);
                    if (Util.ONLINE) {
                        json = SharedPreference.getInstance(TestQuestion.this).getString(C.ONLINE_TEST_LIST);
                    } else {

                        Cursor resss = mydb.gettest_details_json_string(testList.getId(), sId);
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
                        int mval = 0;
                        JSONObject array = (new JSONObject(res)).getJSONObject("data");
                        JSONObject jsonobj_2 = (JSONObject) array;
                        JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
                        JSONArray subObjDetailsquestion = null;
                        JSONArray otherlanguagejson = null;
                        if (activeDetails.equalsIgnoreCase("0")) {
                            if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                                subObjDetailsquestion = subObjDetails.getJSONArray("theoryQuestions");
                            } else {
                                otherlanguagejson = subObjDetails.getJSONArray("theoryQuestions");
                                JSONObject jsonobjvalues = (JSONObject) otherlanguagejson.get(Globalclass.guestioncount);
                                subObjDetailsquestion = jsonobjvalues.getJSONArray("otherLanguages");
                                for (int m = 0; m < subObjDetailsquestion.length(); m++) {
                                    JSONObject jsonobjvaluesjsonother = (JSONObject) subObjDetailsquestion.get(m);
                                    String questionvaldsd = jsonobjvaluesjsonother.getString("language_code").toString();
                                    if (Globalclass.languagecode.equalsIgnoreCase(questionvaldsd)) {
                                        mval = m;
                                    }
                                }
                            }
                        } else {
                            if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                                subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                            } else {
                                otherlanguagejson = subObjDetails.getJSONArray("practical_questions");
                                JSONObject jsonobjvalues = (JSONObject) otherlanguagejson.get(Globalclass.guestioncount);
                                subObjDetailsquestion = jsonobjvalues.getJSONArray("otherLanguages");
                                for (int m = 0; m < subObjDetailsquestion.length(); m++) {
                                    JSONObject jsonobjvaluesjsonother = (JSONObject) subObjDetailsquestion.get(m);
                                    String questionvaldsd = jsonobjvaluesjsonother.getString("language_code").toString();
                                    if (Globalclass.languagecode.equalsIgnoreCase(questionvaldsd)) {
                                        mval = m;
                                    }
                                }
                            }
                        }
                        JSONObject jsonobj_2_otytherlanguageimage = null;
                        JSONObject jsonobj_2_answer = null;
                        if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                            jsonobj_2_answer = (JSONObject) subObjDetailsquestion.get(Globalclass.guestioncount);
                        } else {
                            jsonobj_2_answer = (JSONObject) subObjDetailsquestion.get(mval);
                        }
                        String questionval = jsonobj_2_answer.getString("question").toString();
                        txtquestion.setText(questionval);
                        if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                            testquestionid.setText(jsonobj_2_answer.getString("id").toString());
                        } else {
                            testquestionid.setText(jsonobj_2_answer.getString("parent_id").toString());
                        }
                        testquestionid.setVisibility(View.INVISIBLE);
                        String strimage = null;

                        hashmapquestion = new ArrayList<String>();
                        hashmapquestion.clear();
                        //  Toast.makeText(TestQuestion.this,testquestionid.getText().toString(),Toast.LENGTH_SHORT).show();
                        if (Globalclass.languagecode.equalsIgnoreCase("en")) {

                            strimage = jsonobj_2_answer.getString("ImageHash").toString();
                            hashmapquestion.add(jsonobj_2_answer.get("ImageHash").toString());
                        } else {
                            jsonobj_2_otytherlanguageimage = (JSONObject) otherlanguagejson.get(Globalclass.guestioncount);
                            strimage = jsonobj_2_otytherlanguageimage.get("ImageHash").toString();
                            hashmapquestion.add(jsonobj_2_otytherlanguageimage.get("ImageHash").toString());
                        }
                        // hashmapquestion.add(jsonobj_2_answer.get("ImageHash").toString());
                        if (strimage.equalsIgnoreCase("")) {
                            imgvalues.setVisibility(View.GONE);
                        } else {
                            imgvalues.setVisibility(View.VISIBLE);
//                Glide.tearDown();
//                Glide.with(getApplicationContext()).load(hashmapquestion.get(0)).into(imgvalues);
                            Util.loadImage(hashmapquestion.get(0), imgvalues);
                            // imgvalues.setImageURI(Uri.parse(hashmapquestion.get(0)));
                            // imgvalues.setImageURI();
                        }
                        questiontype = jsonobj_2_answer.getString("question_type").toString();
                        Cursor questiongiven = mydb.getAllquestiondetilsval(testList.getId(), Globalclass.userids, testquestionid.getText().toString());
                        if (questiongiven.getCount() > 0) {
                            StringBuffer buffer = new StringBuffer();
                            questiongiven.moveToNext();
                            answeroptionval = questiongiven.getString(2);
                            isbookmark = questiongiven.getString(5);
                            currentcount = questiongiven.getString(6);
                            bookmarkcount = questiongiven.getString(5);
                            if (activeDetails.equalsIgnoreCase("1")) {
                                answeroptionnoval = questiongiven.getString(15);
                            }
                            if (isbookmark.equalsIgnoreCase("")) {
                                isbookmark = "0";
                            }
                            if (currentcount.equalsIgnoreCase("")) {
                                currentcount = "1";
                            }
                            if (bookmarkcount.equalsIgnoreCase("")) {
                                bookmarkcount = "0";
                            }
                            int countvalvalues = Integer.valueOf(currentcount) + 1;
                            currentcount = String.valueOf(countvalvalues);
                            int bookmarkcountval = Integer.valueOf(bookmarkcount) + 1;
                            bookmarkcount = String.valueOf(bookmarkcountval);
                            //  Toast.makeText(TestQuestion.this,questiongiven.toString() , Toast.LENGTH_LONG).show();
                        }

                        JSONArray Questionanswer = jsonobj_2_answer.getJSONArray("answers");
                        String ids;

                        if(activeDetails.equalsIgnoreCase("0") && questiontype.equals("4")){
                            editTextType4 = new EditText(TestQuestion.this);
                         //   editText.setId(Integer.parseInt(ids));
                            editTextType4.setText(answeroptionval);
                            editTextType4.setHint("Please enter");
                            editTextType4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            if (linearLayout != null) {
                                linearLayout.addView(editTextType4);
                            }
                        }
                        for (int jj = 0; jj < Questionanswer.length(); jj++) {
                            JSONObject jsonobj_2_answerval = (JSONObject) Questionanswer.get(jj);
                            String answer = jsonobj_2_answerval.getString("answer").toString();
                            if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                                ids = jsonobj_2_answerval.getString("id").toString();
                            } else {
                                ids = jsonobj_2_answerval.getString("parent_id").toString();
                                if (ids.equalsIgnoreCase("") || ids.equalsIgnoreCase("null")) {
                                    ids = jsonobj_2_answerval.getString("id").toString();
                                }
                            }
                            final String hash = jsonobj_2_answerval.getString("hash");
                            optionid.add(jj, ids);
                            if (activeDetails.equalsIgnoreCase("0")) {
                                if (questiontype.equalsIgnoreCase("1") || questiontype.equalsIgnoreCase("")
                                        || questiontype.equalsIgnoreCase("0")) {
                                    CheckBox checkBox = new CheckBox(TestQuestion.this);
                                    checkBox.setId(Integer.parseInt(ids));
                                    checkBox.setText(answer);
                                    String strquestion[] = answeroptionval.split(",");
                                    for (int i = 0; i < strquestion.length; i++) {
                                        if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                        {
                                            checkBox.setChecked(true);
                                            given = "1";
                                            answergivenornot = "1";
                                            //  Toast.makeText(TestQuestion.this,strquestion[i] , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked == true) {
                                                // int id = buttonView.getId();
                                                String idval = String.valueOf(buttonView.getId());
                                                answeroptionval = idval + "," + answeroptionval;
                                                //   Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                            }
                                            if (isChecked == false) {
                                                String id = String.valueOf(buttonView.getId());
                                                String str[] = answeroptionval.split(id + ",");
                                                if (str.length == 0) {
                                                    answeroptionval = "";
                                                } else {
                                                    for (int i = 0; i <= str.length - 1; i++) {
                                                        answeroptionval = str[i];
                                                    }
                                                }
                                                //  Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    ImageView imageView = new ImageView(TestQuestion.this);
                                    imageView.setId(Integer.parseInt(ids));
                                    Util.loadImage(hash, imageView);

                                    if (linearLayout != null) {
                                        linearLayout.addView(checkBox);
                                        linearLayout.addView(imageView);
                                    }
                                } else if (questiontype.equalsIgnoreCase("2")) {
                                    final RadioButton radioButton = new RadioButton(TestQuestion.this);
                                    radioButton.setId(Integer.parseInt(ids));
                                    radioButton.setText(answer);
                                    //android:drawableRight="@drawable/map"
                                    String strquestion[] = answeroptionval.split(",");
                                    for (int i = 0; i < strquestion.length; i++) {
                                        if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                        {
                                            radioButton.setChecked(true);
                                            given = "1";
                                            answergivenornot = "1";
                                            radioButtonO = radioButton;
                                            //  Toast.makeText(TestQuestion.this,strquestion[i] , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked == true) {
                                                if (radioButtonO != null) {
                                                    radioButtonO.setChecked(false);
                                                    radioButtonO = radioButton;
                                                } else {
                                                    radioButtonO = radioButton;

                                                }


                                                // int id = buttonView.getId();
                                                String idval = String.valueOf(buttonView.getId());
                                                answeroptionval = idval + ",";
                                                //  Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    // radioGroupoption.addView(radioButton, pss);
                                    ImageView imageView = new ImageView(TestQuestion.this);
                                    imageView.setId(Integer.parseInt(ids));

//                        Glide.with(getApplicationContext()).load(hash).into(imageView);
                                    Util.loadImage(hash, imageView);
                                    if (llQuestion2View != null) {
                                        llQuestion2View.addView(radioButton);
                                        llQuestion2View.addView(imageView);
                                    }
                                } else if (questiontype.equalsIgnoreCase("3")) {

                                    RadioButton radioButton = new RadioButton(TestQuestion.this);
                                    radioButton.setId(Integer.parseInt(ids));
                                    radioButton.setText(answer);
                                    String strquestion[] = answeroptionval.split(",");
                                    for (int i = 0; i < strquestion.length; i++) {
                                        if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                        {
                                            radioButton.setChecked(true);
                                            given = "1";
                                            answergivenornot = "1";
                                            //  Toast.makeText(TestQuestion.this,strquestion[i] , Toast.LENGTH_LONG).show();

                                        }
                                    }
                                    radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked == true) {
                                                // int id = buttonView.getId();
                                                String idval = String.valueOf(buttonView.getId());
                                                answeroptionval = idval + ",";
                                                //  Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    ImageView imageView = new ImageView(TestQuestion.this);
                                    imageView.setId(Integer.parseInt(ids));
//                        Glide.with(getApplicationContext()).load(hash).into(imageView);
                                    Util.loadImage(hash, imageView);
                                    if (llQuestion2View != null) {
                                        llQuestion2View.addView(radioButton);
                                        // hour_radio_group.addView(radioButton);
                                        //hour_radio_group.addView(imageView);
                                    }
                                } else if (questiontype.equalsIgnoreCase("4")) {
                                    EditText editText = new EditText(TestQuestion.this);
                                    editText.setId(Integer.parseInt(ids));
                                    editText.setText(answer);
                                    editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    if (linearLayout != null) {
                                        linearLayout.addView(editText);
                                    }
                                }

                            } else {
                                // String rangeval = jsonobj_2_answerval.getString("range_marking").toString();

                                final TextView textView = new TextView(TestQuestion.this);
                                textView.setId(Integer.parseInt(ids));
                                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                textView.setText(answer);
                                final TextView textView1 = new TextView(TestQuestion.this);
                                textView1.setText(ids);
                                textView1.setVisibility(View.GONE);
                                final RadioButton radioButton = new RadioButton(TestQuestion.this);
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    radioButton.setText(R.string.yeshnval);
                                } else {
                                    radioButton.setText("Yes");
                                }
                                radioButton.setId(Integer.parseInt("1"));

                                final RadioButton radioButton1 = new RadioButton(TestQuestion.this);
                                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                    radioButton1.setText(R.string.nohnval);
                                } else {
                                    radioButton1.setText("no");
                                }
                                radioButton1.setId(Integer.parseInt("2"));
                                // CheckBox checkBox = new CheckBox(this);
                                // checkBox.setId(Integer.parseInt(ids));
                                // checkBox.setText(answer);
                                String strquestion[] = answeroptionval.split(",");
                                for (int i = 0; i < strquestion.length; i++) {
                                    if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                    {
                                        radioButton.setChecked(true);
                                        radioButton1.setChecked(false);
                                        given = "1";
                                        answergivenornot = "1";
                                        //  Toast.makeText(TestQuestion.this,strquestion[i] , Toast.LENGTH_LONG).show();
                                    }
                                }

                                String strnooption[] = answeroptionnoval.split(",");
                                for (int j = 0; j < strnooption.length; j++) {
                                    if (ids.equalsIgnoreCase(strnooption[j]))  //answeroptionval = str[i];
                                    {
                                        radioButton1.setChecked(true);
                                        radioButton.setChecked(false);
                                        given = "1";
                                        answergivenornot = "1";
                                        //  Toast.makeText(TestQuestion.this,strquestion[i] , Toast.LENGTH_LONG).show();
                                    }
                                }
                                radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked == true) {
                                            String id = String.valueOf(textView1.getText().toString());
                                            answeroptionnoval = id + "," + answeroptionnoval;
                                            radioButton.setChecked(false);
                                            //   String idval = String.valueOf(buttonView.getId());
                                            //   answeroptionval = idval + "," + answeroptionval;

                                            //   Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                        }
                                        if (isChecked == false) {
                                            String id = String.valueOf(textView1.getText().toString());
                                            String str[] = answeroptionnoval.split(id + ",");
                                            if (str.length == 0) {
                                                answeroptionnoval = "";
                                            } else {
                                                for (int i = 0; i <= str.length - 1; i++) {
                                                    answeroptionnoval = str[i];
                                                }
                                            }
                                        }
                                    }
                                });
                                radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked == true) {
                                            radioButton1.setChecked(false);
                                            // int id = buttonView.getId();
                                            String idval = String.valueOf(textView1.getText().toString());
                                            answeroptionval = idval + "," + answeroptionval;
                                            //   Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                        }
                                        if (isChecked == false) {
                                            String id = String.valueOf(textView1.getText().toString());
                                            String str[] = answeroptionval.split(id + ",");
                                            if (str.length == 0) {
                                                answeroptionval = "";
                                            } else {
                                                for (int i = 0; i <= str.length - 1; i++) {
                                                    answeroptionval = str[i];
                                                }
                                            }
                                            //  Toast.makeText(TestQuestion.this, answeroptionval, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                ImageView imageView = new ImageView(TestQuestion.this);
                                imageView.setId(Integer.parseInt(ids));
//                    Glide.with(getApplicationContext()).load(hash).into(imageView);
                                Util.loadImage(hash, imageView);
                                RadioGroup radioGroup = new RadioGroup(TestQuestion.this);
                                radioGroup.setOrientation(RadioGroup.HORIZONTAL);

                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT

                                );

                                if (linearLayout != null) {
                                    linearLayout.addView(textView);
                                    linearLayout.addView(textView1);

                                    linearLayout.addView(imageView);
                                    linearLayout.addView(radioGroup, p);
                                    //linearLayout.addView(radioButton);
                                    // linearLayout.addView(radioButton1);
                                }

                                radioGroup.addView(radioButton, p);
                                radioGroup.addView(radioButton1, p);

                            }
                        }
                        //for (int j = 0; j < subObjDetailsquestion.length(); j++) {
                        // JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ((databaseHelper.getTotalAnsweredGiven(Globalclass.userids, sId, activeDetails) == total_no_of_quest - 1) && (answeroptionval.equals("") || answeroptionval.equals("0")) && !isbookmarkReviewed) {
                        btnSubmitTest.setVisibility(View.VISIBLE);
                //        next.setVisibility(View.GONE);
                        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                            btnSubmitTest.setText(R.string.SUBMIThn);
                            // isSubmit = true;
                        } else {
                            // isSubmit = true;
                            btnSubmitTest.setText("Submit");

                        }

                    } else if ((databaseHelper.getTotalAnsweredGiven(Globalclass.userids, sId, activeDetails) == total_no_of_quest) && !isbookmarkReviewed) {
                        btnSubmitTest.setVisibility(View.VISIBLE);
                       // next.setVisibility(View.GONE);
                        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                            btnSubmitTest.setText(R.string.SUBMIThn);
                            //    isSubmit = true;
                        } else {
                            btnSubmitTest.setText("Submit");
                            //   isSubmit = true;
                        }
                    } else {
                        isSubmit = false;
                        btnSubmitTest.setVisibility(View.GONE);
                        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                            next.setText(R.string.Nexthn);
                        } else {
                            next.setText("Next");
                        }
                    }
                }
        catch (Exception e){
                    e.printStackTrace();
                }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getquestionarrylist() {
        try {
            String json = "";
            valuesbookmark = "1";
            DatabaseHelper mydb = new DatabaseHelper(TestQuestion.this);
            if (Util.ONLINE) {
                json = SharedPreference.getInstance(TestQuestion.this).getString(C.ONLINE_TEST_LIST);
            } else {
                Cursor resss = mydb.gettest_details_json_string(testList.getId(), sId);
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
                // JSONObject jsonobj_2 = (JSONObject) array;
                JSONArray subjsonlanguage = jsonobj_2.getJSONArray("test_languages");
                for (int j = 0; j < subjsonlanguage.length(); j++) {
                    JSONObject jsonobj_2_language = (JSONObject) subjsonlanguage.get(j);
                    String languagenameval = jsonobj_2_language.get("language_name").toString();
                    if (languagenameval.equalsIgnoreCase("Hindi")) {
                        Globalclass.hindipresent = "1";
                    }
                    String language_code = jsonobj_2_language.get("language_code").toString();
                    if (language_code.equalsIgnoreCase("en")) {
                        Globalclass.languagecode = language_code;
                    }
                    tagid.add(j, languagenameval);
                    Student.add(j, language_code);
                }
                String[] spinnerArray = new String[tagid.size()];
                spinnerMap = new HashMap<Integer, String>();
                spinneriduser = new HashMap<Integer, String>();
                for (int i = 0; i < tagid.size(); i++) {
                    spinnerMap.put(i, tagid.get(i));
                    spinnerArray[i] = tagid.get(i);
                    //  spinneriduser.put(i,iduser);
                }
                spinnerArrayAdapter = new ArrayAdapter<String>(
                        this, R.layout.spionnertotext, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.testviewtospinner);
                // ArrayAdapter<String> adapter =new ArrayAdapter<String>(TestQuestion.this,android.R.layout.simple_spinner_item, spinnerArray);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dd_tagid.setAdapter(spinnerArrayAdapter);
                JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
                // JSONObject jsonpaperquestion = (JSONObject) subObjDetails;
                //for (int j = 0; j < subObjDetails.length(); j++) {
                // JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                JSONArray subObjDetailsquestion;
                if (activeDetails.equalsIgnoreCase("0")) {
                    subObjDetailsquestion = subObjDetails.getJSONArray("theoryQuestions");
                } else {
                    subObjDetailsquestion = subObjDetails.getJSONArray("practical_questions");
                }
                final ArrayList<String> q_list = new ArrayList<String>();
                // q_list.clear();
                for (int q = 1; q <= subObjDetailsquestion.length(); q++) {
                    q_list.add(q + "");
                }


                total_no_of_quest = subObjDetailsquestion.length();
                Globalclass.total_no_of_quest_val = subObjDetailsquestion.length();
                //question ki list at upper
                JSONArray subObjDetailsdetails = jsonobj_2.getJSONArray("test_details");
                test_duration = "00";
                for (int j = 0; j < subObjDetailsdetails.length(); j++) {
                    JSONObject jsonobj_2_answer = (JSONObject) subObjDetailsdetails.get(j);
                    test_duration = jsonobj_2_answer.get("test_duration").toString();
                    if (test_duration.equalsIgnoreCase("")) {
                        test_duration = "60";
                    }

                }
                Cursor cursor = mydb.gettimereamining(testList.getId(), sId, Globalclass.userids, activeDetails);
                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    String timereaming = cursor.getString(3);

                    if (timereaming.equalsIgnoreCase("")) {

                    } else {
                        test_duration = timereaming;
                        String str[] = timereaming.split(":");
                        test_duration = str[0].trim();
                    }
                }

                final int[] i = {0};
                {
                    Log.e("min remaining", "" + "min");
                    countDownTimer = new CountDownTimer(Integer.parseInt(test_duration) * 60000, 1000) {
                        int ii = 0;

                        public void onTick(long millisUntilFinished) {
                            String text = String.format(Locale.getDefault(), " %02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                            tvTimer.setText(text);
                            tvRec.setText("Rec : " + getRecTime(test_duration + ":00", tvTimer.getText().toString()));

                            //     if (Main3Activity.rendomclick!="null")
                            //here you can have your logic to set text to edittext
                            //           if (ii%Integer.parseInt(Main3Activity.rendomclick)==0)
                            //    if (ii%10==0)
                            //    takePicture();
                            ii++;
                        }

                        public void onFinish() {
                            tvTimer.setText("TIME FINISHED !");
                            Intent intent = new Intent(TestQuestion.this, ActivityThankyou.class);
                            intent.putExtra(C.TEST, testList);
                            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                            startActivity(intent);
                            finish();
                            // showMessage("Worning","Your Time Has Been finished");
                        }

                    }.start();


                    if (activeDetails.equalsIgnoreCase("1")) {
                        //   tvTimer.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
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

    public void savebookmarkdetails() {
        try {
            //  bookmarkreviewcountvalue = bookmarkreviewcountvalue + 1;
            String optionvaluesrange = "";
            String strid = testquestionid.getText().toString();
            String strdate = getDateTime();
            DatabaseHelper db = new DatabaseHelper(TestQuestion.this);
            int bookmarkflag = 1;
            if (answeroptionval.equalsIgnoreCase("") ||
                    answeroptionval.equalsIgnoreCase("0")) {


           /* if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                showMessage("", "आगे बढ़ने के लिए उत्तर दें");
            } else {
                showMessage("", "Please Give Answer to Move Next");
            }*/
                if (!isbookmarkReviewed) {
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    llQuestion2View.removeAllViews();
                    linearLayout.removeAllViews();
                    Globalclass.guestioncount = Globalclass.tempQuestionNo;
                    linearLayout.removeAllViews();
                    linearLayout.removeView((View) linearLayout.getParent());
                    hour_radio_group.removeAllViews();
                    hour_radio_group1.removeAllViews();
                    linearLayout.removeAllViews();
                    new LongOperationgetquestiondetails().execute();
                } else {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                    } else {
                        showMessage("", "Please Give Answer to Move Next");
                    }
                }
            } else {

                boolean succ;
                if (activeDetails.equalsIgnoreCase("0")) {
                    succ = db.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, isbookmark, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "100", "0", activeDetails, answeroptionnoval);
                } else {
                    String rangemark = "";
                    String strquestion[] = answeroptionval.split(",");
                    for (int i = 0; i < strquestion.length; i++) {
                        rangemark = "100" + "," + rangemark;
                    }
                    succ = db.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, isbookmark, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, rangemark, "1", activeDetails, answeroptionnoval);
                }
                if (succ == true) {
                    //  Toast.makeText(TestQuestion.this,"Update:-" +testList.getId()+strid+ answeroptionval+ Globalclass.userids+ strdate+ "0"+ "0"+ String.valueOf(Globalclass.guestioncount) , Toast.LENGTH_LONG).show();
                    String str = btn_ReviewBookmark.getText().toString();
                    if (str.equalsIgnoreCase("Submit") || str.equalsIgnoreCase(String.valueOf("जमा करें"))) {
                        // if(isSubmit){
                        DatabaseHelper dbvsl = new DatabaseHelper(TestQuestion.this);
                        Boolean success = dbvsl.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                        if (success == true) {
                            boolean successval = dbvsl.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                            if (successval == true) {
                                Globalclass.tempQuestionNo = 0;
                                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                Globalclass.total_no_of_quest_val = 0;
                                // Globalclass.userids = "";
                                currentbookmark = "";
                                isbookmark = "";
                                reviewquestion = "";
                                isbookmark = "0";
                                //     isAnyQuestionBookMarked = false;
                                setupAlarmManagerstop();
                                Globalclass.lastpicturecandidate = "1";
//                            CameraManager cr = new CameraManager(TestQuestion.this);
//                            cr.takePhoto();
                                currentbookmark = "0";
                                currentcount = "1";
                                openPhotoCaptureActivity();

                            } else {
                                showMessage("", "Unable to submit test");
                            }
                        } else {
                            showMessage("Oppps", "Somthing went wrong ...");
                        }
                    } else {
                        currentbookmark = "";
                        isbookmark = "";
                        reviewquestion = "";
                        isbookmark = "0";
                        linearLayout.removeView((View) linearLayout.getParent());
                        hour_radio_group.removeAllViews();
                        llQuestion2View.removeAllViews();
                        linearLayout.removeAllViews();
                        bookmarkreviewcountvalue = bookmarkreviewcountvalue + 1;
                        if (bookmarkreviewcountvalue == lastquestiontoview - 1) {
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btn_ReviewBookmark.setText(R.string.SUBMIThn);
                            } else {
                                btn_ReviewBookmark.setText("Submit");
                            }
                        }
                        int id = Integer.parseInt(bookmarkreview.get(bookmarkreviewcountvalue));
                        Globalclass.tempQuestionNo = id;
                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
                        linearLayout.removeAllViews();
                        linearLayout.removeView((View) linearLayout.getParent());
                        hour_radio_group.removeAllViews();
                        llQuestion2View.removeAllViews();
                        linearLayout.removeAllViews();
                        hour_radio_group1.removeAllViews();
                        new LongOperationgetquestiondetails().execute();
                        progressDialog.show();
                        new Thread() {

                            public void run() {
                                try {

                                    sleep(1000);

                                } catch (Exception e) {

                                    Log.e("tag", e.getMessage());
                                }
                                // dismiss the progress dialog
                                progressDialog.dismiss();
                            }
                        }.start();
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void uploadBitmap(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        try {
            final ProgressDialog Dialog1 = new ProgressDialog(TestQuestion.this);
            Dialog1.setMessage("Uploding Video Please wait...");
            try {
                Dialog1.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //getting the tag from the edittext
            //final String tags = editTextTags.getText().toString().trim();
            String url = C.API_UPLOAD_VIDEO_AND_PHOTO;
            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            //   JSONObject obj = new JSONObject(new String(response.data));
                            try {
                                if (Dialog1 != null && Dialog1.isShowing()) {
                                    Dialog1.dismiss();
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            //  mMediaRecorder.reset();
                            //   mCamera.release();
                            currentbookmark = "0";
                            isbookmark = "";
                            reviewquestion = "";
                            isbookmark = "0";
                            setupAlarmManagerstop();
                            Globalclass.lastpicturecandidate = "1";
                            //  CameraManager cr = new CameraManager(TestQuestion.this);
                            //  cr.takePhoto();
                            currentbookmark = "0";
                            currentcount = "1";
                            //   Intent intent = new Intent(TestQuestion.this, candidatefeedbackform.class);
                            //  startActivity(intent);
                            //  finish();
                            //Globalclass.evidencecapture = "hola";
                            // finish();
                            // showMessage("","Image Uploded...");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                if (Dialog1 != null && Dialog1.isShowing()) {
                                    Dialog1.dismiss();
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                          //  uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                            //   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userId", SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getUserID());
                    params.put("api_key", SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getApiKey());
                    params.put("testID", testList.getId());
                    params.put("uniqueID", testList.getUniqueID());
                    params.put("picType", imagetypeval);
                    params.put("candidate_id", tagidval);
                    params.put("version", "m");
                    params.put("target_dir", targetid);
                    params.put("filename", nameimageval);
                    // params.put("imageHash", encodedImage);
                    // params.put("imageHash", encodedImage);
                    return params;
                }

                /*
                 * Here we are passing image by renaming it with a unique name
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("imageHash", new DataPart(nameimageval, readBytesFromFile(imageurlval)));
                    return params;
                }
            };
          //  Volley.newRequestQueue(this,new HurlStack(null, new ClientSSLSocketFactory(300000).getSocketFactory())).add(volleyMultipartRequest);
            RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack() {


                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection urlConnection =
                            (HttpsURLConnection)url.openConnection();
                    urlConnection.setHostnameVerifier(Util.getHostname());


                    return urlConnection;
                }
            });
            requestQueue.add(volleyMultipartRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void uploadBitmapvideoinbetween(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        final ProgressDialog Dialog1 = new ProgressDialog(TestQuestion.this);
        Dialog1.setMessage("Uploding Video Please wait...");
        Dialog1.show();
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        String url = C.API_UPLOAD_VIDEO_AND_PHOTO;
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //   JSONObject obj = new JSONObject(new String(response.data));
                        Dialog1.dismiss();
                        Globalclass.capture = "1";
                        // Intent intent = new Intent(TestQuestion.this,PictureCaptureBefore.class);
                        // startActivity(intent);
                        //Globalclass.evidencecapture = "hola";
                        // finish();
                        // showMessage("","Image Uploded...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog1.dismiss();
                        uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                        //   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getUserID());
                params.put("api_key", SharedPreference.getInstance(TestQuestion.this).getUser(C.LOGIN_USER).getApiKey());
                params.put("testID", testList.getId());
                params.put("uniqueID", testList.getUniqueID());
                params.put("picType", imagetypeval);
                params.put("candidate_id", tagidval);
                params.put("version", "m");
                params.put("target_dir", targetid);
                params.put("filename", nameimageval);
                // params.put("imageHash", encodedImage);
                // params.put("imageHash", encodedImage);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("imageHash", new DataPart(nameimageval, readBytesFromFile(imageurlval)));
                return params;
            }
        };
      //  Volley.newRequestQueue(this,new HurlStack(null, new ClientSSLSocketFactory(300000).getSocketFactory())).add(volleyMultipartRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack() {


            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection)url.openConnection();
                urlConnection.setHostnameVerifier(Util.getHostname());


                return urlConnection;
            }
        });
        requestQueue.add(volleyMultipartRequest);
    }


    private static byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
