package com.valecom.yingul.main.motorized;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.CategoryAdapter;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.categories.ItemsByCategoryActivity;
import com.valecom.yingul.main.property.PropertyActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MotorizedActivity extends AppCompatActivity {

    private MaterialDialog progressDialog;
    private Context mContext;

    RecyclerView recycler_list;
    ListGridAdapter adapter_list;
    ArrayList<Yng_Item> array_list;

    private LinearLayout filterLayout,carsLayout,motorcicleLayout,truckLayout,allCategoryLayout;
    private ImageView categoryImage;
    private TextView categoryNameText;

    private MaterialDialog setting_address_edit_dialog;
    private CategoryAdapter adapter;
    private ArrayList<Yng_Category> array_list1;
    ListView list;
    private Double maxPriceItem;
    private Double minPriceItem;
    private RangeSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorized);

        mContext = MotorizedActivity.this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vehículos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_list1 = new ArrayList<Yng_Category>();
        adapter = new CategoryAdapter(this, array_list1);
        maxPriceItem = Double.valueOf(0);
        minPriceItem = Double.valueOf(99999999);

        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        carsLayout = (LinearLayout) findViewById(R.id.carsLayout);
        motorcicleLayout = (LinearLayout) findViewById(R.id.motorcicleLayout);
        truckLayout = (LinearLayout) findViewById(R.id.truckLayout);
        categoryImage = (ImageView) findViewById(R.id.categoryImage);
        categoryNameText = (TextView) findViewById(R.id.categoryNameText);
        allCategoryLayout = (LinearLayout) findViewById(R.id.allCategoryLayout);

        filterLayout.setVisibility(View.GONE);
        carsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Autos y\nCamionetas");
                loadJSONFromAssetCategoryList1(Long.valueOf(2001));
            }
        });
        motorcicleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Motos");
                loadJSONFromAssetCategoryList1(Long.valueOf(2007));
            }
        });
        truckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Camiones");
                loadJSONFromAssetCategoryList1(Long.valueOf(2002));
            }
        });
        allCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(MotorizedActivity.this)
                .customView(R.layout.filter_set_category_layout, false)
                .cancelable(true)
                .showListener(new android.content.DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        list = (ListView) view.findViewById(R.id.list);
                        // Assigning the adapter to ListView
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_Category item = adapter.getItem(position);
                                filterLayout.setVisibility(View.VISIBLE);
                                motorcicleLayout.setVisibility(View.GONE);
                                truckLayout.setVisibility(View.GONE);
                                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                                categoryNameText.setText(item.getName());
                                loadJSONFromAssetCategoryList1(item.getCategoryId());
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });
                        RunGetCategoryService();
                    }
                })
                .callback(new MaterialDialog.ButtonCallback()
                {

                })
                .show();
            }
        });

        seekBar = (RangeSeekBar) findViewById(R.id.rangeSeekbar);
        seekBar.setRangeValues(0, 100);

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {

            }
        });
        seekBar.setNotifyWhileDragging(true);

        array_list = new ArrayList<>();
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recycler_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_list.setHasFixedSize(false);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setLayoutManager(new GridLayoutManager(mContext, 2));
        recycler_list.addItemDecoration(itemDecoration);

        loadJSONFromAssetCategoryList();
    }

    public ArrayList<Yng_Item> loadJSONFromAssetCategoryList() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/motorized/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item itemPublicSellerList = new Yng_Item();
                                itemPublicSellerList.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                itemPublicSellerList.setName(jo_inside.getString("name"));
                                itemPublicSellerList.setPrincipalImage(jo_inside.getString("principalImage"));
                                itemPublicSellerList.setDescription(jo_inside.getString("description"));
                                itemPublicSellerList.setPrice(Double.valueOf(jo_inside.getString("price")));
                                itemPublicSellerList.setType(jo_inside.getString("type"));
                                itemPublicSellerList.setDuildedArea(Integer.valueOf(jo_inside.getString("duildedArea")));
                                itemPublicSellerList.setMoney(jo_inside.getString("money"));

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

    public ArrayList<Yng_Item> loadJSONFromAssetCategoryList1(Long categoryId) {

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/itemsByCategory/"+categoryId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            array_list.clear();
                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setType(jo_inside.getString("type"));
                                item.setMoney(jo_inside.getString("money"));
                                item.setCondition(jo_inside.getString("condition"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setOver(jo_inside.getBoolean("over"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                Gson gson = new Gson();
                                Yng_Ubication yngUbication = gson.fromJson(jo_inside.getString("yng_Ubication"), Yng_Ubication.class);
                                item.setYng_Ubication(yngUbication);

                                //Log.e("envia",jo_inside.getString("yng_Ubication"));
                                /***********filtro**************/
                                if(maxPriceItem<item.getPrice()){
                                    maxPriceItem=item.getPrice();
                                }
                                if(minPriceItem>item.getPrice()){
                                    minPriceItem=item.getPrice();
                                }
                                /********************************/

                                array_list.add(item);

                            }
                            /**************filtro*************
                            array_cat_list_backup=array_cat_list;
                            /***********************************/
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(MotorizedActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MotorizedActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(MotorizedActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MotorizedActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(MotorizedActivity.this).addToRequestQueue(postRequest);
        return array_list;
    }

    public void setAdapterHomeCategoryList() {
        adapter_list = new ListGridAdapter(mContext, array_list);
        //txtNoOfItem.setText(adapter_list.getItemCount()+"");
        recycler_list.setAdapter(adapter_list);
    }
    public void RunGetCategoryService()
    {
        progressDialog.show();


        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/Motorized/0",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {


                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
                            array_list1.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);

                                Yng_Category item = new Yng_Category();

                                item.setCategoryId(obj.getLong("categoryId"));
                                item.setName(obj.getString("name"));


                                array_list1.add(item);
                            }

                            adapter.notifyDataSetChanged();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                                Toast.makeText(MotorizedActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MotorizedActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(MotorizedActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MotorizedActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( MotorizedActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(MotorizedActivity.this).addToRequestQueue(postRequest);

    }

}
