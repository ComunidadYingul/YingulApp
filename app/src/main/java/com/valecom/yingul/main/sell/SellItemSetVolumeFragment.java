package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetVolumeFragment extends Fragment {

    Button buttonVolumen;
    EditText editHeight, editLength, editWidth;
    TextInputLayout editHeightContent, editLengthContent, editWidthContent, txtVolumeContent;
    TextView txtVolume;
    int length=1,width=1,height=1;
    double volume=1;

    public SellItemSetVolumeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_volume, container, false);

        editHeightContent = (TextInputLayout)v.findViewById(R.id.editHeightContent);
        editWidthContent = (TextInputLayout)v.findViewById(R.id.editWidthContent);
        editLengthContent = (TextInputLayout)v.findViewById(R.id.editLengthContent);
        txtVolumeContent = (TextInputLayout)v.findViewById(R.id.txtVolumeContent);

        editHeight = (EditText)v.findViewById(R.id.editHeight);
        editWidth = (EditText)v.findViewById(R.id.editWidth);
        editLength = (EditText)v.findViewById(R.id.editLength);
        txtVolume = (TextView)v.findViewById(R.id.txtVolume);
        buttonVolumen = (Button)v.findViewById(R.id.buttonVolume);

        buttonVolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editWidth.getText().toString().trim().length() == 0) {
                    editWidthContent.setError("Ingresar ancho");
                }else if(editHeight.getText().toString().trim().length() == 0){
                    editHeightContent.setError("Ingresar Altura");
                }else if(editLength.getText().toString().trim().length() == 0){
                    editLengthContent.setError("Ingresar longitud");
                }else if(txtVolume.getText().toString().trim().length() == 0){
                    txtVolumeContent.setError("Ingresar volumen");
                }
                else{

                    height = Integer.parseInt(editHeight.getText().toString());
                    length = Integer.parseInt(editLength.getText().toString());
                    width = Integer.parseInt(editWidth.getText().toString());

                    ((SellActivity) getActivity()).product.setProducVolumen((height * length * width) + "");
                    ((SellActivity) getActivity()).product.setProductHeight(height);
                    ((SellActivity) getActivity()).product.setProductLength(length);
                    ((SellActivity) getActivity()).product.setProductWidth(width);

                    SellItemSetWeightFragment fragment = new SellItemSetWeightFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return v;
    }

}
