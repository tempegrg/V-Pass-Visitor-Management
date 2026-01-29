package com.example.v_pass;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvGoToRegister;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Setup Toolbar & Drawer
        Toolbar toolbar = findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_login);
        navigationView = findViewById(R.id.nav_view_login);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 2. Sidebar Navigation logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutUsActivity.class));
            } else if (id == R.id.nav_login) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 3. Login logic
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
                        Toast.makeText(this, "Welcome to V-PASS", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("LOGIN");
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        // Handle Back Button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}