package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetQuantityFragment extends Fragment {

    Button buttonQuantity;
    EditText editQuantity;

    public SellItemSetQuantityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_quantity, container, false);

        editQuantity = (EditText)v.findViewById(R.id.editQuantity);
        buttonQuantity = (Button)v.findViewById(R.id.buttonQuantity);

        buttonQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = editQuantity.getText().toString();
                if(quantity=="" || quantity==null){
                    Toast.makeText(getActivity(),"Ingresar Cantidad",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).item.setQuantity(Integer.parseInt(quantity));

                    SellItemSetPriceFragment fragment = new SellItemSetPriceFragment();
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
