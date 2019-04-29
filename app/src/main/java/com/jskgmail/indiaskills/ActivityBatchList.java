package com.jskgmail.indiaskills;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityBatchList extends AppCompatActivity implements View.OnClickListener{
    ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
    static ArrayList<String> login_name_arr = new ArrayList<>(), login_username_arr = new ArrayList<>();
    TestList testList;
    @BindView(R.id.backtodetails)
    ImageButton backtodetails;
    @BindView(R.id.textViewName)
    AppCompatTextView textViewName;
    @BindView(R.id.recyclerview)
    ListView recyclerView;
    @BindView(R.id.next)
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);
        ButterKnife.bind(this);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        backtodetails.setOnClickListener(this);
        ResponseLogin responseLogin = SharedPreference.getInstance(ActivityBatchList.this).getUser(C.LOGIN_USER);

        recyclervie(Globalclass.userids, responseLogin.getApiKey());
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityBatchList.this, ActivityTestStart.class));
                finish();

            }
        });
    }


    void recyclervie(final String name, final String password) {

        DatabaseHelper mydb = new DatabaseHelper(this);
        String json = "";
        if (Util.ONLINE) {
            json = SharedPreference.getInstance(ActivityBatchList.this).getString(C.ONLINE_TEST_LIST);
        } else {
            Cursor resss = mydb.gettest_details_json_string(testList.getId(), testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
                return;
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        try {
            String batchId, batchName, uniqueID, testID, login_name, login_username, login_email;
            String res = json.toString();
            //  JSONParser parser = new JSONParser();
            //  JSONObject json = (JSONObject) parser.parse(res);
            JSONObject array = (new JSONObject(res)).getJSONObject("data");
            //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
            //JSONObject array=json.getJSONObject("data");

            JSONObject jsonobj_2 = (JSONObject) array;
            JSONArray subObjDetails = jsonobj_2.getJSONArray("batch_details");
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                String test_name = jsonobj_2_answer.get("name").toString();
                String test_Details = jsonobj_2_answer.get("username").toString();
                //String test_descriptions = jsonobj_2_answer.get("test_descriptions").toString();
                //batch=userList.getJSONObject(i);
                login_name = jsonobj_2_answer.get("name").toString();
                login_username = jsonobj_2_answer.get("username").toString();
                login_email = "";
                login_name_arr.add(login_name);
                login_username_arr.add(login_username);
                HashMap map = new HashMap<String, String>();
                map.put("login_name", login_name);
                map.put("login_username", login_username);

                menuItems.add(map);

            }

            ListAdapter k;
            k = new SimpleAdapter(ActivityBatchList.this, menuItems, R.layout.list_batch,
                    new String[]{"login_name", "login_username"},
                    new int[]{R.id.name, R.id.username}) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    final View name = v.findViewById(R.id.name);
                    final View username = v.findViewById(R.id.username);
                    TextView tv_Date = (TextView) name;
                    final String name1 = tv_Date.getText().toString();
                    TextView tv_Date1 = (TextView) username;
                    final String username1 = tv_Date.getText().toString();
                    return v;
                }
            };
            recyclerView.setAdapter(k);

            //  Log.e("arrgrapikjjloginnaa", String.valueOf(login_name_arr));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
      //  builder.setTitle(title);
        builder.setPositiveButton("OK", null);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backtodetails:
                onBackPressed();
                break;
        }
    }
}
