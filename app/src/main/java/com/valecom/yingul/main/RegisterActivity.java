package com.valecom.yingul.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.model.Yng_Person;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText editFirstname;
    private EditText editLastname;
    private EditText editEmail;
    private EditText editPassword;
    private TextView textLogo, textTitle,txtPolicies;
    private CheckBox checkBussines;

    Yng_Person persona;
    Yng_User user;

    public static final String TAG = "RegisterActivity";
    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        Typeface typeface1 = Typeface.createFromAsset(RegisterActivity.this.getAssets(), "fonts/font-yingul.ttf");

        textLogo = (TextView)findViewById(R.id.text_logo);
        textTitle = (TextView)findViewById(R.id.text_title);
        textLogo.setTypeface(typeface1);
        textTitle.setTypeface(typeface1);
        editFirstname = (EditText) findViewById(R.id.editFirstname);
        editLastname = (EditText) findViewById(R.id.editLastname);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        txtPolicies = (TextView) findViewById(R.id.txtPolicies);
        checkBussines = (CheckBox) findViewById(R.id.checkBussines);

        String styledText = "<font color='#ffffff'>Al registrarme, declaro que soy mayor de edad y acepto los </font><font color='#1E88E5'>Términos y Condiciones</font><font color='#ffffff'> y las Políticas de Privacidad de Yingul Company.</font>";
        txtPolicies.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        txtPolicies.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://s3-us-west-2.amazonaws.com/jsa-s3-bucketimage/politicas/terminos-y-condiciones-de-uso.pdf"));
            startActivity(viewIntent);
            }
        });
        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.buttonSignUp:
                if (isFormValid())
                {
                    api_parameter = new JSONObject();
                    try
                    {
                        api_parameter.put("email", editEmail.getText().toString().trim());
                        api_parameter.put("password", editPassword.getText().toString().trim());
                        api_parameter.put("firstname", editFirstname.getText().toString().trim());
                        api_parameter.put("lastname", editLastname.getText().toString().trim());
                    }
                    catch (JSONException e)   {}

                    RunRegisterService();
                }

                break;
            default:
                break;
        }
    }

    public void RunRegisterService()
    {
        progressDialog.show();

        persona = new Yng_Person();
        user = new Yng_User();

        persona.setName(editFirstname.getText().toString());
        persona.setLastname(editLastname.getText().toString());

        user.setEmail(editEmail.getText().toString());
        user.setPassword(editPassword.getText().toString());

        persona.setYng_User(user);

        if(checkBussines.isChecked()){
            persona.setBusiness(true);
        }else{
            persona.setBusiness(false);
        }

        Gson gson = new Gson();
        String json = gson.toJson(persona);

        getAsyncCall(Network.API_URL + "signup",json);
        Log.e("persona",json);
    }

    public void  getAsyncCall(String url, String json){
        MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e("Mensaje", "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }

                final String responce=""+(responseBody.string());
                Log.d("responce",""+responce);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("responce:------------",""+responce);
                        if(responce.equals("save")) {
                            Toast.makeText(RegisterActivity.this, responce, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            if(responce.equals("email exist")){
                                new MaterialDialog.Builder(RegisterActivity.this)
                                        .title("El correo ya esta registrado")
                                        .content("El correo que ingreso ya se encuentra registrado en Yingul Company intente con un correo diferente")
                                        .positiveText(R.string.ok)
                                        .cancelable(false)
                                        .positiveColorRes(R.color.colorAccent)
                                        .callback(new MaterialDialog.ButtonCallback()
                                        {
                                            @Override
                                            public void onPositive(MaterialDialog dialog)
                                            {
                                                progressDialog.dismiss();
                                                dialog.dismiss();

                                                if (dialog != null && dialog.isShowing())
                                                {
                                                    // If the response is JSONObject instead of expected JSONArray
                                                    dialog.dismiss();
                                                }
                                            }
                                        })
                                        .show();
                            }else{
                                new MaterialDialog.Builder(RegisterActivity.this)
                                        .title("Oops!!!")
                                        .content("Algo salio mal por favor vuelva a intentar en unos minutos")
                                        .positiveText(R.string.ok)
                                        .cancelable(false)
                                        .positiveColorRes(R.color.colorAccent)
                                        .callback(new MaterialDialog.ButtonCallback()
                                        {
                                            @Override
                                            public void onPositive(MaterialDialog dialog)
                                            {
                                                progressDialog.dismiss();
                                                dialog.dismiss();

                                                if (dialog != null && dialog.isShowing())
                                                {
                                                    // If the response is JSONObject instead of expected JSONArray
                                                    dialog.dismiss();
                                                }
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                });

            }
        });
    }

    public boolean isFormValid()
    {
        boolean isValid = true;

        if (editFirstname.getText().toString().trim().length() == 0)
        {
            editFirstname.setError("Nombre requerido");
            isValid = false;
        } else
        {
            editFirstname.setError(null);
        }

        if (editLastname.getText().toString().trim().length() == 0)
        {
            editLastname.setError("Apellido requerido");
            isValid = false;
        } else
        {
            editLastname.setError(null);
        }

        if (editEmail.getText().toString().trim().length() == 0)
        {
            editEmail.setError("Correo requerido");
            isValid = false;
        } else
        {
            editEmail.setError(null);
        }

        if (editPassword.getText().toString().trim().length() == 0)
        {
            editPassword.setError("Contraseña requerida");
            isValid = false;
        } else
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
}
