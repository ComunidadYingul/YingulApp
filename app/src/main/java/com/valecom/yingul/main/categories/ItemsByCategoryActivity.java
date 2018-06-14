package com.valecom.yingul.main.categories;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.valecom.yingul.Item.ItemCategoryList;
import com.valecom.yingul.Item.ItemColorSize;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.adapter.ListRowAdapter;
import com.valecom.yingul.adapter.SelectColorAdapter;
import com.valecom.yingul.adapter.SelectSizeAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.filter.FilterActivity;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.FilterParam;
import com.valecom.yingul.model.Yng_StateShipping;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemsByCategoryActivity extends AppCompatActivity {

    RecyclerView recycler_cat_list;
    ListGridAdapter adapter_cat_list;
    ArrayList<ItemCategoryList> array_cat_list;//ArrayList<Yng_Item> array_cat_list;
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
    LinearLayout lay_filter_click;
    private MaterialDialog progressDialog;

    String categoryId;
    private MaterialDialog setting_address_edit_dialog;
    /******filtros*****/
    static final int ITEM_PICKER_TAG = 1;
    ArrayList<ItemCategoryList> array_cat_list_backup;
    private FilterParam filterParams;
    /*********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.home_latest));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = this.getIntent().getExtras();
        categoryId = bundle.getString("categoryId");

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
        /*************/

        array_cat_list = new ArrayList<>();
        array_cat_list_backup = new ArrayList<>();
        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.loadAd(new AdRequest.Builder().build());

        recycler_cat_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new GridLayoutManager(ItemsByCategoryActivity.this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ItemsByCategoryActivity.this, R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        txtNoOfItem=(TextView)findViewById(R.id.text_cat_list_item);
        ImgList=(ImageView)findViewById(R.id.image_list);
        ImgGrid=(ImageView)findViewById(R.id.image_grid);
        ImgFilter=(ImageView)findViewById(R.id.image_filter);
        lay_filter_click=(LinearLayout)findViewById(R.id.lay_filter_click);

        ImgGrid.setImageResource(R.drawable.ic_grid_hover);
        ImgList.setImageResource(R.drawable.ic_list);

        ImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(ItemsByCategoryActivity.this, 2));
                adapter_cat_list = new ListGridAdapter(ItemsByCategoryActivity.this, array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list);
                ImgGrid.setImageResource(R.drawable.ic_grid_hover);
                ImgList.setImageResource(R.drawable.ic_list);
            }
        });

        ImgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(ItemsByCategoryActivity.this, 1));
                adapter_cat_list_listview = new ListRowAdapter(ItemsByCategoryActivity.this, array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list_listview);
                ImgList.setImageResource(R.drawable.ic_listview_hover);
                ImgGrid.setImageResource(R.drawable.ic_grid);
            }
        });
        /*******filtro********/
        lay_filter_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsByCategoryActivity.this, FilterActivity.class);
                Gson json = new Gson();
                intent.putExtra("itemList", json.toJson(array_cat_list_backup).toString());
                intent.putExtra("filterParams", filterParams);
                startActivityForResult(intent, ITEM_PICKER_TAG);

            }
        });
        /*****************/
        loadJSONFromAssetCategoryList();

    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetCategoryList() {

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/itemsByCategory/"+categoryId,
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
                                itemPublicSellerList.setCategoryListMoney(jo_inside.getString("money"));
                                itemPublicSellerList.setCategoryListCondition(jo_inside.getString("condition"));
                                itemPublicSellerList.setCategoryListEnvio(jo_inside.getString("productPagoEnvio"));
                                itemPublicSellerList.setCategoryListOver(jo_inside.getString("over"));
                                itemPublicSellerList.setCategoryListPriceNormal(jo_inside.getString("priceNormal"));
                                itemPublicSellerList.setCategoryListPriceDiscount(jo_inside.getString("priceDiscount"));

                                Gson gson = new Gson();
                                Yng_Ubication yngUbication = gson.fromJson(jo_inside.getString("yng_Ubication"), Yng_Ubication.class);
                                itemPublicSellerList.setCategoryListUbication(yngUbication);

                                //Log.e("envia",jo_inside.getString("yng_Ubication"));

                                array_cat_list.add(itemPublicSellerList);

                            }
                            array_cat_list_backup=array_cat_list;
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                            Toast.makeText(ItemsByCategoryActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ItemsByCategoryActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ItemsByCategoryActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ItemsByCategoryActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(ItemsByCategoryActivity.this).addToRequestQueue(postRequest);
        return array_cat_list;

        /*ArrayList<ItemCategoryList> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("category_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();

                itemHomeCategoryList.setCategoryListName(jo_inside.getString("cat_list_title"));
                itemHomeCategoryList.setCategoryListImage(jo_inside.getString("cat_list_image"));
                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("cat_list_description"));
                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("cat_list_price"));

                array_cat_list.add(itemHomeCategoryList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCategoryList();
        return array_cat_list;*/
    }

    public void setAdapterHomeCategoryList() {
        adapter_cat_list = new ListGridAdapter(ItemsByCategoryActivity.this, array_cat_list);
        txtNoOfItem.setText(adapter_cat_list.getItemCount()+"");
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

    public ArrayList<ItemCategoryList> stringToArrayItemCategoryList(String itemList) throws JSONException {
        ArrayList<ItemCategoryList> array_cat_list_new= new ArrayList<>();

        JSONArray m_jArry = new JSONArray(itemList);

        for (int i = 0; i < m_jArry.length(); i++) {
            JSONObject jo_inside = m_jArry.getJSONObject(i);
            ItemCategoryList itemPublicSellerList = new ItemCategoryList();
            itemPublicSellerList.setCategoryListId(jo_inside.getString("CategoryListId"));
            itemPublicSellerList.setCategoryListName(jo_inside.getString("CategoryListName"));
            itemPublicSellerList.setCategoryListImage(jo_inside.getString("CategoryListImage"));
            itemPublicSellerList.setCategoryListDescription(jo_inside.getString("CategoryListDescription"));
            itemPublicSellerList.setCategoryListPrice(jo_inside.getString("CategoryListPrice"));
            itemPublicSellerList.setCategoryListType(jo_inside.getString("CategoryListType"));
            itemPublicSellerList.setCategoryListMoney(jo_inside.getString("CategoryListMoney"));
            itemPublicSellerList.setCategoryListCondition(jo_inside.getString("CategoryListCondition"));
            itemPublicSellerList.setCategoryListEnvio(jo_inside.getString("CategoryListEnvio"));
            itemPublicSellerList.setCategoryListOver(jo_inside.getString("CategoryListOver"));
            itemPublicSellerList.setCategoryListPriceNormal(jo_inside.getString("CategoryListPriceNormal"));
            itemPublicSellerList.setCategoryListPriceDiscount(jo_inside.getString("CategoryListPriceDiscount"));

            Gson gson = new Gson();
            Yng_Ubication yngUbication = gson.fromJson(jo_inside.getString("yng_Ubication"), Yng_Ubication.class);
            itemPublicSellerList.setCategoryListUbication(yngUbication);
            //itemPublicSellerList.setCategoryListUbication(jo_inside.getString("CategoryListUbication"));

            Log.e("envia",itemPublicSellerList.getCategoryListId()+"");


            array_cat_list_new.add(itemPublicSellerList);

        }

        return array_cat_list_new;
    }
}
