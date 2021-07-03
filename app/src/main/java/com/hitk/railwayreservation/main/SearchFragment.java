package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.StationName;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private NavController navController;

    private TextInputLayout sourceTextInput, destinationTextInput, dateTextInput;

    private AutoCompleteTextView sourceTextView, destinationTextView, dateTextView;

	private MaterialDatePicker<Long> datePicker;

	private long searchDate;

	private ArrayList<TrainModel> trainList;

	private RecyclerView recyclerView;

	private ProgressBar progressBar;

	private LinearLayout emptyLayout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_search, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.toolbar.setTitle("Search Trains");

        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.rv_search_train);
		progressBar = view.findViewById(R.id.pb_search);
		emptyLayout = view.findViewById(R.id.ll_empty_search_train);

        sourceTextInput = view.findViewById(R.id.til_search_source);
        sourceTextView = view.findViewById(R.id.act_source);
        sourceTextView.setOnClickListener(v -> sourceTextInput.setError(null));

        destinationTextInput = view.findViewById(R.id.til_search_destination);
        destinationTextView = view.findViewById(R.id.act_destination);
        destinationTextView.setOnClickListener(v -> destinationTextInput.setError(null));
		
		dateTextInput = view.findViewById(R.id.til_search_date);
		dateTextView = view.findViewById(R.id.act_date);
		dateTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				dateTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		setUpFields();
		
		dateTextView.setOnClickListener(v -> {
			datePicker.show(getParentFragmentManager(), datePicker.toString());
		});
		
		datePicker.addOnPositiveButtonClickListener(selection -> {
			searchDate = selection;
			dateTextView.setText(datePicker.getHeaderText());
		});
		
		Button searchTrainButton = view.findViewById(R.id.btn_search_train);
		searchTrainButton.setOnClickListener(v -> {
			if (validateFields()) {
				progressBar.setVisibility(View.VISIBLE);
				searchTrain(sourceTextView.getText().toString().replace(" ", "_"),
				            destinationTextView.getText().toString().replace(" ", "_"));
			}
		});
		
		
	}
	
	
	private void setUpFields ( ) {
		
		StationName[] stationNameValues = StationName.values();
		String[] stations = new String[stationNameValues.length];
		for (int i = 0; i < stationNameValues.length; i++) {
			stations[i] = stationNameValues[i].name().replace("_", " ");
		}
		ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(requireContext(),
		                                                         R.layout.item_dropdown, stations);
		
		sourceTextView.setAdapter(stationAdapter);
		destinationTextView.setAdapter(stationAdapter);
		
		CalendarConstraints.DateValidator validator = DateValidatorPointForward
				.from(MaterialDatePicker.todayInUtcMilliseconds());
		CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
				.setValidator(validator).build();
		
		datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
		                                       .setCalendarConstraints(calendarConstraints)
		                                       .setSelection(
				                                       MaterialDatePicker.todayInUtcMilliseconds())
		                                       .build();
	}
	
	
	private boolean validateFields ( ) {
		
		String source = sourceTextView.getText().toString();
		String sourceValidation = validateSource(source);
		
		String destination = destinationTextView.getText().toString();
		String destinationValidation = validateDestination(destination);
		
		String date = dateTextView.getText().toString();
		String dateValidation = validateDate(date);
		
		if (sourceValidation == null && destinationValidation == null && dateValidation == null) {
			return true;
		}
		
		if (sourceValidation != null) {
			sourceTextInput.setError(sourceValidation);
		}
		
		if (destinationValidation != null) {
			destinationTextInput.setError(destinationValidation);
		}
		
		if (dateValidation != null) {
			dateTextInput.setError(dateValidation);
		}
		
		return false;
	}
	
	
	private String validateSource (String source) {
		
		if (source.trim().isEmpty()) {
			return "Source cannot be empty";
		}
		return null;
	}
	
	
	private String validateDestination (String destination) {
		
		if (destination.trim().isEmpty()) {
			return "Destination cannot be empty";
		}
		return null;
	}
	
	
	private String validateDate (String date) {
		
		if (date.trim().isEmpty()) {
			return "Date cannot be empty";
		}
		return null;
	}
	
	
	private void searchTrain (String source, String destination) {
		
		trainList = new ArrayList<>();
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot dataSnapshot) {
				                emptyLayout.setVisibility(View.INVISIBLE);
				
				                progressBar.setVisibility(View.INVISIBLE);
				
				                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
					                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						                TrainModel train = snapshot.getValue(TrainModel.class);

										if (train.getSource().equals(source) && train
												.getDestination().equals(destination) && train
												.getDepartureDate() == searchDate) {
											trainList.add(train);
										}
									}
					                
					                if (trainList.isEmpty()) {
						                emptyLayout.setVisibility(View.VISIBLE);
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


    private void setUpRecyclerView() {

        SearchTrainAdapter searchTrainAdapter = new SearchTrainAdapter(trainList, this::bookTrain);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(searchTrainAdapter);
    }

    private void bookTrain(int i) {
        TrainModel train = trainList.get(i);
        navController.navigate(SearchFragmentDirections.actionBook(train));
    }


}