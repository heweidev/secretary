package com.hewei.secretary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.tojson.NoteTypeAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;

import javax.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengyinpeng on 2018/1/4.
 */

public class Network {
    private static final String TAG = "OKHttp";
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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Note.class, new NoteTypeAdapter())
                .setDateFormat(DateFormat.LONG)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.100.247.196:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        mApi = retrofit.create(NotesApi.class);
    }

    public NotesApi getApi() {
        return mApi;
    }


    public static class StringConvertFactory extends Converter.Factory {
        @Nullable
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return super.responseBodyConverter(type, annotations, retrofit);
        }

        @Nullable
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        }
    }
}
