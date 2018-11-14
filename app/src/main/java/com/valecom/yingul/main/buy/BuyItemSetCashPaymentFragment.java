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

        spinner_type_document.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                editDocument.setText("");
            }
        });

        editDocument.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(spinner_type_document.getSelectedItemPosition() == 0) {
                    String aux = editDocument.getText().toString().trim();
                    if (editDocument.getText().toString().trim().contains(".")) {
                        aux = editDocument.getText().toString().trim().replace(".", "");
                    }
                    if (aux.length() > 2 && aux.length() <= 5) {
                        if (aux.contains(".")) {
                            aux = aux.replace(".", "");
                        }
                        editDocument.removeTextChangedListener(this);
                        editDocument.setText(aux.substring(0, 2) + ".".toString() + aux.substring(2, aux.length()));
                        editDocument.setSelection(editDocument.getText().toString().trim().length());  // Set selection
                        editDocument.addTextChangedListener(this);
                    }
                    if (aux.length() > 5) {
                        if (aux.contains(".")) {
                            aux = aux.replace(".", "");
                        }
                        editDocument.removeTextChangedListener(this);
                        editDocument.setText(aux.substring(0, 2) + "." + aux.substring(2, 5) + "." + aux.substring(5, aux.length()));
                        editDocument.setSelection(editDocument.getText().toString().trim().length());  // Set selection
                        editDocument.addTextChangedListener(this);
                    }
                }else if(spinner_type_document.getSelectedItemPosition() == 1){
                    String aux = editDocument.getText().toString().trim();
                    if (editDocument.getText().toString().trim().contains("-")) {
                        aux = editDocument.getText().toString().trim().replace("-", "");
                    }
                    if (aux.length() > 2 && aux.length() <= 10) {
                        if (aux.contains("-")) {
                            aux = aux.replace("-", "");
                        }
                        editDocument.removeTextChangedListener(this);
                        editDocument.setText(aux.substring(0, 2) + "-".toString() + aux.substring(2, aux.length()));
                        editDocument.setSelection(editDocument.getText().toString().trim().length());  // Set selection
                        editDocument.addTextChangedListener(this);
                    }
                    if (aux.length() > 10) {
                        if (aux.contains("-")) {
                            aux = aux.replace("-", "");
                        }
                        editDocument.removeTextChangedListener(this);
                        editDocument.setText(aux.substring(0, 2) + "-" + aux.substring(2, 10) + "-" + aux.substring(10, aux.length()));
                        editDocument.setSelection(editDocument.getText().toString().trim().length());  // Set selection
                        editDocument.addTextChangedListener(this);
                    }
                }
            }
        });

        buttonSetCashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();//editDocument
                if(spinner_type_document.getSelectedItemPosition() == 0 && val.valDni(editDocument)) {

                    ((BuyActivity) getActivity()).cashPayment.setDocumentType("DNI");
                    setCashPayment();

                }else if(spinner_type_document.getSelectedItemPosition() == 1 && val.valCuit(editDocument)){
                    ((BuyActivity) getActivity()).cashPayment.setDocumentType("CUIT");
                    setCashPayment();
                }

            }
        });

        return v;
    }

    public void setCashPayment(){
        /*      verificar y quitar caracteres (-) ó (.)        */
        if(editDocument.getText().toString().contains(".")) {
            ((BuyActivity) getActivity()).cashPayment.setDocumentNumber(editDocument.getText().toString().trim().replace(".",""));
        }else if (editDocument.getText().toString().contains("-")){
            ((BuyActivity) getActivity()).cashPayment.setDocumentNumber(editDocument.getText().toString().trim().replace("-",""));
        }
        /*     */
        ((BuyActivity) getActivity()).payment.setCashPayment(((BuyActivity) getActivity()).cashPayment);
        BuyItemConfirmFragment fragment = new BuyItemConfirmFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
