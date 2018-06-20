package com.valecom.yingul.main.sell;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        editHeight.addTextChangedListener(new TextWatcher() {
            long largo,ancho,alto;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editLength.getText().toString().trim().equals(""))largo=0;
                else largo = Long.valueOf(editLength.getText().toString());

                if(editWidth.getText().toString().trim().equals(""))ancho=0;
                else ancho = Long.valueOf(editWidth.getText().toString());

                if(editHeight.getText().toString().trim().equals(""))alto=0;
                else alto = Long.valueOf(editHeight.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtVolume.setText((largo * ancho * alto)+"");
            }
        });

        editLength.addTextChangedListener(new TextWatcher() {
            long largo,ancho,alto;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editLength.getText().toString().trim().equals(""))largo=0;
                else largo = Long.valueOf(editLength.getText().toString());

                if(editWidth.getText().toString().trim().equals(""))ancho=0;
                else ancho = Long.valueOf(editWidth.getText().toString());

                if(editHeight.getText().toString().trim().equals(""))alto=0;
                else alto = Long.valueOf(editHeight.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtVolume.setText((largo * ancho * alto)+"");
            }
        });

        editWidth.addTextChangedListener(new TextWatcher() {
            long largo,ancho,alto;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editLength.getText().toString().trim().equals(""))largo=0;
                else largo = Long.valueOf(editLength.getText().toString());

                if(editWidth.getText().toString().trim().equals(""))ancho=0;
                else ancho = Long.valueOf(editWidth.getText().toString());

                if(editHeight.getText().toString().trim().equals(""))alto=0;
                else alto = Long.valueOf(editHeight.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtVolume.setText((largo * ancho * alto)+"");
            }
        });

        buttonVolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editWidth.getText().toString().trim().length() == 0) {
                    editWidthContent.setError("Ingresar ancho");
                    editWidthContent.requestFocus();
                }else if(editHeight.getText().toString().trim().length() == 0){
                    editHeightContent.setError("Ingresar Altura");
                    editHeightContent.requestFocus();
                }else if(editLength.getText().toString().trim().length() == 0){
                    editLengthContent.setError("Ingresar longitud");
                    editLengthContent.requestFocus();
                }else if(txtVolume.getText().toString().trim().length() == 0){
                    txtVolumeContent.setError("Ingresar volumen");
                    txtVolumeContent.requestFocus();
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
