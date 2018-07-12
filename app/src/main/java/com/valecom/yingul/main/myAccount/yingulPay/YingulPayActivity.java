package com.valecom.yingul.main.myAccount.yingulPay;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.valecom.yingul.R;
import com.valecom.yingul.adapter.TransactionListAdapter;

public class YingulPayActivity extends AppCompatActivity {

    LinearLayout lytTransactionCash,lytWithdrawCash,lytDetailCash,lytBanner;
    Context mContext;
    CardView card_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yingul_pay);

        mContext = this;

        lytDetailCash = (LinearLayout) findViewById(R.id.lytDetailCash);
        lytWithdrawCash = (LinearLayout) findViewById(R.id.lytWithdrawCash);
        lytTransactionCash = (LinearLayout) findViewById(R.id.lytTransactionCash);
        lytBanner = (LinearLayout) findViewById(R.id.lytBanner);
        card_view2 = (CardView) findViewById(R.id.card_view2);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels
        int dpi = metrics.densityDpi;

        Log.e("G--ancho:---",width+"");
        Log.e("G--alto:---",height+"");
        Log.e("G--dpi:---",dpi+"");
        Log.e("G--dpi x:---",metrics.xdpi+"");
        Log.e("G--dpi y:---",metrics.ydpi+"");

        lytBanner.getLayoutParams().height = height*1/5;

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
