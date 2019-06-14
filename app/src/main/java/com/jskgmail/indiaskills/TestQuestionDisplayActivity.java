package com.jskgmail.indiaskills;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.util.DisplayMetrics;
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
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jskgmail.indiaskills.camera.CemeraService;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.Language;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.UploadZipResponse;
import com.jskgmail.indiaskills.util.Api;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.ProgressRequestBody;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestQuestionDisplayActivity extends AppCompatActivity implements SurfaceHolder.Callback, ProgressRequestBody.UploadCallbacks {

    @BindView(R.id.audio_layout)
    View audioLayout;
    @BindView(R.id.video_layout)
    View videoLayout;

    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.audioView)
    UniversalVideoView audioView;
    @BindView(R.id.mediaControllerAudio)
    UniversalMediaController mediaControllerAudio;
    @BindView(R.id.scale_button)
    ImageButton turnButton;

    @BindView(R.id.loading_text)
    TextView loadingText;
    @BindView(R.id.btnSubmitTest)
    Button btnSubmitTest;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.tvTestName)
    TextView tvTestName;
    @BindView(R.id.spnLanguage)
    Spinner spnLanguage;
    @BindView(R.id.ibtn_vedio)
    ImageButton ibtnVedio;
    @BindView(R.id.ibnt_image)
    ImageButton ibntImage;
    @BindView(R.id.textViewtimer)
    TextView textViewtimer;
    @BindView(R.id.testquestionid)
    TextView testquestionid;
    @BindView(R.id.surfaceView_camera)
    SurfaceView surfaceViewCamera;
    @BindView(R.id.tvRec)
    TextView tvRec;
    @BindView(R.id.framelayout_recording)
    FrameLayout framelayoutRecording;
    @BindView(R.id.tv_Qustionnumber)
    TextView tvQustionnumber;
    @BindView(R.id.tv_question_name)
    TextView tvQuestionName;
    @BindView(R.id.imgvalues)
    ImageButton imgvalues;
    @BindView(R.id.optionvalues)
    LinearLayout LLOptionvaluesView;
    @BindView(R.id.hour_radio_group)
    RadioGroup hourRadioGroup;
    @BindView(R.id.llQuestion2View)
    LinearLayout llQuestion2View;
    @BindView(R.id.btn_priviousquestion)
    Button btnPriviousquestion;
    @BindView(R.id.btn_nextquestion)
    Button btnNextquestion;
    @BindView(R.id.btn_bookmarkquestion)
    Button btnBookmarkquestion;
    @BindView(R.id.btn_ReviewBookmark)
    Button btnReviewBookmark;
    @BindView(R.id.hour_radio_group1)
    RadioGroup hourRadioGroup1;
    @BindView(R.id.hour_scroll_view)
    ScrollView hourScrollView;
    @BindView(R.id.ivQuestionImage)
    ImageView ivQuestionImage;

    Dialog progressDialog;
    TestList testList;
    String activeDetails, sId;
    DatabaseHelper databaseHelper;
    ArrayAdapter<String> spinnerArrayAdapter;
    int total_no_of_quest;
    RadioButton radioButtonO;
    boolean isbookmarkReviewed = false;
    String json = "";
    EditText editTextType4;
    boolean isSubmit = false, isShufflingImage = false;
    boolean runningbookmark = false;
    int lastquestiontoview = 0, bookmarkreviewcountvalue = 0;
    ArrayList<String> bookmarkreview;
    int imageCount = 0;
    ArrayList<String> imageArray;
    String randomePic, isVideoRecord;
    private SurfaceHolder mHolder;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private String VIDEO_PATH_NAME, test_duration, reviewquestion = "0", filename, currentbookmark = "", answeroptionnoval = "", currentcount = "1", bookmarkcount = "0", isbookmark = "0", media, answerMedia, questiontype, mediaType, answerMediaType, answeroptionval = "";
    private CountDownTimer countDownTimer;
    private String moveToPrev;
    private String fixedTime;
    private String timeFrequency = "";
    private Timer timer;
    private String submitWithoutAttemptAll;
    private Dialog progressDialogUp;

    private String rangeFlag = "1";
    private HashMap<String, String> hashMap;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question_display);

        ButterKnife.bind(this);

        hashMap = new HashMap<>();
        Drawable spinnerDrawable = spnLanguage.getBackground().getConstantState().newDrawable();

        spinnerDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spnLanguage.setBackground(spinnerDrawable);
        } else {
            spnLanguage.setBackgroundDrawable(spinnerDrawable);
        }
        audioLayout.setVisibility(View.GONE);
        videoView.setMediaController(mediaController);
        audioView.setMediaController(mediaControllerAudio);
        loadingText.setText("");
        turnButton.setVisibility(View.GONE);
        imageArray = new ArrayList<>();
        //setVideoLayout();
        changeLanguage();
        bookmarkreview = new ArrayList<String>();
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        databaseHelper = new DatabaseHelper(TestQuestionDisplayActivity.this);
        sId = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tvTestName.setText(SharedPreference.getInstance(TestQuestionDisplayActivity.this).getTest(C.ONGOING_TEST).getTestName());
        getTestJson();
        json = getJson();
        try {
            JSONObject scheduleSettings = getScheduleSettings();
            moveToPrev = scheduleSettings.getString("movtoprev");
            fixedTime = scheduleSettings.getString("fixedTime");
            timeFrequency = scheduleSettings.getString("tfeq");
            submitWithoutAttemptAll = scheduleSettings.getString("submit_without_all_attempt");

            if (fixedTime.equals(C.YES)) {
                startTimer();
            }

            if (moveToPrev.equals(C.NO) || fixedTime.equals(C.YES)) {
                btnPriviousquestion.getLayoutParams().width = 0;
                btnPriviousquestion.getLayoutParams().height = 0;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPriviousquestion.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                btnPriviousquestion.setBackground(null);
                btnBookmarkquestion.getLayoutParams().width = 0;
                btnBookmarkquestion.getLayoutParams().height = 0;
                layoutParams = (LinearLayout.LayoutParams) btnPriviousquestion.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                btnBookmarkquestion.setBackground(null);
            }

            if (activeDetails.equalsIgnoreCase("0")) {
                try {
                    surfaceViewCamera.setVisibility(View.GONE);
                    framelayoutRecording.setVisibility(View.GONE);
                    randomePic = scheduleSettings.getString("random_picture");
                    if (randomePic.equals("1")) {
                        setupAlarmManager(Integer.parseInt(scheduleSettings.getString("rm_pic_time")));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                isVideoRecord = scheduleSettings.getString("video_recording");
                if (isVideoRecord.equals("1")) {
                    mHolder = surfaceViewCamera.getHolder();
                    mHolder.addCallback(this);
                    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                if (isVideoRecord.equals("1")) {
                    framelayoutRecording.setVisibility(View.VISIBLE);
                    surfaceViewCamera.setVisibility(View.VISIBLE);

                    // mMediaRecorder.start();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                mMediaRecorder.start();
                                mCamera.setDisplayOrientation(90);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1 * 1000);
                } else {
                    surfaceViewCamera.setVisibility(View.GONE);
                    framelayoutRecording.setVisibility(View.GONE);
                }
            }
            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                btnNextquestion.setText(R.string.Nexthn);
                btnBookmarkquestion.setText(R.string.reviewbookmarkhn);
                btnPriviousquestion.setText(R.string.PREVIOUShn);
                btnReviewBookmark.setText(R.string.reviewbookmarkhn);
            } else {
                btnNextquestion.setText(R.string.Next);
                btnPriviousquestion.setText(R.string.PREVIOUS);
                btnReviewBookmark.setText(R.string.reviewbookmark);
                btnBookmarkquestion.setText(R.string.BOOKMARK);
            }
            getquestionarrylist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        try {
            long time = 0;
            time = 60 * Integer.parseInt(timeFrequency) * 1000;
            timer = new Timer();
            if (activeDetails == "1") {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btnNextquestion.getVisibility() == View.GONE) {
                            isSubmit = true;
                        } else {
                            Globalclass.tempQuestionNo = Globalclass.guestioncount + 1;
                            next();
                        }
                    }
                }, time);
            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        if (btnNextquestion.getVisibility() == View.GONE) {
                            isSubmit = true;
                        } else {
                            Globalclass.tempQuestionNo = Globalclass.guestioncount + 1;
                        }

                        next();

                    }
                }, time, time);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setVideoLayout() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        params.width = (int) (300 * metrics.density);
        params.height = (int) (250 * metrics.density);
        params.leftMargin = 30;
        videoView.setLayoutParams(params);
    }

    void changeLanguage() {
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                if (Globalclass.countlanguage.equalsIgnoreCase("0")) {
                if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
//                        if (Globalclass.hindipresent.equalsIgnoreCase("1")) {
//                            spnLanguage.setSelection(spinnerArrayAdapter.getPosition("Hindi"));
//                        } else {
//                            spnLanguage.setSelection(spinnerArrayAdapter.getPosition("English"));
//                        }
                    Globalclass.countlanguage = "1";
                }
//                }
                String strval = spnLanguage.getSelectedItem().toString();
                if (strval.equalsIgnoreCase("English")) {
                    insertLogHistory(testquestionid.getText().toString(), C.Event_Change_Language, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                    btnNextquestion.setText(R.string.Next);
                    btnPriviousquestion.setText(R.string.PREVIOUS);
                    btnReviewBookmark.setText(R.string.reviewbookmark);
                    btnBookmarkquestion.setText(R.string.BOOKMARK);
                    Globalclass.spinnerstringlang = "en";
                } else {
                    insertLogHistory(testquestionid.getText().toString(), C.Event_Change_Language, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                    Globalclass.spinnerstringlang = "hn";
                    btnNextquestion.setText(R.string.Nexthn);
                    btnBookmarkquestion.setText(R.string.reviewbookmarkhn);
                    btnPriviousquestion.setText(R.string.PREVIOUShn);
                    btnReviewBookmark.setText(R.string.reviewbookmarkhn);
                }
                String item = spnLanguage.getSelectedItem().toString();
                boolean success = getlanguagevalues(item);
                if (success) {
                    LLOptionvaluesView.removeView((View) LLOptionvaluesView.getParent());
                    hourRadioGroup.removeAllViews();
                    LLOptionvaluesView.removeAllViews();
                    hourRadioGroup1.removeAllViews();
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

    }

    public boolean getlanguagevalues(String values) {
        Boolean success = false;
        try {

            DatabaseHelper mydb = new DatabaseHelper(this);

            String json = "";
            if (Util.ONLINE) {
                json = SharedPreference.getInstance(TestQuestionDisplayActivity.this).getString(C.ONLINE_TEST_LIST);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    private void setupAlarmManager(int minute) {
        try {
            PendingIntent pi = PendingIntent.getService(
                    this,
                    101,
                    new Intent(this, CemeraService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minute * 60000, pi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnSubmitTest, R.id.btn_priviousquestion, R.id.btn_nextquestion, R.id.btn_bookmarkquestion, R.id.btn_ReviewBookmark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitTest:
                submit();
                break;
            case R.id.btn_priviousquestion:
                if (Globalclass.capture.equalsIgnoreCase("1")) {
                    Globalclass.capture = "0";
                    if (isVideoRecord.equals("1")) {
                        mMediaRecorder.start();
                    }
                }
                Globalclass.tempQuestionNo = Globalclass.tempQuestionNo - 1;
                removeViews();
                new LongOperationgetquestiondetails().execute();
                break;
            case R.id.btn_nextquestion:

                if (submitWithoutAttemptAll.equals(C.NO) && moveToPrev.equals(C.NO) && answeroptionval.isEmpty()) {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                    } else {
                        showMessage("", "Please Give Answer to Move Next");
                    }
                } else {
                    Globalclass.tempQuestionNo = Globalclass.guestioncount + 1;
                    next();
                }

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                break;
            case R.id.btn_bookmarkquestion:
                if (Globalclass.capture.equalsIgnoreCase("1")) {
                    Globalclass.capture = "0";
                    if (isVideoRecord.equals("1")) {
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
                }
                //
                // centerLockHorizontalScrollview.setBookmark(currIndex, 1);

                currentbookmark = "1";
                isbookmark = "1";
                reviewquestion = "1";
                if (activeDetails.equalsIgnoreCase("1")) {
                    Globalclass.bookmardepractrical = "1";
                }
                insertLogHistory(testquestionid.getText().toString(), C.Event_Bookmark, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                break;
            case R.id.btn_ReviewBookmark:
                hourRadioGroup1.removeAllViews();
                savebookmarkdetails();
                break;
        }
    }

    void submit() {
        try {
            if (activeDetails.equals("0") && questiontype.equals("4")) {
                answeroptionval = editTextType4.getText().toString();
            }

            if ((answeroptionval.equalsIgnoreCase("") ||
                    answeroptionval.equalsIgnoreCase("0")) && (answeroptionnoval.equals("") || answeroptionnoval.equals(""))) {

                if (submitWithoutAttemptAll.equals(C.YES)) {

                    showBeforeSubmit("", "Are you sure do you want to submit the test?");


                } else {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        showMessage("", "आगे बढ़ने के लिए उत्तर दें");
                    } else {
                        showMessage("", "Please Give Answer to Move Next");
                    }
                }
                return;
            }
            isSubmit = true;
            next();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getquestionarrylist() {
        try {


            json = getJson();
            try {


                JSONObject array = (new JSONObject(json)).getJSONObject("data");
                //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
                //JSONObject array=json.getJSONObject("data");
                JSONObject jsonobj_2 = (JSONObject) array;
                // JSONObject jsonobj_2 = (JSONObject) array;
                JSONArray subjsonlanguage = jsonobj_2.getJSONArray("test_languages");
                String[] spinnerArray = new String[subjsonlanguage.length()];
                HashMap<String, Language> stringLanguageHashMap = new HashMap<>();
                for (int j = 0; j < subjsonlanguage.length(); j++) {
                    JSONObject jsonobj_2_language = (JSONObject) subjsonlanguage.get(j);
                    spinnerArray[j] = jsonobj_2_language.get("language_name").toString();
                    Language language = new Language();
                    language.setLanguageName(jsonobj_2_language.get("language_name").toString());
                    language.setLanguageCode(jsonobj_2_language.get("language_code").toString());
                    stringLanguageHashMap.put(jsonobj_2_language.get("language_name").toString(), language);
                    if (jsonobj_2_language.get("language_code").toString().equalsIgnoreCase("en")) {
                        Globalclass.languagecode = jsonobj_2_language.get("language_code").toString();
                    }
                }


                spinnerArrayAdapter = new ArrayAdapter<String>(
                        this, R.layout.spionnertotext, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.testviewtospinner);
                spnLanguage.setAdapter(spinnerArrayAdapter);


                JSONObject subObjDetails = jsonobj_2.getJSONObject("questions");
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
                Cursor cursor = databaseHelper.gettimereamining(testList.getId(), sId, Globalclass.userids, activeDetails);
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


                countDownTimer = new CountDownTimer(Integer.parseInt(test_duration) * 60000, 1000) {


                    public void onTick(long millisUntilFinished) {
                        String text = String.format(Locale.getDefault(), " %02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                        textViewtimer.setText(text);
                        tvRec.setText("Rec : " + Util.getRecTime(test_duration + ":00", textViewtimer.getText().toString()));
                    }

                    public void onFinish() {
                        textViewtimer.setText("TIME FINISHED !");
                        Intent intent = new Intent(TestQuestionDisplayActivity.this, ActivityThankyou.class);
                        intent.putExtra(C.TEST, testList);
                        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                        startActivity(intent);
                        finish();
                    }

                }.start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next() {
        try {
            if (Globalclass.capture.equalsIgnoreCase("1")) {
                Globalclass.capture = "0";
                if (isVideoRecord.equals("1")) {
                    mMediaRecorder.start();
                }
            }

            if (testList.getId().equalsIgnoreCase("") || testList.getId().isEmpty() || activeDetails.equalsIgnoreCase("") || sId.equalsIgnoreCase("") || Globalclass.userids.equalsIgnoreCase("")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TestQuestionDisplayActivity.this);
                builder.setMessage("Opsss Somthing went wrong please login again");
                builder.setCancelable(false);
                builder.setNeutralButton("Login Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(TestQuestionDisplayActivity.this, ActivityMain.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        finish();
                        //   Toast.makeText(TestQuestionDisplayActivity.this, "Neutral button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            } else {
                if (activeDetails.equals("0") && questiontype.equals("4")) {
                    answeroptionval = editTextType4.getText().toString();
                }

                new savinganswerdetails().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    public void savesetails() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseHelper db = new DatabaseHelper(TestQuestionDisplayActivity.this);
                    String str = textViewtimer.getText().toString();
                    // Save Time
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

                        boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                        savevalues();
                    } else {
                        if (activeDetails.equalsIgnoreCase("0")) {
                            if (isSubmit) {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                savevalues();
                            } else if (answeroptionval.equalsIgnoreCase("") && answeroptionnoval.equalsIgnoreCase("")) {

                                removeViews();
                                new LongOperationgetquestiondetails().execute();
                            } else {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                savevalues();
                            }
                        } else if (activeDetails.equalsIgnoreCase("1")) {

                            if (isSubmit) {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                savevalues();
                            } else if (answeroptionval.equalsIgnoreCase("") && answeroptionnoval.equalsIgnoreCase("")) {
                                removeViews();
                                new LongOperationgetquestiondetails().execute();
                            } else {
                                boolean b = db.insert_logdetails(Globalclass.idcandidate, testList.getId(), sId, SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER).getUserID(), reviewquestion, str, answeroptionval);
                                savevalues();
                            }
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savevalues() {
        try {
            String strid = testquestionid.getText().toString();
            String strdate = Util.getCurrentTimeStamp();
            boolean alreadyhave = databaseHelper.getdetailsvaluesquestion(testList.getId(), Globalclass.userids, strid);
            if (alreadyhave) {
                int bookmarkflag = 0;
                if (currentbookmark.equalsIgnoreCase("1")) {
                    bookmarkflag = 1;
                }
                boolean succ;


                if (activeDetails.equalsIgnoreCase("0")) {
                    succ = databaseHelper.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "{100}", "0", activeDetails, answeroptionnoval);
                } else {

                    String rangemark = "";
                    for (String key : answeroptionval.split(",")) {
                        rangemark = rangemark + hashMap.get(key) + ",";
                    }
                    succ = databaseHelper.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, rangemark, rangeFlag, activeDetails, answeroptionnoval);

                }
                if (submitWithoutAttemptAll.equals(C.YES)) {
                    succ = true;
                }

                if (succ) {
                    if (isSubmit) {
                        if (activeDetails.equalsIgnoreCase("1")) {
                            if (getIsAnyBookMarkQuestionAvailable()) {
                                gotoBookMarkQuestions();
                            } else {
                                Boolean success = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                if (success == true) {
                                    boolean successval = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {
                                        setConstants();
                                        openPhotoCaptureActivity();
                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                } else {
                                    showMessage("Oppps", "Somthing went wrong ...");
                                }
                            }
                        } else {
                            if (!getIsAnyBookMarkQuestionAvailable()) {
//                                if (reviewquestion.equalsIgnoreCase("1")) {
//                                    showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
//                                } else {
                                Boolean success = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                if (success == true) {
                                    boolean successval = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {
                                        setConstants();
                                        if (activeDetails.equalsIgnoreCase("1")) {
                                            if (Util.ONLINE) {
                                                uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                                            } else {
                                                success = databaseHelper.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                                if (!success) {
                                                    Toast.makeText(TestQuestionDisplayActivity.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            if (isVideoRecord.equals("1")) {
                                                mMediaRecorder.reset();
                                                mCamera.release();
                                            }
                                        }
                                        openPhotoCaptureActivity();


                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                } else {
                                    showMessage("Oppps", "Somthing went wrong ...");
                                }
//                                }
                            } else {
                                gotoBookMarkQuestions();
                            }
                        }
                    } else {
                        removeViews();

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
                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }.start();
                        if (activeDetails.equalsIgnoreCase("0")) {
                            new LongOperationgetquestiondetails().execute();
                        } else if (activeDetails.equalsIgnoreCase("1")) {
                            if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                                if (activeDetails.equalsIgnoreCase("1")) {
                                    gotoNextQuestionForPractical();
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
                                        boolean success = databaseHelper.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                        if (!success) {
                                            Toast.makeText(TestQuestionDisplayActivity.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                        }
                                        if (isVideoRecord.equals("1")) {
                                            mMediaRecorder.reset();
                                            mCamera.release();
                                        }
                                    }
                                    gotoNextQuestionForPractical();
                                }
                            }

                        }
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            } else {

                int bookmarkflag = 0;

                if (currentbookmark.equalsIgnoreCase("1")) {
                    bookmarkflag = 1;
                }
                boolean success;
                if (activeDetails.equalsIgnoreCase("0")) {
                    success = databaseHelper.insert_useranswer(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, "{100}", "0", activeDetails, answeroptionnoval);
                } else {

                    String rangemark = "";
                    for (String key : answeroptionval.split(",")) {
                        rangemark = rangemark + hashMap.get(key) + ",";
                    }
                    success = databaseHelper.insert_useranswer(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, bookmarkcount, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, rangemark, rangeFlag, activeDetails, answeroptionnoval);
                }
                if (submitWithoutAttemptAll.equals(C.YES)) {
                    success = true;
                }
                if (success == true) {
                    if (isSubmit) {
                        if (activeDetails.equalsIgnoreCase("1")) {
                            if (getIsAnyBookMarkQuestionAvailable()) {
                                gotoBookMarkQuestions();
                            } else {
                                DatabaseHelper db1 = new DatabaseHelper(TestQuestionDisplayActivity.this);
                                Boolean success1 = db1.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                                if (success1 == true) {
                                    boolean successval = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                    if (successval == true) {

                                        setConstants();

                                        uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);

                                    } else {
                                        showMessage("", "Unable to submit test");
                                    }
                                } else {
                                    showMessage("Oppps", "Somthing went wrong ...");
                                }
                            }
                        } else {


                            if (!getIsAnyBookMarkQuestionAvailable()) {
//                                if (reviewquestion.equalsIgnoreCase("1")) {
//                                    showMessage("", "You Didn't Answer Some Question Please Attempt All Question");
//                                } else {
                                setupAlarmManagerstop();
                                Globalclass.lastpicturecandidate = "1";

                                boolean successval = databaseHelper.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                                if (successval == true) {

                                    if (Util.ONLINE) {
                                        if (activeDetails.equalsIgnoreCase("1")) {
                                            uploadBitmap(VIDEO_PATH_NAME, sId, filename, sId, "video", filename);
                                        }
                                    } else {
                                        if (activeDetails.equalsIgnoreCase("1")) {
                                            success = databaseHelper.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                            if (!success) {
                                                Toast.makeText(TestQuestionDisplayActivity.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                            }
                                            if (isVideoRecord.equals("1")) {
                                                mMediaRecorder.reset();
                                                mCamera.release();
                                            }
                                        }

                                    }
                                    setConstants();
                                    openPhotoCaptureActivity();

                                } else {
                                    showMessage("", "Unable to submit test");
                                }
//                                }

                            } else {
                                gotoBookMarkQuestions();
                            }
                        }
                    } else {
                        removeViews();
                        isbookmark = "0";
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
                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }.start();
                        if (Globalclass.total_no_of_quest_val == Globalclass.guestioncount) {
                            if (activeDetails.equalsIgnoreCase("1")) {
                                gotoNextQuestionForPractical();
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
                                        boolean success12 = databaseHelper.insert_imagesval("Video", "non", filename, testList.getId(), VIDEO_PATH_NAME, sId, sId);
                                        if (success12 == true) {

                                        } else {
                                            Toast.makeText(TestQuestionDisplayActivity.this, "Not able to save ", Toast.LENGTH_LONG).show();
                                        }
                                        if (isVideoRecord.equals("1")) {
                                            mMediaRecorder.reset();
                                            mCamera.release();
                                        }
                                        gotoNextQuestionForPractical();
                                    }

                                }
                            }

                        }
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadBitmap(final String imageurlval, final String tagidval, String nameimageval, String targetid, final String imagetypeval, String nameimageorg) {
        try {
            shutdown();
            progressDialogUp = Util.getProgressDialog(this, R.string.uploading_video_please_wait);
            progressDialogUp.show();
            File file = new File(imageurlval);
            // Uri fileUri  =  Uri.fromFile(new File(filPath));
            ResponseLogin responseLogin = SharedPreference.getInstance(TestQuestionDisplayActivity.this).getUser(C.LOGIN_USER);

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), responseLogin.getUserID());
            RequestBody api_key = RequestBody.create(MediaType.parse("text/plain"), responseLogin.getApiKey());
            RequestBody testID = RequestBody.create(MediaType.parse("text/plain"), testList.getId());
            RequestBody uniqueID = RequestBody.create(MediaType.parse("text/plain"), testList.getUniqueID());
            RequestBody picType = RequestBody.create(MediaType.parse("text/plain"), imagetypeval);
            RequestBody candidate_id = RequestBody.create(MediaType.parse("text/plain"), tagidval);
            RequestBody version = RequestBody.create(MediaType.parse("text/plain"), "m");
            RequestBody target_dir = RequestBody.create(MediaType.parse("text/plain"), targetid);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), nameimageval);

            //  RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            String credentials = "tagusp:t@g$c0re";
            String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            ProgressRequestBody fileBody = new ProgressRequestBody(file, "multipart/form-data", this);

            //  RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("filename", nameimageval, fileBody);
            //creating a file
            //   File file = new File(getRealPathFromURI(fileUri));

            //creating request body for file

            //The gson builder
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES)
                    .writeTimeout(30, TimeUnit.MINUTES).addInterceptor(interceptor).build();
            //creating retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL).
                            client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            //creating our api
            Api api = retrofit.create(Api.class);

            //creating a call and calling the upload image method
            Call<UploadZipResponse> call = api.uploadImageVideo(auth, body, userId, api_key, testID, uniqueID, picType, candidate_id, version, target_dir);

            call.enqueue(new Callback<UploadZipResponse>() {
                @Override
                public void onResponse(Call<UploadZipResponse> call, retrofit2.Response<UploadZipResponse> response) {
                    progressDialogUp.dismiss();
                    currentbookmark = "0";
                    isbookmark = "";
                    reviewquestion = "";
                    isbookmark = "0";
                    setupAlarmManagerstop();
                    Globalclass.lastpicturecandidate = "1";
                    currentbookmark = "0";
                    currentcount = "1";
                    if (isSubmit)
                        openPhotoCaptureActivity();
                    else
                        gotoNextQuestionForPractical();
                }

                @Override
                public void onFailure(Call<UploadZipResponse> call, Throwable t) {
                    progressDialogUp.dismiss();
                    gotoNextQuestionForPractical();
                    if (isSubmit)
                        openPhotoCaptureActivity();
                    else
                        gotoNextQuestionForPractical();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialogUp.dismiss();
            if (isSubmit)
                openPhotoCaptureActivity();
            else
                gotoNextQuestionForPractical();
        }
        //finally performing the call
    }

    void removeViews() {
        LLOptionvaluesView.removeView((View) LLOptionvaluesView.getParent());
        hourRadioGroup.removeAllViews();
        llQuestion2View.removeAllViews();
        LLOptionvaluesView.removeAllViews();
        Globalclass.guestioncount = Globalclass.tempQuestionNo;
        hourRadioGroup1.removeAllViews();
    }

    void gotoBookMarkQuestions() {
        LayoutInflater layoutInflater = LayoutInflater.from(TestQuestionDisplayActivity.this);
        View mView = null;
        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
            mView = layoutInflater.inflate(R.layout.validatebookmarkhindi, null);
        } else {
            mView = layoutInflater.inflate(R.layout.validatebookmarkquestion, null);
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestQuestionDisplayActivity.this);
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
    }

    void setConstants() {
        Globalclass.tempQuestionNo = 0;
        Globalclass.guestioncount = Globalclass.tempQuestionNo;
        Globalclass.total_no_of_quest_val = 0;
        reviewquestion = "";
        isbookmark = "0";
        setupAlarmManagerstop();
        Globalclass.lastpicturecandidate = "1";
        currentbookmark = "0";
        currentcount = "1";
        bookmarkcount = "0";
    }

    private void openPhotoCaptureActivity() {

        try {
            long time = getTotalTime(test_duration + ":00", textViewtimer.getText().toString());

            boolean isSuccess = databaseHelper.updateTotalTimeTaken(testList.getId(), Globalclass.userids, sId, activeDetails, time + "");

            shutdown();

            Intent intent = new Intent(TestQuestionDisplayActivity.this, ActivityLastImageCapture.class);
            intent.putExtra(C.TEST, testList);
            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void gotoNextQuestionForPractical() {
        if (activeDetails.equalsIgnoreCase("1")) {
            Globalclass.countlanguage = "0";
            Intent intent = new Intent(TestQuestionDisplayActivity.this, TestQuestionDisplayActivity.class);
            intent.putExtra(C.TEST, testList);
            intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
            startActivity(intent);
            finish();
        }

    }

    void getTestJson() {
        if (Util.ONLINE) {
            json = SharedPreference.getInstance(TestQuestionDisplayActivity.this).getString(C.ONLINE_TEST_LIST);
        } else {

            Cursor resss = databaseHelper.gettest_details_json_string(testList.getId(), sId);
            if (resss.getCount() == 0) {
                showMessage("Error", "Nothing found");
                return;
            }
            resss.moveToFirst();
            json = resss.getString(1);
        }
    }

    public void getquestiondetails() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        radioButtonO = null;

                        answeroptionval = "";
                        LinearLayout ll = null;
                        ViewGroup hourButtonLayout = (ViewGroup) findViewById(R.id.hour_radio_group1);  // This is the id of the RadioGroup we defined
                        for (int i = 0; i < Globalclass.total_no_of_quest_val; i++) {

                            if (i % 2 == 0) {
                                ll = new LinearLayout(TestQuestionDisplayActivity.this);
                                ll.setOrientation(LinearLayout.HORIZONTAL);
                                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                ll.setWeightSum(2f);
                                ll.setLayoutParams(layoutParams);
                                hourButtonLayout.addView(ll);
                            }

                            final RadioButton button = new RadioButton(TestQuestionDisplayActivity.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, hourButtonLayout.getWidth() / 4, 1f);
                            params.setMargins(15, 15, 15, 15);
                            button.setLayoutParams(params);
                            button.setId(i);
                            button.setPadding(8, 8, 8, 8);
                            button.setText(Integer.toString(i + 1));
                            button.setGravity(Gravity.CENTER);
                            button.setButtonDrawable(R.drawable.null_selector);
                            button.setChecked(i == Globalclass.guestioncount);// Only select button with same index as currently selected number of hours
                            DatabaseHelper dbs = new DatabaseHelper(TestQuestionDisplayActivity.this);
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
                            }


                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (moveToPrev.equals(C.NO) || fixedTime.equals(C.YES)) {
                                        return;
                                    }
                                    Globalclass.tempQuestionNo = view.getId();
                                    next();
                                }

                            });
                            if (ll != null)
                                ll.addView(button);
                            if (i == Globalclass.total_no_of_quest_val - 1 && Globalclass.total_no_of_quest_val % 2 == 1) {
                                RadioButton button1 = new RadioButton(TestQuestionDisplayActivity.this);
                                button1.setLayoutParams(params);
                                button1.setEnabled(false);
                                button1.setVisibility(View.INVISIBLE);
                                ll.addView(button1);

                            }
//
                        }
                        int questionid = Globalclass.guestioncount;
                        int questiontoset = questionid + 1;
                        tvQustionnumber.setText(String.valueOf(" " + questiontoset + " "));
                        if (runningbookmark) {
                            btnPriviousquestion.setVisibility(View.GONE);
                        } else {
                            btnPriviousquestion.setVisibility(View.VISIBLE);
                        }
                        if (Globalclass.guestioncount == total_no_of_quest - 1) {

                            btnNextquestion.setVisibility(View.GONE);
                        } else {
                            if (!isbookmarkReviewed) {
                                btnNextquestion.setVisibility(View.VISIBLE);
                            }
                        }


                        try {
                            int mval = 0;
                            JSONObject array = (new JSONObject(json)).getJSONObject("data");
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
                            String questionval = jsonobj_2_answer.getString("question");
                            tvQuestionName.setText(questionval);
                            if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                                testquestionid.setText(jsonobj_2_answer.getString("id").toString());
                            } else {
                                testquestionid.setText(jsonobj_2_answer.getString("parent_id").toString());
                            }
                            testquestionid.setVisibility(View.INVISIBLE);
                            String strimage = null;

                            if (Globalclass.languagecode.equalsIgnoreCase("en")) {

                                strimage = jsonobj_2_answer.getString("ImageHash").toString();
                            } else {
                                jsonobj_2_otytherlanguageimage = (JSONObject) otherlanguagejson.get(Globalclass.guestioncount);
                                strimage = jsonobj_2_otytherlanguageimage.get("ImageHash").toString();
                            }

                            if (strimage.equalsIgnoreCase("")) {
                                imgvalues.setVisibility(View.GONE);
                            } else {
                                imgvalues.setVisibility(View.GONE);
                                //Util.loadImage(strimage, imgvalues);
                            }


                            if (videoView != null && videoView.getVisibility() == View.VISIBLE) {
                                if (videoView.isPlaying()) {
                                    videoView.stopPlayback();
                                    videoView.refreshDrawableState();
                                    videoView.suspend();
                                    videoView.closePlayer();
                                }
                            }
                            if (audioView != null && audioView.getVisibility() == View.VISIBLE) {
                                if (audioView.isPlaying()) {
                                    audioView.stopPlayback();
                                    audioView.refreshDrawableState();
                                    audioView.suspend();
                                    audioView.closePlayer();
                                }
                            }
                            ivQuestionImage.setVisibility(View.GONE);
                            videoLayout.setVisibility(View.GONE);
                            audioLayout.setVisibility(View.GONE);
                            questiontype = jsonobj_2_answer.getString("question_type").toString();
                            mediaType = jsonobj_2_answer.getString("media_type").toString();
                            media = jsonobj_2_answer.getString("media").toString();

                            String mediaTyepeArray[] = mediaType.split(",");
                            String mediaArray[] = media.split(",");
                            if (activeDetails.equals("1")) {
                                imageCount = 0;

                                if (imageArray != null) {
                                    imageArray.clear();
                                }
                                for (int i = 0; i < mediaTyepeArray.length; i++) {
                                    if (mediaTyepeArray[i] != null && mediaTyepeArray[i].trim().equals("image")) {
                                        imageCount++;
                                        imageArray.add(mediaArray[i]);
                                    }
                                }
                            }
                            int index = 0;
                            if (imageCount > 1) {
                                index = Util.getRandomValue(imageCount - 1);
                            }
                            int j = 0;
                            while (j < mediaTyepeArray.length) {

                                if (mediaTyepeArray[j] != null && mediaTyepeArray[j].trim().equals("image")) {
                                    ivQuestionImage.setVisibility(View.VISIBLE);
                                    if (imageCount > 1) {
                                        Util.setImage(ivQuestionImage, TestQuestionDisplayActivity.this, imageArray.get(index), testList.getId());
                                    } else {
                                        Util.setImage(ivQuestionImage, TestQuestionDisplayActivity.this, mediaArray[j], testList.getId());
                                    }
                                } else if (mediaTyepeArray[j] != null && mediaTyepeArray[j].trim().equalsIgnoreCase("video")) {
                                    videoLayout.setVisibility(View.VISIBLE);
                                    videoView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, mediaArray[j], testList.getId()));
                                    videoView.start();
                                    videoView.requestFocus();
                                } else if (mediaTyepeArray[j] != null && mediaTyepeArray[j].trim().equalsIgnoreCase("audio")) {
                                    audioLayout.setVisibility(View.VISIBLE);
                                    audioView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, mediaArray[j], testList.getId()));
                                    audioView.start();
                                    audioView.requestFocus();
                                } else {
                                    ivQuestionImage.setVisibility(View.GONE);
                                    videoLayout.setVisibility(View.GONE);
                                    audioLayout.setVisibility(View.GONE);
                                }
                                j++;
                            }
                            Cursor questiongiven = databaseHelper.getAllquestiondetilsval(testList.getId(), Globalclass.userids, testquestionid.getText().toString());
                            if (questiongiven.getCount() > 0) {
                                StringBuffer buffer = new StringBuffer();
                                questiongiven.moveToNext();
                                answeroptionval = questiongiven.getString(2);//answer_id
                                isbookmark = questiongiven.getString(5); // bootest_detailskmark count
                                currentcount = questiongiven.getString(6);
                                bookmarkcount = questiongiven.getString(5);// bookmark count
                                String rangemark = "";
                                rangemark = questiongiven.getString(12);
                                int i = 0;
                                for (String key : answeroptionval.split(",")) {
                                    hashMap.put(key, rangemark.split(",")[i]);
                                    i++;
                                }

                                if (activeDetails.equalsIgnoreCase("1")) {
                                    answeroptionnoval = questiongiven.getString(15); //optionnoval for practical No
                                }
                                if (isbookmark == null || isbookmark.equalsIgnoreCase("")) {
                                    isbookmark = "0";
                                }
                                if (currentcount == null || currentcount.equalsIgnoreCase("")) {
                                    currentcount = "1";
                                }
                                if (bookmarkcount == null || bookmarkcount.equalsIgnoreCase("")) {
                                    bookmarkcount = "0";
                                }
                                currentcount = String.valueOf(Integer.valueOf(currentcount) + 1);
                                bookmarkcount = String.valueOf(Integer.valueOf(bookmarkcount) + 1);
                            }

                            JSONArray Questionanswer = jsonobj_2_answer.getJSONArray("answers");
                            String ids;

                            if (activeDetails.equalsIgnoreCase("0") && questiontype.equals("4")) {
                                editTextType4 = new EditText(TestQuestionDisplayActivity.this);
                                //   editText.setId(Integer.parseInt(ids));
                                editTextType4.setText(answeroptionval);
                                editTextType4.setHint("Please enter");
                                editTextType4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                if (LLOptionvaluesView != null) {
                                    LLOptionvaluesView.addView(editTextType4);
                                }
                            }
                            for (int jj = 0; jj < Questionanswer.length(); jj++) {
                                JSONObject jsonobj_2_answerval = (JSONObject) Questionanswer.get(jj);
                                String answer = jsonobj_2_answerval.getString("answer").toString();
                                answer = answer.replaceAll("\n", "");
                                if (Globalclass.languagecode.equalsIgnoreCase("en")) {
                                    ids = jsonobj_2_answerval.getString("id").toString();
                                } else {
                                    ids = jsonobj_2_answerval.getString("parent_id").toString();
                                    if (ids == null || ids.equalsIgnoreCase("")) {
                                        ids = jsonobj_2_answerval.getString("id").toString();
                                    }
                                }
                                final String hash = jsonobj_2_answerval.getString("hash");
                                try {
                                    answerMedia = jsonobj_2_answerval.getString("media");
                                    answerMediaType = jsonobj_2_answerval.getString("media_type");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (activeDetails.equalsIgnoreCase("0")) {
                                    if (questiontype.equalsIgnoreCase("1") || questiontype.equalsIgnoreCase("")
                                            || questiontype.equalsIgnoreCase("0")) {
                                        CheckBox checkBox = new CheckBox(TestQuestionDisplayActivity.this);
                                        checkBox.setId(Integer.parseInt(ids));
                                        checkBox.setText(answer);
                                        checkBox.setTextColor(Color.BLACK);
                                        String strquestion[] = answeroptionval.split(",");
                                        for (int i = 0; i < strquestion.length; i++) {
                                            if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                            {
                                                checkBox.setChecked(true);

                                            }
                                        }
                                        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    String idval = String.valueOf(buttonView.getId());
                                                    answeroptionval = idval + "," + answeroptionval;
                                                } else {
                                                    String id = String.valueOf(buttonView.getId());
                                                    String str[] = answeroptionval.split(id + ",");
                                                    if (str.length == 0) {
                                                        answeroptionval = "";
                                                    } else {
                                                        for (int i = 0; i <= str.length - 1; i++) {
                                                            answeroptionval = str[i];
                                                        }
                                                    }
                                                }
                                                insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                            }
                                        });
                                        if (LLOptionvaluesView != null) {
                                            LLOptionvaluesView.addView(checkBox);
                                        }
                                        setAnswerMedia(answerMediaType, answerMedia, LLOptionvaluesView, ids);
/*
                                        if(!answerMediaType.equals("")){
                                            String mt[]=answerMediaType.split(",");
                                            String m[]=answerMediaType.split(",");
                                            for(int i=0;i<mt.length;i++){

                                                if(mt[i] != null && mt[i].trim().equals("image")){
                                                    ImageView imageView = new ImageView(TestQuestionDisplayActivity.this);
                                                    imageView.setId(Integer.parseInt(ids));
                                                    Util.setImage(imageView, TestQuestionDisplayActivity.this, m[i], testList.getId());
                                                    if (LLOptionvaluesView != null) {
                                                        LLOptionvaluesView.addView(imageView);
                                                    }
                                                }
                                                else if(mt[i] != null && mt[i].trim().equals("video")){
                                                    final VideoView videoView = new VideoView(TestQuestionDisplayActivity.this);
                                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                            */
                                        /*width*//*
 ViewGroup.LayoutParams.MATCH_PARENT,
                                                            */
                                        /*height*//*
 200
                                                    );
                                                    layoutParams.setMargins(10, 10, 10, 10);
                                                    videoView.setLayoutParams(layoutParams);

                                                    videoView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, m[i], testList.getId()));
                                                    if (LLOptionvaluesView != null) {
                                                        LLOptionvaluesView.addView(videoView);
                                                    }
                                                    videoView.start();

                                                }
                                              else   if(mt[i] != null && mt[i].trim().equals("audio")){
                                                    final VideoView videoView = new VideoView(TestQuestionDisplayActivity.this);
                                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                            */
                                        /*width*//*
 ViewGroup.LayoutParams.MATCH_PARENT,
                                                            */
                                        /*height*//*
 40
                                                    );
                                                    layoutParams.setMargins(10, 10, 10, 10);
                                                    videoView.setLayoutParams(layoutParams);

                                                    videoView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, m[i], testList.getId()));
                                                    if (LLOptionvaluesView != null) {
                                                        LLOptionvaluesView.addView(videoView);
                                                    }
                                                    videoView.start();
                                                }
                                            }
                                        }
*/
                                        // Util.loadImage(hash, imageView);

                                       /* if (LLOptionvaluesView != null) {
                                            LLOptionvaluesView.addView(checkBox);
                                            LLOptionvaluesView.addView(imageView);
                                        }*/
                                    } else if (questiontype.equalsIgnoreCase("2")) {
                                        final RadioButton radioButton = new RadioButton(TestQuestionDisplayActivity.this);

                                        radioButton.setId(Integer.parseInt(ids));
                                        radioButton.setText(answer);
                                        radioButton.setTextColor(Color.BLACK);
                                        String strquestion[] = answeroptionval.split(",");
                                        for (int i = 0; i < strquestion.length; i++) {
                                            if (ids.equalsIgnoreCase(strquestion[i])) {
                                                radioButton.setChecked(true);
                                                radioButtonO = radioButton;
                                            }
                                        }
                                        radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    if (radioButtonO != null) {
                                                        radioButtonO.setChecked(false);
                                                        radioButtonO = radioButton;
                                                    } else {
                                                        radioButtonO = radioButton;

                                                    }

                                                    String idval = String.valueOf(buttonView.getId());
                                                    answeroptionval = idval + ",";
                                                    insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                                }
                                            }
                                        });
                                      /*  ImageView imageView = new ImageView(TestQuestionDisplayActivity.this);
                                        imageView.setId(Integer.parseInt(ids));
                                        Util.loadImage(hash, imageView);*/
                                        if (llQuestion2View != null) {
                                            llQuestion2View.addView(radioButton);
                                            //llQuestion2View.addView(imageView);
                                        }
                                        setAnswerMedia(answerMediaType, answerMedia, llQuestion2View, ids);

                                    } else if (questiontype.equalsIgnoreCase("3")) {

                                        RadioButton radioButton = new RadioButton(TestQuestionDisplayActivity.this);
                                        radioButton.setId(Integer.parseInt(ids));
                                        radioButton.setText(answer);
                                        radioButton.setTextColor(Color.BLACK);
                                        String strquestion[] = answeroptionval.split(",");
                                        for (int i = 0; i < strquestion.length; i++) {
                                            if (ids.equalsIgnoreCase(strquestion[i]))  //answeroptionval = str[i];
                                            {
                                                radioButton.setChecked(true);

                                            }
                                        }
                                        radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {
                                                    String idval = String.valueOf(buttonView.getId());
                                                    answeroptionval = idval + ",";
                                                    insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                                }
                                            }
                                        });
                                       /* ImageView imageView = new ImageView(TestQuestionDisplayActivity.this);
                                        imageView.setId(Integer.parseInt(ids));
                                        Util.loadImage(hash, imageView);*/
                                        if (llQuestion2View != null) {
                                            llQuestion2View.addView(radioButton);

                                        }
                                        setAnswerMedia(answerMediaType, answerMedia, llQuestion2View, ids);

                                    }

                                } else {
                                    rangeFlag = getScheduleSettings().getString("range_marking").toString();
                                    if ("3".equalsIgnoreCase(rangeFlag)) {
                                        final TextView textView = new TextView(TestQuestionDisplayActivity.this);
                                        textView.setId(Integer.parseInt(ids));
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        textView.setText(answer);
                                        textView.setTextColor(Color.BLACK);
                                        final TextView textView1 = new TextView(TestQuestionDisplayActivity.this);
                                        textView1.setText(ids);
                                        textView1.setVisibility(View.GONE);

                                        final RadioButton radioButton = new RadioButton(TestQuestionDisplayActivity.this);
                                        radioButton.setText("Excellent ");
                                        radioButton.setId(Integer.parseInt("1"));
                                        radioButton.setTextColor(Color.BLACK);

                                        final RadioButton radioButton1 = new RadioButton(TestQuestionDisplayActivity.this);
                                        radioButton1.setText("Good");
                                        radioButton1.setId(Integer.parseInt("2"));
                                        radioButton1.setTextColor(Color.BLACK);

                                        final RadioButton radioButton2 = new RadioButton(TestQuestionDisplayActivity.this);
                                        radioButton2.setText("Satisfactory");
                                        radioButton2.setId(Integer.parseInt("3"));
                                        radioButton2.setTextColor(Color.BLACK);


                                        final RadioButton radioButton3 = new RadioButton(TestQuestionDisplayActivity.this);
                                        radioButton3.setText("Poor");
                                        radioButton3.setId(Integer.parseInt("4"));
                                        radioButton3.setTextColor(Color.BLACK);

                                        if (hashMap.get(ids) != null) {
                                            if (hashMap.get(ids).equals("100")) {
                                                radioButton.setChecked(true);
                                            } else if (hashMap.get(ids).equals("75")) {
                                                radioButton1.setChecked(true);
                                            } else if (hashMap.get(ids).equals("50")) {
                                                radioButton2.setChecked(true);
                                            } else if (hashMap.get(ids).equals("25")) {
                                                radioButton3.setChecked(true);
                                            }
                                        }

                                        RadioGroup radioGroup = new RadioGroup(TestQuestionDisplayActivity.this);
                                        radioGroup.setOrientation(RadioGroup.VERTICAL);
                                        radioGroup.setTag(jj);
                                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                                                String idval = String.valueOf(textView1.getText().toString());

                                                if (!answeroptionval.contains(idval))
                                                    answeroptionval = idval + "," + answeroptionval;
                                                String val = "";
                                                if (i == 1) {
                                                    val = "100";
                                                } else if (i == 2) {
                                                    val = "75";
                                                } else if (i == 3) {
                                                    val = "50";
                                                } else if (i == 4) {
                                                    val = "25";
                                                }

                                                hashMap.put(idval, val);

                                                insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                            }
                                        });

                                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT

                                        );


                                        if (LLOptionvaluesView != null) {
                                            LLOptionvaluesView.addView(textView);
                                            LLOptionvaluesView.addView(textView1);

                                            //     LLOptionvaluesView.addView(imageView);
                                            setAnswerMedia(answerMediaType, answerMedia, LLOptionvaluesView, ids);

                                            LLOptionvaluesView.addView(radioGroup, p);

                                        }

                                        radioGroup.addView(radioButton, p);
                                        radioGroup.addView(radioButton1, p);
                                        radioGroup.addView(radioButton2, p);
                                        radioGroup.addView(radioButton3, p);

                                    } else {
                                        final TextView textView = new TextView(TestQuestionDisplayActivity.this);
                                        textView.setId(Integer.parseInt(ids));
                                        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        textView.setText(answer);
                                        textView.setTextColor(Color.BLACK);
                                        final TextView textView1 = new TextView(TestQuestionDisplayActivity.this);
                                        textView1.setText(ids);
                                        textView1.setVisibility(View.GONE);
                                        final RadioButton radioButton = new RadioButton(TestQuestionDisplayActivity.this);
                                        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                            radioButton.setText(R.string.yeshnval);
                                        } else {
                                            radioButton.setText("Yes");
                                        }
                                        radioButton.setId(Integer.parseInt("1"));
                                        radioButton.setTextColor(Color.BLACK);
                                        final RadioButton radioButton1 = new RadioButton(TestQuestionDisplayActivity.this);
                                        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                            radioButton1.setText(R.string.nohnval);
                                        } else {
                                            radioButton1.setText("no");
                                        }
                                        radioButton1.setId(Integer.parseInt("2"));
                                        radioButton1.setTextColor(Color.BLACK);
                                        String strquestion[] = answeroptionval.split(",");
                                        for (int i = 0; i < strquestion.length; i++) {
                                            if (ids.equalsIgnoreCase(strquestion[i])) {
                                                radioButton.setChecked(true);
                                                radioButton1.setChecked(false);
                                            }
                                        }

                                        String strnooption[] = answeroptionnoval.split(",");
                                        for (int k = 0; k < strnooption.length; k++) {
                                            if (ids.equalsIgnoreCase(strnooption[k]))  //answeroptionval = str[i];
                                            {
                                                radioButton.setChecked(false);
                                                radioButton1.setChecked(true);
                                            }
                                        }
                                        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked == true) {
                                                    String id = String.valueOf(textView1.getText().toString());
                                                    answeroptionnoval = id + "," + answeroptionnoval;
                                                    radioButton.setChecked(false);
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
                                                insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                            }
                                        });
                                        radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked == true) {
                                                    radioButton1.setChecked(false);
                                                    String idval = String.valueOf(textView1.getText().toString());
                                                    answeroptionval = idval + "," + answeroptionval;
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
                                                }
                                                insertLogHistory(testquestionid.getText().toString(), C.Event_Answer_Change, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                                            }
                                        });
                                  /*  ImageView imageView = new ImageView(TestQuestionDisplayActivity.this);
                                    imageView.setId(Integer.parseInt(ids));
                                    Util.loadImage(hash, imageView);*/
                                        RadioGroup radioGroup = new RadioGroup(TestQuestionDisplayActivity.this);
                                        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

                                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT

                                        );

                                        if (LLOptionvaluesView != null) {
                                            LLOptionvaluesView.addView(textView);
                                            LLOptionvaluesView.addView(textView1);

                                            //     LLOptionvaluesView.addView(imageView);
                                            setAnswerMedia(answerMediaType, answerMedia, LLOptionvaluesView, ids);

                                            LLOptionvaluesView.addView(radioGroup, p);

                                        }

                                        radioGroup.addView(radioButton, p);
                                        radioGroup.addView(radioButton1, p);

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if ((databaseHelper.getTotalAnsweredGiven(Globalclass.userids, sId, activeDetails) == total_no_of_quest - 1) && (answeroptionval.equals("") || answeroptionval.equals("0")) && !isbookmarkReviewed) {
                            btnSubmitTest.setVisibility(View.VISIBLE);
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btnSubmitTest.setText(R.string.SUBMIThn);
                            } else {
                                btnSubmitTest.setText("Submit");

                            }

                        } else if ((databaseHelper.getTotalAnsweredGiven(Globalclass.userids, sId, activeDetails) == total_no_of_quest) && !isbookmarkReviewed) {
                            btnSubmitTest.setVisibility(View.VISIBLE);
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btnSubmitTest.setText(R.string.SUBMIThn);
                            } else {
                                btnSubmitTest.setText("Submit");
                            }
                        } else if (submitWithoutAttemptAll.equals(C.YES)) {
                            btnSubmitTest.setVisibility(View.VISIBLE);
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btnSubmitTest.setText(R.string.SUBMIThn);
                            } else {
                                btnSubmitTest.setText("Submit");
                            }
                        } else {

                            isSubmit = false;
                            btnSubmitTest.setVisibility(View.GONE);
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btnNextquestion.setText(R.string.Nexthn);
                            } else {
                                btnNextquestion.setText("Next");
                            }
                        }

                        insertLogHistory(testquestionid.getText().toString(), C.Event_Question_Visit, Util.getCurrentDateTime(), testList.getId(), activeDetails, Globalclass.idcandidate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timer == null && fixedTime.equals(C.YES) && activeDetails.equals("0")) {
            startTimer();
        }
    }

    void showDialog() {
        progressDialog = Util.getProgressDialog(this, R.string.loading);
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initRecorder(Surface surface) throws Exception {
        if (isVideoRecord.equals("1")) {
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
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
                //       mMediaRecorder.setOutputFormat(8);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
                mMediaRecorder.setVideoFrameRate(30);
                //   mMediaRecorder.setOrientationHint(180);
                mMediaRecorder.setVideoSize(640, 480);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                mMediaRecorder.setOutputFile(VIDEO_PATH_NAME);
                // getOutputMediaFile(MEDIA_TYPE_VIDEO);
                try {
                    mMediaRecorder.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {

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
            if (isVideoRecord.equals("1")) {
                if (mCamera != null) {
                    countDownTimer.cancel();
                    mMediaRecorder.reset();
                    mMediaRecorder.release();
                    mCamera.release();
                    mMediaRecorder = null;
                    mCamera = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupAlarmManagerstop() {
        try {
            if (randomePic.equals("1")) {
                PendingIntent pi = PendingIntent.getService(this, 101, new Intent(this, CemeraService.class),
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showBeforeSubmit(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //   builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isSubmit = true;
                next();
            }
        });
        builder.setMessage(Message);
        builder.show();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //   builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
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

    boolean getIsAnyBookMarkQuestionAvailable() {
        int count = 0;
        Cursor cursor = databaseHelper.getbookmarkquestionvalues(Globalclass.userids, sId, "1", activeDetails);
        if (cursor != null) {
            count = lastquestiontoview = cursor.getCount();
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    public void getbookmarkquestionvalues() {
        try {
            // linearLayout.removeAllViews();
            //  isbookmark= "0";
            isbookmarkReviewed = true;
            currentbookmark = "1";
            bookmarkcount = "0";
            currentcount = "2";
            hourRadioGroup1.removeAllViews();
            llQuestion2View.removeAllViews();
            answeroptionval = "";
            runningbookmark = true;
            btnNextquestion.setVisibility(View.GONE);
            btnBookmarkquestion.setVisibility(View.GONE);
            btnPriviousquestion.setVisibility(View.GONE);
            btnReviewBookmark.setVisibility(View.VISIBLE);
            Cursor cursor = databaseHelper.getbookmarkquestionvalues(Globalclass.userids, sId, "1", activeDetails);
            lastquestiontoview = cursor.getCount();
            for (int i = 0; i < cursor.getCount(); i++) {
                isSubmit = false;
                btnSubmitTest.setVisibility(View.GONE);
                btnNextquestion.setVisibility(View.GONE);
                cursor.moveToNext();
                bookmarkreview.add(i, cursor.getString(8));
            }
            if (lastquestiontoview > 0) {
                LLOptionvaluesView.removeView((View) LLOptionvaluesView.getParent());
                hourRadioGroup.removeAllViews();
                llQuestion2View.removeAllViews();
                LLOptionvaluesView.removeAllViews();
                int id = Integer.parseInt(bookmarkreview.get(0));
                Globalclass.tempQuestionNo = id;
                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                hourRadioGroup1.removeAllViews();

                if (bookmarkreviewcountvalue == lastquestiontoview - 1) {
                    if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                        btnReviewBookmark.setText(R.string.SUBMIThn);

                    } else {
                        btnReviewBookmark.setText("Submit");

                    }
                    btnSubmitTest.getLayoutParams().width = 0;
                    btnSubmitTest.getLayoutParams().height = 0;
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPriviousquestion.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 0);
                    btnSubmitTest.setBackground(null);
                }
                new LongOperationgetquestiondetails().execute();
            } else {
                openPhotoCaptureActivity();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savebookmarkdetails() {
        try {
            //  bookmarkreviewcountvalue = bookmarkreviewcountvalue + 1;
            String optionvaluesrange = "";
            String strid = testquestionid.getText().toString();
            String strdate = Util.getCurrentTimeStamp();
            DatabaseHelper db = new DatabaseHelper(TestQuestionDisplayActivity.this);
            int bookmarkflag = 1;
            if ((answeroptionval.equalsIgnoreCase("") ||
                    answeroptionval.equalsIgnoreCase("0")) && answeroptionnoval.equalsIgnoreCase("") ||
                    answeroptionnoval.equalsIgnoreCase("0")) {
                //      if(!isbookmarkReviewed){
                if (!isbookmarkReviewed) {
                    removeViews();
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
                    for (String key : answeroptionval.split(",")) {
                        rangemark = rangemark + hashMap.get(key) + ",";
                    }
                    succ = db.update_record_question_answer_given(testList.getId(), strid, answeroptionval, Globalclass.userids, strdate, isbookmark, currentcount, String.valueOf(Globalclass.guestioncount), sId, bookmarkflag, rangemark, rangeFlag, activeDetails, answeroptionnoval);
                }
                if (succ == true) {
                    String str = btnReviewBookmark.getText().toString();
                    if (str.equalsIgnoreCase("Submit") || str.equalsIgnoreCase(String.valueOf("जमा करें"))) {

                        DatabaseHelper dbvsl = new DatabaseHelper(TestQuestionDisplayActivity.this);
                        Boolean success = dbvsl.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, "0", "0", Globalclass.userids, sId, activeDetails, "0");
                        if (success == true) {
                            boolean successval = dbvsl.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, sId, activeDetails, "0");
                            if (successval == true) {
                                Globalclass.tempQuestionNo = 0;
                                Globalclass.guestioncount = Globalclass.tempQuestionNo;
                                Globalclass.total_no_of_quest_val = 0;
                                currentbookmark = "";
                                isbookmark = "";
                                reviewquestion = "";
                                isbookmark = "0";
                                setupAlarmManagerstop();
                                Globalclass.lastpicturecandidate = "1";

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

                        bookmarkreviewcountvalue = bookmarkreviewcountvalue + 1;
                        if (bookmarkreviewcountvalue == lastquestiontoview - 1) {
                            if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
                                btnReviewBookmark.setText(R.string.SUBMIThn);

                            } else {
                                btnReviewBookmark.setText("Submit");

                            }
                            btnSubmitTest.getLayoutParams().width = 0;
                            btnSubmitTest.getLayoutParams().height = 0;
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPriviousquestion.getLayoutParams();
                            layoutParams.setMargins(0, 0, 0, 0);
                            btnSubmitTest.setBackground(null);
                        }
                        int id = Integer.parseInt(bookmarkreview.get(bookmarkreviewcountvalue));
                        Globalclass.tempQuestionNo = id;
                        Globalclass.guestioncount = Globalclass.tempQuestionNo;
                        LLOptionvaluesView.removeAllViews();
                        LLOptionvaluesView.removeView((View) LLOptionvaluesView.getParent());
                        hourRadioGroup.removeAllViews();
                        llQuestion2View.removeAllViews();
                        hourRadioGroup1.removeAllViews();
                        new LongOperationgetquestiondetails().execute();
                        if (progressDialog != null)
                            progressDialog.show();
                        new Thread() {

                            public void run() {
                                try {

                                    sleep(1000);

                                } catch (Exception e) {

                                    Log.e("tag", e.getMessage());
                                }
                                // dismiss the progress dialog
                                if (progressDialog != null && progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }.start();
                    }
                } else {
                    showMessage("Worning", "Something Went Wrong");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void insertLogHistory(String Questionid, String event, String timestamp, String testid, String activedetails, String userID) {
        databaseHelper.insertLogHistory(Questionid, event, System.currentTimeMillis() + "", testid, activedetails, Globalclass.idcandidate);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.getVisibility() == View.VISIBLE) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Globalclass.spinnerstringlang.equalsIgnoreCase("hn")) {
            spnLanguage.setSelection(spinnerArrayAdapter.getPosition("Hindi"));
        } else {
            spnLanguage.setSelection(spinnerArrayAdapter.getPosition("English"));
        }
        if (videoView != null && videoView.getVisibility() == View.VISIBLE) {
            if (videoView.isPlaying()) {
                videoView.resume();
                videoView.requestFocus();
            }
        }
        if (audioView != null && audioView.getVisibility() == View.VISIBLE) {
            if (audioView.isPlaying()) {
                audioView.resume();
                audioView.requestFocus();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (videoView != null && videoView.getVisibility() == View.VISIBLE) {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
            }
        }
        if (audioView != null && audioView.getVisibility() == View.VISIBLE) {
            if (audioView.isPlaying()) {
                audioView.stopPlayback();
            }
        }
        setupAlarmManagerstop();
    }

    void setAnswerMedia(String amt, String am, LinearLayout LLView, String ids) {
        if (amt != null && !amt.equals("")) {
            String mt[] = amt.split(",");
            String m[] = am.split(",");
            for (int i = 0; i < mt.length; i++) {

                if (mt[i] != null && mt[i].trim().equals("image")) {
                    ImageView imageView = new ImageView(TestQuestionDisplayActivity.this);
                    imageView.setId(Integer.parseInt(ids));
                    Util.setImage(imageView, TestQuestionDisplayActivity.this, m[i], testList.getId());
                    if (LLView != null) {
                        LLView.addView(imageView);
                    }
                } else if (mt[i] != null && mt[i].trim().equals("video")) {
                    final VideoView videoView = new VideoView(TestQuestionDisplayActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            /*width*/ ViewGroup.LayoutParams.MATCH_PARENT,
                            /*height*/ 200
                    );
                    layoutParams.setMargins(10, 10, 10, 10);
                    videoView.setLayoutParams(layoutParams);

                    videoView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, m[i], testList.getId()));
                    if (LLView != null) {
                        LLView.addView(videoView);
                    }
                    videoView.start();

                } else if (mt[i] != null && mt[i].trim().equals("audio")) {
                    final VideoView videoView = new VideoView(TestQuestionDisplayActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            /*width*/ ViewGroup.LayoutParams.MATCH_PARENT,
                            /*height*/ 40
                    );
                    layoutParams.setMargins(10, 10, 10, 10);
                    videoView.setLayoutParams(layoutParams);

                    videoView.setVideoURI(Util.getVideoUrl(TestQuestionDisplayActivity.this, m[i], testList.getId()));
                    if (LLView != null) {
                        LLView.addView(videoView);
                    }
                    videoView.start();
                }
            }
        }

    }

    String getJson() {
        if (Util.ONLINE) {
            json = SharedPreference.getInstance(TestQuestionDisplayActivity.this).getString(C.ONLINE_TEST_LIST);
        } else {
            Cursor resss = databaseHelper.gettest_details_json_string(testList.getId(), sId);
            if (resss.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
                return "";
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        return json;
    }

    @Override
    protected void onDestroy() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();

    }

    JSONObject getScheduleSettings() {

        try {
            JSONObject array = (new JSONObject(json)).getJSONObject("data");
            JSONObject jsonobj_2 = (JSONObject) array;

            String settings = jsonobj_2.getString("schedule_setttings");
            Log.e("DEBUG", "schedule_setttings=" + settings);
            return new JSONObject(settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class savinganswerdetails extends AsyncTask<Void, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pd = new ProgressDialog(TestQuestionDisplayActivity.this);
                pd.setMessage("Please wait...");
                pd.setCancelable(false);
                pd.show();
            } catch (Exception e) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LongOperationgetquestiondetails extends AsyncTask<Void, Void, String> {

        ProgressDialog pro;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                tvQuestionName.setVisibility(View.GONE);
                pro = new ProgressDialog(TestQuestionDisplayActivity.this);
                pro.setMessage("Please wait...");
                pro.setCancelable(false);
                pro.show();
            } catch (Exception e) {
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
                if (Globalclass.guestioncount == 0 && !runningbookmark) {
                    btnPriviousquestion.setEnabled(false);
                    btnPriviousquestion.setVisibility(View.GONE);
                } else if (!runningbookmark) {
                    btnPriviousquestion.setEnabled(true);
                    btnPriviousquestion.setVisibility(View.VISIBLE);
                }
                tvQuestionName.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
