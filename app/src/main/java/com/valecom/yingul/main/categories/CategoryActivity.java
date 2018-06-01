package com.valecom.yingul.main.categories;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.valecom.yingul.R;

public class CategoryActivity extends AppCompatActivity {

    String categoryId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Categorías");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        categoryId = getIntent().getStringExtra("categoryId");
        Log.e("categoryId:----",""+categoryId);

        Bundle bundle = new Bundle();
        bundle.putString("categoryId",categoryId);

        SubCategoryFragment fragment = new SubCategoryFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
}
