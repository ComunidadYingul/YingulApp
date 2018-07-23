package com.valecom.yingul.main.buy;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.sell.SellItemAddUbicationFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemInfoReserveFragment extends Fragment {

    TextView txtShowInfo;
    private MaterialDialog setting_address_edit_dialog;
    private Button buttonSetInfo;

    public BuyItemInfoReserveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_info_reserve, container, false);

        txtShowInfo = (TextView) v.findViewById(R.id.txtShowInfo);
        buttonSetInfo = (Button) v.findViewById(R.id.buttonSetinfo);

        txtShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.info_reserve, true)
                            .positiveText("OK")
                            .cancelable(false)
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    View view = setting_address_edit_dialog.getCustomView();

                                }
                            })
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
            }
        });

        buttonSetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity) getActivity()).shipping.setTypeShipping("home");
                BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
