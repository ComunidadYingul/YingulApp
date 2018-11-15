package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.rey.material.widget.Spinner;
import com.valecom.yingul.R;
import com.google.gson.Gson;
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUbicationSetDetailFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editStreet,editNumber,editFlor,editWithinStreets,editRefence,editPhone,editDocument;
    CheckBox checkWithoutNumber;
    Spinner spinner_type_document;

    public NewUbicationSetDetailFragment() {
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

        if(((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.getPhone().equals(null)) {
            editPhone.setText(((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.getPhone());
        }

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

                if (checkValidation()) {

                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setStreet(editStreet.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setNumber(editNumber.getText().toString().trim());

                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setWithinStreets(editWithinStreets.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setDepartment(editFlor.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setAditional(editRefence.getText().toString().trim());

                    switch (spinner_type_document.getSelectedItemPosition()) {
                        case 0:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("DNI");
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim().replace(".",""));
                            break;
                        case 1:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("LC");
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                        case 2:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("CI");
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                        case 3:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("LE");
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                            break;
                    }
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setPhone(editPhone.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).returnNewUbication();

                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication);
                    Log.e("ubica:---", jsonBody);
                }
            }
        });
        return v;
    }

    public boolean checkValidation(){
        /*Validacion val=new Validacion();
        if(val.validarNumero(editPhone,editPhone.getText().toString())){
            editPhone.setError("Campo requerido");
        }else if(val.validarNumero(editDocument,editDocument.getText().toString())){
            editDocument.setError("Campo requerido");
        }else if(val.validarNumero(editStreet,editStreet.getText().toString())){
            editStreet.setError("Campo requerido");
        }else if(val.validarNumero(editNumber,editNumber.getText().toString()) && !checkWithoutNumber.isChecked()){
            editNumber.setError("Campo requerido");
        }else if(val.validarCadena(editRefence,editRefence.getText().toString())){
            editRefence.setError("Campo requerido");
        }*/

        Validacion val=new Validacion();
        if(val.validarNumero(editPhone,editPhone.getText().toString())){
            editPhone.setError("Campo requerido");
            return false;
        }else if(spinner_type_document.getSelectedItemPosition() == 0 && !val.valDni(editDocument)) {
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
