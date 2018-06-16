package com.valecom.yingul.main.myAccount.yingulPay;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.ItemOffsetDecoration;
import com.valecom.yingul.adapter.CategoryListAdapter;
import com.valecom.yingul.adapter.TransactionListAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Transaction;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionCashFragment extends Fragment {

    RecyclerView recycler_transaction;
    ArrayList<Yng_Transaction> array_list;
    TransactionListAdapter adapter_transaction;
    private MaterialDialog progressDialog;

    String authorization,username;

    public TransactionCashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transaction_cash, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
        username = settings.getString("username","");
        authorization = settings.getString("password","");
        Log.e("authorization",username+" "+authorization);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        array_list = new ArrayList<>();

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);

        recycler_transaction = (RecyclerView) rootView.findViewById(R.id.rv_transaction_cash);
        recycler_transaction.setHasFixedSize(false);
        recycler_transaction.setNestedScrollingEnabled(false);
        recycler_transaction.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_transaction.addItemDecoration(itemDecoration);

        getRunTransaction();

        return rootView;
    }

    /***************************** ALL OVERS ********************************/

    public ArrayList<Yng_Transaction> getRunTransaction() {

        progressDialog.show();

        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "account/getTransactionsByUser/"+username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {

                            JSONArray m_jArry = response;
                            Log.e("Transaction:--",m_jArry.toString());
                            Log.e("Items", m_jArry.length()+"");
                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);

                                Yng_Transaction item = new Yng_Transaction();
                                item.setTransactionId(jo_inside.getLong("transactionId"));
                                item.setDay(jo_inside.getInt("day"));
                                item.setMonth(jo_inside.getInt("month"));
                                item.setYear(jo_inside.getInt("year"));
                                item.setSecond(jo_inside.getInt("second"));
                                item.setMinute(jo_inside.getInt("minute"));
                                item.setHour(jo_inside.getInt("hour"));
                                item.setType(jo_inside.getString("type"));
                                item.setCurrency(jo_inside.getString("currency"));
                                item.setAmount(jo_inside.getDouble("amount"));
                                item.setDescription(jo_inside.getString("description"));
                                Log.e("Items",item.getDay()+"");
                                array_list.add(item);


                            }
                            setAdapterTransaction();

                            /**/
                            //JSONObject result = ((JSONObject)response.get("data"));
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Log.e("error","1");
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
                        Log.e("error","2");
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Log.e("error","3");
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.e("error","4");
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
                params.put("Authorization", authorization);
                /*params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (settings.getString("email","")+":" + settings.getString("api_key","")).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
        return array_list;

    }

    public void setAdapterTransaction() {
        Log.e("error","llego a adapter");
        adapter_transaction = new TransactionListAdapter(getActivity(), array_list);
        recycler_transaction.setAdapter(adapter_transaction);

    }

}
