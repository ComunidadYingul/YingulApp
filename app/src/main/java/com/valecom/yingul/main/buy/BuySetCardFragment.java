package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetCardFragment extends Fragment {

    EditText editCardNumber,editFullName,editDueDate,editSecurityCode,editTitularDNI;
    Button buttonSetCard;
    String dueDate;
    public BuySetCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_card, container, false);

        editCardNumber = (EditText) v.findViewById(R.id.editCardNumber);
        editFullName = (EditText) v.findViewById(R.id.editFullName);
        editDueDate = (EditText) v.findViewById(R.id.editDueDate);
        editSecurityCode = (EditText) v.findViewById(R.id.editSecurityCode);
        editTitularDNI = (EditText) v.findViewById(R.id.editTitularDNI);

        editTitularDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String aux = editTitularDNI.getText().toString().trim();
                if (editTitularDNI.getText().toString().trim().contains(".")) {
                    aux = editTitularDNI.getText().toString().trim().replace(".", "");
                }
                if (aux.length() > 2 && aux.length() <= 5) {
                    if (aux.contains(".")) {
                        aux = aux.replace(".", "");
                    }
                    editTitularDNI.removeTextChangedListener(this);
                    editTitularDNI.setText(aux.substring(0, 2) + ".".toString() + aux.substring(2, aux.length()));
                    editTitularDNI.setSelection(editTitularDNI.getText().toString().trim().length());  // Set selection
                    editTitularDNI.addTextChangedListener(this);
                }
                if (aux.length() > 5) {
                    if (aux.contains(".")) {
                        aux = aux.replace(".", "");
                    }
                    editTitularDNI.removeTextChangedListener(this);
                    editTitularDNI.setText(aux.substring(0, 2) + "." + aux.substring(2, 5) + "." + aux.substring(5, aux.length()));
                    editTitularDNI.setSelection(editTitularDNI.getText().toString().trim().length());  // Set selection
                    editTitularDNI.addTextChangedListener(this);
                }
            }
        });

        buttonSetCard = (Button) v.findViewById(R.id.buttonSetCard);
        buttonSetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(checkValidation()) {
                ((BuyActivity) getActivity()).card.setNumber(Long.valueOf(editCardNumber.getText().toString().trim()));
                dueDate = editDueDate.getText().toString().trim();
                String[] parts = dueDate.split("/");
                ((BuyActivity) getActivity()).card.setDueMonth(Integer.parseInt(parts[0]));
                ((BuyActivity) getActivity()).card.setDueYear(2000 + Integer.parseInt(parts[1]));
                ((BuyActivity) getActivity()).card.setFullName(editFullName.getText().toString().trim());
                ((BuyActivity) getActivity()).card.setDni(Long.valueOf(editTitularDNI.getText().toString().trim().replace(".","")));
                ((BuyActivity) getActivity()).card.setSecurityCode(Integer.parseInt(editSecurityCode.getText().toString().trim()));
                ((BuyActivity) getActivity()).card.setUser(((BuyActivity) getActivity()).user);
                ((BuyActivity) getActivity()).payment.setYng_Card(((BuyActivity) getActivity()).card);
                BuyItemConfirmFragment fragment = new BuyItemConfirmFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            }
        });

        return v;
    }

    public boolean checkValidation(){
        Validacion val = new Validacion();
        if(!val.valNumRange(editCardNumber,14,16)){
            return false;
        }else if(!val.valCantString(editFullName,3)){
            return false;
        }else if(!val.valExpireDate(editDueDate)){
            return false;
        }else if(!val.valCantString(editSecurityCode,3)){
            return false;
        }else if(!val.valDni(editTitularDNI)){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
