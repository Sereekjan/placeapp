package kz.ikar.cities.di.modules;

import android.content.Context;

import kz.ikar.cities.di.provider.ApiProvider;
import kz.ikar.cities.di.provider.OkHttpClientProvider;
import kz.ikar.cities.di.provider.AppSchedulers;
import kz.ikar.cities.di.provider.SchedulersProvider;
import kz.ikar.cities.model.API;
import kz.ikar.cities.model.AuthHolder;
import okhttp3.OkHttpClient;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import toothpick.config.Module;

public class AppModule extends Module {
    public AppModule(Context context) {
        bind(Context.class).toInstance(context);
        bind(SchedulersProvider.class).toInstance(new AppSchedulers());

        Cicerone<Router> cicerone = Cicerone.create();
        bind(Router.class).toInstance(cicerone.getRouter());
        bind(NavigatorHolder.class).toInstance(cicerone.getNavigatorHolder());

        bind(OkHttpClient.class).toProvider(OkHttpClientProvider.class).providesSingleton();
        bind(API.class).toProvider(ApiProvider.class).providesSingleton();
        bind(AuthHolder.class).toInstance(new AuthHolder());
    }
}
