package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemAddContactFragment extends Fragment {

    TextView txtOtherPhone, txtAddPictures, txtPhoneContact;
    Button buttonPhoneContact;

    public SellItemAddContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_add_contact, container, false);

        txtOtherPhone = (TextView) v.findViewById(R.id.txtOtherPhone);
        txtAddPictures = (TextView) v.findViewById(R.id.txtAddPictures);
        txtPhoneContact = (TextView) v.findViewById(R.id.txtPhoneContact);

        buttonPhoneContact = (Button) v.findViewById(R.id.buttonPhoneContact);

        txtPhoneContact.setText(((SellActivity)getActivity()).user.getPhone());

        switch (((SellActivity)getActivity()).item.getType()) {
            case "Motorized":
                txtAddPictures.setText("¿Quieres que te contacten al teléfono de tu cuenta?");
                break;
            case "Property":
                txtAddPictures.setText("¿Quieres que te contacten al teléfono de tu cuenta?");
                break;
        }

        txtOtherPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellItemSetPhoneContactFragment fragment = new SellItemSetPhoneContactFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        buttonPhoneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellItemAddSummaryFragment fragment = new SellItemAddSummaryFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
