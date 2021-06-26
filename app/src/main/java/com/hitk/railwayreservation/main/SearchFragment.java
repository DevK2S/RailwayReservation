package com.hitk.railwayreservation.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.hitk.railwayreservation.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchFragment extends Fragment {

    AutoCompleteTextView act_source;
    AutoCompleteTextView act_destination;
    AutoCompleteTextView act_date;
    ArrayList<String> stations;
    ArrayAdapter searchAdapter;
    Button btn_search_train;
    MaterialDatePicker<Long> datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search");
        act_source = view.findViewById(R.id.act_source);
        act_destination = view.findViewById(R.id.act_destination);
        btn_search_train = view.findViewById(R.id.btn_search_train);
        act_date = view.findViewById(R.id.act_date);

        setUpFields();
        act_date.setOnClickListener(v -> {
            datePicker.show(getParentFragmentManager(), datePicker.toString());
        });
        //datePicker.addOnPositiveButtonClickListener();

    }

    private void setUpFields() {
        stations = new ArrayList<>(Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4"));
        searchAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, stations);
        act_source.setAdapter(searchAdapter);
        act_destination.setAdapter(searchAdapter);
        act_source.setThreshold(1);
        act_destination.setThreshold(1);
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
    }
}