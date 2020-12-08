package kz.ikar.cities.model;

import javax.inject.Inject;

import io.reactivex.Single;
import kz.ikar.cities.entity.Pagination;
import kz.ikar.cities.model.API;
import kz.ikar.cities.entity.AuthResponse;
import kz.ikar.cities.di.provider.SchedulersProvider;
import kz.ikar.cities.model.AuthHolder;

public class MainRepository {
    @Inject
    API api;

    @Inject
    SchedulersProvider schedulers;

    @Inject
    AuthHolder authHolder;

    public Single<AuthResponse> auth(String username, String password) {
        return api.auth(username, password)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui());
    }

    public Single<Pagination> getData(int page) {
        return api.getData(authHolder.getCode(), page)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui());
    }

    public boolean isUserAuthorized() {
        return authHolder.getCode() != null;
    }

    public void clearAuthData() {
        authHolder.setCode(null);
    }

    public void setAuthCode(String code) {
        authHolder.setCode(code);
    }
}
