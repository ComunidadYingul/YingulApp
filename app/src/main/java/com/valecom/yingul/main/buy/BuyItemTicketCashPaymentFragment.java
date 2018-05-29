package com.valecom.yingul.main.buy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Payment;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemTicketCashPaymentFragment extends Fragment {

    TextView txtTitle;
    Button btnShowTicket,btnDownloadTicket;

    public BuyItemTicketCashPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_ticket_cash_payment, container, false);

        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtTitle.setText("Pagá $(ARS) "+((BuyActivity)getActivity()).paymentToTicket.getValue()+" en "+((BuyActivity)getActivity()).paymentToTicket.getCashPayment().getPaymentMethod()+" para completar tu compra");
        btnShowTicket = (Button) v.findViewById(R.id.btnShowTicket);
        btnDownloadTicket = (Button) v.findViewById(R.id.btnDownloadTicket);

        btnShowTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(((BuyActivity)getActivity()).paymentToTicket.getCashPayment().getURL_PAYMENT_RECEIPT_HTML()));
                startActivity(viewIntent);
            }
        });

        btnDownloadTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(((BuyActivity)getActivity()).paymentToTicket.getCashPayment().getURL_PAYMENT_RECEIPT_PDF()));
                startActivity(viewIntent);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
