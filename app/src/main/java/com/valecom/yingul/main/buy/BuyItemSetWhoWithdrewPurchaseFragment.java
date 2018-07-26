package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.valecom.yingul.R;
import com.rey.material.widget.Spinner;
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetWhoWithdrewPurchaseFragment extends Fragment {

    Button buttonSetWhoWithdrew;
    EditText editName,editLastName,editPhone;

    public BuyItemSetWhoWithdrewPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_set_who_withdrew_purchase, container, false);
        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editName = (EditText) v.findViewById(R.id.editName);
        editLastName = (EditText) v.findViewById(R.id.editLastName);
        buttonSetWhoWithdrew = (Button) v.findViewById(R.id.buttonSetWhoWithdrew);
        buttonSetWhoWithdrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if(val.valStringFiveChar(editName) && val.valStringFiveChar(editLastName) && val.valNumMayorADig(editPhone,8)){
                    ((BuyActivity) getActivity()).shipping.setNameContact(editName.getText().toString().trim());
                    ((BuyActivity) getActivity()).shipping.setLastName(editLastName.getText().toString().trim());
                    ((BuyActivity) getActivity()).shipping.setPhoneContact(editPhone.getText().toString().trim());
                    BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
