package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetDeliveryFragment extends Fragment {

    CheckBox cbEnvioExpress;
    CheckBox cbRetiroPersona;
    Button buttonSetDelivery;
    String productFormDelivery="";

    public SellItemSetDeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_delivery, container, false);

        cbEnvioExpress = (CheckBox)v.findViewById(R.id.cbEnvioExpress);
        cbRetiroPersona = (CheckBox)v.findViewById(R.id.cbRetiroPersona);
        buttonSetDelivery = (Button) v.findViewById(R.id.buttonSetDelivery);

        buttonSetDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbEnvioExpress.isChecked()){
                    productFormDelivery = "YingulEnvios";
                    ((SellActivity)getActivity()).product.setProductFormDelivery(productFormDelivery);

                    SellItemSetDelivShipFragment fragment = new SellItemSetDelivShipFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        return v;
    }

}
