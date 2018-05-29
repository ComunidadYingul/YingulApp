package com.valecom.yingul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Province;

import java.util.ArrayList;


public class ProvinceAdapter extends ArrayAdapter<Yng_Province>
{
    private final Context context;
    private final ArrayList<Yng_Province> values;

    public ProvinceAdapter(Context context, ArrayList<Yng_Province> values) {
        super(context, R.layout.layout_item_row, values);

        this.context = context;
        this.values = values;


    }

    public Yng_Province getItem(int position)
    {
        return values.get(position);
    }

    /**
     * Here we go and get our rowlayout.xml file and set the textview text.
     * This happens for every row in your listview.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_category_row, parent, false);
        Yng_Province province = values.get(position);
        TextView province_name = (TextView) rowView.findViewById(R.id.text_name);
        province_name.setText(province.getName());

        return rowView;
    }
}
