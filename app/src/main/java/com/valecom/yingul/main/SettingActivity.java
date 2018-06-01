package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.buy.BuyItemSetCashPaymentFragment;
import com.valecom.yingul.network.MySingleton;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = "SettingActivity";
    private MaterialDialog progressDialog;
    private JSONObject api_parameter;

    private String api_key,currency,address;
    private Long user_id;
    private Toolbar toolbar;

    public static final String CURRENCY_SYMBOL_KEY = "currency_symbol";
    public static final String ADDRESS_SYMBOL_KEY = "address";

    private Button buttonSell;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

        //Checks whether a user is logged in, otherwise redirects to the Login screen
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        api_key = settings.getString("api_key", "");
        user_id = settings.getLong("id", 0);

        currency = settings.getString(CURRENCY_SYMBOL_KEY, "$");
        address = settings.getString(ADDRESS_SYMBOL_KEY, "$");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_settings);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        Button settings_logout_button = (Button) findViewById(R.id.settings_logout_button);
        settings_logout_button.setOnClickListener(this);




    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.settings_logout_button:
                SharedPreferences.Editor settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();
                settings.clear();
                settings.commit();
                Intent settingsIntent = new Intent(SettingActivity.this, LoginActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingsIntent);
                finish();
                break;

        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        MySingleton.getInstance(this).getRequestQueue().cancelAll(TAG);
    }


}
