package com.jskgmail.indiaskills.adpater;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.util.Util;

public class AdapterCusmtomSpinner extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;
    String[] spinnerPopulation;
    Context mContext;

    public AdapterCusmtomSpinner(@NonNull Context context, String[] titles, int[] images, String[] population) {
        super(context, R.layout.custom_spinner_row);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.spinnerPopulation = population;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);

    }

    @Override
    public int getCount() {
        return spinnerTitles.length + 1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_row, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tvName);
            mViewHolder.mPopulation = (TextView) convertView.findViewById(R.id.tvPopulation);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mPopulation.setVisibility(View.GONE);
        if (position == 0) {
            mViewHolder.mFlag.setVisibility(View.GONE);
            mViewHolder.mName.setText("Select Candidate");
            mViewHolder.mName.setTextColor(Color.DKGRAY);
            return convertView;
        }
        else
            mViewHolder.mFlag.setVisibility(View.VISIBLE);

        position = position - 1;
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);
        mViewHolder.mPopulation.setText(spinnerPopulation[position]);
        mViewHolder.mPopulation.setVisibility(View.GONE);
        String str = spinnerPopulation[position].toString();
        if (Util.ONLINE) {
            if (str.equalsIgnoreCase("1")) {
                mViewHolder.mFlag.setVisibility(View.VISIBLE);
                mViewHolder.mName.setTextColor(Color.rgb(10, 10, 101));

            } else {
                mViewHolder.mFlag.setVisibility(View.VISIBLE);
                mViewHolder.mName.setEnabled(false);
                mViewHolder.mName.setTextColor(Color.GRAY);
            }
        } else {
            if (str.equalsIgnoreCase("0")) {
                mViewHolder.mFlag.setVisibility(View.VISIBLE);
                mViewHolder.mName.setTextColor(Color.rgb(10, 10, 101));

            } else {
                mViewHolder.mFlag.setVisibility(View.VISIBLE);
                mViewHolder.mName.setEnabled(false);
                mViewHolder.mName.setTextColor(Color.GRAY);
            }
        }
        /*else if(str.equalsIgnoreCase("2")){
            mViewHolder.mFlag.setVisibility(View.VISIBLE);
            mViewHolder.mName.setEnabled(false);
            mViewHolder.mName.setTextColor(Color.GRAY);
        }
        else {
            mViewHolder.mFlag.setVisibility(View.VISIBLE);
            mViewHolder.mName.setEnabled(false);
            mViewHolder.mName.setTextColor(Color.GRAY);
        }*/
        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
        TextView mPopulation;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        } else {
            return true;
        }
    }
}
