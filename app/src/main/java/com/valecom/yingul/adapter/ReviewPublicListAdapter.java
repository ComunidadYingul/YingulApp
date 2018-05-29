package com.valecom.yingul.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.Item.ItemReviewPublic;
import com.valecom.yingul.R;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gonzalo on 24-05-18.
 */

public class ReviewPublicListAdapter extends RecyclerView.Adapter<ReviewPublicListAdapter.ItemRowHolder> {

    private ArrayList<ItemReviewPublic> dataList;
    private Context mContext;

    public ReviewPublicListAdapter(Context context, ArrayList<ItemReviewPublic> dataList) {
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
        final ItemReviewPublic itemReviewPublic = dataList.get(position);

        holder.text_title.setText(itemReviewPublic.getReviewTitle());
        holder.text_price.setText(itemReviewPublic.getReviewPrice());
        holder.text_description.setText(itemReviewPublic.getReviewDescription());
        Picasso.with(mContext).load(Network.BUCKET_URL+itemReviewPublic.getReviewImage()).into(holder.image_public);

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
