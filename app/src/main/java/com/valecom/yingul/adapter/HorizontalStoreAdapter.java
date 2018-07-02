package com.valecom.yingul.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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
import com.valecom.yingul.model.Yng_Store;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 02-07-18.
 */

public class HorizontalStoreAdapter extends RecyclerView.Adapter<HorizontalStoreAdapter.ItemRowHolder> {
    private ArrayList<Yng_Store> dataList;
    private Context mContext;

    public HorizontalStoreAdapter(Context context, ArrayList<Yng_Store> dataList) {
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
        int tam= dataList.size();
        Log.e("tamano:--",position+"");
        if(tam==position+1){
            Log.e("tamano:--","llego al final");
            final Yng_Store item = dataList.get(position);

            holder.text_cat_title.setText(item.getName());
            holder.text_cat_title.setTextColor(Color.parseColor("#245fff"));
            holder.filtro.setVisibility(View.INVISIBLE);
            holder.lytText.setVisibility(View.VISIBLE);
            holder.card1.setCardElevation(0);
            holder.card2.setCardElevation(0);
            Picasso.with(mContext).load(Network.BUCKET_URL + item.getBannerImage()).into(holder.banner_cat);
            Picasso.with(mContext).load("file:///android_asset/image/more_stores.png").into(holder.image_cat);


        }else{
            final Yng_Store item = dataList.get(position);

            holder.text_cat_title.setText(item.getName());
            holder.text_cat_title.setTextColor(Color.parseColor("#777777"));
            holder.filtro.setVisibility(View.VISIBLE);
            holder.lytText.setVisibility(View.INVISIBLE);
            holder.card1.setCardElevation(5);
            holder.card2.setCardElevation(5);
            Picasso.with(mContext).load(Network.BUCKET_URL + item.getBannerImage()).into(holder.banner_cat);
            Picasso.with(mContext).load(Network.BUCKET_URL + item.getMainImage()).into(holder.image_cat);


        }


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
        public ImageView image_cat,banner_cat,filtro;
        public TextView text_cat_title;
        public LinearLayout lyt_parent,lytText;
        public CardView card1,card2;

        public ItemRowHolder(View itemView) {
            super(itemView);
            filtro = (ImageView) itemView.findViewById(R.id.filtro_transparente);
            banner_cat = (ImageView) itemView.findViewById(R.id.banner_item_cat_home);
            image_cat = (ImageView) itemView.findViewById(R.id.image_item_cat_home);
            text_cat_title = (TextView) itemView.findViewById(R.id.text_item_cat_home);
            lytText = (LinearLayout) itemView.findViewById(R.id.lytText);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            card1 = (CardView)itemView.findViewById(R.id.card_logo);
            card2 = (CardView)itemView.findViewById(R.id.card_view_cat);
        }
    }
}
