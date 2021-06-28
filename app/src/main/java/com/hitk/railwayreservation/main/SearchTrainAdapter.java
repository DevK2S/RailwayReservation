package com.hitk.railwayreservation.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class SearchTrainAdapter
        extends RecyclerView.Adapter<SearchTrainAdapter.SearchTrainViewHolder> {

    private final ArrayList<TrainModel> trains;

    private final SearchTrainOnClickListener onClickListener;


    SearchTrainAdapter(ArrayList<TrainModel> trains, SearchTrainOnClickListener onClickListener) {

        this.trains = trains;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @NotNull
    @Override
    public SearchTrainViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
                                                    int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new SearchTrainViewHolder(
                layoutInflater.inflate(R.layout.item_search_train, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchTrainViewHolder holder, int position) {

        TrainModel train = trains.get(position);

        holder.trainNumber.setText(String.valueOf(train.getNumber()));
        holder.trainName.setText(train.getName());
        holder.source.setText(train.getSource());
        holder.destination.setText(train.getDestination());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        String deptTime = sdf.format(new Date(train.getDepartureDate())) + " " + train
                .getDepartureTime();
        holder.deptTime.setText(deptTime);

        String arrTime = sdf.format(new Date(train.getArrivalDate())) + " " + train
                .getArrivalTime();
        holder.arrTime.setText(arrTime);

        holder.itemView.setOnClickListener(v -> {
            onClickListener.bookTrain(position);
        });

    }


    @Override
    public int getItemCount() {

        return trains.size();
    }


    public interface SearchTrainOnClickListener {

        void bookTrain(int position);

    }


    static class SearchTrainViewHolder extends RecyclerView.ViewHolder {

        TextView trainNumber, trainName, source, destination, deptTime, arrTime;


        public SearchTrainViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            trainNumber = itemView.findViewById(R.id.tv_train_number);
            trainName = itemView.findViewById(R.id.tv_train_name);
            source = itemView.findViewById(R.id.tv_train_source);
            destination = itemView.findViewById(R.id.tv_train_destination);
            deptTime = itemView.findViewById(R.id.tv_train_departure_time);
            arrTime = itemView.findViewById(R.id.tv_train_arrival_time);

        }

    }

}
