package com.valecom.yingul.main.property;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.SimpleAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyActivity extends AppCompatActivity {

    private MaterialDialog progressDialog;
    private Context mContext;

    Spinner optionsCategory,optionsCondition;
    EditText textUbication;

    ArrayList<Yng_Category> array_list;
    SimpleAdapter adapter;
    ArrayList<Yng_Category> array_list_2;

    Long father;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        mContext = PropertyActivity.this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inmuebles");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_list = new ArrayList<Yng_Category>();



        optionsCategory = (Spinner) findViewById(R.id.options_category);

        adapter = new SimpleAdapter(this, array_list);

        optionsCategory.setAdapter(adapter);

        optionsCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if ((pos!=0) && (id!=0)) {
                    Yng_Category item = (Yng_Category) parent.getItemAtPosition(pos);
                    String nombre = item.getCategoryId().toString();
                    father = item.getCategoryId();
                    Toast.makeText(getBaseContext(), nombre, Toast.LENGTH_SHORT).show();

                    FillSubCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RunGetCategory();


    }

    public void RunGetCategory()
    {
        progressDialog.show();


        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/Property/0",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {


                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
                            array_list.clear();
                            Yng_Category item = new Yng_Category();
                            item.setCategoryId((long)0);
                            item.setName("Elija una opción");
                            array_list.add(item);
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);

                                item = new Yng_Category();

                                item.setCategoryId(obj.getLong("categoryId"));
                                item.setName(obj.getString("name"));

                                Gson gson = new Gson();
                                String json = gson.toJson(item);
                                Log.e("obj category:--",json);

                                array_list.add(item);
                            }

                            adapter.notifyDataSetChanged();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(mContext, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(mContext, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //SharedPreferences settings = getActivity().getSharedPreferences(ActivityLogin.SESSION_USER, getActivity().MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", Network.API_KEY);
                /*params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (settings.getString("email","")+":" + settings.getString("api_key","")).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance( mContext).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);

    }

    public void FillSubCategory(){
        array_list_2 = new ArrayList<Yng_Category>();

        RunGetSubCategory();

        optionsCondition = (Spinner) findViewById(R.id.options_condition);

        adapter = new SimpleAdapter(this, array_list_2);

        optionsCondition.setAdapter(adapter);
    }

    public void RunGetSubCategory()
    {
        progressDialog.show();


        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/father/"+father,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {


                            JSONArray items = response;
                            if(items.length()==0){

                            }

                            array_list_2.clear();
                            Yng_Category item = new Yng_Category();
                            item.setCategoryId((long)0);
                            item.setName("Elija una opción");
                            array_list_2.add(item);
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);

                                item = new Yng_Category();

                                item.setCategoryId(obj.getLong("categoryId"));
                                item.setName(obj.getString("name"));


                                array_list_2.add(item);
                            }

                            adapter.notifyDataSetChanged();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(mContext, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(mContext, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(mContext, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //SharedPreferences settings = getActivity().getSharedPreferences(ActivityLogin.SESSION_USER, getActivity().MODE_PRIVATE);
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
        RequestQueue queue = MySingleton.getInstance( mContext).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);

    }

}
