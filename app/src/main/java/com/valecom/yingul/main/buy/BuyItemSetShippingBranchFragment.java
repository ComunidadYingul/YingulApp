package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QuoteAdapter;
import com.valecom.yingul.model.Yng_Branch;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Quote;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemSetShippingBranchFragment extends Fragment {

    ListView list;
    QuoteAdapter adapter;
    ArrayList<Yng_Quote> array_list;
    MaterialDialog progressDialog;

    public BuyItemSetShippingBranchFragment() {
        // Required empty public constructor
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_set_shipping_branch, container, false);

        array_list = new ArrayList<Yng_Quote>();
        adapter = new QuoteAdapter(getContext(), array_list);

        list = (ListView) v.findViewById(R.id.list);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_Quote item = adapter.getItem(position);
                ((BuyActivity)getActivity()).quote=item;
                BuyItemSetWhoWithdrewPurchaseFragment fragment = new BuyItemSetWhoWithdrewPurchaseFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        try {
            setQuotesList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setQuotesList() throws JSONException {

        JSONArray items = ((BuyActivity)getActivity()).quotes;
        Log.e("Eddy",items.toString());
        array_list.clear();
        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);
            Yng_Quote item = new Yng_Quote();
            item.setQuoteId(obj.getLong("quoteId"));
            item.setRate(obj.getDouble("rate"));
            item.setRateOrigin(obj.getDouble("rateOrigin"));
            item.setQuantity(obj.getInt("quantity"));
            item.setRespuesta(obj.getString("respuesta"));
            Yng_Branch branch = new Yng_Branch();
            Gson gson = new Gson();
            branch = gson.fromJson(String.valueOf(obj.getJSONObject("yng_Branch")) , Yng_Branch.class);
            item.setYng_Branch(branch);
            Yng_Item newItem = new Yng_Item();
            newItem = gson.fromJson(String.valueOf(obj.getJSONObject("yng_Item")) , Yng_Item.class);
            item.setYng_Item(newItem);
            String jsonBody = gson.toJson(item);
            Log.e("quote return", jsonBody);
            array_list.add(item);
        }

        adapter.notifyDataSetChanged();
    }
}