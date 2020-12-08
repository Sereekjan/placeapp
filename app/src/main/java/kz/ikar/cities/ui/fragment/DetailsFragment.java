package kz.ikar.cities.ui.fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import kz.ikar.cities.R;
import kz.ikar.cities.ServerInfo;
import kz.ikar.cities.entity.Place;
import kz.ikar.cities.ui.viewmodel.DetailsViewModel;

public class DetailsFragment extends BaseFragment implements OnMapReadyCallback {

    private DetailsViewModel mViewModel;

    private Toolbar toolbar;
    private ImageView imageView;
    private TextInputEditText nameEditText;
    private TextInputEditText countryEditText;
    private MapView mapView;
    private GoogleMap gMap;

    private Place place;
    private static float ZOOM_LEVEL = 14F;

    public static DetailsFragment newInstance(Place place) {
        DetailsFragment fragment = new DetailsFragment();
        fragment.place = place;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.exit();
            }
        });

        imageView = view.findViewById(R.id.imageView);
        nameEditText = view.findViewById(R.id.nameEditText);
        countryEditText = view.findViewById(R.id.countryEditText);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        mapView.getMapAsync(this);
        Glide.with(imageView)
            .load(ServerInfo.IMG_URL)
            .addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    imageView.setImageResource(R.drawable.ic_picture);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imageView.setImageDrawable(resource);
                    return false;
                }
            })
            .apply(RequestOptions.circleCropTransform())
            .into(imageView);

        nameEditText.setText(place.getName());
        countryEditText.setText(place.getCountry());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        showMarker();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();

        toolbar = null;
        imageView = null;
        nameEditText = null;
        countryEditText = null;
        mapView = null;
        gMap = null;

        super.onDestroyView();
    }

    private void showMarker() {
        LatLng position = new LatLng(place.getLat(), place.getLon());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        gMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, ZOOM_LEVEL);
        gMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onBackPressed() {
        mViewModel.exit();
    }
}
