package com.valecom.yingul.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.valecom.yingul.Item.ItemColorSize;
import com.valecom.yingul.Item.ItemGallery;
import com.valecom.yingul.Item.ItemOrderProduct;
import com.valecom.yingul.Item.ItemReview;
import com.valecom.yingul.Item.ItemReviewPublic;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.Util.RecyclerItemClickListener;
import com.valecom.yingul.adapter.GalleryAdapter;
import com.valecom.yingul.adapter.ReviewListAdapter;
import com.valecom.yingul.adapter.ReviewPublicListAdapter;
import com.valecom.yingul.adapter.SelectColorAdapter;
import com.valecom.yingul.adapter.SelectSizeAdapter;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.github.ornolfr.ratingview.RatingView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityProductDetail extends AppCompatActivity {

    RecyclerView recyclerViewDetail, recycler_detail_review, recyclerView_color, recyclerView_size, recyclerView_order_place;
    ImageView ImgDetail;
    ArrayList<ItemGallery> array_gallery;
    GalleryAdapter adapter_gallery;
    ArrayList<ItemReview> array_review;
    ReviewListAdapter adapter_review;
    ItemGallery itemGalleryList;
    TextView edit_quantity, text_product_name, text_product_price, text_no_cost, text_product_rate, text_select_size, text_select_color,
            text_product_buy, text_product_cart, txt_order_total_rs, txt_order_item, text_product_con_shop, text_product_place_order,text_description,text_product_title,text_desc;
    //EditText edt_pincode;
    TextView web_desc;
    View button_public_seller;
    RatingView ratingView;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    ArrayList<ItemOrderProduct> array_order_place;
    Dialog mDialogPlaceOrder;
    OrderPlaceAdapter adapter_orderPlaceAdapter;
    //LinearLayout linear_Layout_stars;

    ReviewPublicListAdapter adapter_review_public;
    ArrayList<ItemReviewPublic> array_publicaciones;

    private Menu menu;
    ScrollView scrollView;
    private MaterialDialog progressDialog;
    String itemId,itemSeller;
    Yng_Item itemTemp;
    Yng_User userTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.product_detail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle datos = this.getIntent().getExtras();
        itemId = datos.getString("itemId");
        //itemSeller = datos.getString("seller");
        Log.e("Eddy:-------","recupero:"+itemId);
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_publicaciones = new ArrayList<>();

        array_gallery = new ArrayList<>();
        array_review = new ArrayList<>();
        array_order_place = new ArrayList<>();
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        ImgDetail = (ImageView) findViewById(R.id.image_product_image);
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(ActivityProductDetail.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        text_product_name = (TextView) findViewById(R.id.text_product_title);
        text_product_price = (TextView) findViewById(R.id.text_product_price);
        text_no_cost = (TextView) findViewById(R.id.text_product_no_emi);
        //text_product_rate = (TextView) findViewById(R.id.text_product_rate);
        //text_select_size = (TextView) findViewById(R.id.text_select_size);
        //text_select_color = (TextView) findViewById(R.id.text_select_color);
        text_product_buy = (TextView) findViewById(R.id.text_product_buy);
        text_product_cart = (TextView) findViewById(R.id.text_product_cart);
        //edt_pincode = (EditText) findViewById(R.id.edt_delivery_code);
        web_desc = (TextView) findViewById(R.id.web_product_desc);
        //ratingView = (RatingView) findViewById(R.id.rating_product_rating);
        recycler_detail_review = (RecyclerView) findViewById(R.id.vertical_detail_review);
        //edt_pincode.setFocusable(false);
        button_public_seller = (View) findViewById(R.id.button_public_seller);
        edit_quantity = (EditText) findViewById(R.id.edit_quantity);

        recycler_detail_review.setHasFixedSize(false);
        recycler_detail_review.setNestedScrollingEnabled(false);
        recycler_detail_review.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        recycler_detail_review.addItemDecoration(itemDecoration);
        //*******************danielAgregado***
        text_desc=(TextView) findViewById(R.id.text_desc);
        text_product_title=(TextView) findViewById(R.id.text_product_title);
        //text_description=(TextView) findViewById(R.id.text_description);
        //linear_Layout_stars=(LinearLayout) findViewById(R.id.linear_Layout_stars);
        //linear_Layout_stars.setVisibility(View.INVISIBLE);

        //text_description.setText("Descripción del Producto");
        text_product_name.setText("");
        text_product_price.setText("");
        text_no_cost.setText("");
        //text_product_rate.setText("texto1");
        web_desc.setText("");
        text_desc.setText("Descripción:");






        /*text_select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectColor();
            }
        });*/

        /*text_select_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectSize();
            }
        });*/

        text_product_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemTemp.getType().equals("Product") || itemTemp.getType().equals("Motorized")){

                    showOrderPlace();
                }else{
                }



            }
        });

        /*text_product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityProductDetail.this, getResources().getString(R.string.item_added_cart), Toast.LENGTH_SHORT).show();
            }
        });*/

        button_public_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityProductDetail.this, ActivityPubliSellerList.class);
                //intent.putExtra("itemId",itemId);
                intent.putExtra("seller",itemSeller);
                startActivity(intent);
            }
        });

        loadJSONFromAssetGallery();
        RunLoginService();

//        scrollView.setOnTouchListener(new OnSwipeTouchListener(ActivityProductDetail.this) {
//
//            public void onSwipeRight() {
//                //Toast.makeText(ActivityProductDetail.this, "right", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//            public void onSwipeLeft() {
//                // Toast.makeText(ActivityProductDetail.this, "left", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//
//
//        });
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
                            Toast.makeText(ActivityProductDetail.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ActivityProductDetail.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ActivityProductDetail.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ActivityProductDetail.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(ActivityProductDetail.this).addToRequestQueue(postRequest);

        return array_gallery;
    }

    public void setAdapterGalleryList() {

        Log.e("Eddy","segundo");
        adapter_gallery = new GalleryAdapter(ActivityProductDetail.this, array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        itemGalleryList = array_gallery.get(0);
        Picasso.with(ActivityProductDetail.this).load(Network.BUCKET_URL + itemGalleryList.getGalleryImage()).into(ImgDetail);

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(ActivityProductDetail.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemGalleryList = array_gallery.get(position);
                Picasso.with(ActivityProductDetail.this).load(Network.BUCKET_URL + itemGalleryList.getGalleryImage()).into(ImgDetail);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ImgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(ActivityProductDetail.this, ActivityGalleryDetail.class);
                intent_gallery.putExtra("itemId",itemId);
                startActivity(intent_gallery);
            }
        });
        loadJSONFromAssetReview();
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

    public ArrayList<ItemReviewPublic> loadJSONFromAssetReview() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/Item/"+itemSeller,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            int size_jArray;
                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            if(m_jArry.length() >= 3){
                                size_jArray = 3;
                            }else{
                                size_jArray = m_jArry.length();
                            }
                            for (int i = 0; i < size_jArray; i++) {
                                //for (int i = 0; i < 3; i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                ItemReviewPublic itemPublicSellerList = new ItemReviewPublic();
                                itemPublicSellerList.setReviewId(jo_inside.getString("itemId"));
                                itemPublicSellerList.setReviewTitle(jo_inside.getString("name"));
                                itemPublicSellerList.setReviewImage(jo_inside.getString("principalImage"));
                                itemPublicSellerList.setReviewDescription(jo_inside.getString("description"));
                                itemPublicSellerList.setReviewPrice(jo_inside.getString("price"));

                                array_publicaciones.add(itemPublicSellerList);

                            }

                            setAdapterReviewList();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            //if (isAdded()) {
                                Toast.makeText(ActivityProductDetail.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ActivityProductDetail.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(ActivityProductDetail.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ActivityProductDetail.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(ActivityProductDetail.this).addToRequestQueue(postRequest);
        return array_publicaciones;

        /*ArrayList<ItemReview> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("review_list.json");
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
                ItemReview itemReview = new ItemReview();

                itemReview.setReviewUserName(jo_inside.getString("review_user"));
                itemReview.setReviewTime(jo_inside.getString("review_time"));
                itemReview.setReviewMessage(jo_inside.getString("review_message"));

                array_review.add(itemReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterReviewList();
        return array_review;*/
    }

    public void setAdapterReviewList() {

        adapter_review_public = new ReviewPublicListAdapter(ActivityProductDetail.this, array_publicaciones);
        recycler_detail_review.setAdapter(adapter_review_public);

    }

    private void showSelectColor() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_color_dialog);
        array_color = new ArrayList<>();
        recyclerView_color = (RecyclerView) mDialog.findViewById(R.id.vertical_color);
        recyclerView_color.setHasFixedSize(false);
        recyclerView_color.setNestedScrollingEnabled(false);
        recyclerView_color.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_color.addItemDecoration(itemDecoration);
        prepareColorData();

        recyclerView_color.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));

        mDialog.show();
    }

    private void prepareColorData() {
        String[] color = getResources().getStringArray(R.array.color_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectColor(color[k]);
            array_color.add(itemColorSize);
        }
        adapter_color = new SelectColorAdapter(this, array_color);
        recyclerView_color.setAdapter(adapter_color);

    }

    private void showSelectSize() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_size_dialog);
        array_size = new ArrayList<>();
        recyclerView_size = (RecyclerView) mDialog.findViewById(R.id.vertical_size);
        recyclerView_size.setHasFixedSize(false);
        recyclerView_size.setNestedScrollingEnabled(false);
        recyclerView_size.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_size.addItemDecoration(itemDecoration);
        prepareSizeData();

        recyclerView_size.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));
        mDialog.show();
    }

    private void prepareSizeData() {
        String[] color = getResources().getStringArray(R.array.size_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectSize(color[k]);
            array_size.add(itemColorSize);
        }
        adapter_size = new SelectSizeAdapter(ActivityProductDetail.this, array_size);
        recyclerView_size.setAdapter(adapter_size);


    }

    private void showOrderPlace() {
        Intent intent = new Intent(ActivityProductDetail.this, BuyActivity.class);
        intent.putExtra("itemId",itemTemp.getItemId());
        intent.putExtra("itemQuantity",Integer.parseInt(edit_quantity.getText().toString()));
        Log.e("Quantity:----", ""+edit_quantity.getText());
        startActivity(intent);

        /*mDialogPlaceOrder = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialogPlaceOrder.setContentView(R.layout.my_cart_dialog);
        array_color = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) mDialogPlaceOrder.findViewById(R.id.vertical_cart_product);
        text_product_con_shop = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_con_shop);
        text_product_place_order = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_place_order);
        txt_order_total_rs = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_rs);
        txt_order_item = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_item);
        recyclerView_order_place.setHasFixedSize(false);
        recyclerView_order_place.setNestedScrollingEnabled(false);
        recyclerView_order_place.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_order_place.addItemDecoration(itemDecoration);
        loadJSONFromAssetOrderPlace();
        text_product_con_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
            }
        });

        text_product_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
                Intent intent_order = new Intent(ActivityProductDetail.this, ActivityPlaceOrder.class);
                startActivity(intent_order);

            }
        });

        mDialogPlaceOrder.show();*/
    }

    public ArrayList<ItemOrderProduct> loadJSONFromAssetOrderPlace() {
        ArrayList<ItemOrderProduct> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("order_product_list.json");
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
                ItemOrderProduct itemOrderProduct = new ItemOrderProduct();

                itemOrderProduct.setOrderName(jo_inside.getString("order_title"));
                itemOrderProduct.setOrderImage(jo_inside.getString("order_image"));
                itemOrderProduct.setOrderSeller(jo_inside.getString("order_seller"));
                itemOrderProduct.setOrderPrice(jo_inside.getString("order_price"));
                itemOrderProduct.setOrderOfferPercentage(jo_inside.getString("order_offer"));
                itemOrderProduct.setOrderDiscountPrice(jo_inside.getString("order_discount"));
                itemOrderProduct.setOrderDate(jo_inside.getString("order_delivery_date"));
                itemOrderProduct.setOrderQuantity(jo_inside.getInt("order_quantity"));

                array_order_place.add(itemOrderProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterOrderList();
        return array_order_place;
    }

    public void setAdapterOrderList() {

        adapter_orderPlaceAdapter = new OrderPlaceAdapter(ActivityProductDetail.this, array_order_place);
        recyclerView_order_place.setAdapter(adapter_orderPlaceAdapter);

    }

    public class OrderPlaceAdapter extends RecyclerView.Adapter<OrderPlaceAdapter.ItemRowHolder> {

        private ArrayList<ItemOrderProduct> dataList;
        private Context mContext;

        public OrderPlaceAdapter(Context context, ArrayList<ItemOrderProduct> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final ItemRowHolder holder, final int position) {
            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            Picasso.with(mContext).load("file:///android_asset/image/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
            holder.text_order_title.setText(itemOrderProduct.getOrderName());
            holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());
            holder.text_order_price.setText(itemOrderProduct.getOrderPrice());
            holder.text_order_price_dic.setText(itemOrderProduct.getOrderDiscountPrice());
            holder.text_order_price_percentage.setText(itemOrderProduct.getOrderOfferPercentage());
            holder.text_order_time.setText(getResources().getString(R.string.delivery) + itemOrderProduct.getOrderDate());
            holder.text_integer_number.setText(String.valueOf(itemOrderProduct.getOrderQuantity()));
            holder.text_integer_number.setTag(String.valueOf(itemOrderProduct.getOrderQuantity()));

            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) - 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) + 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail = new Intent(mContext, ActivityProductDetail.class);
                    mContext.startActivity(intent_detail);
                }
            });

            holder.btn_remove_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogPlaceOrder.dismiss();
                    array_order_place.clear();
                }
            });


        }

        @Override
        public int getItemCount() {
            return (null != dataList ? dataList.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public ImageView image_item_order_image;
            public TextView text_integer_number, text_order_title, text_order_seller, text_order_price, text_order_price_dic, text_order_price_percentage,
                    text_order_time;
            public LinearLayout lyt_parent;
            public Button btn_remove_order, btn_plus, btn_minus;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image_item_order_image = (ImageView) itemView.findViewById(R.id.image_item_order_image);
                text_integer_number = (TextView) itemView.findViewById(R.id.integer_number);
                text_order_title = (TextView) itemView.findViewById(R.id.text_order_title);
                text_order_seller = (TextView) itemView.findViewById(R.id.text_order_seller);
                text_order_price = (TextView) itemView.findViewById(R.id.text_order_price);
                text_order_price_dic = (TextView) itemView.findViewById(R.id.text_order_price_dic);
                text_order_price_percentage = (TextView) itemView.findViewById(R.id.text_order_price_percentage);
                text_order_time = (TextView) itemView.findViewById(R.id.text_order_time);
                btn_remove_order = (Button) itemView.findViewById(R.id.btn_remove_order);
                lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
                btn_plus = (Button) itemView.findViewById(R.id.increase);
                btn_minus = (Button) itemView.findViewById(R.id.decrease);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;

            case R.id.menu_fav:

                Toast.makeText(ActivityProductDetail.this, getResources().getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new OnSwipeTouchListener.GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    public void RunLoginService()
    {

        //progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (
                        Request.Method.POST,
                        Network.API_URL+"/item/ItemById/"+itemId,
                        null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try
                                {
                                    Log.e("daniel:------",response.toString());
                                    Gson gson=new GsonBuilder().create();

                                    //Yng_Item itemTemp=gson.fromJson(response.toString(),Yng_Item.class);
                                    itemTemp=gson.fromJson(response.toString(),Yng_Item.class);
                                    String s=response.getString("description");
                                    try {
                                        Log.e("daniel description:",itemTemp.getDescription());
                                    }catch (Exception e){
                                        Log.e("daniel description:","sin descripcion");
                                    }

                                    userTemp=itemTemp.getUser();
                                    itemSeller = userTemp.getUsername();
                                    Log.e("seller",""+itemSeller);

                                    if(itemTemp.getType().equals("Product") || itemTemp.getType().equals("Motorized")){
                                        text_product_buy.setText("Comprar");
                                    }else{
                                        text_product_buy.setText("Consultar");
                                    }


                                    setData(itemTemp);
                                }
                                catch (Exception ex)
                                {
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
                                //Toast.makeText(actib.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(ActivityProductDetail.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            new AlertDialog.Builder(ActivityProductDetail.this)
                                    .setTitle("Algo Salio mal")
                                    .setMessage("Usuario o contraseña incorrecto")
                                    .setCancelable(true)
                                    .show();
                            //Toast.makeText(ActivityLogin.this, error != null && error.getMessage() != null ? error.getMessage() : "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", Network.API_KEY);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag("");

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }
    public void setData(Yng_Item itemTemp){
        Log.e("getName:",""+itemTemp.getName());
        //Typeface typeface = Typeface.createFromAsset(ActivityProductDetail.this.getAssets(), "fonts/"+"Roboto-Light.ttf");
        //text_product_price.setTypeface(typeface);
        text_product_title.setText(itemTemp.getName());
        text_product_price.setText("$  "+itemTemp.getPrice());
        if(itemTemp.getProductPagoEnvio().equals("gratis")) text_no_cost.setText("Envío gratis a todo el país");
        else {text_no_cost.setText("Envío a todo el país");}
        // text_no_cost.setText(itemTemp.get);
        //text_product_rate.setText("4.8");
        try{
            web_desc.setText(itemTemp.getDescription());
        }catch (Exception e){
            web_desc.setText("");
        }
    }

}
