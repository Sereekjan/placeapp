package kz.ikar.cities.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pagination {
    @SerializedName("status")
    private String status;
    @SerializedName("page")
    private int page;
    @SerializedName("data")
    private List<Place> places;

    public Pagination(String status, int page, List<Place> places) {
        this.status = status;
        this.page = page;
        this.places = places;
    }

    public String getStatus() {
        return status;
    }

    public int getPage() {
        return page;
    }

    public List<Place> getPlaces() {
        return places;
    }
}
