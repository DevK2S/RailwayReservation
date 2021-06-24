package com.hitk.railwayreservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	public NavOptions.Builder leftToRightBuilder, rightToLeftBuilder;
	
	public NavController mNavController;
	
	private DrawerLayout mDrawerLayout;
	
	private NavigationView mNavigationView;
	
	private AppBarConfiguration mAppBarConfiguration;
	
	
	public void onBackPressed ( ) {
		
		if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	
	@Override
	public boolean onSupportNavigateUp ( ) {
		
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super
				.onSupportNavigateUp();
	}
	
	
	@Override
	public boolean onNavigationItemSelected (@NonNull MenuItem item) {
		
		int itemId = item.getItemId();
		
		if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		}
		
		switch (itemId) {
			
			case R.id.homeFragment:
				
				if (mNavController.getCurrentDestination().getId() != R.id.homeFragment) {
					mNavController.navigate(R.id.homeFragment, null, rightToLeftBuilder.build());
				}
				
				return true;
			
			case R.id.nav_logout:
				FirebaseAuth.getInstance().signOut();
				
				mNavController.navigate(R.id.nav_logout);
				
				finishAffinity();
				
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				
				return true;
			
			default:
				Toast.makeText(this, "This feature is not yet available", Toast.LENGTH_SHORT)
				     .show();
				
				return false;
		}
	}
	
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toolbar toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		
		mDrawerLayout = findViewById(R.id.drawer_layout);
		
		mNavigationView = findViewById(R.id.nav_view);
		
		mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
		
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.nav_logout)
				.setDrawerLayout(mDrawerLayout).build();
		
		NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
		
		NavigationUI.setupWithNavController(mNavigationView, mNavController);
		
		mNavigationView.setCheckedItem(R.id.homeFragment);
		
		mNavigationView.setNavigationItemSelectedListener(this);
		
		toolbar.setNavigationOnClickListener(view -> {
			if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
				mDrawerLayout.closeDrawer(GravityCompat.START);
			} else {
				mDrawerLayout.openDrawer(GravityCompat.START);
			}
		});
		
		leftToRightBuilder = new NavOptions.Builder();
		leftToRightBuilder.setEnterAnim(R.anim.slide_in_right);
		leftToRightBuilder.setExitAnim(R.anim.slide_out_left);
		leftToRightBuilder.setPopEnterAnim(R.anim.slide_in_left);
		leftToRightBuilder.setPopExitAnim(R.anim.slide_out_right);
		leftToRightBuilder.setLaunchSingleTop(true);
		
		rightToLeftBuilder = new NavOptions.Builder();
		rightToLeftBuilder.setEnterAnim(R.anim.slide_in_left);
		rightToLeftBuilder.setExitAnim(R.anim.slide_out_right);
		rightToLeftBuilder.setPopEnterAnim(R.anim.slide_in_right);
		rightToLeftBuilder.setPopExitAnim(R.anim.slide_out_left);
		rightToLeftBuilder.setLaunchSingleTop(true);
		
	}
	
	
	@Override
	protected void onResume ( ) {
		
		super.onResume();
		
		ImageView profileImage = mNavigationView.getHeaderView(0)
		                                        .findViewById(R.id.navigationDrawerProfileImage);
		TextView profileFullName = mNavigationView.getHeaderView(0)
		                                          .findViewById(R.id.navigationDrawerProfileName);
		TextView profileEmail = mNavigationView.getHeaderView(0)
		                                       .findViewById(R.id.navigationDrawerEmail);
		
		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			
			if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
				profileFullName
						.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
			}
			
			profileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
			
			profileEmail.setOnClickListener(view -> {
				if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
					FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
					            .addOnSuccessListener(new OnSuccessListener<Void>() {
						            @Override
						            public void onSuccess (Void unused) {
							            Toast.makeText(MainActivity.this,
							                           "Verification email sent successfully",
							                           Toast.LENGTH_SHORT)
							                 .show();
						            }
					            });
				}
			});
			
		} else {
			startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
			
			finishAffinity();
			
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
	}
	
}