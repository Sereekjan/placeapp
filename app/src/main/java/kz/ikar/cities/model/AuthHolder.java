package kz.ikar.cities.model;

import android.content.Context;
import android.content.SharedPreferences;

import kz.ikar.cities.di.DI;
import toothpick.Toothpick;

public class AuthHolder {
    private static final String PREFS_NAME = "general";
    private static final String CODE = "code";

    private Context getContext() {
        return Toothpick.openScope(DI.APP_SCOPE)
            .getInstance(Context.class);
    }

    private SharedPreferences getPrefs() {
        return getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getCode() {
        return getPrefs().getString(CODE, null);
    }

    public void setCode(String code) {
        getPrefs().edit()
            .putString(CODE, code)
            .apply();
    }
}
