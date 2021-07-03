package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.TicketModel;
import com.hitk.railwayreservation.model.TicketState;
import com.hitk.railwayreservation.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewTicketsFragment extends Fragment {
	
	private static final String TAG = "ViewTicket Fragment";
	
	private final ArrayList<TicketModel> tickets = new ArrayList<>();
	
	private RecyclerView recyclerView;
	
	private ViewTicketAdapter viewTicketAdapter;
	
	private LinearLayout emptyLayout;
	
	private ProgressBar progressBar;
	
	private UserModel user;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_view_tickets, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("View Tickets");
		
		recyclerView = view.findViewById(R.id.rv_view_ticket);
		emptyLayout = view.findViewById(R.id.ll_empty_view_ticket);
		progressBar = view.findViewById(R.id.pb_view_ticket);
		progressBar.setVisibility(View.VISIBLE);
		
		getUser();
	}
	
	
	private void setUpData ( ) {
		
		ArrayList<String> ticketNumberList = getTicketNumbers();
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TICKETS)
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot dataSnapshot) {
				                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
					                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						                String ticketId = snapshot.getKey();
						                if (ticketNumberList.contains(ticketId)) {
							                tickets.add(snapshot.getValue(TicketModel.class));
						                }
					                }
					                setUpRecyclerView();
				                } else {
					                emptyLayout.setVisibility(View.VISIBLE);
				                }
			                }
			
			
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
				                progressBar.setVisibility(View.INVISIBLE);
				                emptyLayout.setVisibility(View.VISIBLE);
			                }
		                });
		
	}
	
	
	private void setUpRecyclerView ( ) {
		
		progressBar.setVisibility(View.INVISIBLE);
		
		viewTicketAdapter = new ViewTicketAdapter(tickets, this::cancelBooking);
		recyclerView.addItemDecoration(
				new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setAdapter(viewTicketAdapter);
		
	}
	
	
	private void cancelBooking (int i) {
		
		progressBar.setVisibility(View.VISIBLE);
		
		TicketModel ticket = tickets.get(i);
		ticket.setTicketState(TicketState.CANCELLED);
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TICKETS)
		                .child(ticket.getPnrNumber()).setValue(ticket);
		viewTicketAdapter.notifyItemChanged(i);
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	
	private ArrayList<String> getTicketNumbers ( ) {
		
		return new Gson().fromJson(user.getTicketNumbers(),
		                           new TypeToken<ArrayList<String>>() { }.getType());
	}
	
	
	private void getUser ( ) {
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot snapshot) {
				
				                if (snapshot.exists()) {
					                user = snapshot.getValue(UserModel.class);
					                setUpData();
				                }
			                }
			
			
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
			                }
		                });
	}
	
}