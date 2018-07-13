package com.valecom.yingul.main.motorized;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.CategoryAdapter;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_FindMotorized;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MotorizedActivity extends AppCompatActivity {

    public static final String TAG = "MotorizedActivity";
    private MaterialDialog progressDialog;
    private Context mContext;

    RecyclerView recycler_list;
    ListGridAdapter adapter_list;
    ArrayList<Yng_Item> array_list;
    private JSONObject api_parameter;

    private LinearLayout filterLayout,carsLayout,motorcicleLayout,truckLayout,allCategoryLayout,layoutSetMinYear,layoutSetMaxYear,layoutCategory,layoutSetCategory;
    private ImageView categoryImage;
    private TextView categoryNameText,textPrecio,textYearInit,textYearEnd,textCategoryName;
    private Button buttonSendParams;

    private MaterialDialog setting_address_edit_dialog;
    private CategoryAdapter adapter;
    private ArrayList<Yng_Category> array_list1;
    ListView list;
    private Double maxPriceItem;
    private Double minPriceItem;
    private RangeSeekBar seekBar;
    private RelativeLayout content_header;
    private Yng_FindMotorized findMotorized;
    private ListView listYear;
    private ArrayAdapter year_adapter;
    private ArrayList year_list;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Yng_Category> array_list_category;

    private Long minPrice;
    private Long maxPrice;
    private Long maxYear;
    private Long minYear;
    private Yng_Category category;
    private String pathCategory="";
    /*********/
    int col=2;
    String modo="grid";
    DisplayMetrics metrics = new DisplayMetrics();

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
        findMotorized = new Yng_FindMotorized();
        maxPriceItem = Double.valueOf(0);
        minPriceItem = Double.valueOf(99999999);
        final float scale = getResources().getDisplayMetrics().density;
        array_list_category = new ArrayList<Yng_Category>();
        categoryAdapter = new CategoryAdapter(this, array_list_category);
        minPrice= Long.valueOf(0);
        maxPrice= Long.valueOf(0);
        maxYear= Long.valueOf(0);
        minYear= Long.valueOf(0);
        category=null;

        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        carsLayout = (LinearLayout) findViewById(R.id.carsLayout);
        motorcicleLayout = (LinearLayout) findViewById(R.id.motorcicleLayout);
        truckLayout = (LinearLayout) findViewById(R.id.truckLayout);
        categoryImage = (ImageView) findViewById(R.id.categoryImage);
        categoryNameText = (TextView) findViewById(R.id.categoryNameText);
        allCategoryLayout = (LinearLayout) findViewById(R.id.allCategoryLayout);
        content_header = (RelativeLayout) findViewById(R.id.content_header);
        textPrecio = (TextView) findViewById(R.id.textPrecio);
        layoutSetMinYear = (LinearLayout) findViewById(R.id.layoutSetMinYear);
        textYearInit = (TextView) findViewById(R.id.textYearInit);
        layoutSetMaxYear = (LinearLayout) findViewById(R.id.layoutSetMaxYear);
        textYearEnd = (TextView) findViewById(R.id.textYearEnd);
        layoutCategory = (LinearLayout) findViewById(R.id.layoutCategory);
        layoutSetCategory = (LinearLayout) findViewById(R.id.layoutSetCategory);
        textCategoryName = (TextView) findViewById(R.id.textCategoryName);
        buttonSendParams = (Button) findViewById(R.id.buttonSendParams);

        buttonSendParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MotorizedActivity.this, MotorizedFilteredActivity.class);
                intent.putExtra("minPrice", minPrice);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("maxYear", maxYear);
                intent.putExtra("minYear", minYear);
                if(category==null){
                    intent.putExtra("category", findMotorized.getCategoryId());
                    intent.putExtra("categoryId",findMotorized.getCategoryId()+"");
                }else{
                    intent.putExtra("category", category.getCategoryId());
                    intent.putExtra("categoryId",category.getCategoryId()+"");
                }
                startActivity(intent);
            }
        });

        filterLayout.setVisibility(View.GONE);
        carsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_header.getLayoutParams().height = (int) (200 * scale);
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Autos y\nCamionetas");
                loadJSONFromAssetCategoryList1(Long.valueOf(2001));
                RunFindMotorizedService(Long.valueOf(2001));
            }
        });
        motorcicleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_header.getLayoutParams().height = (int) (200 * scale);;
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Motos");
                loadJSONFromAssetCategoryList1(Long.valueOf(2007));
                RunFindMotorizedService(Long.valueOf(2007));
            }
        });
        truckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_header.getLayoutParams().height = (int) (200 * scale);;
                filterLayout.setVisibility(View.VISIBLE);
                motorcicleLayout.setVisibility(View.GONE);
                truckLayout.setVisibility(View.GONE);
                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                categoryNameText.setText("Camiones");
                loadJSONFromAssetCategoryList1(Long.valueOf(2002));
                RunFindMotorizedService(Long.valueOf(2002));
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
                                content_header.getLayoutParams().height = (int) (200 * scale);;
                                Yng_Category item = adapter.getItem(position);
                                filterLayout.setVisibility(View.VISIBLE);
                                motorcicleLayout.setVisibility(View.GONE);
                                truckLayout.setVisibility(View.GONE);
                                Picasso.with(MotorizedActivity.this).load("file:///android_asset/image/car.png").into(categoryImage);
                                categoryNameText.setText(item.getName());
                                loadJSONFromAssetCategoryList1(item.getCategoryId());
                                RunFindMotorizedService(item.getCategoryId());
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
        layoutSetMinYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMinYear();
            }
        });
        layoutSetMaxYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaxYear();
            }
        });
        layoutCategory.setVisibility(View.GONE);
        layoutSetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathCategory="";
                RunGetSubCategoryService(findMotorized.getCategoryId());
            }
        });

        seekBar = (RangeSeekBar) findViewById(R.id.rangeSeekbar);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                minPrice= Long.valueOf(minValue);
                maxPrice= Long.valueOf(maxValue);
                textPrecio.setText(minValue + "$-" + maxValue+"$");
                if(Double.valueOf(minValue).equals(minPriceItem)&&Double.valueOf(maxValue).equals(maxPriceItem)){
                    Log.e("min,max,maxitem","entro");
                    textPrecio.setText("¿Qué rango de precio?");
                }
            }
        });
        seekBar.setNotifyWhileDragging(true);

        array_list = new ArrayList<>();
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
        recycler_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_list.setHasFixedSize(false);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setLayoutManager(new StaggeredGridLayoutManager(col,1));
        recycler_list.addItemDecoration(itemDecoration);

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
                                    Log.e("maxprice",item.getPrice()+"");
                                }
                                if(minPriceItem>item.getPrice()){
                                    minPriceItem=item.getPrice();
                                    Log.e("minprice",item.getPrice()+"");
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

    public void RunFindMotorizedService(Long categoryId)
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "item/findMotorized/"+categoryId, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Persona por su nombre " , response.toString());
                            Gson gson = new Gson();
                            findMotorized = gson.fromJson(String.valueOf(response), Yng_FindMotorized.class);
                            minPriceItem = Double.valueOf(findMotorized.getMinPrice());
                            maxPriceItem = Double.valueOf(findMotorized.getMaxPrice());
                            seekBar.setRangeValues(minPriceItem.intValue(), maxPriceItem);

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(MotorizedActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MotorizedActivity.this, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(MotorizedActivity.this, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(MotorizedActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
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

    public void setMinYear(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_year_layout, true)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        TextView title=(TextView) view.findViewById(R.id.title);
                        title.setText("Desde");

                        year_list = new ArrayList();
                        year_list.add("Todos");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                        Calendar calendar = Calendar.getInstance();
                        for (Long i = Long.valueOf(Integer.parseInt(simpleDateFormat.format(calendar.getTime())))+1;i>=findMotorized.getMinYear();i--){
                            year_list.add(i);
                        }
                        year_adapter = new ArrayAdapter<String>(MotorizedActivity.this,android.R.layout.simple_list_item_1, year_list);

                        listYear = (ListView) view.findViewById(R.id.listYear);
                        // Assigning the adapter to ListView
                        listYear.setAdapter(year_adapter);

                        listYear.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                String year = String.valueOf(year_adapter.getItem(position));
                                if(year.equals("Todos")){
                                    layoutSetMinYear.setBackgroundResource(R.drawable.oval_margin_white);
                                    textYearInit.setText("Desde");
                                    textYearInit.setTextColor(getResources().getColor(R.color.white));
                                    minYear= Long.valueOf(0);
                                }else{
                                    layoutSetMinYear.setBackgroundResource(R.drawable.oval_background_white);
                                    textYearInit.setText(year);
                                    textYearInit.setTextColor(getResources().getColor(R.color.yngOrange));
                                    minYear= Long.valueOf(year);
                                }
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
    public void setMaxYear(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_year_layout, true)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        TextView title=(TextView) view.findViewById(R.id.title);
                        title.setText("Hasta");

                        year_list = new ArrayList();
                        year_list.add("Todos");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                        Calendar calendar = Calendar.getInstance();
                        for (Long i = Long.valueOf(Integer.parseInt(simpleDateFormat.format(calendar.getTime())))+1;i>=findMotorized.getMinYear();i--){
                            year_list.add(i);
                        }
                        year_adapter = new ArrayAdapter<String>(MotorizedActivity.this,android.R.layout.simple_list_item_1, year_list);

                        listYear = (ListView) view.findViewById(R.id.listYear);
                        // Assigning the adapter to ListView
                        listYear.setAdapter(year_adapter);

                        listYear.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                String year = String.valueOf(year_adapter.getItem(position));
                                if(year.equals("Todos")){
                                    layoutSetMaxYear.setBackgroundResource(R.drawable.oval_margin_white);
                                    textYearEnd.setText("Hasta");
                                    textYearEnd.setTextColor(getResources().getColor(R.color.white));
                                    maxYear= Long.valueOf(0);
                                }else{
                                    layoutSetMaxYear.setBackgroundResource(R.drawable.oval_background_white);
                                    textYearEnd.setText(year);
                                    textYearEnd.setTextColor(getResources().getColor(R.color.yngOrange));
                                    maxYear= Long.valueOf(year);
                                }
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
    public void RunGetSubCategoryService(Long fatherId){
        progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "category/father/"+fatherId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            JSONArray items = response;
                            Log.e("Eddy",items.toString());
                            if(response.toString().equals("[]")){
                                layoutCategory.setVisibility(View.VISIBLE);
                                textCategoryName.setText(category.getName());
                            }else{
                                array_list_category.clear();
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject obj = items.getJSONObject(i);
                                    Yng_Category item = new Yng_Category();
                                    item.setCategoryId(obj.getLong("categoryId"));
                                    item.setName(obj.getString("name"));
                                    array_list_category.add(item);
                                }
                                categoryAdapter.notifyDataSetChanged();
                                setSubCategory();
                            }
                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //0if (isAdded()) {
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
                //params.put("X-API-KEY", Network.API_KEY);
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

    public void setSubCategory(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_country_layout, false)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();
                        LinearLayout layoutSetUbication = (LinearLayout) view.findViewById(R.id.layoutSetUbication);
                        LinearLayout layoutClearUbication = (LinearLayout) view.findViewById(R.id.layoutClearUbication);
                        TextView textPathUbication = (TextView) view.findViewById(R.id.textPathUbication);
                        TextView textClearUbication = (TextView) view.findViewById(R.id.textClearUbication);

                        layoutSetUbication.setVisibility(View.VISIBLE);
                        textClearUbication.setText("Limpiar");

                        if(category==null||pathCategory.equals("")){
                            pathCategory="Marca/Modelo";
                            layoutClearUbication.setVisibility(View.GONE);
                        }else{
                            if(pathCategory.equals("Marca/Modelo")){
                                pathCategory=category.getName();
                            }else{
                                pathCategory=pathCategory+">"+category.getName();
                            }
                            layoutClearUbication.setVisibility(View.VISIBLE);
                        }
                        textPathUbication.setText(pathCategory);

                        layoutSetUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if(pathCategory.equals("Marca/Modelo")){
                                    category=null;
                                    layoutCategory.setVisibility(View.GONE);
                                }else{
                                    layoutCategory.setVisibility(View.VISIBLE);
                                    textCategoryName.setText(category.getName());
                                }
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });
                        layoutClearUbication.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                pathCategory="";
                                RunGetSubCategoryService(findMotorized.getCategoryId());
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });


                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(categoryAdapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_Category getCategory = categoryAdapter.getItem(position);
                                category= new Yng_Category();
                                category= getCategory;
                                RunGetSubCategoryService(category.getCategoryId());
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
    /*************************/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        recyclerResponsive();

        if(modo.equals("grid")) {
            recycler_list.setLayoutManager(new StaggeredGridLayoutManager(col, 1));
            adapter_list = new ListGridAdapter(getApplicationContext(), array_list);
            recycler_list.setAdapter(adapter_list);
        }
    }

    public void recyclerResponsive(){
        Log.e("oriencation:----",""+getApplicationContext().getResources().getConfiguration().orientation);
        Log.e("dpi:----",""+metrics.xdpi);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (metrics.xdpi < 160) {col = 4;}
            else if (metrics.xdpi < 220) {col = 4;}
            else if (metrics.xdpi < 320) {col = 3;}
            else {col = 2;}
        }else if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(metrics.xdpi < 160){col=2;}
            else if(metrics.xdpi < 220){col=2;}
            else if(metrics.xdpi < 320){col=2;}
            else {col=2;}
        }
    }
}
