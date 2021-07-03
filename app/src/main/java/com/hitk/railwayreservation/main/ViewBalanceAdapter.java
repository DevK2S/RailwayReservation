package com.hitk.railwayreservation.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.TicketModel;
import com.hitk.railwayreservation.model.TicketState;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class ViewBalanceAdapter extends RecyclerView.Adapter<ViewBalanceAdapter.ViewBalanceViewHolder> {
	
	private final Context context;
	
	private final ArrayList<TicketModel> tickets;
	
	
	ViewBalanceAdapter (Context context, ArrayList<TicketModel> tickets) {
		
		this.context = context;
		this.tickets = tickets;
	}
	
	
	@NonNull
	@NotNull
	@Override
	public ViewBalanceViewHolder onCreateViewHolder (@NonNull @NotNull ViewGroup parent,
	                                                 int viewType) {
		
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		return new ViewBalanceViewHolder(
				layoutInflater.inflate(R.layout.item_balance, parent, false));
	}
	
	
	@Override
	public void onBindViewHolder (@NonNull @NotNull ViewBalanceViewHolder holder, int position) {
		
		TicketModel ticket = tickets.get(position);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
		
		if (ticket.getPaymentMade()) {
			holder.constraintLayout.setBackgroundColor(
					context.getResources().getColor(R.color.green, context.getTheme()));
			
			holder.paymentStatus.setText("Payment: COMPLETED");
			
			holder.paymentDate.setVisibility(View.VISIBLE);
			holder.paymentDate.setText(sdf.format(new Date(ticket.getPaymentDate())));
		} else {
			holder.constraintLayout.setBackgroundColor(
					context.getResources().getColor(R.color.errorTint, context.getTheme()));
			
			holder.paymentStatus.setText("Payment: DUE");
			
			holder.paymentDate.setVisibility(View.GONE);
		}
		
		if (ticket.getTicketState() == TicketState.BOOKED) {
			holder.ticketStatus.setText("BOOKED");
		} else {
			holder.ticketStatus.setText("CANCELLED");
		}
		
		holder.ticketPnr.setText("PNR: " + ticket.getPnrNumber().toUpperCase());
		
		holder.trainNumber.setText(String.valueOf(ticket.getTrainNumber()));
		
		holder.trainName.setText(ticket.getTrainName());
		
		holder.source.setText(ticket.getSource());
		
		holder.destination.setText(ticket.getDestination());
		
		
		String deptTime = sdf.format(new Date(ticket.getDeptDate())) + " " + ticket.getDeptTime();
		holder.deptDate.setText(deptTime);
		
		String arrTime = sdf.format(new Date(ticket.getArrivalDate())) + " " + ticket
				.getArrivalTime();
		holder.arrDate.setText(arrTime);
		
		holder.noPassenger.setText("No of Passengers: " + ticket.getNumberOfPassengers());
		
		holder.totalCost.setText("Total Cost: " + ticket.getTotalAmount());
	}
	
	
	@Override
	public int getItemCount ( ) {
		
		return tickets.size();
	}
	
	
	public static class ViewBalanceViewHolder extends RecyclerView.ViewHolder {
		
		ConstraintLayout constraintLayout;
		
		TextView paymentStatus, paymentDate, ticketStatus, ticketPnr, trainNumber, trainName,
				source, destination, deptDate, arrDate, noPassenger, totalCost;
		
		
		public ViewBalanceViewHolder (@NonNull @NotNull View itemView) {
			
			super(itemView);
			
			constraintLayout = itemView.findViewById(R.id.cl_view_balance);
			
			paymentStatus = itemView.findViewById(R.id.tv_payment_status);
			paymentDate = itemView.findViewById(R.id.tv_payment_date);
			
			ticketStatus = itemView.findViewById(R.id.tv_ticket_status);
			ticketPnr = itemView.findViewById(R.id.tv_ticket_pnr);
			trainNumber = itemView.findViewById(R.id.tv_train_number);
			trainName = itemView.findViewById(R.id.tv_train_name);
			
			source = itemView.findViewById(R.id.tv_train_source);
			destination = itemView.findViewById(R.id.tv_train_destination);
			deptDate = itemView.findViewById(R.id.tv_ticket_departure_time);
			arrDate = itemView.findViewById(R.id.tv_ticket_arrival_time);
			
			noPassenger = itemView.findViewById(R.id.tv_number_of_passenger);
			totalCost = itemView.findViewById(R.id.tv_total_cost);
		}
		
	}
	
}
