package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.SeatType;

import org.jetbrains.annotations.NotNull;

public class AddTrainFragment extends Fragment {
	
	TextInputLayout trainNumberTextInput, trainNameTextInput, sourceTextInput,
			departureTimeTextInput, destinationTextInput, arrivalTimeTextInput,
			numberOfSeatsTextInput, seatTypeTextInput, fareTextInput;
	
	TextInputEditText trainNumberEditText, trainNameEditText, sourceEditText,
			departureTimeEditText, destinationEditText, arrivalTimeEditText, numberOfSeatsEditText
			, fareEditText;
	
	MaterialAutoCompleteTextView seatTypeTextView;
	
	ProgressBar progressBar;
	
	Button addTrainButton;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_add_train, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		progressBar = view.findViewById(R.id.pb_add_train);
		addTrainButton = view.findViewById(R.id.btn_add_train);
		
		trainNumberTextInput = view.findViewById(R.id.til_train_number);
		trainNumberEditText = view.findViewById(R.id.et_train_number);
		trainNumberEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				trainNumberTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		trainNameTextInput = view.findViewById(R.id.til_train_name);
		trainNameEditText = view.findViewById(R.id.et_train_name);
		trainNameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				trainNameTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		sourceTextInput = view.findViewById(R.id.til_train_source);
		sourceEditText = view.findViewById(R.id.et_train_source);
		sourceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				sourceTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		departureTimeTextInput = view.findViewById(R.id.til_train_departure_time);
		departureTimeEditText = view.findViewById(R.id.et_train_departure_time);
		departureTimeEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				
				departureTimeTextInput.setError(null);
			}
		});
		
		destinationTextInput = view.findViewById(R.id.til_train_destination);
		destinationEditText = view.findViewById(R.id.et_train_destination);
		destinationEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				destinationTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		arrivalTimeTextInput = view.findViewById(R.id.til_train_arrival_time);
		arrivalTimeEditText = view.findViewById(R.id.et_train_arrival_time);
		arrivalTimeEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				
				arrivalTimeTextInput.setError(null);
				
			}
		});
		
		numberOfSeatsTextInput = view.findViewById(R.id.til_train_number_of_seats);
		numberOfSeatsEditText = view.findViewById(R.id.et_train_number_of_seats);
		numberOfSeatsEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				numberOfSeatsTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		seatTypeTextInput = view.findViewById(R.id.til_train_seat_type);
		seatTypeTextView = view.findViewById(R.id.mactv_train_seat_type);
		
		SeatType[] seatTypeValues = SeatType.values();
		String[] seatTypes = new String[seatTypeValues.length];
		for (int i = 0; i < seatTypeValues.length; i++) {
			seatTypes[i] = seatTypeValues[i].name();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
		                                                  R.layout.item_seat_type,
		                                                  seatTypes);
		seatTypeTextView.setAdapter(adapter);
		
		fareTextInput = view.findViewById(R.id.til_train_fare);
		fareEditText = view.findViewById(R.id.et_train_fare);
		fareEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				fareTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
	}
	
}