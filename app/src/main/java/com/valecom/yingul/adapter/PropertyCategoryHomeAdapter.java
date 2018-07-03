package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.categories.CategoryActivity;
import com.valecom.yingul.main.motorized.MotorizedActivity;
import com.valecom.yingul.main.property.PropertyActivity;
import com.valecom.yingul.main.service.ServiceActivity;
import com.valecom.yingul.main.store.AllStoreActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_City;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gonzalo on 23-05-18.
 */

public class PropertyCategoryHomeAdapter extends RecyclerView.Adapter<PropertyCategoryHomeAdapter.ItemRowHolder>{

    private ArrayList<Yng_Category> dataList;
    private Context mContext;

    public PropertyCategoryHomeAdapter(Context context, ArrayList<Yng_Category> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public Yng_Category getItem(int position)
    {
        return dataList.get(position);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_property_category_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final Yng_Category item = dataList.get(position);
        holder.textCategory.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public CircleImageView image_all_category;
        public TextView textCategory;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            textCategory = (TextView) itemView.findViewById(R.id.textCategory);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
