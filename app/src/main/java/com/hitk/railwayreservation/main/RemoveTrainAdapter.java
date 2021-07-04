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

public class RemoveTrainAdapter
		extends RecyclerView.Adapter<RemoveTrainAdapter.RemoveTrainViewHolder> {
	
	private final ArrayList<TrainModel> trains;
	
	private final RemoveTrainOnClickListener onClickListener;
	
	
	RemoveTrainAdapter (ArrayList<TrainModel> trains, RemoveTrainOnClickListener onClickListener) {
		
		this.trains = trains;
		this.onClickListener = onClickListener;
	}


	@NonNull
	@NotNull
	@Override
	public RemoveTrainViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent,
													int viewType) {

		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		return new RemoveTrainViewHolder(
				layoutInflater.inflate(R.layout.item_remove_train, parent, false));
	}

	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

	@Override
	public void onBindViewHolder(@NonNull @NotNull RemoveTrainViewHolder holder, int position) {

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
		
		holder.delete.setOnClickListener(v -> onClickListener.deleteTrain(position));
	}
	
	
	@Override
	public int getItemCount ( ) {
		
		return trains.size();
	}
	
	
	public interface RemoveTrainOnClickListener {
		
		void deleteTrain (int position);
		
	}
	
	
	static class RemoveTrainViewHolder extends RecyclerView.ViewHolder {
		
		TextView trainNumber, trainName, source, destination, deptTime, arrTime;
		
		ImageButton delete;
		
		
		public RemoveTrainViewHolder (@NonNull @NotNull View itemView) {
			
			super(itemView);
			
			trainNumber = itemView.findViewById(R.id.tv_train_number);
			trainName = itemView.findViewById(R.id.tv_train_name);
			source = itemView.findViewById(R.id.tv_train_source);
			destination = itemView.findViewById(R.id.tv_train_destination);
			deptTime = itemView.findViewById(R.id.tv_train_departure_time);
			arrTime = itemView.findViewById(R.id.tv_train_arrival_time);
			
			delete = itemView.findViewById(R.id.ib_train_delete);
		}
		
	}
	
}
