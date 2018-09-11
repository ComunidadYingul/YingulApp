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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.valecom.yingul.adapter.ShippingStateAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.myAccount.confirmDelivery.ConfirmDeliveryActivity;
import com.valecom.yingul.model.Yng_Confirm;
import com.valecom.yingul.model.Yng_ShippingState;
import com.valecom.yingul.model.Yng_ShippingTraceability;
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
import okhttp3.ResponseBody;

public class MyAccountSaleDetailFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Yng_Confirm confirm;
    private TextView buyTime,txtItemType,txtCurrencyPrice,txtShippingCost,txtTypeOSchedules,txtTotal,txtItemName,txtQuantity,txtBranchName,txtBranchStreet,txtDischargeDate,txtDate,txtStatus,txtReason,txtBranch;
    private ImageView principalImage,imgShipping;
    private LinearLayout layoutShipping,layBranch,layoutStatusShipment,layoutConfirmDelivery,layoutTicket;
    private Button btnFindShipping,btnConfirmDelivery,btnTicket;

    ListView listShipState;
    ShippingStateAdapter adapter;
    ArrayList<Yng_ShippingState> array_list;

    private MaterialDialog setting_address_edit_dialog;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

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

        array_list = new ArrayList<Yng_ShippingState>();
        adapter = new ShippingStateAdapter(getContext(), array_list);

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
        btnFindShipping = (Button) view.findViewById(R.id.btnFindShipping);
        layoutStatusShipment = (LinearLayout) view.findViewById(R.id.layoutStatusShipment);
        layoutTicket = (LinearLayout) view.findViewById(R.id.layoutTicket);
        layoutConfirmDelivery = (LinearLayout) view.findViewById(R.id.layoutConfirmDelivery);
        btnConfirmDelivery = (Button) view.findViewById(R.id.btnConfirmDelivery);
        btnTicket = (Button) view.findViewById(R.id.btnTicket);

        txtQuantity.setText(String.valueOf(confirm.getBuy().getQuantity()));
        txtItemName.setText(confirm.getBuy().getYng_item().getName());
        buyTime.setText(confirm.getBuy().getTime());
        txtCurrencyPrice.setText("$ "+confirm.getBuy().getItemCost());
        txtTotal.setText("$ "+confirm.getBuy().getCost());
        layoutConfirmDelivery.setVisibility(View.GONE);
        Picasso.with(getActivity()).load(Network.BUCKET_URL+confirm.getBuy().getYng_item().getPrincipalImage()).into(principalImage);

        if(confirm.getBuy().getShipping()==null){
            imgShipping.setImageResource(R.drawable.home);
            layoutShipping.setVisibility(View.GONE);
            layBranch.setVisibility(LinearLayout.GONE);
            layoutStatusShipment.setVisibility(LinearLayout.GONE);
            layoutTicket.setVisibility(LinearLayout.GONE);
            txtTypeOSchedules.setText("Retiro en el domicilio del vendedor");
            if(confirm.getStatus().equals("pending")){
                layoutConfirmDelivery.setVisibility(View.VISIBLE);
            }else{
                layoutConfirmDelivery.setVisibility(View.GONE);
            }
        }else{
            imgShipping.setImageResource(R.drawable.branch);
            layoutShipping.setVisibility(View.VISIBLE);
            layBranch.setVisibility(View.VISIBLE);
            layoutStatusShipment.setVisibility(View.VISIBLE);
            layoutTicket.setVisibility(View.VISIBLE);
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

        btnFindShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findShipping();
            }
        });

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Network.API_URL+"buy/getTicket/"+confirm.getBuy().getShipping().getYng_Shipment().getShipmentCod();
                Log.e("Url:--",url);
                requestArrayPost(url);
            }
        });

        btnConfirmDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConfirmDeliveryActivity.class);
                intent.putExtra("confirmId", confirm.getConfirmId());
                startActivity(intent);
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
                (Request.Method.GET, Network.API_URL+"buy/getTrazabBuy/"+confirm.getBuy().getShipping().getYng_Shipment().getShipmentCod(), api_parameter, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }

                        try{
                            JSONObject obj1 = response;
                            Log.e("estado",String.valueOf(response));
                            Yng_ShippingTraceability shipTra = new Yng_ShippingTraceability();
                            //como esto statusShip.setEstado(response.optString("estado"));
                            shipTra.setFechaAlta(obj1.optString("fechaAlta"));
                            shipTra.setEventos(obj1.optJSONObject("eventos"));
                            shipTra.setNombreEnvio(obj1.optString("nombreEnvio"));
                            shipTra.setNroAndreani(obj1.optString("nroAndreani"));

                            JSONArray items = shipTra.getEventos().optJSONArray("evento_");
                            Log.e("trazabilidad",items.toString());
                            array_list.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);
                                Yng_ShippingState item = new Yng_ShippingState();
                                item.setSucursal(obj.optString("sucursal"));
                                item.setFecha(obj.optString("fecha"));
                                item.setIdMotivo(obj.optString("idMotivo"));
                                item.setMotivo(obj.optJSONObject("motivo"));
                                item.setIdEstado(obj.optString("idEstado"));
                                item.setEstado(obj.optString("estado"));
                                array_list.add(item);
                            }

                            adapter.notifyDataSetChanged();
                            /*******************/
                            final Yng_ShippingTraceability finalShipTra = shipTra;
                            setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                                    .customView(R.layout.my_account_shipping_traceability_layout, true)
                                    .positiveText("OK")
                                    .cancelable(false)
                                    .showListener(new DialogInterface.OnShowListener()
                                    {
                                        @Override
                                        public void onShow(DialogInterface dialog)
                                        {
                                            View view = setting_address_edit_dialog.getCustomView();

                                            listShipState = (ListView) view.findViewById(R.id.listShipState);
                                            listShipState.setAdapter(adapter);

                                            listShipState.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                            {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                                {
                                                    final Yng_ShippingState item = adapter.getItem(position);
                                                    /***************/
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

                                                                    //txtDischargeDate = (TextView) view.findViewById(R.id.txtDischargeDate);
                                                                    //String[] parts = finalStatusShip.getFechaAlta().split("T1");
                                                                    //txtDischargeDate.setText(parts[0]);

                                                                    txtDate = (TextView) view.findViewById(R.id.txtDate);
                                                                    //parts = finalStatusShip.getFecha().split("T1");
                                                                    txtDate.setText(item.getFecha().replace("T"," "));

                                                                    txtStatus = (TextView) view.findViewById(R.id.txtStatus);
                                                                    txtStatus.setText(item.getEstado());

                                                                    txtReason = (TextView) view.findViewById(R.id.txtReason);
                                                                    //txtReason.setText(item.getMotivo());
                                                                    txtBranch = (TextView) view.findViewById(R.id.txtBranch);
                                                                    txtBranch.setText(item.getSucursal());
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
                                                }
                                            });
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

    public void  requestArrayPost(String url){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .writeTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build();
        //RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .get()
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
                Log.e("Url_ticket:--------",""+responce);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(responce));
                        startActivity(intent);

                        /*if(responce.equals("save")) {
                            Toast.makeText(this, item.getType().equals("Motorized") ? "Reserva exitosa revise su email" : "Compra exitosa revise su email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BuyActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            if(responce.contains(":")){
                                String[] parts = responce.split(":");
                                paymentId = Long.parseLong(parts[1]);
                                Log.e("paymentId",""+paymentId);
                                RunGetPaymentToTicketService();
                            }else{
                                Toast.makeText(getActivity(),"No se guardo 1",Toast.LENGTH_LONG).show();
                            }
                        }*/
                    }
                });

            }
        });
    }

    public void end(String end) throws JSONException {
        Log.e("end",""+end);
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.e("start",""+start);
        progressDialog.show();
    }

}
