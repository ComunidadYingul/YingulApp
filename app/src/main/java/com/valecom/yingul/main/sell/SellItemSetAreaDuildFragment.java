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
public class SellItemSetAreaDuildFragment extends Fragment {

    Button buttonAreaDuild;
    EditText editAreaDuild;

    public SellItemSetAreaDuildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_area_duild, container, false);

        editAreaDuild = (EditText)v.findViewById(R.id.editAreaDuild);
        buttonAreaDuild = (Button)v.findViewById(R.id.buttonAreaDuild);

        buttonAreaDuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String areaDuild = editAreaDuild.getText().toString();
                Validacion val = new Validacion();
                if(val.validarNumero(editAreaDuild,areaDuild)){
                    editAreaDuild.setError("Ingrese la superficie cubierta");
                }else if(Long.parseLong(areaDuild) > Long.parseLong(((SellActivity)getActivity()).property.getPropertyTotalArea())){
                    editAreaDuild.setError("La superficie cubierta no debe sobrepasar a la superficie total");
                }
                else{
                    ((SellActivity)getActivity()).item.setDuildedArea(Integer.parseInt(areaDuild));

                    SellItemSetAmenitiesFragment fragment = new SellItemSetAmenitiesFragment();
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
