package com.hitk.railwayreservation.oboarding;

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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hitk.railwayreservation.main.MainActivity;
import com.hitk.railwayreservation.R;
import com.hitk.railwayreservation.model.UserModel;

import java.util.ArrayList;

public class SignUpFragment extends Fragment {

    private TextInputLayout nameTextInput, emailTextInput, passwordTextInput,
            confirmPasswordTextInput, phoneNumberTextInput, addressTextInput;

    private TextInputEditText nameEditText, emailEditText, passwordEditText,
            confirmPasswordEditText, phoneNumberEditText, addressEditText;

    private ProgressBar progressBar;
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_sign_up, container, false);
	}
	
	
	@Override
	public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		
		progressBar = view.findViewById(R.id.pb_signup);
		
		nameTextInput = view.findViewById(R.id.til_name_signup);
		nameEditText = view.findViewById(R.id.et_name_signup);
		
		emailTextInput = view.findViewById(R.id.til_email_signup);
		emailEditText = view.findViewById(R.id.et_email_signup);
		
		passwordTextInput = view.findViewById(R.id.til_password_signup);
		passwordEditText = view.findViewById(R.id.et_password_signup);
		
		confirmPasswordTextInput = view.findViewById(R.id.til_confirm_password_signup);
		confirmPasswordEditText = view.findViewById(R.id.et_confirm_password_signup);
		
		phoneNumberTextInput = view.findViewById(R.id.til_phone_signup);
		phoneNumberEditText = view.findViewById(R.id.et_phone_signup);
		
		addressTextInput = view.findViewById(R.id.til_address_signup);
		addressEditText = view.findViewById(R.id.et_address_signup);
		
		nameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				nameTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
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
		
		confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				confirmPasswordTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		phoneNumberEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				phoneNumberTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		addressEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
			
			}
			
			
			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				
				addressTextInput.setError(null);
			}
			
			
			@Override
			public void afterTextChanged (Editable s) {
			
			}
		});
		
		view.findViewById(R.id.btn_create_account).setOnClickListener(v -> {
			progressBar.setVisibility(View.VISIBLE);
			
			if (validateFields()) {
				signUp();
			} else {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
		
		view.findViewById(R.id.btn_login_signup)
		    .setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
		
	}
	
	
	private void signUp ( ) {
		
		String email = emailEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		
		FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
		            .addOnCompleteListener(task -> {
			
			            if (task.isSuccessful()) {
				            storeUserDetails();
				
			            } else {
				            progressBar.setVisibility(View.INVISIBLE);
				
				            Toast.makeText(requireContext(), task.getException().getMessage(),
				                           Toast.LENGTH_SHORT).show();
				
				            task.getException().printStackTrace();
			            }
		            });
		
	}
	
	
	private void storeUserDetails ( ) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String address = addressEditText.getText().toString();


        UserModel passenger = new UserModel(name, email, phoneNumber, address, new Gson().toJson(new ArrayList<String>()));

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(passenger)
                .addOnCompleteListener(setValueTask -> {

                    if (setValueTask.isSuccessful()) {

                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                        UserProfileChangeRequest.Builder profileUpdates =
                                new UserProfileChangeRequest.Builder();
				                profileUpdates.setDisplayName(name);
				
				                FirebaseAuth.getInstance().getCurrentUser()
				                            .updateProfile(profileUpdates.build())
				                            .addOnCompleteListener(profileUpdateTask -> {
					
					                            progressBar.setVisibility(View.INVISIBLE);
					
					                            startActivity(new Intent(requireActivity(),
					                                                     MainActivity.class));
					
					                            requireActivity().finishAffinity();
					
				                            });
			                } else {
				
				                progressBar.setVisibility(View.INVISIBLE);
				
				                Toast.makeText(requireContext(),
				                               setValueTask.getException().getMessage(),
				                               Toast.LENGTH_SHORT).show();
				
				                setValueTask.getException().printStackTrace();
			                }
		                });
		
		
	}
	
	
	private boolean validateFields ( ) {
		
		String name = nameEditText.getText().toString();
		String nameValidation = validateName(name);
		
		String email = emailEditText.getText().toString();
		String emailValidation = validateEmail(email);
		
		String password = passwordEditText.getText().toString();
		String confirmPassword = confirmPasswordEditText.getText().toString();
		
		String passwordValidation = validatePassword(password, confirmPassword);
		String confirmPasswordValidation = validateConfirmPassword(confirmPassword, password);
		
		String phoneNumber = phoneNumberEditText.getText().toString();
		String phoneNumberValidation = validatePhoneNumber(phoneNumber);
		
		String address = addressEditText.getText().toString();
		String addressValidation = validateAddress(address);
		
		if (nameValidation == null && emailValidation == null && passwordValidation == null && confirmPasswordValidation == null && phoneNumberValidation == null && addressValidation == null) {
			return true;
		}
		
		if (nameValidation != null) {
			nameTextInput.setError(nameValidation);
		}
		
		if (emailValidation != null) {
			emailTextInput.setError(emailValidation);
		}
		
		if (passwordValidation != null) {
			passwordTextInput.setError(passwordValidation);
		}
		
		if (confirmPasswordValidation != null) {
			confirmPasswordTextInput.setError(confirmPasswordValidation);
		}
		
		if (phoneNumberValidation != null) {
			phoneNumberTextInput.setError(phoneNumberValidation);
		}
		
		if (addressValidation != null) {
			addressTextInput.setError(addressValidation);
		}
		
		return false;
	}
	
	
	private String validateName (String name) {
		
		if (name.trim().length() == 0) {
			return "Name cannot be empty";
		} else if (name.trim().matches("^[0-9]+$")) {
			return "Name cannot have numbers in it";
		} else if (!name.trim().matches("^[a-zA-Z][a-zA-Z ]++$")) {
			return "Invalid Name";
		}
		return null;
	}
	
	
	private String validateEmail (String email) {
		
		if (email.trim().length() == 0) {
			return "Email cannot be empty";
		} else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
			return "Invalid Email";
		}
		return null;
	}
	
	
	private String validatePassword (String password, String confirmPassword) {
		
		if (password.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (confirmPassword.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (!confirmPassword.trim().matches(password.trim())) {
			return "Passwords do not match";
		} else if (password.trim().length() < 8) {
			return "Password too small. Minimum length is 8";
		} else if (password.trim().length() > 15) {
			return "Password too long. Maximum length is 15";
		} else if (password.trim().contains(" ")) {
			return "Password cannot contain spaces";
		}
		return null;
	}
	
	
	private String validateConfirmPassword (String confirmPassword, String password) {
		
		if (confirmPassword.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (password.trim().length() == 0) {
			return "Passwords cannot be empty";
		} else if (!confirmPassword.trim().matches(password.trim())) {
			return "Passwords do not match";
		} else if (password.trim().length() < 8) {
			return "Password too small. Minimum length is 8";
		} else if (password.trim().length() > 15) {
			return "Password too long. Maximum length is 15";
		} else if (password.trim().contains(" ")) {
			return "Password cannot contain spaces";
		}
		return null;
		
	}
	
	
	private String validatePhoneNumber (String phoneNumber) {
		
		if (phoneNumber.trim().length() == 0) {
			return "Phone Number cannot be empty";
		} else if (phoneNumber.length() != 10) {
			return "Phone Number should have 10 digits";
		} else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
			return "Invalid Phone Number";
		}
		return null;
	}
	
	
	private String validateAddress (String address) {
		
		if (address.trim().length() == 0) {
			return "Address cannot be empty";
		} else if (address.length() < 10) {
			return "Address is too small";
		} else if (address.length() > 50) {
			return "Address is too long";
		}
		return null;
	}
	
}