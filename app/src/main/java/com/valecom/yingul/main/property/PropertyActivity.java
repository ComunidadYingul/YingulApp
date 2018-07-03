package com.valecom.yingul.main.property;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.Util.RecyclerItemClickListener;
import com.valecom.yingul.adapter.AllCategoryHomeAdapter;
import com.valecom.yingul.adapter.CityAdapter;
import com.valecom.yingul.adapter.CountryAdapter;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.adapter.PropertyCategoryHomeAdapter;
import com.valecom.yingul.adapter.ProvinceAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.PrincipalFragment;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.filter.FilterActivity;
import com.valecom.yingul.main.motorized.MotorizedActivity;
import com.valecom.yingul.main.motorized.MotorizedFilteredActivity;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyActivity extends AppCompatActivity {

    private MaterialDialog progressDialog;
    private Context mContext;

    RecyclerView recycler_list;
    ListGridAdapter adapter_list;
    ArrayList<Yng_Item> array_list;

    RecyclerView recycler_home_all_category;
    PropertyCategoryHomeAdapter adapter_all_category;
    ArrayList<Yng_Category> array_all_category;

    private Yng_Category category;
    private String condition;
    private Yng_Province province;
    private Yng_Country country;
    private Yng_City city;
    private Yng_Ubication ubication;
    private LinearLayout saleLayout,rentalLayout,rentalTemporalLayout,filterLayout,layoutCategory,card_view_all_category,layoutSetCity,layoutSetUbication,layoutClearUbication,layoutCity;
    private TextView textCategory,textPathUbication,textUbicationName;
    private RelativeLayout content_header;
    private Button buttonSendParams;

    private ListView list;
    private ArrayList<Yng_Country> array_list5;
    private CountryAdapter adapter;
    private MaterialDialog setting_address_edit_dialog;
    private ProvinceAdapter adapter1;
    private ArrayList<Yng_Province> array_list1;
    private CityAdapter adapter2;
    private ArrayList<Yng_City> array_list2;

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

        category=null;
        condition="";

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
        recycler_list.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        recycler_list.addItemDecoration(itemDecoration);

        array_all_category = new ArrayList<>();
        recycler_home_all_category = (RecyclerView) findViewById(R.id.rv_home_all_category);
        recycler_home_all_category.setHasFixedSize(false);
        recycler_home_all_category.setNestedScrollingEnabled(false);
        recycler_home_all_category.setLayoutManager(new LinearLayoutManager(PropertyActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recycler_home_all_category.addItemDecoration(itemDecoration);

        content_header = (RelativeLayout) findViewById(R.id.content_header);
        saleLayout = (LinearLayout) findViewById(R.id.saleLayout);
        rentalLayout = (LinearLayout) findViewById(R.id.rentalLayout);
        rentalTemporalLayout = (LinearLayout) findViewById(R.id.rentalTemporalLayout);
        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        layoutCategory = (LinearLayout) findViewById(R.id.layoutCategory);
        textCategory = (TextView) findViewById(R.id.textCategory);
        card_view_all_category = (LinearLayout) findViewById(R.id.card_view_all_category);
        layoutSetCity = (LinearLayout) findViewById(R.id.layoutSetCity);
        layoutCity = (LinearLayout) findViewById(R.id.layoutCity);
        textUbicationName = (TextView) findViewById(R.id.textUbicationName);
        buttonSendParams = (Button) findViewById(R.id.buttonSendParams);

        layoutCategory.setVisibility(View.GONE);
        filterLayout.setVisibility(View.GONE);
        layoutCity.setVisibility(View.GONE);

        country = new Yng_Country();
        province = new Yng_Province();
        city = new Yng_City();
        ubication = null;
        array_list5 = new ArrayList<Yng_Country>();
        adapter = new CountryAdapter(this, array_list5);
        array_list1 = new ArrayList<Yng_Province>();
        adapter1 = new ProvinceAdapter(this, array_list1);
        array_list2 = new ArrayList<Yng_City>();
        adapter2 = new CityAdapter(this, array_list2);

        final float scale = getResources().getDisplayMetrics().density;

        recycler_home_all_category.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Yng_Category categoryP = adapter_all_category.getItem(position);
                category=new Yng_Category();
                category=categoryP;
                layoutCategory.setVisibility(View.VISIBLE);
                textCategory.setText(categoryP.getName());
                card_view_all_category.setVisibility(View.GONE);
            }
        }));

        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category=null;
                content_header.getLayoutParams().height = (int) (200 * scale);
                card_view_all_category.setVisibility(View.VISIBLE);
                condition = "Venta";
                loadJSONFromAssetHomeCoupon(condition);
                filterLayout.setVisibility(View.VISIBLE);
                layoutCategory.setVisibility(View.GONE);
            }
        });
        rentalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category=null;
                content_header.getLayoutParams().height = (int) (200 * scale);
                card_view_all_category.setVisibility(View.VISIBLE);
                condition = "Alquiler";
                loadJSONFromAssetHomeCoupon(condition);
                filterLayout.setVisibility(View.VISIBLE);
                layoutCategory.setVisibility(View.GONE);
            }
        });
        rentalTemporalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category=null;
                content_header.getLayoutParams().height = (int) (200 * scale);
                card_view_all_category.setVisibility(View.VISIBLE);
                condition = "temporario";
                loadJSONFromAssetHomeCoupon(condition);
                filterLayout.setVisibility(View.VISIBLE);
                layoutCategory.setVisibility(View.GONE);
            }
        });

        layoutSetCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RunGetCountryService();
            }
        });

        buttonSendParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyActivity.this, PropertyFilteredActivity.class);
                intent.putExtra("condition", condition);
                if(category==null){
                }else{
                    intent.putExtra("category", category);
                }
                if(ubication==null){
                    intent.putExtra("typeUbication", "All");
                    intent.putExtra("idTypeUbication", 0);
                }else {
                    if (ubication.getYng_City() == null) {
                        if (ubication.getYng_Province() == null) {
                            if (ubication.getYng_Country() == null) {
                                intent.putExtra("typeUbication", "All");
                                intent.putExtra("idTypeUbication", 0);
                            } else {
                                intent.putExtra("typeUbication", "country");
                                intent.putExtra("idTypeUbication", ubication.getYng_Country().getCountryId());
                            }
                        } else {
                            intent.putExtra("typeUbication", "province");
                            intent.putExtra("idTypeUbication", ubication.getYng_Province().getProvinceId());
                        }
                    } else {
                        intent.putExtra("typeUbication", "city");
                        intent.putExtra("idTypeUbication", ubication.getYng_City().getCityId());
                    }
                }
                startActivity(intent);
            }
        });

        loadJSONFromAssetCategoryList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList<Yng_Item> loadJSONFromAssetCategoryList() {

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
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setType(jo_inside.getString("type"));
                                item.setDuildedArea(Integer.valueOf(jo_inside.getString("duildedArea")));
                                item.setMoney(jo_inside.getString("money"));

                                array_list.add(item);

                            }
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) { Probablemente habra que usar su metodo del dani
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

    public ArrayList<Yng_Category> loadJSONFromAssetHomeCoupon(String subcategory) {
        progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/fatherForItemTypeAndNamecategory/Property/"+subcategory,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {


                            JSONArray items = response;
                            if(items.length()==0){

                            }

                            array_all_category.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);

                                Yng_Category item = new Yng_Category();

                                item.setCategoryId(obj.getLong("categoryId"));
                                item.setName(obj.getString("name"));


                                array_all_category.add(item);
                            }
                            setAdapterHomeCoupon();
                            array_all_category.notify();
                            //return array_all_category;

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                                Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PropertyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PropertyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( PropertyActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(PropertyActivity.this).addToRequestQueue(postRequest);
        return array_all_category;

    }

    public void setAdapterHomeCoupon() {

        adapter_all_category = new PropertyCategoryHomeAdapter(PropertyActivity.this, array_all_category);
        recycler_home_all_category.setAdapter(adapter_all_category);

        //loadJSONFromAssetHomeTrending();
    }

    public void RunGetCountryService(){
        progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "ubication/country/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
                            array_list5.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);
                                Yng_Country item = new Yng_Country();
                                item.setCountryId(obj.getInt("countryId"));
                                item.setName(obj.getString("name"));
                                array_list5.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            setCountry();
                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //0if (isAdded()) {
                            Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PropertyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PropertyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( PropertyActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(PropertyActivity.this).addToRequestQueue(postRequest);

    }

    public void setCountry(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_country_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();
                        layoutSetUbication = (LinearLayout) view.findViewById(R.id.layoutSetUbication);
                        layoutClearUbication = (LinearLayout) view.findViewById(R.id.layoutClearUbication);
                        textPathUbication = (TextView) view.findViewById(R.id.textPathUbication);

                        layoutSetUbication.setVisibility(View.VISIBLE);
                        layoutClearUbication.setVisibility(View.GONE);

                        layoutSetUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                ubication = null;
                                layoutCity.setVisibility(View.GONE);
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });

                        textPathUbication.setText("Todas");

                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_Country getCountry = adapter.getItem(position);
                                country = getCountry;
                                RunGetProvinceService();
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
    public void RunGetProvinceService(){
        progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "ubication/province/"+country.getCountryId(),
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
                                Yng_Province item = new Yng_Province();
                                item.setProvinceId(obj.getInt("provinceId"));
                                item.setName(obj.getString("name"));
                                array_list1.add(item);
                            }

                            adapter1.notifyDataSetChanged();
                            setProvince();
                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PropertyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PropertyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( PropertyActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(PropertyActivity.this).addToRequestQueue(postRequest);

    }
    public void setProvince(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_country_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();
                        layoutSetUbication = (LinearLayout) view.findViewById(R.id.layoutSetUbication);
                        layoutClearUbication = (LinearLayout) view.findViewById(R.id.layoutClearUbication);
                        textPathUbication = (TextView) view.findViewById(R.id.textPathUbication);

                        layoutSetUbication.setVisibility(View.VISIBLE);
                        layoutClearUbication.setVisibility(View.VISIBLE);

                        layoutClearUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                RunGetCountryService();
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });
                        layoutSetUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                ubication = new Yng_Ubication();
                                ubication.setYng_Country(country);
                                layoutCity.setVisibility(View.VISIBLE);
                                textUbicationName.setText(country.getName());
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });

                        textPathUbication.setText("Todas > "+country.getName());

                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(adapter1);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_Province getProvince = adapter1.getItem(position);
                                province=getProvince;
                                RunGetCityService();
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
    public void RunGetCityService(){
        progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "ubication/city/"+province.getProvinceId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
                            array_list2.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);
                                Yng_City item = new Yng_City();
                                item.setCityId(obj.getInt("cityId"));
                                item.setName(obj.getString("name"));
                                array_list2.add(item);
                            }
                            adapter2.notifyDataSetChanged();
                            setCity();
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PropertyActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(PropertyActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PropertyActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( PropertyActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(PropertyActivity.this).addToRequestQueue(postRequest);

    }
    public void setCity(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_country_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        layoutSetUbication = (LinearLayout) view.findViewById(R.id.layoutSetUbication);
                        layoutClearUbication = (LinearLayout) view.findViewById(R.id.layoutClearUbication);
                        textPathUbication = (TextView) view.findViewById(R.id.textPathUbication);
                        layoutSetUbication.setVisibility(View.VISIBLE);
                        layoutClearUbication.setVisibility(View.VISIBLE);

                        layoutClearUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                RunGetCountryService();
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });
                        layoutSetUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                ubication = new Yng_Ubication();
                                ubication.setYng_Province(province);
                                layoutCity.setVisibility(View.VISIBLE);
                                textUbicationName.setText(province.getName());
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });

                        if(province.getName().length()>7){
                            textPathUbication.setText("Todas > "+country.getName()+" > "+province.getName().substring(0,7)+"...");
                        }else{
                            textPathUbication.setText("Todas > "+country.getName()+" > "+province.getName()+"...");
                        }

                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(adapter2);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_City getCity = adapter2.getItem(position);
                                city=getCity;
                                ubication = new Yng_Ubication();
                                ubication.setYng_City(city);
                                layoutCity.setVisibility(View.VISIBLE);
                                textUbicationName.setText(city.getName());
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

}
