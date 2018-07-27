package com.valecom.yingul.main.filter;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.valecom.yingul.Item.ItemColorSize;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.CustomScrollListener;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.adapter.ListRowAdapter;
import com.valecom.yingul.adapter.SelectColorAdapter;
import com.valecom.yingul.adapter.SelectSizeAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.MainFindCategoryFragment;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.categories.ItemsByCategoryActivity;
import com.valecom.yingul.main.index.InicioFragment;
import com.valecom.yingul.model.FilterParam;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recycler_cat_list;
    ListGridAdapter adapter_cat_list;
    ArrayList<Yng_Item> array_cat_list;//ArrayList<Yng_Item> array_cat_list;
    ListRowAdapter adapter_cat_list_listview;
    TextView txtNoOfItem;
    ImageView ImgList,ImgGrid,ImgFilter;
    Dialog dialog;
    //CrystalRangeSeekbar appCompatSeekBar;
    Button buttonPriceMin,buttonPriceMax, buttonApply;
    int progressChangedValue = 100;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    RecyclerView recyclerView_color, recyclerView_size;
    ImageView lay_filter_click;
    private MaterialDialog progressDialog;

    String categoryId,itemName="a";
    private MaterialDialog setting_address_edit_dialog;
    /******filtros*****/
    static final int ITEM_PICKER_TAG = 1;
    ArrayList<Yng_Item> array_cat_list_backup;
    private FilterParam filterParams;
    private Double maxPriceItem;
    private Double minPriceItem;
    /**** numero total de coincidencias ****/
    int numCoincidencia = 0;
    int paso = 30;
    int start = 0;
    int end = paso;
    /*********/
    int col=2;
    String modo="grid";
    DisplayMetrics metrics = new DisplayMetrics();

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = this.getIntent().getExtras();
        try{
            itemName = bundle.getString("itemName");
        }catch (Exception e){
            itemName="";
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(itemName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerResponsive();

        Log.e("CategoryId recuperado:",""+categoryId);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        /*******filtros******/
        filterParams = new FilterParam();
        filterParams.setFreeShipping(false);
        filterParams.setCondition("none");
        filterParams.setDiscount("none");
        filterParams.setUbication(null);
        filterParams.setMinPrice(null);
        filterParams.setMaxPrice(null);
        maxPriceItem = Double.valueOf(0);
        minPriceItem = Double.valueOf(9999999);
        /*************/

        array_cat_list = new ArrayList<>();
        array_cat_list_backup = new ArrayList<>();
        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.loadAd(new AdRequest.Builder().build());

        recycler_cat_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new StaggeredGridLayoutManager(col,1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        txtNoOfItem=(TextView)findViewById(R.id.text_cat_list_item);
        ImgList=(ImageView)findViewById(R.id.image_list);
        ImgGrid=(ImageView)findViewById(R.id.image_grid);
        ImgFilter=(ImageView)findViewById(R.id.image_filter);
        lay_filter_click=(ImageView)findViewById(R.id.lay_filter_click);

        ImgGrid.setImageResource(R.drawable.ic_grid_hover);
        ImgList.setImageResource(R.drawable.ic_list);

        recycler_cat_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Log.e("Position:---","final");
                    if(numCoincidencia > array_cat_list.size()) {
                        start += paso;
                        end += paso;
                        updateListArray();
                    }
                }
            }
        });

        ImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo="grid";
                recycler_cat_list.setLayoutManager(new StaggeredGridLayoutManager(col,1));
                adapter_cat_list = new ListGridAdapter(getApplicationContext(), array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list);
                ImgGrid.setImageResource(R.drawable.ic_grid_hover);
                ImgList.setImageResource(R.drawable.ic_list);
            }
        });

        ImgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo="row";
                recycler_cat_list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                adapter_cat_list_listview = new ListRowAdapter(getApplicationContext(), array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list_listview);
                ImgList.setImageResource(R.drawable.ic_listview_hover);
                ImgGrid.setImageResource(R.drawable.ic_grid);
            }
        });
        /*******filtro********/
        lay_filter_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(array_cat_list.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No hay productos para filtrar", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                    Gson json = new Gson();
                    intent.putExtra("itemList", json.toJson(array_cat_list_backup).toString());
                    intent.putExtra("filterParams", filterParams);
                    intent.putExtra("maxPriceItem", maxPriceItem);
                    intent.putExtra("minPriceItem", minPriceItem);
                    startActivityForResult(intent, ITEM_PICKER_TAG);
                }
            }
        });


        /*****************/
        loadJSONFromAssetCategoryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        /*searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    searchView.setQuery("", false);
                    InicioFragment fragment = new InicioFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                Log.e("onclick buscador",searchView.getQuery().toString());
                itemName = searchView.getQuery().toString().replace(" ","");
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                intent.putExtra("itemName",itemName);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                Log.e("buscador",searchView.getQuery().toString());
                Bundle bundle = new Bundle();
                bundle.putString("nameCategory",searchView.getQuery().toString());
                /*MainFindCategoryFragment fragment = new MainFindCategoryFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();*/
                return false;
            }
        });
        return true;
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

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/listItemByName/"+itemName.replace(" ","")+"/0/30",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("tam dev:----",m_jArry.length()+"");
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
                                array_cat_list.add(item);
                            }
                            Log.e("resultado final",array_cat_list.toString());
                            /**************filtro*************/
                            array_cat_list_backup=array_cat_list;
                            /***********************************/
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(getApplicationContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
        return array_cat_list;
    }

    public ArrayList<Yng_Item> updateListArray() {

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/listItemByName/"+itemName.replace(" ","")+"/"+start+"/"+end,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("tam dev:----",m_jArry.length()+"");
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
                                array_cat_list.add(item);
                            }
                            Log.e("resultado final",array_cat_list.toString());
                            /**************filtro*************/
                            array_cat_list_backup=array_cat_list;
                            /***********************************/
                            if(modo.equals("grid")) {
                                adapter_cat_list.notifyDataSetChanged();
                            }else{
                                adapter_cat_list_listview.notifyDataSetChanged();
                            }

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(getApplicationContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
        return array_cat_list;
    }

    public void setAdapterHomeCategoryList() {
        recycler_cat_list.setLayoutManager(new StaggeredGridLayoutManager(col,1));
        adapter_cat_list = new ListGridAdapter(getApplicationContext(), array_cat_list);
        txtNoOfItem.setText("");
        getCountSearch(Network.API_URL+"/item/getQuantityItemByName/"+itemName);
        recycler_cat_list.setAdapter(adapter_cat_list);
    }

    /**************filtros**************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == ITEM_PICKER_TAG) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if(((String)data.getSerializableExtra("itemList")).equals("clean")){
                    array_cat_list=array_cat_list_backup;
                }else{
                    try {
                        array_cat_list=stringToArrayItemCategoryList((String)data.getSerializableExtra("itemList"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                filterParams = (FilterParam)data.getSerializableExtra("filterParams");
                Gson json = new Gson();
                Log.e("itemList response",json.toJson(array_cat_list).toString());
                Log.e("filer response",json.toJson(filterParams).toString());
                setAdapterHomeCategoryList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ArrayList<Yng_Item> stringToArrayItemCategoryList(String itemList) throws JSONException {
        ArrayList<Yng_Item> array_cat_list_new= new ArrayList<>();

        JSONArray m_jArry = new JSONArray(itemList);

        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            Yng_Item item = new Yng_Item();
            item.setItemId(jo_inside.getLong("itemId"));
            item.setName(jo_inside.getString("name"));
            item.setPrincipalImage(jo_inside.getString("principalImage"));
            item.setDescription(jo_inside.getString("description"));
            item.setPrice(jo_inside.getDouble("price"));
            item.setType(jo_inside.getString("type"));
            item.setMoney(jo_inside.getString("money"));
            item.setCondition(jo_inside.getString("condition"));
            item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
            item.setOver(jo_inside.getBoolean("over"));
            item.setPriceNormal(jo_inside.getDouble("priceNormal"));
            item.setPriceDiscount(jo_inside.getDouble("priceDiscount"));

            Gson gson = new Gson();
            Yng_Ubication yngUbication = gson.fromJson(jo_inside.getString("yng_Ubication"), Yng_Ubication.class);
            item.setYng_Ubication(yngUbication);

            array_cat_list_new.add(item);

        }

        return array_cat_list_new;
    }
    /*************************/

    public void  getCountSearch(String url){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("X-API-KEY", Network.API_KEY)
                .get()
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }
                //

                final String responce=""+(responseBody.string());
                try {
                    end(""+responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("responce:------------",""+responce);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtNoOfItem.setText(responce+"");
                        numCoincidencia = Integer.valueOf(responce+"");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        recyclerResponsive();

        if(modo.equals("grid")) {
            recycler_cat_list.setLayoutManager(new StaggeredGridLayoutManager(col, 1));
            adapter_cat_list = new ListGridAdapter(getApplicationContext(), array_cat_list);
            recycler_cat_list.setAdapter(adapter_cat_list);
        }
    }

    public void recyclerResponsive(){
        Log.e("oriencation:----",""+getApplicationContext().getResources().getConfiguration().orientation);
        Log.e("dpi:----",""+metrics.xdpi);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double d1 = metrics.widthPixels / metrics.xdpi;
        double d2 = metrics.heightPixels / metrics.ydpi;
        double deviceInches = Math.sqrt(d1 * d1 + d2 * d2);
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (deviceInches > 8) {col = 4;}
            else if (deviceInches >= 6) {col = 4;}
            else if (deviceInches < 6) {col = 3;}
            else {col = 2;}
        }else if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(deviceInches > 8){col=3;}
            else if(deviceInches >= 6){col=3;}
            else if(deviceInches < 6){col=2;}
            else {col=2;}
        }
    }
}
