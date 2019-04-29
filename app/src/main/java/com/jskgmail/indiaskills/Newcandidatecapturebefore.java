package com.jskgmail.indiaskills;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jskgmail.indiaskills.camera.CameraManager;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.UploadZipResponse;
import com.jskgmail.indiaskills.util.Api;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.ClientSSLSocketFactory;
import com.jskgmail.indiaskills.util.ProgressRequestBody;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Newcandidatecapturebefore extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
    int prof1=0,prof2=0;
    CardView cardview,cardviewidcard;
    private final int requestCode = 20;
    String encodedImage;
    String capturetype = "";
    ImageView imgicon,imgiconidcard;
    ImageView btn_imagecapture, btn_imagecaptureidcard;
    ImageView img_photoidcard,img_photo;
    TextView txt_userabsent;
    Button btnNext;
    String imgcount = "0";
    boolean isIdCardClicked=false,isCandidatePicClicked=false;
    Toolbar toolbar;
    TestList testList;
    String activeDetails,usernameSelected,tagId;
    byte[] byteArray;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcandidatecapturebefore);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails=getIntent().getStringExtra(C.ACTIVE_DETAILS);
        usernameSelected=getIntent().getStringExtra(C.SELECTED_USERNAME);
        tagId=getIntent().getStringExtra(C.TAG_ID);

        cardview = (CardView) findViewById(R.id.cardview);
        cardviewidcard = (CardView) findViewById(R.id.cardviewidcard);
        imgicon = (ImageView) findViewById(R.id.imgicon);
        imgiconidcard = (ImageView) findViewById(R.id.imgiconidcard);
        btn_imagecapture = (ImageView) findViewById(R.id.btn_imagecapture);
        btn_imagecaptureidcard = (ImageView) findViewById(R.id.btn_imagecaptureidcard);
        img_photoidcard = (ImageView) findViewById(R.id.img_photoidcard);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        txt_userabsent = (TextView) findViewById(R.id.txt_userabsent);
        btnNext = (Button) findViewById(R.id.btn_next);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prof1==1 && prof2==1) {
                    Intent intent = new Intent(Newcandidatecapturebefore.this, StudentInstruction.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                }
                else if(prof1!=1){
                    showMessage("",getString(R.string.candidate_photo_required));
                }
                else if(prof2!=1){
                    showMessage("",getString(R.string.cid_card_photo_required));
                }
            }
        });
        txt_userabsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Newcandidatecapturebefore.this);

                builder.setMessage("Are you sure you want to mark Absent" + " "+ usernameSelected);
                builder.setCancelable(true);
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                                savevaluesforabsent();
                        //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcount = "1";

                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
                capturetype = "photo";
            }
        });

        cardviewidcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgcount = "2";
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
                capturetype = "IDcard";
            }
        });
      //  btnNext.setEnabled(false);
    }

    public void savevaluesforabsent(){
        DatabaseHelper db = new DatabaseHelper(Newcandidatecapturebefore.this);
        boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids,Globalclass.userids, Util.ONLINE?testList.getScheduleIdPk():testList.getUniqueID(),activeDetails,"1");
        if(successval == true){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Newcandidatecapturebefore.this);
            builder.setMessage("Candidate" + usernameSelected +" Marked Absent ");
            builder.setCancelable(false);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent    = new Intent(Newcandidatecapturebefore.this,ActivityInstruction.class);
                    intent.putExtra(C.ACTIVE_DETAILS,activeDetails);
                    intent.putExtra(C.TEST,testList);
                    startActivity(intent);
                    finish();
                    //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
        else {
            showMessage("","Unable to save absent mark please try again");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Newcandidatecapturebefore.this,ActivityInstruction.class);
            intent.putExtra(C.ACTIVE_DETAILS,activeDetails);
            intent.putExtra(C.TEST,testList);
            startActivity(intent);
            finish();        }
        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = null;
        byte[] bytes;
        // File pictureFileDir = getDir();
        CameraManager cm = new CameraManager(Newcandidatecapturebefore.this);
        File pictureFileDir = cm.getDir();
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            //Log.e("TAG", "Can't create directory to save image.");
            // return null;
        }
        super.onActivityResult(requestCode, resultCode, data);
        ByteArrayOutputStream byteBuffer;
        if (resultCode == RESULT_OK) {
            if (this.requestCode == requestCode && resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                // Toast.makeText(EvidenceActivity.this, bitmap.toString(), Toast.LENGTH_LONG).show();
                // img.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                bytes = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(EvidenceActivity.this, "Conversion Done",
                //  Toast.LENGTH_SHORT).show();
             //   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            //    String date = dateFormat.format(new Date());
                String date=Util.getCurrentTimeStamp();
                String photoFile = Util.ONLINE?testList.getScheduleIdPk():testList.getUniqueID();

                photoFile=photoFile+"-"+Globalclass.idcandidate+"-"+"CdPic-"+testList.getId()+"_" + date + ".jpg";
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
                if(Util.ONLINE) {

                    uploadImage(pictureFile.getPath(),Globalclass.userids,photoFile, Util.ONLINE?testList.getScheduleIdPk():testList.getUniqueID() + "/" + Globalclass.idcandidate,"CdPic",photoFile);
                }
                else {
                    DatabaseHelper db = new DatabaseHelper(Newcandidatecapturebefore.this);
                    boolean success = db.insert_imagesval("CdPic","non",photoFile,testList.getId(),filepath,Globalclass.idcandidate,Util.ONLINE?testList.getScheduleIdPk():testList.getUniqueID());
                    if(success == true)
                    {
                        if(capturetype.equalsIgnoreCase("photo")){
                            btn_imagecapture.setVisibility(View.GONE);
                            imgicon.setVisibility(View.VISIBLE);
                            img_photo.setVisibility(View.VISIBLE);
                            img_photo.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                            txt_userabsent.setVisibility(View.GONE);
                            if(imgcount.equalsIgnoreCase("2")){
                                prof2=1;
                            }
                            else if(imgcount.equalsIgnoreCase("1")){
                                prof1=1;
                            }

                            if(prof2==1 && prof1==1){
                                btnNext.setEnabled(true);
                            }
                        }
                        else {
                            btn_imagecaptureidcard.setVisibility(View.GONE);
                            imgiconidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                            txt_userabsent.setVisibility(View.GONE);
                            if(imgcount.equalsIgnoreCase("2")){
                                prof2=1;
                            }
                            else if(imgcount.equalsIgnoreCase("1")){
                                prof1=1;
                            }
                            if(prof2==1 && prof1==1){
                                btnNext.setEnabled(true);
                            }
                        }
                    }
                }
            }
        }
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
       // builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }


        private void uploadImage(final String imageurlval, final String tagidval, String nameimageval, String targetid, final String imagetypeval,String nameimageorg) {
        progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
        progressDialog.show();
        Log.e("Send zip", imageurlval );
        File file= new File(imageurlval);
        // Uri fileUri  =  Uri.fromFile(new File(filPath));
        ResponseLogin responseLogin = SharedPreference.getInstance(Newcandidatecapturebefore.this).getUser(C.LOGIN_USER);

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
        String auth = "Basic"+" "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "multipart/form-data",this);

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
        Call<UploadZipResponse> call = api.uploadImageVideo(auth,body,userId, api_key,testID,uniqueID,picType,candidate_id,version,target_dir);

        call.enqueue(new Callback<UploadZipResponse>() {
            @Override
            public void onResponse(Call<UploadZipResponse> call, retrofit2.Response<UploadZipResponse> response) {
                progressDialog.dismiss();
                if(capturetype.equalsIgnoreCase("photo")){
                    btn_imagecapture.setVisibility(View.GONE);
                    imgicon.setVisibility(View.VISIBLE);
                    img_photo.setVisibility(View.VISIBLE);
                    img_photo.setImageURI(Uri.parse("file://" + imageurlval));
                    txt_userabsent.setVisibility(View.GONE);
                }
                else {
                    btn_imagecaptureidcard.setVisibility(View.GONE);
                    imgiconidcard.setVisibility(View.VISIBLE);
                    img_photoidcard.setVisibility(View.VISIBLE);
                    img_photoidcard.setImageURI(Uri.parse("file://" + imageurlval));
                    txt_userabsent.setVisibility(View.GONE);
                }
                if(imgcount.equalsIgnoreCase("2"))
                {
                    prof2=1;

                }
                else if(imgcount.equalsIgnoreCase("1")) {
                    prof1=1;
                }
                if(prof2==1 && prof1==1){
                    btnNext.setEnabled(true);
                }
                showMessage("","Image Uploded...");
            }

            @Override
            public void onFailure(Call<UploadZipResponse> call, Throwable t) {
                progressDialog.dismiss();
                //   Log.e("DEBUG",t.getMessage());
                showMessage("","Unable to upload please try again");

            }
        });
        //finally performing the call
    }

    private void uploadBitmap(final String imageurlval, final String tagidval, final String  nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        final ResponseLogin responseLogin= SharedPreference.getInstance(Newcandidatecapturebefore.this).getUser(C.LOGIN_USER);

        final ProgressDialog Dialog1 = new ProgressDialog(Newcandidatecapturebefore.this);
        Dialog1.setMessage("Uploding Image Please wait...");
        Dialog1.show();
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        String url=C.API_UPLOAD_VIDEO_AND_PHOTO;
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {


                    @Override
                    public void onResponse(NetworkResponse response) {
                        //   JSONObject obj = new JSONObject(new String(response.data));
                        Dialog1.dismiss();

                        if(capturetype.equalsIgnoreCase("photo")){
                            btn_imagecapture.setVisibility(View.GONE);
                            imgicon.setVisibility(View.VISIBLE);
                            img_photo.setVisibility(View.VISIBLE);
                            img_photo.setImageURI(Uri.parse("file://" + imageurlval));
                            txt_userabsent.setVisibility(View.GONE);
                        }
                        else {
                            btn_imagecaptureidcard.setVisibility(View.GONE);
                            imgiconidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setImageURI(Uri.parse("file://" + imageurlval));
                            txt_userabsent.setVisibility(View.GONE);
                        }
                        if(imgcount.equalsIgnoreCase("2"))
                        {
                            prof2=1;

                        }
                        else if(imgcount.equalsIgnoreCase("1")) {
                            prof1=1;
                        }
                        if(prof2==1 && prof1==1){
                            btnNext.setEnabled(true);
                        }
                         showMessage("","Image Uploded...");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("","Unable to upload please try again");
                        Dialog1.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("api_key",responseLogin.getApiKey());
                params.put("testID",testList.getId());
                params.put("uniqueID",testList.getUniqueID());
                params.put("picType",imagetypeval);
                params.put("candidate_id",tagidval);
                params.put("version","m");
                params.put("target_dir",targetid);
               // params.put("filename",nameimageval);
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

    /*    RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack() {


            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection urlConnection =
                        (HttpsURLConnection)url.openConnection();
                urlConnection.setHostnameVerifier(Util.getHostname());


                return urlConnection;
            }
        });
        requestQueue.add(volleyMultipartRequest);*/
        Volley.newRequestQueue(this,new HurlStack(null, new ClientSSLSocketFactory(300000).getSocketFactory())).add(volleyMultipartRequest);
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
    protected void onStart() {
        super.onStart();

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
