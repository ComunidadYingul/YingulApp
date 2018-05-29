package com.valecom.yingul.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.Item.ItemCategory;
import com.valecom.yingul.R;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gonzalo on 25-05-18.
 */

public class StoreHomeAdapter extends RecyclerView.Adapter<StoreHomeAdapter.ItemRowHolder> {

    private ArrayList<ItemCategory> dataList;
    private Context mContext;

    public StoreHomeAdapter(Context context, ArrayList<ItemCategory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemCategory itemCategory = dataList.get(position);

        holder.text_cat_title.setText(itemCategory.getCategoryName());
        Picasso.with(mContext).load(Network.BUCKET_URL + itemCategory.getCategoryImageBanner()).into(holder.banner_cat);
        Picasso.with(mContext).load(Network.BUCKET_URL + itemCategory.getCategoryImage()).into(holder.image_cat);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(mContext, ActivityCategoryList.class);
                mContext.startActivity(intent);*/
            }
        });

        /*ScaleAnimation scaleAnim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF , 1);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setFillBefore(true);
        scaleAnim.setFillEnabled(true);
        holder.lyt_parent.startAnimation(scaleAnim);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image_cat,banner_cat;
        public TextView text_cat_title;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            banner_cat = (ImageView) itemView.findViewById(R.id.banner_item_cat_home);
            image_cat = (ImageView) itemView.findViewById(R.id.image_item_cat_home);
            text_cat_title = (TextView) itemView.findViewById(R.id.text_item_cat_home);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
