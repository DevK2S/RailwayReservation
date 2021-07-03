package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.FirebaseDatabase;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.SeatType;
import com.hitk.railwayreservation.model.StationName;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class AddTrainFragment extends Fragment {
	
	private NavController navController;
	
	private TextInputLayout trainNumberTextInput, trainNameTextInput, sourceTextInput,
			departureDateTextInput, departureTimeTextInput, destinationTextInput,
			arrivalDateTextInput, arrivalTimeTextInput, numberOfSeatsTextInput, seatTypeTextInput,
			fareTextInput;
	
	private TextInputEditText trainNumberEditText, trainNameEditText, departureDateEditText,
			departureTimeEditText, arrivalDateEditText, arrivalTimeEditText, numberOfSeatsEditText
			, fareEditText;
	
	private MaterialAutoCompleteTextView seatTypeTextView, sourceTextView, destinationTextView;
	
	private ProgressBar progressBar;
	
	private MaterialDatePicker<Long> departureDatePicker, arrivalDatePicker;
	
	private Long departureDate, arrivalDate;
	
	private MaterialTimePicker departureTimePicker, arrivalTimePicker;
	
	private Integer departureTimeHour, departureTimeMinute, arrivalTimeHour, arrivalTimeMinute;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_add_train, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Add Train");
		
		navController = Navigation.findNavController(view);
		
		progressBar = view.findViewById(R.id.pb_add_train);
		
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
		sourceTextView = view.findViewById(R.id.mact_train_source);
		sourceTextView.setOnClickListener(v -> sourceTextInput.setError(null));
		
		StationName[] sourceStationNameValues = StationName.values();
		String[] sourceStationNames = new String[sourceStationNameValues.length];
		for (int i = 0; i < sourceStationNameValues.length; i++) {
			sourceStationNames[i] = sourceStationNameValues[i].name().replace("_", " ");
		}
		ArrayAdapter<String> sourceStationNameAdapter = new ArrayAdapter<>(requireContext(),
		                                                                   R.layout.item_dropdown,
		                                                                   sourceStationNames);
		
		sourceTextView.setAdapter(sourceStationNameAdapter);
		
		departureDateTextInput = view.findViewById(R.id.til_train_departure_date);
		departureDateEditText = view.findViewById(R.id.et_train_departure_date);
		departureDateEditText.setInputType(InputType.TYPE_NULL);
		departureDateEditText.setOnClickListener(v -> {
			
			departureDateTextInput.setError(null);
			showDepartureDatePicker();
		});
		
		departureTimeTextInput = view.findViewById(R.id.til_train_departure_time);
		departureTimeEditText = view.findViewById(R.id.et_train_departure_time);
		departureTimeEditText.setInputType(InputType.TYPE_NULL);
		departureTimeEditText.setOnClickListener(v -> {
			
			departureTimeTextInput.setError(null);
			showDepartureTimePicker();
		});
		
		destinationTextInput = view.findViewById(R.id.til_train_destination);
		destinationTextView = view.findViewById(R.id.mact_train_destination);
		destinationTextView.setOnClickListener(v -> destinationTextInput.setError(null));
		
		StationName[] destinationStationNameValues = StationName.values();
		String[] destinationStationNames = new String[destinationStationNameValues.length];
		for (int i = 0; i < destinationStationNameValues.length; i++) {
			destinationStationNames[i] = destinationStationNameValues[i].name().replace("_", " ");
		}
		ArrayAdapter<String> destinationStationNameAdapter = new ArrayAdapter<>(requireContext(),
		                                                                        R.layout.item_dropdown,
		                                                                        destinationStationNames);
		
		destinationTextView.setAdapter(destinationStationNameAdapter);
		
		arrivalDateTextInput = view.findViewById(R.id.til_train_arrival_date);
		arrivalDateEditText = view.findViewById(R.id.et_train_arrival_date);
		arrivalDateEditText.setInputType(InputType.TYPE_NULL);
		arrivalDateEditText.setOnClickListener(v -> {
			
			arrivalDateTextInput.setError(null);
			showArrivalDatePicker();
		});
		
		arrivalTimeTextInput = view.findViewById(R.id.til_train_arrival_time);
		arrivalTimeEditText = view.findViewById(R.id.et_train_arrival_time);
		arrivalTimeEditText.setInputType(InputType.TYPE_NULL);
		arrivalTimeEditText.setOnClickListener(v -> {
			
			arrivalDateTextInput.setError(null);
			arrivalTimeTextInput.setError(null);
			showArrivalTimePicker();
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
		seatTypeTextView.setOnClickListener(v -> seatTypeTextInput.setError(null));
		
		SeatType[] seatTypeValues = SeatType.values();
		String[] seatTypes = new String[seatTypeValues.length];
		for (int i = 0; i < seatTypeValues.length; i++) {
			seatTypes[i] = seatTypeValues[i].name();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown,
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
		
		Button addTrainButton = view.findViewById(R.id.btn_add_train);
		addTrainButton.setOnClickListener(v -> {
			
			progressBar.setVisibility(View.VISIBLE);
			if (validateFields()) {
				addTrain();
			} else {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	
	private void showDepartureDatePicker ( ) {
		
		CalendarConstraints.DateValidator validator = DateValidatorPointForward
				.from(MaterialDatePicker.todayInUtcMilliseconds());
		CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
				.setValidator(validator).build();
		
		departureDatePicker = MaterialDatePicker.Builder.datePicker()
		                                                .setTitleText("Select Departure Date")
		                                                .setCalendarConstraints(calendarConstraints)
		                                                .setSelection(MaterialDatePicker
				                                                              .todayInUtcMilliseconds())
		                                                .build();
		
		departureDatePicker.show(getParentFragmentManager(), departureDatePicker.toString());
		departureDatePicker.addOnPositiveButtonClickListener(selection -> {
			departureDate = selection;
			departureDateEditText.setText(departureDatePicker.getHeaderText());
		});
	}
	
	
	private void showArrivalDatePicker ( ) {
		
		CalendarConstraints.DateValidator validator = DateValidatorPointForward
				.from(MaterialDatePicker.todayInUtcMilliseconds());
		CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
				.setValidator(validator).build();
		
		arrivalDatePicker = MaterialDatePicker.Builder.datePicker()
		                                              .setTitleText("Select Arrival Date")
		                                              .setCalendarConstraints(calendarConstraints)
		                                              .setSelection(MaterialDatePicker
				                                                            .todayInUtcMilliseconds())
		                                              .build();
		
		arrivalDatePicker.show(getParentFragmentManager(), arrivalDatePicker.toString());
		arrivalDatePicker.addOnPositiveButtonClickListener(selection -> {
			arrivalDate = selection;
			arrivalDateEditText.setText(arrivalDatePicker.getHeaderText());
		});
	}
	
	
	private String getTime (int hour, int minute) {
		
		String hourText = (hour < 10) ? "0" + hour : String.valueOf(hour);
		String minuteText = (minute < 10) ? "0" + minute : String.valueOf(minute);
		return hourText + ":" + minuteText;
	}
	
	
	private void showDepartureTimePicker ( ) {
		
		int hour = departureTimeHour == null ? LocalDateTime.now().getHour() : departureTimeHour;
		int minute =
				departureTimeMinute == null ? LocalDateTime.now().getMinute() :
				departureTimeMinute;
		
		departureTimePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
		                                                      .setHour(hour).setMinute(minute)
		                                                      .setTitleText(
				                                                      "Select Departure " + "Time")
		                                                      .build();
		departureTimePicker.show(getParentFragmentManager(), departureTimePicker.toString());
		departureTimePicker.addOnPositiveButtonClickListener(v -> {
			departureTimeHour = departureTimePicker.getHour();
			departureTimeMinute = departureTimePicker.getMinute();
			departureTimeEditText.setText(getTime(departureTimeHour, departureTimeMinute));
		});
	}
	
	
	private void showArrivalTimePicker ( ) {
		
		int hour = arrivalTimeHour == null ? LocalDateTime.now().getHour() : arrivalTimeHour;
		int minute =
				arrivalTimeMinute == null ? LocalDateTime.now().getMinute() : arrivalTimeMinute;
		
		arrivalTimePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
		                                                    .setHour(hour).setMinute(minute)
		                                                    .setTitleText("Select Arrival Time")
		                                                    .build();
		arrivalTimePicker.show(getParentFragmentManager(), arrivalTimePicker.toString());
		arrivalTimePicker.addOnPositiveButtonClickListener(v -> {
			arrivalTimeHour = arrivalTimePicker.getHour();
			arrivalTimeMinute = arrivalTimePicker.getMinute();
			arrivalTimeEditText.setText(getTime(arrivalTimeHour, arrivalTimeMinute));
		});
	}
	
	
	private boolean validateFields ( ) {
		
		String trainNumber = trainNumberEditText.getText().toString();
		String trainNumberValidation = validateTrainNumber(trainNumber);
		
		String trainName = trainNameEditText.getText().toString();
		String trainNameValidation = validateTrainName(trainName);
		
		String source = sourceTextView.getText().toString();
		String sourceValidation = validateSource(source);
		
		String deptDate = departureDateEditText.getText().toString();
		String deptDateValidation = validateDepartureDate(deptDate);
		
		String deptTime = departureTimeEditText.getText().toString();
		String deptTimeValidation = validateDepartureTime(deptTime);
		
		String destination = destinationTextView.getText().toString();
		String destinationValidation = validateDestination(destination);
		
		String arrDate = arrivalDateEditText.getText().toString();
		String arrDateValidation = validateArrivalDate(arrDate);
		
		String arrTime = arrivalTimeEditText.getText().toString();
		String arrTimeValidation = validateArrivalTime(arrTime);
		
		String numberOfSeats = numberOfSeatsEditText.getText().toString();
		String numberOfSeatsValidation = validateNumberOfSeats(numberOfSeats);
		
		String seatType = seatTypeTextView.getText().toString();
		String seatTypeValidation = validateSeatType(seatType);
		
		String fare = fareEditText.getText().toString();
		String fareValidation = validateFare(fare);
		
		if (trainNumberValidation == null && trainNameValidation == null && sourceValidation == null && deptDateValidation == null && deptTimeValidation == null && destinationValidation == null && arrDateValidation == null && arrTimeValidation == null && numberOfSeatsValidation == null && seatTypeValidation == null && fareValidation == null) {
			return true;
		}
		
		if (trainNumberValidation != null) {
			trainNumberTextInput.setError(trainNumberValidation);
		}
		
		if (trainNameValidation != null) {
			trainNameTextInput.setError(trainNameValidation);
		}
		
		if (sourceValidation != null) {
			sourceTextInput.setError(sourceValidation);
		}
		
		if (deptDateValidation != null) {
			departureDateTextInput.setError(deptDateValidation);
		}
		
		if (deptTimeValidation != null) {
			departureTimeTextInput.setError(deptTimeValidation);
		}
		
		if (destinationValidation != null) {
			destinationTextInput.setError(destinationValidation);
		}
		
		if (arrDateValidation != null) {
			arrivalDateTextInput.setError(arrDateValidation);
		}
		
		if (arrTimeValidation != null) {
			arrivalTimeTextInput.setError(arrTimeValidation);
		}
		
		if (numberOfSeatsValidation != null) {
			numberOfSeatsTextInput.setError(numberOfSeatsValidation);
		}
		
		if (seatTypeValidation != null) {
			seatTypeTextInput.setError(seatTypeValidation);
		}
		
		if (fareValidation != null) {
			fareTextInput.setError(fareValidation);
		}
		
		return false;
	}
	
	
	private void addTrain ( ) {
		
		long trainNumber = Long.parseLong(trainNumberEditText.getText().toString());
		String trainName = trainNameEditText.getText().toString();
		String source = sourceTextView.getText().toString().replace(" ", "_");
		String deptTime = departureTimeEditText.getText().toString();
		String destination = destinationTextView.getText().toString().replace(" ", "_");
		String arrTime = arrivalTimeEditText.getText().toString();
		int numberOfSeats = Integer.parseInt(numberOfSeatsEditText.getText().toString());
		SeatType seatType = SeatType.valueOf(seatTypeTextView.getText().toString());
		double fare = Double.parseDouble(fareEditText.getText().toString());
		
		TrainModel train = new TrainModel(trainNumber, trainName, source, departureDate, deptTime,
		                                  destination, arrivalDate, arrTime, numberOfSeats,
		                                  numberOfSeats, seatType, fare);
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
		                .child(String.valueOf(trainNumber)).setValue(train)
		                .addOnCompleteListener(task -> {
			
			                progressBar.setVisibility(View.INVISIBLE);
			
			                if (task.isSuccessful()) {
				
				                Toast.makeText(requireContext(), "Train Added Successfully",
				                               Toast.LENGTH_SHORT).show();
				
				                navController.navigateUp();
				
			                } else {
				                if (task.getException() != null) {
					                Toast.makeText(requireContext(),
					                               task.getException().getMessage(),
					                               Toast.LENGTH_SHORT).show();
				                } else {
					                Toast.makeText(requireContext(), "Cannot add train",
					                               Toast.LENGTH_SHORT).show();
				                }
			                }
		                });
	}
	
	
	private String validateTrainNumber (String trainNumber) {
		if (trainNumber.trim().isEmpty()) {
			return "Train Number cannot be empty";
		} else if (trainNumber.trim().length() != 6) {
			return "Train Number should be 6 numbers";
		}
		return null;
	}
	
	
	private String validateTrainName (String trainName) {
		
		if (trainName.trim().isEmpty()) {
			return "Train Name cannot be empty";
		} else if (trainName.trim().length() < 10) {
			return "Train Name should be of at least 10 characters";
		} else if (trainName.trim().length() > 20) {
			return "Train Name should be of maximum 20 characters";
		}
		return null;
	}
	
	
	private String validateSource (String source) {
		
		if (source.trim().isEmpty()) {
			return "Source cannot be empty";
		}
		return null;
	}
	
	
	private String validateDepartureDate (String departureDate) {
		
		if (departureDate.trim().isEmpty()) {
			return "Departure Date cannot be empty";
		}
		return null;
	}
	
	
	private String validateDepartureTime (String departureTime) {
		
		if (departureTime.trim().isEmpty()) {
			return "Departure Time cannot be empty";
		}
		return null;
	}
	
	
	private String validateDestination (String destination) {
		
		if (destination.trim().isEmpty()) {
			return "Destination cannot be empty";
		}
		return null;
	}
	
	
	private String validateArrivalDate (String arrDate) {
		
		if (arrDate.trim().isEmpty()) {
			return "Arrival Date cannot be empty";
		} else if (arrivalDate < departureDate) {
			return "Arrival Date should be after Departure Date";
		} else if (arrivalDate.equals(departureDate) && arrivalTimeHour < departureTimeHour) {
			return "Arrival Date should be after Departure Date";
		} else if (arrivalDate.equals(departureDate) && arrivalTimeHour
				.equals(departureTimeHour) && arrivalTimeMinute <= departureTimeMinute) {
			return "Arrival Date should be after Departure Date";
		}
		return null;
	}
	
	
	private String validateArrivalTime (String arrTime) {
		
		if (arrTime.trim().isEmpty()) {
			return "Arrival Time cannot be empty";
		}
		return null;
	}
	
	
	private String validateNumberOfSeats (String numberOfSeats) {
		
		if (numberOfSeats.trim().isEmpty()) {
			return "Number of Seats cannot be empty";
		}
		return null;
	}
	
	
	private String validateSeatType (String seatType) {
		
		if (seatType.trim().isEmpty()) {
			return "Seat Type cannot be empty";
		}
		return null;
	}
	
	
	private String validateFare (String fare) {
		
		if (fare.trim().isEmpty()) {
			return "Fare cannot be empty";
		}
		return null;
	}
	
}