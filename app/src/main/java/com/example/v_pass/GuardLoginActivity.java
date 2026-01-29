package com.example.v_pass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class GuardLoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etGuardEmail);
        etPassword = findViewById(R.id.etGuardPassword);
        btnLogin = findViewById(R.id.btnGuardLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("Authorizing...");

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        if (email.equalsIgnoreCase("admin@vpass.com")) {
                            startActivity(new Intent(this, GuardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Access Denied: Guard account required", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            resetButton();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        resetButton();
                    });
        });
    }

    private void resetButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("AUTHORIZE LOGIN");
    }
}