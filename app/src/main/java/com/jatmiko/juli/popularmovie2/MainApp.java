package com.jatmiko.juli.popularmovie2;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jatmiko.juli.popularmovie2.api.RetrofitApi;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Miko on 10/08/2017.
 */

public class MainApp extends Application {
    private static MainApp instance;
    private Retrofit retrofit;
    private EventBus eventBus;
    private Gson gson;

    public MainApp() {
        instance = this;
    }

    public static MainApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createEventBus();
        createRetrofitClient();
        createGson();
    }

    private void createGson() {
        gson = new GsonBuilder().create();
    }

    private void createRetrofitClient() {
        retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void createEventBus() {
        eventBus = EventBus.builder().logNoSubscriberMessages(false).sendNoSubscriberEvent(false).build();
    }

    public Retrofit getRetrofitClient() {
        return retrofit;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Gson getGson() {
        return gson;
    }

    public RetrofitApi getApiService() {
        return getRetrofitClient().create(RetrofitApi.class);
    }
}
