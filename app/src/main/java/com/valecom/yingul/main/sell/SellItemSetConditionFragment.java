package com.valecom.yingul.main.sell;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class SellItemSetConditionFragment extends Fragment {

    ListView list;
    ArrayAdapter adapter;
    ArrayList array_list;
    MaterialDialog progressDialog;
    private JSONObject api_parameter;

    public SellItemSetConditionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        View v = inflater.inflate(R.layout.fragment_sell_item_set_condition, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        array_list = new ArrayList();
        array_list.add("Nuevo");
        array_list.add("Usado");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ((SellActivity)getActivity()).setTypeCondition(""+position);
            }
        });

        api_parameter = new JSONObject();
        try
        {
            api_parameter.put("user_id", settings.getLong("id", 0));
        }
        catch(JSONException ex) {}

        ////

        return v;
    }

}
