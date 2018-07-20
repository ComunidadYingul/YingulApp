package com.valecom.yingul.main.edit;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.Util.MyApplication;
import com.valecom.yingul.adapter.ItemImageAdapter;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_ItemImage;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class EditImageActivity extends AppCompatActivity {

    RecyclerView recyclerViewDetail;
    ArrayList<Yng_ItemImage> array_gallery;
    ItemImageAdapter adapter_gallery;
    Yng_ItemImage itemGalleryList;
    private MyApplication myApplication;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    ImageView image_slider;
    private int positionImg;
    private String principalImage;
    private LinearLayout layoutMore;
    private MaterialDialog setting_address_edit_dialog;
    private MaterialDialog progressDialog;
    String itemId="2783";

    private ListView list;
    private ArrayAdapter adapter1;
    private ArrayList array_list;
    static final int ADD_PICTURES_TAG = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private Button buttonReturnImages;
    private Yng_Item item;
    private Yng_User user;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    String TAG="OkHttpConection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_edit_image);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = new Yng_User();
        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }else{
            user.setUsername(settings.getString("username",""));
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
        }

        array_list = new ArrayList();
        array_list.add("Tomar foto");
        array_list.add("Elegir existente");
        adapter1 = new ArrayAdapter<String>(EditImageActivity.this,android.R.layout.simple_list_item_1, array_list);

        itemId = getIntent().getStringExtra("itemId");
        principalImage = getIntent().getStringExtra("principalImage");
        positionImg = 0;
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        item = new Yng_Item();
        array_gallery = new ArrayList<>();
        viewpager_slider = (ViewPager) findViewById(R.id.viewPager);
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        layoutMore = (LinearLayout) findViewById(R.id.layoutMore);
        buttonReturnImages = (Button) findViewById(R.id.buttonReturnImages);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(EditImageActivity.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(EditImageActivity.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        loadJSONFromAssetGallery();

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(EditImageActivity.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                positionImg=position;
                viewpager_slider.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_address_edit_dialog = new MaterialDialog.Builder(EditImageActivity.this)
                        .customView(R.layout.type_upload_image_layout, true)
                        .cancelable(true)
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                View view = setting_address_edit_dialog.getCustomView();
                                list = (ListView) view.findViewById(R.id.list);
                                // Assigning the adapter to ListView
                                list.setAdapter(adapter1);

                                list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        switch (position){
                                            case 0:
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (checkSelfPermission(Manifest.permission.CAMERA)
                                                            != PackageManager.PERMISSION_GRANTED) {
                                                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                                MY_CAMERA_REQUEST_CODE);
                                                    }else{
                                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                        }
                                                    }
                                                }else{
                                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                    }
                                                }
                                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                                    setting_address_edit_dialog.dismiss();
                                                }
                                                break;
                                            case 1:
                                                new MaterialDialog.Builder(EditImageActivity.this)
                                                        .title("Importante")
                                                        .content("Presione prolongadamente para agregar mas de una foto")
                                                        .positiveText(R.string.ok)
                                                        .negativeText("CANCELAR")
                                                        .cancelable(false)
                                                        .negativeColorRes(R.color.colorAccent)
                                                        .positiveColorRes(R.color.colorAccent)
                                                        .callback(new MaterialDialog.ButtonCallback()
                                                        {
                                                            @Override
                                                            public void onPositive(MaterialDialog dialog)
                                                            {
                                                                Intent intent = new Intent();
                                                                intent.setType("image/*");
                                                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADD_PICTURES_TAG);
                                                                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing())
                                                                {
                                                                    setting_address_edit_dialog.dismiss();
                                                                }
                                                            }

                                                            @Override
                                                            public void onNegative(MaterialDialog dialog)
                                                            {
                                                                //Cancel
                                                                dialog.dismiss();

                                                                if (dialog != null && dialog.isShowing())
                                                                {
                                                                    // If the response is JSONObject instead of expected JSONArray
                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                        })
                                                        .show();
                                                break;
                                        }
                                    }
                                });

                            }
                        })
                        .show();
            }
        });
        buttonReturnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(array_gallery.isEmpty()){
                    Toast.makeText(EditImageActivity.this, "Debes adicionar por lo menos una imagen.", Toast.LENGTH_LONG).show();
                }else{
                    Set<Yng_ItemImage> itemImageList = new HashSet<>();
                    for (int i=0;i<array_gallery.size();i++) {
                        if(i==0){
                            item.setPrincipalImage("data:image/jpeg;base64,"+array_gallery.get(i).getImage());
                        }else{
                            Yng_ItemImage itemImage = new Yng_ItemImage();
                            itemImage.setImage("data:image/jpeg;base64," + array_gallery.get(i).getImage());
                            itemImageList.add(itemImage);
                        }
                    }
                    item.setItemId(Long.valueOf(itemId));
                    item.setItemImage(itemImageList);

                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(item);
                    Log.e("item final", jsonBody);
                    requestArrayPost(Network.API_URL + "item/updateImages",jsonBody);
                    /*Intent returnIntent = new Intent();
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(item);
                    Log.e("item envio",jsonBody);
                    returnIntent.putExtra("item", jsonBody);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();*/
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);

        return true;
    }

    public ArrayList<Yng_ItemImage> loadJSONFromAssetGallery() {

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/Image/"+itemId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            Yng_ItemImage itemGalleryList = new Yng_ItemImage();


                            Bitmap bitmap = getBitmapFromUrl(Network.BUCKET_URL+principalImage);
                            itemGalleryList.setImage(encodeImage(bitmap));

                            array_gallery.add(itemGalleryList);

                            JSONArray m_jArry = response;
                            Log.e("Eddy",m_jArry.toString());
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);
                                itemGalleryList = new Yng_ItemImage();

                                bitmap = getBitmapFromUrl(Network.BUCKET_URL+jo_inside.getString("image"));
                                itemGalleryList.setImage(encodeImage(bitmap));

                                array_gallery.add(itemGalleryList);
                            }

                            setAdapterGalleryList();
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(EditImageActivity.this, R.string.error_try_again_support, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(EditImageActivity.this, json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(EditImageActivity.this, R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EditImageActivity.this, error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(EditImageActivity.this).addToRequestQueue(postRequest);

        return array_gallery;
    }

    public void setAdapterGalleryList() {

        adapter_gallery = new ItemImageAdapter(EditImageActivity.this, array_gallery);
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

            image_slider.setImageBitmap(StringToBitMap(itemGalleryList.getImage()));


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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Log.e("Select menu",id+"comparado con"+android.R.id.home);
        if (id == R.id.menu_delete)
        {
            if(array_gallery.size()>0){
                Log.e("sen envia esto",""+array_gallery.size()+"position"+positionImg);
                array_gallery.remove(positionImg);
                positionImg=0;
                setAdapterGalleryList();
            }
        }
        else if (id == R.id.menu_rotate)
        {
            Yng_ItemImage itemImage = array_gallery.get(positionImg);
            Bitmap bmap = StringToBitMap(itemImage.getImage());
            Bitmap bmap2 = rotateBitmap(bmap,90);
            itemImage.setImage(encodeImage(bmap2));
            array_gallery.set(positionImg,itemImage);
            setAdapterGalleryList();
            viewpager_slider.setCurrentItem(positionImg);
            Log.e("sen envia esto","rotte");
        }
        else if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image, Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public static Bitmap getBitmapFromUrl(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bitmapByteCount=android.support.v4.graphics.BitmapCompat.getAllocationByteCount(bm);
        int byteCount=bitmapByteCount/1024;
        Log.e("tamaño byte",""+bitmapByteCount/1024);
        if(byteCount>0 && byteCount<600) {
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        }
        if(byteCount>=600 && byteCount<1800) {
            bm.compress(Bitmap.CompressFormat.JPEG,90,baos);
        }
        if(byteCount>=1800 && byteCount<3600) {
            bm.compress(Bitmap.CompressFormat.JPEG,80,baos);
        }
        if(byteCount>=3600 && byteCount<10800) {
            bm.compress(Bitmap.CompressFormat.JPEG,60,baos);
        }
        if(byteCount>=10800 && byteCount<32400) {
            bm.compress(Bitmap.CompressFormat.JPEG,40,baos);
        }
        if(byteCount>=32400 && byteCount<40000) {
            bm.compress(Bitmap.CompressFormat.JPEG,20,baos);
        }
        if(byteCount>=40000 && byteCount<50000) {
            bm.compress(Bitmap.CompressFormat.JPEG,10,baos);
        }
        if(byteCount>=50000) {
            bm.compress(Bitmap.CompressFormat.JPEG,1,baos);
        }

        byte[] b = baos.toByteArray();
        String encImage;
        encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
        /*Canvas canvas = new Canvas(rotatedBitmap);
        canvas.drawBitmap(original, 5.0f, 0.0f, null);*/

        return rotatedBitmap;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case ADD_PICTURES_TAG:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    Log.e("entro", "donde devia");
                    if (null != data) { // checking empty selection
                        if (null != data.getClipData()) { // checking multiple selection or not
                            Set<Yng_ItemImage> itemImageList = new HashSet<>();
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                final InputStream imageStream;
                                try {
                                    imageStream = getContentResolver().openInputStream(uri);
                                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                    String encodedImage = encodeImage(selectedImage);

                                    Yng_ItemImage itemGalleryList = new Yng_ItemImage();
                                    itemGalleryList.setImage(encodedImage);
                                    array_gallery.add(itemGalleryList);
                                    setAdapterGalleryList();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(uri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                String encodedImage = encodeImage(selectedImage);
                                Yng_ItemImage itemGalleryList = new Yng_ItemImage();
                                itemGalleryList.setImage(encodedImage);
                                array_gallery.add(itemGalleryList);
                                setAdapterGalleryList();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        /*que hacer despues de mandar la nueva foto*/

                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String encodedImage = encodeImage(imageBitmap);
                    Yng_ItemImage itemGalleryList = new Yng_ItemImage();
                    itemGalleryList.setImage(encodedImage);
                    array_gallery.add(itemGalleryList);
                    setAdapterGalleryList();
                }
                break;
        }
    }



    public void  requestArrayPost(String url, String json){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",user.getPassword())
                .post(body)
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
                        if(responce.equals("save")) {
                            Intent intent_gallery = new Intent(EditImageActivity.this, ActivityProductDetail.class);
                            intent_gallery.putExtra("itemId",itemId);
                            intent_gallery.putExtra("item",item);
                            startActivity(intent_gallery);
                            /*Intent returnIntent = new Intent();
                            Gson gson = new Gson();
                            String jsonBody = gson.toJson(item);
                            returnIntent.putExtra("item", jsonBody);
                            setResult(Activity.RESULT_OK, returnIntent);*/
                            finish();
                        }else{
                            Toast.makeText(EditImageActivity.this,"No se guardo",Toast.LENGTH_LONG).show();
                        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                    setting_address_edit_dialog.dismiss();
                }
            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
}
