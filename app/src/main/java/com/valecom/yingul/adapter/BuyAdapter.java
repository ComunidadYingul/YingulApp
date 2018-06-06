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
import com.valecom.yingul.model.Yng_Buy;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

public class BuyAdapter extends ArrayAdapter<Yng_Buy>
{
    private final Context context;
    private final ArrayList<Yng_Buy> values;

    public BuyAdapter(Context context, ArrayList<Yng_Buy> values) {
        super(context, R.layout.layout_buy_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_Buy getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_buy_row, parent, false);

        Yng_Buy buy = values.get(position);

        ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemImage);
        TextView itemName = (TextView) rowView.findViewById(R.id.itemName);
        TextView buyCost = (TextView) rowView.findViewById(R.id.buyCost);
        TextView buyTime = (TextView) rowView.findViewById(R.id.buyTime);

        Picasso.with(context).load(Network.BUCKET_URL+buy.getYng_item().getPrincipalImage()).into(itemImage);

        itemName.setText(buy.getYng_item().getName());
        buyCost.setText("ARS "+buy.getCost());
        buyTime.setText(buy.getTime());


        return rowView;
    }
}
