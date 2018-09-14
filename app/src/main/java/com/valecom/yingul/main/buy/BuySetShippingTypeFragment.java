package com.valecom.yingul.main.buy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.model.Yng_Shipment;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;
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
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetShippingTypeFragment extends Fragment {

    MaterialDialog progressDialog;

    private TextView txtWithdrawProduct;
    private Yng_User user;
    private Yng_Ubication userUbication,ubication;
    private Button btnUbicationLocal,btnOtherUbication;
    private Yng_Quote quote;
    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    public BuySetShippingTypeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_buy_set_shipping_type, container, false);

        btnUbicationLocal = (Button) v.findViewById(R.id.btnUbicationLocal);
        btnOtherUbication = (Button) v.findViewById(R.id.btnOtherUbication);
        txtWithdrawProduct = (TextView) v.findViewById(R.id.txtWithdrawProduct);
        user = new Yng_User();
        ubication = new Yng_Ubication();
        quote = new Yng_Quote();

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){
                userUbication=null;
            }else{
                Gson gson = new Gson();
                userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
        }
        if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
            Intent intent = new Intent(getContext(), BuyActivity.class);
            intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
            intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
            startActivity(intent);
            getActivity().finish();
        }else{
            btnUbicationLocal.setText("Enviar a domicilio actual" + '\n' +"("+userUbication.getYng_Province().getName()+" "+userUbication.getYng_City().getName()+" "+userUbication.getStreet()+" "+userUbication.getNumber()+")");
        }

        btnUbicationLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
                if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
                {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    user.setUsername(settings.getString("username",""));
                    /*para obtener la ubicacion del usuario*/
                    if(settings.getString("yng_Ubication","").equals("null")){
                        userUbication=null;
                    }else{
                        Gson gson = new Gson();
                        userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                    }
                    /*fin de la ubicacion del usuario*/
                    user.setPhone(settings.getString("phone",""));
                    user.setDocumentType(settings.getString("documentType",""));
                    user.setDocumentNumber(settings.getString("documentNumber",""));
                    user.setPassword(settings.getString("password",""));
                }
                if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                    Intent intent = new Intent(getContext(), BuyActivity.class);
                    intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
                    intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    ((BuyActivity) getActivity()).shipping.setTypeShipping("branchHome");
                    ubication.setPostalCode(userUbication.getPostalCode());
                    user = ((BuyActivity) getActivity()).user;
                    user.setYng_Ubication(ubication);
                    quote.setYng_Item(((BuyActivity) getActivity()).item);
                    quote.setQuantity(((BuyActivity) getActivity()).quantity);
                    quote.setYng_User(user);
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(quote);
                    Log.e("quote final", jsonBody);
                    //
                    getAsyncCall(Network.API_URL + "logistics/quoteBranchHome", jsonBody);
                }
            }
        });

        btnOtherUbication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
                if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
                {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    user.setUsername(settings.getString("username",""));
                    /*para obtener la ubicacion del usuario*/
                    if(settings.getString("yng_Ubication","").equals("null")){
                        userUbication=null;
                    }else{
                        Gson gson = new Gson();
                        userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                    }
                    /*fin de la ubicacion del usuario*/
                    user.setPhone(settings.getString("phone",""));
                    user.setDocumentType(settings.getString("documentType",""));
                    user.setDocumentNumber(settings.getString("documentNumber",""));
                    user.setPassword(settings.getString("password",""));
                }
                if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                    Intent intent = new Intent(getContext(), BuyActivity.class);
                    intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
                    intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    ((BuyActivity) getActivity()).shipping.setTypeShipping("branchHome");
                    BuyItemFindShippingBranchFragment fragment = new BuyItemFindShippingBranchFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        txtWithdrawProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
                if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
                {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    user.setUsername(settings.getString("username",""));
                    /*para obtener la ubicacion del usuario*/
                    if(settings.getString("yng_Ubication","").equals("null")){
                        userUbication=null;
                    }else{
                        Gson gson = new Gson();
                        userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                    }
                    /*fin de la ubicacion del usuario*/
                    user.setPhone(settings.getString("phone",""));
                    user.setDocumentType(settings.getString("documentType",""));
                    user.setDocumentNumber(settings.getString("documentNumber",""));
                    user.setPassword(settings.getString("password",""));
                }
                if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                    Intent intent = new Intent(getContext(), BuyActivity.class);
                    intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
                    intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    BuySetShippingWithdrawTypeFragment fragment = new BuySetShippingWithdrawTypeFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
        u.setPostalCode(ubication.getPostalCode());
        u.setStreet(userUbication.getStreet());
        u.setNumber(userUbication.getNumber());
        u.setWithinStreets(userUbication.getWithinStreets());
        u.setDepartment(userUbication.getDepartment());
        u.setAditional(userUbication.getAditional());

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
