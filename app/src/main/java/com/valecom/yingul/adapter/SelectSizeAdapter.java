package com.valecom.yingul.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.Item.ItemColorSize;
import com.valecom.yingul.R;

import java.util.ArrayList;

/**
 * Created by gonzalo on 22-05-18.
 */

public class SelectSizeAdapter extends RecyclerView.Adapter<SelectSizeAdapter.MyViewHolder> {

    private ArrayList<ItemColorSize> ColorList;
    private Context mContext;
    private int row_index = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_color;
        public LinearLayout lay_size;
        public LinearLayout card_size;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            text_color = (TextView) view.findViewById(R.id.text_size);
            lay_size=(LinearLayout) view.findViewById(R.id.rootLayout);
            card_size=(LinearLayout)view.findViewById(R.id.rootLayout_bg);
            card_view=(CardView)view.findViewById(R.id.card_view);

        }
    }

    public SelectSizeAdapter(Context context,ArrayList<ItemColorSize> colorList) {
        this.ColorList = colorList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_size_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemColorSize itemColorSize = ColorList.get(position);
        holder.text_color.setText(itemColorSize.getSelectSize());

        holder.lay_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.card_size.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            holder.text_color.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.card_size.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_light));
            holder.text_color.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        }


    }

    @Override
    public int getItemCount() {
        return ColorList.size();
    }
}
