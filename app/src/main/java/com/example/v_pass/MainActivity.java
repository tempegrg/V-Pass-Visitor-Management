package com.example.v_pass;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private CardView cardVisitor, cardGuard;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private long backPressedTime; // For double-tap exit logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Setup Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Handle Back Button (Close drawer or Exit app)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Double tap to exit logic
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        finishAffinity(); // Close all activities and exit
                    } else {
                        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                    }
                    backPressedTime = System.currentTimeMillis();
                }
            }
        });

        // 4. Sidebar Menu Logic (Fixed Logout/Exit)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(this, SignUpActivity.class));
            } else if (id == R.id.nav_team) {
                startActivity(new Intent(this, TeamActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutUsActivity.class));
            } else if (id == R.id.nav_logout) {
                // THE FIX: This is the exit functionality
                Toast.makeText(this, "Exiting V-PASS", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 5. Portal Cards (Dashboard Navigation)
        cardVisitor = findViewById(R.id.cardVisitor);
        cardGuard = findViewById(R.id.cardGuard);

        cardVisitor.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        cardGuard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GuardLoginActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
}