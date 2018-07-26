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
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;
import com.rey.material.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetCashPaymentFragment extends Fragment {

    Button buttonSetCashPayment;
    EditText editDocument;
    Spinner spinner_type_document;

    public BuyItemSetCashPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_set_cash_payment, container, false);

        editDocument = (EditText) v.findViewById(R.id.editDocument);
        buttonSetCashPayment = (Button) v.findViewById(R.id.buttonSetCashPayment);
        spinner_type_document = (Spinner) v.findViewById(R.id.spinner_type_document);

        String typeDocument[] = {"DNI","CUIT"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_type_document.setAdapter(spinnerArrayAdapter);

        buttonSetCashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if(val.valNumMayorADig(editDocument,8)) {
                    switch (spinner_type_document.getSelectedItemPosition()) {
                        case 0:
                            ((BuyActivity) getActivity()).cashPayment.setDocumentType("DNI");
                            break;
                        case 1:
                            ((BuyActivity) getActivity()).cashPayment.setDocumentType("CUIT");
                            break;
                    }
                    ((BuyActivity) getActivity()).cashPayment.setDocumentNumber(editDocument.getText().toString().trim());
                    ((BuyActivity) getActivity()).payment.setCashPayment(((BuyActivity) getActivity()).cashPayment);
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
