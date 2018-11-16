package utils;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Ashish.Kumar on 23-01-2018.
 */

public class WebApiCall {
    OkHttpClient client;
    private  OkHttpClient.Builder client1;
    public static final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Context context;


    public WebApiCall(Context context) {
        client = new OkHttpClient();
        this.context = context;
    }

    public void getData(String url) {
        client.newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        try {
            client.newCall(request).execute();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

    }



}

