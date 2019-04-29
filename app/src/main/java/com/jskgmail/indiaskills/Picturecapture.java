package com.jskgmail.indiaskills;

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
import android.util.Base64;
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
import com.jskgmail.indiaskills.camera.CameraManager;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.ClientSSLSocketFactory;
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

import javax.net.ssl.HttpsURLConnection;

public class Picturecapture extends AppCompatActivity {

    CardView cardview,cardviewidcard;
    private final int requestCode = 20;
    String encodedImage;
    String capturetype = "";
    ImageView imgicon,imgiconidcard;
    ImageView btn_imagecapture, btn_imagecaptureidcard;
    ImageView img_photoidcard,img_photo;
    TextView txt_userabsent;
    Button btn_next;
    String imgcount = "0";
    ImageButton buttonback;
    String activeDetails;
    TestList testList;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturecapture);
       testList=(TestList)getIntent().getSerializableExtra(C.TEST);
       activeDetails=getIntent().getStringExtra(C.ACTIVE_DETAILS);
        cardview = (CardView) findViewById(R.id.cardview);
       // cardviewidcard = (CardView) findViewById(R.id.cardviewidcard);
        imgicon = (ImageView) findViewById(R.id.imgicon);
        imgiconidcard = (ImageView) findViewById(R.id.imgiconidcard);
        btn_imagecapture = (ImageView) findViewById(R.id.btn_imagecapture);
        btn_imagecaptureidcard = (ImageView) findViewById(R.id.btn_imagecaptureidcard);
        img_photoidcard = (ImageView) findViewById(R.id.img_photoidcard);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        txt_userabsent = (TextView) findViewById(R.id.txt_userabsent);
        btn_next = (Button) findViewById(R.id.btn_next);
        buttonback = (ImageButton) findViewById(R.id.buttonback);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Picturecapture.this,ActivityInstruction.class);
                intent.putExtra(C.ACTIVE_DETAILS,activeDetails);
                intent.putExtra(C.TEST,testList);
                startActivity(intent);
                finish();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Picturecapture.this,StudentInstruction.class);
                intent.putExtra(C.ACTIVE_DETAILS,activeDetails);
                intent.putExtra(C.TEST,testList);
                startActivity(intent);
                finish();
            }
        });
        txt_userabsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Picturecapture.this);

                builder.setMessage("Are you sure you want to mark Absent" + " "+ Globalclass.ddusernameselected);
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
        btn_next.setEnabled(false);
    }

    public void savevaluesforabsent(){
        DatabaseHelper db = new DatabaseHelper(Picturecapture.this);
        boolean successval = db.insertData_tablebatchdetails(testList.getId(), Globalclass.userids, Globalclass.userids, Globalclass.userids, Globalclass.userids, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID(),activeDetails,"1");
        if(successval == true){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Picturecapture.this);
            builder.setMessage("Candidate" + Globalclass.ddusernameselected +" Marked Absent ");
            builder.setCancelable(false);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent    = new Intent(Picturecapture.this,ActivityInstruction.class);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filepath = null;
        byte[] bytes;
        // File pictureFileDir = getDir();
        CameraManager cm = new CameraManager(Picturecapture.this);
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
              //  SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
              //  String date = dateFormat.format(new Date());
                String date=Util.getCurrentTimeStamp();
                String photoFile = Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID()+"-"+Globalclass.idcandidate+"-"+"CdPic-"+testList.getId()+"_" + date + ".jpg";
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
                    if(capturetype.equalsIgnoreCase("photo")){
                        btn_imagecapture.setVisibility(View.GONE);
                        imgicon.setVisibility(View.VISIBLE);
                        img_photo.setVisibility(View.VISIBLE);
                        img_photo.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                        txt_userabsent.setVisibility(View.GONE);
                    }
                    else {
                        btn_imagecaptureidcard.setVisibility(View.GONE);
                        imgiconidcard.setVisibility(View.VISIBLE);
                        img_photoidcard.setVisibility(View.VISIBLE);
                        img_photoidcard.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                        txt_userabsent.setVisibility(View.GONE);
                    }
                    uploadBitmap(pictureFile.getPath(),Globalclass.idcandidate,photoFile,Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID() + "/" + Globalclass.idcandidate,"CdPic",photoFile);
                }
                else {
                    DatabaseHelper db = new DatabaseHelper(Picturecapture.this);
                    boolean success = db.insert_imagesval("CdPic","non",photoFile,testList.getId(),filepath,Globalclass.idcandidate,Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
                    if(success == true)
                    {
                        if(capturetype.equalsIgnoreCase("photo")){
                            btn_imagecapture.setVisibility(View.GONE);
                            imgicon.setVisibility(View.VISIBLE);
                            img_photo.setVisibility(View.VISIBLE);
                            img_photo.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                            txt_userabsent.setVisibility(View.GONE);
                            if(imgcount.equalsIgnoreCase("2")){
                                btn_next.setEnabled(true);
                            }
                        }
                        else {
                            btn_imagecaptureidcard.setVisibility(View.GONE);
                            imgiconidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setVisibility(View.VISIBLE);
                            img_photoidcard.setImageURI(Uri.parse("file://" + pictureFile.getPath()));
                            txt_userabsent.setVisibility(View.GONE);
                            if(imgcount.equalsIgnoreCase("2")){
                                btn_next.setEnabled(true);
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
     //   builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }

    private void uploadBitmap(final String imageurlval, final String tagidval, final String  nameimageval, final String targetid, final String imagetypeval, String nameimageorg) {
        final ProgressDialog Dialog1 = new ProgressDialog(Picturecapture.this);
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
                        if(imgcount.equalsIgnoreCase("2"))
                        {
                            btn_next.setEnabled(true);
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
                params.put("userId", SharedPreference.getInstance(Picturecapture.this).getUser(C.LOGIN_USER).getUserID());
                params.put("api_key",SharedPreference.getInstance(Picturecapture.this).getUser(C.LOGIN_USER).getApiKey());
                params.put("testID",testList.getId());
                params.put("uniqueID",testList.getUniqueID());
                params.put("picType",imagetypeval);
                params.put("candidate_id",tagidval);
                params.put("version","m");
                params.put("target_dir",targetid);
                params.put("filename",nameimageval);
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

}

