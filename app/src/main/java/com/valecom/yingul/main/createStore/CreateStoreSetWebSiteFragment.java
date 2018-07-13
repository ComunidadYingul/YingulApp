package com.valecom.yingul.main.createStore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStoreSetWebSiteFragment extends Fragment {

    private Button buttonSetName;
    private EditText editName;
    private TextView txtTitle,textSpecification;

    public CreateStoreSetWebSiteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_name, container, false);
        editName = (EditText) v.findViewById(R.id.editName);
        buttonSetName = (Button) v.findViewById(R.id.buttonSetName);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        textSpecification = (TextView) v.findViewById(R.id.textSpecification);
        textSpecification.setText("Llena este campo solo si cuentas con un sitio web.");
        txtTitle.setText("Ingresa la dirección de tu sítio web");
        editName.setHint("ej: https://www.yingul.com");

        buttonSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editName.getText().toString().trim().length()==0){
                    ((CreateStoreActivity)getActivity()).createStore();
                }else{
                    ((CreateStoreActivity)getActivity()).store.setWebSite(editName.getText().toString().trim());
                    CreateStoreSetTraficInvestFragment fragment = new CreateStoreSetTraficInvestFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
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

        //Checks to make sure fragment is still attached to activity
        /*if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Envio");
        }*/
    }
}
