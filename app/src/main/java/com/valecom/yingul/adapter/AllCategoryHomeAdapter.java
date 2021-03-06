package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.Item.ItemAllCategory;
import com.valecom.yingul.R;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.main.property.PropertyActivity;
import com.valecom.yingul.main.categories.CategoryActivity;
import com.valecom.yingul.main.motorized.MotorizedActivity;
import com.valecom.yingul.main.service.ServiceActivity;
import com.valecom.yingul.main.store.AllStoreActivity;
import com.valecom.yingul.model.Yng_Category;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gonzalo on 23-05-18.
 */

public class AllCategoryHomeAdapter extends RecyclerView.Adapter<AllCategoryHomeAdapter.ItemRowHolder>{

    private ArrayList<Yng_Category> dataList;
    private Context mContext;

    public AllCategoryHomeAdapter(Context context, ArrayList<Yng_Category> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_category_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final Yng_Category item = dataList.get(position);

        holder.textCategory.setText(item.getName());
        Picasso.with(mContext).load("file:///android_asset/image/"+item.getImage()).into(holder.image_all_category);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                //Log.e("seleccionado:--",item.getItemType().toString());
                switch (item.getItemType()){
                    case "motorized":
                        intent = new Intent(mContext, MotorizedActivity.class);
                        mContext.startActivity(intent);
                        //Toast.makeText(mContext,"Mostrar motorizados",Toast.LENGTH_SHORT).show();
                        break;
                    case "property":
                        intent = new Intent(mContext, PropertyActivity.class);
                        mContext.startActivity(intent);
                        //Toast.makeText(mContext,"Mostrar inmuebles",Toast.LENGTH_SHORT).show();
                        break;
                    case "service":
                        intent = new Intent(mContext, ServiceActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case "stores":
                        intent = new Intent(mContext, AllStoreActivity.class);
                        mContext.startActivity(intent);
                    break;
                    default:
                        intent = new Intent(mContext, CategoryActivity.class);
                        intent.putExtra("categoryId", item.getCategoryId().toString());
                        mContext.startActivity(intent);
                    break;
                }
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
        //public ImageView image_all_category;
        public CircleImageView image_all_category;
        public TextView textCategory;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_all_category = (CircleImageView) itemView.findViewById(R.id.image_item_all_category_list_image);
            textCategory = (TextView) itemView.findViewById(R.id.textCategory);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
