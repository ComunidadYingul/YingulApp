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
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetPhoneContactFragment extends Fragment {

    Button buttonPhoneContact;
    EditText editPhoneContact;

    public SellItemSetPhoneContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_phone_contact, container, false);

        editPhoneContact = (EditText)v.findViewById(R.id.editPhoneContact);
        buttonPhoneContact = (Button)v.findViewById(R.id.buttonPhoneContact);

        buttonPhoneContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneContact = editPhoneContact.getText().toString();
                Validacion val = new Validacion();
                if(val.validarNumero(editPhoneContact,phoneContact)){
                    editPhoneContact.setError("Ingrese teléfono");
                }else{
                    ((SellActivity)getActivity()).user.setPhone(phoneContact);

                    SellItemAddSummaryFragment fragment = new SellItemAddSummaryFragment();
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
