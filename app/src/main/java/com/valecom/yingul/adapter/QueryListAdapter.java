package com.valecom.yingul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

public class QueryListAdapter extends ArrayAdapter<Yng_Query>
{
    private final Context context;
    private final ArrayList<Yng_Query> values;

    public QueryListAdapter(Context context, ArrayList<Yng_Query> values) {
        super(context, R.layout.layout_query_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_Query getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_query_list, parent, false);

        Yng_Query query = values.get(position);

        TextView answer = (TextView) rowView.findViewById(R.id.answer);
        TextView itemQuery = (TextView) rowView.findViewById(R.id.itemQuery);
        TextView queryDate = (TextView) rowView.findViewById(R.id.queryDate);

        if(query.getAnswer().equals("null")){
            answer.setText("");
        }else{
            answer.setText(query.getAnswer());
        }

        itemQuery.setText(query.getQuery());
        queryDate.setText(query.getDate());

        return rowView;
    }
}
