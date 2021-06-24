package com.hitk.railwayreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class BoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //Log.d(TAG, "onCreate: User already logged in");

            //findViewById(R.id.loginScreenProgressBar).setVisibility(View.VISIBLE);
            startActivity(new Intent(BoardingActivity.this, MainActivity.class));
            finishAffinity();
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }
}