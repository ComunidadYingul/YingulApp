package com.valecom.yingul.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.Item.ItemAllCategory;
import com.valecom.yingul.Item.ItemCategory;
import com.valecom.yingul.Item.ItemCategoryList;
import com.valecom.yingul.Item.ItemHomeSlider;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.AllCategoryHomeAdapter;
import com.valecom.yingul.adapter.CategoryListAdapter;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.adapter.StoreHomeAdapter;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    ArrayList<ItemHomeSlider> array_Slider;
    ItemHomeSlider itemSlider;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    CircleIndicator circleIndicator;
    int currentCount = 0;

    Button view_all_cat,view_all_latest,view_all_trending;

    RecyclerView recycler_home_all_category;
    AllCategoryHomeAdapter adapter_all_category;
    ArrayList<ItemAllCategory> array_all_category;

    RecyclerView recycler_home_trending;
    CategoryListAdapter adapter_trending;
    ArrayList<ItemCategoryList> array_trending;

    RecyclerView recycler_home_latest;
    LatestListAdapter adapter_latest;
    ArrayList<ItemCategoryList> array_latest;

    RecyclerView recycler_home_category;
    StoreHomeAdapter adapter_category;
    ArrayList<ItemCategory> array_category;

    private MaterialDialog progressDialog;
    private FragmentManager fragmentManager;

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);

        array_Slider = new ArrayList<>();
        array_all_category = new ArrayList<>();
        array_latest = new ArrayList<>();
        array_trending = new ArrayList<>();
        array_category = new ArrayList<>();
        fragmentManager = getActivity().getSupportFragmentManager();

        viewpager_slider = (ViewPager) rootView.findViewById(R.id.viewPager);
        array_Slider = new ArrayList<>();
        circleIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator_unselected_background);

        view_all_latest=(Button)rootView.findViewById(R.id.btn__view_all_latest);
        view_all_trending=(Button)rootView.findViewById(R.id.btn__view_all_trending);
        view_all_cat=(Button)rootView.findViewById(R.id.btn__view_all);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);

        recycler_home_all_category = (RecyclerView) rootView.findViewById(R.id.rv_home_all_category);
        recycler_home_all_category.setHasFixedSize(false);
        recycler_home_all_category.setNestedScrollingEnabled(false);
        recycler_home_all_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_all_category.addItemDecoration(itemDecoration);

        recycler_home_latest = (RecyclerView) rootView.findViewById(R.id.rv_home_latest);
        recycler_home_latest.setHasFixedSize(false);
        recycler_home_latest.setNestedScrollingEnabled(false);
        recycler_home_latest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_latest.addItemDecoration(itemDecoration);

        recycler_home_trending = (RecyclerView) rootView.findViewById(R.id.rv_home_trending);
        recycler_home_trending.setHasFixedSize(false);
        recycler_home_trending.setNestedScrollingEnabled(false);
        recycler_home_trending.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_trending.addItemDecoration(itemDecoration);

        recycler_home_category = (RecyclerView) rootView.findViewById(R.id.rv_home_category);
        recycler_home_category.setHasFixedSize(false);
        recycler_home_category.setNestedScrollingEnabled(false);
        recycler_home_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_category.addItemDecoration(itemDecoration);



        loadJSONFromAssetHomeSlider();

        return rootView;
    }

    public ArrayList<ItemHomeSlider> loadJSONFromAssetHomeSlider() {
        ArrayList<ItemHomeSlider> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("home_slider.json");
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
        setAdapter();
        return array_Slider;

    }

    public void setAdapter() {

        adapter = new ImagePagerAdapter();
        viewpager_slider.setAdapter(adapter);
        circleIndicator.setViewPager(viewpager_slider);
        autoPlay(viewpager_slider);

        loadJSONFromAssetHomeCoupon();

    }

    private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (adapter != null && viewpager_slider.getAdapter().getCount() > 0) {
                        int position = currentCount % adapter.getCount();
                        currentCount++;
                        viewpager_slider.setCurrentItem(position);
                        autoPlay(viewpager_slider);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 2000);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return array_Slider.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.row_home_slider_item, container, false);
            assert imageLayout != null;
            itemSlider = array_Slider.get(position);
            ImageView image_slider = (ImageView) imageLayout.findViewById(R.id.imageView_home_slider);
            TextView text_title = (TextView) imageLayout.findViewById(R.id.text_home_slider_title);
            TextView txt_description = (TextView) imageLayout.findViewById(R.id.text_home_slider_desc);

            text_title.setText(itemSlider.getHomeSliderName());
            txt_description.setText(itemSlider.getHomeSliderDescription());

            Picasso.with(getActivity()).load("file:///android_asset/image/" + itemSlider.getHomeSliderImage()).placeholder(R.drawable.placeholder320).into(image_slider);

            image_slider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                    startActivity(intent_detail);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    /***************************** ALL CATEGORIES ********************************/

    public ArrayList<ItemAllCategory> loadJSONFromAssetHomeCoupon() {
        ArrayList<ItemAllCategory> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("all_category_list.json");
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
                ItemAllCategory itemHomeAllCategory = new ItemAllCategory();

                itemHomeAllCategory.setAllCategoryTitle(jo_inside.getString("all_category_title"));
                itemHomeAllCategory.setAllCategoryImage(jo_inside.getString("all_category_image"));
                itemHomeAllCategory.setAllCategoryLink(jo_inside.getString("all_category_link"));
                itemHomeAllCategory.setAllCategoryDescription(jo_inside.getString("all_category_desc"));

                array_all_category.add(itemHomeAllCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCoupon();
        return array_all_category;

    }

    public void setAdapterHomeCoupon() {

        adapter_all_category = new AllCategoryHomeAdapter(getActivity(), array_all_category);
        recycler_home_all_category.setAdapter(adapter_all_category);

        loadJSONFromAssetHomeTrending();
    }

    /***************************** ALL OVERS ********************************/

    public ArrayList<ItemCategoryList> loadJSONFromAssetHomeTrending() {
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/over/true",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();
                                itemHomeCategoryList.setCategoryListId(jo_inside.getString("itemId"));
                                itemHomeCategoryList.setCategoryListName(jo_inside.getString("name"));
                                itemHomeCategoryList.setCategoryListImage(jo_inside.getString("principalImage"));
                                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("description"));
                                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("price"));


                                JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                itemHomeCategoryList.setCategorySeller(seller.getUsername());

                                array_trending.add(itemHomeCategoryList);

                            }
                            setAdapterHomeTrending();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
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

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
        return array_trending;

    }

    public void setAdapterHomeTrending() {

        adapter_trending = new CategoryListAdapter(getActivity(), array_trending);
        recycler_home_trending.setAdapter(adapter_trending);

        loadJSONFromAssetHomeLatest();
    }

    /***************************** ALL ITEMS ********************************/

    public ArrayList<ItemCategoryList> loadJSONFromAssetHomeLatest() {

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
                                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();
                                itemHomeCategoryList.setCategoryListId(jo_inside.getString("itemId"));
                                itemHomeCategoryList.setCategoryListName(jo_inside.getString("name"));
                                itemHomeCategoryList.setCategoryListImage(jo_inside.getString("principalImage"));
                                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("description"));
                                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("price"));

                                JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                itemHomeCategoryList.setCategorySeller(seller.getUsername());


                                array_latest.add(itemHomeCategoryList);

                            }
                            setAdapterHomeCategoryList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
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

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);


        return array_latest;

    }

    public void setAdapterHomeCategoryList() {

        adapter_latest = new LatestListAdapter(getActivity(), array_latest);
        recycler_home_latest.setAdapter(adapter_latest);

        //loadJSONFromAssetHomeCoupon();
        loadJSONFromAssetHomeCategory();
    }

    /***************************** ALL STORES ********************************/

    public ArrayList<ItemCategory> loadJSONFromAssetHomeCategory() {
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

                                ItemCategory itemHomeCategory = new ItemCategory();

                                itemHomeCategory.setCategoryName(jo_inside.getString("name"));
                                itemHomeCategory.setCategoryImage("store/"+jo_inside.getString("mainImage"));
                                itemHomeCategory.setCategoryImageBanner("store/"+jo_inside.getString("bannerImage"));
                                itemHomeCategory.setCategoryNoItem(jo_inside.getString("storeId"));

                                array_category.add(itemHomeCategory);

                            }
                            setAdapterHomeCategory();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
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
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
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

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
        return array_category;

    }

    public void setAdapterHomeCategory() {

        adapter_category = new StoreHomeAdapter(getActivity(), array_category);
        recycler_home_category.setAdapter(adapter_category);

        //loadJSONFromAssetHomeLatest();
    }


}
