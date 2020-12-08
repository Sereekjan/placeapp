package kz.ikar.cities.di.provider;

import javax.inject.Inject;
import javax.inject.Provider;

import kz.ikar.cities.ServerInfo;
import kz.ikar.cities.model.API;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProvider implements Provider<API> {
    private OkHttpClient okHttpClient;

    @Inject
    public ApiProvider(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public API get() {
        return new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(ServerInfo.HOST)
            .build()
            .create(API.class);
    }
}
