package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.rey.material.widget.Spinner;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.model.Yng_Business;
import com.valecom.yingul.model.Yng_Person;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountUserProfileFragment extends Fragment {

    private TextView textFullname,textUsername,textEmail,textPhone,textDocument,textTitleUser,
            textBusinessName,textBusinessDocument,editInputTitle,textUserUbication,textLegend;
    private LinearLayout lytEditDocument,lytTitleBusiness,lytBodyBusiness,lytEditBusinessName,lytEditBusinessDocument,lytEditPhone,lytSecondInput;
    private MaterialDialog setting_address_edit_dialog;
    private EditText editUserDocumentNumber,editInputText,editSecondInputText;
    Context mContext;
    Spinner spinner_currency;

    String tempUsername;
    private Yng_User user;
    private Yng_Ubication userUbication;
    private Yng_Person person;
    private Yng_Business business;

    public static final String TAG = "LoginActivity";
    private MaterialDialog progressDialog;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public MyAccountUserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
        tempUsername = settings.getString("username","");
        Log.e("username",tempUsername);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_account_user_profile, container, false);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        mContext = getActivity();
        final Validacion val = new Validacion();
        user = new Yng_User();
        person = new Yng_Person();
        userUbication = new Yng_Ubication();
        business = new Yng_Business();
        textUserUbication = (TextView) v.findViewById(R.id.textUserUbication);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){
                userUbication=null;
                textUserUbication.setText("");
            }else{
                Gson gson = new Gson();
                userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                textUserUbication.setText(userUbication.getYng_Country().getName()+" "+userUbication.getYng_Province().getName()+" "+userUbication.getYng_City().getName()+" "+userUbication.getStreet()+" "+userUbication.getNumber());
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
        }
        lytEditPhone = (LinearLayout) v.findViewById(R.id.lytEditPhone);
        lytEditBusinessDocument = (LinearLayout) v.findViewById(R.id.lytEditBusinessDocument);
        lytTitleBusiness = (LinearLayout) v.findViewById(R.id.lytTitleBusiness);
        lytBodyBusiness = (LinearLayout) v.findViewById(R.id.lytBodyBusiness);
        textBusinessDocument = (TextView) v.findViewById(R.id.textBusinessDocument);
        textBusinessName = (TextView) v.findViewById(R.id.textBusinessName);
        textTitleUser = (TextView) v.findViewById(R.id.textTitleUser);
        textFullname = (TextView)v.findViewById(R.id.textFullname);
        textUsername = (TextView)v.findViewById(R.id.textUsername);
        textEmail = (TextView)v.findViewById(R.id.textEmail);
        textPhone = (TextView)v.findViewById(R.id.textPhone);
        textDocument = (TextView)v.findViewById(R.id.textDocument);
        lytEditBusinessName = (LinearLayout) v.findViewById(R.id.lytEditBusinessName);
        lytEditDocument = (LinearLayout) v.findViewById(R.id.lytEditDocument);
        lytEditDocument.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.edit_document_layout, true)
                    .positiveText("OK")
                    .cancelable(false)
                    .negativeText("Cancelar")
                    .showListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            View view = setting_address_edit_dialog.getCustomView();
                            editUserDocumentNumber = (EditText) view.findViewById(R.id.editDocumentNumber);
                            spinner_currency = (Spinner) view.findViewById(R.id.spinner_currency);
                            String currency[] = {"DNI"};
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),   android.R.layout.simple_spinner_item, currency);
                            spinner_currency.setAdapter(spinnerArrayAdapter);

                            spinner_currency.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(Spinner parent, View view, int position, long id) {
                                    editUserDocumentNumber.setText("");
                                }
                            });

                            setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                            editUserDocumentNumber.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if(spinner_currency.getSelectedItemPosition() == 0){
                                        String aux=editUserDocumentNumber.getText().toString().trim();
                                        if(editUserDocumentNumber.getText().toString().trim().contains("-")){
                                            aux=editUserDocumentNumber.getText().toString().trim().replace("-","");
                                        }
                                        if(aux.length()>2&&aux.length()<=10){
                                            if(aux.contains(".")){
                                                aux=aux.replace(".","");
                                            }
                                            editUserDocumentNumber.removeTextChangedListener(this);
                                            editUserDocumentNumber.setText(aux.substring(0, 2)+".".toString()+aux.substring(2, aux.length()));
                                            editUserDocumentNumber.setSelection(editUserDocumentNumber.getText().toString().trim().length() );  // Set selection
                                            editUserDocumentNumber.addTextChangedListener(this);
                                        }
                                        if(aux.length()>5){
                                            if(aux.contains(".")){
                                                aux=aux.replace(".","");
                                            }
                                            editUserDocumentNumber.removeTextChangedListener(this);
                                            editUserDocumentNumber.setText(aux.substring(0, 2)+"."+aux.substring(2, 5)+"."+aux.substring(5, aux.length()));
                                            editUserDocumentNumber.setSelection(editUserDocumentNumber.getText().toString().trim().length() );  // Set selection
                                            editUserDocumentNumber.addTextChangedListener(this);
                                        }

                                        if(!val.valDni2(editUserDocumentNumber)){
                                            setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                        }else{
                                            setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                        }
                                    }else{

                                    }
                                }
                            });
                        }
                    })
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            if (val.valNumber(editUserDocumentNumber))
                            {
                                Yng_User temp = new Yng_User();
                                temp = user;
                                if (spinner_currency.getSelectedItemPosition() == 0) {
                                    temp.setDocumentType("DNI");
                                } else {
                                    temp.setDocumentType("CUIT");
                                }
                                temp.setDocumentNumber(editUserDocumentNumber.getText().toString().trim().replace(".",""));
                                Gson gson = new Gson();
                                String jsonBody = gson.toJson(temp);
                                Log.e("user final", jsonBody);
                                requestPost(Network.API_URL + "user/updateUserDocument",jsonBody);
                                dialog.dismiss();
                            }

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
            }
        });
        lytEditBusinessName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                        .customView(R.layout.edit_input_layout, true)
                        .positiveText("OK")
                        .cancelable(false)
                        .negativeText("Cancelar")
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                View view = setting_address_edit_dialog.getCustomView();
                                editInputTitle = (TextView) view.findViewById(R.id.textTitle);
                                editInputText = (EditText) view.findViewById(R.id.editInputText);
                                editInputTitle.setText("Editar razón social");
                                editInputText.setHint("Razón social");
                                editInputText.setInputType(InputType.TYPE_CLASS_TEXT);

                            }
                        })
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                if (val.valStringFiveChar(editInputText)) {
                                    Yng_Business temp = new Yng_Business();
                                    temp = business;

                                    temp.setBusinessName(editInputText.getText().toString().trim());
                                    Gson gson = new Gson();
                                    String jsonBody = gson.toJson(temp);
                                    Log.e("user final", jsonBody);
                                    requestPost(Network.API_URL + "user/updateBusinessName", jsonBody);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });
        lytEditBusinessDocument.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                        .customView(R.layout.edit_input_layout, true)
                        .positiveText("OK")
                        .cancelable(false)
                        .negativeText("Cancelar")
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                View view = setting_address_edit_dialog.getCustomView();
                                editInputTitle = (TextView) view.findViewById(R.id.textTitle);
                                editInputText = (EditText) view.findViewById(R.id.editInputText);
                                textLegend = (TextView) view.findViewById(R.id.textLegend);

                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                                editInputText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        String aux=editInputText.getText().toString().trim();
                                        if(editInputText.getText().toString().trim().contains("-")){
                                            aux=editInputText.getText().toString().trim().replace("-","");
                                        }
                                        if(aux.length()>2&&aux.length()<=10){
                                            if(aux.contains("-")){
                                                aux=aux.replace("-","");
                                            }
                                            editInputText.removeTextChangedListener(this);
                                            editInputText.setText(aux.substring(0, 2)+"-".toString()+aux.substring(2, aux.length()));
                                            editInputText.setSelection(editInputText.getText().toString().trim().length() );  // Set selection
                                            editInputText.addTextChangedListener(this);
                                        }
                                        if(aux.length()>10){
                                            if(aux.contains("-")){
                                                aux=aux.replace("-","");
                                            }
                                            editInputText.removeTextChangedListener(this);
                                            editInputText.setText(aux.substring(0, 2)+"-"+aux.substring(2, 10)+"-"+aux.substring(10, aux.length()));
                                            editInputText.setSelection(editInputText.getText().toString().trim().length() );  // Set selection
                                            editInputText.addTextChangedListener(this);
                                        }

                                        if(!val.valCuit2(editInputText)){
                                            setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                        }else{
                                            setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                        }
                                    }
                                });

                                editInputTitle.setText("Editar CUIT");
                                editInputText.setHint("CUIT escribe solo números");
                                editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);

                            }
                        })
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                if (val.valNumber(editInputText)) {
                                    Yng_Business temp = new Yng_Business();
                                    temp = business;
                                    temp.setDocumentType("CUIT");
                                    temp.setDocumentNumber(editInputText.getText().toString().trim().replace("-",""));
                                    Gson gson = new Gson();
                                    String jsonBody = gson.toJson(temp);
                                    Log.e("user final", jsonBody);
                                    requestPost(Network.API_URL + "user/updateBusinessDocumentNumber", jsonBody);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });
        lytEditPhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                        .customView(R.layout.edit_input_layout, true)
                        .positiveText("OK")
                        .cancelable(false)
                        .negativeText("Cancelar")
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                View view = setting_address_edit_dialog.getCustomView();
                                lytSecondInput = (LinearLayout) view.findViewById(R.id.lytSecondInput);
                                editInputTitle = (TextView) view.findViewById(R.id.textTitle);
                                editInputText = (EditText) view.findViewById(R.id.editInputText);
                                editSecondInputText = (EditText) view.findViewById(R.id.editSecondInputText);
                                lytSecondInput.setVisibility(View.VISIBLE);
                                editInputTitle.setText("Editar Teléfono");
                                editInputText.setHint("Teléfono principal");
                                editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                editSecondInputText.setHint("Teléfono secundario");
                                editSecondInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            }
                        })
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                if (val.valNumber(editInputText)) {
                                    Yng_User temp = new Yng_User();
                                    temp = user;
                                    temp.setPhone(editInputText.getText().toString().trim());
                                    Gson gson = new Gson();
                                    temp.setPhone2(editSecondInputText.getText().toString().trim());
                                    String jsonBody = gson.toJson(temp);
                                    Log.e("user final", jsonBody);
                                    if(editSecondInputText.getText().toString().trim()!="")
                                    {
                                        requestPost(Network.API_URL + "user/updatePhone", jsonBody);
                                    }else{
                                        requestPost(Network.API_URL + "user/updatePhones", jsonBody);
                                    }
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });


        RunGetPerson();

        return v;
    }


    public void RunGetPerson()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "user/person/"+tempUsername, null, new Response.Listener<JSONObject>()
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
                            Gson gson = new Gson();
                            person = gson.fromJson(String.valueOf(response), Yng_Person.class);
                            textFullname.setText(response.getString("name")+" "+response.getString("lastname"));
                            textUsername.setText(response.getJSONObject("yng_User").getString("username"));
                            textEmail.setText(response.getJSONObject("yng_User").getString("email"));
                            if(response.getJSONObject("yng_User").getString("phone")==null||response.getJSONObject("yng_User").getString("phone")=="null"){
                                textPhone.setText("");
                            }else{
                                textPhone.setText(response.getJSONObject("yng_User").getString("phone"));
                            }
                            String docType =response.getJSONObject("yng_User").getString("documentType");
                            String docNumber = response.getJSONObject("yng_User").getString("documentNumber");
                            if(docNumber.length() > 0) {
                                docNumber = docNumber.substring(0, 2) + "." + docNumber.substring(2, 5) + "." + docNumber.substring(5);
                            }
                            textDocument.setText(docType+" "+docNumber);
                            if(person.isBusiness()){
                                RunGetBusiness();
                                textTitleUser.setText("Datos del representante");
                                lytTitleBusiness.setVisibility(View.VISIBLE);
                                lytBodyBusiness.setVisibility(View.VISIBLE);
                            }else{
                                textTitleUser.setText("Mis datos");
                                lytTitleBusiness.setVisibility(View.GONE);
                                lytBodyBusiness.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(mContext, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(mContext,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("X-API-KEY", Network.API_KEY);
                params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(mContext.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);
    }

    public void RunGetBusiness()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "user/business/"+tempUsername, null, new Response.Listener<JSONObject>()
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
                            Gson gson = new Gson();
                            business = gson.fromJson(String.valueOf(response), Yng_Business.class);
                            textBusinessName.setText(business.getBusinessName());
                            if(business.getDocumentType().equals("CUIT")){
                                String cuitNumber = business.getDocumentNumber().toString();
                                textBusinessDocument.setText(cuitNumber.substring(0,2)+"-"+cuitNumber.substring(2,10)+"-"+cuitNumber.substring(10));
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(mContext, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(mContext,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("X-API-KEY", Network.API_KEY);
                params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(mContext.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);
    }

    public void  requestPost(String url, String json){
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (responce){
                            case "save":
                                Toast.makeText(getContext(), "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
                                RunGetPerson();
                                break;
                            case "documentNumberExists":
                                Toast.makeText(getContext(), "El CUIT ya esta registrado porfavor ingrese otro.",Toast.LENGTH_LONG).show();
                                break;
                            default :
                                Toast.makeText(getContext(), "Algo salio mal porfavor intente mas tarde.",Toast.LENGTH_LONG).show();
                                break;
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
