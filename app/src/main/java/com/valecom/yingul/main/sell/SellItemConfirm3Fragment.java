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
public class SellItemConfirm3Fragment extends Fragment {

    Button buttonConfirm3;
    EditText editPrice,editTitle,editKilometer,editPhoneContact,editUbication;
    Yng_Item item = new Yng_Item();
    Yng_Ubication ubication = new Yng_Ubication();
    String typePrice, includes;

    public SellItemConfirm3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_confirm3, container, false);

        buttonConfirm3 = (Button) v.findViewById(R.id.buttonConfirm3);
        editTitle = (EditText) v.findViewById(R.id.editTitle);
        editPrice = (EditText) v.findViewById(R.id.editPrice);
        editKilometer = (EditText) v.findViewById(R.id.editKilometer);
        editPhoneContact = (EditText) v.findViewById(R.id.editPhoneContact);
        editUbication = (EditText) v.findViewById(R.id.editUbication);

        String valor = ((SellActivity)getActivity()).item.getPrice()+"";

        editTitle.setText(((SellActivity)getActivity()).item.getName());
        editPrice.setText(valor);
        editKilometer.setText(((SellActivity)getActivity()).item.getKilometer()+"");
        editPhoneContact.setText(((SellActivity)getActivity()).user.getPhone());
        editUbication.setText(ubication.getYng_Country().getName()+" . "+ubication.getYng_Province().getName()+", "+ubication.getYng_City().getName());

        buttonConfirm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).confirm();
            }
        });

        return v;
    }

}
