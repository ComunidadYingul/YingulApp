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
import com.valecom.yingul.model.Client;

import java.util.ArrayList;


public class ClientAdapter extends ArrayAdapter<Client>
{
    private final Context context;
    private final ArrayList<Client> values;
    private String currency;

    public ClientAdapter(Context context, ArrayList<Client> values) {
        super(context, R.layout.layout_client_row, values);

        this.context = context;
        this.values = values;

        SharedPreferences settings = context.getSharedPreferences(LoginActivity.SESSION_USER, context.MODE_PRIVATE);
        currency = settings.getString(SettingActivity.CURRENCY_SYMBOL_KEY, "$");
    }

    public Client getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_client_row, parent, false);

        TextView text_name = (TextView) rowView.findViewById(R.id.text_name);
        TextView text_total = (TextView) rowView.findViewById(R.id.text_total);

        text_name.setText(values.get(position).Name);
        text_total.setText(currency + String.format( "%.2f", values.get(position).TotalMoney));

        return rowView;
    }
}
