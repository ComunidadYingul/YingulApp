package com.valecom.yingul.main.rememberPassword;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoFragment;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_ResetPassword;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RememberPasswordActivity extends AppCompatActivity {
    public static final String TAG = "ItemPickerActivity";

    Toolbar toolbar;
    Yng_User user;
    Yng_ResetPassword resetPassword;
    MaterialDialog progressDialog;
    private JSONObject api_parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("¿Olvidó su contraseña?");
        user = new Yng_User();
        resetPassword = new Yng_ResetPassword();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String in = (String) getIntent().getSerializableExtra("rest");
        if (in.equals("rest")) {
            RememberPasswordSetCodeFragment fragment = new RememberPasswordSetCodeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        } else {


        RememberPasswordFragment fragment = new RememberPasswordFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
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

}