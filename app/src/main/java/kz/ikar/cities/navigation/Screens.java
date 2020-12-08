package kz.ikar.cities.navigation;

import androidx.fragment.app.Fragment;

import kz.ikar.cities.entity.Place;
import kz.ikar.cities.ui.fragment.DetailsFragment;
import kz.ikar.cities.ui.fragment.LoginFragment;
import kz.ikar.cities.ui.fragment.MainFragment;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {
    public static class Login extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return LoginFragment.newInstance();
        }
    }

    public static class Main extends SupportAppScreen {
        @Override
        public Fragment getFragment() {
            return MainFragment.newInstance();
        }
    }

    public static class Details extends SupportAppScreen {
        private Place place;

        public Details(Place place) {
            this.place = place;
        }

        @Override
        public Fragment getFragment() {
            return DetailsFragment.newInstance(place);
        }
    }
}
