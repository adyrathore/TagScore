package com.jskgmail.indiaskills.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.pojo.Theory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterTheoryWiseResult extends BaseAdapter {
    Context context;
    ArrayList<Theory> theories;
    private final LayoutInflater inflater;


    public AdapterTheoryWiseResult(Context context, ArrayList<Theory> theories) {
        this.context = context;
        this.theories = theories;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }




    @Override
    public int getCount() {
        return (theories == null) ? 0 : theories.size();
    }

    @Override
    public Theory getItem(int i) {
        return theories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {


        ViewHolder holder;
        if (view == null) {
            view= inflater.inflate(R.layout.item_theory_wise, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvQuestion.setText(theories.get(position).getQuestion());
        holder.tvAnswer.setText(theories.get(position).getAnswer());
//        if(theories.get(position).get)
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(theories.get(position).getTimeStamp())*1000L);
        String date = DateFormat.format("dd-MMM-yyyy hh:mm", cal).toString();
        holder.tvTimeStap.setText(date);
        holder.tvAttemptCount.setText("Attempt Count : " + theories.get(position).getAttemptCount());
        holder.tvObtainedMarks.setText("Obtain Marks : " + theories.get(position).getMarksGain());
        if("0".equals(theories.get(position).getMarksGain())||theories.get(position).getMarksGain()==null)
        {
            holder.tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.wrong,0);

        }
        else {
            holder.tvAnswer.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.right, 0);

        }
        if(theories.get(position).getMarksGain()==null)
        {
            holder.tvObtainedMarks.setText("Obtain Marks : 0" );
            holder.tvAnswer.setText("N/A");


        }

        return view;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        TextView tvAttemptCount;
        TextView tvObtainedMarks;
        TextView tvAnswer;
        TextView tvTimeStap;


        public ViewHolder(View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvTimeStap = itemView.findViewById(R.id.tvTimeStap);
            tvAttemptCount = itemView.findViewById(R.id.tvTotalMarks);
            tvObtainedMarks = itemView.findViewById(R.id.tvObtainedMarks);

        }

    }


}
