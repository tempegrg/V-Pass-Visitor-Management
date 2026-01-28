package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvGoToRegister;
    FirebaseAuth mAuth;

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

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        // Ambil email terus dari authResult (lebih tepat)
                        String loggedInEmail = authResult.getUser().getEmail();
                        String role = getIntent().getStringExtra("user_role");

                        // --- FIX 2: REDIRECT LOGIC ---
                        if (loggedInEmail != null && (loggedInEmail.equals("admin@vpass.com") || "guard".equals(role))) {
                            // Hantar ke Guard Dashboard (GuardActivity), bukan terus ke scanner
                            startActivity(new Intent(LoginActivity.this, GuardActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        }
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // --- FIX 3: ADD FAILURE LISTENER ---
                        // Ini penting! Kalau login gagal, awak akan nampak SEBABnya (cth: akaun tak wujud)
                        Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}