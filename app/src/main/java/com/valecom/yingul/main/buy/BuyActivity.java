package com.valecom.yingul.main.buy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.sell.SellItemSetTitleFragment;
import com.valecom.yingul.model.Yng_Buy;
import com.valecom.yingul.model.Yng_Card;
import com.valecom.yingul.model.Yng_CashPayment;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_IpApi;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_ItemImage;
import com.valecom.yingul.model.Yng_Payment;
import com.valecom.yingul.model.Yng_Product;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Quote;
import com.valecom.yingul.model.Yng_Shipment;
import com.valecom.yingul.model.Yng_Shipping;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class BuyActivity extends AppCompatActivity {
    Toolbar toolbar;
    Yng_Item item;
    Yng_Product product;
    Yng_Ubication ubication, userUbication;
    Yng_Country country;
    Yng_Province province;
    Yng_City city;
    MaterialDialog progressDialog;
    Yng_User user;
    Long itemId;
    /***************/
    int quantity;
    Yng_Buy buy;
    Yng_Payment payment;
    Yng_Card card;
    Yng_IpApi ipApi;
    Yng_CashPayment cashPayment;
    Yng_Shipping shipping;
    Yng_Quote quote;
    Yng_Shipment shipment;
    /***********/
    /****************/
    JSONArray quotes;
    Long paymentId;
    Yng_Payment paymentToTicket;
    Yng_CashPayment cashPaymentToTicket;
    /***************/
    public static final String TAG = "bUYActivity";
    static final int ITEM_PICKER_TAG = 1;
    private JSONObject api_parameter;

    String TAG1="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Comprar");

        //setear el valor del item que llega cuando presionas comprar
        //itemId = Long.valueOf(1792);
        //quantity = 1;
        Bundle bundle = this.getIntent().getExtras();
        itemId = getIntent().getLongExtra("itemId",-1);
        quantity = getIntent().getIntExtra("itemQuantity", 1);
        Log.e("id y cantidad:------", itemId +"  "+ quantity);



        item = new Yng_Item();
        product = new Yng_Product();
        ubication = new Yng_Ubication();
        country = new Yng_Country();
        province = new Yng_Province();
        city = new Yng_City();
        user = new Yng_User();
        shipment = new Yng_Shipment();
        /*********************/
        buy = new Yng_Buy();
        payment = new Yng_Payment();
        shipping = new Yng_Shipping();
        card = new Yng_Card();
        ipApi = new Yng_IpApi();
        cashPayment = new Yng_CashPayment();
        quote = new Yng_Quote();
        paymentToTicket = new Yng_Payment();
        cashPaymentToTicket = new Yng_CashPayment();
        /*********************/

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*usuario logeado*/
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

        /**/

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        RunGetItemService();
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

    public void RunGetItemService()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, Network.API_URL + "item/ItemById/"+itemId, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("item desde backend ===>" , response.toString());
                            Gson gson = new Gson();
                            item = gson.fromJson(String.valueOf(response), Yng_Item.class);
                            switch (item.getType()){
                                case "Product":
                                    BuySetShippingTypeFragment fragment = new BuySetShippingTypeFragment();
                                    FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.commit();
                                    break;
                                case "Motorized":
                                    BuyItemInfoReserveFragment fragment1 = new BuyItemInfoReserveFragment();
                                    FragmentTransaction fragmentTransaction1  = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction1.replace(R.id.content_frame, fragment1);
                                    fragmentTransaction1.commit();
                                    break;
                            }
                            Log.e("esta llegando ===>","vhjk:"+user.getDocumentNumber());
                            if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                                Intent intent = new Intent(BuyActivity.this, NewUserUbicationEditPersonalInfoActivity.class);
                                intent.putExtra("data", user);
                                startActivityForResult(intent, ITEM_PICKER_TAG);
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(BuyActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BuyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(BuyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(BuyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

    public void RunBuyService(){
        buy.setUser(user);
        buy.setYng_item(item);
        buy.setYng_Payment(payment);
        buy.setQuantity(quantity);
        shipping.setYng_Quote(quote);
        shipping.setYng_Shipment(shipment);
        buy.setShipping(shipping);
        buy.setItemCost((double)Math.round((item.getPrice()*quantity) * 100d) / 100d);

        buy.setShippingCost((double)Math.round(quote.getRate() * 100d) / 100d);
        if(item.getType().equals("Motorized")){
            buy.setCost(1500);
        }else {
            if (!item.getProductPagoEnvio().equals("gratis")) {
                buy.setCost((double)Math.round((quote.getRate() + buy.getItemCost()) * 100d) / 100d);
            } else {
                buy.setCost((double)Math.round(buy.getItemCost() * 100d) / 100d);
            }
        }

        buy.getYng_Payment().setValue(buy.getCost());
        buy.setCookie(user.getPassword());
        buy.setUserAgent(System.getProperty("http.agent"));
        Gson gson = new Gson();
        String jsonBody = gson.toJson(buy);
        Log.e("buy final", jsonBody);
        requestArrayPost(Network.API_URL + "buy/createBuy",jsonBody);
    }

    public void getLocalIpAddress() {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, "http://ip-api.com/json", api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try
                        {
                            Log.e("json de ip" , response.toString());
                            Gson gson = new Gson();
                            ipApi = gson.fromJson(String.valueOf(response), Yng_IpApi.class);
                            buy.setIp(ipApi.getQuery());
                            buy.setOrg(ipApi.getOrg());
                            buy.setLat(String.valueOf(ipApi.getLat()));
                            buy.setLon(String.valueOf(ipApi.getLon()));
                            buy.setCity(ipApi.getCity());
                            buy.setCountry(ipApi.getCountry());
                            buy.setCountryCode(ipApi.getCountryCode());
                            buy.setRegionName(ipApi.getRegionName());
                            buy.setZip(ipApi.getZip());
                            RunBuyService();

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(BuyActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BuyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(BuyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(BuyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(BuyActivity.this, item.getType().equals("Motorized") ? "Reserva exitosa revise su email" : "Compra exitosa revise su email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BuyActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            if(responce.contains(":")){
                                String[] parts = responce.split(":");
                                paymentId = Long.parseLong(parts[1]);
                                Log.e("paymentId",""+paymentId);
                                RunGetPaymentToTicketService();
                            }else{
                                Toast.makeText(BuyActivity.this, responce.toString(),Toast.LENGTH_LONG).show();
                            }
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

    public void RunGetPaymentToTicketService()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, Network.API_URL + "payment/getPaymentById/"+paymentId, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("payment desde backend: " , response.toString());

                            cashPaymentToTicket.setPaymentMethod(response.getJSONObject("cashPayment").getString("paymentMethod"));
                            cashPaymentToTicket.setURL_PAYMENT_RECEIPT_HTML(response.getJSONObject("cashPayment").getString("url_PAYMENT_RECEIPT_HTML"));
                            cashPaymentToTicket.setURL_PAYMENT_RECEIPT_PDF(response.getJSONObject("cashPayment").getString("url_PAYMENT_RECEIPT_PDF"));
                            paymentToTicket.setCashPayment(cashPaymentToTicket);
                            paymentToTicket.setValue(response.getDouble("value"));

                            BuyItemTicketCashPaymentFragment fragment = new BuyItemTicketCashPaymentFragment();
                            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.commit();
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(BuyActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(BuyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(BuyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(BuyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Network.API_KEY);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case ITEM_PICKER_TAG:
                if (resultCode == RESULT_OK) {
                    Yng_User newUser = (Yng_User)data.getSerializableExtra("data");
                    user = newUser;
                    userUbication = newUser.getYng_Ubication();
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(newUser.getYng_Ubication());
                    Log.e("ubica:---",jsonBody);
                    SharedPreferences.Editor user = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();
                    user.putString("yng_Ubication",jsonBody);
                    user.putString("phone",newUser.getPhone());
                    user.putString("documentType",newUser.getDocumentType());
                    user.putString("documentNumber",newUser.getDocumentNumber());
                    user.commit();
                }
                break;
        }
    }
}
