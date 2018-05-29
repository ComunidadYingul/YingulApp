package com.valecom.yingul.adapter;

import android.content.Context;
import android.graphics.Color;
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

public class SelectColorAdapter extends RecyclerView.Adapter<SelectColorAdapter.MyViewHolder> {

    private ArrayList<ItemColorSize> ColorList;
    private Context context;
    private int row_index = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_color;
        public LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            text_color = (TextView) view.findViewById(R.id.text_color);
            layout = (LinearLayout) view.findViewById(R.id.rootLayout);

        }
    }

    public SelectColorAdapter(Context context, ArrayList<ItemColorSize> colorList) {
        this.ColorList = colorList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemColorSize itemColorSize = ColorList.get(position);
        holder.text_color.setBackgroundColor(Color.parseColor(itemColorSize.getSelectColor()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.layout.setBackgroundResource(R.drawable.background_selector);
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));

        }

    }

    @Override
    public int getItemCount() {
        return ColorList.size();
    }
}
