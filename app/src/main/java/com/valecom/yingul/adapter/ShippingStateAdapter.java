package com.valecom.yingul.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_ShippingState;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

public class ShippingStateAdapter extends ArrayAdapter<Yng_ShippingState>
{
    private final Context context;
    private final ArrayList<Yng_ShippingState> values;

    public ShippingStateAdapter(Context context, ArrayList<Yng_ShippingState> values) {
        super(context, R.layout.layout_shipping_state_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_ShippingState getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_shipping_state_row, parent, false);
        Yng_ShippingState item = values.get(position);
        TextView state = (TextView) rowView.findViewById(R.id.state);
        state.setText(item.getEstado());

        return rowView;
    }
}
