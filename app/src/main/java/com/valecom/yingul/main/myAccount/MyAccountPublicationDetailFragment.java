package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.network.Network;
import org.json.JSONObject;

public class MyAccountPublicationDetailFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Yng_Item item;

    private ImageView principalImage;
    private TextView txtItemName,txtCurrencyPrice,txtDescription;
    private Button btnItemDetail;

    private MaterialDialog setting_address_edit_dialog;

    public static final String TAG = "PurchaseDetailFragment";
    public MyAccountPublicationDetailFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountPublicationDetailFragment newInstance(String param1, String param2)
    {
        MyAccountPublicationDetailFragment fragment = new MyAccountPublicationDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_account_publication_detail, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(getActivity(), LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
        }

        Bundle bundle = getArguments();
        Gson gson = new Gson();
        item = gson.fromJson(bundle.getString("item"), Yng_Item.class);

        principalImage = (ImageView) view.findViewById(R.id.principalImage);
        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtCurrencyPrice = (TextView) view.findViewById(R.id.txtCurrencyPrice);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        btnItemDetail = (Button) view.findViewById(R.id.btnItemDetail);

        txtItemName.setText(item.getName());
        txtCurrencyPrice.setText("$ "+item.getPrice());
        txtDescription.setText(item.getDescription());
        Picasso.with(getActivity()).load(Network.BUCKET_URL+item.getPrincipalImage()).into(principalImage);

        btnItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                intent_detail.putExtra("itemId",String.valueOf(item.getItemId()));
                startActivity(intent_detail);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(item.getName());
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
