package com.valecom.yingul.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.model.Yng_Business;
import com.valecom.yingul.model.Yng_Person;
import com.valecom.yingul.model.Yng_Ubication;
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
    private EditText editFirstname, editLastname, editEmail, editPassword, editBusinessName, editDocumentNumber;
    private TextView textTitle, txtPolicies;
    private CheckBox checkBussines;
    com.rey.material.widget.Spinner spinner_type_contributor;

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

        textTitle = (TextView)findViewById(R.id.text_title);
        textTitle.setTypeface(typeface1);
        editFirstname = (EditText) findViewById(R.id.editFirstname);
        editLastname = (EditText) findViewById(R.id.editLastname);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        txtPolicies = (TextView) findViewById(R.id.txtPolicies);
        checkBussines = (CheckBox) findViewById(R.id.checkBussines);
        editBusinessName = (EditText) findViewById(R.id.editBusinessName);
        editDocumentNumber = (EditText) findViewById(R.id.editDocumentNumber);
        editBusinessName.setText("a");
        editDocumentNumber.setText("a");
        editBusinessName.setVisibility(View.GONE);
        editDocumentNumber.setVisibility(View.GONE);
        spinner_type_contributor=(com.rey.material.widget.Spinner) findViewById(R.id.spinner_type_contributor);
        spinner_type_contributor.setVisibility(View.GONE);
        String typeDocument[] = {"Exento","Exterior","IVA No Alcanzado","Monotributista","Responsable Inscripto"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, typeDocument);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        spinner_type_contributor.setAdapter(spinnerArrayAdapter);
        checkBussines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if(isChecked){
                        editBusinessName.setVisibility(View.VISIBLE);
                        editDocumentNumber.setVisibility(View.VISIBLE);
                        spinner_type_contributor.setVisibility(View.VISIBLE);
                        editBusinessName.setText("");
                        editDocumentNumber.setText("");
                    }else{
                        editBusinessName.setVisibility(View.GONE);
                        editDocumentNumber.setVisibility(View.GONE);
                        spinner_type_contributor.setVisibility(View.GONE);
                        editBusinessName.setText("a");
                        editDocumentNumber.setText("a");
                    }
               }
           }
        );
        editDocumentNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)  {    }

            @Override
            public void afterTextChanged(Editable s)
            {
                String aux=editDocumentNumber.getText().toString().trim();
                if(editDocumentNumber.getText().toString().trim().contains("-")){
                    aux=editDocumentNumber.getText().toString().trim().replace("-","");
                }
                if(aux.length()>2&&aux.length()<=10){
                    if(aux.contains("-")){
                        aux=aux.replace("-","");
                    }
                    editDocumentNumber.removeTextChangedListener(this);
                    editDocumentNumber.setText(aux.substring(0, 2)+"-".toString()+aux.substring(2, aux.length()));
                    editDocumentNumber.setSelection(editDocumentNumber.getText().toString().trim().length() );  // Set selection
                    editDocumentNumber.addTextChangedListener(this);
                }
                if(aux.length()>10){
                    if(aux.contains("-")){
                        aux=aux.replace("-","");
                    }
                    editDocumentNumber.removeTextChangedListener(this);
                    editDocumentNumber.setText(aux.substring(0, 2)+"-"+aux.substring(2, 10)+"-"+aux.substring(10, aux.length()));
                    editDocumentNumber.setSelection(editDocumentNumber.getText().toString().trim().length() );  // Set selection
                    editDocumentNumber.addTextChangedListener(this);
                }
            }
        });
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

        persona.setName(editFirstname.getText().toString().trim());
        persona.setLastname(editLastname.getText().toString().trim());

        user.setEmail(editEmail.getText().toString().trim());
        user.setPassword(editPassword.getText().toString().trim());

        persona.setYng_User(user);
        Gson gson = new Gson();
        if(checkBussines.isChecked()){
            persona.setBusiness(true);
            Yng_Ubication ubication = new Yng_Ubication();
            persona.getYng_User().setYng_Ubication(ubication);
            Yng_Business business = new Yng_Business();
            business.setBusinessName(editBusinessName.getText().toString().trim());
            business.setDocumentType("CUIT");
            business.setDocumentNumber(editDocumentNumber.getText().toString().trim().replace("-",""));
            switch (spinner_type_contributor.getSelectedItemPosition())
            {
                case 0:
                    business.setContributorType("Exento");
                    break;
                case 1:
                    business.setContributorType("Exterior");
                    break;
                case 2:
                    business.setContributorType("IVA No Alcanzado");
                    break;
                case 3:
                    business.setContributorType("Monotributista");
                    break;
                case 4:
                    business.setContributorType("Responsable Inscripto");
                    break;
                default:
                    business.setContributorType("Exento");
                    break;
            }
            String json ="\"{\\\"person\\\":"+(gson.toJson(persona).replace("\"","\\\""))+",\\\"business\\\":"+(gson.toJson(business).replace("\"","\\\""))+"}\"";
            //String json ="\"{\\\"person\\\":{\\\"yng_User\\\":{\\\"yng_Ubication\\\":{\\\"yng_Province\\\":{\\\"yng_Country\\\":{}},\\\"yng_City\\\":{},\\\"yng_Barrio\\\":{},\\\"yng_Country\\\":{}},\\\"email\\\":\\\"quenallataeddy@gmail.com\\\",\\\"password\\\":\\\"eddy\\\"},\\\"name\\\":\\\"Eddy\\\",\\\"lastname\\\":\\\"Quenallata\\\",\\\"business\\\":true},\\\"business\\\":{\\\"user\\\":{\\\"yng_Ubication\\\":{\\\"yng_Province\\\":{\\\"yng_Country\\\":{}},\\\"yng_City\\\":{},\\\"yng_Barrio\\\":{},\\\"yng_Country\\\":{}}},\\\"businessName\\\":\\\"yingul compamny\\\",\\\"documentType\\\":\\\"CUIT\\\",\\\"documentNumber\\\":\\\"7054200010\\\"}}\"";
            Log.e("========",json);
            getAsyncCall(Network.API_URL + "business",json);
        }else{
            persona.setBusiness(false);
            String json = gson.toJson(persona);

            getAsyncCall(Network.API_URL + "signup",json);
            Log.e("persona",json);
        }


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
                            Toast.makeText(RegisterActivity.this, "Sea ha registrado a Yingul exitosamente.", Toast.LENGTH_LONG).show();
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

        Validacion val = new Validacion();

        if (!val.valTextWithoutAcent(editFirstname))
        {
            isValid = false;
        }else if (!val.valTextWithoutAcent(editLastname))
        {
            isValid = false;
        }else if (!val.valEmail(editEmail))
        {
            isValid = false;
        }else if (!val.valPassword(editPassword))
        {
            isValid = false;
        }else if (!val.valCadVacia(editBusinessName))
        {
            isValid = false;
        }else if (!val.valCadVacia(editDocumentNumber))
        {
            isValid = false;
        }else {
            isValid = true;
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
