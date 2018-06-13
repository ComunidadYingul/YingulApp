package com.valecom.yingul.main.myAccount.yingulPay;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.valecom.yingul.R;
import com.valecom.yingul.adapter.TransactionListAdapter;

public class YingulPayActivity extends AppCompatActivity {

    LinearLayout lytTransactionCash,lytWithdrawCash,lytDetailCash;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yingul_pay);

        mContext = this;

        lytDetailCash = (LinearLayout) findViewById(R.id.lytDetailCash);
        lytWithdrawCash = (LinearLayout) findViewById(R.id.lytWithdrawCash);
        lytTransactionCash = (LinearLayout) findViewById(R.id.lytTransactionCash);

        DetailCashFragment fragment = new DetailCashFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Container_Order, fragment).commit();

        lytDetailCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailCashFragment fragment = new DetailCashFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, fragment).commit();
            }
        });

        lytWithdrawCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithdrawCashFragment fragment = new WithdrawCashFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, fragment).commit();
            }
        });

        lytTransactionCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionCashFragment fragment = new TransactionCashFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, fragment).commit();
            }
        });
    }
}
