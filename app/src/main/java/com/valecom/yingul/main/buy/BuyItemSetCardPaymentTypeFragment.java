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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetCardPaymentTypeFragment extends Fragment {

    ListView list;
    ArrayAdapter adapter;
    ArrayList array_list;
    MaterialDialog progressDialog;

    public BuyItemSetCardPaymentTypeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_buy_item_set_card_payment_type, container, false);

        array_list = new ArrayList();
        if(((BuyActivity)getActivity()).card.getType().equals("CREDIT")){
            array_list.add("VISA");
            array_list.add("MasterCard");
            array_list.add("American Express");
            array_list.add("TARJETA SHOPPING");
            array_list.add("CABAL");
            array_list.add("Diners Club INTERNATIONAL");
            array_list.add("ArgenCard");
            array_list.add("Naranja");
            array_list.add("Cencosud");
        }else{
            array_list.add("VISA DEBIT");
            array_list.add("CABAL DÉBITO");
            array_list.add("MasterCard");
            array_list.add("Maestro");
        }
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(((BuyActivity)getActivity()).card.getType().equals("CREDIT")){
                    switch (position){
                        case 0:
                            ((BuyActivity)getActivity()).card.setProvider("VISA");
                            break;
                        case 1:
                            ((BuyActivity)getActivity()).card.setProvider("MASTERCARD");
                            break;
                        case 2:
                            ((BuyActivity)getActivity()).card.setProvider("AMEX");
                            break;
                        case 3:
                            ((BuyActivity)getActivity()).card.setProvider("SHOPPING");
                            break;
                        case 4:
                            ((BuyActivity)getActivity()).card.setProvider("CABAL");
                            break;
                        case 5:
                            ((BuyActivity)getActivity()).card.setProvider("DINERS");
                            break;
                        case 6:
                            ((BuyActivity)getActivity()).card.setProvider("ARGENCARD");
                            break;
                        case 7:
                            ((BuyActivity)getActivity()).card.setProvider("NARANJA");
                            break;
                        case 8:
                            ((BuyActivity)getActivity()).card.setProvider("CENCOSUD");
                            break;
                    }
                }else{
                    switch (position){
                        case 0:
                            ((BuyActivity)getActivity()).card.setProvider("VISA");
                            break;
                        case 1:
                            ((BuyActivity)getActivity()).card.setProvider("CABAL");
                            break;
                        case 2:
                            ((BuyActivity)getActivity()).card.setProvider("MASTERCARD");
                            break;
                        case 3:
                            ((BuyActivity)getActivity()).card.setProvider("MAESTRO");
                            break;
                    }
                }
                BuySetCardFragment fragment = new BuySetCardFragment();
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
