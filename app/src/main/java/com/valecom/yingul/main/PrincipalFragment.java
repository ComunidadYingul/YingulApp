package com.valecom.yingul.main;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.Item.ItemHomeSlider;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.AllCategoryHomeAdapter;
import com.valecom.yingul.adapter.CategoryListAdapter;
import com.valecom.yingul.adapter.LatestListAdapter;
import com.valecom.yingul.adapter.ListGridAdapter;
import com.valecom.yingul.adapter.StoreHomeAdapter;
import com.valecom.yingul.main.allItems.AllItemsActivity;
import com.valecom.yingul.main.createStore.CreateStoreActivity;
import com.valecom.yingul.main.over.AllNotOverActivity;
import com.valecom.yingul.main.over.OverActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Store;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    NestedScrollView scrollView2;

    ArrayList<ItemHomeSlider> array_Slider;
    ItemHomeSlider itemSlider;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    CircleIndicator circleIndicator;
    int currentCount = 0;

    Button view_all_cat,view_all_latest,view_all_trending;

    RecyclerView recycler_home_all_category;
    AllCategoryHomeAdapter adapter_all_category;
    ArrayList<Yng_Category> array_all_category;

    RecyclerView recycler_home_trending;
    CategoryListAdapter adapter_trending;
    ArrayList<Yng_Item> array_trending;

    RecyclerView recycler_home_latest;
    LatestListAdapter adapter_latest;
    ArrayList<Yng_Item> array_latest;

    RecyclerView recycler_home_category;
    StoreHomeAdapter adapter_category;
    ArrayList<Yng_Store> array_category;

    RecyclerView recycler_home_all_items;
    ListGridAdapter adapter_all_items;
    ArrayList<Yng_Item> array_all_items;
    StaggeredGridLayoutManager manager_all_items;

    private MaterialDialog progressDialog;
    private FragmentManager fragmentManager;

    private LinearLayout copyrightLayout;

    /*********/
    int col=2;
    String modo="grid";
    DisplayMetrics metrics = new DisplayMetrics();
    int paso = 20;
    int start=0;
    int end =paso;

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);

        recyclerResponsive();

        progressDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_Slider = new ArrayList<>();
        array_all_category = new ArrayList<>();
        array_latest = new ArrayList<>();
        array_trending = new ArrayList<>();
        array_category = new ArrayList<>();
        array_all_items = new ArrayList<>();
        fragmentManager = getActivity().getSupportFragmentManager();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels

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

        manager_all_items = new StaggeredGridLayoutManager(col,1);
        recycler_home_all_items = (RecyclerView) rootView.findViewById(R.id.rv_home_all_items);
        recycler_home_all_items.setHasFixedSize(true);
        recycler_home_all_items.setNestedScrollingEnabled(false);
        recycler_home_all_items.setLayoutManager(manager_all_items);
        recycler_home_all_items.addItemDecoration(itemDecoration);

        recycler_home_all_items.getLayoutParams().height = height;

        scrollView2 = (NestedScrollView) rootView.findViewById(R.id.scrollView2);

        if (scrollView2 != null) {

            scrollView2.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    int[] viewsIds = manager_all_items.findFirstCompletelyVisibleItemPositions(null);

                    if (scrollY > oldScrollY) {
                        Log.i("Scroll:----", "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY && viewsIds[0] == 0) {
                        Log.i("Scroll:----", "Scroll UP");
                        recycler_home_all_items.setHasFixedSize(true);
                        recycler_home_all_items.setNestedScrollingEnabled(false);
                    }

                    if (scrollY == 0) {
                        Log.e("Scroll:----", "TOP SCROLL");
                    }

                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
                        Log.e("Scroll:----", "BOTTOM SCROLL");
                        recycler_home_all_items.setHasFixedSize(false);
                        recycler_home_all_items.setNestedScrollingEnabled(true);
                        //updateProductTestData();
                    }
                }
            });
        }

        recycler_home_all_items.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Log.e("Position:---","final");
                    if(array_all_items.size() % paso == 0) {
                        start += paso;
                        end += paso;
                        updateAllItems();
                    }
                }




            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Log.e("verify_Position:---",((LinearLayoutManager) recycler_home_all_items.getLayoutManager()).findFirstCompletelyVisibleItemPosition()+"");

                /*if(((LinearLayoutManager) recycler_home_all_items.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0){
                    //Log.e("Position:---","toppppp");
                    recycler_home_all_items.setHasFixedSize(true);
                    recycler_home_all_items.setNestedScrollingEnabled(false);
                }*/

                /*if (dy < 0) {
                    Log.e("Position:---","upppp");

                } else if (dy > 0) {
                    Log.e("Position:---","downnnn");
                }*/

                int[] viewsIds = manager_all_items.findFirstCompletelyVisibleItemPositions(null);
                //RecyclerView.ViewHolder firstViewHolder = recycler_home_all_items.findViewHolderForLayoutPosition(viewsIds[0]);
                //View itemView = firstViewHolder.itemView;

                //Log.e("verify_Position:---",""+viewsIds[0]);
            }


        });

        //copyrightLayout = (LinearLayout) rootView.findViewById(R.id.copyrightLayout);
        /*copyrightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://s3-us-west-2.amazonaws.com/jsa-s3-bucketimage/politicas/terminos-y-condiciones-de-uso.pdf"));
                startActivity(viewIntent);
            }
        });*/

        view_all_trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverActivity.class);
                startActivity(intent);
            }
        });

        view_all_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllNotOverActivity.class);
                startActivity(intent);
            }
        });

        loadJSONFromAssetHomeSlider();

        return rootView;
    }

    public ArrayList<Yng_Item> updateAllItems() {

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/All/Desc/"+start+"/"+end,
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
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/


                                //if(item.getPriceDiscount()==0 && !item.getProductPagoEnvio().equals("gratis")){
                                //array_latest.add(item);
                                //}
                                array_all_items.add(item);


                            }
                            adapter_all_items.notifyDataSetChanged();

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

        return array_all_items;

    }

    public void end(String end) throws JSONException {
        Log.i("end",""+end);
    }
    public void start(String start){
        Log.i("start",""+start);
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
                    /*Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                    startActivity(intent_detail);*/
                    Intent intent;
                    if(position == 0){
                        intent = new Intent(getContext(),AllItemsActivity.class);
                        getContext().startActivity(intent);
                    }else  if(position == 1){
                        intent = new Intent(getContext(),SellActivity.class);
                        getContext().startActivity(intent);
                    }else  if(position == 2){
                        intent = new Intent(getContext(),SellActivity.class);
                        getContext().startActivity(intent);
                    }else  if(position == 3){
                        intent = new Intent(getContext(),OverActivity.class);
                        getContext().startActivity(intent);
                    }else  if(position == 4){
                        intent = new Intent(getContext(),OverActivity.class);
                        getContext().startActivity(intent);
                    }else  if(position == 5){
                        intent = new Intent(getContext(),CreateStoreActivity.class);
                        getContext().startActivity(intent);
                    }else{
                        //intent = new Intent(context,AllItemsActivity.class);
                    }
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

    public ArrayList<Yng_Category> loadJSONFromAssetHomeCoupon() {
        ArrayList<Yng_Category> locList = new ArrayList<>();
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
        setAdapterHomeCoupon();
        return array_all_category;

    }

    public void setAdapterHomeCoupon() {

        adapter_all_category = new AllCategoryHomeAdapter(getActivity(), array_all_category);
        recycler_home_all_category.setAdapter(adapter_all_category);

        loadJSONFromAssetHomeTrending();
    }

    /***************************** ALL OVERS ********************************/

    public ArrayList<Yng_Item> loadJSONFromAssetHomeTrending() {
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/listItemParams/All/true/Desc/0/20",
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
                                item.setMoney(jo_inside.getString("money"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));


                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/

                                array_trending.add(item);

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

    public ArrayList<Yng_Item> loadJSONFromAssetHomeLatest() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/false/Desc/"+start+"/"+end,
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
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/


                                //if(item.getPriceDiscount()==0 && !item.getProductPagoEnvio().equals("gratis")){
                                    array_latest.add(item);
                                //}
                                //array_all_items.add(item);


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

    public ArrayList<Yng_Store> loadJSONFromAssetHomeCategory() {
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

                                array_category.add(item);

                            }
                            Yng_Store item = new Yng_Store();

                            item.setStoreId((long) 000000);
                            item.setName("Ver tiendas");
                            item.setMainImage("store/"+"sin.jpg");
                            item.setBannerImage("store/"+"sin.jpg");

                            array_category.add(item);

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

        loadJSONFromAssetHomeAllItems();
        //setAdapterHomeAllItems();
    }

    /***************************** ALL ITEMS BOTTOM********************************/

    public ArrayList<Yng_Item> loadJSONFromAssetHomeAllItems() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "/item/listItemParams/All/All/Desc/"+start+"/"+end,
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
                                item.setMoney(jo_inside.getString("money"));
                                item.setProductPagoEnvio(jo_inside.getString("productPagoEnvio"));
                                item.setPriceNormal(Double.valueOf(jo_inside.getString("priceNormal")));
                                item.setPriceDiscount(Double.valueOf(jo_inside.getString("priceDiscount")));

                                /*JSONObject user = jo_inside.getJSONObject("user");
                                Gson gson = new Gson();
                                Yng_User seller = gson.fromJson(String.valueOf(user) , Yng_User.class);
                                item.setCategorySeller(seller.getUsername());*/


                                //if(item.getPriceDiscount()==0 && !item.getProductPagoEnvio().equals("gratis")){
                                //array_latest.add(item);
                                //}
                                array_all_items.add(item);


                            }
                            setAdapterHomeAllItems();

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

    public void setAdapterHomeAllItems() {

        adapter_all_items = new ListGridAdapter(getActivity(), array_all_items);
        recycler_home_all_items.setAdapter(adapter_all_items);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        recyclerResponsive();

        manager_all_items = new StaggeredGridLayoutManager(col,1);
        recycler_home_all_items.setLayoutManager(manager_all_items);

        Log.e("ROTACION----:", "Rotando...");
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
