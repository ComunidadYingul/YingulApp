package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.buy.BuySetPaymentTypeFragment;
import com.valecom.yingul.network.Network;

public class PaymentMethodActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Menu menu;
    private TextView textBankConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Medios de pago");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textBankConditions = (TextView) findViewById(R.id.textBankConditions);
        textBankConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentMethodActivity.this,BankConditionsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
