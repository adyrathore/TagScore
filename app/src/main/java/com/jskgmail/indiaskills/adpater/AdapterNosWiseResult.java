package com.jskgmail.indiaskills.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.pojo.UserNosWiseDetail;
import com.jskgmail.indiaskills.util.C;

import java.util.ArrayList;

public class AdapterNosWiseResult extends BaseAdapter {
    private final LayoutInflater inflater;
    Context context;
    ArrayList<UserNosWiseDetail> userNosWiseDetails;



    public AdapterNosWiseResult(Context context, ArrayList<UserNosWiseDetail> userNosWiseDetails) {
        this.context = context;
        this.userNosWiseDetails = userNosWiseDetails;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }







    @Override
    public int getCount() {
        return (userNosWiseDetails == null) ? 0 : userNosWiseDetails.size();
    }

    @Override
    public UserNosWiseDetail getItem(int i) {
        return userNosWiseDetails.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       ViewHolder holder;
        if (view == null) {
            view= inflater.inflate(R.layout.item_module_wise, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvModuleName.setText(userNosWiseDetails.get(position).getModuleName());
        holder.tvTotalMarks.setText("Total Marks : "+userNosWiseDetails.get(position).getTotalMarks());
        holder.tvObtainedMarks.setText("Obtain Marks : "+userNosWiseDetails.get(position).getGainMarks());
        if(userNosWiseDetails.get(position).getGainMarks()==null)
        {
            holder.tvObtainedMarks.setText("Obtain Marks : 0" );

        }
        return view;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvModuleName;
        TextView tvTotalMarks;
        TextView tvObtainedMarks;

        public ViewHolder(View itemView) {
            super(itemView);
            tvModuleName = itemView.findViewById(R.id.tvModuleName);
            tvTotalMarks = itemView.findViewById(R.id.tvTotalMarks);
            tvObtainedMarks = itemView.findViewById(R.id.tvObtainedMarks);

        }

    }


}
