package com.example.v_pass;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class EntryLogActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EntryLogAdapter adapter;
    ArrayList<EntryLog> logList;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_log);

        // 1. Setup Toolbar and Navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbarLogs);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_logs);
        navigationView = findViewById(R.id.nav_view_logs);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 2. Sidebar Navigation Logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_guard_dash) {
                // Return to Guard Dashboard
                startActivity(new Intent(this, GuardActivity.class));
                finish();
            } else if (id == R.id.nav_guard_logs) {
                // Already on this page
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_guard_logout) {
                // Sign out
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 3. RecyclerView & Firebase Logic (Maintained)
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logList = new ArrayList<>();
        adapter = new EntryLogAdapter(logList);
        recyclerView.setAdapter(adapter);

        Query logQuery = FirebaseDatabase.getInstance()
                .getReference("entry_logs")
                .limitToLast(10);

        logQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                logList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    EntryLog log = data.getValue(EntryLog.class);
                    if (log != null) {
                        logList.add(0, log);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    // Close drawer when back button is pressed
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}