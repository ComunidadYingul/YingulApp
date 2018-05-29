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
public class SellItemSetAreaFragment extends Fragment {

    Button buttonArea;
    EditText editArea;

    public SellItemSetAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_area, container, false);

        editArea = (EditText)v.findViewById(R.id.editArea);
        buttonArea = (Button)v.findViewById(R.id.buttonArea);

        buttonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = editArea.getText().toString();
                if(area=="" || area==null){
                    Toast.makeText(getActivity(),"Ingresar marca",Toast.LENGTH_SHORT).show();
                }else{
                    ((SellActivity)getActivity()).property.setPropertyTotalArea(area);

                    SellItemSetAreaDuildFragment fragment = new SellItemSetAreaDuildFragment();
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
