package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SESSION_USER = "User";

    private EditText editEmail;
    private EditText editPassword;
    private TextView textLogo, textTitle;

    public static final String TAG = "LoginActivity";
    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        TextView txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        Typeface typeface1 = Typeface.createFromAsset(LoginActivity.this.getAssets(), "fonts/font-yingul.ttf");

        textLogo = (TextView)findViewById(R.id.text_logo);
        textTitle = (TextView)findViewById(R.id.text_title);
        textLogo.setTypeface(typeface1);
        textTitle.setTypeface(typeface1);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txtSignUp:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonLogin:
                if (isFormValid())
                {
                    api_parameter = new JSONObject();
                    try
                    {
                        api_parameter.put("username", editEmail.getText().toString().trim());
                        api_parameter.put("password", editPassword.getText().toString().trim());
                    }
                    catch (JSONException e)   {}
                    Log.e("Meruvia:----: " , api_parameter.toString());
                    RunLoginService();
                }

                break;
            default:
                break;
        }
    }

    public boolean isFormValid()
    {
        boolean isValid = true;

        if (editEmail.getText().toString().trim().length() == 0){
            editEmail.setError("Ingresar correo");
            isValid = false;
        }
        else
        {
            editEmail.setError(null);
        }

        if (editPassword.getText().toString().trim().length() == 0){
            editPassword.setError("Ingresar contraseña");
            isValid = false;
        }
        else
        {
            editPassword.setError(null);
        }

        return isValid;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        MySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
    }

    public void RunLoginService()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, Network.API_URL + "auth/login", api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Response: " , response.toString());
                            SharedPreferences.Editor user = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();

                            user.putLong("id", response.getLong("userId"));
                            user.putInt("logged_in", 1);
                            /*temporalmente mostrar el username en lugar de nombre y apellido*/
                            user.putString("firstname", "Eddy");
                            user.putString("lastname", "Quenallata");

                            user.putString("username",response.getString("username"));
                            user.putString("email", response.getString("email"));
                            user.putString("phone",response.getString("phone"));
                            user.putString("documentType",response.getString("documentType"));
                            user.putString("documentNumber",response.getString("documentNumber"));
                            user.putString("phone2",response.getString("phone2"));
                            user.putString("webSite",response.getString("webSite"));
                            user.putString("profilePhoto",response.getString("profilePhoto"));
                            if(response.has("yng_Ubication") && !response.isNull("yng_Ubication")){
                                user.putString("yng_Ubication", String.valueOf(response.getJSONObject("yng_Ubication")));
                            }else{
                                user.putString("yng_Ubication", "null");
                            }
                            /*Decidir si va ser el password o authorization*/
                            user.putString("api_key", Base64.encodeToString(
                                    (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP));
                            user.putString("password", Base64.encodeToString(
                                    (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP));
                            user.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(LoginActivity.this, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", Network.API_KEY);
                params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP)
                );
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }
}
