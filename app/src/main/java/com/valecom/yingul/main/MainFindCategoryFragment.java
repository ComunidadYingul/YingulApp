package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.CategoryAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.categories.ItemsByCategoryActivity;
import com.valecom.yingul.main.categories.SubCategoryFragment;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.service.ServiceActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Query;
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
public class MainFindCategoryFragment extends Fragment {

    ListView list;
    CategoryAdapter adapter;
    ArrayList<Yng_Category> array_list;
    MaterialDialog progressDialog;
    TextView txtTitleCategory;
    private String nameCategory;
    private JSONObject api_parameter;

    public MainFindCategoryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_main_find_category, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        txtTitleCategory = (TextView) v.findViewById(R.id.txtTitleCategory);

        Bundle bundle = getArguments();
        nameCategory = bundle.getString("nameCategory");
        Log.e("palabra que llega",nameCategory);

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
                Log.e("category id:--",item.getCategoryId().toString());

                Intent intent = new Intent(getActivity(),ItemsByCategoryActivity.class);
                intent.putExtra("categoryId",item.getCategoryId().toString());
                startActivity(intent);
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
        //progressDialog.show();
        if(nameCategory.equals("")){
            nameCategory="a";
        }

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/categories/"+nameCategory.replace(" ",""),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {


                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
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
                params.put("X-API-KEY", Network.API_KEY);
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
