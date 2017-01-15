package com.thoughtworks.iot.buybuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.thoughtworks.iot.buybuddy.model.Cart;

import java.io.IOException;

public class SuccessActivity extends AppCompatActivity {
    Cart cart;
    LazyAdapter adapter;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        ObjectMapper mapper = new ObjectMapper();
        try {
            cart = mapper.readValue(bundle.getString("cart"), Cart.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        try {
            System.out.println(cart.redirectUrl);
            Bitmap bitmap = encodeAsBitmap(cart.redirectUrl);
            View viewById = findViewById(R.id.qrCode);
            ((ImageView) viewById).setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void startAgain(View v) {
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }

}
