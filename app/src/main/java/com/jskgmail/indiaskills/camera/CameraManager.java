package com.jskgmail.indiaskills.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

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
import com.jskgmail.indiaskills.Globalclass;
import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.TestQuestionDisplayActivity;
import com.jskgmail.indiaskills.VolleyMultipartRequest;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

public class CameraManager implements Camera.ErrorCallback, Camera.PreviewCallback, Camera.AutoFocusCallback, Camera.PictureCallback ,ProgressRequestBody.UploadCallbacks{

    public static CameraManager getInstance(Context context) {
        if (mManager == null) mManager = new CameraManager(context);
        return mManager;
    }

    public void takePhoto() {
        try {
            if (isBackCameraAvailable() && !isWorking) {
                initCamera();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initCamera() {
        try {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        isWorking = true;
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Cannot open camera");
                        e.printStackTrace();
                        isWorking = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        if (mCamera != null) {
                            mSurface = new SurfaceTexture(123);
                            mCamera.setPreviewTexture(mSurface);

                            Camera.Parameters params = mCamera.getParameters();
                            int angle = 270;//getCameraRotationAngle(Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
                            params.setRotation(angle);

                            if (autoFocusSupported(mCamera)) {
                                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            } else {
                                Log.w(TAG, "Autofocus is not supported");
                            }

                            mCamera.setParameters(params);
                            mCamera.setPreviewCallback(CameraManager.this);
                            mCamera.setErrorCallback(CameraManager.this);
                            mCamera.startPreview();
                            muteSound();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Cannot set preview for the front camera");
                        e.printStackTrace();
                        releaseCamera();
                    }
                }

            }.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mSurface.release();
                mCamera.release();
                mCamera = null;
                mSurface = null;
            }
            unmuteSound();
            isWorking = false;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        try {
            if (autoFocusSupported(camera)) {
                mCamera.autoFocus(this);
            } else {
                camera.setPreviewCallback(null);
                camera.takePicture(null, null, this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Camera error while taking picture");
            e.printStackTrace();
            releaseCamera();
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (camera != null) {
            try {
                camera.takePicture(null, null, this);
                mCamera.autoFocus(null);
            } catch (Exception e) {
                e.printStackTrace();
                releaseCamera();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        try {
            savePicture(bytes);
            releaseCamera();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int error, Camera camera) {
        switch (error) {
            case Camera.CAMERA_ERROR_SERVER_DIED:
                Log.e(TAG, "Camera error: Media server died");
                break;
            case Camera.CAMERA_ERROR_UNKNOWN:
                Log.e(TAG, "Camera error: Unknown");
                break;
            case Camera.CAMERA_ERROR_EVICTED:
                Log.e(TAG, "Camera error: Camera was disconnected due to use by higher priority user");
                break;
            default:
                Log.e(TAG, "Camera error: no such error id (" + error + ")");
                break;
        }
    }

    public CameraManager(Context context) {
        mContext = context;
    }

    private boolean isBackCameraAvailable() {
        boolean result = false;
        try {

            if (mContext != null && mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                int numberOfCameras = Camera.getNumberOfCameras();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        result = true;
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private boolean autoFocusSupported(Camera camera) {
        try {
            if (camera != null) {
                Camera.Parameters params = camera.getParameters();
                List<String> focusModes = params.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void muteSound() {
        try {
            if (mContext != null) {
                AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
                } else {
                    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void unmuteSound() {
        try {
            if (mContext != null) {
                AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
                } else {
                    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCameraRotationAngle(int cameraId, Camera camera) {
        int result = 270;
        if (camera != null && mContext != null) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            int degrees = getRotationAngle(rotation);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360; //compensate mirroring
                Log.d(TAG, "Screen rotation: " + degrees +
                        "; camera orientation: " + info.orientation +
                        "; rotate to angle: " + result);
            }
        }
        return result;
    }

    private int getRotationAngle(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
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

        private void uploadBitmap(final String imageurlval, final String tagidval, String nameimageval, String targetid, final String imagetypeval,String nameimageorg) {
            final TestList testList = SharedPreference.getInstance(mContext).getTest(C.ONGOING_TEST);
            File file= new File(imageurlval);
        // Uri fileUri  =  Uri.fromFile(new File(filPath));
        ResponseLogin responseLogin = SharedPreference.getInstance(mContext).getUser(C.LOGIN_USER);

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


            }

            @Override
            public void onFailure(Call<UploadZipResponse> call, Throwable t) {


            }
        });
        //finally performing the call
    }

/*
    private void uploadBitmap(final String imageurlval, final String tagidval, final String nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        //   final ProgressDialog Dialog1 = new ProgressDialog(TestDetails.this);
        // Dialog1.setMessage("Uploding Image Please wait...");
        // Dialog1.show();
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        final ResponseLogin responseLogin = SharedPreference.getInstance(mContext).getUser(C.LOGIN_USER);
        final TestList testList = SharedPreference.getInstance(mContext).getTest(C.ONGOING_TEST);
        String url = C.API_UPLOAD_VIDEO_AND_PHOTO;
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //   JSONObject obj = new JSONObject(new String(response.data));
                        //               Dialog1.dismiss();
                        String str = "submitted";
                        // showMessage("","Image Uploded...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

            */
/*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * *//*

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

            */
/*
             * Here we are passing image by renaming it with a unique name
             * *//*

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("imageHash", new DataPart(nameimageval, readBytesFromFile(imageurlval)));
                return params;
            }
        };
      //  Volley.newRequestQueue(mContext,new HurlStack(null, new ClientSSLSocketFactory(300000).getSocketFactory())).add(volleyMultipartRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext, new HurlStack() {


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
*/


    private String savePicture(byte[] bytes) {
        final TestList testList = SharedPreference.getInstance(mContext).getTest(C.ONGOING_TEST);

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
         //   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
          //  String date = dateFormat.format(new Date());
            String date=Util.getCurrentTimeStamp();
            String photoFile = null;
            if (Globalclass.lastpicturecandidate.equalsIgnoreCase("0")) {
                photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                photoFile=photoFile+"-"+Globalclass.idcandidate + "-" + "rmPic-" +testList.getId()+"_"+ date + ".jpg";

            } else {
                photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID();
                photoFile=photoFile+"-"+Globalclass.idcandidate + "-" + "CdPic-" +testList.getId()+"_"+ date + ".jpg";

            }
            filepath = pictureFileDir.getPath() + File.separator + photoFile;
            File pictureFile = new File(filepath);
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(bytes);
            //   String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            DatabaseHelper db = new DatabaseHelper(mContext);
            if (Util.ONLINE) {
                if (Globalclass.lastpicturecandidate.equalsIgnoreCase("0")) {
                    uploadBitmap(filepath, Globalclass.userids, photoFile, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID() + "/" + Globalclass.idcandidate, "rmPic", photoFile);
                } else {
                    uploadBitmap(filepath, Globalclass.userids, photoFile, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID() + "/" + Globalclass.idcandidate, "CdPic", photoFile);
                }
            } else {
                if (Globalclass.lastpicturecandidate.equalsIgnoreCase("1")) {
                    boolean success = db.insert_imagesval("CdPic", "non", photoFile, testList.getId(), filepath, Globalclass.idcandidate, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                    if (success == true) {
                        // Toast.makeText(mContext,"saved",Toast.LENGTH_LONG).show();
                    } else {
                        //  Toast.makeText(mContext,"Not save images",Toast.LENGTH_LONG).show();
                    }
                } else {
                    boolean success = db.insert_imagesval("rmPic", "non", photoFile, testList.getId(), filepath, Globalclass.idcandidate, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                    if (success == true) {
                        // Toast.makeText(mContext,"saved",Toast.LENGTH_LONG).show();
                    } else {
                        //  Toast.makeText(mContext,"Not save images",Toast.LENGTH_LONG).show();
                    }
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
        final TestList testList = SharedPreference.getInstance(mContext).getTest(C.ONGOING_TEST);
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
    }

    public  File getVideoDir() {
        final TestList testList=SharedPreference.getInstance(mContext).getTest(C.ONGOING_TEST);
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
    }

    private static final String TAG = CameraManager.class.getSimpleName();
    private static CameraManager mManager;
    private Context mContext;
    private Camera mCamera;
    private SurfaceTexture mSurface;
    private boolean isWorking = false;

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
