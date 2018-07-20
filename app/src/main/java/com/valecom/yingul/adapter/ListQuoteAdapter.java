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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

/**
 * Created by gonzalo on 20-07-18.
 */

public class ListQuoteAdapter extends RecyclerView.Adapter<ListQuoteAdapter.ItemRowHolder>{
    private ArrayList<Yng_Quote> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;
    public ListQuoteAdapter(Context context, ArrayList<Yng_Quote> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ListQuoteAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_quote_row, parent, false);
        return new ListQuoteAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ListQuoteAdapter.ItemRowHolder holder, final int position) {
        final Yng_Quote quote = dataList.get(position);

        holder.text_quote_branch_location.setText(quote.getYng_Branch().getNameMail()+" ("+quote.getYng_Branch().getLocation()+")");
        holder.text_quote_street.setText(quote.getYng_Branch().getStreet());
        holder.text_quote_schedules.setText(quote.getYng_Branch().getSchedules());
        if(quote.getYng_Item().getProductPagoEnvio()!=null){
            if(quote.getYng_Item().getProductPagoEnvio().equals("gratis")){
                holder.text_quote_rate.setText("GRATIS");
            }else {
                holder.text_quote_rate.setText("$ "+quote.getRate());
            }

        }else{
            holder.text_quote_rate.setText("$ "+quote.getRate());
        }

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView text_quote_branch_location,text_quote_street,text_quote_schedules,text_quote_rate;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text_quote_branch_location = (TextView) itemView.findViewById(R.id.text_quote_branch_location);
            text_quote_street = (TextView) itemView.findViewById(R.id.text_quote_street);
            text_quote_schedules = (TextView) itemView.findViewById(R.id.text_quote_schedules);
            text_quote_rate = (TextView) itemView.findViewById(R.id.text_quote_rate);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.lyt_parent);
        }
    }
}
