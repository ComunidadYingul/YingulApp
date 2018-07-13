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
public class CreateStoreSetVideoFragment extends Fragment {

    private Button buttonSetName;
    private EditText editName;
    private TextView txtTitle;
    private LinearLayout layoutSpecification;

    public CreateStoreSetVideoFragment() {
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
        layoutSpecification = (LinearLayout) v.findViewById(R.id.layoutSpecification);
        layoutSpecification.setVisibility(View.GONE);
        txtTitle.setText("Video de tu tienda");
        editName.setHint("Copia un video(link) de Youtube y pegalo acá.");

        buttonSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateStoreActivity)getActivity()).store.setVideo(editName.getText().toString().trim());
                CreateStoreSetEntryFragment fragment = new CreateStoreSetEntryFragment();
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
