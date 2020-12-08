package kz.ikar.cities.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import kz.ikar.cities.R;
import kz.ikar.cities.ServerInfo;
import kz.ikar.cities.entity.Place;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    public interface OnPlacesListener {
        void onClick(Place place);
        void onLoadMore(int page);
    }

    private List<Place> mPlaces;
    private OnPlacesListener onPlacesListener;

    public PlacesAdapter(List<Place> places, OnPlacesListener onPlacesListener) {
        this.mPlaces = places;
        this.onPlacesListener = onPlacesListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false)
        );
    }

    @Override
    public int getItemCount() {
        return mPlaces == null ? 0 : mPlaces.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.onBind(position);

        if (position == mPlaces.size() - 1) {
            onPlacesListener.onLoadMore((position + 1)/ 10 + 1);
        }
    }

    public void addItems(List<Place> places) {
        mPlaces.addAll(places);
        if (mPlaces.size() <= 10) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(mPlaces.size() - places.size(), places.size());
        }
    }

    public void clear() {
        mPlaces.clear();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout placeLayout;
        private ImageView imageView;
        private TextView titleTextView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            placeLayout = itemView.findViewById(R.id.placeLayout);
        }

        public void onBind(int position) {
            final Place place = mPlaces.get(position);

            titleTextView.setText(place.getName());

            placeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPlacesListener.onClick(place);
                }
            });

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
        }
    }
}