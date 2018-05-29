package com.valecom.yingul.main.buy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetShippingTypeFragment extends Fragment {

    TextView txtWithdrawProduct;

    public BuySetShippingTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_shipping_type, container, false);

        txtWithdrawProduct = (TextView) v.findViewById(R.id.txtWithdrawProduct);

        txtWithdrawProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySetShippingWithdrawTypeFragment fragment = new BuySetShippingWithdrawTypeFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
