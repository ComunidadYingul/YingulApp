package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetDescription1Fragment extends Fragment {

    Button buttonSetDescription1;
    EditText editDescription1;

    public SellItemSetDescription1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_description1, container, false);
        editDescription1 = (EditText) v.findViewById(R.id.editDescription1);
        buttonSetDescription1 = (Button) v.findViewById(R.id.buttonSetDescription1);

        buttonSetDescription1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).notInclude=editDescription1.getText().toString();
                SellItemSetDescription2Fragment fragment = new SellItemSetDescription2Fragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
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
