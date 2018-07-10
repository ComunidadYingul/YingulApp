package com.valecom.yingul.main.edit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_Query;
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
public class EditItemTitleFragment extends Fragment {

    EditText editTitle;
    Button buttonSetTitle;
    MaterialDialog progressDialog;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = "EditFragment";

    public EditItemTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_title, container, false);

        Bundle bundle = getArguments();
        final long itemId = bundle.getLong("itemId");

        progressDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        editTitle = (EditText) v.findViewById(R.id.editTitle);
        buttonSetTitle = (Button) v.findViewById(R.id.buttonSetTitle);

        editTitle.setText(bundle.getString("data"));

        buttonSetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();

                String edit = "{\"itemId\":"+itemId+",\"name\":\""+title+"\"}";
                String url = Network.API_URL+"item/updateName";
                Log.e("parametro",edit);
                Log.e("url",url);

                requestArrayPost(url,edit);
            }
        });

        return  v;
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
