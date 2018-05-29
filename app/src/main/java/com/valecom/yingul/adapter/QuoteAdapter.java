package com.valecom.yingul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Quote;

import java.util.ArrayList;


public class QuoteAdapter extends ArrayAdapter<Yng_Quote>
{
    private final Context context;
    private final ArrayList<Yng_Quote> values;

    public QuoteAdapter(Context context, ArrayList<Yng_Quote> values) {
        super(context, R.layout.layout_item_row, values);

        this.context = context;
        this.values = values;


    }

    public Yng_Quote getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_quote_row, parent, false);
        Yng_Quote invoice = values.get(position);
        TextView text_quote_branch_location = (TextView) rowView.findViewById(R.id.text_quote_branch_location);
        TextView text_quote_street = (TextView) rowView.findViewById(R.id.text_quote_street);
        TextView text_quote_schedules = (TextView) rowView.findViewById(R.id.text_quote_schedules);
        TextView text_quote_rate = (TextView) rowView.findViewById(R.id.text_quote_rate);
        text_quote_branch_location.setText(invoice.getYng_Branch().getNameMail()+" ("+invoice.getYng_Branch().getLocation()+")");
        text_quote_street.setText(invoice.getYng_Branch().getStreet());
        text_quote_schedules.setText(invoice.getYng_Branch().getSchedules());
        text_quote_rate.setText("$ "+invoice.getRate());
        return rowView;
    }
}
