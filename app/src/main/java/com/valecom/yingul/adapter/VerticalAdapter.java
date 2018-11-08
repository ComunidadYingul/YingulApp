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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 02-07-18.
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {
    private ArrayList<Yng_Item> data = new ArrayList<>();
    Context mContext;

    public VerticalAdapter(Context context,ArrayList<Yng_Item> data) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Yng_Item item = data.get(position);

        Log.e("ItemVrtical:------",position+"");

        holder.text_cat_list_title.setText(item.getName());
        holder.text_cat_list_price.setText(String.format("%.0f", item.getPrice()));
        try {
            if (item.getMoney().equals("USD")) {
                holder.textMoney.setText("U$D");
            }else{
                holder.textMoney.setText("$");
            }
        }catch (Exception e){}

        if(item.getPriceDiscount()==0){
            holder.lytDiscount.setVisibility(View.GONE);
        }else{
            Double desc = ((item.getPriceNormal()-item.getPriceDiscount()) * 100)/item.getPriceNormal();
            holder.textDiscountPorcent.setText(String.format("%.0f", desc));
            holder.lytDiscount.setVisibility(View.VISIBLE);
        }

        try{
            if(item.getProductPagoEnvio().equals("gratis")){
                holder.imgEnvioGratis.setVisibility(View.VISIBLE);
            }else{
                holder.imgEnvioGratis.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){}

        Picasso.with(mContext).load(Network.BUCKET_URL+item.getPrincipalImage()).placeholder(R.drawable.placeholder120).into(holder.image_cat_list);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(mContext, ActivityProductDetail.class);
                intent_detail.putExtra("itemId",item.getItemId().toString());
                mContext.startActivity(intent_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_cat_list,imgEnvioGratis;
        public TextView text_cat_list_title,text_cat_list_price,textMoney,textDiscountPorcent;
        public LinearLayout lyt_parent,lyt_otro,lytDiscount;

        public MyViewHolder(View itemView) {
            super(itemView);
            textMoney = (TextView) itemView.findViewById(R.id.textMoney);
            image_cat_list = (ImageView) itemView.findViewById(R.id.image_item_cat_list_image);
            imgEnvioGratis = (ImageView) itemView.findViewById(R.id.imgEnvioGratis);
            text_cat_list_title = (TextView) itemView.findViewById(R.id.text_item_cat__list_title);
            text_cat_list_price = (TextView) itemView.findViewById(R.id.text_item_cat_list_price);
            textDiscountPorcent = (TextView) itemView.findViewById(R.id.textDiscountPorcent);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            lytDiscount = (LinearLayout) itemView.findViewById(R.id.lytDiscount);
        }
    }
}
