package kz.ikar.cities.model;

import io.reactivex.Single;
import kz.ikar.cities.entity.AuthResponse;
import kz.ikar.cities.entity.Pagination;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    /*
    * Auth request
    * */
    @GET("/test/auth.cgi")
    public Single<AuthResponse> auth(
        @Query("username") String username,
        @Query("password") String password
    );

    /*
    * Get places request
    * */
    @GET("/test/data.cgi")
    public Single<Pagination> getData(
        @Query("code") String code,
        @Query("p") int page
    );

}
