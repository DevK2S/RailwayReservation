package com.hitk.railwayreservation.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
	
	private static final String TAG = HomeFragment.class.getSimpleName();
	
	private NavController navController;
	
	private CardView search, book, viewTickets, viewBalance, addTrain, removeTrain, viewReport;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NotNull View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		requireActivity().setTitle("Railway Reservation");
		
		navController = Navigation.findNavController(view);
		
		LinearLayout normal1 = view.findViewById(R.id.ll_normal1);
		LinearLayout normal2 = view.findViewById(R.id.ll_normal2);
		
		LinearLayout admin1 = view.findViewById(R.id.ll_admin1);
		LinearLayout admin2 = view.findViewById(R.id.ll_admin2);
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot snapshot) {
								if (snapshot.exists() && snapshot.hasChildren()) {
									
//									LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//											ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.0f);
//									normal1.setLayoutParams(params);
//									normal2.setLayoutParams(params);
//
//									admin1.setVisibility(View.GONE);
//									admin2.setVisibility(View.GONE);
									
									Log.d(TAG, "onDataChange: " + snapshot);
								}
			                }
			
			
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
			                }
		                });
		
		search = view.findViewById(R.id.cd_search);
		book = view.findViewById(R.id.cd_book);
		
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
		book.setOnClickListener(
				v -> navController.navigate(R.id.action_homeFragment_to_bookFragment));
		
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