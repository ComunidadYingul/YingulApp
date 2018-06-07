package com.valecom.yingul.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Category;

import java.util.ArrayList;

/**
 * Created by gonzalo on 07-06-18.
 */

public class SimpleAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Yng_Category> items;

    public SimpleAdapter (Activity activity, ArrayList<Yng_Category> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Yng_Category> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.row_simple_item, null);
        }

        Yng_Category dir = items.get(position);

        TextView description = (TextView) v.findViewById(R.id.text_name);
        description.setText(dir.getName());

        return v;
    }
}
