package com.valecom.yingul.main.myAccount.confirmDelivery;

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
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Confirm;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
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

public class ConfirmDeliveryActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private MaterialDialog progressDialog;
    private Yng_Confirm confirm;
    private Long confirmId;
    private JSONObject api_parameter;
    private Yng_User user;
    private Button buttonConfirmDelivery;
    private EditText editCodeConfirm;
    private int cont;

    private String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        user = new Yng_User();
        cont=3;


        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

        //Means user is not logged in
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){

            }else{
                Gson gson = new Gson();
                Yng_Ubication userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                user.setYng_Ubication(userUbication);
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
        }

        confirm = new Yng_Confirm();
        confirmId = (Long) getIntent().getSerializableExtra("confirmId");
        editCodeConfirm = (EditText) findViewById(R.id.editCodeConfirm);
        buttonConfirmDelivery = (Button) findViewById(R.id.buttonConfirmDelivery);
        buttonConfirmDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cont>0){
                    confirm.setCodeConfirm(Integer.parseInt(editCodeConfirm.getText().toString().trim()));
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(confirm);
                    Log.e("confirm final", jsonBody);
                    requestArrayPost(Network.API_URL + "confirm/updateConfirm",jsonBody);
                }else{
                    Intent intent = new Intent(ConfirmDeliveryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        getConfirm();



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
    public void getConfirm() {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "confirm/getConfirmForId/"+confirmId, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Confirm por su id" , response.toString());
                            Gson gson = new Gson();
                            confirm = gson.fromJson(String.valueOf(response), Yng_Confirm.class);
                            toolbar.setTitle(confirm.getBuy().getYng_item().getName());
                            if(confirm.getStatus().equals("pending")){
                                if(user.getUsername().equals(confirm.getSeller().getUsername())){

                                }else{
                                    Toast.makeText(ConfirmDeliveryActivity.this, "prohibido", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ConfirmDeliveryActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(ConfirmDeliveryActivity.this, "Ya fue confirmado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmDeliveryActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(ConfirmDeliveryActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ConfirmDeliveryActivity.this, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(ConfirmDeliveryActivity.this, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(ConfirmDeliveryActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("X-API-KEY", Network.API_KEY);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.equals("save")) {
                            Toast.makeText(ConfirmDeliveryActivity.this, "Confirmación exitosa revise su email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmDeliveryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            cont--;
                            Toast.makeText(ConfirmDeliveryActivity.this,"Verifica el código nuevamente te quedan "+cont+" opotunidades.",Toast.LENGTH_LONG).show();
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
