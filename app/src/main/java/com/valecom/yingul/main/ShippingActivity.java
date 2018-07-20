package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.adapter.QuoteAdapter;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.buy.BuyItemSetShippingBranchFragment;
import com.valecom.yingul.main.buy.BuyItemSetWhoWithdrewPurchaseFragment;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ShippingActivity extends AppCompatActivity {

    MaterialDialog progressDialog;

    Toolbar toolbar;
    EditText editPostalCode;
    Button buttonFindBranch;
    ListView list;
    QuoteAdapter adapter;
    ArrayList<Yng_Quote> array_list;

    Yng_Quote quote;
    Yng_User user;
    Yng_Ubication ubication;

    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Envio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new MaterialDialog.Builder(getApplicationContext())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        quote = new Yng_Quote();
        user = new Yng_User();
        ubication = new Yng_Ubication();

        editPostalCode = (EditText) findViewById(R.id.editPostalCode);
        buttonFindBranch = (Button) findViewById(R.id.buttonFindBranch);

        array_list = new ArrayList<Yng_Quote>();
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_Quote item = adapter.getItem(position);
                //((BuyActivity)getActivity()).quote=item;
                /*BuyItemSetWhoWithdrewPurchaseFragment fragment = new BuyItemSetWhoWithdrewPurchaseFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });




        buttonFindBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

                /*if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }else{*/
                    //user.setUsername(settings.getString("username",""));

                    //user.setPhone(settings.getString("phone",""));
                    //user.setDocumentType(settings.getString("documentType",""));
                    //user.setDocumentNumber(settings.getString("documentNumber",""));
                    //user.setPassword(settings.getString("password",""));

                    Validacion val = new Validacion();
                    if(val.valCantString(editPostalCode,4)){
                        ubication.setPostalCode(editPostalCode.getText().toString().trim());
                        user.setYng_Ubication(ubication);

                        quote.setYng_Item((Yng_Item) getIntent().getSerializableExtra("item"));
                        quote.setQuantity(getIntent().getIntExtra("itemQuantity",0));
                        quote.setYng_User(user);
                        Gson gson = new Gson();
                        String jsonBody = gson.toJson(quote);
                        Log.e("quote final", jsonBody);
                        //
                        getAsyncCall(Network.API_URL + "logistics/quote", jsonBody);
                    }

                //}
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    JSONArray jsonArray = new JSONArray(responce);
                    for(int i=0;i<jsonArray.length();i++){
                        Gson gson = new Gson();
                        Yng_Quote quote = gson.fromJson(jsonArray.getJSONObject(i).toString(), Yng_Quote.class);
                        array_list.add(quote);
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            adapter = new QuoteAdapter(getApplicationContext(), array_list);
                            list.setAdapter(adapter);

                        }
                    });
                    end("fin");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void end(String end) throws JSONException {
        Log.i("end",""+end);
        //((BuyActivity)getActivity()).quotes = new JSONArray(end);
        //progressDialog.dismiss();
        /*BuyItemSetShippingBranchFragment fragment = new BuyItemSetShippingBranchFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }
    public void start(String start){
        Log.i("start",""+start);
        //progressDialog.show();
    }
}
