package com.hitk.railwayreservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "Main Activity";
	MaterialToolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);
		toolbar.setOnMenuItemClickListener(item -> {
			switch (item.getItemId()) {
				case R.id.profile:
					return true;

				case R.id.logout:
					Log.d(TAG, "onNavigationItemSelected: logging out");
					FirebaseAuth.getInstance().signOut();
					Navigation.findNavController(MainActivity.this, R.id.fragment_host).navigate(R.id.nav_logout);
					finishAffinity();
					return true;

				default:
			}
			return false;
		});


	}
	
	

	
}