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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Yng_Item>
{
    private final Context context;
    private final ArrayList<Yng_Item> values;

    public ItemAdapter(Context context, ArrayList<Yng_Item> values) {
        super(context, R.layout.layout_item_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_Item getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_item_row, parent, false);

        Yng_Item item = values.get(position);

        ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemImage);
        TextView itemPrice = (TextView) rowView.findViewById(R.id.itemPrice);
        TextView itemName = (TextView) rowView.findViewById(R.id.itemName);
        TextView itemPriceNormal = (TextView) rowView.findViewById(R.id.itemPriceNormal);
        TextView discountRate = (TextView) rowView.findViewById(R.id.discountRate);
        LinearLayout freeShipping = (LinearLayout) rowView.findViewById(R.id.freeShipping);

        Picasso.with(context).load(Network.BUCKET_URL+item.getPrincipalImage()).into(itemImage);

        if(item.getPriceDiscount()==0){
            itemPriceNormal.setVisibility(View.GONE);
            discountRate.setVisibility(View.GONE);
        }else{
            itemPriceNormal.setVisibility(View.VISIBLE);
            discountRate.setVisibility(View.VISIBLE);
            discountRate.setText((int)(100-(item.getPriceDiscount()*100/item.getPriceNormal()))+"% OFF");
            switch (item.getMoney()){
                case "ARS":
                    itemPriceNormal.setText("$ "+String.format("%.0f",item.getPriceNormal()));
                    break;
                case "USD":
                    itemPriceNormal.setText("U$D "+String.format("%.0f",item.getPriceNormal()));
                    break;
            }
            itemPriceNormal.setPaintFlags(itemPriceNormal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        switch (item.getMoney()){
            case "ARS":
                itemPrice.setText("$ "+String.format("%.0f",item.getPrice()));
                break;
            case "USD":
                itemPrice.setText("U$D "+String.format("%.0f",item.getPrice()));
                break;
        }

        itemName.setText(item.getName());

        if(item.getProductPagoEnvio()==null||!item.getProductPagoEnvio().equals("gratis")){
            freeShipping.setVisibility(View.GONE);
        }else{
            freeShipping.setVisibility(View.VISIBLE);
        }


        return rowView;
    }
}
