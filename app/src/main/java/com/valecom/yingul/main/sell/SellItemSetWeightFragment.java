package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetWeightFragment extends Fragment {

    Button buttonWeight;
    EditText editWeight;

    public SellItemSetWeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_weight, container, false);

        editWeight = (EditText)v.findViewById(R.id.editWeight);
        buttonWeight = (Button)v.findViewById(R.id.buttonWeight);

        buttonWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = editWeight.getText().toString();
                if(weight=="" || weight==null){
                    Toast.makeText(getActivity(),"Ingresar peso",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).product.setProductWeight(Integer.parseInt(weight));

                    SellItemSetConditionFragment fragment = new SellItemSetConditionFragment();
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
