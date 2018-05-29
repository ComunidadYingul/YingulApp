package com.valecom.yingul.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.SettingActivity;
import com.valecom.yingul.model.Invoice;
import com.valecom.yingul.model.Yng_Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CategoryAdapter extends ArrayAdapter<Yng_Category>
{
    private final Context context;
    private final ArrayList<Yng_Category> values;

    public CategoryAdapter(Context context, ArrayList<Yng_Category> values) {
        super(context, R.layout.layout_item_row, values);

        this.context = context;
        this.values = values;


    }

    public Yng_Category getItem(int position)
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
        Yng_Category invoice = values.get(position);
        TextView category_name = (TextView) rowView.findViewById(R.id.text_name);
        category_name.setText(invoice.getName());

        return rowView;
    }
}
