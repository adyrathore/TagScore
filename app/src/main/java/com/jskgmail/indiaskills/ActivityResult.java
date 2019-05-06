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

        startTestOnline(testList);
//        result = "{\n" +
//                "    \"responseCode\": 200,\n" +
//                "    \"responseMessage\": \"This is candidate Result\",\n" +
//                "    \"result\": {\n" +
//                "        \"candidate_email\": \"amrjitkumarac596@gmail.com\",\n" +
//                "        \"candidate_name\": \"Amarjit Kumar\",\n" +
//                "        \"theory\": [\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which drawing would you generally refer for room dimensions?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"foundation plan\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037615,84.03977000000002\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563791\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Drafters, engineers, architects, and builders use a system known as orthographic projection. Which of the following statement will be true regarding it ?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Create drawings of three-dimensional buildings on two-dimensional paper.\",\n" +
//                "                \"marks_gain\": \"4\",\n" +
//                "                \"location\": \"26.037615,84.03977000000002\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563819\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following is not part of Site Plan?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Auxiliary Buildings\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037615,84.03977000000002\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563825\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the tool shown in below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Slide Caliper\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563834\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Rotary saw\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563839\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Wire gauge\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563848\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Fret saw\",\n" +
//                "                \"marks_gain\": \"2\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563853\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Heart wood\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563857\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which one of the following is used for boring deep hole in wood?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Wire gauge\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563861\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Marking gauge\",\n" +
//                "                \"marks_gain\": \"2\",\n" +
//                "                \"location\": \"26.03763666666667,84.03983500000001\",\n" +
//                "                \"boomarkedCount\": \"1\",\n" +
//                "                \"TimeStamp\": \"1552563992\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Sander\",\n" +
//                "                \"marks_gain\": \"1\",\n" +
//                "                \"location\": \"26.03760833333333,84.03983833333332\",\n" +
//                "                \"boomarkedCount\": \"1\",\n" +
//                "                \"TimeStamp\": \"1552564145\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Circular saw\",\n" +
//                "                \"marks_gain\": \"1\",\n" +
//                "                \"location\": \"26.03760666666667,84.039815\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564151\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"While sawing sight of eye should be\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Directly above the saw\",\n" +
//                "                \"marks_gain\": \"2\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564166\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Adze\",\n" +
//                "                \"marks_gain\": \"1\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564170\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Dock Leveler\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564174\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Where should you hold the hammer handle while\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"End of the hammer handle\",\n" +
//                "                \"marks_gain\": \"7\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564178\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Screw\",\n" +
//                "                \"marks_gain\": \"7\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564182\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which one of the following is gives the name of the screw?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Thread\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564186\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Box Sweeper\",\n" +
//                "                \"marks_gain\": \"7\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564196\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which tool is used to clean the surface of wood?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Steel scraper\",\n" +
//                "                \"marks_gain\": \"7\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564200\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What is Waste Disposal?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"The release of dirty particles into the air from industries, etc.\",\n" +
//                "                \"marks_gain\": \"8\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564206\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following is a material handling equipment?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Conveyors\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037610000000004,84.03982333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564209\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"To avoid noise at work place, which of the following tool you will use?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Ear Plug\",\n" +
//                "                \"marks_gain\": \"5\",\n" +
//                "                \"location\": \"26.03757666666667,84.03982833333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564212\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Safety goggle\",\n" +
//                "                \"marks_gain\": \"5\",\n" +
//                "                \"location\": \"26.03757666666667,84.03983666666666\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564216\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following is a content of first aid box\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Triangular bandages\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037578333333332,84.03983666666666\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564219\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"The best way to reduce risk is to _____________?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Wear the correct personal protective equipment\",\n" +
//                "                \"marks_gain\": \"5\",\n" +
//                "                \"location\": \"26.037578333333332,84.03985166666668\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564224\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Identify the below image:\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Safety nose mask\",\n" +
//                "                \"marks_gain\": \"5\",\n" +
//                "                \"location\": \"26.037578333333332,84.03985166666668\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564228\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Face shield is primarily used for____________\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Eye protection\",\n" +
//                "                \"marks_gain\": \"5\",\n" +
//                "                \"location\": \"26.037578333333332,84.03985166666668\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564232\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What should you do if you have some difference with your colleague?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Discuss and sort out difficulties in a polite and constructive way\",\n" +
//                "                \"marks_gain\": \"6\",\n" +
//                "                \"location\": \"26.037613333333336,84.03986333333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564243\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What do you understand by maintaining personal presentation?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Making the presentation of an assigned task\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03760833333333,84.03986833333332\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564245\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"_____ is considered unprofessional?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Tattoos on face\",\n" +
//                "                \"marks_gain\": \"6\",\n" +
//                "                \"location\": \"26.03760833333333,84.03986833333332\",\n" +
//                "                \"boomarkedCount\": \"1\",\n" +
//                "                \"TimeStamp\": \"1552564258\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"If your colleague asks for work related information then what should you do?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Give clear and accurate information\",\n" +
//                "                \"marks_gain\": \"6\",\n" +
//                "                \"location\": \"26.037628333333334,84.03981833333334\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564269\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following behaviour makes the customer lose respect?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"Good listening skills\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.03763666666667,84.03981833333334\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564272\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following image denotes Architect’s triangular scale .\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"d.\",\n" +
//                "                \"marks_gain\": \"0\",\n" +
//                "                \"location\": \"26.037629999999996,84.03978833333333\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564288\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Which of the following image regarding Electric Planner is correct?\",\n" +
//                "                \"type_of_question\": \"0\",\n" +
//                "                \"answer\": \"d.\",\n" +
//                "                \"marks_gain\": \"4\",\n" +
//                "                \"location\": \"26.03755833333333,84.03983000000001\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552564432\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"practical\": [\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"How to identify any shortage or defect in raw material and what steps needs to be taken to rectify this issue?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"20\",\n" +
//                "                \"location\": \"26.03759528,84.03986194\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563275\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What should we need to do if the skatches don’t meet the requirement as fore said ?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"20\",\n" +
//                "                \"location\": \"26.03759533,84.03986196\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563280\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Make the structure which is given by the assessor.\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"40\",\n" +
//                "                \"location\": \"26.03759561,84.03986189\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563284\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Demonstrate correct handling of materials, equipment and tools?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"19\",\n" +
//                "                \"location\": \"26.0375961,84.03986174\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563288\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What is role of SMOOTHING PLANE tool in furniture making and draw the diagram of this tool while showing the functioning of it\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"13\",\n" +
//                "                \"location\": \"26.03759631,84.03986175\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563292\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What should you do to maintain hygiene and clean standards?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"20\",\n" +
//                "                \"location\": \"26.03759651,84.03986172\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563297\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What are the hazards and potential risks associated with wooden carpenter work, list out any four.\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"24\",\n" +
//                "                \"location\": \"26.03759668,84.03986163\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563301\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"Why is it important to be well groomed?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"30\",\n" +
//                "                \"location\": \"26.03759667,84.03986153\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563307\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"unique_id\": \"bae049293876vsh112f5b9v4164edb5c\",\n" +
//                "                \"question\": \"What is courteous behavior and what is need to use it arises?\",\n" +
//                "                \"type_of_question\": \"1\",\n" +
//                "                \"answer\": null,\n" +
//                "                \"marks_gain\": \"8\",\n" +
//                "                \"location\": \"26.03759654,84.03986148\",\n" +
//                "                \"boomarkedCount\": \"0\",\n" +
//                "                \"TimeStamp\": \"1552563315\",\n" +
//                "                \"attemptCount\": \"1\"\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"obtain_marks\": 290,\n" +
//                "        \"total_marks\": 500,\n" +
//                "        \"result\": 1,\n" +
//                "        \"percentage\": 58,\n" +
//                "        \"user_nos_wise_details\": [\n" +
//                "            {\n" +
//                "                \"module_id_fk\": \"1892\",\n" +
//                "                \"module_name\": \"Assist in furniture planning and organizing work to meet expected outcome\",\n" +
//                "                \"gain_marks\": \"48\",\n" +
//                "                \"Total_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"module_id_fk\": \"1893\",\n" +
//                "                \"module_name\": \"Assist in furniture making\",\n" +
//                "                \"gain_marks\": \"49\",\n" +
//                "                \"Total_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"module_id_fk\": \"1894\",\n" +
//                "                \"module_name\": \"Maintain work area, tools and machines\",\n" +
//                "                \"gain_marks\": \"68\",\n" +
//                "                \"Total_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"module_id_fk\": \"1895\",\n" +
//                "                \"module_name\": \"Ensure health and safety at workplace\",\n" +
//                "                \"gain_marks\": \"69\",\n" +
//                "                \"Total_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"module_id_fk\": \"1896\",\n" +
//                "                \"module_name\": \"Work effectively with others\",\n" +
//                "                \"gain_marks\": \"56\",\n" +
//                "                \"Total_marks\": \"100\"\n" +
//                "            }\n" +
//                "        ],\n" +
//                "        \"User_Topic_Vice_Report\": [\n" +
//                "            {\n" +
//                "                \"topic_id_fk\": \"8763\",\n" +
//                "                \"topic_name\": \"understand the nature of work & requirement in terms of style\",\n" +
//                "                \"gain_marks\": \"48\",\n" +
//                "                \"question_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"topic_id_fk\": \"8764\",\n" +
//                "                \"topic_name\": \"cut the wood as per the specified measurements using appropriate tools\",\n" +
//                "                \"gain_marks\": \"49\",\n" +
//                "                \"question_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"topic_id_fk\": \"8765\",\n" +
//                "                \"topic_name\": \"handle materials, machinery, equipment and tools safely and correctly\",\n" +
//                "                \"gain_marks\": \"68\",\n" +
//                "                \"question_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"topic_id_fk\": \"8766\",\n" +
//                "                \"topic_name\": \"follow safe working practices at all times\",\n" +
//                "                \"gain_marks\": \"69\",\n" +
//                "                \"question_marks\": \"100\"\n" +
//                "            },\n" +
//                "            {\n" +
//                "                \"topic_id_fk\": \"8767\",\n" +
//                "                \"topic_name\": \"seek and obtain clarifications on policies and procedures, from the supervisor or other authorized personnel\",\n" +
//                "                \"gain_marks\": \"56\",\n" +
//                "                \"question_marks\": \"100\"\n" +
//                "            }\n" +
//                "        ]\n" +
//                "    }\n" +
//                "}";




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
