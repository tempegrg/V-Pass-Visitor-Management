package com.example.v_pass;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class GuardActivity extends AppCompatActivity {

    private MaterialCardView btnStartScan, btnViewLogs;
    private MaterialButton btnLogout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);

        // 1. Setup Toolbar & Drawer
        Toolbar toolbar = findViewById(R.id.toolbarGuard);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_guard);
        navigationView = findViewById(R.id.nav_view_guard);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 2. Sidebar Logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_guard_dash) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_guard_logs) {
                startActivity(new Intent(this, EntryLogActivity.class));
            } else if (id == R.id.nav_guard_logout) {
                performLogout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 3. Original Button Functionality (Untouched)
        btnStartScan = findViewById(R.id.btnStartScan);
        btnViewLogs = findViewById(R.id.btnViewLogs);
        btnLogout = findViewById(R.id.btnGuardLogout);

        btnStartScan.setOnClickListener(v -> startActivity(new Intent(this, GuardScanActivity.class)));
        btnViewLogs.setOnClickListener(v -> startActivity(new Intent(this, EntryLogActivity.class)));
        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}