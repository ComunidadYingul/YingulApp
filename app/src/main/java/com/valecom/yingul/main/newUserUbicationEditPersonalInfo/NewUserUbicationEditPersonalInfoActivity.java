package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;

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

public class NewUserUbicationEditPersonalInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    Yng_Ubication ubication;
    Yng_Country country;
    Yng_Province province;
    Yng_City city;
    Yng_User user;
    MaterialDialog progressDialog;
    private JSONObject api_parameter;
    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_ubication_edit_personal_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ubication = new Yng_Ubication();
        country = new Yng_Country();
        province = new Yng_Province();
        city = new Yng_City();
        user = new Yng_User();
        user = (Yng_User) getIntent().getSerializableExtra("data");
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
        }

        NewUserUbicationEditPersonalInfoFragment fragment = new NewUserUbicationEditPersonalInfoFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();

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

    public void returnNewUbication(){
        ubication.setYng_Country(country);
        ubication.setYng_Province(province);
        ubication.setYng_City(city);
        user.setYng_Ubication(ubication);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(user);
        Log.e("usuario que se envia",jsonBody);
        requestArrayPost(Network.API_URL + "user/setUserUbicationEditPersonalInfo",jsonBody);
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
                .addHeader("Authorization",user.getPassword())
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
                        if(responce.contains("username")) {
                            Log.e("El nuevo usuario", String.valueOf(responce));
                            Gson gson = new Gson();
                            user = gson.fromJson(String.valueOf(responce), Yng_User.class);

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("data", user);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }else{
                            Toast.makeText(NewUserUbicationEditPersonalInfoActivity.this,"Algo salio mal intente nuevamente mas tarde",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
    public void end(String end) throws JSONException{
        Log.i("end",""+end);
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }


}