package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetAmenitiesFragment extends Fragment {

    Button buttonAmenities;
    EditText editAmenities;

    public SellItemSetAmenitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_amenities, container, false);

        editAmenities = (EditText)v.findViewById(R.id.editAmenities);
        buttonAmenities = (Button)v.findViewById(R.id.buttonAmenities);

        buttonAmenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amenities = editAmenities.getText().toString();
                Validacion val = new Validacion();
                if(val.validarNumero(editAmenities,amenities)){
                    editAmenities.setError("Ingresar número de ambientes");
                }else if(Integer.parseInt(amenities) > 10000){
                    editAmenities.setError("El número ingresado es demasiado grande");
                }
                else{
                    ((SellActivity)getActivity()).item.setAmbientes(Integer.parseInt(amenities));

                    SellItemSetPriceFragment fragment = new SellItemSetPriceFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return v;
    }

}
