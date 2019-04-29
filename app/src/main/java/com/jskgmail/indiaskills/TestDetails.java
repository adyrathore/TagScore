package com.jskgmail.indiaskills;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.test.TestDetail;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDetails extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    int acc = 0, selfi = 0;
    boolean assessorValidationRequired=true,annexureMRequired=true;
    byte[] byteArray;
    String encodedImage;
    private final int requestCode = 20;
    TestList testList;

    @BindView(R.id.testName)
    AppCompatTextView testnamede;
    @BindView(R.id.targetCandidate)
    TextView testtargetcandidate;
    @BindView(R.id.userdetails)
    TextView userdetails;
    @BindView(R.id.targetdescription)
    TextView testdescription;
    @BindView(R.id.btn_assessorfeedback)
    ImageButton btn_assessorfeedback;
    @BindView(R.id.anxvalidation)
    ImageView anxvalidation;
    @BindView(R.id.btn_Imagecapurtevalidate)
    ImageButton btn_Imagecapurtevalidate;
    @BindView(R.id.selfevalidation)
    ImageView selfevalidation;
    @BindView(R.id.btn_nextdetail)
    Button movenext;
    @BindView(R.id.llassessor)
    LinearLayout llassessor;
    @BindView(R.id.llSelfiValidation)
    LinearLayout llSelfiValidation;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DatabaseHelper(this);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        getOutputMediaFile();
        anxvalidation.setVisibility(View.GONE);
        btn_assessorfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(TestDetails.this);
                boolean b = databaseHelper.getassessorfeedback(testList.getId(), testList.getUniqueID());
                if (b == true) {
                    Util.showMessage(TestDetails.this, getString(R.string.already_given));
                } else {
                    Intent intent = new Intent(TestDetails.this, ActivityAnnexureM.class);
                    intent.putExtra(C.TEST, testList);
                    startActivityForResult(intent, 3);
                }
            }
        });

        selfevalidation.setVisibility(View.GONE);

        btn_Imagecapurtevalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_Imagecapurtevalidate.setEnabled(false);
                Intent intent = new Intent(TestDetails.this, ActivityFrontFacingCamera.class);
                intent.putExtra("type", "assessorvalidation");
                intent.putExtra(C.TEST, testList);
                startActivityForResult(intent, 2);
               /* final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selfevalidation.setVisibility(View.VISIBLE);
                    }
                }, 2 * 1000);*/

            }
        });

        userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent io = new Intent(TestDetails.this, ActivityBatchList.class);
                io.putExtra(C.TEST, testList);
                startActivity(io);
            }
        });
        movenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BuildConfig.FLAVOR_app.equals(C.tagscoresWithoutAssessor)) {
                    llassessor.setVisibility(View.GONE);
                    if (selfi == 0) {
                        Util.showMessage(TestDetails.this, "Selfie Validation is pending");
                    } else {
                        gotoTestStartScreen();
                    }
                } else {
//                openPhotoCaptureActivity();

                    if (Util.ONLINE) {
                        if (acc == 0 && annexureMRequired) {
                            showDialog();
                        } else if (selfi == 0 && assessorValidationRequired) {
                            Util.showMessage(TestDetails.this, "Selfie Validation is pending");
                        } else {
                           gotoTestStartScreen();
                        }
                    } else {
                        DatabaseHelper db = new DatabaseHelper(TestDetails.this);
                        boolean b = db.getassessorfeedback(testList.getId(), testList.getUniqueID());
                        if (b) {
                            if (acc == 0 && annexureMRequired) {
                                showDialog();
                            } else if (selfi == 0 && assessorValidationRequired) {
                                Util.showMessage(TestDetails.this, "Selfie Validation is pending");
                            } else {
                               gotoTestStartScreen();
                            }
                        } else {
                              if (acc == 0 && annexureMRequired) {
                                Util.showMessage(TestDetails.this, "Assessor Feedback is pending");
                            }
                            if (selfi == 0 && assessorValidationRequired) {
                                Util.showMessage(TestDetails.this, "Selfie Validation is pending");
                            }
                        }
                    }
                }
            }
        });

        json = Util.getJson(TestDetails.this, testList);
        getdetails();
        try {
            databaseHelper = new DatabaseHelper(TestDetails.this);
            boolean b = databaseHelper.getassessorfeedback(testList.getId(), testList.getUniqueID());
            if (b) {
                anxvalidation.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void  gotoTestStartScreen(){
        Intent io = new Intent(TestDetails.this, ActivityTestStart.class);
        io.putExtra(C.TEST, testList);
        startActivity(io);
        finish();
    }

    void setUi() {
        try {
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String am = scheduleSettings.getString("annexure_m");
            if (am != null) {
                if (am.equalsIgnoreCase("0") || am.equalsIgnoreCase("2")) {
                    llassessor.setVisibility(View.GONE);
                    annexureMRequired=false;

                } else if (am.equalsIgnoreCase("1")) {
                    llassessor.setVisibility(View.VISIBLE);
                    annexureMRequired=true;
                } else {
                    llassessor.setVisibility(View.GONE);
                    annexureMRequired=false;
                }
            }

            String av = scheduleSettings.getString("assessor_validation");
            if (av != null) {
                if (av.equalsIgnoreCase("0")) {
                    llSelfiValidation.setVisibility(View.GONE);
                    assessorValidationRequired=false;
                } else if (am.equalsIgnoreCase("1")) {
                    llSelfiValidation.setVisibility(View.VISIBLE);
                    assessorValidationRequired=true;
                } else {
                    llSelfiValidation.setVisibility(View.GONE);
                    assessorValidationRequired=false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.FLAVOR_app.equals(C.tagscoresWithoutAssessor)) {
            llassessor.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(TestDetails.this, ActivityTestList.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void openPhotoCaptureActivity() {
        Intent intent = new Intent(this, ActivityLastImageCapture.class);
        intent.putExtra(C.TEST, new TestList());
        intent.putExtra(C.ACTIVE_DETAILS, "1");
        startActivity(intent);
        finish();
    }

    void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(TestDetails.this);
        View mView = layoutInflater.inflate(R.layout.validation, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TestDetails.this);
        builder.setView(mView);
        builder
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        android.app.AlertDialog alertDialogAndroid = builder.create();
        alertDialogAndroid.show();
    }

    public void getfeedbackgiven() {
        DatabaseHelper db = new DatabaseHelper(TestDetails.this);

        boolean b = db.getassessorfeedback(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        if (b == true) {
            acc = 1;
            anxvalidation.setVisibility(View.VISIBLE);
            btn_assessorfeedback.setEnabled(false);
            SharedPreference.getInstance(this).setString(C.ANNEXURE_DATA, null);
        } else {
            anxvalidation.setVisibility(View.GONE);
            btn_assessorfeedback.setEnabled(true);
        }
    }

    public void getSelfigiven() {
        DatabaseHelper db = new DatabaseHelper(TestDetails.this);

        boolean b = db.getAllImages(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), "AscPic-");
        if (b == true) {
            selfi = 1;
            selfevalidation.setVisibility(View.VISIBLE);
            btn_Imagecapurtevalidate.setEnabled(false);
        } else {
            selfevalidation.setVisibility(View.GONE);
            btn_Imagecapurtevalidate.setEnabled(true);
        }
    }

    public void getdetails() {
        String json = "";
        if (Util.ONLINE) {
            Log.e(C.DEBUG, "" + Util.ONLINE);
            json = SharedPreference.getInstance(TestDetails.this).getString(C.ONLINE_TEST_LIST);

        } else {
            Log.e(C.DEBUG, "" + Util.ONLINE);
            DatabaseHelper mydb = new DatabaseHelper(this);
            Cursor resss = mydb.gettest_details_json_string(testList.getId(), testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
                Util.showMessage(TestDetails.this, "Nothing found");
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
            JSONArray subObjDetails = jsonobj_2.getJSONArray("test_details");
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                String test_name = jsonobj_2_answer.get("schedule_name").toString();
                Globalclass.testname = test_name;
                String test_descriptions = jsonobj_2_answer.get("test_descriptions").toString();
                testnamede.setText(test_name);
                testdescription.setText(test_descriptions);
            }

            String candidatecount = String.valueOf(array.get("total_candidate"));
            testtargetcandidate.setText("You have " + candidatecount + " candidates to Assessed");

        } catch (JSONException e) {
            e.printStackTrace();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filepath = null;
        byte[] bytes;
        // File pictureFileDir = getDir();
       /* CameraManager cm = new CameraManager(TestDetails.this);
        File pictureFileDir = cm.getDir();
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            //Log.e("TAG", "Can't create directory to save image.");
            // return null;
        } else {
            ByteArrayOutputStream byteBuffer;*/
        if (resultCode == RESULT_OK) {
            if (requestCode == 2 && resultCode == RESULT_OK) {
                selfevalidation.setVisibility(View.VISIBLE);

                selfi = 1;
                   /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    // Toast.makeText(EvidenceActivity.this, bitmap.toString(), Toast.LENGTH_LONG).show();
                    // img.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    bytes = stream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // Toast.makeText(EvidenceActivity.this, "Conversion Done",
                    //  Toast.LENGTH_SHORT).show();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                    String date = dateFormat.format(new Date());
                    String photoFile = Globalclass.schduleid + "-" + "AscPic-" + date + ".jpg";
                    filepath = pictureFileDir.getPath() + File.separator + photoFile;
                    // .setImageURI(photoFile);
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
                    if (Util.ONLINE) {
                   //     uploadBitmap(filepath, testList.getScheduleIdPk(), "AscPic-" + photoFile, testList.getScheduleIdPk(), "AscPic-", photoFile);
                        selfevalidation.setVisibility(View.VISIBLE);
                    } else {
                        DatabaseHelper db = new DatabaseHelper(TestDetails.this);
                        boolean success = db.insert_imagesval("AscPic-", "non", photoFile, testList.getId(), filepath, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                        if (success == true) {
                            showMessage("", "Image saved Successfully");
                            //  Toast.makeText(EvidenceActivity.this,"saved",Toast.LENGTH_LONG).show();
                        } else {
                            // Toast.makeText(EvidenceActivity.this,"Not save images",Toast.LENGTH_LONG).show();
                        }
                    }*/
            }

        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            anxvalidation.setVisibility(View.VISIBLE);
        }
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

    private void uploadBitmap(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        final ProgressDialog Dialog1 = new ProgressDialog(TestDetails.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialog1.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestDetails.this);
                        //  builder.setTitle("Seems it's taking more time then usual !");
                        builder.setMessage("Please check your internet connection then try again");
                        builder.setCancelable(true);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
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

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                ResponseLogin responseLogin = SharedPreference.getInstance(TestDetails.this).getUser(C.LOGIN_USER);
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
        //   Volley.newRequestQueue(this,new HurlStack(null, new ClientSSLSocketFactory(300000).getSocketFactory())).add(volleyMultipartRequest);

    }

    private void getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUi();
        if (!BuildConfig.FLAVOR_app.equals(C.tagscoresWithoutAssessor)) {
            getfeedbackgiven();
        }
        getSelfigiven();
        if(!assessorValidationRequired && !annexureMRequired){
            gotoTestStartScreen();
        }
    }
}
