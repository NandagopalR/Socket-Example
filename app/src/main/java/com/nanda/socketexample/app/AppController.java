package com.nanda.socketexample.app;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nanda.socketexample.data.repo.SocketRepo;
import com.nanda.socketexample.socket.SocketManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {

    private static AppController appController;
    private SocketManager socketManager;
    private Gson gson;
    private SocketRepo socketRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        socketManager = SocketManager.getInstance();

        gson = new GsonBuilder().setLenient().serializeNulls().create();

    }

    public static AppController getInstance() {
        return appController;
    }

    public SocketManager getSocketManager() {
        return socketManager;
    }

    public SocketRepo getSocketRepo() {
        if (socketRepo == null) {
            socketRepo = createSocketRepo();
        }
        return socketRepo;
    }

    private SocketRepo createSocketRepo() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(httpClient)
                .baseUrl(ApiConstants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        return new SocketRepo(retrofit.create(SocketApi.class));
    }

}
