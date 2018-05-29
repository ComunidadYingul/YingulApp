package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemAddUbicationFragment extends Fragment {

    TextView txtOtherUbication,txtAddPictures,editUbicationActual;
    String type;
    Button buttonSetUbicationLocal;
    public SellItemAddUbicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_add_ubication, container, false);

        txtOtherUbication = (TextView) v.findViewById(R.id.txtOtherUbication);
        txtAddPictures = (TextView) v.findViewById(R.id.txtAddPictures);
        editUbicationActual = (TextView) v.findViewById(R.id.editUbicationActual);
        buttonSetUbicationLocal = (Button) v.findViewById(R.id.buttonSetUbicationLocal);

        Gson gson = new Gson();
        String json = gson.toJson(((SellActivity)getActivity()).userUbication);
        Log.e("ubicacion:-----",json);

        if(((SellActivity)getActivity()).userUbication!=null)
        {
            editUbicationActual.setText(((SellActivity)getActivity()).userUbication.getYng_Country().getName()+". "+((SellActivity)getActivity()).userUbication.getYng_Province().getName()+", "+((SellActivity)getActivity()).userUbication.getYng_City().getName()+" "+((SellActivity)getActivity()).userUbication.getStreet()+" °"+((SellActivity)getActivity()).userUbication.getNumber());
        }
        switch (type) {
            case "Service":
                txtAddPictures.setText("¿Cuál es la ubicacion de tu servicio?");
                break;
            case "Product":
                txtAddPictures.setText("¿Cuál es la ubicacion de tu servicio?");
                break;
            case "Motorized":
                txtAddPictures.setText("¿Cuál es la ubicacion de tu vehículo?");
                break;
            case "Property":
                txtAddPictures.setText("¿Cuál es la ubicacion de tu inmueble?");
                break;
        }
        txtOtherUbication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).setUbication("null");
            }
        });
        buttonSetUbicationLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).ubication=((SellActivity)getActivity()).userUbication;
                if(((SellActivity)getActivity()).item.getType()=="Service"){
                    SellItemAddSummaryFragment itemSetSummary = new SellItemAddSummaryFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, itemSetSummary);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else if(((SellActivity)getActivity()).item.getType()=="Property" || ((SellActivity)getActivity()).item.getType()=="Motorized"){
                    SellItemAddContactFragment itemSetSummary = new SellItemAddContactFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, itemSetSummary);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
