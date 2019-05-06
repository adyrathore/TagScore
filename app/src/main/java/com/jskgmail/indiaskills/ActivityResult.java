package com.jskgmail.indiaskills;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jskgmail.indiaskills.adpater.AdapterNosWiseResult;
import com.jskgmail.indiaskills.adpater.AdapterTheoryWiseResult;
import com.jskgmail.indiaskills.adpater.AdapterTopicWiseResult;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.ResponseResult;
import com.jskgmail.indiaskills.pojo.TestDetailRequest;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.pojo.TestResultRequest;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.MyListView;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;
import com.jskgmail.indiaskills.webservice.IResult;
import com.jskgmail.indiaskills.webservice.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityResult extends Activity {

    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.tvPercentage)
    TextView tvPercentage;
    @BindView(R.id.btnClose)
    Button btnClose;
    @BindView(R.id.lvModuleWise)
    MyListView lvModuleWise;
    @BindView(R.id.lvTopicWise)
    MyListView lvTopicWise;
    @BindView(R.id.lvTheoryWise)
    MyListView lvTheoryWise;
    private Dialog progressDialog;
    private TestList testList;
    private String result;
    private String activeDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);

        sv.setVisibility(View.GONE);
        startTestOnline(testList);
//        result =




        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityResult.this,  ActivityThankyou.class);

                intent.putExtra(C.TEST, testList);
                intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setListViewHeightBasedOnChildren(lvModuleWise);
//        setListViewHeightBasedOnChildren(lvTopicWise);
//        setListViewHeightBasedOnChildren(lvTheoryWise);
    }

    public void startTestOnline(final TestList testList) {

        progressDialog = Util.getProgressDialog(this, R.string.please_wait);
        progressDialog.show();
        final ResponseLogin responseLogin = SharedPreference.getInstance(this).getUser(C.LOGIN_USER);
        final TestResultRequest testDetailRequest = new TestResultRequest();
        testDetailRequest.setApiKey(responseLogin.getApiKey());
        testDetailRequest.setUserId(Globalclass.idcandidate);
        testDetailRequest.setCandidateId(Globalclass.idcandidate);
        testDetailRequest.setUniqueId(testList.getUniqueID());
        testDetailRequest.setLanguageCode(C.LANGUAGE);
        Gson gson = new Gson();
        String json = gson.toJson(testDetailRequest);
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
                    Log.e("Data response", String.valueOf(response.toString()));
                    Gson gson = new Gson();
                    ResponseResult responseResult = gson.fromJson(response, ResponseResult.class);

                    AdapterNosWiseResult adapterNosWiseResult = new AdapterNosWiseResult(ActivityResult.this, responseResult.getResult().getUserNosWiseDetails());

                    lvModuleWise.setAdapter(adapterNosWiseResult);
                    AdapterTopicWiseResult adapterTopicWiseResult = new AdapterTopicWiseResult(ActivityResult.this, responseResult.getResult().getUserTopicViceReport());
//        lvTopicWise.setLayoutManager(new LinearLayoutManager(this));

                    lvTopicWise.setAdapter(adapterTopicWiseResult);

                    AdapterTheoryWiseResult adapterTheoryWiseResult = new AdapterTheoryWiseResult(ActivityResult.this, responseResult.getResult().getTheory());
//        lvTheoryWise.setLayoutManager(new LinearLayoutManager(this));
                    lvTheoryWise.setAdapter(adapterTheoryWiseResult);


                    tvName.setText(responseResult.getResult().getCandidateName());
                    tvResult.setText(responseResult.getResult().getResult().equals(1) ? "Pass" : "Fail");
                    tvResult.setBackgroundColor(responseResult.getResult().getResult().equals(1) ? Color.parseColor("#11be70") : Color.RED);

                    tvGrandTotal.setText("Obtained Marks : " + responseResult.getResult().getObtainMarks() + "/" + responseResult.getResult().getTotalMarks());
                    tvPercentage.setText("Percentage : " + responseResult.getResult().getPercentage());
                    sv.setVisibility(View.VISIBLE);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
            }
        }, "getTestDetails", C.API_TEST_RESULT, Util.getHeader(), obj);

    }


}
