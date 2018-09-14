package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rey.material.widget.Spinner;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetUbicationDetailShippingHomeBranchFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editStreet,editNumber,editFlor,editWithinStreets,editRefence;
    CheckBox checkWithoutNumber;
    private TextInputLayout lytPhoneContact;
    private LinearLayout lytEditDocument;

    public BuyItemSetUbicationDetailShippingHomeBranchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_ubication_set_detail, container, false);

        lytEditDocument = (LinearLayout) v.findViewById(R.id.lytEditDocument);
        lytPhoneContact = (TextInputLayout) v.findViewById(R.id.lytPhoneContact);
        lytPhoneContact.setVisibility(View.GONE);
        lytEditDocument.setVisibility(View.GONE);

        editStreet = (EditText) v.findViewById(R.id.editStreet);
        editNumber = (EditText) v.findViewById(R.id.editNumber);

        editFlor = (EditText) v.findViewById(R.id.editFlor);
        editWithinStreets = (EditText) v.findViewById(R.id.editWithinStreets);
        editRefence = (EditText) v.findViewById(R.id.editRefence);

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
                if(val.validarNumero(editStreet,editStreet.getText().toString())){
                    editStreet.setError("Campo requerido");
                }else if(val.validarNumero(editNumber,editNumber.getText().toString()) && !checkWithoutNumber.isChecked()){
                    editNumber.setError("Campo requerido");
                }else if(val.validarCadena(editRefence,editRefence.getText().toString())){
                    editRefence.setError("Campo requerido");
                }
                else {
                    ((BuyActivity)getActivity()).shipment.getYng_User().getYng_Ubication().setStreet(editStreet.getText().toString().trim());
                    ((BuyActivity)getActivity()).shipment.getYng_User().getYng_Ubication().setNumber(editNumber.getText().toString().trim());

                    ((BuyActivity)getActivity()).shipment.getYng_User().getYng_Ubication().setWithinStreets(editWithinStreets.getText().toString().trim());
                    ((BuyActivity)getActivity()).shipment.getYng_User().getYng_Ubication().setDepartment(editFlor.getText().toString().trim());
                    ((BuyActivity)getActivity()).shipment.getYng_User().getYng_Ubication().setAditional(editRefence.getText().toString().trim());

                    BuyItemSetShippingBranchFragment fragment = new BuyItemSetShippingBranchFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
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

        //Checks to make sure fragment is still attached to activity
        /*if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Envio");
        }*/
    }
}
