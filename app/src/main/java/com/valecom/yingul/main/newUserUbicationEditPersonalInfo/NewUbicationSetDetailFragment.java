package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.valecom.yingul.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUbicationSetDetailFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editStreet,editNumber;
    CheckBox checkWithoutNumber;

    public NewUbicationSetDetailFragment() {
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
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication.setStreet(editStreet.getText().toString().trim());
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication.setNumber(editNumber.getText().toString().trim());

                Gson gson = new Gson();
                String jsonBody = gson.toJson(((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication);
                Log.e("ubica:---",jsonBody);

                NewUbicationSetDetail1Fragment fragment = new NewUbicationSetDetail1Fragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
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
