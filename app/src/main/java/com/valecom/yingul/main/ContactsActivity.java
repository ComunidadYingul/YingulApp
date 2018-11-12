package com.valecom.yingul.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {

    private Menu menu;

    private JSONObject api_parameter;
    private ContactsFragment.OnFragmentInteractionListener mListener;
    private MaterialDialog progressDialog;
    private ListView list;
    private AndroidContactAdapter adapter;
    private ArrayList<Yng_AndroidContact> array_list;
    private static final int READ_CONTACTS = 100;
    private static final int WRITE_CONTACTS = 101;
    private static final int PICK_CONTACT_REQUEST = 1;
    String TAG="OkHttpConection";

    private static final int i = 100;
    Cursor c;
    ArrayList<Yng_AndroidContact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contactos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setBackgroundColor(getResources().getColor(R.color.yngOrange));

        FloatingActionButton add_client_button = (FloatingActionButton) findViewById(R.id.add_button);
        add_client_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_INSERT);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (Integer.valueOf(Build.VERSION.SDK) > 14)
                    i.putExtra("finishActivityOnSaveCompleted", true); // Fix for 4.0.3 +
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });

        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Yng_AndroidContact contact = adapter.getItem(position);
                if(contact.getUser()==null){
                    if(contact.getAndroid_contact_Name().equals("Nuevo grupo")){

                    }else {
                        Uri uri = Uri.parse("smsto:" + contact.getAndroid_contact_TelefonNr());
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "Descarga la nueva aplicación de Compras,Ventas y Chat YINGUL app y comenzá a publicar gratis y chatea ahora https://play.google.com/store/apps/details?id=com.valecom.yingul");
                        startActivity(it);
                    }
                }
            }
        });

        contacts = new ArrayList<Yng_AndroidContact>();

        if(checkPermission()){
            getContacts();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, i);
        }
    }

    public boolean checkPermission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permission == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == i){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getContacts();
            }else{
                Toast.makeText(this, "Necesitas permisos", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getContacts(){
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME+ " ASC ");
        contacts.clear();

        int n = 0;

        String comp = "";

        while (c.moveToNext()){
            String id = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Yng_AndroidContact android_contact = new Yng_AndroidContact();
            android_contact.setAndroid_contact_Name(name);
            android_contact.setAndroid_contact_TelefonNr(number);

            Log.e("Contactos:---", id+" - "+name);

            if(!name.equals(comp)){
                contacts.add(android_contact);
            }
            comp = name;

            //if(n%2 == 0) {
                //contacts.add(android_contact);
            //}
            //n++;
        }

        Log.e("Total Contacts:  ", String.valueOf(contacts.size()));

        adapter = new AndroidContactAdapter(getApplicationContext(), contacts);

        list.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        Log.e("======","============contacto guardado");
        switch (requestCode) {
            case PICK_CONTACT_REQUEST:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    Log.e("======","============contacto guardado");
                    Toast.makeText(this, "Contacto guardado exitosamente.", Toast.LENGTH_SHORT).show();
                    getContacts();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_update:

                getContacts();

                return true;

            case R.id.menu_search:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void getUserByPhoneNumber(final Yng_AndroidContact android_contact, final int position, boolean last)
    {
        if(android_contact.getAndroid_contact_TelefonNr() != null) {
            JsonObjectRequest postRequest = new JsonObjectRequest
                    (Request.Method.GET, Network.API_URL + "user/getUserByPhoneNumber/" + android_contact.getAndroid_contact_TelefonNr().replace(" ", "").replace("+", "").replace("-", ""), api_parameter, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Yng_User user = new Yng_User();
                                Gson gson = new Gson();
                                user = gson.fromJson(String.valueOf(response), Yng_User.class);
                                android_contact.setUser(user);
                                contacts.set(position, android_contact);
                            } catch (Exception ex) {
                                Toast.makeText(getApplicationContext(), "-----  "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("exception----", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-API-KEY", Network.API_KEY);
                    return params;
                }
            };
            RequestQueue queue = MySingleton.getInstance(getApplicationContext().getApplicationContext()).getRequestQueue();
            postRequest.setTag(TAG);
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
            if (last) {
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

}
