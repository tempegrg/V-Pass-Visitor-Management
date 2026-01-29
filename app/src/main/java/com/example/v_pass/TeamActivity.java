package com.example.v_pass;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TeamActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTeam);
        setSupportActionBar(toolbar);

        // 2. Setup Drawer
        drawerLayout = findViewById(R.id.drawer_layout_team);
        navigationView = findViewById(R.id.nav_view_team);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Creative Animation Logic
        LinearLayout container = findViewById(R.id.teamContainer);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        LayoutAnimationController controller = new LayoutAnimationController(slideUp);
        controller.setDelay(0.2f); // Staggered appearance
        container.setLayoutAnimation(controller);

        // 4. Sidebar Menu Functionality
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Handle Navigation
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
            } else if (id == R.id.nav_team) {
                // Already here, just close drawer
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // Optional: Smooth fade out when leaving
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}