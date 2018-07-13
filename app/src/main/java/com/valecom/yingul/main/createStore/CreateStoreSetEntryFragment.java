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
import com.valecom.yingul.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStoreSetEntryFragment extends Fragment {

    private ListView list;
    private ArrayAdapter adapter;
    private ArrayList array_list;

    public CreateStoreSetEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_entry, container, false);
        array_list = new ArrayList();
        array_list.add("Autos, Motos y Otros");
        array_list.add("Inmuebles");
        array_list.add("Servicios");
        array_list.add("Productos");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, array_list);

        list = (ListView) v.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position){
                    case 0:
                        ((CreateStoreActivity)getActivity()).store.setItemsType("Motorized");
                        ((CreateStoreActivity)getActivity()).store.setMainCategory(null);
                        CreateStoreSetEmployesQuantityFragment fragment = new CreateStoreSetEmployesQuantityFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        ((CreateStoreActivity)getActivity()).store.setItemsType("Property");
                        ((CreateStoreActivity)getActivity()).store.setMainCategory(null);
                        CreateStoreSetEmployesQuantityFragment fragment1 = new CreateStoreSetEmployesQuantityFragment();
                        FragmentTransaction fragmentTransaction1 = getFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.content_frame, fragment1);
                        fragmentTransaction1.addToBackStack(null);
                        fragmentTransaction1.commit();
                        break;
                    case 2:
                        ((CreateStoreActivity)getActivity()).store.setItemsType("Service");
                        ((CreateStoreActivity)getActivity()).store.setMainCategory(null);
                        CreateStoreSetEmployesQuantityFragment fragment2 = new CreateStoreSetEmployesQuantityFragment();
                        FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.content_frame, fragment2);
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();
                        break;
                    case 3:
                        ((CreateStoreActivity)getActivity()).store.setItemsType("Product");
                        CreateStoreSetCategoryFragment fragment3 = new CreateStoreSetCategoryFragment();
                        FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.content_frame, fragment3);
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();
                        break;
                }
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
