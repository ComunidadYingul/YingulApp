package com.valecom.yingul.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_ItemImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by gonzalo on 22-05-18.
 */

public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.ItemRowHolder> {

    private ArrayList<Yng_ItemImage> dataList;
    private Context mContext;

    public ItemImageAdapter(Context context, ArrayList<Yng_ItemImage> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final Yng_ItemImage itemGallery = dataList.get(position);

        holder.image_gallery.setImageBitmap(StringToBitMap(itemGallery.getImage()));
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image_gallery;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_gallery = (ImageView) itemView.findViewById(R.id.image_item_detail);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image, Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
