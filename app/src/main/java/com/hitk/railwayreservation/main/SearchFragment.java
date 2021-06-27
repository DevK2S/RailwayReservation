package com.hitk.railwayreservation.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.StationName;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchFragment extends Fragment {

    private static final String TAG = "Search Fragment";
    private TextInputLayout sourceTextInput, destinationTextInput, dateTextInput;
    private AutoCompleteTextView sourceText, destinationText, dateText;
    private ArrayList<StationName> stations;
    private ArrayAdapter searchAdapter;
    private Button searchTrainButton;
    private MaterialDatePicker<Long> datePicker;
    private DatabaseReference databaseReference;
    private long searchDate;
    private ArrayList<TrainModel> trainList;
    private SearchTrainAdapter searchTrainAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.toolbar.setTitle("Search Trains");

        recyclerView = view.findViewById(R.id.rv_search_train);
        progressBar = view.findViewById(R.id.pb_search);

        sourceTextInput = view.findViewById(R.id.til_search_source);
        sourceText = view.findViewById(R.id.act_source);
        sourceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sourceTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destinationTextInput = view.findViewById(R.id.til_search_destination);
        destinationText = view.findViewById(R.id.act_destination);
        destinationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                destinationTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateTextInput = view.findViewById(R.id.til_search_date);
        dateText = view.findViewById(R.id.act_date);

        dateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setUpFields();
        dateText.setOnClickListener(v -> {
            datePicker.show(getParentFragmentManager(), datePicker.toString());
        });
        datePicker.addOnPositiveButtonClickListener(selection -> {
            searchDate = selection;
            dateText.setText(datePicker.getHeaderText());
        });

        searchTrainButton = view.findViewById(R.id.btn_search_train);
        searchTrainButton.setOnClickListener(v -> {
            if (validateFields()) {

                Log.i(TAG, "Searching Trains");
                searchTrain(sourceText.getText().toString(), destinationText.getText().toString());
            }
        });


    }

    private void setUpFields() {

        stations = new ArrayList<>(Arrays.asList(StationName.class.getEnumConstants()));
        searchAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, stations);

        sourceText.setAdapter(searchAdapter);
        destinationText.setAdapter(searchAdapter);
        sourceText.setThreshold(1);
        destinationText.setThreshold(1);

        CalendarConstraints.DateValidator validator = DateValidatorPointForward
                .from(MaterialDatePicker.todayInUtcMilliseconds());
        CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
                .setValidator(validator).build();

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setCalendarConstraints(calendarConstraints)
                .setSelection(MaterialDatePicker
                        .todayInUtcMilliseconds())
                .build();
    }

    private boolean validateFields() {

        String source = sourceText.getText().toString();
        String sourceValidation = validateSource(source);

        String destination = destinationText.getText().toString();
        String destinationValidation = validateDestination(destination);

        String date = dateText.getText().toString();
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

    private String validateSource(String source) {

        if (source.trim().isEmpty()) {
            return "Source cannot be empty";
        }
        return null;
    }

    private String validateDestination(String destination) {

        if (destination.trim().isEmpty()) {
            return "Destination cannot be empty";
        }
        return null;
    }

    private String validateDate(String date) {

        if (date.trim().isEmpty()) {
            return "Date cannot be empty";
        }
        return null;
    }

    private void searchTrain(String source, String destination) {
        trainList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                        progressBar.setVisibility(View.INVISIBLE);

                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TrainModel train = snapshot.getValue(TrainModel.class);
                                if (train.getSource().equals(source) && train.getDestination().equals(destination) && train.getDepartureDate() == searchDate)
                                    trainList.add(train);
                            }
                            setUpRecyclerView();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        Log.i(TAG, "List " + trainList.toString());
    }

    private void setUpRecyclerView() {

        searchTrainAdapter = new SearchTrainAdapter(trainList, new SearchTrainAdapter.SearchTrainOnClickListener() {
        });

        recyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(searchTrainAdapter);
    }


}