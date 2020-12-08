package kz.ikar.cities.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kz.ikar.cities.di.DI;
import kz.ikar.cities.entity.ServerCodes;
import kz.ikar.cities.entity.AuthResponse;
import kz.ikar.cities.model.MainRepository;
import kz.ikar.cities.navigation.Screens;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

public class LoginViewModel extends ViewModel {

    public interface OnAuthErrorListener {
        void onError(String message);
    }

    Router router = Toothpick
        .openScope(DI.APP_SCOPE)
        .getInstance(Router.class);

    MainRepository mainRepository = Toothpick
        .openScope(DI.APP_SCOPE)
        .getInstance(MainRepository.class);

    public void login(String login, String password, final OnAuthErrorListener listener) {
        Disposable disposable = mainRepository.auth(login, password)
            .subscribe(
                new Consumer<AuthResponse>() {
                    @Override
                    public void accept(AuthResponse authResponse) {
                        if (authResponse.getStatus().equals(ServerCodes.OK)) {
                            setAuthCode(authResponse.getCode());
                            router.newRootScreen(new Screens.Main());
                        } else {
                            listener.onError("Неверные данные");
                        }
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        listener.onError("Что-то пошло не так, попробуйте позже");
                    }
                }
            );
    }

    private void setAuthCode(String code) {
        mainRepository.setAuthCode(code);
    }

    public void exit() {
        router.exit();
    }
}
