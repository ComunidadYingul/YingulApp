package com.valecom.yingul.main.sell;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
    LinearLayout lytContainer,lytSubcont1,lytSubcont2;
    DisplayMetrics metrics = new DisplayMetrics();

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

        lytContainer = (LinearLayout) v.findViewById(R.id.lytContainer);
        lytSubcont1 = (LinearLayout) v.findViewById(R.id.lytSubcont1);
        lytSubcont2 = (LinearLayout) v.findViewById(R.id.lytSubcont2);

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

        setResponsive2();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setResponsive();
    }

    public void setResponsive(){
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lytContainer.setOrientation(LinearLayout.VERTICAL);

        }else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            lytContainer.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    public void setResponsive2(){
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lytContainer.setOrientation(LinearLayout.HORIZONTAL);

        }else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            lytContainer.setOrientation(LinearLayout.VERTICAL);
        }
    }

}
