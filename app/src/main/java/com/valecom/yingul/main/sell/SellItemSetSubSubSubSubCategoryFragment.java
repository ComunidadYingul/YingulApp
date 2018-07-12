package com.valecom.yingul.main.sell;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.CategoryAdapter;
import com.valecom.yingul.main.LoginActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetSubSubSubSubCategoryFragment extends Fragment {

    ListView list;
    String type;
    CategoryAdapter adapter;
    ArrayList<Yng_Category> array_list;
    MaterialDialog progressDialog;
    TextView txtTitleCategory;
    String father,father1;
    private JSONObject api_parameter;

    public SellItemSetSubSubSubSubCategoryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sell_item_set_sub_category, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        txtTitleCategory = (TextView) v.findViewById(R.id.txtTitleCategory);
        switch (type) {
            case "Service":
                txtTitleCategory.setText("¿Qué servicio quieres publicar?");
                break;
            case "Product":
                txtTitleCategory.setText("¿Qué producto quieres publicar?");
                break;
            case "Motorized":
                txtTitleCategory.setText("¿Qué vehículo quieres publicar?");
                break;
            case "Property":
                txtTitleCategory.setText("¿Qué inmueble quieres publicar?");
                break;
        }

        array_list = new ArrayList<Yng_Category>();
        adapter = new CategoryAdapter(getContext(), array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_Category item = adapter.getItem(position);
                father1=item.getCategoryId().toString();
                ((SellActivity)getActivity()).subSubSubSubCategory=item;
                RunGetCategoryService1();
            }
        });

        api_parameter = new JSONObject();
        try
        {
            api_parameter.put("user_id", settings.getLong("id", 0));
        }
        catch(JSONException ex) {}

        RunGetCategoryService();
        ////

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void RunGetCategoryService()
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

                            array_list.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);

                                Yng_Category item = new Yng_Category();

                                item.setCategoryId(obj.getLong("categoryId"));
                                item.setName(obj.getString("name"));


                                array_list.add(item);
                            }

                            adapter.notifyDataSetChanged();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
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
        RequestQueue queue = MySingleton.getInstance( getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);

    }
    public void RunGetCategoryService1()
    {
        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/father/"+father1,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            JSONArray items = response;
                            if(items.length()==0){
                                if(((SellActivity)getActivity()).item.getType()=="Service"){
                                    SellItemSetTypePriceFragment fragment = new SellItemSetTypePriceFragment();
                                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else if(((SellActivity)getActivity()).item.getType()=="Product"){
                                    /***************colocar largo alto ancho peso******************/
                                    SellItemSetVolumeFragment fragment = new SellItemSetVolumeFragment();
                                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else if(((SellActivity)getActivity()).item.getType()=="Motorized"){
                                    SellItemSetKilometerFragment fragment = new SellItemSetKilometerFragment();
                                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }else if(((SellActivity)getActivity()).item.getType()=="Property"){
                                    SellItemSetAreaFragment fragment = new SellItemSetAreaFragment();
                                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }

                            }
                            else{
                                SellItemSetSubSubSubSubCategoryFragment itemSetSubCategory = new SellItemSetSubSubSubSubCategoryFragment();
                                itemSetSubCategory.father= String.valueOf(father1);
                                itemSetSubCategory.type=((SellActivity)getActivity()).item.getType();
                                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, itemSetSubCategory);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
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
        RequestQueue queue = MySingleton.getInstance( getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);

    }

}
