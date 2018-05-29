package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemUbicationSetDetailFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editStreet,editNumber;
    CheckBox checkWithoutNumber;

    public SellItemUbicationSetDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_ubication_set_detail, container, false);
        editStreet = (EditText) v.findViewById(R.id.editStreet);
        editNumber = (EditText) v.findViewById(R.id.editNumber);
        checkWithoutNumber = (CheckBox) v.findViewById(R.id.checkWithoutNumber);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);

        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setUbicationDetail(editStreet.getText().toString(),editNumber.getText().toString());
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
