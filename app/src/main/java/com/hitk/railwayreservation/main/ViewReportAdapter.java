package com.hitk.railwayreservation.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewReportAdapter extends RecyclerView.Adapter<ViewReportAdapter.ViewReportViewHolder> {

    private final ArrayList<TrainModel> trains;

    public ViewReportAdapter(ArrayList<TrainModel> trains) {
        this.trains = trains;
    }

    @NonNull
    @NotNull
    @Override
    public ViewReportViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewReportAdapter.ViewReportViewHolder(
                layoutInflater.inflate(R.layout.item_report, parent, false));
    }

    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewReportViewHolder holder, int position) {

        TrainModel train = trains.get(position);

        holder.trainNumber.setText(String.valueOf(train.getNumber()));
        holder.trainName.setText(train.getName());
        holder.source.setText(train.getSource().replace("_", " "));
        holder.destination.setText(train.getDestination().replace("_", " "));


        String deptTime = sdf.format(new Date(train.getDepartureDate())) + " " + train
                .getDepartureTime();
        holder.deptTime.setText(deptTime);

        String arrTime = sdf.format(new Date(train.getArrivalDate())) + " " + train
                .getArrivalTime();
        holder.arrTime.setText(arrTime);

        holder.numberOfseats.setText("Number of Seats : " + String.valueOf(train.getNumberOfSeats()));

        holder.seatsAvailable.setText("Seats Available : " + String.valueOf(train.getSeatsAvailable()));


    }

    @Override
    public int getItemCount() {
        return trains.size();
    }

    public static class ViewReportViewHolder extends RecyclerView.ViewHolder {
        TextView trainNumber, trainName, source, destination, deptTime, arrTime, numberOfseats, seatsAvailable;

        public ViewReportViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            trainNumber = itemView.findViewById(R.id.tv_train_number);
            trainName = itemView.findViewById(R.id.tv_train_name);
            source = itemView.findViewById(R.id.tv_train_source);
            destination = itemView.findViewById(R.id.tv_train_destination);
            deptTime = itemView.findViewById(R.id.tv_train_departure_time);
            arrTime = itemView.findViewById(R.id.tv_train_arrival_time);
            numberOfseats = itemView.findViewById(R.id.tv_number_of_seat);
            seatsAvailable = itemView.findViewById(R.id.tv_seats_available);
        }
    }
}
