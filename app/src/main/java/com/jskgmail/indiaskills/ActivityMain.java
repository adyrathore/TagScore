package com.jskgmail.indiaskills;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.RequestLogin;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;
import com.jskgmail.indiaskills.webservice.IResult;
import com.jskgmail.indiaskills.webservice.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivityMain extends AppCompatActivity {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.tvVersionCode)
    TextView tvVersionCode;
    private Dialog progressDialog;

    //TODO remove
    public static boolean online;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
//            etName.setText("express.skills@gmail.com");
//            etPassword.setText("7014680058");
            etName.setText("zlvn0");
            etPassword.setText("55261");
            etName.setText("mukesh.pandey@indiaskills.edu.in");
            etPassword.setText("ispl@123");


        }
        if (BuildConfig.FLAVOR_app.equals(C.MEPSCLogo)) {
            imageView2.setImageResource(R.drawable.mepsc_logo);
        } else {
            imageView2.setImageResource(R.drawable.logo);

        }
        try {
            tvVersionCode.setText("Version "+Util.getVersionInfo(this));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked() {

        String username = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!Util.isNetworkAvailable(ActivityMain.this)) {

            DatabaseHelper databaseHelper = new DatabaseHelper(ActivityMain.this);
            boolean str = databaseHelper.checkUser(username, password);
            if (str) {
                Util.ONLINE = false;
                Intent accountsIntent = new Intent(ActivityMain.this, ActivityTestList.class);
                startActivity(accountsIntent);
                finish();
            } else {
                Util.showMessage(ActivityMain.this, R.string.login_not_exist);
            }

        } else {
            doLogin(username, password);
        }
    }


    protected void doLogin(final String name, final String password) {
        progressDialog = Util.getProgressDialog(this, R.string.please_wait);
        progressDialog.show();
        final RequestLogin requestLogin = new RequestLogin();
        requestLogin.setUsername(name);
        requestLogin.setPassword(password);
        requestLogin.setDeviceToken(C.IMEI);
        requestLogin.setDeviceType(C.ANDROID);
        Gson gson = new Gson();
        String json = gson.toJson(requestLogin);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyService volleyService = new VolleyService(this);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("responseMessage") != null && jsonObject.getString("responseMessage").equalsIgnoreCase("Password is incorrect")) {
                        Util.showMessage(ActivityMain.this, "Password is incorrect");

                    } else {
                        Gson gson = new Gson();
                        ResponseLogin responseLogin = gson.fromJson(response, ResponseLogin.class);
                        if (responseLogin.getResponseMessage().equalsIgnoreCase("Login successfully") || responseLogin.getResponseMessage().equalsIgnoreCase("Already login")) {
                            Util.ONLINE = true;
                            Globalclass.userids = responseLogin.getUserID();
                            Globalclass.roleval = responseLogin.getProfile().get(0).getRole();

                            SharedPreference.getInstance(ActivityMain.this).setUser(C.LOGIN_USER, responseLogin);
                            SharedPreference.getInstance(ActivityMain.this).setString(C.USERNAME, etName.getText().toString());
                            SharedPreference.getInstance(ActivityMain.this).setString(C.PASSWORD, etPassword.getText().toString());
                            Intent intent = new Intent(ActivityMain.this, ActivityTestList.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Util.showMessage(ActivityMain.this, responseLogin.getResponseMessage());
                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
            }
        }, "Login", C.API_LOGIN, Util.getHeader(), obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
