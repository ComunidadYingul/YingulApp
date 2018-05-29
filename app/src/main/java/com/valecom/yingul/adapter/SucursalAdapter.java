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
import com.valecom.yingul.model.Sucursal;

import java.util.ArrayList;

/**
 * Created by gonzalo on 13-05-18.
 */

public class SucursalAdapter extends ArrayAdapter<Sucursal>{

    private final Context context;
    private final ArrayList<Sucursal> values;
    private String currency;


    public SucursalAdapter(Context context, ArrayList<Sucursal> values) {
        super(context, R.layout.layout_sucursal_row, values);

        this.context = context;
        this.values = values;

        SharedPreferences settings = context.getSharedPreferences(LoginActivity.SESSION_USER, context.MODE_PRIVATE);
        currency = settings.getString(SettingActivity.CURRENCY_SYMBOL_KEY, "$");
    }

    public Sucursal getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_sucursal_row, parent, false);

        TextView text_company = (TextView) rowView.findViewById(R.id.text_company);
        TextView text_ubication = (TextView) rowView.findViewById(R.id.text_ubication);
        TextView text_horary = (TextView) rowView.findViewById(R.id.text_horary);
        TextView text_price = (TextView) rowView.findViewById(R.id.text_price);

        text_company.setText(values.get(position).company);
        text_ubication.setText(values.get(position).ubication);
        text_horary.setText(values.get(position).horary);
        text_price.setText(values.get(position).price);

        return rowView;
    }
}
