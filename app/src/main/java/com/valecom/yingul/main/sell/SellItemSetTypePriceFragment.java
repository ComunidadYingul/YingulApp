package com.valecom.yingul.main.sell;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetTypePriceFragment extends Fragment {

    ListView list;
    ArrayAdapter adapter;
    ArrayList array_list;
    MaterialDialog progressDialog;

    public SellItemSetTypePriceFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sell_item_set_type_price, container, false);

        array_list = new ArrayList();
        array_list.add("Con precio total");
        array_list.add("Con precio a convenir");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ((SellActivity)getActivity()).setTypePrice(""+position);
            }
        });


        ////

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }



}
