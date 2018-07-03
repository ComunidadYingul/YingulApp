package com.valecom.yingul.main.index;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.valecom.yingul.Item.ItemHomeSlider;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.adapter.MainAdapter;
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
    public static ArrayList<Yng_Category> array_all_category;
    public static ArrayList<Yng_Store> array_all_stores;

    private ArrayList<Object> objects = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        loadMore = (FloatingActionButton)rootView.findViewById(R.id.loadMore);

        array_all_category = new ArrayList<>();
        array_all_items = new ArrayList<>();
        array_all_over = new ArrayList<>();
        array_all_stores = new ArrayList<>();
        array_Slider = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start += paso;
                end += paso;
                updateAllItems();
            }
        });

        getSlider();

        return rootView;
    }

    private ArrayList<Object> getObject() {
        objects.add(array_Slider.get(0));
        objects.add(array_all_category.get(0));
        objects.add(array_all_over.get(0));
        objects.add(array_all_items.get(0));
        objects.add(array_all_stores.get(0));
        objects.add(array_all_items.get(0));
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
        getAllItems();
        return array_all_category;

    }

    public ArrayList<Yng_Item> getAllItems() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/false/Desc/0/30",
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
                                array_all_items.add(item);
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


        return array_all_items;

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

                            setAdapterHomeCategoryList();

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
        recyclerView.setAdapter(adapter);

        adapter.setArrays(array_all_items,array_all_over,array_all_category,array_all_stores);
    }

    public ArrayList<Yng_Item> updateAllItems() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/false/Desc/"+start+"/"+end,
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
                                array_all_items.add(item);
                                //}
                                //array_all_items.add(item);
                                adapter.updateDataAllItems();

                            }

                            //getAllOver();
                            //setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
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

}
