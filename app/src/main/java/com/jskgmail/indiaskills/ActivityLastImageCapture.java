package com.jskgmail.indiaskills;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
import com.jskgmail.indiaskills.camera.CameraPreview;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityLastImageCapture extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {
    private static final String TAG = "";
    @BindView(R.id.rlCameraPreview)
    RelativeLayout rlCameraPreview;
    @BindView(R.id.btnPhoto)
    Button btnTakePhoto;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    boolean cameraFront = false;
    int flagcapture = 0;
    private Dialog progressDialog;
    private TestList testList;
    private String activeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_camera_picture);
        ButterKnife.bind(this);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCamera.takePicture(null, null, mPicture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle extras = getIntent().getExtras();
        String newString = null;

        if (extras != null) {
            activeDetails = extras.getString(C.ACTIVE_DETAILS);
            if (activeDetails.equals("1")) {
                flagcapture = 2;
                cameraFront = false;
            } else {
                flagcapture = 1;
                cameraFront = true;
            }
        }
        try {

            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPreview = new CameraPreview(this, mCamera);
            rlCameraPreview.addView(mPreview);

            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                releaseCamera();
                openCamera();
                mCamera.startPreview();

            } else {

                Util.showMessage(this, R.string.you_dont_have_front_camera);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(500);
                openCamera();
                mPreview = new CameraPreview(this, mCamera);
                rlCameraPreview.addView(mPreview);
            } catch (Exception execp) {
                execp.printStackTrace();
            }

        }
    }


    private int findFrontFacingCamera() {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        try {
            super.onResume();
            releaseCamera();


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mCamera == null) {
                            openCamera();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onResume();
                    }
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void openCamera() {
        if (cameraFront) {
            int cameraId = findFrontFacingCamera();
//            int cameraId = findBackFacingCamera();

            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                if (mPreview == null)
                    mPreview = new CameraPreview(this, mCamera);

                mPreview.refreshCamera(mCamera);

            }
        } else {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                if (mPreview == null)
                    mPreview = new CameraPreview(this, mCamera);
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                savePicture(data);
            }
        };
        return picture;
    }

    private static byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
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


    private void uploadImage(final String imageurlval, final String tagidval, String nameimageval, String targetid, final String imagetypeval, String nameimageorg) {
        progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
        progressDialog.show();
        Log.e("Send zip", imageurlval);
        File file = new File(imageurlval);
        // Uri fileUri  =  Uri.fromFile(new File(filPath));
        ResponseLogin responseLogin = SharedPreference.getInstance(ActivityLastImageCapture.this).getUser(C.LOGIN_USER);

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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .build();
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

                if (response != null && response.body() != null && response.body().getResponseCode() != null && response.body().getResponseCode().equals("200")) {
                    int camerasNumber = Camera.getNumberOfCameras();
                    if (camerasNumber > 1) {
                        releaseCamera();
                        openCamera();
                    } else {

                    }
                    progressDialog.dismiss();

//                        Util.showMessage(ActivityFrontFacingCamera.this,R.string.image_saved);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLastImageCapture.this);
                    builder.setMessage("Image saved ...");
                    builder.setCancelable(false);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openFeedbackScreen();
//                                finish();
                        }
                    });
                    try {
                        builder.show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    showMessage("", "Unable to upload please try again");

                    int camerasNumber = Camera.getNumberOfCameras();
                    if (camerasNumber > 1) {
                        //release the old camera instance
                        //switch camera, from the front and the back and vice versa

                        releaseCamera();
                        openCamera();
                    } else {

                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UploadZipResponse> call, Throwable t) {
                progressDialog.dismiss();
                //   Log.e("DEBUG",t.getMessage());
                showMessage("", "Unable to upload please try again");

                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    //release the old camera instance
                    //switch camera, from the front and the back and vice versa

                    releaseCamera();
                    openCamera();
                } else {

                }
                progressDialog.dismiss();
            }
        });
        //finally performing the call
    }

    private void uploadBitmap(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        progressDialog = Util.getProgressDialog(this, R.string.uploading_please_wait);
        progressDialog.show();

        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        String url = C.API_UPLOAD_VIDEO_AND_PHOTO;
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //   JSONObject obj = new JSONObject(new String(response.data));
                        int camerasNumber = Camera.getNumberOfCameras();
                        if (camerasNumber > 1) {
                            releaseCamera();
                            openCamera();
                        } else {

                        }
                        progressDialog.dismiss();

//                        Util.showMessage(ActivityFrontFacingCamera.this,R.string.image_saved);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLastImageCapture.this);
                        builder.setMessage("Image saved ...");
                        builder.setCancelable(false);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                openFeedbackScreen();
//                                finish();
                            }
                        });
                        builder.show();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("", "Unable to upload please try again");

                        int camerasNumber = Camera.getNumberOfCameras();
                        if (camerasNumber > 1) {
                            //release the old camera instance
                            //switch camera, from the front and the back and vice versa

                            releaseCamera();
                            openCamera();
                        } else {

                        }
                        progressDialog.dismiss();
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
                ResponseLogin responseLogin = SharedPreference.getInstance(ActivityLastImageCapture.this).getUser(C.LOGIN_USER);
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


    private String savePicture(byte[] bytes) {
        String filepath = null;
        try {
            File pictureFileDir = getDir();
            if (bytes == null) {
                Log.e(TAG, "Can't save image - no data");
                return null;
            }
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                Log.e(TAG, "Can't create directory to save image.");
                return null;
            }
            //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String photoFile = "";
            //    String date = dateFormat.format(new Date());
            String date = Util.getCurrentTimeStamp();
            if (flagcapture == 1) {
                photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                photoFile=photoFile+"-"+Globalclass.idcandidate + "-" + "rmPic-" +testList.getId()+"_"+ date + ".jpg";

            } else if (flagcapture == 2) {
                photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                photoFile = photoFile + "-" + "CdPic-" + testList.getId() + "_" + date + ".jpg";

            }

            filepath = pictureFileDir.getPath() + File.separator + photoFile;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            saveFile(filepath, bytes);
            // .setImageURI(photoFile);
            try {
                ExifInterface exifObject = new ExifInterface(filepath);
                int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, 3);
                Bitmap imageRotate = rotateBitmap(bitmap, 8);


        /*    File pictureFile = new File(filepath);
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(bytes);*/

                //    Bitmap bitmap1=rotateImageIfRequired(bitmap,filepath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageRotate.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                bytes = stream.toByteArray();
                new File(filepath).delete();
                saveFile(filepath, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File pictureFile = new File(filepath);
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(bytes);

            //   String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            if (Util.ONLINE) {
                if (flagcapture == 1) {
//                    uploadBitmap(filepath, Globalclass.schduleid, "rmPic-" + photoFile, Globalclass.schduleid, "rmPic-", photoFile);
                    uploadImage(filepath, testList.getScheduleIdPk(), "rmPic-" + photoFile, testList.getScheduleIdPk(), "rmPic-", photoFile);

                } else if (flagcapture == 2) {
                    uploadImage(pictureFile.getPath(), Globalclass.idcandidate, photoFile, testList.getScheduleIdPk() + "/" + Globalclass.idcandidate, "CdPic", photoFile);

                }

                //uploadBitmap(pictureFile.getPath(),Globalclass.idcandidate,photoFile,Globalclass.schduleid + "/" + Globalclass.idcandidate,"CdPic",photoFile);
            } else {
                DatabaseHelper db = new DatabaseHelper(ActivityLastImageCapture.this);
                boolean success = false;
                if (flagcapture == 1) {
                    success = db.insert_imagesval("rmPic-", "non", photoFile, testList.getId(), filepath, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                } else if (flagcapture == 2) {
                    success = db.insert_imagesval("CdPic", "non", photoFile, testList.getId(), filepath, Globalclass.idcandidate, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                }
                // boolean success = db.insert_imagesval("CdPic","non",photoFile,Globalclass.batchidoffline,filepath,Globalclass.idcandidate,Globalclass.schduleid);
                if (success == true) {
                    int camerasNumber = Camera.getNumberOfCameras();
                    if (camerasNumber > 1) {
                        //release the old camera instance
                        //switch camera, from the front and the back and vice versa

                        releaseCamera();
                        //  openCamera();
                    } else {

                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLastImageCapture.this);
                    builder.setMessage("Image saved ...");
                    builder.setCancelable(false);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            if (flagcapture == 1 ||) {
                            openFeedbackScreen();
//                            }
                            //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                    // Toast.makeText(PictureCaptureBefore.this,"saved",Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(PictureCaptureBefore.this,"Not save images",Toast.LENGTH_LONG).show();
                }
            }
            fos.close();
            Log.d(TAG, "New image was saved:" + photoFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
    }

    @Override
    protected void onDestroy() {
        //android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
        //Runtime.getRuntime().gc();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(ActivityLastImageCapture.this, ActivityLastImageCapture.class);
                intent.putExtra(C.TEST, testList);
                intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                startActivity(intent);
            }
        });
        builder.setMessage(Message);
        builder.show();
    }

    private void openFeedbackScreen() {

        String json = Util.getJson(ActivityLastImageCapture.this, testList);

        try {
            JSONObject scheduleSettings = Util.getScheduleSettings(json);
            String cf = scheduleSettings.getString("candidate_feedback");


            if (cf != null) {
                if (cf.equalsIgnoreCase("0")) {
                    if (Util.ONLINE) {
                        gotoCandidateFeedBack();
                    } else {
                        Globalclass.userids = "";
                        Globalclass.lastpicturecandidate = "0";
                        Intent intent = new Intent(ActivityLastImageCapture.this, ActivityThankyou.class);
                        intent.putExtra(C.TEST, testList);
                        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                        startActivity(intent);
                        finish();
                    }

                } else if (cf.equalsIgnoreCase("1")) {
                    gotoCandidateFeedBack();
                } else {
                    gotoCandidateFeedBack();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void gotoCandidateFeedBack() {
        Intent intent = new Intent(ActivityLastImageCapture.this, ActivityCandidateFeedbackForm.class);
        intent.putExtra(C.TEST, testList);
        intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
        startActivity(intent);
        finish();
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

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    void saveFile(String filepath, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            File pictureFile = new File(filepath);
            fos = new FileOutputStream(pictureFile);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
}
