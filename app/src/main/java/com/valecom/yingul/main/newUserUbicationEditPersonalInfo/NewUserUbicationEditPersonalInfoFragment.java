package com.valecom.yingul.main.newUserUbicationEditPersonalInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserUbicationEditPersonalInfoFragment extends Fragment {

    Button buttonSetPersonalInfo;
    public NewUserUbicationEditPersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_user_ubication_edit_personal_info, container, false);

        buttonSetPersonalInfo = (Button) v.findViewById(R.id.buttonSetPersonalInfo);

        buttonSetPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewUbicationSetCityByZipFragment fragment = new NewUbicationSetCityByZipFragment();
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

    }

}
