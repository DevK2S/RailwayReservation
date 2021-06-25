package com.hitk.railwayreservation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.MainActivity;
import com.hitk.railwayreservation.R;


public class LoginFragment extends Fragment {
 
	private TextInputLayout emailTextInput, passwordTextInput;
	
	private TextInputEditText emailEditText, passwordEditText;
	
	private ProgressBar progressBar;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		progressBar = view.findViewById(R.id.pb_login);
		
		emailTextInput = view.findViewById(R.id.til_email_login);
		emailEditText = view.findViewById(R.id.et_email_login);
        
        passwordTextInput = view.findViewById(R.id.til_password_login);
        passwordEditText = view.findViewById(R.id.et_password_login);
		
		emailEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				emailTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				passwordTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		view.findViewById(R.id.btn_create_account_login).setOnClickListener(
				v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment));

		
		view.findViewById(R.id.btn_login).setOnClickListener(v -> {
			progressBar.setVisibility(View.VISIBLE);
			
			if (validateFields()) {
                login();
            } else {
				progressBar.setVisibility(View.INVISIBLE);
			}
        });
	}
	
	
	public void login ( ) {
		
		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		
		FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
		            .addOnCompleteListener(task -> {
		            	
			            progressBar.setVisibility(View.INVISIBLE);
			
			            if (task.isSuccessful()) {
                        	
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                            requireActivity().finishAffinity();
                            
                        } else {
			            	
                            Toast.makeText(requireContext(), task.getException().getMessage(),
                                           Toast.LENGTH_SHORT).show();
				
				            task.getException().printStackTrace();
			            }
                    });
		
	}
	
	
	private boolean validateFields ( ) {
	    
	    String emailValidation = validateEmail(emailEditText.getText().toString());
		String passwordValidation = validatePassword(passwordEditText.getText().toString());
		
		if (emailValidation == null && passwordValidation == null) {
			return true;
		}
		
		if (emailValidation != null) {
			emailTextInput.setError(emailValidation);
		}
		
		if (passwordValidation != null) {
			passwordTextInput.setError(passwordValidation);
		}
		
		return false;
	}
	
	
	private String validateEmail (String email) {
		
		if (email.trim().length() == 0) {
			return "Email cannot be empty";
		} else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
			return "Invalid Email";
		}
		return null;
	}
	
	
	private String validatePassword (String password) {
		
		if (password.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (password.trim().length() < 8) {
			return "Password too small. Minimum length is 8";
		} else if (password.trim().length() > 15) {
			return "Password too long. Maximum length is 15";
		} else if (password.trim().contains(" ")) {
			return "Password cannot contain spaces";
		}
		return null;
	}
	
}