package com.thoughtworks.iot.buybuddy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.buybuddy.model.Cart;
import com.thoughtworks.iot.buybuddy.model.Product;
import com.thoughtworks.iot.buybuddy.service.AddProductService;
import com.thoughtworks.iot.buybuddy.service.DeleteProductService;
import com.thoughtworks.iot.buybuddy.service.PayService;

import java.io.IOException;
import java.util.List;


public class NfcReaderActivity extends Activity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    LazyAdapter adapter;
    Cart cart;
    Context context = this;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        setContentView(R.layout.activity_nfc_reader);
        ObjectMapper mapper = new ObjectMapper();
        try {
            cart = mapper.readValue(bundle.getString("cart"), Cart.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(cart.value !=0){
            TextView totalPrice = (TextView) findViewById(R.id.totalPrice);
            totalPrice.setText(""+cart.value);
            TextView status = (TextView) findViewById(R.id.status);
            status.setText(cart.status);
        }
        if(cart.value == 0){
            Button payButton = (Button) findViewById(R.id.buttonPay);
            payButton.setVisibility(View.INVISIBLE);
        }
        setupListViewAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addProducts(intent.getStringExtra("nfctag"));
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("ACTION_REFRESH"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setupListViewAdapter() {
        List<Product> products = cart.products;
        adapter = new LazyAdapter(this, R.layout.list_item, products);
        final ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }


    public void removeItem(View v) {
        Product itemToRemove = (Product) v.getTag();
        DeleteProductService service = new DeleteProductService(context,adapter);
        String[] params = new String[2];
        params[0] = cart._id;
        params[1] = itemToRemove.tagId;
        service.execute(params);
    }

    public void pay(View v){
        PayService service = new PayService(context);
        String[] params = new String[2];
        params[0] = cart._id;
        service.execute(params);
    }

    public void addProducts(String result){
        AddProductService service = new AddProductService(context,adapter);
        String[] params = new String[2];
        params[0] = cart._id;
        params[1] = result;
        service.execute(params);
    }


}
