package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetItemTypeFragment extends Fragment {

    LinearLayout btnProduct,btnService,btnMotorized,btnProperty;

    public SellItemSetItemTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_item_type, container, false);

        btnProduct = (LinearLayout) v.findViewById(R.id.btn_product);
        btnService = (LinearLayout) v.findViewById(R.id.btn_service);
        btnMotorized = (LinearLayout) v.findViewById(R.id.btn_motorized);
        btnProperty = (LinearLayout) v.findViewById(R.id.btn_property);

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setItemType("Product");
            }
        });
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setItemType("Service");
            }
        });
        btnMotorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setItemType("Motorized");
            }
        });
        btnProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setItemType("Property");
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
