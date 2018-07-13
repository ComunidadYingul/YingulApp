package com.valecom.yingul.main.createStore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.valecom.yingul.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStoreSetTraficInvestFragment extends Fragment {

    private ListView list;
    private ArrayAdapter adapter;
    private ArrayList array_list;
    private TextView textTitle;

    public CreateStoreSetTraficInvestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_entry, container, false);
        array_list = new ArrayList();
        array_list.add("Si");
        array_list.add("No");
        array_list.add("Necesito ayuda para hacerlo");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        textTitle = (TextView) v.findViewById(R.id.textTitle);
        textTitle.setText("Inveritís para conseguir tu tráfico");
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position){
                    case 0:
                        ((CreateStoreActivity)getActivity()).store.setTraficInvest("yes");
                        break;
                    case 1:
                        ((CreateStoreActivity)getActivity()).store.setTraficInvest("no");
                        break;
                    case 2:
                        ((CreateStoreActivity)getActivity()).store.setTraficInvest("needHelp");
                        break;
                }
                ((CreateStoreActivity)getActivity()).createStore();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Checks to make sure fragment is still attached to activity
        /*if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Envio");
        }*/
    }
}
