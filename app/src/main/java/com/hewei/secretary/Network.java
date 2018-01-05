package com.hewei.secretary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengyinpeng on 2018/1/4.
 */

public class Network {
    private NotesApi mApi;

    private static Network sInst;

    private Network() {

    }

    public static Network getInstance() {
        if (sInst == null) {
            sInst = new Network();
        }

        return sInst;
    }

    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.100.247.196:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = retrofit.create(NotesApi.class);
    }

    public NotesApi getApi() {
        return mApi;
    }
}
