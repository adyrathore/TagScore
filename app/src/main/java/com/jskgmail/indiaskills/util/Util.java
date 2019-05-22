package com.jskgmail.indiaskills.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.TestQuestionDisplayActivity;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Util {

    public static boolean ONLINE=true;

    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no value";
        }
        return telephonyManager.getDeviceId();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static Dialog getProgressDialog(Context context, int msg) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.tvMsg);
        text.setText(context.getResources().getString(msg));
        return dialog;
    }

    public static void setImage(ImageView image,Context context,String fileName,String id){
        String fi=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+id;
        File imgFile = new  File(fi,fileName);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            image.setImageBitmap(myBitmap);

        }
    }

    public static long getInternalStorageSpace(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        }
        else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        }
      //  long megAvailable = bytesAvailable / (1024 * 1024);
     //   Log.e("","Available MB : "+megAvailable);
        return bytesAvailable;
    }

    public static long getInternalStorageSpaceInMB(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        }
        else {
            bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        }
          long megAvailable = bytesAvailable / (1024 * 1024);
        //   Log.e("","Available MB : "+megAvailable);
        return megAvailable;
    }

    public static Uri getVideoUrl( Context context, String fileName, String id){

        Uri uri=Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+id+File.separator+fileName);
        return uri;
    }
    public static Map<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        String credentials = "tagusp:t@g$c0re";
        String auth = "Basic" + " " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }
   public static String getRecTime(String startTime, String endTime) {

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

    public static void showMessage(Context context, int message) {
        showMessage(context, context.getResources().getString(message));
    }

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
       // builder.setTitle(R.string.alert);
        builder.setPositiveButton(R.string.ok, null);
        builder.setMessage(message);
        builder.show();
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void enableGPS(final Activity activity) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);

        builder.setMessage(R.string.enable_gps);
        builder.setCancelable(false);
        builder.setNeutralButton(R.string.enable, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent1);

            }
        });
        builder.show();
    }

    public static JSONArray cur2Json(Cursor cursor) {

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

    public static void loadImage(String s, ImageView imgvalues) {
        try {
            byte[] decodedString = Base64.decode(s.substring(s.indexOf(",") + 1), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgvalues.setImageBitmap(decodedByte);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  static String getCurrentTimeStamp(){
        return""+ System.currentTimeMillis() / 1000L;

    }


    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date date = new Date();
        String timeStamp = formatter.format(date);
        return timeStamp;
        //      return "" + new Date();
    }
    public static boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        try {
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
            Log.i("isMyServiceRunning?", false + "");
            return false;
        } catch (Exception e) {

        }
        return false;
    }
    public static HostnameVerifier getHostname(){
        return   new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify(C.BASE_URL, session);
            }
        };
    }

    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {

                    if (C.BASE_URL.contains(arg0)) {
                        //   System.out.println("HostName " + arg0);

                        return true;
                    } else {
                        // System.out.println("HostName check kr\n\n\n\n\n\n\n\ncheck kr" + arg0);
                        return false;
                    }
                }
            });
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

        private static int BUFFER_SIZE = 6 * 1024;

        public static void unzip(String zipFile, String location)  {
            try {
                File f = new File(location);
                if (!f.isDirectory()) {
                    f.mkdirs();
                }
                ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
                try {
                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String path = location + File.separator + ze.getName();

                        if (ze.isDirectory()) {
                            File unzipFile = new File(path);
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            FileOutputStream fout = new FileOutputStream(path, false);

                            try {
                                for (int c = zin.read(); c != -1; c = zin.read()) {
                                    fout.write(c);
                                }
                                zin.closeEntry();
                            } finally {
                                fout.close();
                            }
                        }
                    }
                } finally {
                    zin.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DEBUG", "Unzip exception", e);
            }
        }

  public static int getRandomValue(int max){
      return new Random().nextInt((max - 1) + 1) + 1;
  }

    public static String getVersionInfo(Context  context) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

       return versionName+"("+versionCode+")";
    }
    public static int getVersionCode(Context  context) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }
  public static   JSONObject getScheduleSettings(String json) {

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

    public static String getJson(Context context, TestList testList){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        String json;
        if (Util.ONLINE) {
            json = SharedPreference.getInstance(context).getString(C.ONLINE_TEST_LIST);
        } else {
            Cursor resss = databaseHelper.gettest_details_json_string(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
              //  showMessage("Error", "Nothing found");
                return "";
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        return json;
    }

}
