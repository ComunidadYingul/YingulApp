package com.valecom.yingul.main.buy;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetPaymentTypeFragment extends Fragment {

    private LinearLayout btn_creditCard,btn_debitCard,btn_cash,lytContainer,lytSubcont1,lytSubcont2;
    DisplayMetrics metrics = new DisplayMetrics();

    public BuySetPaymentTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_payment_type, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        btn_creditCard = (LinearLayout) v.findViewById(R.id.btn_creditCard);
        btn_debitCard = (LinearLayout) v.findViewById(R.id.btn_debitCard);
        btn_cash = (LinearLayout) v.findViewById(R.id.btn_cash);
        lytContainer = (LinearLayout) v.findViewById(R.id.lytContainer);
        lytSubcont1 = (LinearLayout) v.findViewById(R.id.lytSubcont1);
        lytSubcont2 = (LinearLayout) v.findViewById(R.id.lytSubcont2);
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,0);
            params.weight = 3;
            lytSubcont1.setLayoutParams(params);
            lytSubcont2.setLayoutParams(params);
            lytContainer.setOrientation(LinearLayout.VERTICAL);
        }else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 4;
            lytSubcont1.setLayoutParams(params);
            params.weight = 2;
            lytSubcont2.setLayoutParams(params);
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
