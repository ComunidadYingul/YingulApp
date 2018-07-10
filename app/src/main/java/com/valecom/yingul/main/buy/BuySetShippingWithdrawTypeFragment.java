package com.valecom.yingul.main.buy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.main.sell.SellActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetShippingWithdrawTypeFragment extends Fragment {

    ListView list;
    ArrayList array_list;
    ArrayAdapter adapter;
    TextView txtItemName,txtCurrencyPrice;

    public BuySetShippingWithdrawTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_shipping_withdraw_type, container, false);

        array_list = new ArrayList();

        if(((BuyActivity)getActivity()).item.getType().equals("Motorized")){
            array_list.add("Retiro en domicilio del vendedor (Al finalizar la compra te daremos los datos del vendedor)");
        }else if(((BuyActivity)getActivity()).item.getType().equals("Product")){
            array_list.add("Retiro en sucursal");
            if(!(((BuyActivity)getActivity()).item.getProductPagoEnvio()==null)){
                if(!((BuyActivity)getActivity()).item.getProductPagoEnvio().equals("gratis")){
                    array_list.add("Retiro en domicilio del vendedor (Al finalizar la compra te daremos los datos del vendedor)");
                }
            }
        }


        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        txtItemName = (TextView) v.findViewById(R.id.txtItemName);
        txtCurrencyPrice = (TextView) v.findViewById(R.id.txtCurrencyPrice);
        switch(((BuyActivity)getActivity()).item.getType()){
            case "Product":
                txtItemName.setText("Producto");
                break;
            case "Service":
                txtItemName.setText("Servicio");
                break;
            case "Motorized":
                txtItemName.setText("Vehículo");
                break;
            case "Property":
                txtItemName.setText("Inmueble");
                break;
        }
        txtCurrencyPrice.setText("$ "+((BuyActivity)getActivity()).item.getPrice());
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(((BuyActivity)getActivity()).item.getType().equals("Product")) {
                    if (position == 0) {
                        ((BuyActivity) getActivity()).shipping.setTypeShipping("branch");
                        BuyItemFindShippingBranchFragment fragment = new BuyItemFindShippingBranchFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        ((BuyActivity) getActivity()).shipping.setTypeShipping("home");
                        BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }else if(((BuyActivity)getActivity()).item.getType().equals("Motorized")){
                    if (position == 0){
                        ((BuyActivity) getActivity()).shipping.setTypeShipping("home");
                        BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
