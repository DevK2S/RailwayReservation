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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.TrainModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewReportFragment extends Fragment {

	private final ArrayList<TrainModel> trains = new ArrayList<>();

	private RecyclerView recyclerView;

	private ViewReportAdapter viewReportAdapter;

	private LinearLayout emptyLayout;

	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_view_report, container, false);
	}


	@Override
	public void onViewCreated(@NonNull @NotNull View view,
							  @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Train Report");
		recyclerView = view.findViewById(R.id.rv_report_train);
		emptyLayout = view.findViewById(R.id.ll_empty_report_train);
		progressBar = view.findViewById(R.id.pb_report_train);
		progressBar.setVisibility(View.VISIBLE);

		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

						progressBar.setVisibility(View.INVISIBLE);

						if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
							for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
								trains.add(snapshot.getValue(TrainModel.class));
							}
							setUpRecyclerView();
						} else {
							emptyLayout.setVisibility(View.VISIBLE);
						}
					}


					@Override
					public void onCancelled(@NonNull @NotNull DatabaseError error) {

						progressBar.setVisibility(View.INVISIBLE);
						emptyLayout.setVisibility(View.VISIBLE);
					}
				});
	}

	private void setUpRecyclerView() {
		viewReportAdapter = new ViewReportAdapter(trains);

		recyclerView.addItemDecoration(
				new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setAdapter(viewReportAdapter);

	}

}