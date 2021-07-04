package com.hitk.railwayreservation.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hitk.railwayreservation.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class ViewBalanceFragment extends Fragment {
	
	private final ArrayList<TicketModel> tickets = new ArrayList<>();
	
	private LinearLayout emptyLayout;
	
	private RecyclerView recyclerView;
	
	private ViewBalanceAdapter viewBalanceAdapter;
	
	private TextView outstandingBalance;
	
	private ProgressBar progressBar;
	
	private Button makePayment;
	
	private UserModel user;
	
	private ArrayList<String> listOfPatterns;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_view_balance, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull @NotNull View view,
	                           @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		MainActivity mainActivity = (MainActivity) requireActivity();
		mainActivity.toolbar.setTitle("Balance Report");
		
		emptyLayout = view.findViewById(R.id.ll_empty_view_balance);
		recyclerView = view.findViewById(R.id.rv_view_balance);
		
		progressBar = view.findViewById(R.id.pb_view_balance);
		progressBar.setVisibility(View.VISIBLE);
		
		outstandingBalance = view.findViewById(R.id.tv_outstanding_balance);
		makePayment = view.findViewById(R.id.btn_make_payment);
		
		makePayment.setOnClickListener(v -> showPaymentDialog());
		
		getUser();
		
		listOfPatterns = new ArrayList<>();
		String ptVisa = "^4[0-9]{6,}$";
		listOfPatterns.add(ptVisa);
		String ptMasterCard = "^5[1-5][0-9]{5,}$";
		listOfPatterns.add(ptMasterCard);
		String ptAmeExp = "^3[47][0-9]{5,}$";
		listOfPatterns.add(ptAmeExp);
		String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
		listOfPatterns.add(ptDinClb);
		String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
		listOfPatterns.add(ptDiscover);
		String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
		listOfPatterns.add(ptJcb);
	}
	
	
	private void showPaymentDialog ( ) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_payment, null);
		builder.setView(dialogView);
		
		AlertDialog alertDialog = builder.create();
		
		Button payInPersonButton = dialogView.findViewById(R.id.btn_pay_in_person);
		payInPersonButton.setOnClickListener(v -> {
			alertDialog.dismiss();
			
			Toast.makeText(requireContext(), "You have 48 hours to make the payment",
			               Toast.LENGTH_SHORT).show();
		});
		
		TextInputLayout creditCardTextInput = dialogView.findViewById(R.id.til_credit_card);
		TextInputEditText creditCardEditText = dialogView.findViewById(R.id.et_credit_card);
		
		creditCardEditText.addTextChangedListener(new TextWatcher() {
			private static final char space = '-';
			
			
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				creditCardTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
				
				if (s.length() > 0 && (s.length() % 5) == 0) {
					final char c = s.charAt(s.length() - 1);
					if (space == c) {
						s.delete(s.length() - 1, s.length());
					}
				}
				if (s.length() > 0 && (s.length() % 5) == 0) {
					char c = s.charAt(s.length() - 1);
					if (Character.isDigit(c) && TextUtils
							.split(s.toString(), String.valueOf(space)).length <= 3) {
						s.insert(s.length() - 1, String.valueOf(space));
					}
				}
			}
		});
		
		
		Button payNowButton = dialogView.findViewById(R.id.btn_pay_now);
		payNowButton.setOnClickListener(v -> {
			progressBar.setVisibility(View.VISIBLE);
			
			String creditCardNumber = creditCardEditText.getText().toString().replace("-", "");
			
			boolean isValid = false;
			for (String pattern : listOfPatterns) {
				if (creditCardNumber.matches(pattern)) {
					isValid = true;
					break;
				}
			}
			
			if (isValid) {
				user.setOutstandingBalance(0);
				
				FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
				                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
				                .setValue(user);
				
				for (TicketModel ticket : tickets) {
					if (!ticket.getPaymentMade()) {
						ticket.setPaymentMade(true);
						ticket.setPaymentDate(new Date().getTime());
						
						FirebaseDatabase.getInstance().getReference()
						                .child(Constants.FIREBASE_TICKETS)
						                .child(ticket.getPnrNumber()).setValue(ticket);
					}
				}
				
				viewBalanceAdapter.notifyDataSetChanged();
				
				progressBar.setVisibility(View.INVISIBLE);
				
				setOutstandingBalanceText();
				
				alertDialog.dismiss();
				
				Toast.makeText(requireContext(), "Payment made successfully", Toast.LENGTH_SHORT)
				     .show();
			} else {
				progressBar.setVisibility(View.INVISIBLE);
				creditCardTextInput.setError("Invalid Credit Card");
			}
		});
		
		alertDialog.show();
		
	}
	
	
	private void setUpRecyclerView ( ) {
		
		viewBalanceAdapter = new ViewBalanceAdapter(requireContext(), tickets);
		recyclerView.addItemDecoration(
				new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setAdapter(viewBalanceAdapter);
		
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	
	private ArrayList<String> getTicketNumbers ( ) {
		
		return new Gson().fromJson(user.getTicketNumbers(),
		                           new TypeToken<ArrayList<String>>() { }.getType());
	}
	
	
	private void setUpData ( ) {
		
		ArrayList<String> ticketNumberList = getTicketNumbers();
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_TICKETS)
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot dataSnapshot) {
				
				                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
					                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
						                String ticketId = snapshot.getKey();
						                if (ticketNumberList.contains(ticketId)) {
							                tickets.add(snapshot.getValue(TicketModel.class));
						                }
					                }
					                tickets.sort((o1, o2) -> {
					                	if (o1.getPaymentMade() == o2.getPaymentMade()) {
							                if (o1.getTicketState() == TicketState.BOOKED && o2.getTicketState() == TicketState.BOOKED) {
								                return (int) (o2.getTotalAmount() - o1.getTotalAmount());
							                } else if (o1.getTicketState() == TicketState.BOOKED) {
								                return -1;
							                } else {
								                return 1;
							                }
						                } else {
							                return Boolean.compare(o1.getPaymentMade(), o2.getPaymentMade());
						                }
					                });
					
					                setUpRecyclerView();
				                } else {
					                progressBar.setVisibility(View.INVISIBLE);
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
	
	
	private void getUser ( ) {
		
		FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_USERS)
		                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
		                .addListenerForSingleValueEvent(new ValueEventListener() {
			                @Override
			                public void onDataChange (@NonNull @NotNull DataSnapshot snapshot) {
				
				                if (snapshot.exists()) {
					                user = snapshot.getValue(UserModel.class);
					
					                setOutstandingBalanceText();
					
					                setUpData();
					
					                progressBar.setVisibility(View.GONE);
				                }
			                }
			
			
			                @Override
			                public void onCancelled (@NonNull @NotNull DatabaseError error) {
				
			                }
		                });
	}
	
	
	private void setOutstandingBalanceText ( ) {
		
		if (user.getOutstandingBalance() > 0) {
			outstandingBalance.setText("-" + user.getOutstandingBalance());
			outstandingBalance.setTextColor(
					getResources().getColor(R.color.errorTint, requireActivity().getTheme()));
		} else {
			if (user.getOutstandingBalance() < 0) {
				outstandingBalance
						.setText(String.valueOf(user.getOutstandingBalance()).substring(1));
			} else {
				outstandingBalance.setText(String.valueOf(user.getOutstandingBalance()));
			}
			outstandingBalance.setTextColor(
					getResources().getColor(R.color.green, requireActivity().getTheme()));
			makePayment.setVisibility(View.GONE);
		}
	}
	
}