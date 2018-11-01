package com.valecom.yingul.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.ViewPagerAdapter2;
import com.valecom.yingul.main.index.InicioFragment;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.valecom.yingul.service.NotificationService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


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
    private String username,password;

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
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            username = "";
        }else{
            username = settings.getString("username","");
            password = settings.getString("password","");
        }
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        loadViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabIcons();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getContext(),  tabLayout.getSelectedTabPosition()+"", Toast.LENGTH_SHORT).show();
                if(tabLayout.getSelectedTabPosition()==1){
                    TextView cart_badge = (TextView) tab.getCustomView().findViewById(R.id.cart_badge);
                    if(cart_badge.getVisibility()==View.VISIBLE){
                        cart_badge.setVisibility(View.GONE);
                        cart_badge.setText("0");
                        if(username!=""){
                            editNotifications(Network.API_URL + "notification/updateAllNotificationsForUser/"+username+"/viewed","",password);
                        }
                    }
                    try {
                        Badges.setBadge(getContext(),0);
                    } catch (BadgesNotSupportedException badgesNotSupportedException) {

                    }
                }
                iconColor(tab,"#FF5000");
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
        //tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
        ImageView itemImage = tab.getCustomView().findViewById(R.id.itemImage);
        itemImage.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    private void loadViewPager(ViewPager viewPager){
        ViewPagerAdapter2 adapter = new ViewPagerAdapter2(getChildFragmentManager());
        adapter.addFragment(newInstance());
        adapter.addFragment(newInstance1());
        adapter.addFragment(newInstance1());
        //adapter.addFragment(newInstance3());
        viewPager.setAdapter(adapter);
    }

    private void tabIcons(){
        tabLayout.getTabAt(0).setCustomView(getTabView(R.drawable.ic_buy_now_black,0));
        if(username==""){
            tabLayout.getTabAt(1).setCustomView(getTabView(R.drawable.ic_notifications_black_24dp,0));
            tabLayout.getTabAt(2).setCustomView(getTabView(R.drawable.ic_chat_black,0));
            //tabLayout.getTabAt(3).setCustomView(getTabView(R.drawable.ic_contacts_black,0));
            colorDefaultIcons();
            iconColor(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()),"#FF5000");
        }else{
            getNotificationsNotified();
        }
    }

    public View getTabView(int icon,int badge) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_tablayout_item_layout, null);
        ImageView itemImage = (ImageView) v.findViewById(R.id.itemImage);
        TextView cart_badge = (TextView) v.findViewById(R.id.cart_badge);
        itemImage.setImageResource(icon);
        if(badge==0){
           cart_badge.setVisibility(View.GONE);
        }else{
            cart_badge.setVisibility(View.VISIBLE);
            cart_badge.setText(String.valueOf(badge));
        }
        return v;
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

    private NotificationFragment2 newInstance1(){
        NotificationFragment2 fragment = new NotificationFragment2();
        return fragment;
    }

    /*private ContactsFragment newInstance3(){
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }*/

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

    public void getNotificationsNotified(){
        //progressDialog.show();
        JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "notification/getNotificationByUserAndStatus/"+username+"/notified/0/0",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            JSONArray items = response;
                            Log.e("===",items.length()+password);
                            tabLayout.getTabAt(1).setCustomView(getTabView(R.drawable.ic_notifications_black_24dp,items.length()));
                            tabLayout.getTabAt(2).setCustomView(getTabView(R.drawable.ic_chat_black,0));
                            //tabLayout.getTabAt(3).setCustomView(getTabView(R.drawable.ic_contacts_black,0));
                            colorDefaultIcons();
                            iconColor(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()),"#FF5000");
                        }
                        catch(Exception ex)
                        {
                            if (isAdded()) {
                                Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_LONG).show();
                            }
                        }

                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                // TODO Auto-generated method stub
                if (progressDialog != null && progressDialog.isShowing()) {
                    // If the response is JSONObject instead of expected JSONArray
                    progressDialog.dismiss();
                }

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null)
                {
                    try
                    {
                        JSONObject json = new JSONObject(new String(response.data));
                        Toast.makeText(getContext(), json.has("message") ? json.getString("message") : json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), R.string.error_try_again_support, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), error != null && error.getMessage() != null ? error.getMessage() : error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //SharedPreferences settings = getActivity().getSharedPreferences(ActivityLogin.SESSION_USER, getActivity().MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", password);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance( getContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(MainActivity.TAG);

        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
    }

    public void  editNotifications(String url, String json, String authorization){
        final String TAG = "bUYActivity";
        final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",authorization)
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }

            }
        });
    }

}
