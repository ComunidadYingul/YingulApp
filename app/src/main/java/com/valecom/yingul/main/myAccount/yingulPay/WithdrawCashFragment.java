package com.valecom.yingul.main.myAccount.yingulPay;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.rey.material.widget.Spinner;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.adapter.SimpleAdapterBank;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.model.Yng_Account;
import com.valecom.yingul.model.Yng_Bank;
import com.valecom.yingul.model.Yng_Transaction;
import com.valecom.yingul.model.Yng_WireTransfer;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawCashFragment extends Fragment {

    Yng_Transaction yngTransaction;
    Yng_WireTransfer yngWireTransfer;
    Yng_Bank yngBank;
    Yng_Account yngAccount;
    JSONObject dataForBuyer;

    String username,authorization;

    EditText editTitularName,editCuitCuil,editCBU,editAmount;
    Spinner spBank,spTypeAccount,spCuitCuil;
    Button buttonWiretransfer;

    private MaterialDialog progressDialog;
    Context mContext;
    ArrayList<Yng_Bank> array_list_bank;
    SimpleAdapterBank spAdapter;

    public WithdrawCashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_withdraw_cash, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
        username = settings.getString("username","");
        authorization = settings.getString("password","");
        Log.e("authorization",username+" "+authorization);

        mContext = getActivity();
        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
        array_list_bank = new ArrayList<>();

        editTitularName = (EditText)v.findViewById(R.id.editTitularName);
        editCuitCuil  = (EditText)v.findViewById(R.id.editCuitCuil);
        editCBU = (EditText)v.findViewById(R.id.editCBU);
        editAmount = (EditText)v.findViewById(R.id.editAmount);
        spCuitCuil = (Spinner)v.findViewById(R.id.spCuitCuil);
        spBank = (Spinner)v.findViewById(R.id.spBank);
        spTypeAccount = (Spinner)v.findViewById(R.id.spTypeAccount);
        buttonWiretransfer = (Button)v.findViewById(R.id.buttonWiretransfer);

        ArrayAdapter<String> spinnerArrayAdapter;

        String cuitCuil[] = {"CUIL","CUIT"};
        spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, cuitCuil);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCuitCuil.setAdapter(spinnerArrayAdapter);

        String typeAccount[] = {"Elige tipo de cuenta","Cuenta corriente","Caja de ahorro"};
        spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, typeAccount);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spTypeAccount.setAdapter(spinnerArrayAdapter);

        spCuitCuil.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                editCuitCuil.setText("");
            }
        });

        editCuitCuil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(spCuitCuil.getSelectedItemPosition() == 1) {
                    String aux = editCuitCuil.getText().toString().trim();
                    if (editCuitCuil.getText().toString().trim().contains("-")) {
                        aux = editCuitCuil.getText().toString().trim().replace("-", "");
                    }
                    if (aux.length() > 2 && aux.length() <= 10) {
                        if (aux.contains("-")) {
                            aux = aux.replace("-", "");
                        }
                        editCuitCuil.removeTextChangedListener(this);
                        editCuitCuil.setText(aux.substring(0, 2) + "-".toString() + aux.substring(2, aux.length()));
                        editCuitCuil.setSelection(editCuitCuil.getText().toString().trim().length());  // Set selection
                        editCuitCuil.addTextChangedListener(this);
                    }
                    if (aux.length() > 10) {
                        if (aux.contains("-")) {
                            aux = aux.replace("-", "");
                        }
                        editCuitCuil.removeTextChangedListener(this);
                        editCuitCuil.setText(aux.substring(0, 2) + "-" + aux.substring(2, 10) + "-" + aux.substring(10, aux.length()));
                        editCuitCuil.setSelection(editCuitCuil.getText().toString().trim().length());  // Set selection
                        editCuitCuil.addTextChangedListener(this);
                    }
                }
            }
        });

        buttonWiretransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(isFormValid()){
                    yngTransaction = new Yng_Transaction();

                    try {

                        yngTransaction.setAccount(yngAccount);
                        yngTransaction.setAmount(Double.parseDouble(editAmount.getText().toString()));
                        yngTransaction.setCurrency("ARS");
                        yngTransaction.setIp(dataForBuyer.getString("query"));
                        yngTransaction.setOrg(dataForBuyer.getString("org"));
                        yngTransaction.setLat(dataForBuyer.getString("lat"));
                        yngTransaction.setLon(dataForBuyer.getString("lon"));
                        yngTransaction.setCity(dataForBuyer.getString("city"));
                        yngTransaction.setCountry(dataForBuyer.getString("country"));
                        yngTransaction.setCountryCode(dataForBuyer.getString("countryCode"));
                        yngTransaction.setRegionName(dataForBuyer.getString("regionName"));
                        yngTransaction.setZip(dataForBuyer.getString("zip"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    yngBank = new Yng_Bank();
                    yngBank.setBankId(((Yng_Bank)spBank.getSelectedItem()).getBankId());

                    yngWireTransfer = new Yng_WireTransfer();

                    yngWireTransfer.setBank(yngBank);
                    yngWireTransfer.setTitularName(editTitularName.getText().toString());
                    yngWireTransfer.setCuitCuil(spCuitCuil.getSelectedItem().toString());
                    yngWireTransfer.setCuitCuilNumber(Long.valueOf(editCuitCuil.getText().toString().trim().replace("-","")));
                    if(spTypeAccount.getSelectedItemPosition()==1){
                        yngWireTransfer.setAccountType("CURRENT");
                    }else if(spTypeAccount.getSelectedItemPosition()==2){
                        yngWireTransfer.setAccountType("SAVING");
                    }
                    yngWireTransfer.setCbu(Long.valueOf(editCBU.getText().toString()));
                    yngWireTransfer.setAmount(Double.parseDouble(editAmount.getText().toString()));
                    yngWireTransfer.setCurrency("ARS");
                    yngWireTransfer.setTransaction(yngTransaction);

                    Gson gson = new Gson();
                    String json = gson.toJson(yngWireTransfer);

                    //String json = "{\"bank\":{\"bankId\":\"32\"},\"transaction\":{\"account\":{\"accountId\":27,\"withheldMoney\":0,\"availableMoney\":3680.5,\"releasedMoney\":1500,\"currency\":\"ARS\",\"accountNonExpired\":true,\"accountNonLocked\":true,\"user\":null},\"amount\":\"100\",\"currency\":\"ARS\",\"ip\":\"181.188.176.133\",\"org\":\"Telefónica Celular de Bolivia S.A.\",\"lat\":-16.5,\"lon\":-68.15,\"city\":\"La Paz\",\"country\":\"Bolivia\",\"countryCode\":\"BO\",\"regionName\":\"Departamento de La Paz\",\"zip\":\"\"},\"titularName\":\"simon\",\"cuitCuil\":\"CUIL\",\"cuitCuilNumber\":\"7654765\",\"accountType\":\"SAVING\",\"cbu\":\"768767\",\"amount\":\"100\",\"currency\":\"ARS\"}";

                    Log.e("Transaction:----",json);

                    SetCreateWiretranfer(Network.API_URL + "wireTransfer/create",json);
                }
            }
        });

        RunGetBank();

        return v;
    }

    public void RunGetBank()
    {
        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest
                (Request.Method.GET, Network.API_URL + "bank/all", null,
                        new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Transaction:--",m_jArry.toString());
                            Log.e("Items", m_jArry.length()+"");
                            Yng_Bank item = new Yng_Bank();
                            item.setBankId(null);
                            item.setName("Seleccione una opcion");
                            array_list_bank.add(item);
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);

                                item = new Yng_Bank();
                                item.setBankId(jo_inside.getLong("bankId"));
                                item.setName(jo_inside.getString("name"));

                                Log.e("Items",item.getName()+"");
                                array_list_bank.add(item);


                            }
                            setAdapterBank();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Log.e("error","1");
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
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
                //params.put("X-API-KEY", Network.API_KEY);
                //params.put("Authorization",authorization);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(mContext.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        //postRequest.setTag(TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);
    }

    public void setAdapterBank(){
        spAdapter = new SimpleAdapterBank(getActivity(), array_list_bank);
        spBank.setAdapter(spAdapter);

        spBank.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if ((position!=0) && (id!=0)) {
                    Object item = parent.getSelectedItem();
                    //Object item = parent.getItemAtPosition(pos);
                    //int idSeleccionado = ((YngCategory) item).getId();
                    //String nombre = ((Yng_Bank) item).getBankId().toString();
                    //Toast.makeText(getActivity(), nombre, Toast.LENGTH_SHORT).show();
                }
            }
        });

        RunGetDataForUser();
    }

    public void RunGetDataForUser()
    {
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://ip-api.com/json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                try {
                    Log.e("RespuestaReq:-----",response.getString("query"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dataForBuyer = response;

                RunGetAccount();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    // If the response is JSONObject instead of expected JSONArray
                    progressDialog.dismiss();
                }
                Log.e("ErrorResp:-----",error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void SetCreateWiretranfer(String url, String json){
        start("inicio");
        MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","Z29uemFsb21lcnV2aWE6bWVydXZpYQ==")
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
                try {
                    end(""+responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("responce",""+responce);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("responce:------------",""+responce);
                        if(responce.equals("save")) {
                            Toast.makeText(getActivity(), responce, Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(getActivity(), LoginActivity.class);
                            //startActivity(intent);
                        }else
                            Toast.makeText(getActivity(),"Error al guardar",Toast.LENGTH_LONG).show();
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

    public void RunGetAccount()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "account/getAccountByUser/"+username, null, new Response.Listener<JSONObject>()
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
                            yngAccount = gson.fromJson(response.toString(), Yng_Account.class);

                            Log.e("Response:-- " , response.toString());


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
                //params.put("X-API-KEY", Network.API_KEY);
                params.put("Authorization",authorization);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(mContext.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        //postRequest.setTag(TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);
    }

    public boolean isFormValid()
    {

        boolean isValid = true;

        Validacion val = new Validacion();

        if (editTitularName.getText().toString().trim().length() == 0)
        {
            editTitularName.setError("Nombre requerido");
            editTitularName.requestFocus();
            isValid = false;
        } else if (spCuitCuil.getSelectedItemPosition() == 1 && !val.valCuit(editCuitCuil))
        {
            isValid = false;
        } else if (spCuitCuil.getSelectedItemPosition() == 0 && editCuitCuil.getText().toString().trim().length() == 0)
        {
            editCuitCuil.setError("Campo requerido");
            editCuitCuil.requestFocus();
            isValid = false;
        } else if (spBank.getSelectedItemPosition()==0)
        {
            Toast.makeText(getActivity(),"Elegir banco",Toast.LENGTH_SHORT).show();
            spBank.requestFocus();
            isValid = false;
        } else if (spTypeAccount.getSelectedItemPosition()==0)
        {
            Toast.makeText(getActivity(),"Selecciona un tipo de cuenta",Toast.LENGTH_SHORT).show();
            spTypeAccount.requestFocus();
            isValid = false;
        } else if (editCBU.getText().toString().trim().length() == 0)
        {
            editCBU.setError("Correo requerido");
            editCBU.requestFocus();
            isValid = false;
        } else if (editAmount.getText().toString().trim().length() == 0)
        {
            editAmount.setError("Contraseña requerida");
            editAmount.requestFocus();
            isValid = false;
        } else
        {
            editAmount.setError(null);
        }

        return isValid;
    }
}
