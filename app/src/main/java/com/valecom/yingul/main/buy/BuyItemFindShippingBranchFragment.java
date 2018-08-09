package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.model.Yng_Shipment;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemFindShippingBranchFragment extends Fragment {

    MaterialDialog progressDialog;

    EditText editPostalCode;
    Button buttonFindBranch;
    private LinearLayout layoutWithoutZip;

    Yng_Quote quote;
    Yng_User user;
    Yng_Ubication ubication;

    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    public BuyItemFindShippingBranchFragment() {
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
        View v = inflater.inflate(R.layout.fragment_buy_item_find_shipping_branch, container, false);

        quote = new Yng_Quote();
        user = new Yng_User();
        ubication = new Yng_Ubication();
        layoutWithoutZip = (LinearLayout) v.findViewById(R.id.layoutWithoutZip);
        editPostalCode = (EditText) v.findViewById(R.id.editPostalCode);
        buttonFindBranch = (Button) v.findViewById(R.id.buttonFindBranch);
        //Por el momento esconder no se mi código postal
        layoutWithoutZip.setVisibility(View.GONE);
        buttonFindBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if(val.valCantString(editPostalCode,4)){
                    ubication.setPostalCode(editPostalCode.getText().toString().trim());
                    user = ((BuyActivity) getActivity()).user;
                    user.setYng_Ubication(ubication);
                    quote.setYng_Item(((BuyActivity) getActivity()).item);
                    quote.setQuantity(((BuyActivity) getActivity()).quantity);
                    quote.setYng_User(user);
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(quote);
                    Log.e("quote final", jsonBody);
                    //
                    getAsyncCall(Network.API_URL + "logistics/quote", jsonBody);
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void  getAsyncCall(String url, String json){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .writeTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
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

                String responce=""+(responseBody.string());
                Log.i("responce",""+responce);
                try {
                    end(""+responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void end(String end) throws JSONException {
        Log.i("end",""+end);
        ((BuyActivity)getActivity()).quotes = new JSONArray(end);
        Yng_Ubication u = new Yng_Ubication();
        u.setPostalCode(editPostalCode.getText().toString().trim());
        Yng_User us = new Yng_User();
        us.setYng_Ubication(u);
        Yng_Shipment sh = new Yng_Shipment();
        sh.setYng_User(us);
        ((BuyActivity)getActivity()).shipment = sh;
        progressDialog.dismiss();
        BuyItemSetShippingBranchFragment fragment = new BuyItemSetShippingBranchFragment();
        FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }

}