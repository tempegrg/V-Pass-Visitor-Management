package com.example.v_pass;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnVisitorEntry, btnGuardEntry;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup Toolbar (Required for the Hamburger icon)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Setup Drawer and Hamburger Toggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Setup Sidebar Menu Clicks
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Do nothing, already home
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            } else if (id == R.id.nav_about) {
                // This is what makes "About Us" work!
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            }

            drawerLayout.closeDrawers(); // Closes sidebar after clicking
            return true;
        });

        // 4. Portal Buttons Logic
        btnVisitorEntry = findViewById(R.id.btnVisitorEntry);
        btnGuardEntry = findViewById(R.id.btnGuardEntry);

        btnVisitorEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("user_role", "visitor");
            startActivity(intent);
        });

        btnGuardEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("user_role", "guard");
            startActivity(intent);
        });
    }

    // This makes the hamburger menu close if the user presses the physical back button
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}