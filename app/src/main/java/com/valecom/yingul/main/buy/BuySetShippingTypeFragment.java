package com.valecom.yingul.main.buy;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySetShippingTypeFragment extends Fragment {

    private TextView txtWithdrawProduct;
    private Yng_User user;
    private Yng_Ubication userUbication;

    public BuySetShippingTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_set_shipping_type, container, false);

        txtWithdrawProduct = (TextView) v.findViewById(R.id.txtWithdrawProduct);
        user = new Yng_User();

        txtWithdrawProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                    Intent intent = new Intent(getContext(), BuyActivity.class);
                    intent.putExtra("itemId",((BuyActivity)getActivity()).itemId);
                    intent.putExtra("itemQuantity",((BuyActivity)getActivity()).quantity);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    BuySetShippingWithdrawTypeFragment fragment = new BuySetShippingWithdrawTypeFragment();
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
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

    }

}
