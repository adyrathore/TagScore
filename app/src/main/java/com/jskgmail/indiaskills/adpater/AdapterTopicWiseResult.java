package com.jskgmail.indiaskills.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.pojo.UserTopicViceReport;
import com.jskgmail.indiaskills.util.C;

import java.util.ArrayList;

public class AdapterTopicWiseResult extends BaseAdapter {

    private final LayoutInflater inflater;
    Context context;
    ArrayList<UserTopicViceReport> userTopicViceReports;


    public AdapterTopicWiseResult(Context context, ArrayList<UserTopicViceReport> UserTopicViceReports) {
        this.context = context;
        this.userTopicViceReports = UserTopicViceReports;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }








    @Override
    public int getCount() {
        return (userTopicViceReports == null) ? 0 : userTopicViceReports.size();
    }

    @Override
    public Object getItem(int i) {
        return userTopicViceReports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view= inflater.inflate(R.layout.item_topic_wise, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvTopicName.setText(userTopicViceReports.get(position).getTopicName());
        holder.tvTotalMarks.setText("Total Marks : "+ userTopicViceReports.get(position).getQuestionMarks());
        holder.tvObtainedMarks.setText("Obtain Marks : "+ userTopicViceReports.get(position).getGainMarks());
        return view;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTopicName;
        TextView tvTotalMarks;
        TextView tvObtainedMarks;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tvTopicName);
            tvTotalMarks = itemView.findViewById(R.id.tvTotalMarks);
            tvObtainedMarks = itemView.findViewById(R.id.tvObtainedMarks);

        }

    }


}
