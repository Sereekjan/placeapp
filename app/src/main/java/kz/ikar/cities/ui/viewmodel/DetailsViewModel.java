package kz.ikar.cities.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import kz.ikar.cities.di.DI;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

public class DetailsViewModel extends ViewModel {

    Router router = Toothpick
        .openScope(DI.APP_SCOPE)
        .getInstance(Router.class);

    public void exit() {
        router.exit();
    }
}
