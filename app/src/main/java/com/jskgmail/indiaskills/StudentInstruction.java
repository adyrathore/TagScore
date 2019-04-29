package com.jskgmail.indiaskills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jskgmail.indiaskills.adpater.AdapterInstructions;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.Instruction;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentInstruction extends AppCompatActivity {
    Button btnTestStart;
    TextView view;
    Spinner dd_tagid;
    LocationManager locationManager;
    Context context;
    CardView cardviewinstruction;
    TextView txt_instructionbv, txt_selectlanguage;
    boolean GpsStatus;
    TestList testList;
    String activeDetails;
    private AdapterInstructions adapterInstructions;

    public void GPSStatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_instruction);
        view = (TextView) findViewById(R.id.txt_instruction);
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        txt_instructionbv = (TextView) findViewById(R.id.txt_instructionbv);
        txt_selectlanguage = (TextView) findViewById(R.id.txt_selectlanguage);
        btnTestStart = (Button) findViewById(R.id.btn_question);

        context = getApplicationContext();
        cardviewinstruction = (CardView) findViewById(R.id.cardviewinstruction);
        btnTestStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSStatus();
                Globalclass.tempQuestionNo = 0;
                if (GpsStatus == true) {
                    Intent intent = new Intent(StudentInstruction.this, TestQuestionDisplayActivity.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                    finish();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(StudentInstruction.this);

                    builder.setMessage("Your GPS is not Enabled Please Enable it for processing further");
                    builder.setCancelable(false);
                    builder.setNeutralButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent1);

                            //   Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                }

            }
        });
        dd_tagid = (Spinner) findViewById(R.id.dd_tagid);

        getinstruction();
    }

    public void getinstructionhn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  view.setText(Html.fromHtml(Globalclass.instructionhn, Html.FROM_HTML_MODE_LEGACY));
            view.setText(Globalclass.instructionhn);
        } else
            //   view.setText(Html.fromHtml(Globalclass.instructionhn));
            view.setText(Globalclass.instructionhn);
    }

    public void getinstruction() {
        String json = "";

        if (Util.ONLINE) {
            json = SharedPreference.getInstance(StudentInstruction.this).getString(C.ONLINE_TEST_LIST);
        } else {
            DatabaseHelper mydb = new DatabaseHelper(this);
            Cursor resss = mydb.gettest_details_json_string(testList.getId(), Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
            if (resss.getCount() == 0) {
                // show message
                Util.showMessage(StudentInstruction.this, "Nothing found");
                return;
            }
            //buffer.append("Id :"+ res.getString(2));
            resss.moveToFirst();
            json = resss.getString(1);
        }
        try {
            String res = json.toString();
            //  JSONParser parser = new JSONParser();
            //  JSONObject json = (JSONObject) parser.parse(res);
            JSONObject array = (new JSONObject(res)).getJSONObject("data");
            //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz5Pqfn75JI
            //JSONObject array=json.getJSONObject("data");

            JSONObject jsonobj_2 = (JSONObject) array;
            String instructions = "";
            String languageName="";
            String languageCode="";

            JSONArray subObjDetails = jsonobj_2.getJSONArray("Instructions");
            Instruction[] instructions1=new Instruction[subObjDetails.length()];
            for (int j = 0; j < subObjDetails.length(); j++) {
                JSONObject jsonobj_2_answer = (JSONObject) subObjDetails.get(j);
                instructions = jsonobj_2_answer.get("Instruction").toString();
                languageName = jsonobj_2_answer.get("language_name").toString();
                languageCode = jsonobj_2_answer.get("language_code").toString();

                instructions1[j]= new Instruction(languageName,languageCode,instructions);
                //String test_descriptions = jsonobj_2_answer.get("test_descriptions").toString();
//                view.setText(Html.fromHtml(instructions));
            }
            adapterInstructions =new AdapterInstructions(StudentInstruction.this,android.R.layout.simple_spinner_dropdown_item,instructions1);
            dd_tagid.setAdapter(adapterInstructions);
            dd_tagid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        StudentInstruction.this.view.setText(Html.fromHtml(adapterInstructions.getItem(i).getInstruction(),Html.FROM_HTML_MODE_LEGACY));

                    } else {
                        StudentInstruction.this.view.setText(Html.fromHtml(adapterInstructions.getItem(i).getInstruction()));
                    }

                    if (adapterInstructions.getItem(i).getLanguage_name().equalsIgnoreCase("English")) {
                        Globalclass.spinnerstringlang = "en";
                        btnTestStart.setText(R.string.StartTest);
                        txt_instructionbv.setText(R.string.Instruction);
                        txt_selectlanguage.setText(R.string.SelectLanguage);
                    }
                    else {

                        Globalclass.spinnerstringlang = "hn";
                        btnTestStart.setText(R.string.StartTesthn);
                        txt_instructionbv.setText(R.string.instructionhn);
                        txt_selectlanguage.setText(R.string.SelectLanguagehn);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            dd_tagid.setSelection(0);

//            if (instructions.equalsIgnoreCase("")) {
//                view.setText("");
//            } else {
//              //  String str = instructions.replace("<li>", "<br/>");
//              //  String str1 = str.replace("</li>", "<br/>");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    view.setText(Html.fromHtml(instructions, Html.FROM_HTML_MODE_LEGACY));
//                } else
//                    view.setText(Html.fromHtml(instructions));
//                // view.setText(Html.fromHtml(instructions));
//
//              //  webView.loadDataWithBaseURL(null, instructions, "text/html", "utf-8", null);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
