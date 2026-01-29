package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvGoToRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        // Get the role passed from MainActivity
        final String role = getIntent().getStringExtra("user_role");

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Disable button to prevent double-clicks
            btnLogin.setEnabled(false);
            btnLogin.setText("Logging in...");

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        String loggedInEmail = authResult.getUser().getEmail();

                        // Debug log to see what's happening in Logcat
                        Log.d("LOGIN_DEBUG", "Email: " + loggedInEmail + " | Role: " + role);

                        // REDIRECT LOGIC
                        // 1. Check if it's the admin email
                        // 2. OR check if the intent role is "guard"
                        if (loggedInEmail != null && (loggedInEmail.equals("admin@vpass.com") || "guard".equalsIgnoreCase(role))) {
                            startActivity(new Intent(LoginActivity.this, GuardActivity.class));
                        } else {
                            // Default for visitors
                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        }

                        finish(); // Close LoginActivity
                    })
                    .addOnFailureListener(e -> {
                        // Re-enable button on failure
                        btnLogin.setEnabled(true);
                        btnLogin.setText("LOGIN");
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}