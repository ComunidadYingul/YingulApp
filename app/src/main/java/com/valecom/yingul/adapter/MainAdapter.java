package com.valecom.yingul.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Store;

import java.util.ArrayList;

/**
 * Created by gonzalo on 02-07-18.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Object> items;
    private final int HERO = 0;
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 2;
    private final int HORIZONTAL_ALL_CAT = 3;
    private final int HORIZONTAL_ALL_STORE = 4;

    public ArrayList<Yng_Item> array_all_items;
    public ArrayList<Yng_Item> array_all_over;
    public ArrayList<Yng_Category> array_all_category;
    public ArrayList<Yng_Store> array_all_stores;

    public MainAdapter(Context context, ArrayList<Object> items) {
        this.context = context;
        this.items = items;
        array_all_category = new ArrayList<>();
        array_all_items = new ArrayList<>();
        array_all_over = new ArrayList<>();
        array_all_stores = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        Log.e("tipo",viewType+"");
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case HERO:
                View viewHero = inflater.inflate(R.layout.row_slider, parent, false);
                holder = new HeroVH(viewHero);
                break;
            case HORIZONTAL_ALL_CAT:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            case VERTICAL:
                view = inflater.inflate(R.layout.vertical, parent, false);
                holder = new VerticalViewHolder(view);
                break;
            case HORIZONTAL:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            case HORIZONTAL_ALL_STORE:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.horizontal, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == HERO) {
            final HeroVH heroVh = (HeroVH) holder;

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context);
            heroVh.viewPager.setAdapter(viewPagerAdapter);



            //loadImage(result.getBackdropPath()).into(heroVh.mPosterImg);
        }
        else if (holder.getItemViewType() == HORIZONTAL_ALL_CAT) {
            horizontalViewCat((HorizontalViewHolder) holder);
        }
        else if (holder.getItemViewType() == VERTICAL) {
            verticalView((VerticalViewHolder) holder);
        }
        else if (holder.getItemViewType() == HORIZONTAL) {
            horizontalView((HorizontalViewHolder) holder);
        }
        else if (holder.getItemViewType() == HORIZONTAL_ALL_STORE) {
            horizontalViewStore((HorizontalViewHolder) holder);
        }


    }

    private void horizontalViewStore(HorizontalViewHolder holder) {
        HorizontalStoreAdapter adapter = new HorizontalStoreAdapter(context,array_all_stores);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    private void horizontalViewCat(HorizontalViewHolder holder) {
        HorizontalCircleAdapter adapter = new HorizontalCircleAdapter(context,array_all_category);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }
    VerticalAdapter adapter1;
    private void verticalView(VerticalViewHolder holder) {
        adapter1 = new VerticalAdapter(context,array_all_items);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recyclerView.setAdapter(adapter1);
    }


    private void horizontalView(HorizontalViewHolder holder) {
        HorizontalAdapter adapter = new HorizontalAdapter(context,array_all_over);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("position:---",position+"");

        switch (position){
            case 0:
                return HERO;
            case 1:
                return HORIZONTAL_ALL_CAT;
            case 2:
                return HORIZONTAL;
            case 3:
                return HORIZONTAL;
            case 4:
                return HORIZONTAL_ALL_STORE;
            default:
                return VERTICAL;
        }
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    protected class HeroVH extends RecyclerView.ViewHolder {
        ViewPager viewPager;

        public HeroVH(View itemView) {
            super(itemView);

            viewPager = (ViewPager)itemView.findViewById(R.id.viewPager);
        }
    }

    public  void updateDataAllItems(){
        adapter1.notifyDataSetChanged();
    }

    public void setArrays(ArrayList<Yng_Item> array_all_items,ArrayList<Yng_Item> array_all_over,ArrayList<Yng_Category> array_all_category,ArrayList<Yng_Store> array_all_stores){
        this.array_all_category =array_all_category;
        this.array_all_items =array_all_items;
        this.array_all_over =array_all_over;
        this.array_all_stores =array_all_stores;
    }
}
