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

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetBrandFragment extends Fragment {

    Button buttonBrand;
    EditText editBrand, editModel;

    public SellItemSetBrandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_brand, container, false);

        editBrand = (EditText)v.findViewById(R.id.editBrand);
        editModel = (EditText)v.findViewById(R.id.editModel);
        buttonBrand = (Button)v.findViewById(R.id.buttonBrand);

        buttonBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = editBrand.getText().toString();
                String model = editModel.getText().toString();
                if(brand=="" || brand==null){
                    Toast.makeText(getActivity(),"Ingresar marca",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).motorized.setMotorizedBrand(brand);
                    ((SellActivity)getActivity()).motorized.setMotorizedModel(model);

                    SellItemSetMotorizedYearFragment fragment = new SellItemSetMotorizedYearFragment();
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
