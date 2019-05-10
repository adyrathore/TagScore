package com.jskgmail.indiaskills;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jskgmail.indiaskills.adpater.AdapterPhotoList;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.AnnesureDataSave;
import com.jskgmail.indiaskills.pojo.Photo;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.UploadZipResponse;
import com.jskgmail.indiaskills.util.Api;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.ClientSSLSocketFactory;
import com.jskgmail.indiaskills.util.ProgressRequestBody;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityAnnexureM extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
    Button submit;
    RadioGroup a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22;
    private final int requestCode = 20;
    String strvaluesassessment = "0";
    Button btnTakePicture, btnTakePic15, btnTakePic16, btnTakePic17, btnTakePic20, btnTakePic21, btnTakePic22;
    Button viewclass2, viewtraineefeedbackform15, viewAttendacepic16, viewTraineeEnrolmentform17, viewbrandingpic20, viewbrandingpic21, viewCenterPic22;
    byte[] byteArray;
    String encodedImage;
    int a1click = 0, a2click = 0, a3click = 0, a4click = 0, a5clik = 0, a6clik = 0, a7clik = 0, a8clik = 0, a9clik = 0, a10clik = 0, a11clik = 0, q12clik = 0, a13clik = 0, a14clik = 0, a15clik = 0, a16clik = 0, a17clik = 0, a18clik = 0, a19clik = 0, a20clik = 0, a21clik = 0, a22clik = 0;
    EditText editText1, editText2, editText3, editText5, editText4, editText6, editText7, editText8, editText9, editText10, editText11, editText12, editText13, editText14, editText15, editText16, editText17, editText18, editText19, editText20, editText21, editText22;
    ScrollView scrollView;

    TestList testList;
    private Dialog progressDialog;

    String name;
    RecyclerView rvImages1;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    private RecyclerView rvImages15;
    private RecyclerView rvImages16;
    private RecyclerView rvImages17;
    private RecyclerView rvImages20;
    private RecyclerView rvImages21;
    private RecyclerView rvImages22;
    boolean autoCameraOpen = false;
    boolean isClearCheckCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annexure_m);
        ButterKnife.bind(this);
        name = SharedPreference.getInstance(ActivityAnnexureM.this).getString(C.USERNAME);


        if (!Util.isNetworkAvailable(this)) {
            Util.ONLINE = false;
        }

        rvImages1 = (RecyclerView) findViewById(R.id.rvImages1);
        rvImages15 = (RecyclerView) findViewById(R.id.rvImages15);
        rvImages16 = (RecyclerView) findViewById(R.id.rvImages16);
        rvImages17 = (RecyclerView) findViewById(R.id.rvImages17);
        rvImages20 = (RecyclerView) findViewById(R.id.rvImages20);
        rvImages21 = (RecyclerView) findViewById(R.id.rvImages21);
        rvImages22 = (RecyclerView) findViewById(R.id.rvImages22);

        scrollView = (ScrollView) findViewById(R.id.scrollview);

        editText1 = (EditText) findViewById(R.id.edittext1);
        editText2 = (EditText) findViewById(R.id.edittext2);
        editText3 = (EditText) findViewById(R.id.edittext3);
        editText4 = (EditText) findViewById(R.id.edittext4);
        editText5 = (EditText) findViewById(R.id.edittext5);
        editText6 = (EditText) findViewById(R.id.edittext6);
        editText7 = (EditText) findViewById(R.id.edittext7);
        editText8 = (EditText) findViewById(R.id.edittext8);
        editText9 = (EditText) findViewById(R.id.edittext9);
        editText10 = (EditText) findViewById(R.id.edittext10);
        editText11 = (EditText) findViewById(R.id.edittext11);
        editText12 = (EditText) findViewById(R.id.edittext12);
        editText13 = (EditText) findViewById(R.id.edittext13);
        editText14 = (EditText) findViewById(R.id.edittext14);
        editText15 = (EditText) findViewById(R.id.edittext15);
        editText16 = (EditText) findViewById(R.id.edittext16);
        editText17 = (EditText) findViewById(R.id.edittext17);
        editText18 = (EditText) findViewById(R.id.edittext18);
        editText19 = (EditText) findViewById(R.id.edittext19);
        editText20 = (EditText) findViewById(R.id.edittext20);
        editText21 = (EditText) findViewById(R.id.edittext21);
        editText22 = (EditText) findViewById(R.id.edittext22);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        viewAttendacepic16 = (Button) findViewById(R.id.btn_viewdetails16);
        viewAttendacepic16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra("values", "Attendence sheet");
                intent.putExtra(C.TEST, testList);
                startActivity(intent);
            }
        });
        viewTraineeEnrolmentform17 = (Button) findViewById(R.id.btn_viewdetails17);
        viewTraineeEnrolmentform17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra(C.TEST, testList);
                intent.putExtra("values", "Assessor Feedback");
                startActivity(intent);
            }
        });
        viewbrandingpic20 = (Button) findViewById(R.id.btn_viewdetails20);
        viewbrandingpic20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra(C.TEST, testList);
                intent.putExtra("values", "Branding/Gate picture");
                startActivity(intent);
            }
        });
        viewbrandingpic21 = (Button) findViewById(R.id.btn_viewdetails21);
        viewbrandingpic21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra(C.TEST, testList);
                intent.putExtra("values", "Branding/Gate picture");
                startActivity(intent);
            }
        });

        viewCenterPic22 = (Button) findViewById(R.id.btn_viewdetails22);
        viewCenterPic22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra(C.TEST, testList);
                intent.putExtra("values", "Group Picture");
                startActivity(intent);
            }
        });

        btnTakePic16 = (Button) findViewById(R.id.btn_takepic16);
        btnTakePic16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText16.requestFocus();
                strvaluesassessment = "16";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        btnTakePic17 = (Button) findViewById(R.id.btn_takepic17);
        btnTakePic17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText17.requestFocus();
                strvaluesassessment = "17";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        btnTakePic20 = (Button) findViewById(R.id.btn_takepic20);
        btnTakePic20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText20.requestFocus();
                strvaluesassessment = "20";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        btnTakePic21 = (Button) findViewById(R.id.btn_takepic21);
        btnTakePic21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strvaluesassessment = "21";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        btnTakePic22 = (Button) findViewById(R.id.btn_takepic22);
        btnTakePic22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText22.requestFocus();
                strvaluesassessment = "22";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });

        btnTakePic15 = (Button) findViewById(R.id.btn_takepic15);
        viewtraineefeedbackform15 = (Button) findViewById(R.id.btn_viewdetails15);
        btnTakePic15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText15.requestFocus();
                strvaluesassessment = "15";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        viewtraineefeedbackform15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra("values", "Training Center Feedback");
                intent.putExtra(C.TEST, testList);
                startActivity(intent);
            }
        });
        btnTakePicture = (Button) findViewById(R.id.btn_takepic);
        viewclass2 = (Button) findViewById(R.id.btn_viewdetails);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.requestFocus();
                strvaluesassessment = "2";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        viewclass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAnnexureM.this, ActivityFeedback.class);
                intent.putExtra("values", "Assessment Picture");
                intent.putExtra(C.TEST, testList);
                startActivity(intent);
            }
        });

        a1 = (RadioGroup) findViewById(R.id.rdg_question1);
        a2 = (RadioGroup) findViewById(R.id.rdg_question2);
        a3 = (RadioGroup) findViewById(R.id.rdg_question3);
        a4 = (RadioGroup) findViewById(R.id.rdg_question4);
        a5 = (RadioGroup) findViewById(R.id.rgd_question5);
        a6 = (RadioGroup) findViewById(R.id.rgd_question6);
        a7 = (RadioGroup) findViewById(R.id.rgd_question7);
        a8 = (RadioGroup) findViewById(R.id.rgd_question8);
        a9 = (RadioGroup) findViewById(R.id.rgd_question9);
        a10 = (RadioGroup) findViewById(R.id.rgd_question10);
        a11 = (RadioGroup) findViewById(R.id.rdg_question11);
        a12 = (RadioGroup) findViewById(R.id.rdg_question12);
        a13 = (RadioGroup) findViewById(R.id.rdg_question13);
        a14 = (RadioGroup) findViewById(R.id.rdg_question14);
        a15 = (RadioGroup) findViewById(R.id.rdg_question15);
        a16 = (RadioGroup) findViewById(R.id.rdg_question16);
        a17 = (RadioGroup) findViewById(R.id.rdg_question17);
        a18 = (RadioGroup) findViewById(R.id.rdg_question18);
        a19 = (RadioGroup) findViewById(R.id.rdg_question19);
        a20 = (RadioGroup) findViewById(R.id.rdg_question20);
        a21 = (RadioGroup) findViewById(R.id.rdg_question21);
        a22 = (RadioGroup) findViewById(R.id.rdg_question22);
        submit = (Button) findViewById(R.id.btn_savefeedback);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    savefeedbackdetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        a1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a1click = 1;
            }
        });
        a2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a2.getCheckedRadioButtonId();
                strvaluesassessment = "2";
                a2click = 1;
                if (!autoCameraOpen)
                    return;

                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePicture.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText2.requestFocus();
                    } else {

                        btnTakePicture.setVisibility(View.GONE);
                    }
                }
            }
        });

        a3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a3click = 1;
            }
        });

        a4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a4click = 1;
            }
        });

        a5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a5clik = 1;
            }
        });
        a6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a6clik = 1;
            }
        });
        a7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a7clik = 1;
            }
        });
        a8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a8clik = 1;
            }
        });
        a9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a9clik = 1;
            }
        });
        a10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a10clik = 1;
            }
        });
        a11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a11clik = 1;
            }
        });
        a12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                q12clik = 1;
            }
        });
        a13.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a13clik = 1;
            }
        });
        a14.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a14clik = 1;
            }
        });


        a15.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a15.getCheckedRadioButtonId();
                strvaluesassessment = "15";
                a15clik = 1;
                if (!autoCameraOpen)
                    return;

                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic15.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText15.requestFocus();

                    } else {
                        btnTakePic15.setVisibility(View.GONE);
                    }
                }
            }
        });

        a16.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a16.getCheckedRadioButtonId();
                strvaluesassessment = "16";
                a16clik = 1;
                if (!autoCameraOpen)
                    return;

                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic16.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText16.requestFocus();

                    } else {
                        btnTakePic16.setVisibility(View.GONE);
                    }
                }
            }
        });

        a17.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a17.getCheckedRadioButtonId();
                strvaluesassessment = "17";
                a17clik = 1;
                if (!autoCameraOpen)
                    return;
                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic17.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText17.requestFocus();

                    } else {
                        btnTakePic17.setVisibility(View.GONE);
                    }
                }
            }
        });

        a18.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a18clik = 1;
            }
        });
        a19.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                a19clik = 1;
            }
        });

        a20.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a20.getCheckedRadioButtonId();
                strvaluesassessment = "20";
                a20clik = 1;
                if (!autoCameraOpen)
                    return;
                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic20.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText20.requestFocus();

                    } else {
                        btnTakePic20.setVisibility(View.GONE);
                    }
                }
            }
        });

        a21.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a21.getCheckedRadioButtonId();
                strvaluesassessment = "21";
                a21clik = 1;
                if (!autoCameraOpen)
                    return;
                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic21.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText21.requestFocus();

                    } else {
                        btnTakePic21.setVisibility(View.GONE);
                    }
                }
            }
        });
        a22.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isClearCheckCalled) {
                    isClearCheckCalled = false;
                    return;
                }
                int str = a22.getCheckedRadioButtonId();
                strvaluesassessment = "22";
                a22clik = 1;
                if (!autoCameraOpen)
                    return;
                RadioButton radioButton = (RadioButton) findViewById(str);
                if (radioButton != null) {
                    if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {
                        btnTakePic22.setVisibility(View.VISIBLE);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, requestCode);
                        editText22.requestFocus();

                    } else {
                        btnTakePic22.setVisibility(View.GONE);
                    }
                }
            }
        });

        if (SharedPreference.getInstance(ActivityAnnexureM.this).getString(C.ANNEXURE_DATA) != null) {
            setAdapter("2", "Assessment Picture");
            setAdapter("15", "Training Center Feedback");
            setAdapter("16", "Attendence sheet");
            setAdapter("17", "Assessor Feedback");
            setAdapter("20", "Branding/Gate picture");
            setAdapter("21", "Branding/Gate picture");
            setAdapter("22", "Group Picture");
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                submit.setEnabled(b);
            }
        });
        submit.setEnabled(false);
    }

    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
    }


    public ArrayList<Photo> getPhotos(String str) {

        File pictureFileDir = getDir();

        ArrayList<Photo> menuItems = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllVauesofimages(str, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        while (cursor.moveToNext()) {
            String type = cursor.getString(0);
            String nameval = cursor.getString(2);
            String urlval = cursor.getString(4);

            if (str.equalsIgnoreCase(type)) {
                Photo photo = new Photo();
                photo.setName(nameval);
                photo.setUrl(urlval);
                menuItems.add(photo);
            }
        }
        Log.e("Photo", menuItems.toString());
        return menuItems;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK) {
                if (strvaluesassessment.equalsIgnoreCase("2")) {
                    Globalclass.evidencecapture = "Assessment Picture";
                } else if (strvaluesassessment.equalsIgnoreCase("15")) {
                    Globalclass.evidencecapture = "Training Center Feedback";
                } else if (strvaluesassessment.equalsIgnoreCase("16")) {
                    Globalclass.evidencecapture = "Attendence sheet";
                } else if (strvaluesassessment.equalsIgnoreCase("17")) {
                    Globalclass.evidencecapture = "Assessor Feedback";
                } else if (strvaluesassessment.equalsIgnoreCase("20")) {
                    Globalclass.evidencecapture = "Branding/Gate picture";
                } else if (strvaluesassessment.equalsIgnoreCase("21")) {
                    Globalclass.evidencecapture = "Branding/Gate picture";
                } else if (strvaluesassessment.equalsIgnoreCase("22")) {
                    Globalclass.evidencecapture = "Group Picture";
                }
                String filepath = null;
                byte[] bytes;
                File pictureFileDir = getDir();
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    if (this.requestCode == requestCode && resultCode == RESULT_OK) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        bytes = stream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                        //    String date = dateFormat.format(new Date());
                        String date = Util.getCurrentTimeStamp();

                        String photoFile = "";
                        if (strvaluesassessment.equalsIgnoreCase("2")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();

                            photoFile = photoFile + "-" + "ClPic-" + date + ".jpg";
                        } else if (strvaluesassessment.equalsIgnoreCase("15")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                            photoFile = photoFile + "-" + "FdPic-" + date + ".jpg";
                        } else if (strvaluesassessment.equalsIgnoreCase("16")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();

                            photoFile = photoFile + "-" + "AtPic-" + date + ".jpg";

                        } else if (strvaluesassessment.equalsIgnoreCase("17")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                            photoFile = photoFile + "-" + "EnPic-" + date + ".jpg";
                        } else if (strvaluesassessment.equalsIgnoreCase("20")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                            photoFile = photoFile + "-" + "BdPicf-" + date + ".jpg";
                        } else if (strvaluesassessment.equalsIgnoreCase("21")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                            photoFile = photoFile + "-" + "BdPicf2-" + date + ".jpg";
                        } else if (strvaluesassessment.equalsIgnoreCase("22")) {
                            photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                            photoFile = photoFile + "-" + "CePic-" + date + ".jpg";
                        }
                        Log.e("DEBUG", "photoFile=" + photoFile);
                        filepath = pictureFileDir.getPath() + File.separator + photoFile;
                        File pictureFile = new File(filepath);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(pictureFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!Util.ONLINE) {
                            DatabaseHelper db = new DatabaseHelper(ActivityAnnexureM.this);
                            boolean success = db.insert_imagesval(Globalclass.evidencecapture, "non", photoFile, testList.getId(), filepath, testList.getUniqueID(), testList.getUniqueID());
                            if (success == true) {
                                Util.showMessage(ActivityAnnexureM.this, "Image saved successfully");

                                setAdapter(strvaluesassessment, Globalclass.evidencecapture);
                            }
                        } else if (Util.ONLINE) {

                            DatabaseHelper db = new DatabaseHelper(ActivityAnnexureM.this);
                            boolean success = db.insert_imagesval(Globalclass.evidencecapture, "non", photoFile, testList.getId(), filepath, testList.getScheduleIdPk(), testList.getScheduleIdPk());
                            String imagetypeval = "", nameimageval = "", targetid = "";

                            if (Globalclass.evidencecapture.equalsIgnoreCase("Attendence sheet")) {
                                imagetypeval = "AttPic";
                                nameimageval = "AttPic-" + photoFile;
                                targetid = testList.getScheduleIdPk();
                            }
                            if (Globalclass.evidencecapture.equalsIgnoreCase("Assessment Picture")) {
                                imagetypeval = "EcPic";
                                nameimageval = "EcPic-" + photoFile;
                                targetid = testList.getScheduleIdPk();
                            }
                            if (Globalclass.evidencecapture.equalsIgnoreCase("Training Center Feedback") || Globalclass.evidencecapture.equalsIgnoreCase("Assessor Feedback") || Globalclass.evidencecapture.equalsIgnoreCase("Candidate Feedback")) {
                                imagetypeval = "FeedPic";
                                nameimageval = "FeedPic-" + photoFile;
                                targetid = testList.getScheduleIdPk();
                            }
                            if (Globalclass.evidencecapture.equalsIgnoreCase("Branding/Gate picture")) {
                                imagetypeval = "EcPic";
                                nameimageval = "EcPic-" + photoFile;
                                targetid = testList.getScheduleIdPk();
                            }
                            if (Globalclass.evidencecapture.equalsIgnoreCase("Group Picture")) {
                                imagetypeval = "EcPic";
                                nameimageval = "EcPic-" + photoFile;
                                targetid = testList.getScheduleIdPk();
                            }
                            uploadImage(filepath, testList.getScheduleIdPk(), nameimageval, targetid, imagetypeval, photoFile);


                        }
                    }
                }
            } else {
                if (strvaluesassessment.equalsIgnoreCase("2")) {

                    if (getPhotos("Assessment Picture").size() == 0) {
                        isClearCheckCalled = true;
                        btnTakePicture.setVisibility(View.GONE);
                        a2.clearCheck();

                        a2click = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("15")) {
                    if (getPhotos("Training Center Feedback").size() == 0) {
                        isClearCheckCalled = true;
                        btnTakePic15.setVisibility(View.GONE);
                        a15.clearCheck();
                        a15clik = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("16")) {
                    if (getPhotos("Attendence sheet").size() == 0) {
                        btnTakePic16.setVisibility(View.GONE);
                        isClearCheckCalled = true;
                        a16.clearCheck();
                        a16clik = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("17")) {
                    if (getPhotos("Assessor Feedback").size() == 0) {
                        isClearCheckCalled = true;
                        btnTakePic17.setVisibility(View.GONE);
                        a17.clearCheck();
                        a17clik = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("20")) {
                    if (getPhotos("Branding/Gate picture").size() == 0) {
                        isClearCheckCalled = true;
                        btnTakePic20.setVisibility(View.GONE);
                        a20.clearCheck();
                        a20clik = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("21")) {
                    if (getPhotos("Branding/Gate picture").size() == 0) {
                        isClearCheckCalled = true;
                        btnTakePic21.setVisibility(View.GONE);
                        a21.clearCheck();
                        a21clik = 0;
                    }
                } else if (strvaluesassessment.equalsIgnoreCase("22")) {
                    if (getPhotos("Group Picture").size() == 0) {
                        btnTakePic22.setVisibility(View.GONE);
                        isClearCheckCalled = true;
                        a22.clearCheck();
                        a22clik = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Util.showMessage(this, R.string.please_capture_image_again);
        }
    }

    public void savefeedbackdetails() {
        setAdapter("2", "Assessment Picture");
        setAdapter("15", "Training Center Feedback");
        setAdapter("16", "Attendence sheet");
        setAdapter("17", "Assessor Feedback");
        setAdapter("20", "Branding/Gate picture");
        setAdapter("21", "Branding/Gate picture");
        setAdapter("22", "Group Picture");
        if (a1click == 0 || a2click == 0 || a3click == 0 || a4click == 0 || a5clik == 0 || a6clik == 0 || a7clik == 0 || a8clik == 0 || a9clik == 0 || a10clik == 0 || a11clik == 0 || q12clik == 0 || a13clik == 0 || a14clik == 0 || a15clik == 0 || a16clik == 0 || a17clik == 0 || a18clik == 0 || a19clik == 0 || a20clik == 0 || a21clik == 0 || a22clik == 0) {
            Util.showMessage(ActivityAnnexureM.this, "Please Answer All Questions.");
        } else if (isYesClick(a2) && getPhotos("Assessment Picture").size() == 0) {
            editText2.requestFocus();
            focusOnView(btnTakePicture);
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a15) && getPhotos("Training Center Feedback").size() == 0) {
            editText15.requestFocus();
            focusOnView(btnTakePic15);
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a16) && getPhotos("Attendence sheet").size() == 0) {
            editText16.requestFocus();
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a17) && getPhotos("Assessor Feedback").size() == 0) {
            editText17.requestFocus();
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a20) && getPhotos("Branding/Gate picture").size() == 0) {
            editText20.requestFocus();
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a21) && getPhotos("Branding/Gate picture").size() == 0) {
            editText21.requestFocus();
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else if (isYesClick(a22) && getPhotos("Group Picture").size() == 0) {
            editText22.requestFocus();
            Util.showMessage(ActivityAnnexureM.this, "Please Take the picture.");

        } else {
            String test1 = "", test2 = "", test3 = "", test4 = "", test5 = "", test6 = "", test7 = "", test8 = "", test9 = "", test10 = "", test11 = "", test12 = "", test13 = "", test14 = "", test15 = "", test16 = "", test17 = "", test18 = "", test19 = "", test20 = "", test21 = "", test22 = "";
            DatabaseHelper db = new DatabaseHelper(ActivityAnnexureM.this);
            test1 = editText1.getText().toString();
            test2 = editText2.getText().toString();
            test3 = editText3.getText().toString();
            test4 = editText4.getText().toString();
            test5 = editText5.getText().toString();
            test6 = editText6.getText().toString();
            test7 = editText7.getText().toString();
            test8 = editText8.getText().toString();
            test9 = editText9.getText().toString();
            test10 = editText10.getText().toString();
            test11 = editText11.getText().toString();
            test12 = editText12.getText().toString();
            test13 = editText13.getText().toString();
            test14 = editText14.getText().toString();
            test15 = editText15.getText().toString();
            test16 = editText16.getText().toString();
            test17 = editText17.getText().toString();
            test18 = editText18.getText().toString();
            test19 = editText19.getText().toString();
            test20 = editText20.getText().toString();
            test21 = editText21.getText().toString();
            test22 = editText22.getText().toString();
            int str = a1.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(str);
            if (radioButton.getText().toString().equalsIgnoreCase("Yes")) {

                boolean s = db.insert_feedbackform("M", name, "1", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test1);

                if (s) {

                }
            } else {
                boolean s = db.insert_feedbackform("M", name, "1", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test1);
                if (s) {

                }
            }

            int str2 = a2.getCheckedRadioButtonId();

            RadioButton radioButton2 = (RadioButton) findViewById(str2);

            if (radioButton2.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "2", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test2);

            } else {
                db.insert_feedbackform("M", name, "2", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test2);

            }
            int str3 = a3.getCheckedRadioButtonId();

            RadioButton radioButton3 = (RadioButton) findViewById(str3);

            if (radioButton3.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "3", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test3);

            } else {
                db.insert_feedbackform("M", name, "3", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test3);

            }
            int str4 = a4.getCheckedRadioButtonId();

            RadioButton radioButton4 = (RadioButton) findViewById(str4);

            if (radioButton4.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "4", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test4);

            } else {
                db.insert_feedbackform("M", name, "4", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test4);
            }
            int str5 = a5.getCheckedRadioButtonId();

            RadioButton radioButton5 = (RadioButton) findViewById(str5);

            if (radioButton5.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "5", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test5);

            } else {
                db.insert_feedbackform("M", name, "5", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test5);

            }
            int str6 = a6.getCheckedRadioButtonId();

            RadioButton radioButton6 = (RadioButton) findViewById(str6);

            if (radioButton6.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "6", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test6);

            } else {
                db.insert_feedbackform("M", name, "6", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test6);

            }
            int str7 = a7.getCheckedRadioButtonId();

            RadioButton radioButton7 = (RadioButton) findViewById(str7);

            if (radioButton7.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "7", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test7);

            } else {
                db.insert_feedbackform("M", name, "7", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test7);

            }
            int str8 = a8.getCheckedRadioButtonId();

            RadioButton radioButton8 = (RadioButton) findViewById(str8);

            if (radioButton8.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "8", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test8);

            } else {
                db.insert_feedbackform("M", name, "8", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test8);

            }
            int str9 = a9.getCheckedRadioButtonId();

            RadioButton radioButton9 = (RadioButton) findViewById(str9);

            if (radioButton9.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "9", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test9);

            } else {
                db.insert_feedbackform("M", name, "9", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test9);

            }
            int str10 = a10.getCheckedRadioButtonId();

            RadioButton radioButton10 = (RadioButton) findViewById(str10);

            if (radioButton10.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "10", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test10);

            } else {
                db.insert_feedbackform("M", name, "10", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test10);

            }
            int str11 = a11.getCheckedRadioButtonId();

            RadioButton radioButton11 = (RadioButton) findViewById(str11);

            if (radioButton11.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "11", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test11);

            } else {
                db.insert_feedbackform("M", name, "11", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test11);

            }
            int str12 = a12.getCheckedRadioButtonId();

            RadioButton radioButton12 = (RadioButton) findViewById(str12);

            if (radioButton12.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "12", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test12);

            } else {
                db.insert_feedbackform("M", name, "12", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test12);

            }
            int str13 = a13.getCheckedRadioButtonId();

            RadioButton radioButton13 = (RadioButton) findViewById(str13);

            if (radioButton13.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "13", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test13);

            } else {
                db.insert_feedbackform("M", name, "13", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test13);

            }
            int str14 = a14.getCheckedRadioButtonId();

            RadioButton radioButton14 = (RadioButton) findViewById(str14);

            if (radioButton14.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "14", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test14);

            } else {
                db.insert_feedbackform("M", name, "14", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test14);

            }
            int str15 = a15.getCheckedRadioButtonId();

            RadioButton radioButton15 = (RadioButton) findViewById(str15);

            if (radioButton15.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "15", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test15);

            } else {
                db.insert_feedbackform("M", name, "15", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test15);

            }
            int str16 = a16.getCheckedRadioButtonId();

            RadioButton radioButton16 = (RadioButton) findViewById(str16);

            if (radioButton16.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "16", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test16);

            } else {
                db.insert_feedbackform("M", name, "16", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test16);

            }
            int str17 = a17.getCheckedRadioButtonId();

            RadioButton radioButton17 = (RadioButton) findViewById(str17);

            if (radioButton17.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "17", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test17);

            } else {
                db.insert_feedbackform("M", name, "17", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test17);

            }
            int str18 = a18.getCheckedRadioButtonId();

            RadioButton radioButton18 = (RadioButton) findViewById(str18);

            if (radioButton18.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "18", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test18);

            } else {
                db.insert_feedbackform("M", name, "18", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test18);

            }
            int str19 = a19.getCheckedRadioButtonId();

            RadioButton radioButton19 = (RadioButton) findViewById(str19);

            if (radioButton19.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "19", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test19);

            } else {
                db.insert_feedbackform("M", name, "19", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test19);

            }
            int str20 = a20.getCheckedRadioButtonId();

            RadioButton radioButton20 = (RadioButton) findViewById(str20);

            if (radioButton20.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "20", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test20);

            } else {
                db.insert_feedbackform("M", name, "20", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test20);

            }
            int str21 = a21.getCheckedRadioButtonId();

            RadioButton radioButton21 = (RadioButton) findViewById(str21);

            if (radioButton21.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "21", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test21);

            } else {
                db.insert_feedbackform("M", name, "21", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test21);
            }
            int str22 = a22.getCheckedRadioButtonId();

            RadioButton radioButton22 = (RadioButton) findViewById(str22);

            if (radioButton22.getText().toString().equalsIgnoreCase("Yes")) {
                db.insert_feedbackform("M", name, "22", "Yes", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test22);

            } else {
                boolean e = db.insert_feedbackform("M", name, "22", "No", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), testList.getId(), test22);
                Log.e("DEBUG", "ee=" + e);
            }

            if (Util.ONLINE) {
                submitfeedback();
            } else {

                finish();
            }
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
        }

        cursor.close();
        return resultSet;
    }


    public void submitfeedback() {
        final ProgressDialog Dialog1 = new ProgressDialog(ActivityAnnexureM.this);
        Dialog1.setMessage("Saving Details please wait ...");
        Dialog1.show();
        final DatabaseHelper myDb = new DatabaseHelper(ActivityAnnexureM.this);
        final Cursor cursor = myDb.getAllfeedbackdatavaluesforonline(testList.getId(), testList.getScheduleIdPk(), name);
        Log.e("DEBUG", "COUNT=" + cursor.getCount());
        if (cursor.getCount() > 0) {
            JSONArray array = cur2Json(cursor);
            JSONObject sd = new JSONObject();
            try {
                sd.put("data", (Object) array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String strsddata = sd.toString();
            Map<String, String> params = new HashMap<>();
            params.put("userId", SharedPreference.getInstance(ActivityAnnexureM.this).getUser(C.LOGIN_USER).getUserID());
            params.put("api_key", SharedPreference.getInstance(ActivityAnnexureM.this).getUser(C.LOGIN_USER).getApiKey());
            params.put("schedule_unique_key", testList.getScheduleIdPk());
            params.put("feedback", strsddata.trim());
            Log.e("params :", params.toString());
            // showMessage("message",params.toString());
            String url = C.API_SUBMIT_FEEDBACK_URL;
            try {
                JSONObject jsonObject = new JSONObject(params.toString());
                Log.e("jsonObject :", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params)
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Dialog1.dismiss();
                    Log.e("Data response", String.valueOf(response.toString()));
                    //  boolean bx = myDb.delete_feedbackbyid(testList.getId(), testList.getScheduleIdPk(), Globalclass.userids);
                    Globalclass.onlineassessorgiven = "1";
                    setResult(RESULT_OK);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VolleyError", "Error response", error);
                    Dialog1.dismiss();
                    //     boolean bx = myDb.delete_feedbackbyid(testList.getId(), testList.getScheduleIdPk(), name);
                    finish();

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
            //   VolleyAppController.getInstance().addToRequestQueue(request, tag_json_obj);

            RetryPolicy policy = new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request).setTag(tag_json_obj);
        }

    }


    public void setAdapter(String strvaluesassessment, String evidenceCaptured) {
        AdapterPhotoList adapterPhotoList = new AdapterPhotoList(ActivityAnnexureM.this, getPhotos(evidenceCaptured));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        if (strvaluesassessment.equalsIgnoreCase("2")) {
            rvImages1.setLayoutManager(linearLayoutManager);
            rvImages1.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("15")) {
            rvImages15.setLayoutManager(linearLayoutManager);
            rvImages15.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("16")) {

            rvImages16.setLayoutManager(linearLayoutManager);
            rvImages16.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("17")) {
            rvImages17.setLayoutManager(linearLayoutManager);
            rvImages17.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("20")) {
            rvImages20.setLayoutManager(linearLayoutManager);
            rvImages20.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("21")) {
            rvImages21.setLayoutManager(linearLayoutManager);
            rvImages21.setAdapter(adapterPhotoList);
        } else if (strvaluesassessment.equalsIgnoreCase("22")) {

            rvImages22.setLayoutManager(linearLayoutManager);
            rvImages22.setAdapter(adapterPhotoList);
        }


    }

    @Override
    public void onBackPressed() {


        AnnesureDataSave dataSave = new AnnesureDataSave();
        dataSave.setText1(editText1.getText().toString().trim());
        dataSave.setText2(editText2.getText().toString().trim());
        dataSave.setText3(editText3.getText().toString().trim());
        dataSave.setText4(editText4.getText().toString().trim());
        dataSave.setText5(editText5.getText().toString().trim());
        dataSave.setText6(editText6.getText().toString().trim());
        dataSave.setText7(editText7.getText().toString().trim());
        dataSave.setText8(editText8.getText().toString().trim());
        dataSave.setText9(editText9.getText().toString().trim());
        dataSave.setText10(editText10.getText().toString().trim());
        dataSave.setText11(editText11.getText().toString().trim());
        dataSave.setText12(editText12.getText().toString().trim());
        dataSave.setText13(editText13.getText().toString().trim());
        dataSave.setText14(editText14.getText().toString().trim());
        dataSave.setText15(editText15.getText().toString().trim());
        dataSave.setText16(editText16.getText().toString().trim());
        dataSave.setText17(editText17.getText().toString().trim());
        dataSave.setText18(editText18.getText().toString().trim());
        dataSave.setText19(editText19.getText().toString().trim());
        dataSave.setText20(editText20.getText().toString().trim());
        dataSave.setText21(editText21.getText().toString().trim());
        dataSave.setText22(editText22.getText().toString().trim());

        dataSave.setB1(a1.getCheckedRadioButtonId());
        dataSave.setB2(a2.getCheckedRadioButtonId());
        dataSave.setB3(a3.getCheckedRadioButtonId());
        dataSave.setB4(a4.getCheckedRadioButtonId());
        dataSave.setB5(a5.getCheckedRadioButtonId());
        dataSave.setB6(a6.getCheckedRadioButtonId());
        dataSave.setB7(a7.getCheckedRadioButtonId());
        dataSave.setB8(a8.getCheckedRadioButtonId());
        dataSave.setB9(a9.getCheckedRadioButtonId());
        dataSave.setB10(a10.getCheckedRadioButtonId());
        dataSave.setB11(a11.getCheckedRadioButtonId());
        dataSave.setB12(a12.getCheckedRadioButtonId());
        dataSave.setB13(a13.getCheckedRadioButtonId());
        dataSave.setB14(a14.getCheckedRadioButtonId());
        dataSave.setB15(a15.getCheckedRadioButtonId());
        dataSave.setB16(a16.getCheckedRadioButtonId());
        dataSave.setB17(a17.getCheckedRadioButtonId());
        dataSave.setB18(a18.getCheckedRadioButtonId());
        dataSave.setB19(a19.getCheckedRadioButtonId());
        dataSave.setB20(a20.getCheckedRadioButtonId());
        dataSave.setB21(a21.getCheckedRadioButtonId());
        dataSave.setB22(a22.getCheckedRadioButtonId());

        Gson gson = new Gson();
        String data = gson.toJson(dataSave);
        SharedPreference.getInstance(ActivityAnnexureM.this).setString(C.ANNEXURE_DATA, data);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreference.getInstance(ActivityAnnexureM.this).getString(C.ANNEXURE_DATA) != null) {
            Gson gson = new Gson();
            AnnesureDataSave data = gson.fromJson(SharedPreference.getInstance(ActivityAnnexureM.this).getString(C.ANNEXURE_DATA), AnnesureDataSave.class);
            editText1.setText(data.getText1());
            editText2.setText(data.getText2());
            editText3.setText(data.getText3());
            editText4.setText(data.getText4());
            editText5.setText(data.getText5());
            editText6.setText(data.getText6());
            editText7.setText(data.getText7());
            editText8.setText(data.getText8());
            editText9.setText(data.getText9());
            editText10.setText(data.getText10());
            editText11.setText(data.getText11());
            editText12.setText(data.getText12());
            editText13.setText(data.getText13());
            editText14.setText(data.getText14());
            editText15.setText(data.getText15());
            editText16.setText(data.getText16());
            editText17.setText(data.getText17());
            editText18.setText(data.getText18());
            editText19.setText(data.getText19());
            editText20.setText(data.getText20());
            editText21.setText(data.getText21());
            editText22.setText(data.getText22());

            check(a1, data.getB1());
            check(a2, data.getB2());
            check(a3, data.getB3());
            check(a4, data.getB4());
            check(a5, data.getB5());
            check(a6, data.getB6());
            check(a7, data.getB7());
            check(a8, data.getB8());
            check(a9, data.getB9());
            check(a10, data.getB10());
            check(a11, data.getB11());
            check(a12, data.getB12());
            check(a13, data.getB13());
            check(a14, data.getB14());
            check(a15, data.getB15());
            check(a16, data.getB16());
            check(a17, data.getB17());
            check(a18, data.getB18());
            check(a19, data.getB19());
            check(a20, data.getB20());
            check(a21, data.getB21());
            check(a22, data.getB22());

            if (data.getB2() != -1)
                btnTakePicture.setVisibility(View.VISIBLE);
            if (data.getB15() != -1)
                btnTakePic15.setVisibility(View.VISIBLE);
            if (data.getB16() != -1)
                btnTakePic16.setVisibility(View.VISIBLE);
            if (data.getB17() != -1)
                btnTakePic17.setVisibility(View.VISIBLE);
            if (data.getB20() != -1)
                btnTakePic20.setVisibility(View.VISIBLE);
            if (data.getB21() != -1)
                btnTakePic21.setVisibility(View.VISIBLE);
            if (data.getB22() != -1)
                btnTakePic22.setVisibility(View.VISIBLE);

        }
        autoCameraOpen = true;
    }

    private void check(RadioGroup a1, int b1) {
        if (b1 != -1)
            a1.check(b1);

    }

    boolean isYesClick(RadioGroup radioGroup) {
        int str2 = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton2 = (RadioButton) findViewById(str2);

        return radioButton2.getText().toString().equalsIgnoreCase("Yes");


    }

    private void focusOnView(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, (int) view.getY());
            }
        });
    }


    private void uploadImage(final String imageurlval, final String tagidval, String nameimageval, String targetid, final String imagetypeval, String nameimageorg) {
        progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
        progressDialog.show();
        Log.e("Send zip", imageurlval);
        File file = new File(imageurlval);
        // Uri fileUri  =  Uri.fromFile(new File(filPath));
        ResponseLogin responseLogin = SharedPreference.getInstance(ActivityAnnexureM.this).getUser(C.LOGIN_USER);

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
                progressDialog.dismiss();

                if (response.body().getResponseCode().equals("200")) {
                    showMessage("", "Image Uploded...");
                    setAdapter(strvaluesassessment, Globalclass.evidencecapture);
                } else {
                    showMessage("", "Unable to upload please try again");
                }
            }

            @Override
            public void onFailure(Call<UploadZipResponse> call, Throwable t) {
                progressDialog.dismiss();
                //   Log.e("DEBUG",t.getMessage());
                showMessage("", "Unable to upload please try again");

            }
        });
        //finally performing the call
    }

    private void uploadBitmap(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        final ResponseLogin responseLogin = SharedPreference.getInstance(ActivityAnnexureM.this).getUser(C.LOGIN_USER);

        final ProgressDialog Dialog1 = new ProgressDialog(ActivityAnnexureM.this);
        Dialog1.setCancelable(false);
        Dialog1.setMessage("Uploding Image Please wait...");
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
                        showMessage("", "Image Uploded...");
                        setAdapter(strvaluesassessment, Globalclass.evidencecapture);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("", "Unable to upload please try again");
                        Dialog1.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("userId", responseLogin.getUserID());
                params.put("api_key", responseLogin.getApiKey());
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
        RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack() {


            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(Util.getHostname());


                return urlConnection;
            }
        });
        requestQueue.add(volleyMultipartRequest);
        //  RequestQueue queue= Volley.newRequestQueue(this);
        // queue.add(volleyMultipartRequest);

    }

    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify(C.BASE_URL, session);
        }
    };

    private static byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
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
}
