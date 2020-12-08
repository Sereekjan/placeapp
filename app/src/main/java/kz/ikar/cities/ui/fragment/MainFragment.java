package kz.ikar.cities.ui.fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import kz.ikar.cities.R;
import kz.ikar.cities.entity.Place;
import kz.ikar.cities.ui.adapter.PlacesAdapter;
import kz.ikar.cities.ui.viewmodel.MainViewModel;

public class MainFragment extends BaseFragment {

    private MainViewModel mViewModel;
    private Toolbar toolbar;
    private RecyclerView placesRecyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeToRefresh;

    private boolean showMainProgress = true;
    private static final int FIRST_PAGE = 1;

    private PlacesAdapter adapter = new PlacesAdapter(
        new ArrayList(),
        new PlacesAdapter.OnPlacesListener() {
            @Override
            public void onLoadMore(int page) {
                loadMorePlaces(page);
            }

            @Override
            public void onClick(Place place) {
                mViewModel.openPlace(place);
            }
        }
    );

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainViewModel.OnRequestListener onRequestListener = new MainViewModel.OnRequestListener() {
        @Override
        public void onSubscribed() {
            if (showMainProgress) {
                placesRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTerminated() {
            if (showMainProgress) {
                progressBar.setVisibility(View.GONE);
                placesRecyclerView.setVisibility(View.VISIBLE);
            }

            if (showMainProgress) {
                showMainProgress = false;
            }

            if (swipeToRefresh.isRefreshing()) {
                swipeToRefresh.setRefreshing(false);
            }
        }

        @Override
        public void onError(String message) {
            Snackbar.make(placesRecyclerView, message, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onUpdate(List<Place> places) {
            adapter.addItems(places);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        placesRecyclerView = view.findViewById(R.id.placesRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logoutAction) {
                    new AlertDialog.Builder(getContext())
                        .setTitle("Внимание")
                        .setMessage("Вы действительно хотите выйти с аккаунта?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mViewModel.logout();
                            }})
                        .setNegativeButton("Отмена", null).show();
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        placesRecyclerView.setHasFixedSize(true);
        placesRecyclerView.setLayoutManager(layoutManager);
        placesRecyclerView.setAdapter(adapter);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMorePlaces(FIRST_PAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        loadMorePlaces(FIRST_PAGE);
    }

    private void loadMorePlaces(int page) {
        if (page == FIRST_PAGE) {
            adapter.clear();
        }

        mViewModel.loadMorePlaces(page, onRequestListener);
    }

    @Override
    public void onBackPressed() {
        mViewModel.exit();
    }
}
