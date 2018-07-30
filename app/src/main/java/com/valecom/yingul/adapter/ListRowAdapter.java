package com.valecom.yingul.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 29-05-18.
 */

public class ListRowAdapter extends RecyclerView.Adapter<ListRowAdapter.ItemRowHolder> {

    private ArrayList<Yng_Item> dataList;
    private Activity mContext;
    private InterstitialAd mInterstitial;
    public ListRowAdapter(Activity context, ArrayList<Yng_Item> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ListRowAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
        return new ListRowAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ListRowAdapter.ItemRowHolder holder, final int position) {
        final Yng_Item item = dataList.get(position);

        holder.text_cat_list_title.setText(item.getName());
        holder.text_cat_list_price.setText(String.format("%.0f", item.getPrice()));
        holder.textPriceNormal.setText(String.valueOf(String.format("%.0f", item.getPriceNormal())));
        holder.textPriceNormal.setPaintFlags(holder.textPriceNormal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Picasso.with(mContext).load(Network.BUCKET_URL+item.getPrincipalImage()).into(holder.image_cat_list);

        try {
            if (item.getMoney().equals("USD")) {
                holder.textMoney.setText("U$D");
                holder.textMoneyNormal.setText("U$D");
            }else{
                holder.textMoney.setText("$");
                holder.textMoneyNormal.setText("$");
            }
        }catch (Exception e){}

        if(item.getPriceDiscount()==0){
            holder.lytDiscount.setVisibility(View.GONE);
            holder.lytPriceNormal.setVisibility(View.GONE);
        }else{
            Double desc = ((item.getPriceNormal()-item.getPriceDiscount()) * 100)/item.getPriceNormal();
            holder.textDiscountPorcent.setText(String.format("%.0f", desc));
            holder.lytDiscount.setVisibility(View.VISIBLE);
            holder.lytPriceNormal.setVisibility(View.VISIBLE);
        }

        try {
            if (item.getProductPagoEnvio().toString().equals("gratis")) {
                holder.lytEnvioGratis.setVisibility(View.VISIBLE);
            }else{
                holder.lytEnvioGratis.setVisibility(View.GONE);
            }
        }catch (Exception e){}

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mInterstitial = new InterstitialAd(mContext);
                mInterstitial.setAdUnitId(mContext.getString(R.string.admob_interstitial_id));
                mInterstitial.loadAd(new AdRequest.Builder().build());
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // TODO Auto-generated method stub
                        super.onAdLoaded();
                        if (mInterstitial.isLoaded()) {
                            mInterstitial.show();
                        }
                    }

                    public void onAdClosed() {
                        Intent intent_detail=new Intent(mContext, ActivityProductDetail.class);
                        intent_detail.putExtra("itemId",item.getCategoryListId());
                        //intent_detail.putExtra("seller",item.getCategorySeller());
                        mContext.startActivity(intent_detail);
                    }

                });*/
                Intent intent_detail=new Intent(mContext, ActivityProductDetail.class);
                intent_detail.putExtra("itemId",item.getItemId().toString());
                //intent_detail.putExtra("seller",item.getCategorySeller());
                mContext.startActivity(intent_detail);

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
        public ImageView image_cat_list;
        public TextView text_cat_list_title,text_cat_list_price,textPriceNormal,textDiscountPorcent,textMoney,textMoneyNormal;
        public LinearLayout lyt_parent,lytDiscount,lytPriceNormal;
        public RelativeLayout lytEnvioGratis;
        public RatingView ratingBarCatList;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_cat_list = (ImageView) itemView.findViewById(R.id.image_item_cat_list_image);
            text_cat_list_title = (TextView) itemView.findViewById(R.id.text_item_cat_list_title);
            text_cat_list_price = (TextView) itemView.findViewById(R.id.text_item_cat_list_price);
            textDiscountPorcent = (TextView) itemView.findViewById(R.id.textDiscountPorcent);
            textMoney = (TextView) itemView.findViewById(R.id.textMoney);
            textMoneyNormal = (TextView) itemView.findViewById(R.id.textMoneyNormal);
            textPriceNormal=(TextView)itemView.findViewById(R.id.textPriceNormal);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            lytDiscount = (LinearLayout) itemView.findViewById(R.id.lytDiscount);
            lytPriceNormal = (LinearLayout) itemView.findViewById(R.id.lytPriceNormal);
            lytEnvioGratis = (RelativeLayout) itemView.findViewById(R.id.lytEnvioGratis);
            ratingBarCatList=(RatingView)itemView.findViewById(R.id.rating_cat_list);
        }
    }
}
