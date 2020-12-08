package kz.ikar.cities.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kz.ikar.cities.di.DI;
import kz.ikar.cities.entity.Pagination;
import kz.ikar.cities.entity.Place;
import kz.ikar.cities.entity.ServerCodes;
import kz.ikar.cities.model.MainRepository;
import kz.ikar.cities.navigation.Screens;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

public class MainViewModel extends ViewModel {
    public interface OnRequestListener {
        void onSubscribed();
        void onTerminated();
        void onError(String message);
        void onUpdate(List<Place> places);
    }

    Router router = Toothpick
            .openScope(DI.APP_SCOPE)
            .getInstance(Router.class);

    MainRepository mainRepository = Toothpick
            .openScope(DI.APP_SCOPE)
            .getInstance(MainRepository.class);

    public void loadMorePlaces(int page, final OnRequestListener listener) {
        Disposable disposable = mainRepository.getData(page)
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) {
                    listener.onSubscribed();
                }
            })
            .doAfterTerminate(new Action() {
                @Override
                public void run() {
                    listener.onTerminated();
                }
            })
            .subscribe(
                new Consumer<Pagination>() {
                    @Override
                    public void accept(Pagination pagination) {
                        if (pagination.getStatus().equals(ServerCodes.OK)) {
                            listener.onUpdate(pagination.getPlaces());
                        } else {
                            listener.onError("Произошла ошибка при попытке получить данные");
                        }
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        listener.onError("Что-то пошло не так. Попробуйте позднее");
                    }
                }
            );
    }

    public void openPlace(Place place) {
        router.navigateTo(new Screens.Details(place));
    }

    public void logout() {
        mainRepository.clearAuthData();
        router.newRootScreen(new Screens.Login());
    }

    public void exit() {
        router.exit();
    }
}
