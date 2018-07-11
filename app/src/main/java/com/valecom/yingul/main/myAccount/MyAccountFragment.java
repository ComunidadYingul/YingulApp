package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.myAccount.yingulPay.ActividadYingulPayFragment;
import com.valecom.yingul.main.myAccount.yingulPay.YingulPayActivity;
import com.valecom.yingul.main.sell.SellActivity;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private Button buttonSell,settings_logout_button;
    //private LinearLayout shoppingQuestions,purchasesLayout,publicationsLayout,salesQuestionsLayout,userProfile,;
    private LinearLayout shoppingQuestions,purchasesLayout,publicationsLayout,salesQuestionsLayout,userProfile,yingulPay,salesListLayout;

    public MyAccountFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InvoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2)
    {
        MyAccountFragment fragment = new MyAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        final SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        buttonSell = (Button) view.findViewById(R.id.buttonSell);
        settings_logout_button = (Button) view.findViewById(R.id.settings_logout_button);
        shoppingQuestions = (LinearLayout) view.findViewById(R.id.shoppingQuestions);
        purchasesLayout = (LinearLayout) view.findViewById(R.id.purchasesLayout);
        publicationsLayout = (LinearLayout) view.findViewById(R.id.publicationsLayout);
        salesQuestionsLayout = (LinearLayout) view.findViewById(R.id.salesQuestionsLayout);
        userProfile = (LinearLayout) view.findViewById(R.id.userProfile);
        yingulPay = (LinearLayout) view.findViewById(R.id.yingulPay);
        salesListLayout = (LinearLayout) view.findViewById(R.id.salesListLayout);

        buttonSell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellActivity.class);
                startActivity(intent);
            }
        });
        settings_logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE).edit();
                settings.clear();
                settings.commit();
                Intent settingsIntent = new Intent(getActivity(), MainActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingsIntent);
            }
        });
        shoppingQuestions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAccountShoppingQuestionsListFragment fragment = new MyAccountShoppingQuestionsListFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        purchasesLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAccountPurchasesListFragment fragment = new MyAccountPurchasesListFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        publicationsLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAccountPublicationsListFragment fragment = new MyAccountPublicationsListFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        salesQuestionsLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAccountSalesQuestionsListFragment fragment = new MyAccountSalesQuestionsListFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        salesListLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MyAccountSalesListFragment fragment = new MyAccountSalesListFragment();
                FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tempUsername = settings.getString("username",null);
                //Bundle bundle = new Bundle();
                //bundle.putString("item",tempUsername);


                if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }else{
                    Log.e("Username:--",tempUsername);

                    MyAccountUserProfileFragment fragment = new MyAccountUserProfileFragment();
                    //fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        yingulPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YingulPayActivity.class);
                startActivity(intent);
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
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mi cuenta");
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
