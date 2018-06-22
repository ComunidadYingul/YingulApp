package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QueryListAdapter;
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

public class QueriesItemActivity extends AppCompatActivity {
    public static final String TAG = "ItemPickerActivity";

    Toolbar toolbar;
    Yng_User user;
    private Yng_Ubication userUbication;
    MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private String queries1;
    private ListView list;
    private QueryListAdapter adapter;
    private ArrayList<Yng_Query> array_list;
    private Button buttonNewQuestion;
    private EditText editNewQuestion;
    private Yng_Item item;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Preguntas al vendedor");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = new Yng_User();
        item = new Yng_Item();

        queries1 = (String)getIntent().getSerializableExtra("queries1");
        item = (Yng_Item) getIntent().getSerializableExtra("item");
        array_list = new ArrayList<Yng_Query>();
        adapter = new QueryListAdapter(this, array_list);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        buttonNewQuestion = (Button) findViewById(R.id.buttonNewQuestion);
        editNewQuestion = (EditText) findViewById(R.id.editNewQuestion);

        buttonNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunCreateNewQuery();
            }
        });

        try {
            listQueries();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void listQueries() throws JSONException {
        JSONArray queries = new JSONArray(queries1);
        array_list.clear();
        for (int i = 0; i < queries.length(); i++) {
            JSONObject obj = queries.getJSONObject(i);
            Yng_Query query = new Yng_Query();
            query.setQueryId(obj.optLong("queryId"));
            query.setQuery(obj.optString("query"));
            query.setAnswer(obj.getString("answer"));
            query.setDate(obj.getString("date"));
            query.setStatus(obj.optString("status"));
            Yng_Item item = new Yng_Item();
            Gson gson = new Gson();
            item = gson.fromJson(String.valueOf(obj.optJSONObject("yng_Item")), Yng_Item.class);
            Yng_User buyer = new Yng_User();
            buyer = gson.fromJson(String.valueOf(obj.optJSONObject("user")), Yng_User.class);
            query.setYng_Item(item);
            query.setUser(buyer);
            array_list.add(query);
        }

        adapter.notifyDataSetChanged();
    }
    public void RunCreateNewQuery(){
        SharedPreferences settings = this.getSharedPreferences(LoginActivity.SESSION_USER, this.MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(this, LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
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
            user.setYng_Ubication(userUbication);

            Yng_Query newQuery = new Yng_Query();
            newQuery.setQuery(editNewQuestion.getText().toString().trim());
            newQuery.setYng_Item(item);
            newQuery.setUser(user);
            Gson gson = new Gson();
            String jsonBody = gson.toJson(newQuery);
            Log.e("query final", jsonBody);
            requestArrayPost1(Network.API_URL + "query/create",jsonBody);
        }
    }
    public void requestArrayPost1(String url, String json){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.equals("save")) {
                            Toast.makeText(QueriesItemActivity.this, "consulta realizada exitosamente", Toast.LENGTH_SHORT).show();
                            RunGetQueriesByItem();
                        }else{
                            Toast.makeText(QueriesItemActivity.this,responce.toString(),Toast.LENGTH_LONG).show();
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
    public void RunGetQueriesByItem()
    {
        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/Query/"+item.getItemId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            Log.e("preguntas por usuario",response.toString());
                            queries1=response.toString();
                            JSONArray queries = response;
                            array_list.clear();
                            for (int i = 0; i < queries.length(); i++) {
                                JSONObject obj = queries.getJSONObject(i);
                                Yng_Query query = new Yng_Query();
                                query.setQueryId(obj.optLong("queryId"));
                                query.setQuery(obj.optString("query"));
                                query.setAnswer(obj.getString("answer"));
                                query.setDate(obj.getString("date"));
                                query.setStatus(obj.optString("status"));
                                Yng_Item item = new Yng_Item();
                                Gson gson = new Gson();
                                item = gson.fromJson(String.valueOf(obj.optJSONObject("yng_Item")), Yng_Item.class);
                                Yng_User buyer = new Yng_User();
                                buyer = gson.fromJson(String.valueOf(obj.optJSONObject("user")), Yng_User.class);
                                query.setYng_Item(item);
                                query.setUser(buyer);
                                array_list.add(query);
                            }

                            adapter.notifyDataSetChanged();
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(QueriesItemActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            //}
                        }

                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
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
                        Toast.makeText(QueriesItemActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(QueriesItemActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(QueriesItemActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                SharedPreferences settings = QueriesItemActivity.this.getSharedPreferences(LoginActivity.SESSION_USER, QueriesItemActivity.this.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                //params.put("X-API-KEY", Network.API_KEY);
                /*params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (settings.getString("email","")+":" + settings.getString("api_key","")).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        //RequestQueue queue = MySingleton.getInstance( getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(QueriesItemActivity.this).addToRequestQueue(postRequest);
    }

}