package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.hitk.railwayreservation.model.TrainModel;
import com.hitk.railwayreservation.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BookFragment extends Fragment {
	
	private static final String TAG = "BookFragment";
	
	private NavController navController;
	
	private TextView trainNumberTextView, trainNameTextView, trainSourceTextView,
			trainDestinationTextView, trainDeptDateTextView, trainDeptTimeTextView,
			trainArrDateTextView, trainArrTimeTextView, trainSeatTypeTextView, trainFareTextVew,
			trainSeatAvailableTextView, totalCostTextView;
	
	private LinearLayout trainDeptLayout, trainArrLayout;
	
	private TextInputLayout numberOfPassengerInputLayout;
	
	private TextInputEditText numberOfPassengerEditText;
	
	private ProgressBar progressBar;
	
	private Button bookTicketButton;
	
	private UserModel user;
	
	private TrainModel train;
	
	private double totalCost;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_book, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Book Train");
		
		train = BookFragmentArgs.fromBundle(getArguments()).getTrain();
		navController = Navigation.findNavController(view);
		
		getUser();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
		
		progressBar = view.findViewById(R.id.pb_book_train);
		
		trainNumberTextView = view.findViewById(R.id.tv_train_number);
		trainNumberTextView.setText(String.valueOf(train.getNumber()));
		
		trainNameTextView = view.findViewById(R.id.tv_train_name);
		trainNameTextView.setText(train.getName());
		
		trainSourceTextView = view.findViewById(R.id.tv_source_station_name);
		trainSourceTextView.setText("Source : " + train.getSource().replace("_", " "));
		
		trainDeptLayout = view.findViewById(R.id.ll_departure_date_time);
		trainDeptDateTextView = view.findViewById(R.id.tv_train_departure_date);
		trainDeptDateTextView.setText("Dept. Date : " + sdf.format(train.getDepartureDate()));
		
		trainDeptTimeTextView = view.findViewById(R.id.tv_train_departure_time);
		trainDeptTimeTextView.setText("Dept. Time : " + train.getDepartureTime());
		
		trainDestinationTextView = view.findViewById(R.id.tv_destination_station_name);
		trainDestinationTextView.setText("Destination : " + train.getDestination().replace("_", " "));
		
		trainArrLayout = view.findViewById(R.id.ll_arrival_date_time);
		trainArrDateTextView = view.findViewById(R.id.tv_train_arrival_date);
		trainArrDateTextView.setText("Arrival Date :  " + sdf.format(train.getDepartureDate()));
		
		trainArrTimeTextView = view.findViewById(R.id.tv_train_arrival_time);
		trainArrTimeTextView.setText("Arrival Time : " + train.getArrivalTime());
		
		trainSeatTypeTextView = view.findViewById(R.id.tv_seat_type);
		trainSeatTypeTextView.setText("Seat Type : " + train.getSeatType());
		
		trainFareTextVew = view.findViewById(R.id.tv_fare);
		trainFareTextVew.setText("Fare : " + train.getFare());
		
		trainSeatAvailableTextView = view.findViewById(R.id.tv_number_of_seat);
		trainSeatAvailableTextView.setText("Available Seats : " + train.getSeatsAvailable());
		
		totalCostTextView = view.findViewById(R.id.tv_total_cost);
		
		numberOfPassengerInputLayout = view.findViewById(R.id.til_passenger_number);
		numberOfPassengerEditText = view.findViewById(R.id.et_passenger_number);
		numberOfPassengerEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				numberOfPassengerInputLayout.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
				if (validateFields()) {
					totalCost = train.getFare() * Double.parseDouble(s.toString().trim());
					totalCostTextView.setVisibility(View.VISIBLE);
					totalCostTextView.setText("Total Cost : Rs." + totalCost);
				} else {
					totalCostTextView.setVisibility(View.GONE);
				}
			}
		});
		
		
		bookTicketButton = view.findViewById(R.id.btn_bookTicket);
		bookTicketButton.setOnClickListener(v -> {
			progressBar.setVisibility(View.VISIBLE);
			if (validateFields()) {
				String pnrNumber = createPnrNumber();
				updateUserDetails(pnrNumber);
				Toast.makeText(requireContext(), "Tickets Booked Successfully", Toast.LENGTH_SHORT).show();
				navController.navigateUp();
			} else {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	
	private void updateTrainDetails ( ) {
		
		train.setSeatsAvailable(train.getSeatsAvailable() - Integer
				.parseInt(numberOfPassengerEditText.getText().toString().trim()));
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
		                .child(String.valueOf(train.getNumber())).setValue(train);
	}
	
	
	private boolean validateFields ( ) {
		
		String numberOfPassengers = numberOfPassengerEditText.getText().toString();
		String numberOfPassengersValidation = validateNumberOfPassengers(numberOfPassengers);
		if (numberOfPassengersValidation == null) {
			return true;
		}
		numberOfPassengerInputLayout.setError(numberOfPassengersValidation);
		return false;
	}
	
	
	private String validateNumberOfPassengers (String numberOfPassengers) {
		
		if (numberOfPassengers.trim().isEmpty()) {
			return "Number of passengers cannot be empty";
		} else if (Integer.parseInt(numberOfPassengers.trim()) > train.getSeatsAvailable()) {
			return "Number of passengers cannot be more than seats available";
		}
		return null;
	}
	
	
	private void updateUserDetails (String pnrNumber) {
		
		Gson gson = new Gson();
		ArrayList<String> ticketNumberList = gson.fromJson(user.getTicketNumbers(),
		                                                   new TypeToken<ArrayList<String>>() { }
				                                                   .getType());
		
		ticketNumberList.add(pnrNumber);
		
		String ticketNo = gson.toJson(ticketNumberList);
		user.setTicketNumbers(ticketNo);
		user.setOutstandingBalance(user.getOutstandingBalance() + totalCost);
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
		
		storeTicket(pnrNumber);
	}
	
	
	private String createPnrNumber ( ) {
		
		UUID uuid = UUID.randomUUID();
		String pnrNumber = uuid.toString().replace("-", "");
		pnrNumber = pnrNumber.substring(0, 11);
		return pnrNumber;
	}
	
	
	private void storeTicket (String pnrNumber) {
		
		boolean paymentMade = false;
		long paymentDate = 0;
		
		if (user.getOutstandingBalance() <= 0) {
			paymentMade = true;
			paymentDate = new Date().getTime();
		}
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TICKETS)
		                .child(pnrNumber).setValue(
				new TicketModel(pnrNumber, TicketState.BOOKED, train.getNumber(), train.getName(),
				                train.getSource(), train.getDestination(),
				                train.getDepartureDate(),
				                train.getDepartureTime(), train.getArrivalDate(),
				                train.getArrivalTime(), Integer.parseInt(
						numberOfPassengerEditText.getText().toString().trim()), totalCost, paymentMade, paymentDate));
		
		updateTrainDetails();
	}
	
	
	private void getUser ( ) {
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot snapshot) {
				
				                if (snapshot.exists()) {
					                user = snapshot.getValue(UserModel.class);
				                }
			                }
			                
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
			                }
		                });
	}
	
}