package com.sisfo.firebaseloginsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView user;
    Button logOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) gotoLogin();

        user = findViewById(R.id.user_label);
        user.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName() + "!");

        // Log out button
        logOut = findViewById(R.id.log_out_button);
        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            gotoLogin();
        });

    }

    private void gotoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}