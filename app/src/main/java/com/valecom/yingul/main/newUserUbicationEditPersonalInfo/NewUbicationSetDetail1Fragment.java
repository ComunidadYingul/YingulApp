package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.valecom.yingul.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUbicationSetDetail1Fragment extends Fragment {

    Button buttonSetUbicationDetail1;
    EditText editFlor,editWithinStreets,editRefence;

    public NewUbicationSetDetail1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_ubication_set_detail1, container, false);
        editFlor = (EditText) v.findViewById(R.id.editFlor);
        editWithinStreets = (EditText) v.findViewById(R.id.editWithinStreets);
        editRefence = (EditText) v.findViewById(R.id.editRefence);
        buttonSetUbicationDetail1 = (Button) v.findViewById(R.id.buttonSetUbicationDetail1);
        buttonSetUbicationDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication.setWithinStreets(editWithinStreets.getText().toString().trim());
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication.setDepartment(editFlor.getText().toString().trim());
                ((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication.setAditional(editRefence.getText().toString().trim());

                Gson gson = new Gson();
                String jsonBody = gson.toJson(((NewUserUbicationEditPersonalInfoActivity)getActivity()).ubication);
                Log.e("ubica:---",jsonBody);

                AddPersonalInfo1Fragment fragment = new AddPersonalInfo1Fragment();
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
