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
public class SellItemSetMotorizedYearFragment extends Fragment {

    Button buttonMotorizedYear;
    EditText editMotorizedYear;

    public SellItemSetMotorizedYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_motorized_year, container, false);

        editMotorizedYear = (EditText)v.findViewById(R.id.editMotorizedYear);
        buttonMotorizedYear = (Button)v.findViewById(R.id.buttonMotorizedYear);

        buttonMotorizedYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motorizedYear = editMotorizedYear.getText().toString();
                if(motorizedYear=="" || motorizedYear==null){
                    Toast.makeText(getActivity(),"Ingresar valor",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).item.setItemYear(Integer.parseInt(motorizedYear));

                    SellItemSetQuantityFragment fragment = new SellItemSetQuantityFragment();
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
