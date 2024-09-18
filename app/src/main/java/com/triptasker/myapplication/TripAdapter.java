package com.triptasker.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList = new ArrayList<>();

    private final OnItemClickListener listener;

    // Interface para escutar cliques
    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    // Construtor
    public TripAdapter(List<Trip> tripList, OnItemClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.bind(trip, listener);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripName;
        private final ImageView tripImage;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tvNomeViagem);
            tripImage = itemView.findViewById(R.id.ivTripImage);
        }

        public void bind(final Trip trip, final OnItemClickListener listener) {
            tripName.setText(trip.getTitle());
            // Defina a imagem usando Glide ou outra biblioteca
//             Glide.with(itemView.getContext()).load(trip.getImageUrl()).into(tripImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(trip);
                }
            });
        }
    }
}

