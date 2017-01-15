package com.thoughtworks.iot.buybuddy.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.buybuddy.SuccessActivity;
import com.thoughtworks.iot.buybuddy.model.Cart;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayService extends AsyncTask<String, Void, String> {
    String url;
    Retrofit retrofit;
    RestAPI restInt;
    Context context;

    public PayService(Context context) {
        this.context = context;
        url = "http://ec2-54-255-184-116.ap-southeast-1.compute.amazonaws.com:3000";
        retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        restInt = retrofit.create(RestAPI.class);
    }

    @Override
    protected String doInBackground(String... params) {
        Object result = null;
        String stringResult = null;
        try {
            result = findMethodAndExecute(params);
            ObjectMapper mapper = new ObjectMapper();
            stringResult = mapper.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringResult;
    }

    private Object findMethodAndExecute(String[] params) throws IOException {
        Response<Cart> cart = restInt.get(params[0]).execute();
        return cart.body();
    }

    @Override
    protected void onPostExecute(String result) {
        Bundle bundle = new Bundle();
        bundle.putString("cart",result);
        Intent intent = new Intent(context, SuccessActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
