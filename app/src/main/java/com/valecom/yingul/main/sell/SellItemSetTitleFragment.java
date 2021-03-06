package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetTitleFragment extends Fragment {

    Button buttonSetTitle;
    EditText editTitle;
    TextView txtTitle;
    TextInputLayout editTitleContent;

    public SellItemSetTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_title, container, false);
        editTitle = (EditText) v.findViewById(R.id.editTitle);
        txtTitle = (TextView)v.findViewById(R.id.txtTitle);
        buttonSetTitle = (Button) v.findViewById(R.id.buttonSetTitle);
        editTitleContent = (TextInputLayout) v.findViewById(R.id.editTitleContent);

        switch (((SellActivity)getActivity()).item.getType()){
            case "Motorized":
                txtTitle.setText("¿Que vehículo quieres vender?");
                editTitleContent.setHint("Ejemplo: Renault Clio");
                break;
            case "Property":
                editTitleContent.setHint("Ejemplo: Casa de 5 ambientes y 2 baños");
                break;
            case "Service":
                editTitleContent.setHint("Ejemplo: Clases particulares de inglés");
                break;
            case "Product":
                editTitleContent.setHint("Ejemplo: Camisa Lacoste hombre manga larga");
                break;
            default:
                break;
        }

        buttonSetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val=new Validacion();
                if(val.validarCadena(editTitle.getText().toString())){
                    editTitleContent.setError("Ingresar título no menos de 5 caracteres");
                    editTitleContent.requestFocus();
                }else{
                    ((SellActivity)getActivity()).setItemTitle(editTitle.getText().toString().trim());
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
