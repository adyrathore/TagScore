package com.jskgmail.indiaskills.adpater;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jskgmail.indiaskills.ActivityTestList;
import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.TestDetails;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.TestList;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;
import com.jskgmail.indiaskills.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aditya.singh on 12/12/2018.
 */

public class AdapterExamList extends BaseAdapter {

    private final DatabaseHelper databaseHelper;
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<TestList> testLists;

    public AdapterExamList(Activity activity, ArrayList<TestList> testLists) {
        this.activity = activity;
        this.testLists = testLists;
        layoutInflater = LayoutInflater.from(activity);
        databaseHelper = new DatabaseHelper(activity);
    }

    @Override
    public int getViewTypeCount() {
        return testLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return testLists.size();
    }

    @Override
    public TestList getItem(int i) {
        return testLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_test, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final TestList testList = getItem(i);
        viewHolder.tvTestName.setText(testList.getTestName());

        if (Util.ONLINE) {
            boolean isTestCompleted = databaseHelper.isTestCompletedOrNot(testList.getScheduleIdPk());
            boolean isTestDownloaded = databaseHelper.getAllDate_batchid(testList.getScheduleIdPk());
            viewHolder.btnStartTest.setVisibility(View.VISIBLE);

//            if (testList.getTestType().equals(C.OFFLINE)) {

                if (!isTestDownloaded && Util.ONLINE) {
                    viewHolder.ivTestStatus.setVisibility(View.GONE);
                    viewHolder.btnOperation.setText(R.string.download);
                    viewHolder.btnOperation.setEnabled(true);
                } else if (isTestDownloaded && !isTestCompleted && Util.ONLINE) {
                    viewHolder.ivTestStatus.setVisibility(View.GONE);
                    viewHolder.btnOperation.setText(R.string.downloaded);
                    viewHolder.btnOperation.setEnabled(false);
                    viewHolder.btnOperation.setBackgroundResource(R.drawable.unselected_grey);
                } else if (isTestDownloaded && isTestCompleted && Util.ONLINE) {
                    viewHolder.btnOperation.setEnabled(true);
                    viewHolder.ivTestStatus.setVisibility(View.VISIBLE);
                    viewHolder.btnOperation.setText(R.string.upload);
                    viewHolder.btnOperation.setEnabled(true);

                }

//            } else if (testList.getTestType().equals(C.ONLINE)) {

//                if (!isTestCompleted) {
//                    viewHolder.ivTestStatus.setVisibility(View.GONE);
//                    viewHolder.btnOperation.setText(R.string.start_test);
//                }
                else {
                    viewHolder.ivTestStatus.setVisibility(View.VISIBLE);
                    viewHolder.btnOperation.setText(R.string.upload);
                }


//            }
        } else {
            viewHolder.btnStartTest.setVisibility(View.GONE);
            if (testList.getPurchasedTime().equals("1")) {

                viewHolder.ivTestStatus.setVisibility(View.VISIBLE);
                viewHolder.btnOperation.setText(R.string.upload);
                viewHolder.btnOperation.setEnabled(false);
            } else {
                viewHolder.ivTestStatus.setVisibility(View.GONE);
                viewHolder.btnOperation.setText(R.string.take_test);
                viewHolder.btnOperation.setEnabled(true);
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) viewHolder.btnOperation.getLayoutParams();
                param.weight=2.0f;
                viewHolder.btnOperation.setLayoutParams(param);
            }
        }

        viewHolder.btnOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.isGPSEnabled(activity)) {

                    SharedPreference.getInstance(activity).setTest(C.ONGOING_TEST, testList);
                    if (viewHolder.btnOperation.getText().equals(activity.getResources().getText(R.string.download))) {
                        ((ActivityTestList) activity).downloadTestQuestionaryDetails(testList);
                        // ((ActivityTestList) activity).zipFileDownload(testList);

                    } else if (viewHolder.btnOperation.getText().equals(activity.getResources().getText(R.string.upload))) {
                        ((ActivityTestList) activity).uploadRecords(testList);
                    } else if (viewHolder.btnOperation.getText().equals(activity.getResources().getText(R.string.start_test))) {
                        ((ActivityTestList) activity).startTestOnline(testList);

                    } else if (viewHolder.btnOperation.getText().equals(activity.getResources().getString(R.string.take_test))) {
                        Intent intent = new Intent(activity, TestDetails.class);
                        intent.putExtra(C.TEST_ID, testList.getId());
                        intent.putExtra(C.TEST, testList);
                        intent.putExtra(C.SCHEDULE_ID_PK, testList.getUniqueID());
                        activity.startActivity(intent);
                    }

                } else {
                    Util.enableGPS(activity);
                }
            }
        });
        viewHolder.btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.isGPSEnabled(activity)) {

                    SharedPreference.getInstance(activity).setTest(C.ONGOING_TEST, testList);
                   ((ActivityTestList) activity).startTestOnline(testList);


                } else {
                    Util.enableGPS(activity);
                }
            }
        });

        return view;
    }

    private void downloadQuestionsFromServer() {
    }


    static class ViewHolder {
        @BindView(R.id.ivTestStatus)
        ImageView ivTestStatus;
        @BindView(R.id.tvTestName)
        TextView tvTestName;
        @BindView(R.id.btnOperation)
        Button btnOperation;
        @BindView(R.id.btnStartTest)
        Button btnStartTest;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
