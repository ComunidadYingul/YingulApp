package com.valecom.yingul.main.buy;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.sell.SellItemAddUbicationFragment;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemInfoReserveFragment extends Fragment {

    TextView txtAddPictures,txtShowInfo;
    private MaterialDialog setting_address_edit_dialog;
    private Button buttonSetInfo;
    private Yng_User user;
    private Yng_Ubication userUbication;

    public BuyItemInfoReserveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_info_reserve, container, false);

        txtAddPictures = (TextView) v.findViewById(R.id.txtAddPictures);
        txtShowInfo = (TextView) v.findViewById(R.id.txtShowInfo);
        buttonSetInfo = (Button) v.findViewById(R.id.buttonSetinfo);

        txtAddPictures.setText("Vas a reservar tu próximo vehículo");
        txtShowInfo.setText("Elegís un medio para pagar $ 1500 de reserva. Luego, acordás con el vendedor el resto del pago y la entrega.");
        txtShowInfo.setTextColor(Color.WHITE);

        user = new Yng_User();
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){
                userUbication=null;
            }else{
                Gson gson = new Gson();
                userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
        }

        /*txtShowInfo.setOnClickListener(new View.OnClickListener() {
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
        });*/

        buttonSetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                    Intent intent = new Intent(getContext(), BuyActivity.class);
                    intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
                    intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    ((BuyActivity) getActivity()).shipping.setTypeShipping("home");
                    BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return v;
    }

}
