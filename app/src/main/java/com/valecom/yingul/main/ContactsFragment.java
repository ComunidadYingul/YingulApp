package com.valecom.yingul.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.AndroidContactAdapter;
import com.valecom.yingul.model.Yng_AndroidContact;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment
{
    private JSONObject api_parameter;
    private OnFragmentInteractionListener mListener;
    private MaterialDialog progressDialog;
    private ListView list;
    private AndroidContactAdapter adapter;
    private ArrayList<Yng_AndroidContact> array_list;
    private static final int READ_CONTACTS = 100;
    private static final int WRITE_CONTACTS = 101;
    private static final int PICK_CONTACT_REQUEST = 1;
    String TAG="OkHttpConection";

    public ContactsFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2)
    {
        ContactsFragment fragment = new ContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        FloatingActionButton add_client_button = (FloatingActionButton) view.findViewById(R.id.add_button);
        add_client_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_INSERT);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (Integer.valueOf(Build.VERSION.SDK) > 14)
                    i.putExtra("finishActivityOnSaveCompleted", true); // Fix for 4.0.3 +
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });
        array_list = new ArrayList<Yng_AndroidContact>();
        adapter = new AndroidContactAdapter(getContext(), array_list);

        list = (ListView) view.findViewById(R.id.list);
        // Assigning the adapter to ListView
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_AndroidContact contact = adapter.getItem(position);
                if(contact.getUser()==null){
                    Uri uri = Uri.parse("smsto:"+contact.getAndroid_contact_TelefonNr());
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "Descarga la nueva aplicación de Compras,Ventas y Chat YINGUL app y comenzá a publicar gratis y chatea ahora https://play.google.com/store/apps/details?id=com.valecom.yingul");
                    startActivity(it);
                }
            }
        });
        loadContacts();
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
    public void onResume() {
        super.onResume();
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void loadContacts(){
        //progressDialog.show();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Log.e("Mostrar","mostrar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},
                        WRITE_CONTACTS);
            }else{
                readPermissions();
            }
        }else{
            readPermissions();
        }
    }
    public void readPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS);
            }else{
                fp_get_Android_Contacts();
            }
        }else{
            fp_get_Android_Contacts();
        }
    }

    public void fp_get_Android_Contacts() {
        array_list.clear();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        Yng_AndroidContact android_contact = new Yng_AndroidContact();
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        android_contact.setAndroid_contact_Name(name);
                        android_contact.setAndroid_contact_TelefonNr(phoneNo);
                        if(android_contact.getAndroid_contact_Name()!=null){
                            Log.e("Contacto",android_contact.getAndroid_contact_ID()+"---"+android_contact.getAndroid_contact_Name()+"---"+android_contact.getAndroid_contact_TelefonNr());
                            if(!checkRepeated(array_list,android_contact)) {
                                array_list.add(android_contact);
                            }
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        Collections.sort(array_list, new Comparator<Yng_AndroidContact>() {
            public int compare(Yng_AndroidContact obj1, Yng_AndroidContact obj2) {
                return obj1.getAndroid_contact_Name().compareTo(obj2.getAndroid_contact_Name());
            }
        });

        YingulcheckAndOrder(array_list);


    }

    public boolean checkRepeated(ArrayList<Yng_AndroidContact> array_list,Yng_AndroidContact android_contact){
        for (Yng_AndroidContact androidContact : array_list) {
            if(androidContact.getAndroid_contact_TelefonNr().contains(android_contact.getAndroid_contact_TelefonNr().replace(" ","").replace("+","").replace("-",""))||android_contact.getAndroid_contact_TelefonNr().contains(androidContact.getAndroid_contact_TelefonNr().replace(" ","").replace("+","").replace("-",""))){
                return true;
            }
        }
        return false;
    }

    public void YingulcheckAndOrder(ArrayList<Yng_AndroidContact> listBackup){
        for (int i=0; i<listBackup.size();i++) {
            if(i==(listBackup.size()-1)){
                getUserByPhoneNumber(listBackup.get(i),i,true);
            }else{
                getUserByPhoneNumber(listBackup.get(i),i,false);
            }
        }
        //adapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fp_get_Android_Contacts();
            } else {

            }
        }
        if (requestCode == WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readPermissions();
            } else {

            }
        }
    }
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("======","============contacto guardado");
        switch (requestCode) {
            case PICK_CONTACT_REQUEST:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    Log.e("======","============contacto guardado");
                    loadContacts();
                }
                break;
        }
    }

    public void getUserByPhoneNumber(final Yng_AndroidContact android_contact, final int position, boolean last)
    {
        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "user/getUserByPhoneNumber/"+android_contact.getAndroid_contact_TelefonNr().replace(" ","").replace("+","").replace("-",""), api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Yng_User user = new Yng_User();
                            Gson gson = new Gson();
                            user = gson.fromJson(String.valueOf(response), Yng_User.class);
                            android_contact.setUser(user);
                            array_list.set(position,android_contact);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-API-KEY", Network.API_KEY);
                return params;
            }
        };
        RequestQueue queue = MySingleton.getInstance(getContext().getApplicationContext()).getRequestQueue();
        postRequest.setTag(TAG);
        MySingleton.getInstance(getContext()).addToRequestQueue(postRequest);
        if(last){
            /*Collections.sort(array_list, new Comparator<Yng_AndroidContact>() {
                public int compare(Yng_AndroidContact o1, Yng_AndroidContact o2){
                    if(o1.getUser()==null && o2.getUser()==null){
                        return o1.getAndroid_contact_Name().compareTo(o2.getAndroid_contact_Name());
                    }
                    else if(o1.getUser()==null && o2.getUser()!=null){
                        return 1;
                    }
                    else if(o1.getUser()!=null && o2.getUser()==null){
                        return -1;
                    }
                    else{
                        return o1.getAndroid_contact_Name().compareTo(o2.getAndroid_contact_Name());
                    }
                }
            });*/
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            adapter.notifyDataSetChanged();
        }
    }

}
