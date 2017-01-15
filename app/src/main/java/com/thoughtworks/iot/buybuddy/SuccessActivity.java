package com.thoughtworks.iot.buybuddy;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.buybuddy.model.Cart;
import com.thoughtworks.iot.buybuddy.model.Product;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class SuccessActivity extends AppCompatActivity {
    Cart cart;
    LazyAdapter adapter;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_reader);
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        ObjectMapper mapper = new ObjectMapper();
        try {
            cart = mapper.readValue(bundle.getString("cart"), Cart.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupListViewAdapter();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    public void startAgain(View v){
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }

    private void setupListViewAdapter() {
        List<Product> products = cart.products;
        adapter = new LazyAdapter(this, R.layout.list_item, products);
        final ListView listView = (ListView) findViewById(R.id.listview);
        TextView heading = (TextView) findViewById(R.id.thanks);
        heading.setText("Thank you for shopping! We miss you already..");
        listView.setAdapter(adapter);
        TextView price = (TextView)findViewById(R.id.totalPrice);
        price.setText(cart.value+"");
        price.setVisibility(View.VISIBLE);
        TextView status = (TextView)findViewById(R.id.status);
        status.setText(cart.status);
        status.setVisibility(View.VISIBLE);
        Button payButton = (Button) findViewById(R.id.buttonPay);
        payButton.setVisibility(View.INVISIBLE);
        Button startAgain = (Button) findViewById(R.id.startAgain);
        startAgain.setVisibility(View.VISIBLE);


    }

}
