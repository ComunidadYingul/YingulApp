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
import com.valecom.yingul.model.Yng_Notification;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;


public class NotificationAdapter extends ArrayAdapter<Yng_Notification>
{
    private final Context context;
    private final ArrayList<Yng_Notification> values;

    public NotificationAdapter(Context context, ArrayList<Yng_Notification> values) {
        super(context, R.layout.layout_notification_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_Notification getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_notification_row, parent, false);

        Yng_Notification notification = values.get(position);
        ImageView itemImage = (ImageView) rowView.findViewById(R.id.notficationImage);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        Picasso.with(context).load(Network.BUCKET_URL+notification.getItem().getPrincipalImage()).into(itemImage);
        title.setText(notification.getTitle());
        description.setText(notification.getDescription());
        date.setText(notification.getDate());

        return rowView;
    }
}
