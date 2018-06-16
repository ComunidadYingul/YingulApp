package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 29-05-18.
 */

public class ListGridAdapter extends RecyclerView.Adapter<ListGridAdapter.ItemRowHolder>{
    private ArrayList<Yng_Item> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;

    public ListGridAdapter(Context context, ArrayList<Yng_Item> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ListGridAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grid_item, parent, false);
        return new ListGridAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ListGridAdapter.ItemRowHolder holder, final int position) {
        final Yng_Item item = dataList.get(position);

        holder.text_cat_list_title.setText(item.getName());
        holder.text_cat_list_price.setText(String.valueOf(item.getPrice()));

        try{
            if(item.getMoney().equals("ARS")){
                holder.moneyUsd.setVisibility(View.GONE);
            }else{
                holder.moneyArs.setVisibility(View.VISIBLE);
            }


        }catch (Exception e){

        }

        try {
            if (item.getType().toString().equals("Property")) {
                holder.lyt_otro.setVisibility(View.VISIBLE);
                holder.text_cat_list_otro.setText(item.getDuildedArea() + " m2");
            }
        }catch (Exception e){}
        Picasso.with(mContext).load(Network.BUCKET_URL+item.getPrincipalImage()).into(holder.image_cat_list);

        try {
            Log.e("envio:----",item.getProductPagoEnvio().toString());
            if (item.getProductPagoEnvio().toString().equals("gratis")) {
                holder.imgEnvioGratis.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){}


        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(mContext, ActivityProductDetail.class);
                intent_detail.putExtra("itemId",item.getItemId().toString());
                //intent_detail.putExtra("seller",item.getCategorySeller());
                mContext.startActivity(intent_detail);
                //Log.e("gonzalo:----","hola");

                /*
                Log.e("Eddy","click");
                mInterstitial = new InterstitialAd(mContext);
                mInterstitial.setAdUnitId(mContext.getString(R.string.admob_interstitial_id));
                mInterstitial.loadAd(new AdRequest.Builder().build());
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // TODO Auto-generated method stub
                        super.onAdLoaded();
                        if (mInterstitial.isLoaded()) {
                            mInterstitial.show();
                            mInterstitial.|
                        }
                    }

                    public void onAdClosed() {

                    }

                });*/

            }
        });
        /*ScaleAnimation scaleAnim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF , 1);
        scaleAnim.setDuration(750);
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
        public ImageView image_cat_list,imgEnvioGratis;
        public TextView text_cat_list_title,text_cat_list_price,text_cat_list_otro,moneyArs,moneyUsd;
        public LinearLayout lyt_parent,lyt_otro;

        public ItemRowHolder(View itemView) {
            super(itemView);
            moneyArs = (TextView) itemView.findViewById(R.id.money_ars);
            moneyUsd = (TextView) itemView.findViewById(R.id.money_usd);
            image_cat_list = (ImageView) itemView.findViewById(R.id.image_item_cat_list_image);
            imgEnvioGratis = (ImageView) itemView.findViewById(R.id.imgEnvioGratis);
            text_cat_list_title = (TextView) itemView.findViewById(R.id.text_item_cat__list_title);
            text_cat_list_otro = (TextView) itemView.findViewById(R.id.text_item_cat__list_otro);
            text_cat_list_price = (TextView) itemView.findViewById(R.id.text_item_cat_list_price);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            lyt_otro = (LinearLayout) itemView.findViewById(R.id.layout_otro);
            lyt_otro.setVisibility(View.GONE);

        }
    }

}
