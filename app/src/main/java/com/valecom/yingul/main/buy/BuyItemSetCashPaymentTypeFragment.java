package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.sell.SellActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetCashPaymentTypeFragment extends Fragment {

    ListView list;
    ArrayAdapter adapter;
    ArrayList array_list;
    MaterialDialog progressDialog;

    public BuyItemSetCashPaymentTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_set_cash_payment_type, container, false);

        array_list = new ArrayList();
        array_list.add("Cobro Express");
        array_list.add("Pago Fácil");
        //array_list.add("Provincia NET");
        array_list.add("Rapipago");
        array_list.add("RIPSA red de pagos");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position){
                    case 0:
                        ((BuyActivity)getActivity()).cashPayment.setPaymentMethod("COBRO_EXPRESS");
                        break;
                    case 1:
                        ((BuyActivity)getActivity()).cashPayment.setPaymentMethod("PAGOFACIL");
                        break;
                    /*case 2:
                        ((BuyActivity)getActivity()).cashPayment.setPaymentMethod("ProvinciaNET");
                        break;*/
                    case 2:
                        ((BuyActivity)getActivity()).cashPayment.setPaymentMethod("RAPIPAGO");
                        break;
                    case 3:
                        ((BuyActivity)getActivity()).cashPayment.setPaymentMethod("RIPSA");
                        break;
                }
                Log.e("typo de cash", String.valueOf(position));
                BuyItemSetCashPaymentFragment fragment = new BuyItemSetCashPaymentFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        ////

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



}
