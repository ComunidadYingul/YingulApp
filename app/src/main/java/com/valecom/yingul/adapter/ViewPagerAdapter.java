package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.valecom.yingul.R;
import com.valecom.yingul.main.allItems.AllItemsActivity;
import com.valecom.yingul.main.over.OverActivity;
import com.valecom.yingul.main.sell.SellActivity;

/**
 * Created by gonzalo on 02-07-18.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,R.drawable.slide4,R.drawable.slide5,R.drawable.slide6};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(position == 0){
                    intent = new Intent(context,AllItemsActivity.class);
                    context.startActivity(intent);
                }else  if(position == 1){
                    intent = new Intent(context,SellActivity.class);
                    context.startActivity(intent);
                }else  if(position == 2){
                    intent = new Intent(context,SellActivity.class);
                    context.startActivity(intent);
                }else  if(position == 3){
                    intent = new Intent(context,OverActivity.class);
                    context.startActivity(intent);
                }else  if(position == 4){
                    intent = new Intent(context,OverActivity.class);
                    context.startActivity(intent);
                }else{
                    //intent = new Intent(context,AllItemsActivity.class);
                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
