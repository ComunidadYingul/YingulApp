package com.valecom.yingul.main.sell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.NewInvoiceActivity;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemSetPriceFragment extends Fragment {

    Button buttonSetPrice;
    private EditText editPrice,editPriceNormal,editPriceDiscount;
    private TextView editPorcentDiscount;
    Spinner spinner_currency;
    private LinearLayout layoutOfferDiscount;
    private MaterialDialog setting_address_edit_dialog;
    private Button btnOfferDiscount;
    Validacion val;

    public SellItemSetPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_set_price, container, false);
        editPrice = (EditText) v.findViewById(R.id.editPrice);
        buttonSetPrice = (Button) v.findViewById(R.id.buttonSetPrice);
        spinner_currency = (Spinner) v.findViewById(R.id.spinner_currency);
        layoutOfferDiscount = (LinearLayout) v.findViewById(R.id.layoutOfferDiscount);
        btnOfferDiscount = (Button) v.findViewById(R.id.btnOfferDiscount);
        val = new Validacion();

        if(((SellActivity)getActivity()).item.getType()=="Product"){
            layoutOfferDiscount.setVisibility(View.VISIBLE);
        }else{
            layoutOfferDiscount.setVisibility(View.GONE);
        }

        String currency[] = {"U$D","$"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(v.getContext(),   android.R.layout.simple_spinner_item, currency);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner_currency.setAdapter(spinnerArrayAdapter);
        if(((SellActivity)getActivity()).item.getType()=="Product"){
            spinner_currency.setSelection(1);
        }


        buttonSetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val.validarNumero(editPrice,editPrice.getText().toString())){
                    editPrice.setError("Ingrese precio");
                }else if((((SellActivity)getActivity()).item.getType().equals("Motorized") || ((SellActivity)getActivity()).item.getType().equals("Property") || ((SellActivity)getActivity()).item.getType().equals("Service")) && editPrice.getText().toString().length() > 9){
                    editPrice.setError("El precio no debe sobrepasar 999999999");
                }else if(((SellActivity)getActivity()).item.getType().equals("Product") && Integer.valueOf(String.valueOf(editPrice.getText())) > 29000){
                    editPrice.setError("El precio no debe sobrepasar de 29000");
                }
                else{

                    if(spinner_currency.getSelectedItemPosition()==0){
                        ((SellActivity)getActivity()).item.setMoney("USD");
                    }else{
                        ((SellActivity)getActivity()).item.setMoney("ARS");
                    }
                    ((SellActivity) getActivity()).item.setPriceDiscount(0);
                    ((SellActivity) getActivity()).item.setPriceNormal(0);
                    ((SellActivity)getActivity()).setItemPrice(Double.parseDouble(editPrice.getText().toString()));

                    /*Log.e("Moneda", String.valueOf(spinner_currency.getSelectedItemPosition()));
                    ((SellActivity)getActivity()).item.setPrice(Double.parseDouble(editPrice.getText().toString()));
                    ((SellActivity)getActivity()).item.setPriceDiscount(0);
                    ((SellActivity)getActivity()).item.setPriceNormal(0);*/

                    /*************   colocar switch ************ */
                    /*if(((SellActivity)getActivity()).item.getType()=="Product"){
                        SellItemSetDeliveryFragment fragment = new SellItemSetDeliveryFragment();
                        FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else if(((SellActivity)getActivity()).item.getType()=="Motorized" || ((SellActivity)getActivity()).item.getType()=="Property" || ((SellActivity)getActivity()).item.getType()=="Service"){
                        SellItemAddUbicationFragment itemAddUbication = new SellItemAddUbicationFragment();
                        FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                        itemAddUbication.type=((SellActivity)getActivity()).item.getType();
                        fragmentTransaction.replace(R.id.content_frame, itemAddUbication);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }*/
                }
            }
        });
        btnOfferDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(val.valNumber(editPrice)) {
                    setting_address_edit_dialog = new MaterialDialog.Builder(getContext())
                            .customView(R.layout.offer_discount_layout, true)
                            .positiveText("OK")
                            .cancelable(false)
                            .negativeText("Cancelar")
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    View view = setting_address_edit_dialog.getCustomView();
                                    editPriceNormal = (EditText) view.findViewById(R.id.editPriceNormal);
                                    editPriceNormal.setText(editPrice.getText().toString());
                                    editPriceDiscount = (EditText) view.findViewById(R.id.editPriceDiscount);
                                    editPorcentDiscount = (TextView) view.findViewById(R.id.editPorcentDiscount);

                                    setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                                    editPriceDiscount.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            Log.e("Gonzalo:----","beforeTextChanged");
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            int normal = Integer.valueOf(editPriceNormal.getText().toString());
                                            int discount = 0;
                                            if(editPriceDiscount.getText().toString().trim().length() > 0){
                                                discount = Integer.valueOf(editPriceDiscount.getText().toString());
                                            }

                                            if(discount > normal){
                                                editPorcentDiscount.setText("El descuento no debe ser mayor al precio normal");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                            }else if(discount == normal || editPriceDiscount.getText().toString().trim().length() <= 0){
                                                editPorcentDiscount.setText("Ingrese el precio con descuento");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                            }
                                            else {
                                                int porcent = ((normal - discount) * 100) / normal;
                                                editPorcentDiscount.setText("Estas ofreciendo un descuento del "+porcent+"%");
                                                setting_address_edit_dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            Log.e("Gonzalo:----","afterTextChanged");
                                        }
                                    });

                                }
                            })
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    ((SellActivity) getActivity()).item.setMoney("ARS");
                                    ((SellActivity) getActivity()).item.setPriceDiscount(Double.parseDouble(editPriceDiscount.getText().toString().trim()));
                                    ((SellActivity) getActivity()).item.setPriceNormal(Double.parseDouble(editPriceNormal.getText().toString().trim()));
                                    ((SellActivity)getActivity()).setItemPrice(Double.parseDouble(editPriceDiscount.getText().toString()));
                                    /*SellItemSetDeliveryFragment fragment = new SellItemSetDeliveryFragment();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();*/
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    if (dialog != null && dialog.isShowing()) {
                                        // If the response is JSONObject instead of expected JSONArray
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .show();
                }
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
