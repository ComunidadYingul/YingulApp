package com.valecom.yingul.main.sell;


import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemAddPicturesFragment extends Fragment {

    TextView txtMoreLater,txtAddPictures;
    String type;
    Button buttonAddPictures;

    public SellItemAddPicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_add_pictures, container, false);

        txtMoreLater = (TextView) v.findViewById(R.id.txtMoreLater);
        txtAddPictures = (TextView) v.findViewById(R.id.txtAddPictures);
        switch (type) {
            case "Service":
                txtAddPictures.setText("Agrega fotos de tu servicio");
                break;
            case "Product":
                txtAddPictures.setText("Agrega fotos de tu producto");
                break;
            case "Motorized":
                txtAddPictures.setText("Agrega fotos de tu vehículo");
                break;
            case "Property":
                txtAddPictures.setText("Agrega fotos de tu inmueble");
                break;
        }
        txtMoreLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setWithoutPictures("sin");
            }
        });

        buttonAddPictures = (Button) v.findViewById(R.id.buttonAddPictures);

        buttonAddPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).openGalery();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
