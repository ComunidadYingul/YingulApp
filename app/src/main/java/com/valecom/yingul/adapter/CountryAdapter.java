package com.valecom.yingul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Country;

import java.util.ArrayList;


public class CountryAdapter extends ArrayAdapter<Yng_Country>
{
    private final Context context;
    private final ArrayList<Yng_Country> values;

    public CountryAdapter(Context context, ArrayList<Yng_Country> values) {
        super(context, R.layout.layout_item_row, values);

        this.context = context;
        this.values = values;


    }

    public Yng_Country getItem(int position)
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
        Yng_Country country = values.get(position);
        TextView country_name = (TextView) rowView.findViewById(R.id.text_name);
        country_name.setText(country.getName());

        return rowView;
    }
}
