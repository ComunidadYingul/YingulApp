package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.model.Yng_Buy;
import com.valecom.yingul.model.Yng_StateShipping;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccountSaleDetailFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Yng_Buy buy;
    private TextView buyTime,txtItemType,txtCurrencyPrice,txtShippingCost,txtTypeOSchedules,txtTotal,txtItemName,txtQuantity,txtBranchName,txtBranchStreet,txtDischargeDate,txtDate,txtStatus,txtReason,txtBranch;
    private ImageView principalImage,imgShipping;
    private LinearLayout layoutShipping,layBranch,layoutStatusShipment;
    private Button btnFindShipping;

    private MaterialDialog setting_address_edit_dialog;

    public static final String TAG = "PurchaseDetailFragment";
    public MyAccountSaleDetailFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountSaleDetailFragment newInstance(String param1, String param2)
    {
        MyAccountSaleDetailFragment fragment = new MyAccountSaleDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_account_sale_detail, container, false);

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
        btnFindShipping = (Button) view.findViewById(R.id.btnFindShipping);
        layoutStatusShipment = (LinearLayout) view.findViewById(R.id.layoutStatusShipment);

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
            layoutStatusShipment.setVisibility(LinearLayout.GONE);
            txtTypeOSchedules.setText("Retiro en el domicilio del vendedor");
        }else{
            imgShipping.setImageResource(R.drawable.branch);
            layoutShipping.setVisibility(View.VISIBLE);
            layBranch.setVisibility(View.VISIBLE);
            layoutStatusShipment.setVisibility(View.VISIBLE);
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

        btnFindShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShipping();
            }
        });

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

    public void findShipping(){
        progressDialog.show();
        Log.e("codigo que llega",buy.getShipping().getYng_Shipment().getShipmentCod());
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL+"buy/getStateBuy/"+buy.getShipping().getYng_Shipment().getShipmentCod(), api_parameter, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }

                        try{
                            Log.e("estado",String.valueOf(response));
                            Yng_StateShipping statusShip = new Yng_StateShipping();
                            statusShip.setEstado(response.optString("estado"));
                            statusShip.setSucursal(response.optString("sucursal"));
                            statusShip.setFecha(response.optString("fecha"));
                            statusShip.setNombreEnvio(response.optString("nombreEnvio"));
                            statusShip.setNroAndreani(response.optString("nroAndreani"));
                            statusShip.setFechaAlta(response.optString("fechaAlta"));
                            statusShip.setMotivo(response.optString("motivo"));
                            /*******************/
                            final Yng_StateShipping finalStatusShip = statusShip;
                            setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                                    .customView(R.layout.my_account_status_shipping_layout, true)
                                    .positiveText("OK")
                                    .cancelable(false)
                                    .showListener(new DialogInterface.OnShowListener()
                                    {
                                        @Override
                                        public void onShow(DialogInterface dialog)
                                        {
                                            View view = setting_address_edit_dialog.getCustomView();

                                            txtDischargeDate = (TextView) view.findViewById(R.id.txtDischargeDate);
                                            String[] parts = finalStatusShip.getFechaAlta().split("T1");
                                            txtDischargeDate.setText(parts[0]);

                                            txtDate = (TextView) view.findViewById(R.id.txtDate);
                                            parts = finalStatusShip.getFecha().split("T1");
                                            txtDate.setText(parts[0]);

                                            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
                                            txtStatus.setText(finalStatusShip.getEstado());

                                            txtReason = (TextView) view.findViewById(R.id.txtReason);
                                            txtReason.setText(finalStatusShip.getMotivo());

                                            txtBranch = (TextView) view.findViewById(R.id.txtBranch);
                                            txtBranch.setText(finalStatusShip.getSucursal());
                                        }
                                    })
                                    .callback(new MaterialDialog.ButtonCallback()
                                    {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            if (dialog != null && dialog.isShowing()) {
                                                // If the response is JSONObject instead of expected JSONArray
                                                dialog.dismiss();
                                            }
                                        }
                                    })
                                    .show();
                            /******************/



                        }
                        catch (Exception ex){
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // TODO Auto-generated method stub
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null)
                        {
                            try
                            {
                                JSONObject json = new JSONObject(new String(response.data));
                                Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                })


        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("X-API-KEY", Network.API_KEY);
                params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }

}