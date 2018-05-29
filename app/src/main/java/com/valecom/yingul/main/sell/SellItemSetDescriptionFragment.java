package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetDescriptionFragment extends Fragment {

    Button buttonSetDescription;
    EditText editDescription;
    TextView txtDescription;

    public SellItemSetDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_description, container, false);
        editDescription = (EditText) v.findViewById(R.id.editDescription);
        txtDescription = (TextView)v.findViewById(R.id.txtDescription);
        buttonSetDescription = (Button) v.findViewById(R.id.buttonSetDescription);

        switch (((SellActivity)getActivity()).item.getType()){
            case "Service":
                txtDescription.setText("¿Qué incluye tu servicio?");
                break;
            case "Product":
                txtDescription.setText("¿Qué incluye tu producto?");
                break;
            default:
                txtDescription.setText("¿Qué incluye tu ?");
                break;
        }

        buttonSetDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).includes=editDescription.getText().toString();
                //if(((SellActivity)getActivity()).item.getType()=="Service"){
                    SellItemSetDescription1Fragment fragment = new SellItemSetDescription1Fragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                /*}else if(((SellActivity)getActivity()).item.getType()=="Product"){
                    SellItemSetCategoryFragment itemSetCategory = new SellItemSetCategoryFragment();
                    itemSetCategory.type=((SellActivity)getActivity()).item.getType();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }*/

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
