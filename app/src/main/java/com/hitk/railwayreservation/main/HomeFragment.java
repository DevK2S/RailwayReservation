package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitk.railwayreservation.Constants;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.UserType;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

	private NavController navController;

	private CardView search, viewTickets, viewBalance, addTrain, removeTrain, viewReport;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NotNull View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Railway Reservation");
		
		navController = Navigation.findNavController(view);
		
		ProgressBar progressBar = view.findViewById(R.id.pb_home);
		
		LinearLayout normal1 = view.findViewById(R.id.ll_normal1);
		LinearLayout normal2 = view.findViewById(R.id.ll_normal2);
		
		LinearLayout admin1 = view.findViewById(R.id.ll_admin1);
		LinearLayout admin2 = view.findViewById(R.id.ll_admin2);
		
		normal1.setVisibility(View.GONE);
		normal2.setVisibility(View.GONE);
		
		admin1.setVisibility(View.GONE);
		admin2.setVisibility(View.GONE);
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot snapshot) {
				
				                progressBar.setVisibility(View.INVISIBLE);
				
				                normal1.setVisibility(View.VISIBLE);
				                normal2.setVisibility(View.VISIBLE);
				
				                if (snapshot.exists() && snapshot.hasChild("userType") && snapshot
						                .child("userType")
						                .getValue(UserType.class) != UserType.ADMIN) {
					
					                LinearLayout.LayoutParams params =
							                new LinearLayout.LayoutParams(
							                ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.0f);
					                
					                normal1.setLayoutParams(params);
					                normal2.setLayoutParams(params);
					
				                } else {
					
					                admin1.setVisibility(View.VISIBLE);
					                admin2.setVisibility(View.VISIBLE);
					
				                }
			                }
			
			
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
				                progressBar.setVisibility(View.INVISIBLE);
				
				                normal1.setVisibility(View.VISIBLE);
				                normal2.setVisibility(View.VISIBLE);
				
				                admin1.setVisibility(View.VISIBLE);
				                admin2.setVisibility(View.VISIBLE);
			                }
		                });
		
		search = view.findViewById(R.id.cd_search);
		
		viewTickets = view.findViewById(R.id.cd_view_tickets);
		viewBalance = view.findViewById(R.id.cd_view_balance);
		
		addTrain = view.findViewById(R.id.cd_add_train);
		removeTrain = view.findViewById(R.id.cd_remove_train);
		
		viewReport = view.findViewById(R.id.cd_view_report);
		
		setOnClickListeners();
	}
	
	
	private void setOnClickListeners ( ) {
		
		search.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_searchFragment));

		viewTickets.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_viewTicketsFragment));
		viewBalance.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_viewBalanceFragment));
		
		addTrain.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_addTrainFragment));
		removeTrain.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_removeTrainFragment));
		
		viewReport.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_viewReportFragment));
	}
	
}