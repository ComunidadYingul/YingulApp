package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 22-06-18.
 */

public class QueryRowAdapter extends RecyclerView.Adapter<QueryRowAdapter.ItemRowHolder>{
    private ArrayList<Yng_Query> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;
    public QueryRowAdapter(Context context, ArrayList<Yng_Query> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public QueryRowAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_query_list, parent, false);
        return new QueryRowAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(QueryRowAdapter.ItemRowHolder holder, final int position) {
        final Yng_Query query = dataList.get(position);

        if(query.getAnswer().equals("null")){
            holder.answer.setText("");
        }else{
            holder.answer.setText(query.getAnswer());
        }

        holder.itemQuery.setText(query.getQuery());

        /*holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(mContext, ActivityProductDetail.class);
                intent_detail.putExtra("itemId",item.getItemId().toString());
                //intent_detail.putExtra("seller",item.getCategorySeller());
                mContext.startActivity(intent_detail);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView answer,itemQuery,queryDate;

        public ItemRowHolder(View itemView) {
            super(itemView);
            answer = (TextView) itemView.findViewById(R.id.answer);
            itemQuery = (TextView) itemView.findViewById(R.id.itemQuery);
            queryDate = (TextView) itemView.findViewById(R.id.queryDate);
        }
    }
}
