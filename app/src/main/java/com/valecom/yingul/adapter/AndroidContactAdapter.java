package com.valecom.yingul.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_AndroidContact;
import com.valecom.yingul.network.Network;

import java.util.ArrayList;

public class AndroidContactAdapter extends ArrayAdapter<Yng_AndroidContact>
{
    private final Context context;
    private final ArrayList<Yng_AndroidContact> values;

    public AndroidContactAdapter(Context context, ArrayList<Yng_AndroidContact> values) {
        super(context, R.layout.layout_androidcontact_row, values);

        this.context = context;
        this.values = values;

    }

    public Yng_AndroidContact getItem(int position)
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

        View rowView = inflater.inflate(R.layout.layout_androidcontact_row, parent, false);

        Yng_AndroidContact item = values.get(position);
        ImageView profilePhotoContact = (ImageView) rowView.findViewById(R.id.profilePhotoContact);
        TextView nameContact = (TextView) rowView.findViewById(R.id.nameContact);
        TextView phoneNumberContact = (TextView) rowView.findViewById(R.id.phoneNumberContact);
        TextView textInvite = (TextView) rowView.findViewById(R.id.textInvite);
        if(item.getUser()==null){
            Picasso.with(context).load(Network.BUCKET_URL+"user/userProfile/profile.jpg").into(profilePhotoContact);
            textInvite.setVisibility(View.VISIBLE);
        }else{
            Log.e("photo",Network.BUCKET_URL+"user/userProfile/"+item.getUser().getProfilePhoto());
            Picasso.with(context).load(Network.BUCKET_URL+"user/userProfile/"+item.getUser().getProfilePhoto()).into(profilePhotoContact);
            textInvite.setVisibility(View.INVISIBLE);
        }
        nameContact.setText(item.getAndroid_contact_Name());
        phoneNumberContact.setText(item.getAndroid_contact_TelefonNr());

        return rowView;
    }
}
