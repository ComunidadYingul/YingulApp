package com.valecom.yingul.main.index;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.valecom.yingul.Item.ItemHomeSlider;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.adapter.MainAdapter;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Store;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    RecyclerView recyclerView;
    MainAdapter adapter;
    FloatingActionButton loadMore;

    int paso = 30;
    int start=0;
    int end =paso;


    public static ArrayList<ItemHomeSlider> array_Slider;

    public static ArrayList<Yng_Item> array_all_items;
    public static ArrayList<Yng_Item> array_all_over;
    public static ArrayList<Yng_Item> array_all_not_over;
    public static ArrayList<Yng_Category> array_all_category;
    public static ArrayList<Yng_Store> array_all_stores;


    private ArrayList<Object> objects = new ArrayList<>();

    /*********/
    int col=2;
    String modo="grid";
    DisplayMetrics metrics = new DisplayMetrics();

    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        ((MainActivity)getActivity()).toolbar.setTitle("Yingul");

        loadMore = (FloatingActionButton)rootView.findViewById(R.id.loadMore);

        array_all_category = new ArrayList<>();
        array_all_items = new ArrayList<>();
        array_all_over = new ArrayList<>();
        array_all_not_over = new ArrayList<>();
        array_all_stores = new ArrayList<>();
        array_Slider = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerResponsive();

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(array_all_items.size() % paso == 0) {
                    start += paso;
                    end += paso;
                    updateAllItems();
                }
            }
        });

        getSlider();

        return rootView;
    }

    private ArrayList<Object> getObject() {
        if(array_Slider.size() > 0) {
            objects.add(array_Slider.get(0));
        }else {
            objects.add(null);
        }
        if(array_all_category.size() > 0) {
            objects.add(array_all_category.get(0));
        }else {
            objects.add(null);
        }
        if(array_all_over.size() > 0) {
            objects.add(array_all_over.get(0));
        }else {
            objects.add(null);
        }
        if(array_all_not_over.size() > 0) {
            objects.add(array_all_not_over.get(0));
        }else {
            objects.add(null);
        }
        if(array_all_stores.size() > 0) {
            objects.add(array_all_stores.get(0));
        }else {
            objects.add(null);
        }
        if(array_all_items.size() > 0) {
            objects.add(array_all_items.get(0));
        }else {
            objects.add(null);
        }
        return objects;
    }

    /*public static ArrayList<SingleVertical> getVerticalData() {
        ArrayList<SingleVertical> singleVerticals = new ArrayList<>();
        singleVerticals.add(new SingleVertical("Charlie Chaplin", "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an English comic actor,....", R.mipmap.charlie));
        singleVerticals.add(new SingleVertical("Mr.Bean", "Mr. Bean is a British sitcom created by Rowan Atkinson and Richard Curtis, and starring Atkinson as the title character.", R.mipmap.mrbean));
        singleVerticals.add(new SingleVertical("Jim Carrey", "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist, screenwriter...", R.mipmap.jim));
        return singleVerticals;
    }*/


    /*public static ArrayList<SingleHorizontal> getHorizontalData() {
        ArrayList<SingleHorizontal> singleHorizontals = new ArrayList<>();
        singleHorizontals.add(new SingleHorizontal(R.mipmap.charlie, "Charlie Chaplin", "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an English comic actor,....", "2010/2/1"));
        singleHorizontals.add(new SingleHorizontal(R.mipmap.mrbean, "Mr.Bean", "Mr. Bean is a British sitcom created by Rowan Atkinson and Richard Curtis, and starring Atkinson as the title character.", "2010/2/1"));
        singleHorizontals.add(new SingleHorizontal(R.mipmap.jim, "Jim Carrey", "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist, screenwriter...", "2010/2/1"));
        return singleHorizontals;
    }*/

    public static ArrayList<Yng_Category> getAllCategoryData() {
        return array_all_category;
    }

    public static ArrayList<Yng_Item> getAllItemsData() {
        return array_all_items;
    }

    public static ArrayList<Yng_Item> getAllOverData() {
        return array_all_over;
    }

    public static ArrayList<Yng_Store> getAllStoreData() {
        return array_all_stores;
    }

    public static ArrayList<ItemHomeSlider> getSliderData() {
        return array_Slider;
    }


    public ArrayList<ItemHomeSlider> getSlider() {
        ArrayList<ItemHomeSlider> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("home_slider.json");
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
            JSONArray m_jArry = obj.getJSONArray("homeslider");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemHomeSlider itemHomeSlider = new ItemHomeSlider();

                itemHomeSlider.setHomeSliderName(jo_inside.getString("title"));
                itemHomeSlider.setHomeSliderDescription(jo_inside.getString("description"));
                itemHomeSlider.setHomeSliderImage(jo_inside.getString("image"));

                array_Slider.add(itemHomeSlider);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //setAdapter();
        getAllCategory();
        return array_Slider;

    }

    public ArrayList<Yng_Category> getAllCategory() {
        ArrayList<Yng_Category> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("all_category_list.json");
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
                Yng_Category item = new Yng_Category();

                item.setCategoryId(jo_inside.getLong("all_category_id"));
                item.setName(jo_inside.getString("all_category_title"));
                item.setItemType(jo_inside.getString("all_category_item_type"));
                item.setImage(jo_inside.getString("all_category_image"));

                array_all_category.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getNotOverItems();
        return array_all_category;

    }

    public ArrayList<Yng_Item> getNotOverItems() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/false/Desc/0/20",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("All Items:---",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/


                                //if(item.getPriceDiscount()==0 && !item.getProductPagoEnvio().equals("gratis")){
                                array_all_not_over.add(item);
                                //}
                                //array_all_items.add(item);


                            }

                            getAllOver();
                            //setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getContext(), "Error 1", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "Error 2", Toast.LENGTH_SHORT).show();
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

        //postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);


        return array_all_not_over;

    }

    public ArrayList<Yng_Item> getAllOver() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/true/Desc/0/20",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("All Overs:---",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/


                                //if(item.getPriceDiscount()==0 && !item.getProductPagoEnvio().equals("gratis")){
                                array_all_over.add(item);
                                //}
                                //array_all_items.add(item);


                            }
                            getAllStores();


                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getContext(), "Error 1", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "Error 2", Toast.LENGTH_SHORT).show();
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

        //postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);


        return array_all_over;

    }

    public ArrayList<Yng_Store> getAllStores() {
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "store/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Eddy--store",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);

                                Yng_Store item = new Yng_Store();

                                item.setStoreId(Long.valueOf(jo_inside.getString("storeId")));
                                item.setName(jo_inside.getString("name"));
                                item.setMainImage("store/"+jo_inside.getString("mainImage"));
                                item.setBannerImage("store/"+jo_inside.getString("bannerImage"));

                                array_all_stores.add(item);

                            }
                            Yng_Store item = new Yng_Store();

                            item.setStoreId((long) 000000);
                            item.setName("Ver tiendas");
                            item.setMainImage("store/"+"sin.jpg");
                            item.setBannerImage("store/"+"sin.jpg");

                            array_all_stores.add(item);

                            loadFirstData();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getContext(), "error 1", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "error 2", Toast.LENGTH_SHORT).show();
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

        //postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
        return array_all_stores;

    }

    public void setAdapterHomeCategoryList() {

        adapter = new MainAdapter(getContext(), getObject());
        adapter.setCol(col);
        recyclerView.setAdapter(adapter);

        adapter.setArrays(array_all_not_over,array_all_over,array_all_category,array_all_stores,array_all_items);
    }

    public void end(String end) throws JSONException {
        Log.i("end",""+end);
    }
    public void start(String start){
        Log.i("start",""+start);
    }

    public ArrayList<Yng_Item> updateAllItems() {
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Network.API_URL + "/item/listItemParams/All/All/Desc/"+start+"/"+end)
                .addHeader("Content-Type","application/json")
                .addHeader("X-API-KEY",Network.API_KEY)
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray m_jArry = new JSONArray(responce);
                            Log.e("All Items:---",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));
                                array_all_items.add(item);
                                adapter.updateDataAllItems();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Mas publicaciones se muestran abajo", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });

        return array_all_items;

    }

    public ArrayList<Yng_Item> loadFirstData() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/All/Desc/"+start+"/"+end,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("All Items:---",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                Yng_Item item = new Yng_Item();
                                item.setItemId(Long.valueOf(jo_inside.getString("itemId")));
                                item.setName(jo_inside.getString("name"));
                                item.setPrincipalImage(jo_inside.getString("principalImage"));
                                item.setDescription(jo_inside.getString("description"));
                                item.setPrice(Double.valueOf(jo_inside.getString("price")));
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                array_all_items.add(item);

                            }

                            setAdapterHomeCategoryList();

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getContext(), "Mas publicaciones se muestran abajo", Toast.LENGTH_SHORT).show();
                            Log.e("ERROR","Error excepiion revisar!!!!!!");
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "Error 2", Toast.LENGTH_SHORT).show();
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

        //postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);


        return array_all_items;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /*adapter = new MainAdapter(getContext(), getObject());*/
        if(adapter == null){recyclerResponsive2();}
        else {
            recyclerResponsive2();
            adapter.setCol(col);
            recyclerView.setAdapter(adapter);

            adapter.setArrays(array_all_not_over, array_all_over, array_all_category, array_all_stores, array_all_items);
        }
    }

    public void recyclerResponsive(){
        Log.e("oriencation:----",""+getContext().getResources().getConfiguration().orientation);
        Log.e("dpi:----",""+metrics.xdpi);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double d1 = metrics.widthPixels / metrics.xdpi;
        double d2 = metrics.heightPixels / metrics.ydpi;
        double deviceInches = Math.sqrt(d1 * d1 + d2 * d2);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (deviceInches > 8) {col = 4;}
            else if (deviceInches >= 6) {col = 4;}
            else if (deviceInches < 6) {col = 3;}
            else {col = 2;}
        }else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(deviceInches > 8){col=3;}
            else if(deviceInches >= 6){col=3;}
            else if(deviceInches < 6){col=2;}
            else {col=2;}
        }
        /*
        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (metrics.xdpi < 160) {col = 4;}
            else if (metrics.xdpi < 220) {col = 4;}
            else if (metrics.xdpi < 320) {col = 3;}
            else {col = 2;}
        }else if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(metrics.xdpi < 160){col=3;}
            else if(metrics.xdpi < 220){col=3;}
            else if(metrics.xdpi < 320){col=2;}
            else {col=2;}
        }
        * */
    }

    public void recyclerResponsive2(){
        Log.e("oriencation:----",""+getContext().getResources().getConfiguration().orientation);
        Log.e("dpi:----",""+metrics.xdpi);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double d1 = metrics.widthPixels / metrics.xdpi;
        double d2 = metrics.heightPixels / metrics.ydpi;
        double deviceInches = Math.sqrt(d1 * d1 + d2 * d2);
        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (deviceInches >8) {col = 3;}
            else if (deviceInches >= 6) {col = 3;}
            else if (deviceInches < 6) {col = 2;}
            else {col = 2;}
        }else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(deviceInches > 8){col=4;}
            else if(deviceInches >= 6){col=4;}
            else if(deviceInches < 6){col=3;}
            else {col=2;}
        }
    }

}
