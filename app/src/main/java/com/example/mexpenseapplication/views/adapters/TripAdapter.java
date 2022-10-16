package com.example.mexpenseapplication.views.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.databinding.TripListItemBinding;
import com.example.mexpenseapplication.entity.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {
    // Code thầy dạy nên dùng, k hiểu thì xem lại video
    // Cơ bản thì là tạo 1 viewHolder để adapt từng objects trong list thành các hàng trong recycler view.
    // Trong đó thì có gắn các listener và bind data lại thành 1 cái layout mình đã tạo.
    // Source: https://developer.android.com/develop/ui/views/layout/recyclerview

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bindData(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{

        private final TripListItemBinding binding;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TripListItemBinding.bind(itemView);
        }

        public void bindData(Trip trip){
            binding.tripName.setText(trip.getName());
            binding.tripDestination.setText(trip.getDestination());
            binding.tripAssessment.setText(trip.getRiskAssessment() ? "Assessment Required" : "Assessment Not Required");
            binding.tripAssessment.setTextColor(trip.getRiskAssessment() ? Color.RED : Color.GREEN);
            binding.tripStart.setText(trip.getStartDate());
            binding.tripEnd.setText(trip.getEndDate());
            binding.getRoot().setOnClickListener( view -> listener.onItemClick(trip.getId()));
        }
    }

    public interface TripItemListener {
        void onItemClick(int tripId);
    }

    private List<Trip> trips;

    private TripItemListener listener;

    public TripAdapter(List<Trip> trips, TripItemListener listener){
        this.trips = trips;
        this.listener = listener;
    }

}
