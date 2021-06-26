package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class RemoveTrainFragment extends Fragment {
	
	private final ArrayList<TrainModel> trains = new ArrayList<>();
	
	private RecyclerView recyclerView;
	
	private RemoveTrainAdapter removeTrainAdapter;
	
	private LinearLayout emptyLayout;
	
	private ProgressBar progressBar;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_remove_train, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Remove Train");
		
		recyclerView = view.findViewById(R.id.rv_remove_train);
		emptyLayout = view.findViewById(R.id.ll_empty_remove_train);
		progressBar = view.findViewById(R.id.pb_remove_train);
		progressBar.setVisibility(View.VISIBLE);
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot dataSnapshot) {
				
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
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
				                progressBar.setVisibility(View.INVISIBLE);
				                emptyLayout.setVisibility(View.VISIBLE);
			                }
		                });
	}
	
	
	private void setUpRecyclerView ( ) {
		
		removeTrainAdapter = new RemoveTrainAdapter(trains, this::removeTrain);
		
		recyclerView.addItemDecoration(
				new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setAdapter(removeTrainAdapter);
	}
	
	
	private void removeTrain (int position) {
		
		progressBar.setVisibility(View.VISIBLE);
		
		TrainModel train = trains.get(position);
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TRAINS)
		                .child(String.valueOf(train.getNumber())).removeValue()
		                .addOnCompleteListener(task -> {
			
			                progressBar.setVisibility(View.INVISIBLE);
			
			                if (task.isSuccessful()) {
				                trains.remove(position);
				                removeTrainAdapter.notifyItemRemoved(position);
				
				                Toast.makeText(requireContext(), "Train " + train
						                .getNumber() + " removed successfully", Toast.LENGTH_SHORT)
				                     .show();
				
			                } else {
				                if (task.getException() != null) {
					                Toast.makeText(requireContext(),
					                               task.getException().getMessage(),
					                               Toast.LENGTH_SHORT).show();
				                } else {
					                Toast.makeText(requireContext(), "Cannot Remove Train",
					                               Toast.LENGTH_SHORT).show();
				                }
			                }
		                });
	}
	
}