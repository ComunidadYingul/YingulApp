package com.valecom.yingul.main.edit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valecom.yingul.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditItemImageFragment extends Fragment {


    public EditItemImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_item_image, container, false);
    }

}
