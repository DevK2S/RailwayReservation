package com.hitk.railwayreservation.oboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.main.MainActivity;

public class OnBoardingActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boarding);
		
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		
		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
			finishAffinity();
		}
	}
	
}