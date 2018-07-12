package com.valecom.yingul.main.myAccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.Item.ItemGallery;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.GalleryAdapter;
import com.valecom.yingul.main.ActivityGalleryDetail;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.categories.SubCategoryFragment;
import com.valecom.yingul.main.edit.EditItemDescriptionFragment;
import com.valecom.yingul.main.edit.EditItemImageFragment;
import com.valecom.yingul.main.edit.EditItemPriceFragment;
import com.valecom.yingul.main.edit.EditItemQuantityFragment;
import com.valecom.yingul.main.edit.EditItemTitleFragment;
import com.valecom.yingul.main.sell.SellItemAddSummaryFragment;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAccountPublicationDetailFragment extends Fragment
{
    ImageView ImgDetail,imgEnvioGratis;
    RecyclerView recyclerViewDetail;
    GalleryAdapter adapter_gallery;
    ItemGallery itemGalleryList;
    ArrayList<ItemGallery> array_gallery;
    String itemId;
    /***************************/

    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Yng_Item item;

    private ImageView principalImage, imgEditImage,imgEditTitle,imgEditDescription,imgEditPrice,imgEditQuantity;
    private TextView txtItemName,txtCurrencyPrice,txtDescription,txtQuantity,textPriceNormal,textMoneyNormal,textMoney,textDiscountPorcent;
    private Button btnItemDetail;
    private LinearLayout lytDiscount,lytPriceNormal;

    private MaterialDialog setting_address_edit_dialog;

    public static final String TAG = "PurchaseDetailFragment";
    public MyAccountPublicationDetailFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountPublicationDetailFragment newInstance(String param1, String param2)
    {
        MyAccountPublicationDetailFragment fragment = new MyAccountPublicationDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_account_publication_detail, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(getActivity(), LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
        }

        final Bundle bundle = getArguments();
        Gson gson = new Gson();
        item = gson.fromJson(bundle.getString("item"), Yng_Item.class);
        itemId = String.valueOf(item.getItemId());

        ImgDetail = (ImageView) view.findViewById(R.id.image_product_image);
        recyclerViewDetail = (RecyclerView) view.findViewById(R.id.vertical_detail);
        array_gallery = new ArrayList<>();

        //principalImage = (ImageView) view.findViewById(R.id.principalImage);
        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtCurrencyPrice = (TextView) view.findViewById(R.id.txtCurrencyPrice);
        textPriceNormal = (TextView) view.findViewById(R.id.textPriceNormal);
        textMoneyNormal = (TextView) view.findViewById(R.id.textMoneyNormal);
        textMoney = (TextView) view.findViewById(R.id.textMoney);
        textDiscountPorcent = (TextView) view.findViewById(R.id.textDiscountPorcent);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        btnItemDetail = (Button) view.findViewById(R.id.btnItemDetail);
        imgEditTitle = (ImageView) view.findViewById(R.id.imgEditTitle);
        imgEditDescription = (ImageView) view.findViewById(R.id.imgEditDescription);
        imgEditPrice = (ImageView) view.findViewById(R.id.imgEditPrice);
        imgEditImage = (ImageView) view.findViewById(R.id.imgEditImage);
        imgEditQuantity = (ImageView) view.findViewById(R.id.imgEditQuantity);
        lytPriceNormal = (LinearLayout) view.findViewById(R.id.lytPriceNormal);
        lytDiscount = (LinearLayout) view.findViewById(R.id.lytDiscount);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        final Bundle bundleEdit = new Bundle();
        bundleEdit.putLong("itemId",item.getItemId());

        //RunLoginService();
        loadJSONFromAssetGallery();

        btnItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                intent_detail.putExtra("itemId",String.valueOf(item.getItemId()));
                startActivity(intent_detail);
            }
        });

        imgEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemTitleFragment fragment = new EditItemTitleFragment();
                bundleEdit.putString("data",txtItemName.getText().toString());
                fragment.setArguments(bundleEdit);
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemDescriptionFragment fragment = new EditItemDescriptionFragment();
                bundleEdit.putString("data",txtDescription.getText().toString());
                fragment.setArguments(bundleEdit);
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgEditPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemPriceFragment fragment = new EditItemPriceFragment();
                bundleEdit.putString("data",txtCurrencyPrice.getText().toString());
                bundleEdit.putString("itemType",item.getType());
                fragment.setArguments(bundleEdit);
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgEditQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemQuantityFragment fragment = new EditItemQuantityFragment();
                bundleEdit.putString("data",txtQuantity.getText().toString());
                fragment.setArguments(bundleEdit);
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        imgEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditItemImageFragment fragment = new EditItemImageFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void setAdapterGalleryList() {

        Log.e("Eddy","segundo");
        adapter_gallery = new GalleryAdapter(getContext(), array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        itemGalleryList = array_gallery.get(0);
        Picasso.with(getContext()).load(Network.BUCKET_URL + itemGalleryList.getGalleryImage()).into(ImgDetail);

        recyclerViewDetail.addOnItemTouchListener(new ActivityProductDetail.RecyclerTouchListener(getContext(), recyclerViewDetail, new ActivityProductDetail.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemGalleryList = array_gallery.get(position);
                Picasso.with(getContext()).load(Network.BUCKET_URL + itemGalleryList.getGalleryImage()).into(ImgDetail);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ImgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(getContext(), ActivityGalleryDetail.class);
                intent_gallery.putExtra("itemId",itemId);
                startActivity(intent_gallery);
            }
        });

        RunLoginService();

    }

    /*******************************************************************************************************/
    public ArrayList<ItemGallery> loadJSONFromAssetGallery() {

        //progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "item/Image/"+itemId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            ItemGallery itemGalleryList = new ItemGallery();

                            itemGalleryList.setGalleryImage(item.getPrincipalImage());
                            Log.e("principalImage:--",item.getPrincipalImage());

                            array_gallery.add(itemGalleryList);

                            JSONArray m_jArry = response;
                            Log.e("Eddy_array---",m_jArry.toString());
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
                            Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                        }

                        /*if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }*/
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
        //RequestQueue queue = MySingleton.getInstance(getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);

        return array_gallery;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(item.getName());
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
        RunLoginService();
        Log.e("gonzalo:---","onResume");
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("gonzalo:---","onStart");
        //RunLoginService();
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void RunLoginService()
    {

        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (
                        Request.Method.POST,
                        Network.API_URL+"/item/ItemById/"+item.getItemId(),
                        null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                Yng_Item itemTemp;

                                try
                                {
                                    Log.e("daniel:------",response.toString());
                                    Gson gson=new GsonBuilder().create();

                                    itemTemp=gson.fromJson(response.toString(),Yng_Item.class);

                                    txtItemName.setText(itemTemp.getName());
                                    txtCurrencyPrice.setText(String.format("%.0f",itemTemp.getPrice()));
                                    textPriceNormal.setText(String.format("%.0f",itemTemp.getPriceNormal()));
                                    textPriceNormal.setPaintFlags(textPriceNormal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    if (itemTemp.getMoney().equals("USD")) {
                                        textMoney.setText("U$D");
                                        textMoneyNormal.setText("U$D");
                                    }else{
                                        textMoney.setText("$");
                                        textMoneyNormal.setText("$");
                                    }
                                    if(item.getPriceDiscount()==0){
                                        lytDiscount.setVisibility(View.GONE);
                                        lytPriceNormal.setVisibility(View.GONE);
                                    }else{
                                        Double desc = ((item.getPriceNormal()-item.getPriceDiscount()) * 100)/item.getPriceNormal();
                                        textDiscountPorcent.setText(String.format("%.0f", desc));
                                        lytDiscount.setVisibility(View.VISIBLE);
                                        lytPriceNormal.setVisibility(View.VISIBLE);
                                    }
                                    txtDescription.setText(itemTemp.getDescription());
                                    txtQuantity.setText(String.valueOf(itemTemp.getQuantity()));
                                    //Picasso.with(getActivity()).load(Network.BUCKET_URL+itemTemp.getPrincipalImage()).into(principalImage);

                                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(itemTemp.getName());

                                    progressDialog.dismiss();


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
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            new AlertDialog.Builder(getContext())
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
        RequestQueue queue = MySingleton.getInstance(getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag("");

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }

}
