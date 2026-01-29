package com.example.v_pass;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView; // Correct Import
import com.google.firebase.auth.FirebaseAuth;

public class GuardActivity extends AppCompatActivity {

    // These MUST be CardViews to match your activity_guard.xml
    private MaterialCardView btnStartScan, btnViewLogs;
    private MaterialButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);

        // Matching IDs from your XML
        btnStartScan = findViewById(R.id.btnStartScan);
        btnViewLogs = findViewById(R.id.btnViewLogs);
        btnLogout = findViewById(R.id.btnGuardLogout);

        btnStartScan.setOnClickListener(v -> {
            startActivity(new Intent(GuardActivity.this, GuardScanActivity.class));
        });

        btnViewLogs.setOnClickListener(v -> {
            startActivity(new Intent(GuardActivity.this, EntryLogActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(GuardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}