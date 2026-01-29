package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("Logging in...");

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        // LOGIK BARU: Sesiapa yang login di sini adalah Visitor.
                        // Terus bawa ke Visitor Dashboard/Home (Contoh: MainActivityVisitor atau DashboardActivity)
                        Toast.makeText(this, "Welcome to V-PASS", Toast.LENGTH_SHORT).show();

                        // Gantikan 'RegisterActivity.class' dengan page utama Visitor awak nanti
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("LOGIN");
                        Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        tvGoToRegister.setOnClickListener(v -> {
            // Pergi ke page Sign Up untuk Visitor baru
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}