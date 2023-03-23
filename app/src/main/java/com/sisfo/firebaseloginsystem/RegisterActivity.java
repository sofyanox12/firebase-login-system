package com.sisfo.firebaseloginsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    TextView toLogin;
    Button signUp;
    EditText fullName, email, password, confirmPassword;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (mAuth.getCurrentUser() != null) gotoHome();

        signUp = findViewById(R.id.sign_up);
        toLogin = findViewById(R.id.go_to_sign_in);
        toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        fullName = findViewById(R.id.full_name_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        confirmPassword = findViewById(R.id.confirm_password_input);

        signUp.setOnClickListener(v -> {
            String fullNameAuth = fullName.getText().toString();
            String emailAuth  = email.getText().toString();
            String passwordAuth = password.getText().toString();
            String confirmPasswordAuth = confirmPassword.getText().toString();
            registerAuth(fullNameAuth, emailAuth, passwordAuth, confirmPasswordAuth);
        });
    }

    private void registerAuth(String fullName, String email, String password, String confirmPassword) {

        if (fullName.isEmpty()) {
            this.fullName.setError("Your Full name is required!");
            return;

        } else if (email.isEmpty()) {
            this.email.setError("Email is required!");
            return;

        } else if (password.isEmpty()) {
            this.password.setError("Password is required!");
            return;

        } else if (confirmPassword.isEmpty()) {
            this.confirmPassword.setError("Confirm password is required!");
            return;

        } else if (!password.equals(confirmPassword)) {
            this.password.setError("Password doesn't match!");
            this.confirmPassword.setError("Password doesn't match!");
            return;
        }

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest userData = new UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(fullName)
                                .build();

                        assert user != null;
                        user.updateProfile(userData).addOnCompleteListener(auth -> {
                            if (auth.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                gotoHome();
                            }
                        });

                    } else
                        this.email.setError("We cannot fetch this one!");

                    progressDialog.dismiss();
                });
    }

    private void gotoHome() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}