package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QueryChatAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_Buy;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyAccountPurchaseDetailFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Yng_Buy buy;
    private TextView buyTime,txtItemType,txtCurrencyPrice,txtShippingCost,txtTypeOSchedules,txtTotal,txtItemName,txtQuantity,txtBranchName,txtBranchStreet,txtPayment;
    private ImageView principalImage,imgShipping,imgPayment;
    private LinearLayout layoutShipping,layBranch,downloadTicket;

    public MyAccountPurchaseDetailFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountPurchaseDetailFragment newInstance(String param1, String param2)
    {
        MyAccountPurchaseDetailFragment fragment = new MyAccountPurchaseDetailFragment();
        return fragment;
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
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_account_purchase_detail, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(getActivity(), LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
        }

        Bundle bundle = getArguments();
        Gson gson = new Gson();
        buy = gson.fromJson(bundle.getString("buy"), Yng_Buy.class);

        buyTime = (TextView) view.findViewById(R.id.buyTime);
        txtItemType = (TextView) view.findViewById(R.id.txtItemType);
        txtCurrencyPrice = (TextView) view.findViewById(R.id.txtCurrencyPrice);
        txtShippingCost = (TextView) view.findViewById(R.id.txtShippingCost);
        imgShipping = (ImageView) view.findViewById(R.id.imgShipping);
        layoutShipping = (LinearLayout) view.findViewById(R.id.layoutShipping);
        layBranch = (LinearLayout) view.findViewById(R.id.layBranch);
        txtTypeOSchedules = (TextView) view.findViewById(R.id.txtTypeOSchedules);
        principalImage = (ImageView) view.findViewById(R.id.principalImage);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        txtBranchName = (TextView) view.findViewById(R.id.txtBranchName);
        txtBranchStreet = (TextView) view.findViewById(R.id.txtBranchStreet);
        imgPayment = (ImageView) view.findViewById(R.id.imgPayment);
        txtPayment = (TextView) view.findViewById(R.id.txtPayment);
        downloadTicket = (LinearLayout) view.findViewById(R.id.downloadTicket);

        txtQuantity.setText(String.valueOf(buy.getQuantity()));
        txtItemName.setText(buy.getYng_item().getName());
        buyTime.setText(buy.getTime());
        txtCurrencyPrice.setText("$ "+buy.getItemCost());
        txtTotal.setText("$ "+buy.getCost());
        Picasso.with(getActivity()).load(Network.BUCKET_URL+buy.getYng_item().getPrincipalImage()).into(principalImage);

        if(buy.getShipping()==null){
            imgShipping.setImageResource(R.drawable.home);
            layoutShipping.setVisibility(View.GONE);
            layBranch.setVisibility(LinearLayout.GONE);
            txtTypeOSchedules.setText("Retiro en el domicilio del vendedor");
        }else{
            imgShipping.setImageResource(R.drawable.branch);
            layoutShipping.setVisibility(View.VISIBLE);
            layBranch.setVisibility(View.VISIBLE);
            if(buy.getYng_item().getProductPagoEnvio().equals("gratis")){
                txtShippingCost.setText("GRATIS");
            }else{
                txtShippingCost.setText("$ "+buy.getShippingCost());
            }
            txtTypeOSchedules.setText(buy.getShipping().getYng_Quote().getYng_Branch().getSchedules());
            txtBranchName.setText("Sucursal "+buy.getShipping().getYng_Quote().getYng_Branch().getNameMail()+" "+buy.getShipping().getYng_Quote().getYng_Branch().getLocation());
            txtBranchStreet.setText(buy.getShipping().getYng_Quote().getYng_Branch().getStreet());
        }
        switch(buy.getYng_item().getType()){
            case "Product":
                txtItemType.setText("Producto");
                break;
            case "Service":
                txtItemType.setText("Servicio");
                break;
            case "Motorized":
                txtItemType.setText("Vehículo");
                break;
            case "Property":
                txtItemType.setText("Inmueble");
                break;
        }
        if(buy.getYng_Payment().getType().equals("CASH")){
            downloadTicket.setVisibility(View.VISIBLE);
            imgPayment.setImageResource(R.drawable.cash1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " en " +buy.getYng_Payment().getCashPayment().getPaymentMethod());
            downloadTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(buy.getYng_Payment().getCashPayment().getURL_PAYMENT_RECEIPT_PDF()));
                    startActivity(viewIntent);
                }
            });
        }else {
            downloadTicket.setVisibility(View.GONE);
            imgPayment.setImageResource(R.drawable.card1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " con " +buy.getYng_Payment().getYng_Card().getProvider()+" "+buy.getYng_Payment().getYng_Card().getType()+"O terminada en "+buy.getYng_Payment().getYng_Card().getNumber());
        }



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(buy.getYng_item().getName());
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
