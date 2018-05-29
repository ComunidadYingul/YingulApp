package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetDelivShipFragment extends Fragment {

    RadioGroup rgEnvio;
    RadioButton rbEnvioComprador;
    RadioButton rbEnvioGratis;
    CheckBox cbRetiroPersona;
    Button buttonSetDeliveryShip;
    String productPagoEnvio="";
    String productFormDelivery="";


    public SellItemSetDelivShipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_deliv_ship, container, false);

        rgEnvio = (RadioGroup) v.findViewById(R.id.rgEnvio);
        rbEnvioComprador = (RadioButton) v.findViewById(R.id.rbEnvioComprador);
        rbEnvioGratis = (RadioButton) v.findViewById(R.id.rbEnvioGratis);
        cbRetiroPersona = (CheckBox)v.findViewById(R.id.cbRetiroPersona);

        buttonSetDeliveryShip = (Button) v.findViewById(R.id.buttonSetDeliveryShip);

        rgEnvio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbEnvioComprador.isChecked()){
                    cbRetiroPersona.setEnabled(true);
                }else{
                    cbRetiroPersona.setEnabled(false);
                    cbRetiroPersona.setChecked(false);
                }
            }
        });

        buttonSetDeliveryShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbEnvioComprador.isChecked()){
                    productPagoEnvio = "comprador";
                    if(cbRetiroPersona.isChecked()){
                        productFormDelivery = "YingulEnviosPersona";
                        ((SellActivity)getActivity()).product.setProductFormDelivery(productFormDelivery);
                    }
                }else{
                    productPagoEnvio = "gratis";
                }
                ((SellActivity)getActivity()).product.setProductPagoEnvio(productPagoEnvio);

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
