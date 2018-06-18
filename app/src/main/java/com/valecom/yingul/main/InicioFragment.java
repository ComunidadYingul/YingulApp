package com.valecom.yingul.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.model.Yng_Item;
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
public class InicioFragment extends Fragment {

    Button view_all_latest;

    RecyclerView recycler_home_latest;
    LatestListAdapter adapter_latest;
    ArrayList<Yng_Item> array_latest;


    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        view_all_latest=(Button)rootView.findViewById(R.id.btn__view_all_latest);

        array_latest = new ArrayList<>();

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);

        recycler_home_latest = (RecyclerView) rootView.findViewById(R.id.rv_home_latest);
        recycler_home_latest.setHasFixedSize(false);
        recycler_home_latest.setNestedScrollingEnabled(false);
        recycler_home_latest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_latest.addItemDecoration(itemDecoration);

        loadJSONFromAssetHomeLatest();

        return rootView;
    }

    /***************************** ALL ITEMS ********************************/

    public ArrayList<Yng_Item> loadJSONFromAssetHomeLatest() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "index/item/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item itemHomeCategoryList = new Yng_Item();
                                itemHomeCategoryList.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                itemHomeCategoryList.setName(jo_inside.getString("name"));
                                itemHomeCategoryList.setPrincipalImage(jo_inside.getString("principalImage"));
                                itemHomeCategoryList.setDescription(jo_inside.getString("description"));
                                itemHomeCategoryList.setPrice(Double.valueOf(jo_inside.getString("price")));

                                array_latest.add(itemHomeCategoryList);

                            }
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                //Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), "error 1", Toast.LENGTH_LONG).show();
                            }
                        }

                        /*if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }*/
                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                // TODO Auto-generated method stub
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    // If the response is JSONObject instead of expected JSONArray
                    progressDialog.dismiss();
                }*/

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null)
                {
                    try
                    {
                        JSONObject json = new JSONObject(new String(response.data));
                        //Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "error 2", Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        //Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "error 3", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //Toast.makeText(getContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "error 4", Toast.LENGTH_LONG).show();
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

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);


        return array_latest;

    }

    public void setAdapterHomeCategoryList() {

        //adapter_latest = new LatestListAdapter(getActivity(), array_latest);
        //recycler_home_latest.setAdapter(adapter_latest);

        //loadJSONFromAssetHomeCoupon();
    }

}
