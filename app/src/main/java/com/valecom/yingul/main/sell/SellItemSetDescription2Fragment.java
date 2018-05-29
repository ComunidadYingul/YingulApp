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
public class SellItemSetDescription2Fragment extends Fragment {

    Button buttonSetDescription2;
    EditText editDescription2;

    public SellItemSetDescription2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_description2, container, false);
        editDescription2 = (EditText) v.findViewById(R.id.editDescription2);
        buttonSetDescription2 = (Button) v.findViewById(R.id.buttonSetDescription2);

        buttonSetDescription2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).aditionalDescription=editDescription2.getText().toString().trim();
                ((SellActivity)getActivity()).setItemAditionalDescription();

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
