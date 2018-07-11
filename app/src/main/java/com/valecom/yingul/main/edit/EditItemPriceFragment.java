package com.valecom.yingul.main.edit;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Spinner;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.sell.SellItemSetDeliveryFragment;
import com.valecom.yingul.network.Network;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditItemPriceFragment extends Fragment {

    //EditText editPrice;
    Button buttonSetPrice,btnOfferDiscount;
    Spinner spinner_currency;
    MaterialDialog progressDialog;
    private MaterialDialog setting_address_edit_dialog;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = "EditFragment";
    String money="0",price="0",priceDiscount="0",priceNormal="0";
    Validacion val = new Validacion();

    private EditText editPrice,editPriceNormal,editPriceDiscount;
    private TextView editPorcentDiscount;

    public EditItemPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_price, container, false);

        Bundle bundle = getArguments();
        final long itemId = bundle.getLong("itemId");

        progressDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        editPrice = (EditText) v.findViewById(R.id.editPrice);
        spinner_currency = (Spinner) v.findViewById(R.id.spinner_currency);
        buttonSetPrice = (Button) v.findViewById(R.id.buttonSetPrice);
        btnOfferDiscount = (Button) v.findViewById(R.id.btnOfferDiscount);

        editPrice.setText(bundle.getString("data"));

        String currency[] = {"U$S","$"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, currency);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_currency.setAdapter(spinnerArrayAdapter);
        if(bundle.getString("itemType")=="Product"){
            spinner_currency.setSelection(1);
        }

        buttonSetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val.validarNumero(editPrice,editPrice.getText().toString())){
                    editPrice.setError("Ingrese precio");
                }else {

                    if (spinner_currency.getSelectedItemPosition() == 0) {
                        money = "USD";
                    } else {
                        money = "ARS";
                    }

                    priceNormal = editPrice.getText().toString();

                    if(priceDiscount.equals("0") ) {
                        price = priceNormal;
                    }else{
                        price = priceDiscount;
                    }

                    String edit = "{\"itemId\":\""+itemId+"\",\"price\":\""+price+"\",\"money\":\""+money+"\",\"priceDiscount\":\""+priceDiscount+"\",\"priceNormal\":\""+priceNormal+"\"}";

                    String url = Network.API_URL + "item/updatePrice";
                    Log.e("parametro", edit);
                    Log.e("url", url);

                    requestArrayPost(url, edit);
                }

            }
        });

        btnOfferDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(val.valNumber(editPrice)) {
                    setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.offer_discount_layout, true)
                            .positiveText("OK")
                            .cancelable(false)
                            .negativeText("Cancelar")
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    View view = setting_address_edit_dialog.getCustomView();
                                    editPriceNormal = (EditText) view.findViewById(R.id.editPriceNormal);
                                    editPriceNormal.setText(editPrice.getText().toString());
                                    editPriceDiscount = (EditText) view.findViewById(R.id.editPriceDiscount);
                                    editPorcentDiscount = (TextView) view.findViewById(R.id.editPorcentDiscount);

                                    setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                                    editPriceDiscount.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            Log.e("Gonzalo:----","beforeTextChanged");
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            double normal = Double.valueOf(editPriceNormal.getText().toString());
                                            double discount = 0;
                                            if(editPriceDiscount.getText().toString().trim().length() > 0){
                                                discount = Integer.valueOf(editPriceDiscount.getText().toString());
                                            }

                                            if(discount > normal){
                                                editPorcentDiscount.setText("El descuento no debe ser mayor al precio normal");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                            }else if(discount == normal || editPriceDiscount.getText().toString().trim().length() <= 0){
                                                editPorcentDiscount.setText("Ingrese el precio con descuento");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                            }
                                            else {
                                                double porcent = ((normal - discount) * 100) / normal;
                                                editPorcentDiscount.setText("Estas ofreciendo un descuento del "+porcent+"%");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Log.e("Gonzalo:----","afterTextChanged");
                                        }
                                    });

                                }
                            })
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    priceDiscount = editPriceDiscount.getText().toString().trim();
                                    priceNormal = editPriceNormal.getText().toString().trim();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    if (dialog != null && dialog.isShowing()) {
                                        // If the response is JSONObject instead of expected JSONArray
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .show();
                }
            }
        });

        return v;
    }

    public void  requestArrayPost(String url, String json){
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
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
                .addHeader("Authorization",settings.getString("password",""))
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
                            Toast.makeText(getActivity(), "Datos modificados exitosamente", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(BuyActivity.this, MainActivity.class);
                            //startActivity(intent);
                            getActivity().onBackPressed();
                        }else if(responce.equals("prohibited")){
                            //if(responce.contains(":")){
                            //String[] parts = responce.split(":");
                            //paymentId = Long.parseLong(parts[1]);
                            //Log.e("paymentId",""+paymentId);
                            //RunGetPaymentToTicketService();
                            //}else{
                            //Toast.makeText(BuyActivity.this,"No se guardo 1",Toast.LENGTH_LONG).show();
                            //}
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
