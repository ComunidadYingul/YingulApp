package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

        buttonSetCard = (Button) v.findViewById(R.id.buttonSetCard);
        buttonSetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if(val.valNumDig(editCardNumber,16) && val.valCantString(editFullName,5) && val.valExpireDate(editDueDate) && val.valCantString(editSecurityCode,3) && val.valNumDig(editTitularDNI,8)) {
                    ((BuyActivity) getActivity()).card.setNumber(Long.valueOf(editCardNumber.getText().toString().trim()));
                    dueDate = editDueDate.getText().toString().trim();
                    String[] parts = dueDate.split("/");
                    ((BuyActivity) getActivity()).card.setDueMonth(Integer.parseInt(parts[0]));
                    ((BuyActivity) getActivity()).card.setDueYear(2000 + Integer.parseInt(parts[1]));
                    ((BuyActivity) getActivity()).card.setFullName(editFullName.getText().toString().trim());
                    ((BuyActivity) getActivity()).card.setDni(Long.valueOf(editTitularDNI.getText().toString().trim()));
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

    @Override
    public void onResume() {
        super.onResume();

    }

}
