package kz.ikar.cities.di.provider;

import io.reactivex.Scheduler;

public interface SchedulersProvider {

    public Scheduler ui();

    public Scheduler io();

}
