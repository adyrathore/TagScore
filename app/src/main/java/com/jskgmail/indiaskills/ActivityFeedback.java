package com.jskgmail.indiaskills;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityFeedback extends AppCompatActivity {

    ImageButton buttonback;
    ListView list;
    private TestList testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_feedback_imageview);
        buttonback = (ImageButton) findViewById(R.id.buttonback);
        list = (ListView) findViewById(R.id.list);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        String newString = null;
        newString = extras.getString("values");
        testList = (TestList) extras.getSerializable(C.TEST);
        getcaputurevalues(newString);
    }


    public File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
    }

    public void getcaputurevalues(String str) {
        ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
        ArrayList<String> login_name_arr = new ArrayList<>(), login_username_arr = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(ActivityFeedback.this);
        Cursor cursor = db.getAllVauesofimages(str, Util.ONLINE ? testList.getScheduleIdPk() : testList.getUniqueID());
        while (cursor.moveToNext()) {
            String type = cursor.getString(0);
            String nameval = cursor.getString(2);
            String urlval = cursor.getString(4);
            login_name_arr.add(cursor.getString(2));
            login_username_arr.add(cursor.getString(4));
            HashMap map = new HashMap<String, String>();
            if (str.equalsIgnoreCase(type)) {
                map.put("name", nameval);
                map.put("url", urlval);
                menuItems.add(map);
            }
        }
        ListAdapter k = null;
        if (str.equalsIgnoreCase("Assessment Video")) {
        } else {
            k = new SimpleAdapter(ActivityFeedback.this, menuItems, R.layout.item_pic, new String[]{"name", "name", "url"},
                    new int[]{R.id.item_text, R.id.filepath, R.id.item_image}) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    File pictureFileDir = getDir();
                    String filepath = null;
                    View v = super.getView(position, convertView, parent);
                    final View name = v.findViewById(R.id.item_text);
                    final View username = v.findViewById(R.id.filepath);
                    final View imagebtn = v.findViewById(R.id.item_image);
                    ImageView imagebtnval = (ImageView) imagebtn;
                    TextView nameval = (TextView) name;
                    final String name1 = nameval.getText().toString();
                    TextView pathval = (TextView) username;
                    final String username1 = pathval.getText().toString();
                    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                    // String date = dateFormat.format(new Date());
                    String photoFile = name1;
                    filepath = pictureFileDir.getPath() + File.separator + photoFile;
                    // .setImageURI(photoFile);
                    File pictureFile = new File(filepath);
                    // File pictureFile = new File(username1);
//                    Picasso.get().load(pictureFile).resize(50, 50).
//                            centerCrop().into(imagebtnval);
                    Picasso.with(ActivityFeedback.this).load(pictureFile).resize(60,60).centerInside().into(imagebtnval);
                    //   imagebtnval.setImageURI(Uri.parse("file://" + pictureFile));
                    //  imagebtnval.setImageURI(username1);
                    return v;
                }
            };
        }
        list.setAdapter(k);
    }

}

