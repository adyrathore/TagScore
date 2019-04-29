package com.jskgmail.indiaskills;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.Util;

public class ActivityThankyou extends AppCompatActivity {

    Button btn;
    TextView txtthanks;
    TestList testList;
    String activeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyouscreen);
        Globalclass.guestioncount = 0;
        testList = (TestList) getIntent().getSerializableExtra(C.TEST);
        activeDetails = getIntent().getStringExtra(C.ACTIVE_DETAILS);
        btn = (Button) findViewById(R.id.btn_next);
        txtthanks = (TextView) findViewById(R.id.txtthanks);
        if(Util.ONLINE){
            DatabaseHelper myDb = new DatabaseHelper(this);
            boolean success = myDb.delete_byID(testList.getId(),Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globalclass.roleval.equalsIgnoreCase("3")) {
                    Intent intent = new Intent(ActivityThankyou.this, ActivityTestList.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ActivityThankyou.this, ActivityInstruction.class);
                    intent.putExtra(C.TEST, testList);
                    intent.putExtra(C.ACTIVE_DETAILS, activeDetails);
                    startActivity(intent);
                    finish();
                }
            }
        });

        if (Globalclass.spinnerstringlang.equalsIgnoreCase("en")) {
            txtthanks.setText(R.string.thankyou);
            btn.setText(R.string.Close);
        } else {
            txtthanks.setText(R.string.thankyouhn);
            btn.setText(R.string.Closehn);
        }
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
}
