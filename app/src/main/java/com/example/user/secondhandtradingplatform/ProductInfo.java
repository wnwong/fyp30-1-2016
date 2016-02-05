package com.example.user.secondhandtradingplatform;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmCamera;
import RealmModel.RealmGadget;
import RealmModel.RealmProduct;
import RealmQuery.QueryCamera;
import adapter.ProductAdapter;
import io.realm.RealmResults;

public class ProductInfo extends AppCompatActivity {
     List<RealmGadget> gadgets = new ArrayList<>();
     public static RealmProduct realmProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView rv;
        rv = (RecyclerView) findViewById(R.id.rview);
        rv.setHasFixedSize(true);

        //use a linear layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        QueryCamera queryCamera = new QueryCamera(this);
        // Retrieve posts of specific gadget
        RealmResults<RealmGadget> result = queryCamera.retrieveProductsByModel(realmProduct.getModel());
        for(int i=0; i<result.size(); i++){
            gadgets.add(result.get(i));
        }
        ProductAdapter adapter =
                new ProductAdapter(gadgets, realmProduct.getBrand() + " " + realmProduct.getModel(), realmProduct.getPrice(), realmProduct.getOs(), realmProduct.getMonitor(), realmProduct.getCamera(), realmProduct.getPath());
        rv.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
