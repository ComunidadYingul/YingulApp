package com.valecom.yingul.main.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.valecom.yingul.R;
import com.valecom.yingul.network.Network;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyItemConfirmFragment extends Fragment {
    Button buttonSetBuy,buttonSetBuy1;
    TextView txtItemType,txtCurrencyPrice,txtTotal,txtItemName,txtQuantity,txtPayment,txtViewShipping,txtViewPayment,txtShippingCost,txtViewBranch,txtTypeOSchedules,txtBranchName,txtBranchStreet,txtPrice,txtTitle;
    ImageView principalImage,imgPayment,imgShipping;
    LinearLayout layoutShipping,layBranch;

    public BuyItemConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_item_confirm, container, false);

        txtItemType = (TextView) v.findViewById(R.id.txtItemType);
        txtCurrencyPrice = (TextView) v.findViewById(R.id.txtCurrencyPrice);
        txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtTotal = (TextView) v.findViewById(R.id.txtTotal);
        txtItemName = (TextView) v.findViewById(R.id.txtItemName);
        txtQuantity = (TextView) v.findViewById(R.id.txtQuantity);
        principalImage = (ImageView) v.findViewById(R.id.principalImage);
        imgPayment = (ImageView) v.findViewById(R.id.imgPayment);
        imgShipping = (ImageView) v.findViewById(R.id.imgShipping);
        txtPayment = (TextView) v.findViewById(R.id.txtPayment);
        txtViewShipping = (TextView) v.findViewById(R.id.txtViewShipping);
        txtViewPayment = (TextView) v.findViewById(R.id.txtViewPayment);
        txtShippingCost = (TextView) v.findViewById(R.id.txtShippingCost);
        layoutShipping = (LinearLayout) v.findViewById(R.id.layoutShipping);
        txtViewBranch = (TextView) v.findViewById(R.id.txtViewBranch);
        txtTypeOSchedules = (TextView) v.findViewById(R.id.txtTypeOSchedules);
        layBranch = (LinearLayout) v.findViewById(R.id.layBranch);
        txtBranchName = (TextView) v.findViewById(R.id.txtBranchName);
        txtBranchStreet = (TextView) v.findViewById(R.id.txtBranchStreet);
        buttonSetBuy = (Button) v.findViewById(R.id.buttonSetBuy);
        buttonSetBuy1 = (Button) v.findViewById(R.id.buttonSetBuy1);
        Log.e("fsd============",((BuyActivity)getActivity()).shipping.getTypeShipping());

        if(((BuyActivity)getActivity()).item.getType().equals("Motorized")){
            txtTitle.setText("Confirma tu reserva");
            buttonSetBuy.setText("Confirma tu reserva");
            buttonSetBuy1.setText("Confirma tu reserva");
            txtTotal.setText("$ 1500");
        }else{
            if(((BuyActivity)getActivity()).shipping.getTypeShipping().equals("home")){
                imgShipping.setImageResource(R.drawable.home);
                layoutShipping.setVisibility(View.GONE);
                layBranch.setVisibility(LinearLayout.GONE);
                txtTypeOSchedules.setText("Retiro en el domicilio del vendedor");
                txtTotal.setText("$ "+(double)Math.round(((((BuyActivity)getActivity()).item.getPrice())*(((BuyActivity)getActivity()).quantity)) * 100d) / 100d);
            }else{
                imgShipping.setImageResource(R.drawable.branch);
                layoutShipping.setVisibility(View.VISIBLE);
                layBranch.setVisibility(View.VISIBLE);
                if(((BuyActivity)getActivity()).item.getProductPagoEnvio().equals("gratis")){
                    txtShippingCost.setText("GRATIS");
                    txtTotal.setText("$ "+(double)Math.round(((((BuyActivity)getActivity()).item.getPrice())*(((BuyActivity)getActivity()).quantity)) * 100d) / 100d);
                }else{
                    txtShippingCost.setText("$ "+(double)Math.round((((BuyActivity)getActivity()).quote.getRate()) * 100d) / 100d);
                    txtTotal.setText("$ "+(double)Math.round((((((BuyActivity)getActivity()).item.getPrice())*(((BuyActivity)getActivity()).quantity))+((BuyActivity)getActivity()).quote.getRate()) * 100d) / 100d);
                }
                txtTypeOSchedules.setText(((BuyActivity)getActivity()).quote.getYng_Branch().getSchedules());
                txtBranchName.setText("Sucursal "+((BuyActivity)getActivity()).quote.getYng_Branch().getNameMail()+" "+((BuyActivity)getActivity()).quote.getYng_Branch().getLocation());
                txtBranchStreet.setText(((BuyActivity)getActivity()).quote.getYng_Branch().getStreet());
            }
        }


        switch(((BuyActivity)getActivity()).item.getType()){
            case "Product":
                txtItemType.setText("Producto");
                break;
            case "Service":
                txtItemType.setText("Servicio");
                break;
            case "Motorized":
                txtItemType.setText("Vehículo");
                break;
            case "Property":
                txtItemType.setText("Inmueble");
                break;
        }
        if(((BuyActivity)getActivity()).item.getType().equals("Motorized")){
            txtCurrencyPrice.setText("$ 1500");
        }else {
            txtCurrencyPrice.setText("$ "+(double)Math.round(((((BuyActivity)getActivity()).item.getPrice())*(((BuyActivity)getActivity()).quantity)) * 100d) / 100d);
        }
        txtPrice.setText("$ "+((BuyActivity)getActivity()).item.getPrice());
        txtItemName.setText(((BuyActivity)getActivity()).item.getName());
        txtQuantity.setText(String.valueOf(((BuyActivity)getActivity()).quantity));
        Picasso.with(getActivity()).load(Network.BUCKET_URL+((BuyActivity)getActivity()).item.getPrincipalImage()).into(principalImage);
        if(((BuyActivity)getActivity()).payment.getType().equals("CASH")){
            imgPayment.setImageResource(R.drawable.cash1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " en " +((BuyActivity)getActivity()).cashPayment.getPaymentMethod()+ '\n' +" No te demores en ir a pagar, solo podemos reservarte stock cuando el pago se acredite.");
        }else {
            imgPayment.setImageResource(R.drawable.card1);
            txtPayment.setText("Paga " + txtTotal.getText().toString() + " con " +((BuyActivity)getActivity()).card.getProvider()+" "+((BuyActivity)getActivity()).card.getType()+"O terminada en "+(((BuyActivity)getActivity()).card.getNumber()%10000));
        }


        buttonSetBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity)getActivity()).getLocalIpAddress();
            }
        });
        buttonSetBuy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BuyActivity)getActivity()).getLocalIpAddress();
            }
        });

        txtViewShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySetShippingWithdrawTypeFragment fragment = new BuySetShippingWithdrawTypeFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        txtViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySetPaymentTypeFragment fragment = new BuySetPaymentTypeFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        txtViewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyItemSetShippingBranchFragment fragment = new BuyItemSetShippingBranchFragment();
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
