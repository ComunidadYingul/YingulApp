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
public class SellItemSetKilometerFragment extends Fragment {

    Button buttonKilometer;
    EditText editKilometer;

    public SellItemSetKilometerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_kilometer, container, false);

        editKilometer = (EditText)v.findViewById(R.id.editKilometer);
        buttonKilometer = (Button)v.findViewById(R.id.buttonKilometer);

        buttonKilometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kilometer = editKilometer.getText().toString();
                if(kilometer=="" || kilometer==null){
                    Toast.makeText(getActivity(),"Ingresar peso",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).item.setKilometer(Integer.parseInt(kilometer));

                    SellItemSetBrandFragment fragment = new SellItemSetBrandFragment();
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
