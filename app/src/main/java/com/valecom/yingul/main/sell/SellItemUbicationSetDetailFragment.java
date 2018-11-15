package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.gson.Gson;
import com.rey.material.widget.Spinner;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemUbicationSetDetailFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editStreet,editNumber,editFlor,editWithinStreets,editRefence,editPhone,editDocument;
    CheckBox checkWithoutNumber;
    Spinner spinner_type_document;
    TextInputLayout lytPhoneContact;

    public SellItemUbicationSetDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_ubication_set_detail, container, false);

        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editDocument = (EditText) v.findViewById(R.id.editDocument);
        spinner_type_document = (Spinner) v.findViewById(R.id.spinner_type_document);

        editStreet = (EditText) v.findViewById(R.id.editStreet);
        editNumber = (EditText) v.findViewById(R.id.editNumber);

        editFlor = (EditText) v.findViewById(R.id.editFlor);
        editWithinStreets = (EditText) v.findViewById(R.id.editWithinStreets);
        editRefence = (EditText) v.findViewById(R.id.editRefence);

        lytPhoneContact = (TextInputLayout) v.findViewById(R.id.lytPhoneContact);

        lytPhoneContact.setVisibility(View.GONE);

        /*if(((SellActivity) getActivity()).user.getPhone().equals(null)) {
            editPhone.setText(((SellActivity) getActivity()).user.getPhone());
        }*/

        //String typeDocument[] = {"DNI","LC","CI","LE"};
        String typeDocument[] = {"DNI"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_type_document.setAdapter(spinnerArrayAdapter);

        checkWithoutNumber = (CheckBox) v.findViewById(R.id.checkWithoutNumber);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);

        spinner_type_document.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                editDocument.setText("");
            }
        });

        checkWithoutNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editNumber.setText("");
                    editNumber.setEnabled(false);
                    editNumber.setError(null);
                    editNumber.clearFocus();
                }else{
                    editNumber.setEnabled(true);
                }
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
                }
            }
        });

        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkValidation()){

                    ((SellActivity) getActivity()).ubication.setStreet(editStreet.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setNumber(editNumber.getText().toString().trim());

                    ((SellActivity) getActivity()).ubication.setWithinStreets(editWithinStreets.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setDepartment(editFlor.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setAditional(editRefence.getText().toString().trim());

                    switch (spinner_type_document.getSelectedItemPosition()) {
                        case 0:
                            ((SellActivity) getActivity()).user.setDocumentType("DNI");
                            ((SellActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim().replace(".",""));
                            break;
                        case 1:
                            ((SellActivity) getActivity()).user.setDocumentType("LC");
                            ((SellActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                        case 2:
                            ((SellActivity) getActivity()).user.setDocumentType("CI");
                            ((SellActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                        case 3:
                            ((SellActivity) getActivity()).user.setDocumentType("LE");
                            ((SellActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                    }
                    //((SellActivity) getActivity()).user.setPhone(editPhone.getText().toString().trim());
                    //((SellActivity) getActivity()).returnNewUbication();

                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(((SellActivity) getActivity()).ubication);
                    Log.e("ubica:---", jsonBody);

                    ((SellActivity)getActivity()).setUbicationDetail();
                }
            }
        });

        /*editStreet = (EditText) v.findViewById(R.id.editStreet);
        editNumber = (EditText) v.findViewById(R.id.editNumber);
        checkWithoutNumber = (CheckBox) v.findViewById(R.id.checkWithoutNumber);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);

        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String street = editStreet.getText().toString();
                String number = editNumber.getText().toString();
                Validacion val = new Validacion();
                if(val.validarCadena(editStreet,street)){
                    editStreet.setError("Ingrese calle");
                }else if(val.validarNumero(editNumber,number)){
                    editNumber.setError("Ingrese número");
                }
                else{
                    ((SellActivity)getActivity()).setUbicationDetail(editStreet.getText().toString(),editNumber.getText().toString());
                }
            }
        });*/
        return v;
    }

    public boolean checkValidation(){
        Validacion val=new Validacion();
        /*if(val.validarNumero(editPhone,editPhone.getText().toString())){
            editPhone.setError("Campo requerido");
        }else*/
        if(spinner_type_document.getSelectedItemPosition() == 0 && !val.valDni(editDocument)) {
            return false;
        }else if(spinner_type_document.getSelectedItemPosition() != 0 && !val.valNumber(editDocument)){
            return false;
        }else if(val.validarNumero(editStreet,editStreet.getText().toString())){
            editStreet.setError("Campo requerido");
            return false;
        }else if(val.validarNumero(editNumber,editNumber.getText().toString()) && !checkWithoutNumber.isChecked()){
            editNumber.setError("Campo requerido");
            return false;
        }else if(val.validarCadena(editRefence,editRefence.getText().toString())){
            editRefence.setError("Campo requerido");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Checks to make sure fragment is still attached to activity
        /*if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Envio");
        }*/
    }
}
