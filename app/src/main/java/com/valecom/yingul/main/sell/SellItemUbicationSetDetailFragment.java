package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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

        String typeDocument[] = {"DNI","LC","CI","LE"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_type_document.setAdapter(spinnerArrayAdapter);

        checkWithoutNumber = (CheckBox) v.findViewById(R.id.checkWithoutNumber);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);

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

        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val=new Validacion();
                /*if(val.validarNumero(editPhone,editPhone.getText().toString())){
                    editPhone.setError("Campo requerido");
                }else*/
                if(val.validarNumero(editDocument,editDocument.getText().toString())){
                    editDocument.setError("Campo requerido");
                }else if(val.validarNumero(editStreet,editStreet.getText().toString())){
                    editStreet.setError("Campo requerido");
                }else if(val.validarNumero(editNumber,editNumber.getText().toString()) && !checkWithoutNumber.isChecked()){
                    editNumber.setError("Campo requerido");
                }else if(val.validarCadena(editRefence,editRefence.getText().toString())){
                    editRefence.setError("Campo requerido");
                }
                else {

                    ((SellActivity) getActivity()).ubication.setStreet(editStreet.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setNumber(editNumber.getText().toString().trim());

                    ((SellActivity) getActivity()).ubication.setWithinStreets(editWithinStreets.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setDepartment(editFlor.getText().toString().trim());
                    ((SellActivity) getActivity()).ubication.setAditional(editRefence.getText().toString().trim());

                    switch (spinner_type_document.getSelectedItemPosition()) {
                        case 0:
                            ((SellActivity) getActivity()).user.setDocumentType("DNI");
                            break;
                        case 1:
                            ((SellActivity) getActivity()).user.setDocumentType("LC");
                            break;
                        case 2:
                            ((SellActivity) getActivity()).user.setDocumentType("CI");
                            break;
                        case 3:
                            ((SellActivity) getActivity()).user.setDocumentType("LE");
                            break;
                    }
                    //((SellActivity) getActivity()).user.setPhone(editPhone.getText().toString().trim());
                    ((SellActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
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
