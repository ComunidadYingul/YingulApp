package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetPaymentTypeFragment extends Fragment {

    LinearLayout btn_creditCard,btn_debitCard,btn_cash;

    public BuySetPaymentTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_payment_type, container, false);

        btn_creditCard = (LinearLayout) v.findViewById(R.id.btn_creditCard);
        btn_debitCard = (LinearLayout) v.findViewById(R.id.btn_debitCard);
        btn_cash = (LinearLayout) v.findViewById(R.id.btn_cash);
        btn_creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity)getActivity()).payment.setName("CARDPAYMENT");
                ((BuyActivity)getActivity()).payment.setType("CARD");
                ((BuyActivity)getActivity()).payment.setCashPayment(null);
                ((BuyActivity)getActivity()).card.setType("CREDIT");
                BuyItemSetCardPaymentTypeFragment fragment = new BuyItemSetCardPaymentTypeFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btn_debitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity)getActivity()).payment.setName("CARDPAYMENT");
                ((BuyActivity)getActivity()).payment.setType("CARD");
                ((BuyActivity)getActivity()).payment.setCashPayment(null);
                ((BuyActivity)getActivity()).card.setType("DEBIT");
                BuyItemSetCardPaymentTypeFragment fragment = new BuyItemSetCardPaymentTypeFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        btn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity)getActivity()).payment.setName("CASHPAYMENT");
                ((BuyActivity)getActivity()).payment.setType("CASH");
                ((BuyActivity)getActivity()).payment.setYng_Card(null);
                BuyItemSetCashPaymentTypeFragment fragment = new BuyItemSetCashPaymentTypeFragment();
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
