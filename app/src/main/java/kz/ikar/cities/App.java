package kz.ikar.cities;

import android.app.Application;

import kz.ikar.cities.di.DI;
import kz.ikar.cities.di.modules.AppModule;
import toothpick.Toothpick;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initAppScope();
    }

    private void initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE)
            .installModules(new AppModule(getApplicationContext()));
    }
}
