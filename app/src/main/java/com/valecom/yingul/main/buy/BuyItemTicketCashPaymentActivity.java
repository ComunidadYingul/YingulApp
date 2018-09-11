package com.valecom.yingul.main.buy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QueryListAdapter;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class BuyItemTicketCashPaymentActivity extends AppCompatActivity {
    public static final String TAG = "ItemPickerActivity";

    Toolbar toolbar;
    private TextView txtTitle;
    private Button btnShowTicket,btnDownloadTicket;
    private double cost;
    private String method, html, pdf;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item_ticket_cash_payment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cost = (double) getIntent().getSerializableExtra("cost");
        method = (String) getIntent().getSerializableExtra("method");
        html = (String) getIntent().getSerializableExtra("html");
        pdf = (String) getIntent().getSerializableExtra("pdf");

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Pagá $(ARS) "+cost+" en "+method+" para completar tu compra");
        btnShowTicket = (Button) findViewById(R.id.btnShowTicket);
        btnDownloadTicket = (Button) findViewById(R.id.btnDownloadTicket);

        btnShowTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(html));
                startActivity(viewIntent);
            }
        });

        btnDownloadTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(pdf));
                startActivity(viewIntent);
            }
        });

        new MaterialDialog.Builder(BuyItemTicketCashPaymentActivity.this)
                .title("Finalizado con éxito.")
                .content("El proceso termino con exito, asegurate a descargar el ticket para pagar. Nota: Tu compra permanecera pendiente hasta que completes el pago.")
                .positiveText("OK")
                .cancelable(false)
                .positiveColorRes(R.color.colorAccent)
                .callback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onPositive(MaterialDialog dialog)
                    {
                        dialog.dismiss();
                        if (dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                })
                .show();

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