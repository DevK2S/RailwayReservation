package com.hitk.railwayreservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavController.OnDestinationChangedListener{
    private static final String TAG ="Main Activity" ;
    private Button logout;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private AppBarConfiguration mAppBarConfiguration;
    public NavOptions.Builder leftToRightBuilder, rightToLeftBuilder;
    private TextView navigationProfileName,navigationProfileEmail;
    public NavController mNavController;

    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: back pressed");
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            Log.d(TAG, "onBackPressed: closing drawer");
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        else {
            Log.d(TAG, "onBackPressed: navigating to home");
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView profileImage = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerProfileImage);
        TextView profileFullName = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerProfileName1);
        TextView profileEmail = mNavigationView.getHeaderView(0).findViewById(R.id.navigationDrawerEmail);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                profileFullName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }

            else {
               // profileImage.setImageResource(R.drawable.username);
            }

            profileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            profileEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Verification email sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        } else {
            startActivity(new Intent(MainActivity.this, BoardingActivity.class));
            finishAffinity();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mNavController.addOnDestinationChangedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.nav_logout)
                .setDrawerLayout(mDrawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        mNavigationView.setCheckedItem(R.id.homeFragment);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    Log.d(TAG, "onClick: closing drawer");
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    Log.d(TAG, "onClick: opening drawer");
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to right of current one");
        leftToRightBuilder = new NavOptions.Builder();
        leftToRightBuilder.setEnterAnim(R.anim.slide_in_right);
        leftToRightBuilder.setExitAnim(R.anim.slide_out_left);
        leftToRightBuilder.setPopEnterAnim(R.anim.slide_in_left);
        leftToRightBuilder.setPopExitAnim(R.anim.slide_out_right);
        leftToRightBuilder.setLaunchSingleTop(true);

        Log.d(TAG, "onNavigationItemSelected: animations for opening fragment to left of current one");
        rightToLeftBuilder = new NavOptions.Builder();
        rightToLeftBuilder.setEnterAnim(R.anim.slide_in_left);
        rightToLeftBuilder.setExitAnim(R.anim.slide_out_right);
        rightToLeftBuilder.setPopEnterAnim(R.anim.slide_in_right);
        rightToLeftBuilder.setPopExitAnim(R.anim.slide_out_left);
        rightToLeftBuilder.setLaunchSingleTop(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: starts");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

        Log.d(TAG, "onDestinationChanged: starts");
        switch (destination.getId()) {
            case R.id.homeFragment:
                break;

//            case R.id.nav_profile:

            default:
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onNavigationItemSelected: starts");
        int itemId = item.getItemId();

        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            Log.d(TAG, "onNavigationItemSelected: closing drawer");
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        switch (itemId) {
            case R.id.homeFragment:
                Log.d(TAG, "onNavigationItemSelected: home selected");
                if (mNavController.getCurrentDestination().getId() != R.id.homeFragment) {
                    Log.d(TAG, "onNavigationItemSelected: opening home fragment");
                    mNavController.navigate(R.id.homeFragment, null,rightToLeftBuilder.build());
                }
                return true;

          /*  case R.id.nav_profile:
                Log.d(TAG, "onNavigationItemSelected: profile selected");
                if (mNavController.getCurrentDestination().getId() != R.id.nav_profile) {
                    Log.d(TAG, "onNavigationItemSelected: opening profile fragment");
                    mNavController.navigate(R.id.nav_profile, null,leftToRightBuilder.build());
                }
                return true;
            */
            case R.id.nav_logout:
                Log.d(TAG, "onNavigationItemSelected: logging out");
                FirebaseAuth.getInstance().signOut();
                mNavController.navigate(R.id.nav_logout);
                finishAffinity();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            default:
                Toast.makeText(this, "This feature is not yet available", Toast.LENGTH_SHORT).show();
                return false;
        }
    }


}