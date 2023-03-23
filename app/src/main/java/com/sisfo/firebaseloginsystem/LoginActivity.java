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

public class LoginActivity extends AppCompatActivity {

    TextView toRegister;
    Button signIn;
    EditText email, password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (mAuth.getCurrentUser() != null) gotoHome();

        signIn = findViewById(R.id.sign_in);

        toRegister = findViewById(R.id.go_to_sign_up);
        toRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        signIn.setOnClickListener(v -> {
            String emailAuth = email.getText().toString();
            String passwordAuth = password.getText().toString();
            loginAuth(emailAuth, passwordAuth);
        });

    }

    private void loginAuth(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            this.email.setError("Email is required");
            this.password.setError("Password is required");
            return;
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            progressDialog.dismiss();

            if (task.isSuccessful()) {
                gotoHome();

            } else {
                // If email is not found
                if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    this.email.setError("User does not exist!");
                }
                // If password is incorrect
                if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                    this.password.setError("Password is incorrect!");
                }
            }
        });


    }

    private void gotoHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}