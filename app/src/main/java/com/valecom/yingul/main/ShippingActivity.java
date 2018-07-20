package com.valecom.yingul.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.Util.RecyclerItemClickListener;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.adapter.ListQuoteAdapter;
import com.valecom.yingul.adapter.QuoteAdapter;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;

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

    RecyclerView recyclerView;
    ListQuoteAdapter quoteAdapter;

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
        toolbar.setTitle("Medios de envío");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new MaterialDialog.Builder(ShippingActivity.this)
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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Yng_Quote quote = array_list.get(position);
                String rate = String.valueOf(quote.getRate());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("rate",rate);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        }));

        buttonFindBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    array_list.clear();
                    JSONArray jsonArray = new JSONArray(responce);
                    for(int i=0;i<jsonArray.length();i++){
                        Gson gson = new Gson();
                        Yng_Quote quote = gson.fromJson(jsonArray.getJSONObject(i).toString(), Yng_Quote.class);
                        array_list.add(quote);
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            quoteAdapter = new ListQuoteAdapter(getApplicationContext(), array_list);
                            recyclerView.setAdapter(quoteAdapter);

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
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }
}
