package com.valecom.yingul.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.ViewPagerAdapter2;
import com.valecom.yingul.main.index.InicioFragment;
import com.valecom.yingul.service.NotificationService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{
    private MaterialDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    private TabLayout tabLayout;
    private int[] tabIcons={R.drawable.ic_buy_now_black, R.drawable.ic_notifications_black_24dp,R.drawable.ic_chat_black,R.drawable.ic_contacts_black};

    public HomeFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2)
    {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        loadViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabIcons();
        colorDefaultIcons();
        iconColor(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()),"#FF5000");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                iconColor(tab,"#FF5000");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab,"#E0E0E0");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //*******************servicio************/
        Intent serviceIntent = new Intent(getContext(), NotificationService.class);
        getContext().startService(serviceIntent);

        return view;
    }

    private void iconColor(TabLayout.Tab tab,String color){
        tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    private void loadViewPager(ViewPager viewPager){
        ViewPagerAdapter2 adapter = new ViewPagerAdapter2(getActivity().getSupportFragmentManager());
        adapter.addFragment(newInstance());
        adapter.addFragment(newInstance1());
        adapter.addFragment(newInstance1());
        adapter.addFragment(newInstance1());
        viewPager.setAdapter(adapter);
    }

    private void tabIcons(){
        for(int i=0; i<4; i++){
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private void colorDefaultIcons(){
        for(int i=0; i<4; i++){
            iconColor(tabLayout.getTabAt(i),"#E0E0E0");
        }
    }

    private InicioFragment newInstance(){
        InicioFragment fragment = new InicioFragment();
        return fragment;
    }

    private NotificationFragment newInstance1(){
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
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

        //Checks to make sure fragment is still attached to activity
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Yingul");
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_home);
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
