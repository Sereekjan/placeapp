package kz.ikar.cities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import javax.inject.Inject;

import kz.ikar.cities.di.DI;
import kz.ikar.cities.model.MainRepository;
import kz.ikar.cities.navigation.Screens;
import kz.ikar.cities.ui.fragment.BaseFragment;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

public class MainActivity extends AppCompatActivity {

    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;

    @Inject
    MainRepository mainRepository;

    private Navigator navigator = new SupportAppNavigator(this, R.id.container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (mainRepository.isUserAuthorized()) {
            router.newRootScreen(new Screens.Main());
        } else {
            router.newRootScreen(new Screens.Login());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
