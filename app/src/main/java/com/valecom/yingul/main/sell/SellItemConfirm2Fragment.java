package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Ubication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemConfirm2Fragment extends Fragment {

    Button buttonConfirm2;
    EditText editPrice,editTitle,editDescription,editCondition;
    Yng_Item item = new Yng_Item();
    Yng_Ubication ubication = new Yng_Ubication();
    String typePrice, includes;


    public SellItemConfirm2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_confirm2, container, false);

        buttonConfirm2 = (Button) v.findViewById(R.id.buttonConfirm2);
        editTitle = (EditText) v.findViewById(R.id.editTitle);
        editPrice = (EditText) v.findViewById(R.id.editPrice);
        editDescription = (EditText) v.findViewById(R.id.editDescription);
        editCondition = (EditText) v.findViewById(R.id.editCondition);

        String valor = ((SellActivity)getActivity()).item.getPrice()+"";
        double precio = Double.parseDouble(valor);
        editTitle.setText(((SellActivity)getActivity()).item.getName());
        editPrice.setText(valor);
        editDescription.setText(((SellActivity)getActivity()).aditionalDescription);
        if(((SellActivity)getActivity()).product.getProductCondition().equals("Nuevo"))
            editCondition.setText("Nuevo");
        else
            editCondition.setText("Usado");

        buttonConfirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).confirm();
            }
        });

        return v;
    }

}
