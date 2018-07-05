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
import android.widget.EditText;
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
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Claim;
import com.valecom.yingul.model.Yng_Confirm;
import com.valecom.yingul.model.Yng_StateShipping;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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

    private Yng_Confirm confirm;
    private TextView buyTime,txtItemType,txtCurrencyPrice,txtShippingCost,txtTypeOSchedules,txtTotal,txtItemName,txtQuantity,txtBranchName,txtBranchStreet,txtPayment,txtDischargeDate,txtDate,txtStatus,txtReason,txtBranch;
    private ImageView principalImage,imgShipping,imgPayment;
    private LinearLayout layoutShipping,layBranch,downloadTicket,layoutStatusShipment,purchaseProblemLayout;
    private Button btnFindShipping;

    private MaterialDialog setting_address_edit_dialog;

    public static final String TAG = "PurchaseDetailFragment";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
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
        confirm = gson.fromJson(bundle.getString("confirm"), Yng_Confirm.class);

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
        btnFindShipping = (Button) view.findViewById(R.id.btnFindShipping);
        layoutStatusShipment = (LinearLayout) view.findViewById(R.id.layoutStatusShipment);
        purchaseProblemLayout = (LinearLayout) view.findViewById(R.id.purchaseProblemLayout);

        txtQuantity.setText(String.valueOf(confirm.getBuy().getQuantity()));
        txtItemName.setText(confirm.getBuy().getYng_item().getName());
        buyTime.setText(confirm.getBuy().getTime());
        txtCurrencyPrice.setText("$ "+confirm.getBuy().getItemCost());
        txtTotal.setText("$ "+confirm.getBuy().getCost());
        Picasso.with(getActivity()).load(Network.BUCKET_URL+confirm.getBuy().getYng_item().getPrincipalImage()).into(principalImage);

        if(confirm.getStatus().equals("confirm")){
            purchaseProblemLayout.setVisibility(View.VISIBLE);
        }else{
            purchaseProblemLayout.setVisibility(View.GONE);
        }

        if(confirm.getBuy().getShipping()==null){
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
            if(confirm.getBuy().getYng_item().getProductPagoEnvio().equals("gratis")){
                txtShippingCost.setText("GRATIS");
            }else{
                txtShippingCost.setText("$ "+confirm.getBuy().getShippingCost());
            }
            txtTypeOSchedules.setText(confirm.getBuy().getShipping().getYng_Quote().getYng_Branch().getSchedules());
            txtBranchName.setText("Sucursal "+confirm.getBuy().getShipping().getYng_Quote().getYng_Branch().getNameMail()+" "+confirm.getBuy().getShipping().getYng_Quote().getYng_Branch().getLocation());
            txtBranchStreet.setText(confirm.getBuy().getShipping().getYng_Quote().getYng_Branch().getStreet());
        }
        switch(confirm.getBuy().getYng_item().getType()){
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
        if(confirm.getBuy().getYng_Payment().getType().equals("CASH")){
            downloadTicket.setVisibility(View.VISIBLE);
            imgPayment.setImageResource(R.drawable.cash1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " en " +confirm.getBuy().getYng_Payment().getCashPayment().getPaymentMethod());
            downloadTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(confirm.getBuy().getYng_Payment().getCashPayment().getURL_PAYMENT_RECEIPT_PDF()));
                    startActivity(viewIntent);
                }
            });
        }else {
            downloadTicket.setVisibility(View.GONE);
            imgPayment.setImageResource(R.drawable.card1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " con " +confirm.getBuy().getYng_Payment().getYng_Card().getProvider()+" "+confirm.getBuy().getYng_Payment().getYng_Card().getType()+"O terminada en "+confirm.getBuy().getYng_Payment().getYng_Card().getNumber());
        }

        btnFindShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShipping();
            }
        });
        purchaseProblemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initClaim();
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(confirm.getBuy().getYng_item().getName());
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
        Log.e("codigo que llega",confirm.getBuy().getShipping().getYng_Shipment().getShipmentCod());
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL+"buy/getStateBuy/"+confirm.getBuy().getShipping().getYng_Shipment().getShipmentCod(), api_parameter, new Response.Listener<JSONObject>()
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

    public void initClaim(){
        setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.init_claim_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();
                        TextView textCancel = (TextView) view.findViewById(R.id.textCancel);
                        TextView textCreateClaim = (TextView) view.findViewById(R.id.textCreateClaim);
                        final EditText editClaimText = (EditText) view.findViewById(R.id.editClaimText);

                        textCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });

                        textCreateClaim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Yng_Claim claim = new Yng_Claim();
                                claim.setClaimText(editClaimText.getText().toString().trim());
                                claim.setConfirm(confirm);
                                Gson gson = new Gson();
                                String jsonBody = gson.toJson(claim);
                                Log.e("buy final", jsonBody);
                                requestArrayPost(Network.API_URL + "claim/createClaim",jsonBody);
                            }
                        });
                    }
                })
                .callback(new MaterialDialog.ButtonCallback()
                {

                })
                .show();
    }

    public void  requestArrayPost(String url, String json){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }
                //

                final String responce=""+(responseBody.string());
                try {
                    end(""+responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("responce:------------",""+responce);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.equals("save")) {
                            Toast.makeText(getContext(), "Venta paralizada, reclamo exitoso revise su email, nos pondremos en contacto con usted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getContext(),"Algo salió mal intente nuevamente mas tarde",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    public void end(String end) throws JSONException {
        Log.i("end",""+end);
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }

}
