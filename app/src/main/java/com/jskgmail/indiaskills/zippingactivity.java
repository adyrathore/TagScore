package com.jskgmail.indiaskills;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class zippingactivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks{

    private static final int BUFFER = 2048;
    private CompressFiles mCompressFiles;
    private ArrayList<String> mFilePathList = new ArrayList<>();
    private TextView mProgressView;
    private ProgressDialog progressDialog;
    TestList testList;
    ResponseLogin responseLogin;
    public static File getOutputZipFile(String fileName) {
      //  String zipFile =  Environment.getExternalStorageDirectory().toString()+"/Pictures"+;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Pictures");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zippingactivity);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        responseLogin= SharedPreference.getInstance(zippingactivity.this).getUser(C.LOGIN_USER);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mProgressView = (TextView) findViewById(R.id.progress_text_view);
        Button btnZip = (Button) findViewById(R.id.btn_zip);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCompressFiles = new CompressFiles();
                mCompressFiles.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 2*1000);
        btnZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        btnZip.setVisibility(View.GONE);
        //Add Files To Zip
        addFilesToZip();
    }


    private void addFilesToZip() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Pictures"+"/"+testList.getScheduleIdPk();
      //  String zipFile =  Environment.getExternalStorageDirectory().toString()+"/Pictures"+"/"+"p"+schduleid+".zip";
        Log.e("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.e("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            mFilePathList.add(Environment.getExternalStorageDirectory().toString()+"/Pictures"+"/"+testList.getScheduleIdPk() +File.separator+files[i].getName());
            Log.e("Files", "FileName:" + files[i].getName());
        }
    }


    public void setCompressProgress(int filesCompressionCompleted) {
        mCompressFiles.publish(filesCompressionCompleted);
    }

    //Zipping function
    public void zip(String zipFilePath) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFilePath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for (int i = 0; i < mFilePathList.size(); i++) {

                setCompressProgress(i + 1);

                FileInputStream fi = new FileInputStream(mFilePathList.get(i));
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(mFilePathList.get(i).substring(mFilePathList.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    private class CompressFiles extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

            try {
                mProgressView.setText("0% Completed");
            } catch (Exception ignored) {
            }
        }

        protected Boolean doInBackground(Void... urls) {

            File file = getOutputZipFile(testList.getScheduleIdPk()+".zip");

            String zipFileName;
            if (file != null) {
                zipFileName = file.getAbsolutePath();

                if (mFilePathList.size() > 0) {
                    zip(zipFileName);
                }
            }

            return true;
        }

        public void publish(int filesCompressionCompleted) {
            int totalNumberOfFiles = mFilePathList.size();
            publishProgress((100 * filesCompressionCompleted) / totalNumberOfFiles);
        }

        protected void onProgressUpdate(Integer... progress) {

            try {
                mProgressView.setText(Integer.toString(progress[0]) + "% Completed");
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        protected void onPostExecute(Boolean flag) {
            Log.d("COMPRESS_TASK", "COMPLETED");

            mProgressView.setText("100 % Completed");

          /*  new Thread(new Runnable() {
                public void run() {
*/

                    //   uploadFile(zipFile,filename);
                    String path = Environment.getExternalStorageDirectory().toString()+"/Pictures"+"/"+testList.getScheduleIdPk();
                    String zipFile =  Environment.getExternalStorageDirectory().toString()+File.separator+"Pictures"+File.separator+testList.getScheduleIdPk()+".zip";
                    String filename = testList.getScheduleIdPk()+ ".zip";
                    progressbarforvalues();
                    //  uploadBitmap(zipFile,filename,testList.getScheduleIdPk(),testList.getId());
                    uploadZipFile(testList.getId(),Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(),responseLogin.getUserID(),responseLogin.getApiKey(),zipFile);
              /*  }
            }).start();*/
           /* final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    //finish();
                }
            }, 1*1000);*/


            Toast.makeText(getApplicationContext(), "Zipping Completed", Toast.LENGTH_SHORT).show();
        }
    }

    public  void progressbarforvalues(){
        progressDialog = new ProgressDialog(zippingactivity.this);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Uploading... Please wait.");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

    }


    private void uploadZipFile(final String testId, final String scheduleId, String user_Id, String apiKey, final String filPath) {
       try {
           Log.e("Send zip", filPath);
           File file = new File(filPath);
           // Uri fileUri  =  Uri.fromFile(new File(filPath));

           RequestBody testID = RequestBody.create(MediaType.parse("text/plain"), testId);
           RequestBody schedule_id = RequestBody.create(MediaType.parse("text/plain"), scheduleId);
           RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), user_Id);
           RequestBody api_key = RequestBody.create(MediaType.parse("text/plain"), apiKey);
           //  RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
           String credentials = "tagusp:t@g$c0re";
           String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
           ProgressRequestBody fileBody = new ProgressRequestBody(file, "multipart/form-data", this);
           Log.e("Filename=", file.getName() + "." + getFileExtension(file));
           //  RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);
           MultipartBody.Part body = MultipartBody.Part.createFormData("zipFile", file.getName(), fileBody);
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
                   .connectTimeout(60, TimeUnit.MINUTES)
                   .readTimeout(60, TimeUnit.MINUTES)
                   .writeTimeout(60, TimeUnit.MINUTES).
           addInterceptor(interceptor).build();
           //creating retrofit object
           Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl(Api.BASE_URL).
                           client(okHttpClient)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .build();

           //creating our api
           Api api = retrofit.create(Api.class);

           //creating a call and calling the upload image method
           Call<UploadZipResponse> call = api.uploadImage(auth, body, userId, api_key, testID, schedule_id);

           call.enqueue(new Callback<UploadZipResponse>() {
               @Override
               public void onResponse(Call<UploadZipResponse> call, retrofit2.Response<UploadZipResponse> response) {
                   progressDialog.dismiss();

                   if (response.body().getResponseCode().equals("200")) {
                       try {
                           Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();

                           DatabaseHelper myDb = new DatabaseHelper(zippingactivity.this);
                           boolean success = myDb.delete_byID(testId, scheduleId);
                           File file = new File(filPath);
                           boolean deleted = file.delete();

                           Intent dialogIntent = new Intent(zippingactivity.this, ActivityTestList.class);
                           dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(dialogIntent);
                           Globalclass.uplodingflagvalues = "1";
                       }
                       catch (Exception e){
                           e.printStackTrace();
                       }
                   } else {
                       //  Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                       uploadError();
                   }
               }

               @Override
               public void onFailure(Call<UploadZipResponse> call, Throwable t) {
                   progressDialog.dismiss();
                   //   Log.e("DEBUG",t.getMessage());
                   //   Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                   uploadError();
               }
           });
           //finally performing the call
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

    void uploadError(){
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(zippingactivity.this);
            builder.setMessage("There is an error in upload file. Please retry");
            builder.setCancelable(false);
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // openFeedbackScreen();
//                                finish();
                    String zipFile = Environment.getExternalStorageDirectory().toString() + File.separator + "Pictures" + File.separator + testList.getScheduleIdPk() + ".zip";
                    progressbarforvalues();
                    uploadZipFile(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(), responseLogin.getUserID(), responseLogin.getApiKey(), zipFile);
                }
            });
            builder.show();
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void uploadBitmap(final String  zipFile, final String filename,final String schduleid, final  String testid) {
        Log.e("Send zip", zipFile );
        //getting the tag from the edittext
        //final String tags = editTextTags.getText().toString().trim();
        String url=C.API_MEDIA_UPLOAD;
        //our custom volley request
        com.jskgmail.indiaskills.VolleyMultipartRequest volleyMultipartRequest = new com.jskgmail.indiaskills.VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("Send values",response.toString());
                        DatabaseHelper myDb = new DatabaseHelper(zippingactivity.this);
                        boolean success = myDb.delete_byID(testid,schduleid);
                        File file = new File(zipFile);
                        boolean deleted = file.delete();

                        Intent dialogIntent = new Intent(zippingactivity.this, ActivityTestList.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                        Globalclass.uplodingflagvalues= "1";
                        //   JSONObject obj = new JSONObject(new String(response.data));
                        // Dialog1.dismiss();
                        // showMessage("","Image Uploded...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Dialog1.dismiss();
                        // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        DatabaseHelper myDb = new DatabaseHelper(zippingactivity.this);
                        boolean success = myDb.delete_byID(testid,schduleid);
                        File file = new File(zipFile);
                        boolean deleted = file.delete();

                        Intent dialogIntent = new Intent(zippingactivity.this, ActivityTestList.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                        Globalclass.uplodingflagvalues= "1";
//                        Log.e("Loop Count ",error.getMessage());
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
             /*   Log.e("usedID",Globalclass.id_login);
                Log.e("api_key",Globalclass.apikeys);
                Log.e("testID",Globalclass.Testidupload);
                Log.e("schedule_id",schduleid);*/
                params.put("userId", responseLogin.getUserID());
                params.put("api_key",responseLogin.getApiKey());
                params.put("testID",testList.getId());
                params.put("schedule_id", Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
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
                params.put("zipFile", new DataPart(filename, readBytesFromFile(zipFile)));
                return params;
            }

        };
        //adding request to Volley
       // Volley.newRequestQueue(this).add(volleyMultipartRequest);

        int socketTimeout = 900000000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        volleyMultipartRequest.setRetryPolicy(policy);
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

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
