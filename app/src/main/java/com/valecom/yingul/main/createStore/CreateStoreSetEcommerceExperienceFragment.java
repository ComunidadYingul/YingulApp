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
public class CreateStoreSetEcommerceExperienceFragment extends Fragment {

    private ListView list;
    private ArrayAdapter adapter;
    private ArrayList array_list;
    private TextView textTitle;

    public CreateStoreSetEcommerceExperienceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_entry, container, false);
        array_list = new ArrayList();
        array_list.add("Menos de 1 año");
        array_list.add("De 1 a 2 años");
        array_list.add("De 3 a 5 años");
        array_list.add("Más de 5 años");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        textTitle = (TextView) v.findViewById(R.id.textTitle);
        textTitle.setText("¿Tienes experiencia con e-commerce?");
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position){
                    case 0:
                        ((CreateStoreActivity)getActivity()).store.setEcommerceExperience("-1");
                        break;
                    case 1:
                        ((CreateStoreActivity)getActivity()).store.setEcommerceExperience("1-2");
                        break;
                    case 2:
                        ((CreateStoreActivity)getActivity()).store.setEcommerceExperience("3-5");
                        break;
                    case 3:
                        ((CreateStoreActivity)getActivity()).store.setEcommerceExperience("+5");
                        break;
                }
                CreateStoreSetWebSiteFragment fragment = new CreateStoreSetWebSiteFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
