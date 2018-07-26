package com.valecom.yingul.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.valecom.yingul.Item.ItemGallery;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.Util.MyApplication;
import com.valecom.yingul.adapter.GalleryAdapter;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityGalleryDetail extends AppCompatActivity {

    RecyclerView recyclerViewDetail;
    // ImageView ImgDetail;
    ArrayList<ItemGallery> array_gallery;
    GalleryAdapter adapter_gallery;
    ItemGallery itemGalleryList;
    private MyApplication myApplication;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    ImageView image_slider;

    private MaterialDialog progressDialog;
    String itemId="2783";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        /*if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);*/
            setTheme(R.style.AppTheme);
        /*}
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
        }*/
        setContentView(R.layout.activity_gallery_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.gallery_screen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        itemId = getIntent().getStringExtra("itemId");

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_gallery = new ArrayList<>();
        viewpager_slider = (ViewPager) findViewById(R.id.viewPager);
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        // ImgDetail=(ImageView)findViewById(R.id.image_product_image);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(ActivityGalleryDetail.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityGalleryDetail.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        loadJSONFromAssetGallery();

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(ActivityGalleryDetail.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewpager_slider.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public ArrayList<ItemGallery> loadJSONFromAssetGallery() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/Image/"+itemId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            ItemGallery itemGalleryList = new ItemGallery();

                            itemGalleryList.setGalleryImage("principal"+itemId+".jpeg");

                            array_gallery.add(itemGalleryList);

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                itemGalleryList = new ItemGallery();

                                itemGalleryList.setGalleryImage(jo_inside.getString("image"));

                                array_gallery.add(itemGalleryList);
                            }

                            setAdapterGalleryList();
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(ActivityGalleryDetail.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                        }

                        if (progressDialog != null && progressDialog.isShowing()) {
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
                        Toast.makeText(ActivityGalleryDetail.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ActivityGalleryDetail.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ActivityGalleryDetail.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
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
        //RequestQueue queue = MySingleton.getInstance(ActivityProductDetail.this).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(ActivityGalleryDetail.this).addToRequestQueue(postRequest);

        return array_gallery;

        /*ArrayList<ItemGallery> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("gallery_product_list.json");
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
                ItemGallery itemGalleryList = new ItemGallery();

                itemGalleryList.setGalleryImage(jo_inside.getString("gallery_image"));

                array_gallery.add(itemGalleryList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterGalleryList();
        return array_gallery;*/
    }

    public void setAdapterGalleryList() {

        adapter_gallery = new GalleryAdapter(ActivityGalleryDetail.this, array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        adapter = new ImagePagerAdapter();
        viewpager_slider.setAdapter(adapter);

        // itemGalleryList = array_gallery.get(0);
//        Picasso.with(ActivityGalleryDetail.this).load("file:///android_asset/image/" + itemGalleryList.getGalleryImage()).into(image_slider);
//


    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return array_gallery.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.viewpager_gallery_detail, container, false);
            assert imageLayout != null;
            itemGalleryList = array_gallery.get(position);
            image_slider = (ImageView) imageLayout.findViewById(R.id.image_product_image);

            Picasso.with(ActivityGalleryDetail.this).load(Network.BUCKET_URL + itemGalleryList.getGalleryImage()).placeholder(R.drawable.placeholder320).into(image_slider);


            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
