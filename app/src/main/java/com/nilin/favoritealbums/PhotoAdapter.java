package com.nilin.favoritealbums;

/**
 * Created by nilin on 2017/2/8.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{


    private Context mContext;

    private List<Photo> mPhotoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView photoImage;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            photoImage = (ImageView) view.findViewById(R.id.photo_image);
        }
    }

    public PhotoAdapter(List<Photo> photoList) {
        mPhotoList = photoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();

        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
//                Photo photo = mPhotoList.get(position);
                Intent intent = new Intent(mContext, PhotoActivity.class);
//                intent.putExtra(PhotoActivity.Photo_IMAGE_ID, photo.getImageId());
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotoList.get(position);
//        holder.photoImage.setImageResource(photo.getImageId());
        Glide.with(mContext).load(photo.getImageId()).into(holder.photoImage);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

}