package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.Yng_Store;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 04-06-18.
 */

public class AllStoresAdapter extends RecyclerView.Adapter<AllStoresAdapter.ItemRowHolder> {

    private ArrayList<Yng_Store> dataList;
    private Context mContext;

    public AllStoresAdapter(Context context, ArrayList<Yng_Store> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public AllStoresAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store_item, parent, false);
        return new AllStoresAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(AllStoresAdapter.ItemRowHolder holder, final int position) {
        final Yng_Store item = dataList.get(position);

        holder.text_title.setText(item.getName());
        Picasso.with(mContext).load(Network.BUCKET_URL +"store/"+ item.getMainImage()).into(holder.image_public);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityStore.class);
                intent.putExtra("store",item.getName());
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
            //text_price = (TextView) itemView.findViewById(R.id.text_price);
            //text_description = (TextView) itemView.findViewById(R.id.text_description);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.lay_home_slider);
        }
    }
}
