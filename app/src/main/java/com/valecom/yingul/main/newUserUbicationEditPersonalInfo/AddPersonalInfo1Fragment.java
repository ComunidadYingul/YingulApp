package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import com.valecom.yingul.R;
import com.rey.material.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPersonalInfo1Fragment extends Fragment {

    Button buttonSetPersonalInfo1;
    EditText editPhone,editDocument;
    Spinner spinner_type_document;


    public AddPersonalInfo1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_personal_info1, container, false);
        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editDocument = (EditText) v.findViewById(R.id.editDocument);
        buttonSetPersonalInfo1 = (Button) v.findViewById(R.id.buttonSetPersonalInfo1);
        spinner_type_document = (Spinner) v.findViewById(R.id.spinner_type_document);

        editPhone.setText(((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.getPhone());

        String typeDocument[] = {"DNI","LC","CI","LE"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_type_document.setAdapter(spinnerArrayAdapter);

        buttonSetPersonalInfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spinner_type_document.getSelectedItemPosition()) {
                    case 0:  ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setDocumentType("DNI");
                        break;
                    case 1:  ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setDocumentType("LC");
                        break;
                    case 2:  ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setDocumentType("CI");
                        break;
                    case 3:  ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setDocumentType("LE");
                        break;
                }
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setPhone(editPhone.getText().toString().trim());
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).user.setDocumentNumber(editDocument.getText().toString().trim());
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).returnNewUbication();
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
