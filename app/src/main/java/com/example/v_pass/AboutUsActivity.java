package com.example.v_pass;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class AboutUsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);

        // 2. Setup the Drawer (Sidebar)
        drawerLayout = findViewById(R.id.drawer_layout_about);
        navigationView = findViewById(R.id.nav_view_about);

        // Synchronize the sidebar icon with the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Navigation Sidebar Menu Logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Go back to Home and close this activity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
            } else if (id == R.id.nav_register) {
                // Pointing to Visitor Registration
                startActivity(new Intent(this, SignUpActivity.class));
            } else if (id == R.id.nav_about) {
                // Already here, just close
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Handles the physical back button
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}