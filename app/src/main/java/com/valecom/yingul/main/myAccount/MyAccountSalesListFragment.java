package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.BuyAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Buy;
import com.valecom.yingul.model.Yng_Card;
import com.valecom.yingul.model.Yng_CashPayment;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Payment;
import com.valecom.yingul.model.Yng_Shipping;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAccountSalesListFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;
    private String username;

    private ListView list;
    private BuyAdapter adapter;
    private ArrayList<Yng_Buy> array_list;

    public MyAccountSalesListFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountSalesListFragment newInstance(String param1, String param2)
    {
        MyAccountSalesListFragment fragment = new MyAccountSalesListFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_account_sales_list, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(getActivity(), LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
        }else{
            username = settings.getString("username","");
        }

        array_list = new ArrayList<Yng_Buy>();
        adapter = new BuyAdapter(getContext(), array_list);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_Buy buy = adapter.getItem(position);

                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String jsonBody = gson.toJson(buy);
                Log.e("ITEM:---",jsonBody);
                bundle.putString("buy",jsonBody);

                MyAccountSaleDetailFragment fragment = new MyAccountSaleDetailFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        RunGetQueriesByUser();

        return view;
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ventas");
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void RunGetQueriesByUser()
    {
        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "buy/getSalesByUser/"+username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            Log.e("compras por usuario",response.toString());
                            JSONArray buys = response;
                            array_list.clear();
                            for (int i = 0; i < buys.length(); i++) {
                                JSONObject obj = buys.getJSONObject(i);
                                Yng_Buy buy = new Yng_Buy();
                                buy.setBuyId(obj.optLong("buyId"));
                                buy.setCost(obj.optDouble("cost"));
                                buy.setItemCost(obj.optDouble("itemCost"));
                                buy.setShippingCost(obj.optDouble("shippingCost"));
                                buy.setTime(obj.optString("time"));
                                buy.setQuantity(obj.optInt("quantity"));
                                Yng_Item item = new Yng_Item();
                                Gson gson = new Gson();
                                item = gson.fromJson(String.valueOf(obj.optJSONObject("yng_item")), Yng_Item.class);
                                Yng_Shipping shipping = new Yng_Shipping();
                                shipping = gson.fromJson(String.valueOf(obj.optJSONObject("shipping")), Yng_Shipping.class);
                                Yng_Payment payment = new Yng_Payment();
                                JSONObject paymentObj = obj.optJSONObject("yng_Payment");
                                payment.setType(paymentObj.optString("type"));
                                if(payment.getType().equals("CARD")){
                                    Yng_Card card = new Yng_Card();
                                    card = gson.fromJson(String.valueOf(paymentObj.optJSONObject("yng_Card")), Yng_Card.class);
                                    payment.setYng_Card(card);
                                }else{
                                    JSONObject cashPaymentObj = paymentObj.optJSONObject("cashPayment");
                                    Log.e("cash",String.valueOf(cashPaymentObj));
                                    Yng_CashPayment cashPayment = new Yng_CashPayment();
                                    cashPayment.setCashPaymentId(cashPaymentObj.optLong("cashPaymentId"));
                                    cashPayment.setURL_PAYMENT_RECEIPT_HTML(cashPaymentObj.optString("url_PAYMENT_RECEIPT_HTML"));
                                    cashPayment.setURL_PAYMENT_RECEIPT_PDF(cashPaymentObj.optString("url_PAYMENT_RECEIPT_PDF"));
                                    cashPayment.setPaymentMethod(cashPaymentObj.optString("paymentMethod"));
                                    payment.setCashPayment(cashPayment);
                                }
                                buy.setShipping(shipping);
                                buy.setYng_Payment(payment);
                                buy.setYng_item(item);
                                array_list.add(buy);
                            }

                            adapter.notifyDataSetChanged();
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
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
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
        //RequestQueue queue = MySingleton.getInstance( getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }

}
