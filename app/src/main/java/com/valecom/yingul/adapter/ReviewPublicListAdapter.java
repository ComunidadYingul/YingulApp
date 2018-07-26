package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gonzalo on 24-05-18.
 */

public class ReviewPublicListAdapter extends RecyclerView.Adapter<ReviewPublicListAdapter.ItemRowHolder> {

    private ArrayList<Yng_Item> dataList;
    private Context mContext;

    public ReviewPublicListAdapter(Context context, ArrayList<Yng_Item> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ReviewPublicListAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_img_item, parent, false);
        return new ReviewPublicListAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewPublicListAdapter.ItemRowHolder holder, final int position) {
        final Yng_Item item = dataList.get(position);

        holder.text_title.setText(item.getName());
        holder.text_price.setText(String.valueOf(item.getPrice()));
        holder.text_description.setText(item.getDescription());
        Picasso.with(mContext).load(Network.BUCKET_URL+item.getPrincipalImage()).into(holder.image_public);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityProductDetail.class);
                intent.putExtra("itemId",item.getItemId().toString());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image_public;
        public TextView text_title,text_price,text_description;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_public = (ImageView) itemView.findViewById(R.id.image_public);
            text_title = (TextView) itemView.findViewById(R.id.text_title);
            text_price = (TextView) itemView.findViewById(R.id.text_price);
            text_description = (TextView) itemView.findViewById(R.id.text_description);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.lay_home_slider);
        }
    }
}
