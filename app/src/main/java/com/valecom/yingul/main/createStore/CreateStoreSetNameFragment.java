package com.valecom.yingul.main.createStore;

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
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.buy.BuyItemConfirmFragment;
import com.valecom.yingul.main.sell.SellActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStoreSetNameFragment extends Fragment {

    private Button buttonSetName;
    private EditText editName;

    public CreateStoreSetNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_name, container, false);
        editName = (EditText) v.findViewById(R.id.editName);
        buttonSetName = (Button) v.findViewById(R.id.buttonSetName);
        editName.setHint("Nombre de tu tienda");

        buttonSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateStoreActivity)getActivity()).store.setName(editName.getText().toString().trim());
                CreateStoreSetSummaryFragment fragment = new CreateStoreSetSummaryFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
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
