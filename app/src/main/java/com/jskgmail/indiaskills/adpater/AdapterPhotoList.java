package com.jskgmail.indiaskills.adpater;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jskgmail.indiaskills.R;
import com.jskgmail.indiaskills.pojo.Photo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aditya.singh on 12/12/2018.
 */

public class AdapterPhotoList extends RecyclerView.Adapter<AdapterPhotoList.ViewHolder> {

    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<Photo> photos;

    public AdapterPhotoList(Activity activity, ArrayList<Photo> Photos) {
        this.activity = activity;
        this.photos = Photos;
        layoutInflater = LayoutInflater.from(activity);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Photo photo = photos.get(position);

        File pictureFile = new File(photo.getUrl());
      //  Picasso.with(activity).load(pictureFile).resize(60,60).centerInside().into(viewHolder.image);
//        Picasso.get().load(pictureFile).resize(60, 60).
//                centerInside().into(viewHolder.image);
        Glide.with(activity)
                .load(Uri.fromFile(pictureFile))
                .apply(new RequestOptions().override(60, 60))
                .into(viewHolder.image);
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImage(photos.get(position).getUrl());
            }
        });
    }

    public void showImage(String imageUri) {
        File pictureFile = new File(imageUri);
        Dialog builder = new Dialog(activity);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(activity);

//        Picasso.get().load(pictureFile).resize(450,450).centerInside().into(imageView);
        Picasso.with(activity).load(pictureFile).resize(450,450).centerInside().into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
