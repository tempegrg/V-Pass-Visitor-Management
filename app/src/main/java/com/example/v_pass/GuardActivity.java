package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class GuardActivity extends AppCompatActivity {

    Button btnStartScan, btnViewLogs, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);

        btnStartScan = findViewById(R.id.btnStartScan);
        btnViewLogs = findViewById(R.id.btnViewLogs);
        btnLogout = findViewById(R.id.btnGuardLogout);

        // Navigate to QR Scanner
        btnStartScan.setOnClickListener(v -> {
            startActivity(new Intent(GuardActivity.this, GuardScanActivity.class));
        });

        // Navigate to Entry Logs
        btnViewLogs.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(GuardActivity.this, EntryLogActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                // Ini akan beritahu awak dalam skrin phone kenapa dia tak boleh buka
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("GUARD_ERROR", "Failed to open logs", e);
            }
        });

        // Logout and go back to Homepage
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(GuardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}