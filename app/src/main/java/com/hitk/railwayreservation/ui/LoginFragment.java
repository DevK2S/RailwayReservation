package com.hitk.railwayreservation.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hitk.railwayreservation.R;


public class LoginFragment extends Fragment {

    private static final String TAG ="Login Screen";
    private TextInputLayout emailTextInput, passwordTextInput;
    private TextInputEditText emailEditText, passwordEditText;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.progressBarLogin);
        emailTextInput = view.findViewById(R.id.txtInput_Email);
        emailEditText = view.findViewById(R.id.editTxt_Email);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordTextInput = view.findViewById(R.id.txtInput_Password);
        passwordEditText = view.findViewById(R.id.editTxt_Password);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.btn_createAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);

            }
        });
        view.findViewById(R.id.btn_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    login(view);
                }
            }
        });
    }
    public void login(View view)
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                });

    }
    private boolean validateFields() {
        Log.d(TAG, "validateFields: starts");
        String emailValidation = validateEmail(emailEditText.getText().toString());
        String passwordValidation = validatePassword(passwordEditText.getText().toString());

        if (emailValidation == null && passwordValidation == null) {
            Log.d(TAG, "validateFields() returned: " + true);
            return true;
        }

        if (emailValidation != null) {
            emailTextInput.setError(emailValidation);
        }

        if (passwordValidation != null) {
            passwordTextInput.setError(passwordValidation);
        }

        Log.d(TAG, "validateFields() returned: " + false);
        return false;
    }

    private String validateEmail(String email) {
        if (email.trim().length() == 0) {
            return "Email cannot be empty";
        }
        return null;
    }

    private String validatePassword(String password) {
        if (password.trim().length() == 0) {
            return "Password cannot be empty";
        }
        return null;
    }
}