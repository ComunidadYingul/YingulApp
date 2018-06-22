package com.valecom.yingul.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QueryListAdapter;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoFragment;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueriesItemActivity extends AppCompatActivity {
    public static final String TAG = "ItemPickerActivity";

    Toolbar toolbar;
    Yng_User user;
    private Yng_Ubication userUbication;
    MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private String queries1;
    private ListView list;
    private QueryListAdapter adapter;
    private ArrayList<Yng_Query> array_list;
    private Button buttonNewQuestion;
    private EditText editNewQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queries_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Preguntas al vendedor");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queries1 = (String)getIntent().getSerializableExtra("queries1");
        array_list = new ArrayList<Yng_Query>();
        adapter = new QueryListAdapter(this, array_list);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        buttonNewQuestion = (Button) findViewById(R.id.buttonNewQuestion);
        editNewQuestion = (EditText) findViewById(R.id.editNewQuestion);

        buttonNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunCreateNewQuery();
            }
        });

        try {
            listQueries();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void listQueries() throws JSONException {
        JSONArray queries = new JSONArray(queries1);
        array_list.clear();
        for (int i = 0; i < queries.length(); i++) {
            JSONObject obj = queries.getJSONObject(i);
            Yng_Query query = new Yng_Query();
            query.setQueryId(obj.optLong("queryId"));
            query.setQuery(obj.optString("query"));
            query.setAnswer(obj.getString("answer"));
            query.setDate(obj.getString("date"));
            query.setStatus(obj.optString("status"));
            Yng_Item item = new Yng_Item();
            Gson gson = new Gson();
            item = gson.fromJson(String.valueOf(obj.optJSONObject("yng_Item")), Yng_Item.class);
            Yng_User buyer = new Yng_User();
            buyer = gson.fromJson(String.valueOf(obj.optJSONObject("user")), Yng_User.class);
            query.setYng_Item(item);
            query.setUser(buyer);
            array_list.add(query);
        }

        adapter.notifyDataSetChanged();
    }
    public void RunCreateNewQuery(){
        SharedPreferences settings = this.getSharedPreferences(LoginActivity.SESSION_USER, this.MODE_PRIVATE);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(this, LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
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
            user.setYng_Ubication(userUbication);

            Yng_Query newQuery = new Yng_Query();
            newQuery.setQuery(editNewQuestion.getText().toString().trim());
            //newQuery.setYng_Item(itemTemp);
            newQuery.setUser(user);
            Gson gson = new Gson();
            String jsonBody = gson.toJson(newQuery);
            Log.e("query final", jsonBody);
            //requestArrayPost1(Network.API_URL + "query/create",jsonBody);
        }
    }

}