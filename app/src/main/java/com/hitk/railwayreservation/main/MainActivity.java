package com.hitk.railwayreservation.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.oboarding.OnBoardingActivity;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MaterialToolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setOnMenuItemClickListener(item -> {
			switch (item.getItemId()) {
				case R.id.profile:
					return true;
				
				case R.id.logout:
					FirebaseAuth.getInstance().signOut();
					startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
					finishAffinity();
					return true;
				
				default:
					return false;
			}
		});
	}
	
}