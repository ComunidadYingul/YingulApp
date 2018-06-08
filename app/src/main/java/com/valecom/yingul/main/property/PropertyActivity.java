package com.valecom.yingul.main.property;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.Item.ItemCategoryList;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.store.ActivityStore;
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

    RecyclerView recycler_list;
    ListGridAdapter adapter_list;
    ArrayList<ItemCategoryList> array_list;

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

        array_list = new ArrayList<>();
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recycler_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_list.setHasFixedSize(false);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setLayoutManager(new GridLayoutManager(mContext, 2));
        recycler_list.addItemDecoration(itemDecoration);

        loadJSONFromAssetCategoryList();

    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetCategoryList() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/property/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                ItemCategoryList itemPublicSellerList = new ItemCategoryList();
                                itemPublicSellerList.setCategoryListId(jo_inside.getString("itemId"));
                                itemPublicSellerList.setCategoryListName(jo_inside.getString("name"));
                                itemPublicSellerList.setCategoryListImage(jo_inside.getString("principalImage"));
                                itemPublicSellerList.setCategoryListDescription(jo_inside.getString("description"));
                                itemPublicSellerList.setCategoryListPrice(jo_inside.getString("price"));
                                itemPublicSellerList.setCategoryListType(jo_inside.getString("type"));
                                itemPublicSellerList.setCategoryListDuildedArea(jo_inside.getString("duildedArea"));
                                itemPublicSellerList.setCategoryListMoney(jo_inside.getString("money"));

                                array_list.add(itemPublicSellerList);

                            }
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(mContext, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            //}
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

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(mContext).addToRequestQueue(postRequest);
        return array_list;
    }

    public void setAdapterHomeCategoryList() {
        adapter_list = new ListGridAdapter(mContext, array_list);
        //txtNoOfItem.setText(adapter_list.getItemCount()+"");
        recycler_list.setAdapter(adapter_list);
    }

}
