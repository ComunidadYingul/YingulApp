package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

        editPhone.setText(((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.getPhone());
        String typeDocument[] = {"DNI","LC","CI","LE"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_type_document.setAdapter(spinnerArrayAdapter);

        checkWithoutNumber = (CheckBox) v.findViewById(R.id.checkWithoutNumber);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);
        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val=new Validacion();
                if(val.validarNumero(editPhone,editPhone.getText().toString())){
                    editPhone.setError("Campo requerido");
                }else if(val.validarNumero(editDocument,editDocument.getText().toString())){
                    editDocument.setError("Campo requerido");
                }else if(val.validarNumero(editStreet,editStreet.getText().toString())){
                    editStreet.setError("Campo requerido");
                }else if(val.validarCadena(editFlor,editFlor.getText().toString())){
                    editFlor.setError("Campo requerido");
                }else if(val.validarCadena(editWithinStreets,editWithinStreets.getText().toString())){
                    editWithinStreets.setError("Campo requerido");
                }else if(val.validarCadena(editRefence,editRefence.getText().toString())){
                    editRefence.setError("Campo requerido");
                }
                else {

                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setStreet(editStreet.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setNumber(editNumber.getText().toString().trim());

                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setWithinStreets(editWithinStreets.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setDepartment(editFlor.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication.setAditional(editRefence.getText().toString().trim());

                    switch (spinner_type_document.getSelectedItemPosition()) {
                        case 0:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("DNI");
                            break;
                        case 1:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("LC");
                            break;
                        case 2:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("CI");
                            break;
                        case 3:
                            ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentType("LE");
                            break;
                    }
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setPhone(editPhone.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                    ((NewUserUbicationEditPersonalInfoActivity) getActivity()).returnNewUbication();

                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(((NewUserUbicationEditPersonalInfoActivity) getActivity()).ubication);
                    Log.e("ubica:---", jsonBody);

                    /*NewUbicationSetDetail1Fragment fragment = new NewUbicationSetDetail1Fragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                }
            }
        });
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
