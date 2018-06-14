package com.valecom.yingul.main.filter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.Switch;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.Item.ItemCategoryList;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.CityAdapter;
import com.valecom.yingul.adapter.CountryAdapter;
import com.valecom.yingul.adapter.ProvinceAdapter;
import com.valecom.yingul.helper.helper_number;
import com.valecom.yingul.helper.helper_string;
import com.valecom.yingul.main.ItemPickerActivity;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.SettingActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.model.Client;
import com.valecom.yingul.model.FilterParam;
import com.valecom.yingul.model.Invoice;
import com.valecom.yingul.model.Item;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    public static final String TAG = "NewInvoiceActivity";
    private MaterialDialog progressDialog;
    private JSONObject api_parameter;
    static final int ITEM_PICKER_TAG = 1;
    private Toolbar toolbar;
    private LinearLayout layoutFreeShipping,layoutClearFilter,layoutCircleFreeShipping,layoutCircleNew,layoutCircleUsed,layoutDiscounts,layoutCity,layoutSetCity,layoutClearUbication,layoutSetUbication;
    private ImageView imgFreeShipping;
    private TextView txtNew,txtUsed,textDiscount,textUbicationName,textPathUbication;

    private FilterParam filterParams;
    ArrayList<ItemCategoryList> array_cat_list;

    ListView list;
    CountryAdapter adapter;
    ArrayList<Yng_Country> array_list;
    ProvinceAdapter adapter1;
    ArrayList<Yng_Province> array_list1;
    CityAdapter adapter2;
    ArrayList<Yng_City> array_list2;

    Yng_Country country;
    Yng_Province province;
    Yng_City city;

    ListView listDiscount;
    private ArrayList discount_list;
    private ArrayAdapter discount_adapter;
    private MaterialDialog setting_address_edit_dialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= 23) verifyStoragePermissions(this);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        setSupportActionBar(toolbar);

        toolbar.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                Gson json = new Gson();
                returnIntent.putExtra("itemList", json.toJson(array_cat_list).toString());
                returnIntent.putExtra("filterParams", filterParams);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        //array_cat_list = (ArrayList<ItemCategoryList>)getIntent().getSerializableExtra("itemList");
        try {
            array_cat_list=stringToArrayItemCategoryList((String)getIntent().getSerializableExtra("itemList"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        filterParams = (FilterParam)getIntent().getSerializableExtra("filterParams");
        Gson json = new Gson();
        Log.e("itemList response",json.toJson(array_cat_list).toString());
        Log.e("filer response",json.toJson(filterParams).toString());

        discount_list = new ArrayList();
        discount_list.add("Todos");
        discount_list.add("10% off");
        discount_list.add("20% off");
        discount_list.add("30% off");
        discount_list.add("40% off");
        discount_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, discount_list);

        array_list = new ArrayList<Yng_Country>();
        adapter = new CountryAdapter(this, array_list);
        array_list1 = new ArrayList<Yng_Province>();
        adapter1 = new ProvinceAdapter(this, array_list1);
        array_list2 = new ArrayList<Yng_City>();
        adapter2 = new CityAdapter(this, array_list2);

        country = new Yng_Country();
        province = new Yng_Province();
        city = new Yng_City();

        layoutFreeShipping = (LinearLayout) findViewById(R.id.layoutFreeShipping);
        imgFreeShipping = (ImageView) findViewById(R.id.imgFreeShipping);
        layoutClearFilter = (LinearLayout) findViewById(R.id.layoutClearFilter);
        layoutCircleFreeShipping = (LinearLayout) findViewById(R.id.layoutCircleFreeShipping);
        layoutCircleNew = (LinearLayout) findViewById(R.id.layoutCircleNew);
        layoutCircleUsed = (LinearLayout) findViewById(R.id.layoutCircleUsed);
        txtNew = (TextView) findViewById(R.id.txtNew);
        txtUsed = (TextView) findViewById(R.id.txtUsed);
        layoutDiscounts = (LinearLayout) findViewById(R.id.layoutDiscounts);
        textDiscount = (TextView) findViewById(R.id.textDiscount);
        layoutCity = (LinearLayout) findViewById(R.id.layoutCity);
        textUbicationName = (TextView) findViewById(R.id.textUbicationName);
        layoutSetCity = (LinearLayout) findViewById(R.id.layoutSetCity);

        layoutFreeShipping.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setShipping();
            }
        });
        layoutClearFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cleanFilter();
                drawActivity();
            }
        });
        layoutCircleUsed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setCondition("used");
            }
        });
        layoutCircleNew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setCondition("new");
            }
        });
        layoutDiscounts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setDiscount();
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

        drawActivity();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        MySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_save)
        {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {

    }

    public void setShipping(){
        filterParams.setFreeShipping(!filterParams.isFreeShipping());
        drawActivity();
    }
    public void setCondition(String cond){
        if(filterParams.getCondition().equals(cond)){
            filterParams.setCondition("none");
        }else{
            filterParams.setCondition(cond);
        }
        drawActivity();
    }
    public void setDiscount(){
        setting_address_edit_dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.filter_set_discount_layout, true)
                .cancelable(true)
                .showListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        View view = setting_address_edit_dialog.getCustomView();

                        listDiscount = (ListView) view.findViewById(R.id.listDiscount);
                        // Assigning the adapter to ListView
                        listDiscount.setAdapter(discount_adapter);

                        listDiscount.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                switch (position){
                                    case 0:
                                        filterParams.setDiscount("all");
                                        break;
                                    case 1:
                                        filterParams.setDiscount("10");
                                        break;
                                    case 2:
                                        filterParams.setDiscount("20");
                                        break;
                                    case 3:
                                        filterParams.setDiscount("30");
                                        break;
                                    case 4:
                                        filterParams.setDiscount("40");
                                        break;
                                }
                                drawActivity();
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
                                filterParams.setUbication(null);
                                drawActivity();
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
                                filterParams.setUbication(null);
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
                                filterParams.setUbication(new Yng_Ubication());
                                filterParams.getUbication().setYng_Country(country);
                                drawActivity();
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
                                filterParams.setUbication(null);
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
                                filterParams.setUbication(new Yng_Ubication());
                                filterParams.getUbication().setYng_Country(country);
                                filterParams.getUbication().setYng_Province(province);
                                drawActivity();
                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                    // If the response is JSONObject instead of expected JSONArray
                                    setting_address_edit_dialog.dismiss();
                                }
                            }
                        });

                        textPathUbication.setText("Todas > "+country.getName()+" > "+province.getName().substring(0,7)+"...");

                        list = (ListView) view.findViewById(R.id.list);
                        list.setAdapter(adapter2);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Yng_City getCity = adapter2.getItem(position);
                                city=getCity;
                                filterParams.setUbication(new Yng_Ubication());
                                filterParams.getUbication().setYng_Country(country);
                                filterParams.getUbication().setYng_Province(province);
                                filterParams.getUbication().setYng_City(city);
                                drawActivity();
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

    public void drawActivity(){
        if(filterParams.isFreeShipping()){
            Picasso.with(this).load("file:///android_asset/image/car.png").into(imgFreeShipping);
            layoutCircleFreeShipping.setBackgroundResource(R.drawable.circle_background_white);
        }else{
            Picasso.with(this).load("file:///android_asset/image/carwhite.png").into(imgFreeShipping);
            layoutCircleFreeShipping.setBackgroundResource(R.drawable.circle_margin_white);
        }
        if(filterParams.getUbication()==null){
            layoutCity.setVisibility(View.GONE);
        }else{
            layoutCity.setVisibility(View.VISIBLE);
            if(filterParams.getUbication().getYng_City()==null){
                if(filterParams.getUbication().getYng_Province()==null){
                    if(filterParams.getUbication().getYng_Country()==null){
                        filterParams.setUbication(null);
                        layoutCity.setVisibility(View.GONE);
                    }else{
                        textUbicationName.setText(filterParams.getUbication().getYng_Country().getName());
                    }
                }else{
                    textUbicationName.setText(filterParams.getUbication().getYng_Province().getName());
                }
            }else{
                textUbicationName.setText(filterParams.getUbication().getYng_City().getName());
            }
        }
        switch (filterParams.getCondition()){
            case "none":
                layoutCircleNew.setBackgroundResource(R.drawable.oval_margin_white);
                layoutCircleUsed.setBackgroundResource(R.drawable.oval_margin_white);
                txtUsed.setTextColor(getResources().getColor(R.color.white));
                txtNew.setTextColor(getResources().getColor(R.color.white));
                break;
            case "new":
                layoutCircleUsed.setBackgroundResource(R.drawable.oval_margin_white);
                layoutCircleNew.setBackgroundResource(R.drawable.oval_background_white);
                txtUsed.setTextColor(getResources().getColor(R.color.white));
                txtNew.setTextColor(getResources().getColor(R.color.yngOrange));
                break;
            case "used":
                layoutCircleNew.setBackgroundResource(R.drawable.oval_margin_white);
                layoutCircleUsed.setBackgroundResource(R.drawable.oval_background_white);
                txtUsed.setTextColor(getResources().getColor(R.color.yngOrange));
                txtNew.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        switch (filterParams.getDiscount()){
            case "none":
                textDiscount.setText("Elegir");
                break;
            case "all":
                textDiscount.setText("Todos");
                break;
            case "10":
                textDiscount.setText("10% off");
                break;
            case "20":
                textDiscount.setText("20% off");
                break;
            case "30":
                textDiscount.setText("30% off");
                break;
            case "40":
                textDiscount.setText("40% off");
                break;
        }

    }

    public void cleanFilter(){
        filterParams.setFreeShipping(false);
        filterParams.setCondition("none");
        filterParams.setDiscount("none");
        filterParams.setUbication(null);
        drawActivity();
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
                            array_list.clear();
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject obj = items.getJSONObject(i);
                                Yng_Country item = new Yng_Country();
                                item.setCountryId(obj.getInt("countryId"));
                                item.setName(obj.getString("name"));
                                array_list.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            setCountry();
                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //0if (isAdded()) {
                                Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(FilterActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(FilterActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( FilterActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(FilterActivity.this).addToRequestQueue(postRequest);

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
                            Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(FilterActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(FilterActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( FilterActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(FilterActivity.this).addToRequestQueue(postRequest);

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
                            Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(FilterActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(FilterActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(FilterActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue queue = MySingleton.getInstance( FilterActivity.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(FilterActivity.this).addToRequestQueue(postRequest);

    }

    public ArrayList<ItemCategoryList> stringToArrayItemCategoryList(String itemList) throws JSONException {
        ArrayList<ItemCategoryList> array_cat_list_new= new ArrayList<>();

        JSONArray m_jArry = new JSONArray(itemList);
        Log.e("Eddy",m_jArry.toString());
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
            itemPublicSellerList.setCategoryListUbication(jo_inside.getString("CategoryListUbication"));

            array_cat_list_new.add(itemPublicSellerList);

        }

        return array_cat_list_new;
    }
}
