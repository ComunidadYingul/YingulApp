package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.adapter.CityAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Person;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUbicationSetCityByZipFragment extends Fragment {

    MaterialDialog progressDialog;
    private JSONObject api_parameter;
    private EditText editPostalCode;
    private Button buttonFindBranch;
    private LinearLayout layoutWithoutZip;
    private Yng_Ubication ubication;
    private Yng_City city;
    private TextView title;

    private ListView list;
    private CityAdapter adapter;
    private ArrayList<Yng_City> array_list;
    private MaterialDialog setting_address_edit_dialog;
    public static final String TAG = "NewUbication";

    public NewUbicationSetCityByZipFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_find_shipping_branch, container, false);

        ubication = new Yng_Ubication();
        city = new Yng_City();
        array_list = new ArrayList<Yng_City>();
        adapter = new CityAdapter(getContext(), array_list);
        title = (TextView) v.findViewById(R.id.title);
        layoutWithoutZip = (LinearLayout) v.findViewById(R.id.layoutWithoutZip);
        editPostalCode = (EditText) v.findViewById(R.id.editPostalCode);
        buttonFindBranch = (Button) v.findViewById(R.id.buttonFindBranch);
        title.setText("Ingresa tu código postal.");
        buttonFindBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if(val.valCantString(editPostalCode,4)){
                    getUbicationByZip();
                }
            }
        });
        layoutWithoutZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUbicationSetCountryFragment fragment = new NewUbicationSetCountryFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        getPerson();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void getUbicationByZip() {
        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "ubication/getCitiesByZip/"+editPostalCode.getText().toString().trim(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            if(response.length()==0){
                                Toast.makeText(getContext(), "Sin resultados, verifique su código postal", Toast.LENGTH_LONG).show();
                            }else{
                                JSONArray queries = response;
                                array_list.clear();
                                for (int i = 0; i < queries.length(); i++) {
                                    JSONObject obj = queries.getJSONObject(i);
                                    Yng_City city = new Yng_City();
                                    city.setCityId(obj.optInt("cityId"));
                                    city.setName(obj.optString("name"));
                                    array_list.add(city);
                                }
                                adapter.notifyDataSetChanged();
                                setCity();
                            }
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }
    public void setCity(){
        setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.set_city_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                city = adapter.getItem(position);
                                setUbicationByCity();
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });
                    }
                })
                .callback(new MaterialDialog.ButtonCallback()
                {

                })
                .show();
    }
    public void setUbicationByCity(){
        progressDialog.show();
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "ubication/getUbicationByCity/"+city.getCityId(), api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try
                        {
                            Gson gson = new Gson();
                            ubication = gson.fromJson(String.valueOf(response), Yng_Ubication.class);
                            ((NewUserUbicationEditPersonalInfoActivity)getActivity()).country = ubication.getYng_Country();
                            ((NewUserUbicationEditPersonalInfoActivity)getActivity()).province = ubication.getYng_Province();
                            ((NewUserUbicationEditPersonalInfoActivity)getActivity()).city = ubication.getYng_City();
                            NewUbicationSetDetailFragment fragment = new NewUbicationSetDetailFragment();
                            FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(getContext(), R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(),"Ocurrio un error vuelva a intentarlo mas tarde.",Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = MySingleton.getInstance(getContext()).getRequestQueue();
        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);
        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }
    Yng_User user;
    public void getPerson(){
        progressDialog.show();
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
        SharedPreferences settings2 = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
        Log.e("settieng : ",""+settings2.getString("phone",""));

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "user/person/"+settings.getString("username",""), api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try
                        {
                            Yng_Person personTemp=new Yng_Person();
                            Gson gson = new Gson();
                            personTemp = gson.fromJson(String.valueOf(response), Yng_Person.class);
                            //Log.e("Yng_User user;",""+user.getUsername());
                            Log.e("da",""+personTemp.isBusiness()+" : "+personTemp.getName());
                            if(personTemp.isBusiness()){
                                title.setText("Ingresa la dirección de la Casa Central de la Empresa");
                            }

                            /*((NewUserUbicationEditPersonalInfoActivity)getActivity()).country = ubication.getYng_Country();
                            ((NewUserUbicationEditPersonalInfoActivity)getActivity()).province = ubication.getYng_Province();
                            ((NewUserUbicationEditPersonalInfoActivity)getActivity()).city = ubication.getYng_City();
                            NewUbicationSetDetailFragment fragment = new NewUbicationSetDetailFragment();
                            FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();*/
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(getContext(), R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(),"Ocurrio un error vuelva a intentarlo mas tarde.",Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = MySingleton.getInstance(getContext()).getRequestQueue();
        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);
        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }
}