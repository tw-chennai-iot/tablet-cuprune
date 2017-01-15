package com.thoughtworks.iot.buybuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.thoughtworks.iot.buybuddy.service.CreateCartService;

public class ShoppingActivity extends AppCompatActivity {
    private static final String TAG = "MyFirebaseIIDService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    void startShopping(View view) {
        CreateCartService createCartService = new CreateCartService(this);
        String[] params = new String[1];
        params[0] = "postCart";
        createCartService.execute(params);
    }
}
