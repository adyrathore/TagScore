package com.jskgmail.indiaskills.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jskgmail.indiaskills.Globalclass;
import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.db.DatabaseHelper;
import com.jskgmail.indiaskills.pojo.ResponseLogin;
import com.jskgmail.indiaskills.util.C;
import com.jskgmail.indiaskills.util.SharedPreference;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static android.app.Activity.RESULT_OK;
import static com.jskgmail.indiaskills.adpater.CustomAdapterbatch.ViewHolder.cam;

public class CustomAdapterbatch extends RecyclerView.Adapter<CustomAdapterbatch.ViewHolder> {
    //int height,width;
    Context context;
    private int pos;

    static ArrayList<String> picsforoffline = new ArrayList<>();

    ArrayList<String> infos, infos1;

    String url = C.API_UPLOAD_VIDEO_AND_PHOTO;


    public CustomAdapterbatch(Context context, ArrayList<String> infos, ArrayList<String> infos1) {
        this.context = context;
        this.infos = infos;
        this.infos1 = infos1;

    }

//    public void getScreenSize(){
//        DisplayMetrics displayMetrics=new DisplayMetrics();
//        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//        height=displayMetrics.heightPixels;
//        width=displayMetrics.widthPixels;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_batch, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        // Toast.makeText(context, "blablabla", Toast.LENGTH_SHORT).show();

        //  getScreenSize();
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //    batch_info info= infos.get(position);

        //  Toast.makeText(context, feed.getUsername(), Toast.LENGTH_SHORT).show();

        holder.title.setText(infos.get(position));
        holder.desc.setText(infos1.get(position));


        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(i, cam);
                holder.b2.setVisibility(View.GONE);
                pos = position;


            }
        });

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(i, cam);
                holder.b1.setVisibility(View.GONE);
                pos = position;

            }
        });

        if ((holder.b1.getVisibility() == View.GONE) && (holder.b2.getVisibility() == View.GONE)) {
            holder.cc.setCardBackgroundColor(Color.GREEN);
        }
        // for(int i=0;i<info.getPhoto().length;i++){//flipper.photoflipper(holder.getAdapterPosition());

        // Glide.with(context).load(info.getPhoto()).into(holder.photo);


    }


    @Override
    public int getItemCount() {
        return (infos == null) ? 0 : infos.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements com.jskgmail.indiaskills.interfaces.ViewHolder {

        TextView desc;
        ViewFlipper viewFlipper;

        TextView title;
        FloatingTextButton b1;

        FloatingTextButton b2;

        final static int cam = 0;

        Bitmap bm;
        CardView cc;


        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.username);
            b1 = (FloatingTextButton) itemView.findViewById(R.id.fab);
            cc = (CardView) itemView.findViewById(R.id.cc);
            b2 = (FloatingTextButton) itemView.findViewById(R.id.fab1);


        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // TODO Auto-generated method stub

            //         super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Bundle bt = data.getExtras();
                bm = (Bitmap) bt.get("data");

                Toast.makeText(context, "Image uploaded", Toast.LENGTH_LONG).show();

                File file = new File("path");
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file));

                    bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
               ResponseLogin responseLogin = SharedPreference.getInstance(context).getUser(C.LOGIN_USER);
                AddGeofencebody44(responseLogin.getUserID(), responseLogin.getApiKey(), infos1.get(pos), file);


                    notifyDataSetChanged();
            }
        }


    }


    private void AddGeofencebody44(String userid, String apikey, String candidateID, File bm) {


        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        DatabaseHelper db = new DatabaseHelper(context);
        boolean success = db.insert_imagesval("Candidate capture", encodedString, bm.toString(), Globalclass.batchidoffline, bm.toString(), Globalclass.tagid, Globalclass.schduleid);
        if (success == true) {
            //  Toast.makeText(context,"saved",Toast.LENGTH_LONG).show();
        } else {
            //  Toast.makeText(context,"not saved",Toast.LENGTH_LONG).show();
        }
    }


}
