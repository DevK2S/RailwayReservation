package com.hitk.railwayreservation.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.oboarding.OnBoardingActivity;

public class MainActivity extends AppCompatActivity {
	
	public MaterialToolbar toolbar;
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toolbar = findViewById(R.id.toolbar);
		toolbar.setOnMenuItemClickListener(item -> {
			if (item.getItemId() == R.id.logout) {
				FirebaseAuth.getInstance().signOut();
				startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
				finishAffinity();
				return true;
			}
			return false;
		});
	}
	
}