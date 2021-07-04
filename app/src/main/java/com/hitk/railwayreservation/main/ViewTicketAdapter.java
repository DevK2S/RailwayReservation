package com.hitk.railwayreservation.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.TicketModel;
import com.hitk.railwayreservation.model.TicketState;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class ViewTicketAdapter extends RecyclerView.Adapter<ViewTicketAdapter.ViewTicketViewHolder> {

    private final ArrayList<TicketModel> tickets;

    private final ViewTicketAdapter.ViewTicketOnClickListener onClickListener;

    ViewTicketAdapter(ArrayList<TicketModel> tickets, ViewTicketAdapter.ViewTicketOnClickListener onClickListener) {

        this.tickets = tickets;
        this.onClickListener = onClickListener;
    }

    
    @NonNull
    @NotNull
    @Override
    public ViewTicketViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewTicketViewHolder(
                layoutInflater.inflate(R.layout.item_ticket, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewTicketViewHolder holder, int position) {

        TicketModel ticket = tickets.get(position);

        if (ticket.getTicketState() == TicketState.BOOKED) {
            holder.ticketStatus.setText("BOOKED");
            holder.cancelTicket.setOnClickListener(v -> onClickListener.cancelBooking(position));
        } else {
            holder.ticketStatus.setText("CANCELLED");
            holder.cancelTicket.setVisibility(View.GONE);
        }

        holder.ticketPnr.setText("PNR: " + ticket.getPnrNumber().toUpperCase());

        holder.trainNumber.setText(String.valueOf(ticket.getTrainNumber()));

        holder.trainName.setText(ticket.getTrainName());

        holder.source.setText(ticket.getSource().replace("_", " "));

        holder.destination.setText(ticket.getDestination().replace("_", " "));
    
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        
        String deptTime = sdf.format(new Date(ticket.getDeptDate())) + " " + ticket
                .getDeptTime();
        holder.deptDate.setText(deptTime);

        String arrTime = sdf.format(new Date(ticket.getArrivalDate())) + " " + ticket
                .getArrivalTime();
        holder.arrDate.setText(arrTime);

        holder.noPassenger.setText("No of Passengers: " + ticket.getNumberOfPassengers());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public interface ViewTicketOnClickListener {
        void cancelBooking(int position);
    }

    public static class ViewTicketViewHolder extends RecyclerView.ViewHolder {

        TextView ticketStatus, ticketPnr, trainNumber, trainName, source, destination, deptDate, arrDate, noPassenger;

        Button cancelTicket;
        
        public ViewTicketViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ticketStatus = itemView.findViewById(R.id.tv_ticket_status);
            ticketPnr = itemView.findViewById(R.id.tv_ticket_pnr);
            trainNumber = itemView.findViewById(R.id.tv_train_number);
            trainName = itemView.findViewById(R.id.tv_train_name);
            source = itemView.findViewById(R.id.tv_train_source);
            destination = itemView.findViewById(R.id.tv_train_destination);
            deptDate = itemView.findViewById(R.id.tv_ticket_departure_time);
            arrDate = itemView.findViewById(R.id.tv_ticket_arrival_time);
            noPassenger = itemView.findViewById(R.id.tv_number_of_passenger);
            cancelTicket = itemView.findViewById(R.id.btn_cancel_ticket);
        }
    }
}
