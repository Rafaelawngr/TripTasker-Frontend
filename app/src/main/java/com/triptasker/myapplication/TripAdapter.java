package com.triptasker.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    private List<Trip> tripList;

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripList, OnItemClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    public void updateTrips(List<Trip> trips) {
        this.tripList.clear();
        this.tripList.addAll(trips);
        notifyDataSetChanged();
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
        Log.d("TripAdapter", "Título da Viagem: " + trip.getTitle());
        holder.tripName.setText(trip.getTitle());
        holder.bind(trip, listener);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tripName;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tvNomeViagem);
        }

        public void bind(final Trip trip, final OnItemClickListener listener) {
            tripName.setText(trip.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(trip));
        }
    }
}